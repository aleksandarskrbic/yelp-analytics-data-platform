package ingestion.service.domain.error

import scala.util.control.NoStackTrace

trait IngestionError extends NoStackTrace

object IngestionError {
  final case class UnableToGenerateFilename(errorMessage: String) extends IngestionError
}
