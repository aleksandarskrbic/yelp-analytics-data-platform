import sbt._

object dependencies {

  object Versions {
    val spark      = "3.1.2"
    val frameless  = "0.10.1"
    val fs2        = "2.5.9"
    val cats       = "2.6.1"
    val catsEffect = "2.5.3"
    val fs2Aws     = "3.1.1"
    val http4s     = "0.22.4"
    val circe      = "0.14.1"
    val pureConfig = "0.16.0"
    val log4cats   = "1.3.1"
    val logback    = "1.2.5"
  }

  object Libs {
    def circe(artifact: String): ModuleID     = "io.circe"      %% artifact % Versions.circe
    def http4s(artifact: String): ModuleID    = "org.http4s"    %% artifact % Versions.http4s
    def frameless(artifact: String): ModuleID = "org.typelevel" %% artifact % Versions.frameless

    val spark = "org.apache.spark" %% "spark-core" % Versions.spark

    val framelessCore    = frameless("frameless-core")
    val framelessCats    = frameless("frameless-cats")
    val framelessDataset = frameless("frameless-dataset")

    val fs2        = "co.fs2"        %% "fs2-core"    % Versions.fs2
    val cats       = "org.typelevel" %% "cats-core"   % Versions.cats
    val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect

    val pureConfig = Seq(
      "com.github.pureconfig" %% "pureconfig"              % Versions.pureConfig,
      "com.github.pureconfig" %% "pureconfig-cats-effect2" % Versions.pureConfig
    )

    val circeCore    = circe("circe-core")
    val circeGeneric = circe("circe-generic")

    val http4sDsl    = http4s("http4s-dsl")
    val http4sServer = http4s("http4s-blaze-server")
    val http4sCirce  = http4s("http4s-circe")

    val fs2Aws = Seq(
      "io.laserdisc" %% "fs2-aws"      % Versions.fs2Aws,
      "io.laserdisc" %% "fs2-aws-core" % Versions.fs2Aws
    )

    val fs2AwsS3 = Seq(
      "io.laserdisc" %% "fs2-aws-s3"      % Versions.fs2Aws,
      "io.laserdisc" %% "pure-s3-tagless" % Versions.fs2Aws
    )

    val logback       = "ch.qos.logback" % "logback-classic" % Versions.logback % Runtime
    val log4cats      = "org.typelevel"  %% "log4cats-core"  % Versions.log4cats
    val log4catsSlf4j = "org.typelevel"  %% "log4cats-slf4j" % Versions.log4cats
  }
}
