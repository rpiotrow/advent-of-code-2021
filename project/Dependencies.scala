import sbt._

object Versions {
  val zio        = "2.0.0-M6-2"
  val scopt      = "4.0.1"
  val scodecBits = "1.1.30"
}

object Dependencies {
  lazy val zio        = "dev.zio"          %% "zio"               % Versions.zio
  lazy val zioStreams = "dev.zio"          %% "zio-streams"       % Versions.zio
  lazy val scopt      = "com.github.scopt" %% "scopt"             % Versions.scopt
  lazy val scodecBits = "org.scodec"       %% "scodec-bits"       % Versions.scodecBits

}

object TestDependencies {
  lazy val zioTest    = "dev.zio" %% "zio-test"          % Versions.zio % "test"
  lazy val zioTestSbt = "dev.zio" %% "zio-test-sbt"      % Versions.zio % "test"
}
