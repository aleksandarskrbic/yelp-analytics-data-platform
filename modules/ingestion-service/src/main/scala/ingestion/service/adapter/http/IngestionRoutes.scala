package ingestion.service.adapter.http

import cats.effect._
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.multipart._
import org.http4s.dsl.Http4sDsl
import ingestion.service.domain.service.IngestionService
import ingestion.service.adapter.http.response.IngestionResponse
import ingestion.service.adapter.http.response.IngestionResponse._
import org.http4s.circe.CirceEntityEncoder._

final class IngestionRoutes[F[_]: Sync](ingestionService: IngestionService[F]) extends Http4sDsl[F] {
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "ingestion" =>
      req.decode[Multipart[F]](multipart =>
        ingestionService.load(multipart.parts) >> Ok(IngestionResponse("File ingestion started"))
      )
  }
}
