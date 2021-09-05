package ingestion.service.adapter.http

import cats.effect._
import cats.implicits._
import org.http4s.multipart._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import ingestion.service.domain.service.StorageService

final class StorageRoutes[F[_]: Sync](storageService: StorageService[F]) extends Http4sDsl[F] {}
