package org.geospark.utils

object RDDSampleUtils {
  /**
    * Gets the sample numbers.
    *
    * @param numPartitions        the num partitions
    * @param totalNumberOfRecords the total number of records
    * @return the sample numbers
    * @throws Exception the exception
    */
  @throws[Exception]
  def getSampleNumbers(numPartitions: Integer, totalNumberOfRecords: Long): Int = {
    var sampleNumbers = 0L
    /*
           * If the input RDD is too small, Geospark will use the entire RDD instead of taking samples.
           */
    if (totalNumberOfRecords >= 1000) sampleNumbers = totalNumberOfRecords / 100
    else sampleNumbers = totalNumberOfRecords
    if (sampleNumbers > Integer.MAX_VALUE) sampleNumbers = Integer.MAX_VALUE
    val result = sampleNumbers.toInt
    // Partition size is too big. Should throw exception for this.
    if (sampleNumbers < 2 * numPartitions) throw new Exception("[RDDSampleUtils][getSampleNumbers] Too many RDD partitions. Please make this RDD's partitions less than " + sampleNumbers / 2)
    result
  }
}
