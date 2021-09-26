import dependencies.{ Libs, _ }

name := "yelp-analytics-data-platform"
fork := true

val scala21214            = "2.12.14"
val batchProcessorVersion = "0.1.0"

val `analytics-service` = project
  .in(file("modules/analytics-service"))
  .settings(scalaVersion := scala21214)
  .settings(version := batchProcessorVersion)
  .settings(
    libraryDependencies := Seq(
      Libs.spark,
      Libs.cats,
      Libs.catsEffect,
      Libs.framelessCore,
      Libs.framelessCats,
      Libs.framelessDataset,
      Libs.hadoopCommon,
      Libs.hadoopClient,
      Libs.hadoopAws
    ) ++ Libs.pureConfig
  )
