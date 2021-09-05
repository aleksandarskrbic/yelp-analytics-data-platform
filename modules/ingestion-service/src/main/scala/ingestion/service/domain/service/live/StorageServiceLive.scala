package ingestion.service.domain.service.live

import cats.effect._
import cats.implicits._
import software.amazon.awssdk.services.s3.model.{ Bucket, CreateBucketRequest }
import ingestion.service.adapter.s3.S3Client
import ingestion.service.domain.service.StorageService
import scala.jdk.CollectionConverters._

final class StorageServiceLive[F[_]: Sync](s3Client: S3Client[F]) extends StorageService[F] {
  override def createBucketIfNotExists(bucketName: String): F[Option[Bucket]] =
    findByName(bucketName).flatMap {
      case Some(bucket) => Option(bucket).pure[F]
      case None =>
        s3Client.s3.createBucket(new CreateBucketRequest(bucketName)) >> findByName(bucketName)
    }

  private def findByName(bucketName: String): F[Option[Bucket]] =
    s3Client.s3.listBuckets.map(_.buckets.asScala.find(_.name == bucketName))
}
