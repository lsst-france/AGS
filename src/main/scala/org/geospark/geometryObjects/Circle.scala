package org.geospark.geometryObjects

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.CoordinateFilter
import com.vividsolutions.jts.geom.CoordinateSequenceComparator
import com.vividsolutions.jts.geom.CoordinateSequenceFilter
import com.vividsolutions.jts.geom.Envelope
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.geom.GeometryComponentFilter
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.GeometryFilter


class Circle(var centerGeometry: Geometry, val givenRadius: Double) extends Geometry(new GeometryFactory(centerGeometry.getPrecisionModel)) {

  private var centerPoint: Coordinate = null
  private var radius = .0
  private var MBR: Envelope = null

  def getCenterPoint: Coordinate = this.centerPoint
  def getRadius: Double = radius

  val centerGeometryMBR: Envelope = this.centerGeometry.getEnvelopeInternal

  this.centerPoint = new Coordinate(
    (centerGeometryMBR.getMinX + centerGeometryMBR.getMaxX) / 2.0,
    (centerGeometryMBR.getMinY + centerGeometryMBR.getMaxY) / 2.0)

  // Get the internal radius of the object. We need to make sure that the circle at least should be the minimum circumscribed circle
  val width: Double = centerGeometryMBR.getMaxX - centerGeometryMBR.getMinX
  val length: Double = centerGeometryMBR.getMaxY - centerGeometryMBR.getMinY
  val centerGeometryInternalRadius: Double = Math.sqrt(width * width + length * length) / 2
  this.radius = if (givenRadius > centerGeometryInternalRadius) givenRadius
  else centerGeometryInternalRadius
  this.MBR = new Envelope(
    this.centerPoint.x - this.radius,
    this.centerPoint.x + this.radius,
    this.centerPoint.y - this.radius,
    this.centerPoint.y + this.radius)
  this.setUserData(centerGeometry.getUserData)

  def getCenterGeometry: Geometry = centerGeometry

  def setRadius(givenRadius: Double): Unit = {
    val centerGeometryMBR = this.centerGeometry.getEnvelopeInternal
    val width = centerGeometryMBR.getMaxX - centerGeometryMBR.getMinX
    val length = centerGeometryMBR.getMaxY - centerGeometryMBR.getMinY
    val centerGeometryInternalRadius = Math.sqrt(width * width + length * length) / 2
    this.radius = if (givenRadius > centerGeometryInternalRadius) givenRadius
    else centerGeometryInternalRadius
    this.MBR = new Envelope(this.centerPoint.x - this.radius, this.centerPoint.x + this.radius, this.centerPoint.y - this.radius, this.centerPoint.y + this.radius)
  }

  override def covers(g: Geometry): Boolean = { // short-circuit test
    if (!getEnvelopeInternal.covers(g.getEnvelopeInternal)) return false
    // optimization for rectangle arguments
    if (isRectangle) { // since we have already tested that the test envelope is covered
      return true
    }
    var x1 = .0
    var x2 = .0
    var y1 = .0
    var y2 = .0
    x1 = g.getEnvelopeInternal.getMinX
    x2 = g.getEnvelopeInternal.getMaxX
    y1 = g.getEnvelopeInternal.getMinY
    y2 = g.getEnvelopeInternal.getMaxY
    covers(x1, y1) && covers(x1, y2) && covers(x2, y2) && covers(x2, y1)
  }

  override def intersects(g: Geometry): Boolean = {
    if (!getEnvelopeInternal.covers(g.getEnvelopeInternal)) return false
    if (isRectangle) return true
    var x1 = .0
    var x2 = .0
    var y1 = .0
    var y2 = .0
    x1 = g.getEnvelopeInternal.getMinX
    x2 = g.getEnvelopeInternal.getMaxX
    y1 = g.getEnvelopeInternal.getMinY
    y2 = g.getEnvelopeInternal.getMaxY
    covers(x1, y1) || covers(x1, y2) || covers(x2, y2) || covers(x2, y1)
  }

  def covers(x: Double, y: Double): Boolean = {
    val distance = Math.sqrt((x - this.centerPoint.x) * (x - this.centerPoint.x) + (y - this.centerPoint.y) * (y - this.centerPoint.y))
    if (distance <= this.radius) true
    else false
  }

  override def getGeometryType = "Circle"
  override def getCoordinate: Coordinate = this.centerGeometry.getCoordinate
  override def getCoordinates: Array[Coordinate] = this.centerGeometry.getCoordinates
  override def getNumPoints: Int = this.centerGeometry.getNumPoints
  override def isEmpty: Boolean = this.centerGeometry.isEmpty
  override def getDimension = 0
  override def getBoundary: Geometry = getFactory.createGeometryCollection(null)
  override def getBoundaryDimension = 0
  override def reverse: Geometry = {
    val g = this.centerGeometry.reverse
    val newCircle = new Circle(g, this.radius)
    newCircle
  }

  override def clone: Geometry = {
    val g = this.centerGeometry.clone.asInstanceOf[Geometry]
    val cloneCircle = new Circle(g, this.radius)
    cloneCircle // return the clone
  }

  override def equalsExact(g: Geometry, tolerance: Double): Boolean = {
    val type1 = this.getGeometryType
    val type2 = g.asInstanceOf[Geometry].getGeometryType
    val radius1 = this.radius
    val radius2 = g.asInstanceOf[Circle].radius
    if (type1 ne type2) return false
    if (radius1 != radius2) return false
    this.centerGeometry == g.asInstanceOf[Circle].centerGeometry
  }

  override def apply(filter: CoordinateFilter): Unit = {}
  override def apply(filter: CoordinateSequenceFilter): Unit = {}
  override def apply(filter: GeometryFilter): Unit = {}
  override def apply(filter: GeometryComponentFilter): Unit = {}
  override def normalize(): Unit = {}

  override protected def computeEnvelopeInternal: Envelope = {
    if (isEmpty) return new Envelope
    this.MBR
  }

  override protected def compareToSameClass(other: Any): Int = {
    val env = other.asInstanceOf[Envelope]
    val mbr = this.MBR
    // compare based on numerical ordering of ordinates
    if (mbr.getMinX < env.getMinX) return -1
    if (mbr.getMinX > env.getMinX) return 1
    if (mbr.getMinY < env.getMinY) return -1
    if (mbr.getMinY > env.getMinY) return 1
    if (mbr.getMaxX < env.getMaxX) return -1
    if (mbr.getMaxX > env.getMaxX) return 1
    if (mbr.getMaxY < env.getMaxY) return -1
    if (mbr.getMaxY > env.getMaxY) return 1
    0
  }

  override protected def compareToSameClass(other: Any, comp: CoordinateSequenceComparator): Int = {
    val env = other.asInstanceOf[Envelope]
    val mbr = this.MBR
    if (mbr.getMinX < env.getMinX) return -1
    if (mbr.getMinX > env.getMinX) return 1
    if (mbr.getMinY < env.getMinY) return -1
    if (mbr.getMinY > env.getMinY) return 1
    if (mbr.getMaxX < env.getMaxX) return -1
    if (mbr.getMaxX > env.getMaxX) return 1
    if (mbr.getMaxY < env.getMaxY) return -1
    if (mbr.getMaxY > env.getMaxY) return 1
    0
  }
}

