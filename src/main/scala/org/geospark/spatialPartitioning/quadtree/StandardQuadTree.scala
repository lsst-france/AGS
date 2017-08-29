package org.geospark.spatialPartitioning.quadtree

import java.io.Serializable
import java.util

import com.vividsolutions.jts.geom.Envelope

object StandardQuadTree { // GLOBAL CONFIGRATION
  // if this is reached,
  // the zone is subdivised
  var maxItemByNode: Int = 5
  var maxLevel: Int = 10
  val REGION_SELF: Int = -1
  val REGION_NW: Int = 0
  val REGION_NE: Int = 1
  val REGION_SW: Int = 2
  val REGION_SE: Int = 3
}

class StandardQuadTree[T](// current rectangle zone
                          var zone: QuadRectangle, var level: Int) extends Serializable {
  nodes = new util.ArrayList[QuadNode[T]]
  // the current nodes
  private[quadtree] var nodes: util.ArrayList[QuadNode[T]] = new util.ArrayList[QuadNode[T]]
  private var serialId: Int = -1
  private[quadtree] var nodeNum: Int = 0
  // the four sub regions,
  // may be null if not needed
  private[quadtree] var regions: Array[StandardQuadTree[T]] = null

  protected def getZone: QuadRectangle = this.zone

  private def findRegion(r: QuadRectangle, split: Boolean): Int = {
    var region: Int = StandardQuadTree.REGION_SELF
    if (nodeNum >= StandardQuadTree.maxItemByNode && this.level < StandardQuadTree.maxLevel) { // we don't want to split if we just need to retrieve
      // the region, not inserting an element
      if (regions == null && split) { // then create the subregions
        this.split()
      }
      // can be null if not splitted
      if (regions != null) if (regions(StandardQuadTree.REGION_NW).getZone.contains(r)) region = StandardQuadTree.REGION_NW
      else if (regions(StandardQuadTree.REGION_NE).getZone.contains(r)) region = StandardQuadTree.REGION_NE
      else if (regions(StandardQuadTree.REGION_SW).getZone.contains(r)) region = StandardQuadTree.REGION_SW
      else if (regions(StandardQuadTree.REGION_SE).getZone.contains(r)) region = StandardQuadTree.REGION_SE
    }
    region
  }

  private def findRegion(x: Int, y: Int): Int = {
    var region: Int = StandardQuadTree.REGION_SELF
    if (regions != null) if (regions(StandardQuadTree.REGION_NW).getZone.contains(x, y)) region = StandardQuadTree.REGION_NW
    else if (regions(StandardQuadTree.REGION_NE).getZone.contains(x, y)) region = StandardQuadTree.REGION_NE
    else if (regions(StandardQuadTree.REGION_SW).getZone.contains(x, y)) region = StandardQuadTree.REGION_SW
    else if (regions(StandardQuadTree.REGION_SE).getZone.contains(x, y)) region = StandardQuadTree.REGION_SE
    region
  }

  private def split(): Unit = {
    regions = new Array[StandardQuadTree[T]](4)
    val newWidth: Double = zone.width / 2
    val newHeight: Double = zone.height / 2
    val newLevel: Int = level + 1
    regions(StandardQuadTree.REGION_NW) = new StandardQuadTree[T](new QuadRectangle(zone.x, zone.y + zone.height / 2, newWidth, newHeight), newLevel)
    regions(StandardQuadTree.REGION_NE) = new StandardQuadTree[T](new QuadRectangle(zone.x + zone.width / 2, zone.y + zone.height / 2, newWidth, newHeight), newLevel)
    regions(StandardQuadTree.REGION_SW) = new StandardQuadTree[T](new QuadRectangle(zone.x, zone.y, newWidth, newHeight), newLevel)
    regions(StandardQuadTree.REGION_SE) = new StandardQuadTree[T](new QuadRectangle(zone.x + zone.width / 2, zone.y, newWidth, newHeight), newLevel)
  }

  // Force the quad tree to grow up to a certain level. The number of
  def forceGrowUp(minLevel: Int): Unit = {
    assert(minLevel >= 1)
    split()
    nodeNum = StandardQuadTree.maxItemByNode
    if (level + 1 >= minLevel) {}
    else {
      regions(StandardQuadTree.REGION_NW).forceGrowUp(minLevel)
      regions(StandardQuadTree.REGION_NE).forceGrowUp(minLevel)
      regions(StandardQuadTree.REGION_SW).forceGrowUp(minLevel)
      regions(StandardQuadTree.REGION_SE).forceGrowUp(minLevel)
    }
  }

  def insert(r: QuadRectangle, element: T): Unit = {
    val region: Int = this.findRegion(r, true)
    if (region == StandardQuadTree.REGION_SELF || this.level == StandardQuadTree.maxLevel) {
      nodes.add(new QuadNode[T](r, element))
      nodeNum += 1
      return
    }
    else regions(region).insert(r, element)
    if (nodeNum >= StandardQuadTree.maxItemByNode && this.level < StandardQuadTree.maxLevel) { // redispatch the elements
      val tempNodes: util.ArrayList[QuadNode[T]] = new util.ArrayList[QuadNode[T]]
      val length: Int = nodes.size
      var i: Int = 0
      while ( {
        i < length
      }) {
        tempNodes.add(nodes.get(i))

        {
          i += 1; i - 1
        }
      }
      nodes.clear()
      import scala.collection.JavaConversions._
      for (node <- tempNodes) {
        this.insert(node.r, node.element)
      }
    }
  }

  def getElements(list: util.ArrayList[T], r: QuadRectangle): util.ArrayList[T] = {
    val region: Int = this.findRegion(r, false)
    val length: Int = nodes.size
    var i: Int = 0
    while ( {
      i < length
    }) {
      list.add(nodes.get(i).element)

      {
        i += 1; i - 1
      }
    }
    if (region != StandardQuadTree.REGION_SELF) regions(region).getElements(list, r)
    else getAllElements(list, true)
    list
  }

  def getAllElements(list: util.ArrayList[T], firstCall: Boolean): util.ArrayList[T] = {
    if (regions != null) {
      regions(StandardQuadTree.REGION_NW).getAllElements(list, false)
      regions(StandardQuadTree.REGION_NE).getAllElements(list, false)
      regions(StandardQuadTree.REGION_SW).getAllElements(list, false)
      regions(StandardQuadTree.REGION_SE).getAllElements(list, false)
    }
    if (!firstCall) {
      val length: Int = nodes.size
      var i: Int = 0
      while (i < length) {
        list.add(nodes.get(i).element)

        {
          i += 1; i - 1
        }
      }
    }
    list
  }

  def getAllZones(list: util.ArrayList[QuadRectangle]): Unit = {
    list.add(this.zone)
    if (regions != null) {
      regions(StandardQuadTree.REGION_NW).getAllZones(list)
      regions(StandardQuadTree.REGION_NE).getAllZones(list)
      regions(StandardQuadTree.REGION_SW).getAllZones(list)
      regions(StandardQuadTree.REGION_SE).getAllZones(list)
    }
  }

  def getAllLeafNodeUniqueId(uniqueIdList: util.HashSet[Integer]): Unit = {
    if (regions != null) {
      regions(StandardQuadTree.REGION_NW).getAllLeafNodeUniqueId(uniqueIdList)
      regions(StandardQuadTree.REGION_NE).getAllLeafNodeUniqueId(uniqueIdList)
      regions(StandardQuadTree.REGION_SW).getAllLeafNodeUniqueId(uniqueIdList)
      regions(StandardQuadTree.REGION_SE).getAllLeafNodeUniqueId(uniqueIdList)
    }
    else { // This is a leaf node
      uniqueIdList.add(zone.hashCode)
    }
  }

  def getTotalNumLeafNode: Int = if (regions != null) regions(StandardQuadTree.REGION_NW).getTotalNumLeafNode + regions(StandardQuadTree.REGION_NE).getTotalNumLeafNode + regions(StandardQuadTree.REGION_SW).getTotalNumLeafNode + regions(StandardQuadTree.REGION_SE).getTotalNumLeafNode
  else 1

  /**
    * Find the zone that fully contains this query point
    *
    * @param x
    * @param y
    * @return
    */
  @throws[ArrayIndexOutOfBoundsException]
  def getZone(x: Int, y: Int): QuadRectangle = {
    val region: Int = this.findRegion(x, y)
    if (region != StandardQuadTree.REGION_SELF) regions(region).getZone(x, y)
    else if (this.zone.contains(x, y)) this.zone
    else throw new ArrayIndexOutOfBoundsException("[Babylon][StandardQuadTree] this pixel is out of the quad tree boundary.")
  }

  /*
      public QuadRectangle getZone(ArrayList<QuadRectangle> zoneList, QuadRectangle queryRectangle) throws ArrayIndexOutOfBoundsException{
          if (regions!= null) {
              regions[REGION_NW].getZone(uniqueIdList, resolutionX, resolutionY);
              regions[REGION_NE].getZone(uniqueIdList, resolutionX, resolutionY);
              regions[REGION_SW].getZone(uniqueIdList, resolutionX, resolutionY);
              regions[REGION_SE].getZone(uniqueIdList, resolutionX, resolutionY);
          } else {
              // This is a leaf node
              uniqueIdList.add(zone.getUniqueId(resolutionX, resolutionY));
          }
      }
      */ @throws[Exception]
  def getParentZone(x: Int, y: Int, minLevel: Int): QuadRectangle = {
    val region: Int = this.findRegion(x, y)
    // Assume this quad tree has done force grow up. Thus, the min tree depth is the min tree level
    if (level < minLevel) { // In our case, this node must have child nodes. But, in general, if the region is still -1, that means none of its child contains
      // the given x and y
      if (region == StandardQuadTree.REGION_SELF) {
        assert(regions == null)
        if (zone.contains(x, y)) { // This should not happen
          throw new Exception("[Babylon][StandardQuadTree][getParentZone] this leaf node doesn't have enough depth. " + "Please check ForceGrowUp. Expected: " + minLevel + " Actual: " + level + ". Query point: " + x + " " + y + ". Tree statistics, total leaf nodes: " + getTotalNumLeafNode)
        }
        else throw new Exception("[Babylon][StandardQuadTree][getParentZone] this pixel is out of the quad tree boundary.")
      }
      else return regions(region).getParentZone(x, y, minLevel)
    }
    if (zone.contains(x, y)) zone
    else throw new Exception("[Babylon][StandardQuadTree][getParentZone] this pixel is out of the quad tree boundary.")
  }

  def getZone(matchedPartitions: util.ArrayList[QuadRectangle], r: QuadRectangle): Unit = { // This is a leaf node. Assign a serial Id to this leaf node.
    if (regions == null) {
      matchedPartitions.add(zone)
    }
    else if (regions != null) {
      if (!disjoint(regions(StandardQuadTree.REGION_NW).getZone.getEnvelope, r.getEnvelope)) regions(StandardQuadTree.REGION_NW).getZone(matchedPartitions, r)
      if (!disjoint(regions(StandardQuadTree.REGION_NE).getZone.getEnvelope, r.getEnvelope)) regions(StandardQuadTree.REGION_NE).getZone(matchedPartitions, r)
      if (!disjoint(regions(StandardQuadTree.REGION_SW).getZone.getEnvelope, r.getEnvelope)) regions(StandardQuadTree.REGION_SW).getZone(matchedPartitions, r)
      if (!disjoint(regions(StandardQuadTree.REGION_SE).getZone.getEnvelope, r.getEnvelope)) regions(StandardQuadTree.REGION_SE).getZone(matchedPartitions, r)
    }
  }

  def disjoint(r1: Envelope, r2: Envelope): Boolean = if (r1.intersects(r2) || r1.covers(r2) || r2.covers(r1)) false
  else true

  def getSeriaIdMapping(uniqueIdList: util.HashSet[Integer]): util.HashMap[Integer, Integer] = {
    var accumulator: Int = 0
    val idMapping: util.HashMap[Integer, Integer] = new util.HashMap[Integer, Integer]
    val uniqueIdIterator: util.Iterator[Integer] = uniqueIdList.iterator
    while ( {
      uniqueIdIterator.hasNext
    }) {
      val curId: Int = uniqueIdIterator.next
      idMapping.put(curId, accumulator)
      accumulator += 1
    }
    idMapping
  }

  def decidePartitionSerialId(serialIdMapping: util.HashMap[Integer, Integer]): Unit = {
    if (regions == null) {
      serialId = serialIdMapping.get(zone.hashCode)
      zone.partitionId = serialId
      return
    }
    else {
      regions(StandardQuadTree.REGION_NW).decidePartitionSerialId(serialIdMapping)
      regions(StandardQuadTree.REGION_NE).decidePartitionSerialId(serialIdMapping)
      regions(StandardQuadTree.REGION_SW).decidePartitionSerialId(serialIdMapping)
      regions(StandardQuadTree.REGION_SE).decidePartitionSerialId(serialIdMapping)
    }
  }
}
