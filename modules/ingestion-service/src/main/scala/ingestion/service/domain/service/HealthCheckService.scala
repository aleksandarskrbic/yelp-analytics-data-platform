package ingestion.service.domain.service

trait HealthCheckService[F[_]] {
  def ingestionStatus(): F[Unit]
  def s3Status(): F[Unit]
}
