package streamit

import java.io.PrintWriter
import java.net.Socket
import javax.net.ServerSocketFactory

import scala.util.{Random, Try}

/**
  * MockServer
  */
object MockServer {
  def main(args: Array[String]): Unit = {
    val p = Try(args(1).toInt).getOrElse(7799)
    val ss = ServerSocketFactory.getDefault.createServerSocket(p)

    val mainThread = Thread.currentThread()
    Runtime.getRuntime().addShutdownHook(new Thread() {
      override def run(): Unit = {
        Try(ss.close())
        mainThread.join()
      }
    })

    println("Server started")
    Try {
      while (true) {
        println("Waiting for incoming connection...")
        val s = ss.accept()
        println("New client connected")
        val gen = new GenData(s)
        (new Thread(gen)).start()
      }
    }.foreach(_ => Try(ss.close()))
    println("Server stopped")
  }
}

class GenData(val s: Socket) extends Runnable {
  override def run(): Unit = {
    try {
      val os = s.getOutputStream
      val pw = new PrintWriter(os, true)
      val rnd = new Random()
      while (!pw.checkError()) {
        val sig = if (rnd.nextBoolean()) 1 else -1
        val v = rnd.nextInt(1000000) * sig
        pw.println(v)
        Thread.sleep(100)
      }
    } finally {
      Try(s.close())
    }
    println("Client disconnected")
  }
}
