package base.snippet
  
import scala.xml._

import net.liftweb._
  import common._
  import http._
    import LiftRules._

object Util {
  def snippetHandlers : SnippetPF = {
    case List("mode") => mode _
    case List("ieConditionalComment") => ieConditionalComment _
    case List("ieconditionalcomment") => ieConditionalComment _
  }

  def mode(xhtml:NodeSeq) = {
    val result =
      for {
        targetMode <- S.attr("is") ?~ "is attribute for target mode was not specified"
        mode <- Box.legacyNullTest(System.getProperty("run.mode")) ?~ "target mode not found"
      } yield {
        if (targetMode == mode) {
          xhtml
        } else {
          NodeSeq.Empty
        }
      }

    result match {
      case Full(data) => data
      case Empty => NodeSeq.Empty
      case Failure(message, _, _) => <div class="error">{message}</div>
    }
  }

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
