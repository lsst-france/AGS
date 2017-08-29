
package org.geospark.enums

object FileDataSplitter extends Enumeration {
  type FileDataSplitter = Value
  val CSV, TSV, GEOJSON, WKT = Value

  def getFileDataSplitter (str: String): FileDataSplitter =
    FileDataSplitter.values.filter(_.toString.equalsIgnoreCase(str)).firstKey

  var splitter : String = ""
}
