package bootstrap.liftweb

import net.liftweb._
  import util._
  import Helpers._

import common._
import http._
import sitemap._
import Loc._

import net.liftweb._
  import common._
  import http._
    import js.jquery.JQuery14Artifacts
  import mongodb._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    val mongoHost = MongoHost(Props.get("mongo.host") openOr "127.0.0.1",
                             (for (port <- Props.get("mongo.port")) yield port.toInt) openOr 27017)
    // define the dbs
    MongoDB.defineDb(DefaultMongoIdentifier, MongoAddress(mongoHost, Props.get("mongo.db") openOr "default"))

    // where to search snippet
    LiftRules.addToPackages("base") // helpers
    LiftRules.addToPackages("com.mycompany")

    LiftRules.jsArtifacts = JQuery14Artifacts

    // Build SiteMap
    val entries = List(
      Menu.i("Home") / "index", // the simple way to declare a menu

      // more complex because this menu allows anything in the
      // /static path to be visible
      Menu(Loc("Static", Link(List("static"), true, "/static/index"), 
	       "Static Content")))

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))

    // Attaches a resource id for <lift:with-resource-id> that is the
    // timestamp of the last modified date for the given resource; if the
    // resource can't be found, fall back on the per-server-instance id
    // provided by default..
    val defaultResourceId = LiftRules.attachResourceId
    LiftRules.attachResourceId = { filename:String =>
      import java.io.File

      (for {
        url <- LiftRules.getResource(filename)
        file <- tryo(new File(url.toURI()))
        timeString = file.lastModified.toString if file.lastModified != 0L
      } yield {
        filename + "?" + timeString
      }) openOr defaultResourceId(filename)
    }

    LiftRules.useXhtmlMimeType = false

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

  }
}
