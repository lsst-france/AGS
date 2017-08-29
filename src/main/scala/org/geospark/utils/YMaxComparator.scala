
package org.geospark.utils

import java.io.Serializable

import com.vividsolutions.jts.geom.Geometry

class YMaxComparator extends Serializable {
  def compare(spatialObject1: Geometry, spatialObject2: Geometry): Int = {
    if (spatialObject1.getEnvelopeInternal.getMaxY > spatialObject2.getEnvelopeInternal.getMaxY) 1
    else if (spatialObject1.getEnvelopeInternal.getMaxY < spatialObject2.getEnvelopeInternal.getMaxY) -1
    else 0
  }
}
