package ingestion.service

import cats.effect._
import cats.implicits._
import scala.concurrent.duration._
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.http4s.blaze.server.BlazeServerBuilder
import org.typelevel.log4cats.SelfAwareStructuredLogger
import ingestion.service.config.AppConfig
import ingestion.service.adapter.s3.S3ClientResource
import ingestion.service.adapter.http.IngestionRoutes
import ingestion.service.domain.service.{ IngestionService, StorageService }

object Main extends IOApp {
  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  def run(args: List[String]): IO[ExitCode] =
    AppConfig.load[IO].flatMap { appConfig =>
      val s3Config = appConfig.s3
      S3ClientResource.make[IO](s3Config).use { s3Client =>
        for {
          storageService   <- StorageService.make[IO](s3Client)
          ingestionService <- IngestionService.make[IO](storageService, s3Client, s3Config)

          _ <- storageService
                .createBucketIfNotExists(appConfig.s3.bucketName)
                .timeoutTo(s3Config.timeout, logAndShutdown)
                .start
          _ <- startHttpServer(appConfig.server, new IngestionRoutes[IO](ingestionService))
        } yield ExitCode.Success
      }
    }

  private def startHttpServer(server: config.Server, routes: IngestionRoutes[IO]) =
    BlazeServerBuilder[IO](
      ExecutionContext.fromExecutor(
        Executors.newFixedThreadPool(server.threads)
      )
    ).bindHttp(server.port, server.host)
      .withHttpApp(routes.routes.orNotFound)
      .withResponseHeaderTimeout(5.minutes)
      .serve
      .compile
      .drain

  private def logAndShutdown: IO[Unit] =
    logger.error("Unable to connect to S3 storage, shutting down application") >> IO(System.exit(1))
}
