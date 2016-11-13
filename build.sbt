name := """letgo-twitter"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .configs(IntegrationTest)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  javaWs,
  "org.twitter4j"       % "twitter4j-async" % "4.0.5",
  "org.mockito"         % "mockito-core"    % "1.10.19" % "it,test",
  "junit"               % "junit"           % "4.12"    % "it",
  "com.typesafe.play"   %% "play-test"      % play.core.PlayVersion.current % "it"
)

resolvers ++= Seq(
  Resolver.mavenLocal,
  "Maven Repository"  at "http://repo1.maven.org/",
  "Maven Central"     at "http://central.maven.org/maven2/"
)

unmanagedSourceDirectories  in Test <+= baseDirectory(_ / "tests" / "shared")
sourceDirectories           in Test <+= baseDirectory(_ / "tests" / "unit")
javaSource                  in Test <<= baseDirectory(_ / "tests" / "unit")
resourceDirectory           in Test <<= baseDirectory(_ / "tests" / "resources")
resourceDirectories         in Test <+= baseDirectory(_ / "tests" / "resources")

Defaults.itSettings
unmanagedSourceDirectories in IntegrationTest <+= baseDirectory(_ / "tests" / "shared")
sourceDirectories          in IntegrationTest <+= baseDirectory(_ / "tests" / "integration")
javaSource                 in IntegrationTest <<= baseDirectory(_ / "tests" / "integration")
resourceDirectory          in IntegrationTest <<= baseDirectory(_ / "tests" / "resources")
resourceDirectories        in IntegrationTest <+= baseDirectory(_ / "tests" / "resources")
