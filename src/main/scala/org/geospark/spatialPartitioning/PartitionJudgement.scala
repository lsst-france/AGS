package org.geospark.spatialPartitioning

import java.io.Serializable
import java.util
import java.util.{ArrayList, HashSet, Iterator, List}

import com.vividsolutions.jts.geom.Envelope
import com.vividsolutions.jts.geom.Geometry
import org.geospark.spatialPartitioning.quadtree.QuadRectangle
import org.geospark.spatialPartitioning.quadtree.StandardQuadTree

import scala.Tuple2


// TODO: Auto-generated Javadoc
/**
  * The Class PartitionJudgement.
  */
object PartitionJudgement {
  /**
    * Gets the partition ID.
    *
    * @param grids         the grids
    * @param spatialObject the spatial object
    * @return the partition ID
    * @throws Exception the exception
    */
  @throws[Exception]
  def getPartitionID(grids: util.List[Envelope], spatialObject: Any): util.Iterator[Tuple2[Int, Any]] = {
    val result = new util.HashSet[Tuple2[Int, Any]]
    val overflowContainerID = grids.size
    var containFlag = false
    var gridId = 0
    while (gridId < grids.size) {
      if (grids.get(gridId).covers(spatialObject.asInstanceOf[Geometry].getEnvelopeInternal)) {
        result.add(new Tuple2[Int, Any](gridId, spatialObject))
        containFlag = true
      }
      else if (grids.get(gridId).intersects(spatialObject.asInstanceOf[Geometry].getEnvelopeInternal) ||
        spatialObject.asInstanceOf[Geometry].getEnvelopeInternal.covers(grids.get(gridId))) {
        result.add(new Tuple2[Int, Any](gridId, spatialObject))
        //containFlag=true;
      }

      {
        gridId += 1; gridId - 1
      }
    }
    if (containFlag == false) result.add(new Tuple2[Int, Any](overflowContainerID, spatialObject))
    result.iterator
  }

  @throws[Exception]
  def getPartitionID(partitionTree: StandardQuadTree[_], spatialObject: Any): util.Iterator[Tuple2[Int, Any]] = {
    val result = new util.HashSet[Tuple2[Int, Any]]
    var containFlag = false
    val matchedPartitions = new util.ArrayList[QuadRectangle]
    try
      partitionTree.getZone(matchedPartitions, new QuadRectangle(spatialObject.asInstanceOf[Geometry].getEnvelopeInternal))
    catch {
      case e: NullPointerException =>
        return result.iterator
    }
    var i = 0
    while (i < matchedPartitions.size) {
      containFlag = true
      result.add(new Tuple2[Int, Any](matchedPartitions.get(i).partitionId, spatialObject))

      {
        i += 1; i - 1
      }
    }
    if (containFlag == false) {
      //throw new Exception("This object cannot find partition: " +spatialObject);
      // This object is not covered by the partition. Should be dropped.
      // Partition tree from StandardQuadTree do not have missed objects.
    }
    result.iterator
  }
}

