import sbt._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) {
  lazy val runMode = propertyOptional[String]("development")
  lazy val serverPort = propertyOptional[Int](8080)

  System.setProperty("run.mode", runMode.value)

  override def jettyPort = serverPort.value

  // uncomment the following if you want to use the snapshot repo
  val scalatoolsSnapshot = ScalaToolsSnapshots

  // If you're using JRebel for Lift development, uncomment
  // this line
  override def scanDirectories = Nil

  val liftVersion = "2.3-SNAPSHOT"
  override def libraryDependencies = Set(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-mongodb" % liftVersion % "compile->default",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "junit" % "junit" % "4.5" % "test->default",
    "ch.qos.logback" % "logback-classic" % "0.9.26",
    "org.scala-tools.testing" %% "specs" % "1.6.6" % "test->default"
  ) ++ super.libraryDependencies
}
