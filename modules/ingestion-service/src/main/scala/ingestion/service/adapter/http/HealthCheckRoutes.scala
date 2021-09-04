package ingestion.service.adapter.http

import cats.MonadThrow
import org.http4s.dsl.Http4sDsl

final class HealthCheckRoutes[F[_]: MonadThrow] extends Http4sDsl[F] {}
