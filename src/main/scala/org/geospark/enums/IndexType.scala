
package org.geospark.enums

object IndexType extends Enumeration {
  type IndexType = Value
  val QUADTREE, RTREE = Value

  def getIndexType(str: String): IndexType =
    IndexType.values.filter(_.toString.equalsIgnoreCase(str)).firstKey

  var splitter : String = ""
}
