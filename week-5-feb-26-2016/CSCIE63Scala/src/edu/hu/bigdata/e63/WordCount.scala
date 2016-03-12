/**
 *
 */
package edu.hu.bigdata.e63

/**
 * @author joglekarrb
 *
 */

import org.apache.spark._
import org.apache.spark.SparkContext._

object WordCount {
  def main(args: Array[String]): Unit={
    val inputFile = args(0)
    val outputFile = args(1)
    val conf = new SparkConf().setMaster("local").setAppName("wordCount")
    // Create a Scala Spark Context.
    val sc = new SparkContext(conf)
    // Load our input data.
    val inputRDD = sc.textFile(inputFile)
    // Split up into words.
    val words = inputRDD.flatMap(_.split(" "))
    // Transform into word and count. _+_
    val counts = words.map(word => (word, 1)).reduceByKey { case (x, y) => x + y }
    // Save the word count back out to a text file, causing evaluation.
    counts.saveAsTextFile(outputFile)
  }
}