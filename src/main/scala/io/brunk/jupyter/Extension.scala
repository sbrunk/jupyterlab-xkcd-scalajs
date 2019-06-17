package io.brunk.jupyter

import org.scalajs.dom.document
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.html.Image
import typings.atJupyterlabApplicationLib.atJupyterlabApplicationMod.{JupyterLab, JupyterLabPlugin}
import typings.atJupyterlabApplicationLib.libLayoutrestorerMod
import typings.atJupyterlabApplicationLib.libLayoutrestorerMod.ILayoutRestorer
import typings.atJupyterlabApplicationLib.libLayoutrestorerMod.ILayoutRestorerNs.IRestoreOptions
import typings.atJupyterlabApputilsLib.atJupyterlabApputilsMod.InstanceTracker
import typings.atJupyterlabApputilsLib.libCommandpaletteMod
import typings.atJupyterlabApputilsLib.libCommandpaletteMod.{ICommandPalette, IPaletteItem}
import typings.atJupyterlabApputilsLib.libInstancetrackerMod.InstanceTrackerNs.IOptions
import typings.atPhosphorCommandsLib.atPhosphorCommandsMod.CommandRegistryNs.ICommandOptions
import typings.atPhosphorCoreutilsLib.atPhosphorCoreutilsMod.{JSONExtNs, Token}
import typings.atPhosphorCoreutilsLib.libJsonMod.ReadonlyJSONObject
import typings.atPhosphorMessagingLib.atPhosphorMessagingMod.Message
import typings.atPhosphorWidgetsLib.atPhosphorWidgetsMod.Widget

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportTopLevel, JSImport}
import scala.scalajs.js.{JSON, Promise, |}

@js.native
@JSImport("../../style/index.css", JSImport.Namespace)
object Style extends js.Object

/**
  * An xckd comic viewer.
  */
class XkcdWidget extends Widget {
  id = "scalajs-jupyterlab"
  title.label = "scalajs"
  title.closable = true
  addClass("jp-xkcdWidget")

  /**
    * The image element associated with the widget.
    */
  val img = document.createElement("img").asInstanceOf[Image]
  img.className = "jp-xkcdCartoon"
  node.appendChild(img)

  img.insertAdjacentHTML("afterend",
    """<div class="jp-xkcdAttribution">
      <a href="https://creativecommons.org/licenses/by-nc/2.5/" class="jp-xkcdAttribution" target="_blank">
        <img src="https://licensebuttons.net/l/by-nc/2.5/80x15.png" />
      </a>
    </div>"""
  )

  /**
    * Handle update requests for the widget.
    */
  override def onUpdateRequest(msg: Message): Unit =
    Ajax.get("https://egszlpbmle.execute-api.us-east-1.amazonaws.com/prod").foreach {
      xhr => {
        val data = JSON.parse(xhr.responseText)
        img.src = data.img.toString
        img.alt = data.title.toString
        img.title = data.alt.toString
      }
    }
}

@JSExportTopLevel("default")
object Extension extends JupyterLabPlugin[Unit] {
  private val style = Style // only to prevent the style import being optimized away
  var id: String = "jupyterlab_scalajs_xkcd"
  autoStart = true
  requires = js.Array(
    libCommandpaletteMod.^.ICommandPalette,
    libLayoutrestorerMod.^.ILayoutRestorer).asInstanceOf[js.UndefOr[js.Array[Token[_]]]]

  /**
    * Activate the xckd widget extension.
    */
  override def activate(app: JupyterLab, args: js.Any*): Unit | Promise[Unit] = {
    println("JupyterLab Scala.js xkcd extension is activated!")

    val palette = args(0).asInstanceOf[ICommandPalette]
    val restorer = args(1).asInstanceOf[ILayoutRestorer]

    // Create a single widget
    var widget: XkcdWidget = null
    // Track and restore the widget state
    val tracker = new InstanceTracker[Widget](js.Dynamic.literal(namespace = "xkcd").asInstanceOf[IOptions])

    // Add an application command
    val command = "xkcd:open"
    app.commands.addCommand(command, js.Dynamic.literal(
      label = "Random xkcd comic",
      execute = (args: ReadonlyJSONObject) => {
        if (widget == null) {
          // Create a new widget if one does not exist
          widget = new XkcdWidget
          widget.update()
        }
        if (!tracker.has(widget)) {
          // Track the state of the widget for later restoration
          tracker.add(widget)
        }
        if (!widget.isAttached) {
          // Attach the widget to the main work area if it's not there
          app.shell.addToMainArea(widget)
        } else {
          // Refresh the comic in the widget
          widget.update()
        }
        // Activate the widget
        app.shell.activateById(widget.id)
        null
      }
    ).asInstanceOf[ICommandOptions])

    // Add the command to the palette.
    palette.addItem(js.Dynamic.literal(
      command = command,
      category = "Tutorial"
    ).asInstanceOf[IPaletteItem])

    // Track and restore the widget state
    restorer.restore(tracker, js.Dynamic.literal(
      command = command,
      args = () => JSONExtNs.emptyObject,
      name = () => "xkcd"
    ).asInstanceOf[IRestoreOptions[_]])
    ()
  }
}

