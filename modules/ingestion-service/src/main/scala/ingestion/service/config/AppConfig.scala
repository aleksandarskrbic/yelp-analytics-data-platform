package ingestion.service.config

import cats.effect._
import pureconfig._
import pureconfig.generic.auto._
import pureconfig.module.catseffect2.syntax._

final case class AppConfig(server: Server, s3: S3)

object AppConfig {

  def load[F[_]: Sync: ContextShift]: F[AppConfig] =
    Blocker[F].use(blocker => ConfigSource.default.loadF[F, AppConfig](blocker))
}
