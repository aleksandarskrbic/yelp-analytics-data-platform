package ingestion.service

import scala.concurrent.duration.FiniteDuration

package object config {
  final case class Server(host: String, port: Int, threads: Int)
  final case class S3(
    url: String,
    accessKeyId: String,
    secretAccessKey: String,
    region: String,
    bucketName: String,
    timeout: FiniteDuration
  )
}
