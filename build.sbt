name := "npmaven-integration-tools"

version := "0.1.0-SNAPSHOT"

organization := "org.npmaven"

scalaVersion := "2.11.6"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-deprecation", "-unchecked")

scalacOptions in Test ++= Seq("-Yrangepos") // Recommended for specs2 3.0

javacOptions in (Compile,compile) ++= Seq("-source", "1.6", "-target", "1.6", "-g")

resolvers ++= Seq(
  "snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "releases"  at "https://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.0" % "test"
)

homepage := Some(url("http://docs.npmaven.org"))

publishTo <<= version { _.endsWith("SNAPSHOT") match {
    case true  => Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
    case false => Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
  }
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
         <scm>
            <url>git@github.com:npmaven/integration-tools.git</url>
            <connection>scm:git:git@github.com:npmaven/integration-tools.git</connection>
         </scm>
         <developers>
            <developer>
              <id>joescii</id>
              <name>Joe Barnes</name>
              <url>https://github.com/joescii</url>
            </developer>
         </developers>
)

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

// OSGi stuff TODO
// osgiSettings
// 
// OsgiKeys.bundleSymbolicName := "com.joescii.j2jsi18n"
// 
// OsgiKeys.exportPackage := Seq("com.joescii.j2jsi18n")
// 
// OsgiKeys.importPackage := Seq()
// 
// OsgiKeys.privatePackage := Seq()
// 
// OsgiKeys.bundleActivator := None
