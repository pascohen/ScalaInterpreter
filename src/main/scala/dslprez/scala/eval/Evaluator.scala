package dslprez.scala.eval

import java.net.URL
import scala.tools.nsc._

class Evaluator(val urls: Array[URL], val stream: java.io.PrintStream) {

  def this(urls: Array[URL]) = this(urls, System.out)

  def this(stream: java.io.PrintStream) = this(Array[URL](), stream)

  def this() = this(Array[URL](), System.out)

  val logFile = new java.io.File("/tmp/scalaeval.out")
  val logWriter = new java.io.PrintWriter(logFile, "UTF-8")
  val evalPrinter = new java.io.PrintWriter(stream)

  private def prepareEval(stream: java.io.PrintStream) = {
    System.setOut(stream)
    System.setErr(stream)

    Console.setOut(stream)
    Console.setErr(stream)

    val env = new Settings()

    urls foreach { url => env.bootclasspath.append(url.toString) }
    //env.bootclasspath.append("/usr/home/pcohen/Dev/workspace/Gr8ConfUS/target/classes")

    //env.pluginOptions.appendToValue("continuations:enable")
    //env.pluginsDir.value="/usr/home/pcohen/Dev/workspace/Gr8ConfUS/lib"

    env.usejavacp.value = true
    env
  }
  
  def eval(s: String) = {

    val oldOut = System.out
    val oldErr = System.err
    var r = new Object()

    try {
      val env = prepareEval(stream)
      val n = new scala.tools.nsc.interpreter.IMain(env, evalPrinter)

      r = n.eval(s)

      n.close()
      r
    } catch {
      case t: Throwable =>
        t.printStackTrace(logWriter)
        r
    } finally {
      logWriter.close()
      System.setOut(oldOut)
      System.setErr(oldErr)
    }
  }

  def interpret(s: String) = {
    val oldOut = System.out
    val oldErr = System.err
    var r = new Object()

    try {
      val env = prepareEval(stream)
      val n = new scala.tools.nsc.interpreter.IMain(env, evalPrinter)

      r = n.interpret(s)

      n.close()
      r
    } catch {
      case t: Throwable =>
        t.printStackTrace(logWriter)
        r
    } finally {
      logWriter.close()
      System.setOut(oldOut)
      System.setErr(oldErr)
    }
  }
}
