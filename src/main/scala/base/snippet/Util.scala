package base.snippet
  
import scala.xml._

import net.liftweb.http._

class Util {
  /**
   * Insert a conditional comment. Ensures that the comment (a) doesn't get
   * stripped in production mode and (b) can contain XML that is pre-processed
   * by, for example, with-resource-id.
   */
  def ieConditionalComment(xhtml:NodeSeq) = {
    val ieVersion =
      (for (version <- S.attr("version")) yield version) openOr "IE"
    Unparsed("<!--[if " + ieVersion + "]>") ++ xhtml ++ Unparsed("<![endif]-->")
  }
}
