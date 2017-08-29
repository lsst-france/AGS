package org.geospark.spatialPartitioning

import org.apache.spark.Partitioner

class SpatialPartitioner extends Partitioner{

  //===================
  /** The num parts. */
  private var numParts = 0

  /**
    * Instantiates a new spatial partitioner.
    *
    * @param grids the grids
    */
  def this(grids: Int) {
    this()
    numParts = grids + 1
  }

  def getPartition(key: Any): Int = {
    key.asInstanceOf[Int]
  }

  def numPartitions: Int = numParts


  //===================


}
