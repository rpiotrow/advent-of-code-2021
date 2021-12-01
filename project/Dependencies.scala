import sbt._

object Versions {
  val zio       = "2.0.0-M6-2"
}

object Dependencies {
  lazy val zio        = "dev.zio"          %% "zio"               % Versions.zio
  lazy val zioStreams = "dev.zio"          %% "zio-streams"       % Versions.zio
}

object TestDependencies {
  lazy val zioTest    = "dev.zio" %% "zio-test"          % Versions.zio % "test"
  lazy val zioTestSbt = "dev.zio" %% "zio-test-sbt"      % Versions.zio % "test"
}
