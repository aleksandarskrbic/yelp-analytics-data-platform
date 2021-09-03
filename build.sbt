import Dependencies._

name := "yelp-analytics-data-platform"

val scalaCoreVersion  = "2.13.6"
val scalaSparkVersion = "2.12.14"

val queryServiceVersion     = "0.0.1"
val batchProcessorVersion   = "0.0.1"
val ingestionServiceVersion = "0.0.1"

val `batch-processor` = project
  .in(file("modules/batch-processor"))
  .settings(scalaVersion := scalaSparkVersion)
  .settings(version := batchProcessorVersion)
  .settings(
    libraryDependencies := Seq(Libs.spark)
  )

val `ingestion-service` = project
  .in(file("modules/ingestion-service"))
  .settings(scalaVersion := scalaCoreVersion)
  .settings(version := ingestionServiceVersion)
  .settings(
    libraryDependencies := Seq()
  )

val `query-service` = project
  .in(file("modules/query-service"))
  .settings(scalaVersion := scalaCoreVersion)
  .settings(version := queryServiceVersion)
  .settings(
    libraryDependencies := Seq()
  )
