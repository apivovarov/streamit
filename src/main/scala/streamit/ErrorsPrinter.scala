package streamit

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Milliseconds, StreamingContext}

object ErrorsPrinter {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()

    val ssc = new StreamingContext(conf, Milliseconds(10000))

    ssc.socketTextStream("localhost", 7799).print()


//    map { x => println("=====================----------==================")
//      x
//    }
      //.filter(_.toLowerCase().contains("error")).print()

    ssc.start()
    ssc.awaitTermination()
  }
}
