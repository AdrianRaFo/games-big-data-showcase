import sbt.Keys.scalaVersion

//Scala
val circeV = "0.14.1"
val enumeratumV = "1.6.0"
val http4sV = "0.21.24"
val http4sClientV = "0.3.7"
val doobieV = "0.13.4"
val flyWayV = "7.10.0"
val log4catsV = "1.1.1"
val logbackClassicV = "1.2.3"
val pureConfigV = "0.16.0"
//Test
val munitV = "0.7.26"
val testcontainersV = "0.39.5"
//Spark
val sparkV = "3.1.2"
val framelessV = "0.10.1"

//compiler plugins
val betterMonadicForV = "0.3.1"
val kindProjectorV = "0.13.0"

lazy val commonSettings = Seq(
  scalaVersion := "2.12.14",
  Test / fork := true,
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForV),
  addCompilerPlugin("org.typelevel" %% "kind-projector" % kindProjectorV cross CrossVersion.full),
  testFrameworks += new TestFramework("munit.Framework"),
  libraryDependencies ++= Seq(
    "org.scalameta" %% "munit" % munitV % Test,
    "com.dimafeng" %% "testcontainers-scala-postgresql" % testcontainersV % Test,
    "com.dimafeng" %% "testcontainers-scala-munit" % testcontainersV % Test
  )
)

lazy val processor =
  project
    .in(file("modules/processor"))
    .settings(name := "processor")
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        "org.apache.spark" %% "spark-sql" % sparkV,
        "org.typelevel" %% "frameless-dataset" % framelessV
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
        "com.beachape" %% "enumeratum" % enumeratumV,
        "com.beachape" %% "enumeratum-circe" % enumeratumV,
        "com.beachape" %% "enumeratum-doobie" % enumeratumV,
        "org.tpolecat" %% "doobie-core" % doobieV,
        "org.tpolecat" %% "doobie-hikari" % doobieV,
        "org.tpolecat" %% "doobie-postgres" % doobieV,
        "com.github.pureconfig" %% "pureconfig" % pureConfigV,
        "com.github.pureconfig" %% "pureconfig-http4s" % pureConfigV,
        "io.chrisdavenport" %% "log4cats-slf4j" % log4catsV,
        "org.flywaydb" % "flyway-core" % flyWayV,
        "ch.qos.logback" % "logback-classic" % logbackClassicV,
        "io.circe" %% "circe-literal" % circeV % Test,
        "org.tpolecat" %% "doobie-munit" % doobieV % Test
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
