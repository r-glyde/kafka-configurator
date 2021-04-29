import Aliases._
import BuildInfo._
import Release._
import Docker._

Global / onChangedBuildSource := ReloadOnSourceChanges

val kafkaVersion = "2.3.1"

val kafkaDeps = Seq(
  "org.apache.kafka" % "kafka-clients",
  "org.apache.kafka" %% "kafka"
).map(_ % kafkaVersion)

val dependencies = Seq(
  "com.github.scopt"           %% "scopt"               % "3.7.1",
  "org.zalando"                %% "grafter"             % "2.6.1",
  "com.typesafe.scala-logging" %% "scala-logging"       % "3.5.0",
  "io.circe"                   %% "circe-yaml"          % "0.12.0",
  "io.circe"                   %% "circe-generic"       % "0.12.3",
  "org.typelevel"              %% "cats-core"           % "1.5.0",
  "org.typelevel"              %% "cats-kernel"         % "1.5.0",
  "org.slf4j"                   % "log4j-over-slf4j"    % "1.7.25",
  "org.slf4j"                   % "slf4j-api"           % "1.7.25",
  "ch.qos.logback"              % "logback-classic"     % "1.2.3"   % Runtime,

  "org.scalatest"              %% "scalatest"                  % "3.0.5"      % Test,
  "net.cakesolutions"          %% "scala-kafka-client-testkit" % kafkaVersion % Test,
  "org.mockito"                 % "mockito-all"                % "1.10.19"    % Test
) ++ kafkaDeps

lazy val user = sys.env.getOrElse("GITHUB_USER", "NO")
lazy val token = sys.env.getOrElse("GITHUB_TOKEN", "STOP")

publishTo := Some("GitHub Package Registry (r-glyde/kafka-configurator)" at "https://maven.pkg.github.com/r-glyde/kafka-configurator")
credentials += Credentials("GitHub Package Registry", "maven.pkg.github.com", user, token)

val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin, JavaAppPackaging, UniversalDeployPlugin, DockerPlugin, AshScriptPlugin)
  .settings(
    defineCommandAliases,
    organization := "com.sky",
    scalaVersion := "2.12.10",
    name := "kafka-configurator",
    libraryDependencies ++= dependencies,
    resolvers += Resolver.bintrayRepo("cakesolutions", "maven"),
    scalacOptions += "-language:implicitConversions",
    fork in run := true,
    buildInfoSettings,
    releaseSettings,
    dockerSettings
  )
