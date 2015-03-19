package org.npmaven.it

import org.specs2.mutable.Specification
import Classifier._

object AssetSpecs extends Specification with Fixtures {
  "Asset class" should {
    "have a name getter" in {
      angular_js.getName must be equalTo ("./angular.js")
    }

    "get the versioned prefix" in {
      angular_js.getVersionedPrefix must be equalTo("angular-1.3.14")
    }

    "get the versioned name of the Classifier.DEFAULT resource" in {
      angular_js.getVersionedName(DEFAULT) must be equalTo("angular-1.3.14.js")
    }

    "get the versioned name of the Classifier.MIN resource" in {
      angular_js.getVersionedName(MIN) must be equalTo("angular-1.3.14.min.js")
    }

    "get the versioned name of the Classifier.MAP resource" in {
      angular_js.getVersionedName(MAP) must be equalTo("angular-1.3.14.min.js.map")
    }
  }
}
