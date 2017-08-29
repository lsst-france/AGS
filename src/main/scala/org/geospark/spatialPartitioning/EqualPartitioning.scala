package org.geospark.spatialPartitioning

import java.io.Serializable
import java.util
import java.util.{ArrayList, List}

import com.vividsolutions.jts.geom.Envelope

class EqualPartitioning(val boundary: Envelope, val partitions: Int)

/**
  * Instantiates a new equal partitioning.
  *
  * @param boundary   the boundary
  * @param partitions the partitions
  */
  extends Serializable { //Local variable should be declared here
  val root: Double = Math.sqrt(partitions)
  var partitionsAxis: Int = 0
  var intervalX: Double = .0
  var intervalY: Double = .0
  //Calculate how many bounds should be on each axis
  partitionsAxis = root.intValue
  intervalX = (boundary.getMaxX - boundary.getMinX) / partitionsAxis
  intervalY = (boundary.getMaxY - boundary.getMinY) / partitionsAxis
  //System.out.println("Boundary: "+boundary+"root: "+root+" interval: "+intervalX+","+intervalY);
  var i: Int = 0
  /** The grids. */
  private[spatialPartitioning] val grids: util.List[Envelope] = new util.ArrayList[Envelope]

  while (i < partitionsAxis) {
    var j: Int = 0
    while (j < partitionsAxis) {
      val grid: Envelope = new Envelope(boundary.getMinX + intervalX * i, boundary.getMinX + intervalX * (i + 1), boundary.getMinY + intervalY * j, boundary.getMinY + intervalY * (j + 1))
      //System.out.println("Grid: "+grid);
      grids.add(grid)


      {
        j += 1; j - 1
      }
    }
    //System.out.println("Finish one column/one certain x");

    {
      i += 1; i - 1
    }
  }

  /**
    * Gets the grids.
    *
    * @return the grids
    */
  def getGrids: util.List[Envelope] = this.grids
}
