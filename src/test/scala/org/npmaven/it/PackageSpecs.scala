package org.npmaven
package it

import org.specs2.mutable.Specification

import scala.io.Source

object PackageSpecs extends Specification {
  def rsrc(name:String):String = {
    val r = this.getClass.getClassLoader.getResourceAsStream(name)
    Source.fromInputStream(r, "utf-8").mkString
  }

  "Package class" should {
    val angular = new Package("angular")

    "have a name constructor/getter" in {
      val name = angular.getName
      name must be equalTo("angular")
    }

    "get the angular version" in {
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
  }
}
