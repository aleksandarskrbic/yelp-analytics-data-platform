package ingestion.service

import cats.effect._
import org.http4s.blaze.server.BlazeServerBuilder
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.SelfAwareStructuredLogger
import ingestion.service.config.AppConfig
import ingestion.service.adapter.http.IngestionRoutes
import ingestion.service.adapter.s3.S3ClientResource
import ingestion.service.domain.service.{ IngestionService, StorageService }
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object Main extends IOApp {

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  def run(args: List[String]): IO[ExitCode] =
    AppConfig.load[IO].flatMap { appConfig =>
      S3ClientResource.make[IO](appConfig.s3).use { s3 =>
        for {
          storageService   <- StorageService.make[IO](s3)
          ingestionService <- IngestionService.make[IO](s3)

          _ <- storageService.createBucketIfNotExists(appConfig.s3.bucketName).start

          _ <- BlazeServerBuilder[IO](
                ExecutionContext.fromExecutor(
                  Executors.newFixedThreadPool(4)
                )
              ).bindHttp(8080, "localhost")
                .withHttpApp(new IngestionRoutes[IO](ingestionService).routes.orNotFound)
                .serve
                .compile
                .drain
        } yield ExitCode.Success
      }
    }

}
