package streamit

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Milliseconds, StreamingContext}

import scala.util.Try

object ErrorsPrinter {

  def main(args: Array[String]): Unit = {

    org.apache.log4j.Logger.getLogger("org.apache.spark").setLevel(org.apache.log4j.Level.WARN)
    org.apache.log4j.Logger.getLogger("akka").setLevel(org.apache.log4j.Level.WARN)

    val conf = new SparkConf()

    val dur = Milliseconds(10000)

    val ssc = new StreamingContext(conf, dur)
    ssc.checkpoint("/tmp/spark-checkpoint")

    val ts = ssc.socketTextStream("localhost", 7799, StorageLevel.MEMORY_AND_DISK_SER).
      map(s => Try(s.toInt).getOrElse(0))

    val keyV = ts.map(x => (if (x >= 0) "p" else "n", x))

    keyV.reduceByKey(_ + _).print()

    //val w = keyV.window(dur * 3)
    //w.reduceByKey((x, y) => x + y).print()
    keyV.reduceByKeyAndWindow(_ + _, _ - _, dur * 3).print()

    ssc.start()
    ssc.awaitTermination()
  }
}
