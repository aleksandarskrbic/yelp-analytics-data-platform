package ingestion.service.adapter.s3

import cats.effect._
import io.laserdisc.pure.s3.tagless.{ Interpreter, S3AsyncClientOp }
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.auth.credentials.{ AwsBasicCredentials, StaticCredentialsProvider }
import ingestion.service.config

import java.net.URI

final case class S3Client[F[_]](s3: S3AsyncClientOp[F], blocker: Blocker)

object S3ClientResource {

  def make[F[_]: Async: ContextShift](s3Config: config.S3): Resource[F, S3Client[F]] =
    for {
      blocker             <- Blocker[F]
      credentials         = AwsBasicCredentials.create(s3Config.accessKeyId, s3Config.secretAccessKey)
      credentialsProvider = StaticCredentialsProvider.create(credentials)
      s3 <- Interpreter[F](blocker).S3AsyncClientOpResource(
             S3AsyncClient
               .builder()
               .credentialsProvider(credentialsProvider)
               .endpointOverride(URI.create(s3Config.url))
               .region(Region.of(s3Config.region))
           )
    } yield S3Client(s3, blocker)
}