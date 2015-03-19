package org.npmaven.it

import scala.io.Source

/**
 * Created by jbarnes on 3/19/2015.
 */
trait Fixtures {
  def rsrc(name:String):String = {
    val r = this.getClass.getClassLoader.getResourceAsStream(name)
    Source.fromInputStream(r, "utf-8").mkString
  }

  lazy val angular = new Package("angular")
  lazy val angular_js = new Asset(angular, "angular")

}