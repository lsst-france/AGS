
package org.geospark.utils

import java.io.Serializable
import java.util.Comparator

import com.vividsolutions.jts.geom.Geometry

class XMaxComparator extends Serializable {
  def compare(spatialObject1: Geometry, spatialObject2: Geometry): Int = {
    if (spatialObject1.getEnvelopeInternal.getMaxX > spatialObject2.getEnvelopeInternal.getMaxX) 1
    else if (spatialObject1.getEnvelopeInternal.getMaxX < spatialObject2.getEnvelopeInternal.getMaxX) -1
    else 0
  }
}
