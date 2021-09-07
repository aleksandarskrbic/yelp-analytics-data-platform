package ingestion.service.adapter.http.response

import io.circe._
import io.circe.generic.semiauto._

final case class IngestionResponse(message: String)

object IngestionResponse {
  implicit val encoder: Encoder[IngestionResponse] = deriveEncoder
}
