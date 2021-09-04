package ingestion.service.domain.service

import cats.implicits._
import cats.effect.Sync
import ingestion.service.adapter.s3.S3Client
import software.amazon.awssdk.services.s3.model.Bucket

import scala.jdk.CollectionConverters._

trait StorageService[F[_]] {
  def createBucketIfNotExists(bucket: String): F[Option[Bucket]]
}

object StorageService {

  def make[F[_]: Sync](s3Client: S3Client[F]): F[StorageServiceLive[F]] =
    Sync[F].delay(new StorageServiceLive[F](s3Client))
}

final class StorageServiceLive[F[_]: Sync](s3Client: S3Client[F]) extends StorageService[F] {
  override def createBucketIfNotExists(bucket: String): F[Option[Bucket]] =
    s3Client.s3.listBuckets.map(_.buckets.asScala.find(_.name == bucket))

}
