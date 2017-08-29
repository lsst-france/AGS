package org.geospark.spatialPartitioning.quadtree

import java.io.Serializable

import com.vividsolutions.jts.geom.Envelope

class QuadRectangle extends Serializable {
  var x = .0
  var y = .0
  var width = .0
  var height = .0
  var partitionId: Integer = -1

  def this(envelope: Envelope) {
    this()
    this.x = envelope.getMinX
    this.y = envelope.getMinY
    this.width = envelope.getWidth
    this.height = envelope.getHeight
  }

  def this(x: Double, y: Double, width: Double, height: Double) {
    this()
    this.x = x
    this.y = y
    this.width = width
    this.height = height
  }

  def contains(x: Int, y: Int): Boolean =
    this.width > 0 &&
      this.height > 0 &&
      x >= this.x &&
      x <= this.x + this.width &&
      y >= this.y &&
      y <= this.y + this.height

  def contains(r: QuadRectangle): Boolean =
    this.width > 0 &&
      this.height > 0 &&
      r.width > 0 &&
      r.height > 0 &&
      r.x >= this.x &&
      r.x + r.width <= this.x + this.width &&
      r.y >= this.y &&
      r.y + r.height <= this.y + this.height


  def getUniqueId: Int = hashCode
  /*
         Long uniqueId = Long.valueOf(-1);
         try {
             uniqueId = Long.valueOf(RasterizationUtils.Encode2DTo1DId(resolutionX,resolutionY,(int)this.x,(int)this.y));
         } catch (Exception e) {
             e.printStackTrace();
         }
         return uniqueId;
         */

  def getEnvelope = new Envelope(x, x + width, y, y + height)

  override def toString: String = "x: " + x + " y: " + y + " w: " + width + " h: " + height + " PartitionId: " + partitionId

  override def hashCode: Int = {
    val stringId = "" + x + y + width + height
    stringId.hashCode
  }
}
