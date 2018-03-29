package display

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane
//import scalafx.Includes._

object Launcher extends JFXApp {
  val INIT_WIDTH: Double = 900
  val INIT_HEIGHT: Double = 600

  stage = new JFXApp.PrimaryStage()
  stage.title = "Viewer"

  val borderPane: BorderPane = new BorderPane {}
  stage.scene = new Scene(INIT_WIDTH, INIT_HEIGHT) {
    /*val menuBar: MenuBar = new MenuBar

    /*root = new BorderPane {
      //padding = Insets(25)
      top = new Group {
        val fileMenu: Menu = new Menu("File")
        Window.fileMenuSetup(fileMenu)
        menuBar.menus = List(fileMenu)
        content = menuBar
      }
    }*/

    root = new Group {
      val fileMenu: Menu = new Menu("File")
      Window.fileMenuSetup(fileMenu)
      menuBar.menus = List(fileMenu)
      content = List(menuBar)
    }*/
    // Bind menu dimensions to scene dimensions
    //menuBar.prefWidth <== width

    content = borderPane
    val envCount: Int = Storage.environments.length
    for(i <- Storage.environments.indices) {
      val env = Storage.environments(i)
      i match {
        case 0 => borderPane.left = env.getCanvas
        case 1 => borderPane.center = env.getCanvas
        case 2 => borderPane.right = env.getCanvas
      }
      env.bindWidthTo(width/envCount)
      env.bindHeightTo(height)
    }
  }

  /*val rootGroup = stage.scene().content(0)
  var dragAnchorX = 0.0
  var dragAnchorY = 0.0
  rootGroup.onMousePressed = (me: MouseEvent) => {
    dragAnchorX = me.screenX - stage.x.value
    dragAnchorY = me.screenY - stage.y.value
  }
  rootGroup.onMouseDragged = (me: MouseEvent) => {
    stage.x = me.screenX - dragAnchorX
    stage.y = me.screenY - dragAnchorY
  }*/
}


// Use Guard to prevent matching multiple cases

// 1. Termination
// 2. Correctness

// Peano numbers... 0, s(0), s(s(0)0 ...

// Correct:
// - Program P says yes and true in domain
// - True in domain but program says no (Things we will not ask, not in the spec!)
// - False in domain and program says no

// Completeness:
// - True in domain and P says yes
// - P says yes but not true in domain

// -- False Positives, False Negatives