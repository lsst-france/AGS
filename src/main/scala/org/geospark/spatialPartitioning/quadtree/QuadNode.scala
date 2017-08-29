package org.geospark.spatialPartitioning.quadtree

import java.io.Serializable

class QuadNode[T] (var r: QuadRectangle, var element: T) extends Serializable {
  override def toString: String = r.toString
}

