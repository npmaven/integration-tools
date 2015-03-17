package org.npmaven
package it

import org.specs2.mutable.Specification

object PackageSpecs extends Specification {
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
  }
}
