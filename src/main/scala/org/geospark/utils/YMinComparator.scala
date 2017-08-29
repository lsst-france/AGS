
package org.geospark.utils

import java.io.Serializable

import com.vividsolutions.jts.geom.Geometry

class YMinComparator extends Serializable {
  def compare(spatialObject1: Geometry, spatialObject2: Geometry): Int = {
    if (spatialObject1.getEnvelopeInternal.getMinY > spatialObject2.getEnvelopeInternal.getMinY) 1
    else if (spatialObject1.getEnvelopeInternal.getMinY < spatialObject2.getEnvelopeInternal.getMinY) -1
    else 0
  }
}
