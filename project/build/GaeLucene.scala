import java.io.File
import sbt._
import Process._

class GaeLuceneProject(info:ProjectInfo) extends DefaultProject(info) {
  // locate the Home directory
  val userHome = system[File]("user.home")
  // define custom property
  val defaultGaeHome = userHome.value + "/Documents/src/gae/" + "appengine-java-sdk-1.3.3"
  val gaeHome = propertyOptional[String](defaultGaeHome)
  val gaePath = Path.fromFile(gaeHome.value)

  // Lucene
  val luceneCore = "org.apache.lucene" % "lucene-core" % "3.0.1"

  // Dependencies for testing
  val junit = "junit" % "junit" % "4.7" % "test->default"
  val specs = "org.scala-tools.testing" % "specs" % "1.6.1-2.8.0.Beta1-RC6" % "test->default"

  // App Engine paths
  val gaeSharedJars = gaePath / "lib" / "shared" * "*.jar" +++ gaePath / "lib" / "user" * "*.jar"
  val gaeTestingJars = gaePath / "lib" / "impl" * "*.jar" +++ gaePath / "lib" / "testing" * "*.jar"

  val jars = gaeSharedJars
  val testingJars = gaeTestingJars

  // override looking for jars in ./lib
  override def dependencyPath = "src" / "main" / "lib"
  // compile with App Engine jars
  override def compileClasspath = super.compileClasspath +++ jars
  // add App Engine jars to console classpath
  override def consoleClasspath = super.consoleClasspath +++ jars +++ testingJars
  // compile tests with App Engine jars
  override def testClasspath = super.testClasspath +++ jars +++ testingJars
  // override path to managed dependency cache
  override def managedDependencyPath = "project" / "lib_managed"
}
