package visualization

import menus.Launcher.stage

import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination}
import scalafx.Includes._
import scalafx.scene.chart.Chart

/**
  * Created by MaxInertia on 2017-04-01.
  */
object Launcher extends JFXApp {

  stage = new JFXApp.PrimaryStage {
    title = "Menus"
    scene = new Scene(600, 300) {
      val menuBar = new MenuBar
      val fileMenu = new Menu("File")

      // File Menu
      val openItem = new MenuItem("Open")
      openItem.accelerator = new KeyCodeCombination(KeyCode.O, KeyCombination.ControlDown)
      openItem.onAction = (e:ActionEvent) => println("Opening something...")

      val saveItem = new MenuItem("Save")
      saveItem.accelerator = new KeyCodeCombination(KeyCode.S, KeyCombination.ControlDown)
      saveItem.onAction = (e:ActionEvent) => println("Saved application state.")

      val exitItem = new MenuItem("Exit")
      //exitItem.accelerator = new KeyCodeCombination(KeyCode.E, KeyCombination.ControlDown)
      exitItem.onAction = (e:ActionEvent) => {println("Exiting application..."); sys.exit}

      fileMenu.items = List(openItem, saveItem, new SeparatorMenuItem, exitItem)

      // Radio Menu
      val dimensionalityOption = new Menu("Dimensions")
      val twoD = new RadioMenuItem("2D")
      twoD.accelerator = new KeyCodeCombination(KeyCode.D, KeyCombination.ControlDown)
      val threeD = new RadioMenuItem("3D")
      threeD.accelerator = new KeyCodeCombination(KeyCode.D, KeyCombination.ControlDown)

      val group = new ToggleGroup
      group.toggles = List(twoD, threeD)
      dimensionalityOption.items = List(twoD, threeD)

      // Types Menu (Combines Menu items)
      val typeMenu = new Menu("Options")
      typeMenu.items = List(dimensionalityOption)

      // Menu Bar (All Menu item)
      menuBar.menus = List(fileMenu, typeMenu)
      menuBar.prefWidth = 600

      content = List(menuBar)
    }
  }
  val scene = stage.scene.value
  //scene.content = new Chart()
}
