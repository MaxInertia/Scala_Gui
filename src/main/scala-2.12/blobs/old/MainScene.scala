package blobs.old

import scalafx.Includes._
import scalafx.animation.AnimationTimer
import scalafx.scene.Scene
import scalafx.scene.input.MouseEvent
import scalafx.scene.shape.Circle

/**
  * Created by MaxInertia on 2017-03-31.
  */
object MainScene extends Scene(800,800){
  val cRad = 3
  val blobCluster = new BlobCluster(1000, 1000, cRad, width.value.asInstanceOf[Int], height.value.asInstanceOf[Int])
  val blobs:Array[Circle] = blobCluster.blobs
  var mouseX: Double = 0
  var mouseY: Double = 0
  var sign = 1

  // Keep track of mouse position
  def mouseOps(): Unit = {
    onMouseMoved = (m: MouseEvent) => {
      mouseX = m.getX
      mouseY = m.getY
    }

    // Mouse click changes sign of attraction
    onMouseClicked = (m: MouseEvent) => if(sign>0) sign = -2*sign else sign = -sign/2
  }

  // Adjust allowable positions of Blobs on resize
  def onResize(): Unit = {
    width.onChange( (source, oldValue, newValue) => blobCluster.windowWidth = newValue.doubleValue())
    height.onChange((source, oldValue, newValue) => blobCluster.windowHeight = newValue.doubleValue())
  }

  //width.delegate <== blobCluster.windowWidth

  // Define the animation timer
  def animationSetup(): Unit = {
    var lastTime = 0L
    val blobSpeed = 300 // Pixels per Second
    val timer = AnimationTimer(t => {
      if (lastTime > 0) {
        val delta = (t - lastTime) / 1e9
        def moveBlobs(): Unit = {
          for (b <- blobs) {
            val dx = b.centerX.value - mouseX
            val dy = b.centerY.value - mouseY
            val dist2Mouse = math.sqrt(dx * dx + dy * dy)

            //println(b.fill.toString())
            val charge = if(b.fill.value.toString.equals("0x0000ffff")) -sign else sign
            //val charge = if(b.fill.value.equals(Color.Red)) sign else -sign
            val newX = b.centerX.value - charge * (dx / dist2Mouse) * blobSpeed * delta
            val newY = b.centerY.value - charge * (dy / dist2Mouse) * blobSpeed * delta
            if(PositionChecker.isValid(blobCluster, newX, newY, cRad, b)) {
              b.centerX = newX
              b.centerY = newY
            }
          }
        }
        moveBlobs()
      }
      lastTime = t
    })
    timer.start()
  }

  content = blobs
  animationSetup()
  mouseOps()
  onResize()
}
