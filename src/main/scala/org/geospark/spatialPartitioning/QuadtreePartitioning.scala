package org.geospark.spatialPartitioning

import com.vividsolutions.jts.geom.Envelope
import com.vividsolutions.jts.geom.Geometry
import org.geospark.spatialPartitioning.quadtree.QuadRectangle
import org.geospark.spatialPartitioning.quadtree.StandardQuadTree
import java.io.Serializable
import java.util
import java.util.{HashMap, HashSet}


// TODO: Auto-generated Javadoc

class QuadtreePartitioning extends Serializable {
  /**
    * The grids.
    */
    private var partitionTree: StandardQuadTree[Int] = null

  /**
    * Instantiates a new rtree partitioning.
    *
    * @param SampleList the sample list
    * @param boundary   the boundary
    * @param partitions the partitions
    * @throws Exception the exception
    */
  def this(SampleList: List[_], boundary: Envelope, partitions: Int) {
    this()
    StandardQuadTree.maxItemByNode = SampleList.size / partitions
    StandardQuadTree.maxLevel = 100000
    partitionTree = new StandardQuadTree[Int](new QuadRectangle(boundary.getMinX, boundary.getMinY, boundary.getWidth, boundary.getHeight), 0)
    var i = 0
    while (i < SampleList.size) {
      if (SampleList.take(i).isInstanceOf[Envelope]) {
        val spatialObject = SampleList.take(i).asInstanceOf[Envelope]
        partitionTree.insert(new QuadRectangle(spatialObject), 1)
      }
      else if (SampleList.take(i).isInstanceOf[Geometry]) {
        val spatialObject = SampleList.take(i).asInstanceOf[Geometry]
        partitionTree.insert(new QuadRectangle(spatialObject.getEnvelopeInternal), 1)
      }
      else throw new Exception("[QuadtreePartitioning][Constrcutor] Unsupported spatial object type")

      {
        i += 1; i - 1
      }
    }
    val uniqueIdList = new util.HashSet[Integer]
    partitionTree.getAllLeafNodeUniqueId(uniqueIdList)
    val serialIdMapping = partitionTree.getSeriaIdMapping(uniqueIdList)
    partitionTree.decidePartitionSerialId(serialIdMapping)
  }

  def this(SampleList: List[_], boundary: Envelope, partitions: Int, minTreeLevel: Int) {
    this()
    StandardQuadTree.maxItemByNode = SampleList.size / partitions
    StandardQuadTree.maxLevel = 100000
    partitionTree = new StandardQuadTree[Int](new QuadRectangle(boundary.getMinX, boundary.getMinY, boundary.getWidth, boundary.getHeight), 0)
    partitionTree.forceGrowUp(minTreeLevel)
    var i = 0
    while ( {
      i < SampleList.size
    }) {
      if (SampleList.take(i).isInstanceOf[Envelope]) {
        val spatialObject = SampleList.take(i).asInstanceOf[Envelope]
        partitionTree.insert(new QuadRectangle(spatialObject), 1)
      }
      else if (SampleList.take(i).isInstanceOf[Geometry]) {
        val spatialObject = SampleList.take(i).asInstanceOf[Geometry]
        partitionTree.insert(new QuadRectangle(spatialObject.getEnvelopeInternal), 1)
      }
      else throw new Exception("[QuadtreePartitioning][Constrcutor] Unsupported spatial object type")

      {
        i += 1; i - 1
      }
    }
    val uniqueIdList = new util.HashSet[Integer]
    partitionTree.getAllLeafNodeUniqueId(uniqueIdList)
    val serialIdMapping = partitionTree.getSeriaIdMapping(uniqueIdList)
    partitionTree.decidePartitionSerialId(serialIdMapping)
  }

  def getPartitionTree: StandardQuadTree[Int] = this.partitionTree
}
