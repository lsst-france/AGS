
package org.geospark.enums

object GridType extends Enumeration {
  type GridType = Value
  val EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE = Value

  def getGridType(str: String): GridType =
    GridType.values.filter(_.toString.equalsIgnoreCase(str)).firstKey

  var splitter : String = ""
}
