package menus

import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination}
import scalafx.Includes._

/**
  * Created by MaxInertia on 2017-03-31.
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
      val saveItem = new MenuItem("Save")
      saveItem.accelerator = new KeyCodeCombination(KeyCode.S, KeyCombination.ControlDown)
      val exitItem = new MenuItem("Exit")
      exitItem.accelerator = new KeyCodeCombination(KeyCode.E, KeyCombination.ControlDown)
      fileMenu.items = List(openItem, saveItem, new SeparatorMenuItem, exitItem)

      // Check Menu
      val checkMenu = new Menu("Checks")
      val check1 = new CheckMenuItem("Check 1")
      val check2 = new CheckMenuItem("Check 2")
      checkMenu.items = List(check1,check2)

      // Radio Menu
      val radioMenu = new Menu("Radio")
      val radio1 = new RadioMenuItem("Radio 1")
      val radio2 = new RadioMenuItem("Radio 2")
      val radio3 = new RadioMenuItem("Radio 3")
      val group = new ToggleGroup
      group.toggles = List(radio1, radio2, radio3)
      radioMenu.items = List(radio1, radio2, radio3)

      // Types Menu (Combines Menu items)
      val typeMenu = new Menu("Types")
      typeMenu.items = List(checkMenu, radioMenu)

      // Menu Bar (All Menu item)
      menuBar.menus = List(fileMenu, typeMenu)
      menuBar.prefWidth = 600

      val menuButton = new MenuButton("Menu Button")
      menuButton.items = List(new MenuItem("Button 1"), new MenuItem("Button 2"))
      menuButton.layoutX = 20
      menuButton.layoutY = 50

      content = List(menuBar, menuButton)

      exitItem.onAction = (e:ActionEvent) => sys.exit
    }
  }
}
