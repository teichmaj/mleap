package ml.combust.mleap

import sbt._
import Keys._
import com.jsuereth.sbtpgp.SbtPgp.autoImport._
import com.jsuereth.sbtpgp.PgpKeys._
import sbtrelease.ReleasePlugin.autoImport._
import xerial.sbt.Sonatype.autoImport._

object Common {
  lazy val defaultMleapSettings = defaultSettings ++ mleapSettings ++ sonatypeSettings
  lazy val defaultBundleSettings = defaultSettings ++ bundleSettings ++ sonatypeSettings
  lazy val defaultMleapXgboostSparkSettings = defaultMleapSettings ++ sonatypeSettings
  lazy val defaultMleapServingSettings = defaultMleapSettings ++ noPublishSettings


  lazy val defaultSettings = buildSettings ++ sonatypeSettings

  lazy val buildSettings: Seq[Def.Setting[_]] = Seq(
    scalaVersion := "2.13.11",
    crossScalaVersions := Seq("2.12.13", "2.13.11"),
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
    ThisBuild / libraryDependencySchemes +=
      "org.scala-lang.modules" %% "scala-collection-compat" % VersionScheme.Always,
    resolvers += Resolver.mavenLocal,
    resolvers += Resolver.jcenterRepo,
    resolvers ++= {
      // Only add Sonatype Snapshots if this version itself is a snapshot version
      if (isSnapshot.value) {
        Resolver.sonatypeOssRepos("snapshots") :+
          ("ASF Snapshots" at "https://repository.apache.org/content/groups/snapshots")
      } else {
        Seq()
      }
    },
    resolvers += "XGBoost4J Snapshot Repo" at "https://s3-us-west-2.amazonaws.com/xgboost-maven-repo/snapshot/"
  )

  lazy val mleapSettings: Seq[Def.Setting[_]] = Seq(organization := "ml.combust.mleap")
  lazy val bundleSettings: Seq[Def.Setting[_]] = Seq(organization := "ml.combust.bundle")

  lazy val noPublishSettings: Seq[Def.Setting[_]] = Seq(
    publishSigned / publishTo := None,
    publishTo := None
  )

  lazy val sonatypeSettings: Seq[Def.Setting[_]] = Seq(
    sonatypeProfileName := "ml.combust",
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    publishMavenStyle := true,
    publishTo := Some({
      if (isSnapshot.value) {
        Opts.resolver.sonatypeOssSnapshots.head
      } else {
        Opts.resolver.sonatypeStaging
      }
    }),
    Test / publishArtifact := false,
    pomIncludeRepository := { _ => false },
    licenses := Seq("Apache 2.0 License" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")),
    homepage := Some(url("https://github.com/combust/mleap")),
    scmInfo := Some(ScmInfo(url("https://github.com/combust/mleap.git"),
      "scm:git:git@github.com:combust/mleap.git")),
    developers := List(Developer("hollinwilkins",
      "Hollin Wilkins",
      "hollinrwilkins@gmail.com",
      url("http://hollinwilkins.com")),
      Developer("seme0021",
        "Mikhail Semeniuk",
        "mikhail@combust.ml",
        url("https://www.linkedin.com/in/semeniuk")),
      Developer("ancasarb",
        "Anca Sarb",
        "sarb.anca@gmail.com",
        url("https://www.linkedin.com/in/anca-sarb")))
  )
}
