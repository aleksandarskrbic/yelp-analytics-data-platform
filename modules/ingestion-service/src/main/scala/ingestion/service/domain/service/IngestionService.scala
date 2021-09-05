package ingestion.service.domain.service

import cats.effect._
import cats.implicits._
import fs2.aws.s3.{ BucketName, FileKey, S3 }
import org.http4s.multipart.Part
import io.chrisdavenport.fuuid.FUUID
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import ingestion.service.adapter.s3.S3Client
import org.typelevel.log4cats.{ Logger, SelfAwareStructuredLogger }
import org.typelevel.log4cats.slf4j.Slf4jLogger

trait IngestionService[F[_]] {
  def load(parts: Vector[Part[F]]): F[Fiber[F, Unit]]
}

object IngestionService {
  implicit private def logger[F[_]: Sync]: SelfAwareStructuredLogger[F] = Slf4jLogger.getLogger[F]

  def make[F[_]: Concurrent: ContextShift](s3Client: S3Client[F]): F[IngestionServiceLive[F]] =
    Sync[F].delay(new IngestionServiceLive[F](s3Client))
}

final class IngestionServiceLive[F[_]: Concurrent: ContextShift: Logger](s3Client: S3Client[F])
    extends IngestionService[F] {
  override def load(parts: Vector[Part[F]]): F[Fiber[F, Unit]] =
    S3.create[F](s3Client.s3, s3Client.blocker).flatMap { s3 =>
      parts.headOption
        .flatMap(_.filename)
        .fold(FUUID.randomFUUID[F].map(_.toString))(_.pure[F])
        .map { name =>
          for {
            filename   <- NonEmptyString.from(name)
            bucketName <- NonEmptyString.from(s3Client.s3Config.bucketName)
          } yield (FileKey(filename), BucketName(bucketName))
        }
        .flatMap {
          case Left(error) =>
            Logger[F].error("Unable to generate filename").flatMap(_ => Sync[F].raiseError(new RuntimeException(error)))
          case Right((fileKey, bucketName)) =>
            Logger[F].info(s"File ${fileKey.value} upload to ${bucketName.value} started") >>
              Concurrent[F].start {
                parts
                  .map(_.body)
                  .reduce(_ ++ _)
                  .through(s3.uploadFileMultipart(bucketName, fileKey, 10))
                  .compile
                  .drain >> Logger[F].info(s"File ${fileKey.value} successfully uploaded to ${bucketName.value} bucket")
              }.onError(_ => Logger[F].error(s"Unable to upload ${fileKey.value} to ${bucketName.value} bucket"))
        }
    }
}
