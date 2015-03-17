package org.npmaven
package it

import org.specs2.mutable.Specification

object PackageSpecs extends Specification {
  "Package class" should {
    "have a name constructor/getter" in {
      val pkg = new Package("blah")
      val name = pkg.getName
      name must be equalTo("blah")
    }
  }
}
