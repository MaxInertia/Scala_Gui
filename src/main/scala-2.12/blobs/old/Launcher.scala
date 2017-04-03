package blobs.old

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color.{DarkGray, DarkRed, Gray, LightGray, Red, White}
import scalafx.scene.paint.{Color, LinearGradient, Stops}
import scalafx.scene.text.Text

/**
  * Created by MaxInertia on 2017-03-31.
  */
object Launcher extends JFXApp {
  stage = MyStage
}

object MyStage extends PrimaryStage {
  title = "ScalaFX GUI"

  object LaunchScene extends Scene {
    fill = Color.rgb(38,38,38)
    content = new VBox {
      padding = Insets(50, 80, 50, 80)
      children = Seq(
        new Text {
          text = "Powered by"
          style = "-fx-font: normal 20pt sans-serif"
          fill = new LinearGradient(
            endX = 0,
            stops = Stops(LightGray, Gray)
          )
        },
        new HBox {
          children = Seq(
            new Text {
              text = "Scala"
              style = "-fx-font: normal bold 100pt sans-serif"
              fill = new LinearGradient(
                endX = 0,
                stops = Stops(Red, DarkRed)
              )
            },
            new Text {
              text = "FX"
              style = "-fx-font: italic bold 100pt sans-serif"
              fill = new LinearGradient(
                endX = 0,
                stops = Stops(White, DarkGray)
              )
              effect = new DropShadow {
                color = DarkGray
                radius = 15
                spread = 0.25
              }
            }
          )
        }
      )
    }

    //onKeyPressed = (e:KeyEvent) => {
    //  println("Key pressed!")
    //}

    onMouseClicked = (e:MouseEvent) => {
      MyStage.hide()
      scene = MainScene
      MyStage.show()
    }
  }

  scene = LaunchScene
}
