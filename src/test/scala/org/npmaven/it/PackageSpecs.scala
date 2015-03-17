package org.npmaven
package it

import org.specs2.mutable.Specification

object PackageSpecs extends Specification {
  "Package class" should {
    "have a name constructor/getter" in {
      val pkg = new Package("angular")
      val name = pkg.getName
      name must be equalTo("angular")
    }

    "get the angular version" in {
      val pkg = new Package("angular")
      pkg.getVersion must be equalTo("1.3.14")
    }
  }
}
