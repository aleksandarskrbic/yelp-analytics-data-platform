import sbt._

object dependencies {

  object Versions {
    val spark      = "3.1.2"
    val hadoop     = "3.3.1"
    val frameless  = "0.10.1"
    val cats       = "2.6.1"
    val catsEffect = "2.5.3"
    val pureConfig = "0.16.0"
    val log4cats   = "1.3.1"
    val logback    = "1.2.5"
  }

  object Libs {
    def frameless(artifact: String): ModuleID = "org.typelevel"     %% artifact % Versions.frameless
    def hadoop(artifact: String): ModuleID    = "org.apache.hadoop" % artifact  % Versions.hadoop

    val spark = "org.apache.spark" %% "spark-core" % Versions.spark

    val hadoopCommon = hadoop("hadoop-common")
    val hadoopClient = hadoop("hadoop-client")
    val hadoopAws    = hadoop("hadoop-aws")

    val framelessCore    = frameless("frameless-core")
    val framelessCats    = frameless("frameless-cats")
    val framelessDataset = frameless("frameless-dataset")

    val cats       = "org.typelevel" %% "cats-core"   % Versions.cats
    val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect

    val pureConfig = Seq(
      "com.github.pureconfig" %% "pureconfig"              % Versions.pureConfig,
      "com.github.pureconfig" %% "pureconfig-cats-effect2" % Versions.pureConfig
    )

    val logback       = "ch.qos.logback" % "logback-classic" % Versions.logback % Runtime
    val log4cats      = "org.typelevel"  %% "log4cats-core"  % Versions.log4cats
    val log4catsSlf4j = "org.typelevel"  %% "log4cats-slf4j" % Versions.log4cats
  }
}
