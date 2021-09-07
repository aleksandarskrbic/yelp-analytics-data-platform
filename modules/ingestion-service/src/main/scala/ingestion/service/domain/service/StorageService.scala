package ingestion.service.domain.service

import cats.effect._
import cats.implicits._
import scala.jdk.CollectionConverters._
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.{ Logger, SelfAwareStructuredLogger }
import software.amazon.awssdk.services.s3.model.{ Bucket, CreateBucketRequest, ListObjectsRequest, S3Object }
import ingestion.service.adapter.s3.S3ClientWrapper

trait StorageService[F[_]] {
  def createBucketIfNotExists(bucketName: String): F[Unit]
  def listFiles(bucketName: String): F[List[S3Object]]
}

object StorageService {
  implicit private def logger[F[_]: Sync]: SelfAwareStructuredLogger[F] = Slf4jLogger.getLogger[F]

  def make[F[_]: Sync](s3Client: S3ClientWrapper[F]): F[StorageServiceLive[F]] =
    Sync[F].delay(new StorageServiceLive[F](s3Client))
}

final class StorageServiceLive[F[_]: Sync: Logger](s3Client: S3ClientWrapper[F]) extends StorageService[F] {
  override def createBucketIfNotExists(bucketName: String): F[Unit] =
    findByName(bucketName).flatMap {
      case Some(_) => Logger[F].info(s"Bucket $bucketName already exists") >> Sync[F].unit
      case None =>
        for {
          _ <- Logger[F].info(s"Creating bucket $bucketName")
          _ <- s3Client.s3.createBucket(CreateBucketRequest.builder.bucket(bucketName).build())
        } yield ()
    }

  override def listFiles(bucketName: String): F[List[S3Object]] =
    s3Client.s3.listObjects(ListObjectsRequest.builder().bucket(bucketName).build()).map(_.contents.asScala.toList)

  private def findByName(bucketName: String): F[Option[Bucket]] =
    s3Client.s3.listBuckets.map(_.buckets.asScala.find(_.name == bucketName))
}
