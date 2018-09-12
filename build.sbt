val `scala 212` = "2.12.6"

val sharedOptions = Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-explaintypes",
  "-Yrangepos",
  "-feature",
  "-Xfuture",
  "-Ypartial-unification",
  "-language:higherKinds",
  "-language:existentials",
  "-unchecked",
  "-Yno-adapted-args",
  "-Xlint:_,-type-parameter-shadow",
  "-Xsource:2.13",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfatal-warnings",
  "-opt:l:inline",
  "-Ywarn-unused:imports",
  "-Ywarn-unused:_,imports",
  "-opt-warnings",
  "-Xlint:constant",
  "-Ywarn-extra-implicit",
  "-opt-inline-from:<source>"
)

val versionOf = new {
  val catsEffect        = "1.0.0"
  val kafkaStreams      = "2.0.0"
  val confluent         = "5.0.0"
  val `logback-classic` = "1.2.3"
}

val root = project
  .in(file("."))
  .settings(
    name      := "test",
    resolvers += "Confluent" at "http://packages.confluent.io/maven/",
    libraryDependencies ++= Seq(
      "org.typelevel"    %% "cats-effect"             % versionOf.catsEffect,
      "org.apache.kafka" %% "kafka"                   % versionOf.kafkaStreams,
      "org.apache.kafka" % "kafka-streams"            % versionOf.kafkaStreams,
      "org.apache.kafka" %% "kafka-streams-scala"     % versionOf.kafkaStreams,
      "ch.qos.logback"   % "logback-classic"          % versionOf.`logback-classic`,
      "io.confluent"     % "kafka-streams-avro-serde" % versionOf.confluent,
    ),
    addCommandAlias("format", ";scalafmt;test:scalafmt;scalafmtSbt")
  )
