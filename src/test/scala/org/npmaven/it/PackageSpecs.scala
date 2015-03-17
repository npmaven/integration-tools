package org.npmaven
package it

import java.io.{FileOutputStream, PrintStream}

import org.specs2.mutable.Specification

import scala.io.Source

object PackageSpecs extends Specification {
  def rsrc(name:String):String = {
    val r = this.getClass.getClassLoader.getResourceAsStream(name)
    Source.fromInputStream(r, "utf-8").mkString
  }

  // TODO: Could make this test suite take parameters and test against multiple packages
  "Package class" should {
    val angular = new Package("angular")

    "have a name getter" in {
      angular.getName must be equalTo("angular")
    }

    "have a resource root getter" in {
      angular.getResourceRoot must be equalTo("META-INF/resources/org/npmaven/angular/")
    }

    "get the version" in {
      angular.getVersion must be equalTo("1.3.14")
    }

    "get the mainBowerName" in {
      angular.getMainBowerName must be equalTo("angular-1.3.14.js")
    }

    "get the mainBowerNameMin" in {
      angular.getMainBowerNameMin must be equalTo("angular-1.3.14.min.js")
    }

    "get the mainBowerNameMap" in {
      angular.getMainBowerNameMap must be equalTo("angular-1.3.14.min.js.map")
    }

    "get the mainBowerString" in {
      angular.getMainBowerString must be equalTo(rsrc("META-INF/resources/org/npmaven/angular/angular.js"))
    }

    "get the mainBowerStringMin" in {
      angular.getMainBowerStringMin must be equalTo(rsrc("META-INF/resources/org/npmaven/angular/angular.min.js"))
    }

    "get the mainBowerStringMap" in {
      angular.getMainBowerStringMap must be equalTo(rsrc("META-INF/resources/org/npmaven/angular/angular.min.js.map"))
    }

    "get the mainBowerStringMap with substitutions" in {
      angular.getMainBowerStringMap("angular-1.3.14.min.js", "angular-1.3.14.js") must be equalTo(rsrc("org/npmaven/artifacts/angular-1.3.14-with-source.min.js.map"))
    }

    "get the mainBowerStringMapWithVersions" in {
      angular.mainBowerStringMapWithVersions must be equalTo(rsrc("org/npmaven/artifacts/angular-1.3.14-with-source.min.js.map"))
    }
  }
}
