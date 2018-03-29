package window

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination}

/**
  * Created by MaxInertia on 2017-03-31.
  */
object Launcher extends JFXApp {
  // Window configuration
  val windowConfig: Config = new Config("wconfig")
  // TODO: Verify windowConfig before proceeding
  Window.initStage(windowConfig)
}
