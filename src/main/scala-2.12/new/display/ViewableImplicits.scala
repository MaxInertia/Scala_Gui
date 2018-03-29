package display

import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color

/**
  * Created by Dorian Thiessen on 2018-03-28.
  */
object ViewableImplicits {

  implicit object ListIsViewable extends Viewable[List[_]] {
    override def draw(o: List[_], gc: GraphicsContext): Unit = {
      def boxUp(item: String, x: Double, y: Double): Unit = {
        gc.strokeText(item, x, y)
        gc.strokeRect(x-4, y-12, 15, 15)
        gc.strokeLine(x+11, y-6, x+41, y-6)
      }

      var x = 20
      var y = 40
      gc.setStroke(Color.Black)
      for(el <- o) {
        boxUp(el.toString, x, y)
        x += 50
      }
    }
  }

}