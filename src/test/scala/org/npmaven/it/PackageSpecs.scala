package org.npmaven
package it

import java.io.{FileOutputStream, PrintStream}

import org.specs2.mutable.Specification

import scala.io.Source

object PackageSpecs extends Specification with Fixtures {
  // TODO: Could make this test suite take parameters and test against multiple packages
  "Package class" should {
    "have a name getter" in {
      angular.getName must be equalTo("angular")
    }

    "have a resource root getter" in {
      angular.getResourceRoot must be equalTo("META-INF/resources/org/npmaven/angular/")
    }

    "get the version" in {
      angular.getVersion must be equalTo("1.3.14")
    }
  }
}
