import sbt._

object dependencies {

  object Versions {
    val spark         = "3.1.2"
    val frameless     = "0.10.1"
    val fs2           = "3.1.1"
    val cats          = "2.6.1"
    val catsEffect2   = "2.5.3"
    val catsEffect3   = "3.2.5"
    val pureAws       = "0.6.0-RC3"
    val http4s        = "0.23.3"
    val circe         = "0.14.1"
    val pureConfigCE2 = "0.14.0"
    val pureConfigCE3 = "0.16.0"
  }

  object Libs {
    def circe(artifact: String): ModuleID     = "io.circe"           %% artifact % Versions.circe
    def http4s(artifact: String): ModuleID    = "org.http4s"         %% artifact % Versions.http4s
    def pureAws(artifact: String): ModuleID   = "com.rewardsnetwork" %% artifact % Versions.pureAws
    def frameless(artifact: String): ModuleID = "org.typelevel"      %% artifact % Versions.frameless

    val spark = "org.apache.spark" %% "spark-core" % Versions.spark

    val framelessCore    = frameless("frameless-core")
    val framelessCats    = frameless("frameless-cats")
    val framelessDataset = frameless("frameless-dataset")

    val fs2         = "co.fs2"        %% "fs2-core"    % Versions.fs2
    val cats        = "org.typelevel" %% "cats-core"   % Versions.cats
    val catsEffect2 = "org.typelevel" %% "cats-effect" % Versions.catsEffect2
    val catsEffect3 = "org.typelevel" %% "cats-effect" % Versions.catsEffect3

    val pureConfigCE2 = Seq(
      "com.github.pureconfig" %% "pureconfig"             % Versions.pureConfigCE2,
      "com.github.pureconfig" %% "pureconfig-cats-effect" % Versions.pureConfigCE2
    )

    val pureConfigCE3 = Seq(
      "com.github.pureconfig" %% "pureconfig"             % Versions.pureConfigCE3,
      "com.github.pureconfig" %% "pureconfig-cats-effect" % Versions.pureConfigCE3
    )

    val circeCore    = circe("circe-core")
    val circeGeneric = circe("circe-generic")

    val http4sDsl    = http4s("http4s-dsl")
    val http4sServer = http4s("http4s-blaze-server")
    val http4sCirce  = http4s("http4s-circe")

    val pureAwsS3        = pureAws("pure-aws-s3")
    val pureAwsS3Testing = pureAws("pure-aws-s3-testing")
  }
}
