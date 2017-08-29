package org.geospark.geometryObjects

import com.vividsolutions.jts.geom._


// TODO: Auto-generated Javadoc
/**
  * The Class Circle.
  */
class Circle extends Geometry(new GeometryFactory()) {
  var centerGeometry: Geometry
  val givenRadius: Double

  /** The center point. */
  private var centerPoint: Coordinate = null
  /** The radius. */
  private var radius: Double = .0
  /** The mbr. */
  private var MBR: Envelope = null

  def getCenterGeometry = centerGeometry
  def getCenterPoint = centerPoint
  def getRadius = radius

  def this(centerGeometry: Geometry, givenRadius: Double) {
    // this(new GeometryFactory)
    // super.(new PrecisionModel(centerGeometry.getPrecisionModel))
    this.centerGeometry = centerGeometry

    val centerGeometryMBR = this.centerGeometry.getEnvelopeInternal
    this.centerPoint = new Coordinate(
      (centerGeometryMBR.getMinX + centerGeometryMBR.getMaxX) / 2.0,
      (centerGeometryMBR.getMinY + centerGeometryMBR.getMaxY) / 2.0)
    // Get the internal radius of the object. We need to make sure that the circle at least should be the minimum circumscribed circle
    val width = centerGeometryMBR.getMaxX - centerGeometryMBR.getMinX
    val length = centerGeometryMBR.getMaxY - centerGeometryMBR.getMinY
    val centerGeometryInternalRadius = Math.sqrt(width * width + length * length) / 2
    this.radius = if (givenRadius > centerGeometryInternalRadius) givenRadius else centerGeometryInternalRadius
    this.MBR = new Envelope(
      this.centerPoint.x - this.radius,
      this.centerPoint.x + this.radius,
      this.centerPoint.y - this.radius,
      this.centerPoint.y + this.radius)
    this.setUserData(centerGeometry.getUserData)
  }

  def setRadius(givenRadius: Double): Unit = { // Get the internal radius of the object. We need to make sure that the circle at least should be the minimum circumscribed circle
    val centerGeometryMBR = this.centerGeometry.getEnvelopeInternal
    val width = centerGeometryMBR.getMaxX - centerGeometryMBR.getMinX
    val length = centerGeometryMBR.getMaxY - centerGeometryMBR.getMinY
    val centerGeometryInternalRadius = Math.sqrt(width * width + length * length) / 2
    this.radius = if (givenRadius > centerGeometryInternalRadius) givenRadius
    else centerGeometryInternalRadius
    this.MBR = new Envelope(
      this.centerPoint.x - this.radius,
      this.centerPoint.x + this.radius,
      this.centerPoint.y - this.radius,
      this.centerPoint.y + this.radius)
  }

  def covers(g: Geometry): Boolean = { // short-circuit test
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

  def intersects(g: Geometry): Boolean = { // short-circuit test
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
    covers(x1, y1) || covers(x1, y2) || covers(x2, y2) || covers(x2, y1)
  }

  def covers(x: Double, y: Double): Boolean = {
    val distance = Math.sqrt(
      (x - this.centerPoint.x) * (x - this.centerPoint.x) +
        (y - this.centerPoint.y) * (y - this.centerPoint.y))
    if (distance <= this.radius) true
    else false
  }

  def getGeometryType = "Circle"
  def getCoordinate = centerGeometry.getCoordinate
  def getCoordinates = centerGeometry.getCoordinates
  def getNumPoints = centerGeometry.getNumPoints
  def isEmpty = centerGeometry.isEmpty
  def getDimension = 0
  def getBoundary = getFactory.createGeometryCollection(null)
  def getBoundaryDimension = 0

  def reverse = {
    val g = centerGeometry.reverse
    val newCircle = new Circle(g, radius)
    newCircle
  }

  def clone = {
    val g = centerGeometry.clone.asInstanceOf[Geometry]
    val cloneCircle = new Circle(g, radius)
    cloneCircle
  }

  def equalsExact(g: Geometry, tolerance: Double): Boolean = {
    val type1 = getGeometryType
    val type2 = g.getGeometryType
    val radius1 = this.radius
    val radius2 = g.asInstanceOf[Circle].radius
    if (type1 ne type2) return false
    if (radius1 != radius2) return false
    centerGeometry.equals(g.asInstanceOf[Circle].centerGeometry)
  }

  def apply(filter: CoordinateFilter): Unit = {
    // Do nothing. This circle is not expected to be a complete geometry.
  }

  def apply(filter: CoordinateSequenceFilter): Unit = {
    // Do nothing. This circle is not expected to be a complete geometry.
  }

  def apply(filter: GeometryFilter): Unit = {
    // Do nothing. This circle is not expected to be a complete geometry.
  }

  def apply(filter: GeometryComponentFilter): Unit = {
    // Do nothing. This circle is not expected to be a complete geometry.
  }

  def normalize(): Unit = {
  }

  protected def computeEnvelopeInternal: Envelope = {
    if (isEmpty) return new Envelope
    MBR
  }

  protected def compareToSameClass(other: Any): Int = {
    val env = other.asInstanceOf[Envelope]
    val mbr = MBR
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

  protected def compareToSameClass(other: Any, comp: CoordinateSequenceComparator): Int = {
    val env = other.asInstanceOf[Envelope]
    val mbr = MBR
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
}

