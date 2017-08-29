
package org.geospark.utils

import java.io.Serializable

import com.vividsolutions.jts.geom.Geometry

class XMinComparator extends Serializable {
  def compare(spatialObject1: Geometry, spatialObject2: Geometry): Int = {
    if (spatialObject1.getEnvelopeInternal.getMinX > spatialObject2.getEnvelopeInternal.getMinX) 1
    else if (spatialObject1.getEnvelopeInternal.getMinX < spatialObject2.getEnvelopeInternal.getMinX) -1
    else 0
  }
}
