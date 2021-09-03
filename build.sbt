import dependencies._

name := "yelp-analytics-data-platform"

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
      Libs.catsEffect2,
      Libs.framelessCore,
      Libs.framelessCats,
      Libs.framelessDataset
    ) ++ Libs.pureConfigCE2
  )

val `ingestion-service` = project
  .in(file("modules/ingestion-service"))
  .settings(scalaVersion := scalaCoreVersion)
  .settings(version := ingestionServiceVersion)
  .settings(
    libraryDependencies := Seq(
      Libs.fs2,
      Libs.cats,
      Libs.catsEffect3,
      Libs.http4sDsl,
      Libs.http4sServer,
      Libs.http4sCirce,
      Libs.pureAwsS3,
      Libs.pureAwsS3Testing
    ) ++ Libs.pureConfigCE3
  )

val `query-service` = project
  .in(file("modules/query-service"))
  .settings(scalaVersion := scalaCoreVersion)
  .settings(version := queryServiceVersion)
  .settings(
    libraryDependencies := Seq(
      Libs.cats,
      Libs.catsEffect3,
      Libs.http4sDsl,
      Libs.http4sServer,
      Libs.http4sCirce,
      Libs.pureAwsS3,
      Libs.pureAwsS3Testing
    ) ++ Libs.pureConfigCE3
  )
