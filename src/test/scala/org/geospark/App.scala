package org.geospark

import com.vividsolutions.jts.geom.{Coordinate, Envelope, GeometryFactory}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
import org.geospark.enums._
import org.geospark.enums.{FileDataSplitter, GridType, IndexType}
import org.geospark.formatMapper.EarthdataHDFPointMapper
import org.geospark.spatialOperator.{JoinQuery, KNNQuery, RangeQuery}
import org.geospark.spatialRDD.{CircleRDD, PointRDD, PolygonRDD}
import org.scalatest.FunSpec

object App {
  def time[R](text: String, block: => R): R = {
    val t0 = System.nanoTime()
    val result = block
    val t1 = System.nanoTime()

    val dt = t1 - t0
    val sec = dt / 1000000000
    val ns = dt % 1000000000
    println("\n" + text + "> Elapsed time: " + sec + "," + ns + " s")
    result
  }

  def test = {
    FileDataSplitter.getFileDataSplitter("csv").toString +
      IndexType.getIndexType("quadtree").toString +
      GridType.getGridType("equalgrid").toString
  }

  def main (arg: Array[String]) = {
    println( "Hello World!" )
    println(test)

    val conf = new SparkConf().setAppName("scalaTest").setMaster("local[2]")
    val sc = new SparkContext(conf)
    Logger.getLogger("org").setLevel(Level.WARN)
    Logger.getLogger("akka").setLevel(Level.WARN)

    // val resourceFolder = System.getProperty("user.dir")+"/src/test/resources/"
    val resourceFolder = "/home/christian.arnault/geospark/GeoSpark/core/src/test/resources/"

    val PointRDDInputLocation = resourceFolder+"arealm-small.csv"
    val PointRDDSplitter = FileDataSplitter.CSV
    val PointRDDIndexType = IndexType.RTREE
    val PointRDDNumPartitions = 5
    val PointRDDOffset = 0

    val PolygonRDDInputLocation = resourceFolder + "primaryroads-polygon.csv"
    val PolygonRDDSplitter = FileDataSplitter.CSV
    val PolygonRDDNumPartitions = 5
    val PolygonRDDStartOffset = 0
    val PolygonRDDEndOffset = 8

    val geometryFactory=new GeometryFactory()
    val kNNQueryPoint=geometryFactory.createPoint(new Coordinate(-84.01, 34.01))
    val rangeQueryWindow=new Envelope (-90.01,-80.01,30.01,40.01)
    val joinQueryPartitioningType = GridType.RTREE
    val eachQueryLoopTimes=1

    /*
    it("should pass spatial range query") {
      val objectRDD = new PointRDD(sc, PointRDDInputLocation, PointRDDOffset, PointRDDSplitter, true, StorageLevel.MEMORY_ONLY)
      for(i <- 1 to eachQueryLoopTimes)
      {
        val resultSize = RangeQuery.SpatialRangeQuery(objectRDD, rangeQueryWindow, false,false).count
      }
    }
*/

  }
}
