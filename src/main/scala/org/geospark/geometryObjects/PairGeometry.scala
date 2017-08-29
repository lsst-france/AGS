package org.geospark.geometryObjects
import collection.JavaConversions._
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.geom.LineString
import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.geom.Polygon

import scala.Tuple2
import java.io.Serializable
import java.util
// import java.util.HashSet
import util.HashSet


class PairGeometry(var keyGeometry: Geometry, var valueGeometries: HashSet[Geometry]) extends Serializable {
  def getPolygonTuple2 = new Tuple2[Polygon, HashSet[Geometry]](keyGeometry.asInstanceOf[Polygon], valueGeometries)

  def getPointTuple2 = new Tuple2[Point, HashSet[Geometry]](keyGeometry.asInstanceOf[Point], valueGeometries)

  def getLineStringTuple2 = new Tuple2[LineString, HashSet[Geometry]](keyGeometry.asInstanceOf[LineString], valueGeometries)

  def getCircleTuple2 = new Tuple2[Circle, HashSet[Geometry]](keyGeometry.asInstanceOf[Circle], valueGeometries)

  override def equals(o: Any): Boolean = {
    val anotherPairGeometry = o.asInstanceOf[PairGeometry]
    if (keyGeometry == o.asInstanceOf[PairGeometry].keyGeometry &&
      valueGeometries == o.asInstanceOf[PairGeometry].valueGeometries) return true
    false
  }

  override def hashCode: Int = keyGeometry.hashCode * 31 + valueGeometries.hashCode
}


