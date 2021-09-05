package ingestion.service

import cats.effect._
import cats.implicits._

object App2 extends IOApp {

  def program[F[_]: Concurrent]: F[Unit] =
    for {
      f1 <- Concurrent[F].start(Sync[F].delay(println(Thread.currentThread().getName)))
      f2 <- Concurrent[F].start(Sync[F].delay(println(Thread.currentThread().getName)))
    } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    program[IO].as(ExitCode.Success)
}
