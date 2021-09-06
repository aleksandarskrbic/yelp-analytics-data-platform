package ingestion.service.adapter.http

import cats.effect._
import cats.implicits._
import org.http4s.multipart._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

final class HealthCheckRoutes[F[_]: Sync] extends Http4sDsl[F] {
  val routes: HttpRoutes[F] = HttpRoutes.of[F](???)
}
