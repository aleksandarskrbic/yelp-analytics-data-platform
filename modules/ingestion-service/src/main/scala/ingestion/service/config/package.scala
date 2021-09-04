package ingestion.service

package object config {
  final case class Server(host: String, port: Int, threads: Int)
  final case class S3(url: String, accessKeyId: String, secretAccessKey: String, region: String)
}
