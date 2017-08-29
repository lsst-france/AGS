package org.geospark.spatialPartitioning

import java.io.Serializable
import java.util
import java.util.{ArrayList, List}

import com.vividsolutions.jts.geom.Envelope
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.index.strtree.STRtree


// TODO: Auto-generated Javadoc
/**
  * The Class RtreePartitioning.
  */
class RtreePartitioning @throws[Exception]
(val SampleList: util.List[_], val boundary: Envelope, val partitions: Int)

/**
  * Instantiates a new rtree partitioning.
  *
  * @param SampleList the sample list
  * @param boundary   the boundary
  * @param partitions the partitions
  * @throws Exception the exception
  */
//grids.add(new EnvelopeWithGrid(boundary,grids.size()));
  extends Serializable {
  val strtree = new STRtree(SampleList.size / partitions)
  var i = 0
  while ( {
    i < SampleList.size
  }) {
    if (SampleList.get(i).isInstanceOf[Envelope]) {
      val spatialObject = SampleList.get(i).asInstanceOf[Envelope]
      strtree.insert(spatialObject, spatialObject)
    }
    else if (SampleList.get(i).isInstanceOf[Geometry]) {
      val spatialObject = SampleList.get(i).asInstanceOf[Geometry]
      strtree.insert(spatialObject.getEnvelopeInternal, spatialObject)
    }
    else throw new Exception("[RtreePartitioning][Constrcutor] Unsupported spatial object type")

    {
      i += 1; i - 1
    }
  }
  val envelopes: util.List[_ <: Any] = strtree.query(boundary)
    // queryBoundary
  i = 0
  while (i < envelopes.size) {
    grids.add(envelopes.get(i).asInstanceOf[Envelope])

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
