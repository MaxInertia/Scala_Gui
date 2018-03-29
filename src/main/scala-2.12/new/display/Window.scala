package display

import scalafx.event.ActionEvent
import scalafx.scene.control._

/**
  * Created by Dorian Thiessen on 2018-03-28.
  */
object Window {
  private[display] def fileMenuSetup(menu: Menu) {
    val openItem = new MenuItem("Open")
    //openItem.accelerator = new KeyCodeCombination(KeyCode.O, KeyCombination.ControlDown)
    val saveItem = new MenuItem("Save")
    //saveItem.accelerator = new KeyCodeCombination(KeyCode.S, KeyCombination.ControlDown)
    val exitItem = new MenuItem("Exit")
    //exitItem.accelerator = new KeyCodeCombination(KeyCode.E, KeyCombination.ControlDown)
    menu.items = List(openItem, saveItem, new SeparatorMenuItem, exitItem)
    // Action
    //exitItem.onAction = (e:ActionEvent) => sys.exit
  }

  private[display] def typeMenuSetup(menu: Menu): Unit = {
    // Checkbox Menu Element
    val checkMenu = new Menu("Checks")
    val check1 = new CheckMenuItem("Check 1")
    val check2 = new CheckMenuItem("Check 2")
    checkMenu.items = List(check1,check2)
    // Radio Menu Element
    val radioMenu = new Menu("Radio")
    val radio1 = new RadioMenuItem("Radio 1")
    val radio2 = new RadioMenuItem("Radio 2")
    val radio3 = new RadioMenuItem("Radio 3")
    val group = new ToggleGroup
    group.toggles = List(radio1, radio2, radio3)
    radioMenu.items = List(radio1, radio2, radio3)
    // Link menu items
    menu.items = List(checkMenu, radioMenu)
  }
}
