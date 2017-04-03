package visualization

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.paint.{Color, PhongMaterial}
import scalafx.scene.shape.{DrawMode, Sphere}
import scalafx.scene.transform.{Rotate, Translate}
import scalafx.scene._
import javafx.scene.{Scene => jfxScene}

/**
  * Created by MaxInertia on 2017-04-01.
  */
class Plotter {

}

class Plotter2D {

}

object Plotter3D {

  def createContent(resolution: Int, wirePlusSolid:Boolean): Group = {
    val root: Group = new Group()

    if(wirePlusSolid) {
      val sphere = new Sphere(200, resolution)
      sphere.setMaterial(new PhongMaterial(Color.LightBlue))
      sphere.setDrawMode(DrawMode.Fill)
      root.getChildren.add(sphere)
    }

    val sphere = new Sphere(200, resolution)
    sphere.setMaterial(new PhongMaterial(Color.White))
    sphere.setDrawMode(DrawMode.Line)

    root.getChildren.add(sphere)
    root
  }

}

object Launcher3D extends JFXApp {

  val group = Plotter3D.createContent(100,true)

  // Create Camera
  val camera:PerspectiveCamera = new PerspectiveCamera(true)
  // Position Camera
  camera.getTransforms.addAll(
    new Rotate(-12, Rotate.YAxis),
    new Rotate(-20, Rotate.XAxis),
    new Translate(-200,-200,-15)
  )
  // Add camera as node to scene graph
  group.getChildren.add(camera)

  val myScene:jfxScene = new jfxScene(group, 400, 400)
  myScene.setCamera(camera)

  stage = new PrimaryStage{
    scene = new Scene(myScene)
  } // end of Stage


}