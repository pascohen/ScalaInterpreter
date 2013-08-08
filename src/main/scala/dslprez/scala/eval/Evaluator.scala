package dslprez.scala.eval

import java.net.URL
import scala.tools.nsc._

class Evaluator(stream: java.io.PrintStream) {

  private[this] val oldOut = System.out
  private[this] val oldErr = System.err

  def this() = this(System.out)

  val logWriter = {
    val logFile = new java.io.File("/tmp/scalaeval.out")
    new java.io.PrintWriter(logFile, "UTF-8")
  }

  val settings = new Settings()
  settings.usejavacp.value = true

  lazy val interpreter = createInterpreter

  def withUrls(urls: Array[URL]) = {
    logWriter.println("Adding urls " + urls)
    urls foreach { url => settings.bootclasspath.append(url.toString) }
    this
  }

  def withPluginOption(s: String) = {
    logWriter.println("Setting pluginOption " + s)
    settings.pluginOptions.appendToValue(s)
    this
  }

  def withContinuations = {
    withPluginOption("continuations:enable")
  }

  def withPluginsDir(dir: String) = {
    logWriter.println("Adding " + dir)
    settings.pluginsDir.value = dir
    this
  }

  def addPlugins(plugins: Array[String]) = {
    logWriter.println("Adding plugins " + plugins)
    plugins foreach { plugin => settings.plugin.appendToValue(plugin) }
    this
  }

  private[this] def createInterpreter = {
    System.setOut(stream)
    System.setErr(stream)

    Console.setOut(stream)
    Console.setErr(stream)
    logWriter.println("createInterpreter")
    new scala.tools.nsc.interpreter.IMain(settings)
  }

  def close = {
    interpreter.close()
    logWriter.close()
    System.setOut(oldOut)
    System.setErr(oldErr)
  }
  

  def bind(s:String, t:String, o:Object) {
    interpreter.bind(s,t,o)
  }

  def addImport(s:String):Unit = addImport(List(s))
  
  def addImport(l:List[String]) = l foreach { i => eval("import "+i)}
  
  def eval(s: String) = {
    try {
      interpreter.eval(s)
    } catch {
      case t: Throwable =>
        t.printStackTrace(logWriter)
        t 
    }
  }

  def interpret(s: String) = {
    try {
      interpreter.interpret(s)
    } catch {
      case t: Throwable =>
        t.printStackTrace(logWriter)
        t
    }
  }
}
