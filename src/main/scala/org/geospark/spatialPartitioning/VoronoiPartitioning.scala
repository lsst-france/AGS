package org.geospark.spatialPartitioning

import java.io.Serializable
import java.util
import java.util.{ArrayList, List}

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Envelope
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.MultiPoint
import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.geom.Polygon
import com.vividsolutions.jts.triangulate.VoronoiDiagramBuilder


// TODO: Auto-generated Javadoc
/**
  * The Class VoronoiPartitioning.
  */
class VoronoiPartitioning @throws[Exception]
(val SampleList: util.List[_], val boundary: Envelope, val partitions: Int)

/**
  * Instantiates a new voronoi partitioning.
  *
  * @param SampleList the sample list
  * @param boundary   the boundary
  * @param partitions the partitions
  * @throws Exception the exception
  */
//grids.add(new EnvelopeWithGrid(boundary,grids.size()));
  extends Serializable {
  val factory = new GeometryFactory()
  val subSampleList = new util.ArrayList[Point]
  //Take a subsample accoring to the partitions

  if (SampleList.get(0).isInstanceOf[Envelope]) {
    var i = 0
    while ( {
      i < SampleList.size
    }) {
      val envelope = SampleList.get(i).asInstanceOf[Envelope]
      val coordinate = new Coordinate(
        (envelope.getMinX + envelope.getMaxX) / 2.0,
        (envelope.getMinY + envelope.getMaxY) / 2.0)
      subSampleList.add(factory.createPoint(coordinate))

      i = i + SampleList.size / partitions
    }
  }
  else if (SampleList.get(0).isInstanceOf[Geometry]) {
    var i = 0
    while ( {
      i < SampleList.size
    }) {
      val envelope = SampleList.get(i).asInstanceOf[Geometry].getEnvelopeInternal
      val coordinate = new Coordinate(
        (envelope.getMinX + envelope.getMaxX) / 2.0,
        (envelope.getMinY + envelope.getMaxY) / 2.0)
      subSampleList.add(factory.createPoint(coordinate))

      i = i + SampleList.size / partitions
    }
  }
  else throw new Exception("[VoronoiPartitioning][Constructor] Unsupported spatial object type")

  var mp = factory.createMultiPoint(subSampleList.toArray(new Array[Point](subSampleList.size)))

  val voronoiBuilder = new VoronoiDiagramBuilder
  voronoiBuilder.setSites(mp)

  val voronoiDiagram: Geometry = voronoiBuilder.getDiagram(factory)
  var i = 0
  while (i < voronoiDiagram.getNumGeometries) {
    val poly = voronoiDiagram.getGeometryN(i).asInstanceOf[Geometry]
    grids.add(poly.getEnvelopeInternal)

    {
      i += 1; i - 1
    }
  }
  /** The grids. */
  private[spatialPartitioning] val grids = new util.ArrayList[Envelope]

  /**
    * Gets the grids.
    *
    * @return the grids
    */
  def getGrids: util.List[Envelope] = this.grids
}
