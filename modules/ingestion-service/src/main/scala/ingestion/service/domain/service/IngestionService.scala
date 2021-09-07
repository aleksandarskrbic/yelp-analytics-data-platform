package ingestion.service.domain.service

import cats.effect._
import cats.implicits._
import fs2.aws.s3.{ BucketName, FileKey, S3 }
import org.http4s.multipart.Part
import io.chrisdavenport.fuuid.FUUID
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import org.typelevel.log4cats.{ Logger, SelfAwareStructuredLogger }
import org.typelevel.log4cats.slf4j.Slf4jLogger
import ingestion.service.adapter.s3.S3ClientWrapper
import ingestion.service.config
import ingestion.service.domain.error.IngestionError.UnableToGenerateFilename

import scala.concurrent.duration.MILLISECONDS

trait IngestionService[F[_]] {
  def load(parts: Vector[Part[F]]): F[Fiber[F, Unit]]
}

object IngestionService {
  implicit private def logger[F[_]: Sync]: SelfAwareStructuredLogger[F] = Slf4jLogger.getLogger[F]

  def make[F[_]: Concurrent: ContextShift: Clock](
    storageService: StorageService[F],
    s3Client: S3ClientWrapper[F],
    s3Config: config.S3
  ): F[IngestionServiceLive[F]] =
    Sync[F].delay(new IngestionServiceLive[F](storageService, s3Client, s3Config))
}

final class IngestionServiceLive[F[_]: Concurrent: ContextShift: Clock: Logger](
  storageService: StorageService[F],
  s3ClientWrapper: S3ClientWrapper[F],
  s3Config: config.S3
) extends IngestionService[F] {
  override def load(parts: Vector[Part[F]]): F[Fiber[F, Unit]] =
    S3.create[F](s3ClientWrapper.s3, s3ClientWrapper.blocker).flatMap { s3 =>
      parts.headOption
        .flatMap(_.filename)
        .fold(FUUID.randomFUUID[F].map(_.toString))(_.pure[F])
        .map { name =>
          for {
            filename   <- NonEmptyString.from(name)
            bucketName <- NonEmptyString.from(s3Config.bucketName)
          } yield (FileKey(filename), BucketName(bucketName))
        }
        .flatMap {
          case Left(error) =>
            Logger[F]
              .error("Unable to generate filename")
              .flatMap(_ => Sync[F].raiseError(UnableToGenerateFilename(error)))
          case Right((fileKey, bucketName)) =>
            for {
              _ <- storageService.createBucketIfNotExists(bucketName.value)
              _ <- Logger[F].info(s"File ${fileKey.value} upload to ${bucketName.value} started")
              f <- Concurrent[F].start {
                    for {
                      start <- Clock[F].monotonic(MILLISECONDS)
                      _ <- parts
                            .map(_.body)
                            .reduce(_ ++ _)
                            .through(s3.uploadFileMultipart(bucketName, fileKey, 10))
                            .compile
                            .drain
                      end   <- Clock[F].monotonic(MILLISECONDS)
                      total <- Sync[F].delay((end - start) / 1000)
                      _ <- Logger[F].info(
                            s"File ${fileKey.value} successfully uploaded to ${bucketName.value} bucket in ${total}s"
                          )
                    } yield ()
                  }.onError(_ => Logger[F].error(s"Unable to upload ${fileKey.value} to ${bucketName.value} bucket"))
            } yield f
        }
    }
}
