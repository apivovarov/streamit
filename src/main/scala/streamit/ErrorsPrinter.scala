package streamit

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Milliseconds, StreamingContext}

import scala.util.Try

object ErrorsPrinter {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()

    val ssc = new StreamingContext(conf, Milliseconds(10000))

    val ts = ssc.socketTextStream("localhost", 7799).map(s => Try(s.toInt).getOrElse(0))

    ts.map(x => (if (x >= 0) "p" else "n", x)).reduceByKey(_ + _).print()

    ssc.start()
    ssc.awaitTermination()
  }
}
