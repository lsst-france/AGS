package org.geospark.spatialPartitioning

import java.io.Serializable
import java.util
import java.util.{ArrayList, Arrays, List}
import com.vividsolutions.jts.geom.Envelope
import com.vividsolutions.jts.geom.Geometry

object HilbertPartitioning {
  /**
    * Compute H value.
    *
    * @param n the n
    * @param x the x
    * @param y the y
    * @return the int
    */
    def computeHValue(n: Int, x0: Int, y0: Int): Int = {
      var h: Int = 0
      var s: Int = n / 2
      var x = x0
      var y = y0

      while ( {
        s > 0
      }) {
        val rx: Int = if ((x & s) > 0) 1
        else 0
        val ry: Int = if ((y & s) > 0) 1
        else 0
        h += s * s * ((3 * rx) ^ ry)
        // Rotate
        if (ry == 0) {
          if (rx == 1) {
            x = n - 1 - x
            y = n - 1 - y
          }
          //Swap x and y
          val t: Int = x
          x = y
          y = t
        }

        s /= 2
      }
      h
    }

  /**
    * Location mapping.
    *
    * @param axisMin      the axis min
    * @param axisLocation the axis location
    * @param axisMax      the axis max
    * @return the int
    */
  def locationMapping(axisMin: Double, axisLocation: Double, axisMax: Double): Int = {
    var gridLocation: Double = .0
    val gridResolution: Int = Short.MaxValue
    gridLocation = (axisLocation - axisMin) * gridResolution / (axisMax - axisMin)
    gridLocation.intValue
  }

  /**
    * Grid ID.
    *
    * @param boundary        the boundary
    * @param spatialObject   the spatial object
    * @param partitionBounds the partition bounds
    * @return the int
    * @throws Exception the exception
    */
  @throws[Exception]
  def gridID(boundary: Envelope, spatialObject: Any, partitionBounds: Array[Int]): Int = {
    var x: Int = 0
    var y: Int = 0
      if (spatialObject.isInstanceOf[Envelope]) {
      x = locationMapping(boundary.getMinX, boundary.getMaxX, (spatialObject.asInstanceOf[Envelope].getMinX + spatialObject.asInstanceOf[Envelope].getMaxX) / 2.0)
      y = locationMapping(boundary.getMinY, boundary.getMaxY, (spatialObject.asInstanceOf[Envelope].getMinY + spatialObject.asInstanceOf[Envelope].getMaxY) / 2.0)
    }
    else if (spatialObject.isInstanceOf[Geometry]) {
      val envelope: Envelope = spatialObject.asInstanceOf[Geometry].getEnvelopeInternal
      x = locationMapping(boundary.getMinX, boundary.getMaxX, (envelope.getMinX + envelope.getMaxX) / 2.0)
      y = locationMapping(boundary.getMinY, boundary.getMaxY, (envelope.getMinY + envelope.getMaxY) / 2.0)
    }
    else throw new Exception("[HilbertPartitioning][gridID] Unsupported spatial object type")
    val gridResolution: Int = Short.MaxValue
    val hValue: Int = computeHValue(gridResolution + 1, x, y)
    var partition: Int = util.Arrays.binarySearch(partitionBounds, hValue)
    if (partition < 0) partition = -partition - 1
    partition
  }

  /**
    * Update envelope.
    *
    * @param envelope      the envelope
    * @param spatialObject the spatial object
    * @return the envelope
    * @throws Exception the exception
    */
  @throws[Exception]
  def updateEnvelope(envelope: Envelope, spatialObject: Any): Envelope = {
    var minX: Double = envelope.getMinX
    var maxX: Double = envelope.getMaxX
    var minY: Double = envelope.getMinY
    var maxY: Double = envelope.getMaxY
    if (spatialObject.isInstanceOf[Envelope]) {
      val i: Envelope = spatialObject.asInstanceOf[Envelope]
      if (minX > i.getMinX) minX = i.getMinX
      if (maxX < i.getMaxX) maxX = i.getMaxX
      if (minY > i.getMinY) minY = i.getMinY
      if (maxY < i.getMaxY) maxY = i.getMaxY
    }
    else if (spatialObject.isInstanceOf[Geometry]) {
      val i: Envelope = spatialObject.asInstanceOf[Geometry].getEnvelopeInternal
      if (minX > i.getMinX) minX = i.getMinX
      if (maxX < i.getMaxX) maxX = i.getMaxX
      if (minY > i.getMinY) minY = i.getMinY
      if (maxY < i.getMaxY) maxY = i.getMaxY
    }
    else throw new Exception("[HilbertPartitioning][updateEnvelope] Unsupported spatial object type")
    new Envelope(minX, maxX, minY, maxY)
  }
}

