import dependencies.{ Libs, _ }

name := "yelp-analytics-data-platform"
fork := true

val scalaCoreVersion  = "2.13.6"
val scalaSparkVersion = "2.12.14"

val queryServiceVersion     = "0.1.0"
val batchProcessorVersion   = "0.1.0"
val ingestionServiceVersion = "0.1.0"

val `batch-processor` = project
  .in(file("modules/batch-processor"))
  .settings(scalaVersion := scalaSparkVersion)
  .settings(version := batchProcessorVersion)
  .settings(
    libraryDependencies := Seq(
      Libs.spark,
      Libs.cats,
      Libs.catsEffect,
      Libs.framelessCore,
      Libs.framelessCats,
      Libs.framelessDataset
    ) ++ Libs.pureConfig
  )

val `ingestion-service` = project
  .in(file("modules/ingestion-service"))
  .settings(scalaVersion := scalaCoreVersion)
  .settings(version := ingestionServiceVersion)
  .settings(
    libraryDependencies := Seq(
      Libs.fs2,
      Libs.cats,
      Libs.catsEffect,
      Libs.logback,
      Libs.log4cats,
      Libs.log4catsSlf4j,
      Libs.fuuid
    ) ++ Libs.circe ++ Libs.http4s ++ Libs.pureConfig ++ Libs.fs2Aws ++ Libs.fs2AwsS3
  )

val `query-service` = project
  .in(file("modules/query-service"))
  .settings(scalaVersion := scalaCoreVersion)
  .settings(version := queryServiceVersion)
  .settings(
    libraryDependencies := Seq(
      Libs.cats,
      Libs.catsEffect
    ) ++ Libs.circe ++ Libs.http4s ++ Libs.pureConfig ++ Libs.fs2Aws ++ Libs.fs2AwsS3
  )
