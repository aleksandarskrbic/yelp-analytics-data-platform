import sbt._

object dependencies {

  object Versions {
    val spark = "3.1.2"
  }

  object Libs {
    val spark = "org.apache.spark" %% "spark-core" % Versions.spark
  }
}
