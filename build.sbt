//Scala
val circeV = "0.14.1"
val enumeratumV = "1.7.0"
val http4sV = "0.23.7"
val http4sClientV = "0.5.0"
val doobieV = "1.0.0-RC1"
val flyWayV = "8.3.0"
val log4catsV = "2.1.1"
val logbackClassicV = "1.2.10"
val pureConfigV = "0.17.1"
//Test
val munitV = "0.7.29"
val testcontainersV = "0.39.12"
//Spark
val sparkV = "3.2.0"
val framelessV = "0.11.1"

//compiler plugins
val betterMonadicForV = "0.3.1"

lazy val commonSettings = Seq(
  Test / fork := true,
  scalaVersion := "2.13.7",
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForV),
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
        "org.typelevel" %% "frameless-dataset" % framelessV,
        "com.beachape" %% "enumeratum" % enumeratumV
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
        "org.typelevel" %% "log4cats-slf4j" % log4catsV,
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
  .settings(commonSettings)
  .aggregate(processor, server)