class HilbertPartitioning @throws[Exception]
(val SampleList: util.List[_], val boundary: Envelope, val partitions: Int)

/**
  * Instantiates a new hilbert partitioning.
  *
  * @param SampleList the sample list
  * @param boundary   the boundary
  * @param partitions the partitions
  * @throws Exception the exception
  */
  extends Serializable { //this.boundary=boundary;
  val gridResolution: Int = Short.MaxValue
  val hValues: Array[Int] = new Array[Int](SampleList.size)
  val gridWithoutID: Array[Envelope] = new Array[Envelope](partitions)
  if (SampleList.get(0).isInstanceOf[Envelope]) {
    var i: Int = 0
    while ( {
      i < SampleList.size
    }) {
      val spatialObject: Envelope = SampleList.get(i).asInstanceOf[Envelope]
      val x: Int = HilbertPartitioning.locationMapping(boundary.getMinX, boundary.getMaxX, (spatialObject.getMinX + spatialObject.getMaxX) / 2.0)
      val y: Int = HilbertPartitioning.locationMapping(boundary.getMinY, boundary.getMaxY, (spatialObject.getMinY + spatialObject.getMaxY) / 2.0)
      hValues(i) = HilbertPartitioning.computeHValue(gridResolution + 1, x, y)

      {
        i += 1; i - 1
      }
    }
  }
  else if (SampleList.get(0).isInstanceOf[Geometry]) {
    var i: Int = 0
    while ( {
      i < SampleList.size
    }) {
      val envelope: Envelope = SampleList.get(i).asInstanceOf[Geometry].getEnvelopeInternal
      val x: Int = HilbertPartitioning.locationMapping(boundary.getMinX, boundary.getMaxX, (envelope.getMinX + envelope.getMaxX) / 2.0)
      val y: Int = HilbertPartitioning.locationMapping(boundary.getMinY, boundary.getMaxY, (envelope.getMinY + envelope.getMaxY) / 2.0)
      hValues(i) = HilbertPartitioning.computeHValue(gridResolution + 1, x, y)

      {
        i += 1; i - 1
      }
    }
  }
  else throw new Exception("[HilbertPartitioning][Constrcutor] Unsupported spatial object type")
  createFromHValues(hValues, partitions)
  var i: Int = 0
  while ( {
    i < SampleList.size
  }) {
    var initialBoundary: Envelope = null
    val spatialObject: Any = SampleList.get(i)
    if (SampleList.get(0).isInstanceOf[Envelope]) initialBoundary = spatialObject.asInstanceOf[Envelope]
    else if (SampleList.get(0).isInstanceOf[Geometry]) initialBoundary = spatialObject.asInstanceOf[Geometry].getEnvelopeInternal
    else throw new Exception("[HilbertPartitioning][Constrcutor] Unsupported spatial object type")
    val partitionID: Int = HilbertPartitioning.gridID(boundary, SampleList.get(i), splits)
    gridWithoutID(partitionID) = initialBoundary

    {
      i += 1; i - 1
    }
  }
  i = 0
  while ( {
    i < SampleList.size
  }) {
    val partitionID: Int = HilbertPartitioning.gridID(boundary, SampleList.get(i), splits)
    gridWithoutID(partitionID) = HilbertPartitioning.updateEnvelope(gridWithoutID(partitionID), SampleList.get(i))

    {
      i += 1; i - 1
    }
  }
  i = 0
  while ( {
    i < gridWithoutID.length
  }) {
    this.grids.add(gridWithoutID(i))

    {
      i += 1; i - 1
    }
  }
  /** The splits. */
  //Partition ID
  protected var splits: Array[Int] = null
  /** The grids. */
  private[spatialPartitioning] val grids: util.List[Envelope] = new util.ArrayList[Envelope]

  /**
    * Creates the from H values.
    *
    * @param hValues    the h values
    * @param partitions the partitions
    */
  protected def createFromHValues(hValues: Array[Int], partitions: Int): Unit = {
    util.Arrays.sort(hValues)
    this.splits = new Array[Int](partitions)
    val maxH: Int = 0x7fffffff
    var i: Int = 0
    while ( {
      i < splits.length
    }) {
      val quantile: Int = ((i + 1).toLong * hValues.length / partitions).toInt
      this.splits(i) = if (quantile == hValues.length) maxH
      else hValues(quantile)

      {
        i += 1; i - 1
      }
    }
  }

  /**
    * Gets the partition bounds.
    *
    * @return the partition bounds
    */
  def getPartitionBounds: Array[Int] = splits

  /**
    * Gets the grids.
    *
    * @return the grids
    */
  def getGrids: util.List[Envelope] = this.grids
}
