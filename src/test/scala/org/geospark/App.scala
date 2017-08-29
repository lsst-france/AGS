package org.geospark

import org.geospark.enums._

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

  def test() = {
    FileDataSplitter.getFileDataSplitter("csv").toString +
      IndexType.getIndexType("quadtree").toString +
      GridType.getGridType("equalgrid").toString
  }

  def main (arg: Array[String]) = {
    println( "Hello World!" )
    println(test)
  }
}
