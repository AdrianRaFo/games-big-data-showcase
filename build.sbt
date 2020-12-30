import sbt.Keys.scalaVersion

//Scala
val circeV = "0.13.0"
val http4sV = "0.21.14"
val http4sClientV = "0.3.2"
val log4catsV = "1.1.1"
val logbackClassicV = "1.2.3"
val pureConfigV = "0.14.0"
//Test
val testcontainersV = "0.38.8"
//Spark
val sparkV = "3.0.1"
val framelessV = "0.9.0"
//compiler plugins
val betterMonadicForV = "0.3.1"
val kindProjectorV = "0.11.2"

lazy val commonSettings = Seq(
  scalaVersion := "2.12.12",
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForV),
  addCompilerPlugin("org.typelevel" %% "kind-projector" % kindProjectorV cross CrossVersion.full)
)

lazy val processor =
  project
    .in(file("modules/processor"))
    .settings(name := "processor")
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        "org.apache.spark" %% "spark-sql" % sparkV,
        "org.typelevel" %% "frameless-dataset" % framelessV,
        "com.dimafeng" %% "testcontainers-scala-postgresql" % testcontainersV % Test,
        "com.dimafeng" %% "testcontainers-scala-munit" % testcontainersV % Test
      )
    )

lazy val server =
  project
    .in(file("modules/server"))
    .settings(name := "server")
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        "org.http4s" %% "http4s-dsl" % http4sV,
        "org.http4s" %% "http4s-blaze-server" % http4sV,
        "org.http4s" %% "http4s-jdk-http-client" % http4sClientV,
        "org.http4s" %% "http4s-circe" % http4sV,
        "io.circe" %% "circe-generic" % circeV,
        "io.circe" %% "circe-parser" % circeV,
        "com.github.pureconfig" %% "pureconfig" % pureConfigV,
        "com.github.pureconfig" %% "pureconfig-http4s" % pureConfigV,
        "io.chrisdavenport" %% "log4cats-slf4j" % log4catsV,
        "ch.qos.logback" % "logback-classic" % logbackClassicV
      )
    )
    .dependsOn(processor)

lazy val root = project
  .in(file("."))
  .settings(
    name := "games big data showcase",
    version := "0.1"
  )
  .aggregate(processor, server)
