import de.johoop.jacoco4sbt.JacocoPlugin._
import de.johoop.jacoco4sbt._

// Play uses JUnit-Interface to run JUnit tests, which exposes a few options:
//   -q   Output for successful tests is swallowed, while failed tests is printed.
//   -a   Print Stacktrace for AssertionErrors. Without this, you don't know which assert* failed.
//
// See https://github.com/sbt/junit-interface for more options

val argument = Tests.Argument("-a")

// Useful for local builds
testOptions in Test             += argument   // sbt test
testOptions in IntegrationTest  += argument   // sbt it:test

