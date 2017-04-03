package blobs.refined

import java.io.IOException

import blobs.refined.canvas.{CCBuilder, ClusterCanvas}
import blobs.refined.objects.Cluster

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, Slider}
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.paint.Color
import scalafx.stage.WindowEvent

/**
  * Created by MaxInertia on 2017-04-02.
  */
private[refined] object Launcher extends JFXApp {

  // == Initialization Values ==

  // Dimensions
  val SCENE_WIDTH = 600
  val SCENE_HEIGHT = 500
  val OPTIONS_HEIGHT = 40
  val CANVAS_HEIGHT = SCENE_HEIGHT - OPTIONS_HEIGHT
  val CANVAS_WIDTH = SCENE_WIDTH

  // Other
  val CLUSTER_SIZE = 2000
  val PARTICLE_RADIUS = 4
  val PARTICLE_COLORS = Array(Color.BlueViolet)
    //Array(Color.Purple, Color.Blue, Color.Green, Color.Yellow, Color.Orange, Color.Red)

  // Create Cluster and CanvasCluster (from Cluster)
  val cluster = new Cluster(CANVAS_WIDTH, CANVAS_HEIGHT, CLUSTER_SIZE, PARTICLE_RADIUS, PARTICLE_COLORS)
  val clusterCanvas: ClusterCanvas = new CCBuilder().withHeight(CANVAS_HEIGHT).withWidth(CANVAS_WIDTH)
    .withCluster(cluster).withColors(PARTICLE_COLORS).build

  // Canvas Properties
  clusterCanvas.widthProperty().addListener(e => clusterCanvas.redraw())
  clusterCanvas.heightProperty().addListener(e => clusterCanvas.redraw())
  //clusterCanvas.margin = Insets(10,10,10,10)

  // Create Stage & Scene (with scene components)

  stage = new PrimaryStage {
    title = "Particle Cluster (using Canvas)"
  }

  // Create option controls

  //val pCountSlider:Slider = new Slider(10,2000,500)
  //val resetButton:Button = new Button("Reset")
  val optionsPane = new HBox() {

    // Settings

    spacing = 10
    //prefHeight = OPTIONS_HEIGHT

    // Contents

    val timerButtonLabel: Label = new Label("Timer: ")
    val timerButton: Button = new Button("Stop")
    timerButton.setOnAction(action => {
      if (timerButton.text.value.equals("Stop")) {
        timerButton.text = "Start"
        clusterCanvas.timer.stop()
      } else if (timerButton.text.value.equals("Start")) {
        timerButton.text = "Stop"
        clusterCanvas.timer.start()
      }
    })

    val speedSliderLabel: Label = new Label("Speed: ")
    val speedSlider: Slider = new Slider(0, 1200, 200)
    cluster.bindParticleSpeedTo(speedSlider.value)
    alignment = Pos.CenterLeft

    // Add Contents

    children.addAll(
      timerButton,
      speedSliderLabel,
      speedSlider
    )

    println(timerButton.width)
  }

  println(optionsPane.height.value)
  //optionsPane.width <== stage.width

  stage.scene = new Scene(clusterCanvas.width.value, clusterCanvas.height.value + optionsPane.height.value) {

    root = new BorderPane() {
      center = clusterCanvas
      bottom = optionsPane

      clusterCanvas.width <== this.width
      clusterCanvas.height <== (this.height - optionsPane.height)
    }

  }   // TODO Figure out why some particles are spawning slightly lower than they should be


}
