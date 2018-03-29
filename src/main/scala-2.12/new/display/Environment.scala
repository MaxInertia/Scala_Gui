package display

import scalafx.beans.binding.NumberExpression
import scalafx.beans.property.DoubleProperty
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.paint.Color

class Environment[Object : Viewable]() {
  var contents: List[Object] = List()
  def addObject(o: Object): Unit =
    contents = o +: contents

  /*val lowerXBound: Double = 0
  val lowerYBound: Double = 0
  val upperXBound: DoubleProperty = DoubleProperty(0)
  val upperYBound: DoubleProperty = DoubleProperty(0)
  upperXBound <== Graphics.width
  upperYBound <== Graphics.height*/

  private[display] def bindWidthTo(doubleProperty: NumberExpression): Unit =
    Graphics.width <== doubleProperty
  private[display] def bindHeightTo(doubleProperty: NumberExpression): Unit =
    Graphics.height <== doubleProperty

  //TODO: What is one unit of time? Be more specific?
  /** Age each object in the environment by one unit of time.
    * NOTE: Only callable if there is an implicit instance of Temporal
    *       for the contents of the environment in scope.
    * @param ev Specifies how the object changes over time
    */
  def tick()(implicit ev: Temporal[Object]): Unit = {
    @annotation.tailrec
    def loop(items: List[Object]): Unit = {
      if(items.isEmpty) return
      ev.update(items.head)
      loop(items.tail)
    }
    loop(contents)
  }

  /** Request Graphics to draw the environment */
  def draw(): Unit = Graphics.redraw(contents)

  /** Canvas accessor
    * @return The Canvas for this environment */
  def getCanvas: Canvas = this.Graphics

  /**
    * Inner object that handles drawing
    */
  protected object Graphics extends Canvas(0, 0) {
    def redraw(viewable: List[Object]): Unit = {
      val ev = implicitly[Viewable[Object]]
      val gc: GraphicsContext = graphicsContext2D
      @annotation.tailrec
      def loop(items: List[Object]): Unit = {
        if(items.isEmpty) return
        ev.draw(items.head, gc)
        loop(items.tail)
      }
      clear()
      loop(viewable)
    }

    private def clear(): Unit = {
      val gc: GraphicsContext = graphicsContext2D
      gc.save()
      gc.setFill(Color.Black)
      gc.fillRect(0, 0, width.value, height.value)
      gc.setFill(Color.White)
      gc.fillRect(5, 5, width.value - 10, height.value - 10)
      gc.restore()
    }
  }
}
