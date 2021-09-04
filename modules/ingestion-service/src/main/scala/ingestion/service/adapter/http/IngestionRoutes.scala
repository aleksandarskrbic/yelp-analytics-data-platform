package ingestion.service.adapter.http

import cats.effect._
import cats.implicits._
import ingestion.service.domain.service.StorageService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.multipart._

final class IngestionRoutes[F[_]: Sync](storageService: StorageService[F]) extends Http4sDsl[F] {

  val routes = HttpRoutes.of[F] {
    case GET -> Root / "ingestion" / bucketName =>
      storageService.createBucketIfNotExists(bucketName).flatMap {
        case Some(bucket) => Ok(bucket.name)
        case None         => Ok()
      }

    case req @ POST -> Root / "multipart" =>
      req.decode[Multipart[F]] { multipart =>
        val a = multipart.parts
        println(a)
        Ok()
      }
  }
}
