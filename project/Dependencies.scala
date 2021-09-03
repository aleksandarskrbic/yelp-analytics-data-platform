import sbt._

object Dependencies {

  object Versions {
    val spark = "3.1.2"
  }

  object Libs {
    val spark = "org.apache.spark" %% "spark-core" % Versions.spark
  }
}
