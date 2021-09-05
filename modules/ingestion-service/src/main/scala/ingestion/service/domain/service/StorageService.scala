package ingestion.service.domain.service

import cats.effect._
import software.amazon.awssdk.services.s3.model.Bucket
import ingestion.service.adapter.s3.S3Client
import ingestion.service.domain.service.live.StorageServiceLive

trait StorageService[F[_]] {
  def createBucketIfNotExists(bucketName: String): F[Option[Bucket]]
}

object StorageService {

  def make[F[_]: Sync](s3Client: S3Client[F]): F[StorageServiceLive[F]] =
    Sync[F].delay(new StorageServiceLive[F](s3Client))
}
