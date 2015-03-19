package org.npmaven.it

import org.specs2.mutable.Specification

object AssetSpecs extends Specification with Fixtures {
  "Asset class" should {
    "have a name getter" in {
      angular_js.getName must be equalTo ("angular")
    }
  }
}
