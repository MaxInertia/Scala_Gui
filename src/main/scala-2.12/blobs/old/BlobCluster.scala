package blobs.old

import scala.util.Random
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

/**
  * Created by MaxInertia on 2017-03-31.
  */
class BlobCluster(redCount:Int, blueCount:Int, radius:Int, var windowWidth:Double, var windowHeight:Double) {
  var blobs: Array[Circle] = Array()
  def getWindowWidth: Double = windowWidth
  def getWindowHeight: Double = windowHeight
  def setWindowWidth(value: Double): Unit = windowWidth = value
  def setWindowHeight(value: Double): Unit = windowHeight = value

  private def setup(n:Int, color:Color) {
    for (i <- 1 to n) {
      var x: Double = 0
      var y: Double = 0
      do {
        x = ((Random.nextDouble() * 1000) % windowHeight - 2 * radius) + radius
        y = ((Random.nextDouble() * 1000) % windowHeight - 2 * radius) + radius
      } while (!PositionChecker.isValid(this, x, y, radius))

      val circle = Circle(x, y, radius)
      circle.fill = color
      circle.stroke = Color.Black
      blobs = blobs :+ circle
    }
  }

  setup(redCount, Color.Red)
  setup(blueCount, Color.Blue)
}



/**
  * Used to check if position of Blob is valid
  */
object PositionChecker {
  def isValid(cluster: BlobCluster, x:Double, y:Double, radius:Int, circle:Circle = null): Boolean = {
    def insideWindow(windowWidth:Double, windowHeight:Double): Boolean = {
      if(x <= windowWidth-radius && x >= radius && y <= windowHeight-radius && y >= radius) true
      else{
        //println("Outside window... x: "+x+", y: "+y)
        false
      }
    }
    def isOverlapping(c: Circle): Boolean = {
      val dx = math.abs(c.centerX.value - x)
      val dy = math.abs(c.centerY.value - y)
      val distance = math.sqrt(dx * dx + dy * dy)
      if (!c.equals(circle) && distance <= 2*radius) true
      else{
        //println("Overlapping")
        false
      }
    }

    insideWindow(cluster.getWindowWidth, cluster.getWindowHeight) && // Checks if inside window
      !cluster.blobs.exists(otherBlob => isOverlapping(otherBlob)) // Checks if overlapping with any other blobs
  }
}

object Testing extends App{
  //val blob = new BlobCluster(30,1,600,600)

  var min:Double = 300
  var max:Double = min
  var sum:Double = 0
  for(i<- 1 to 10000){
    val rand = ((Random.nextDouble()*1000)%560)+20
    if(rand<min) min = rand
    if(rand> max) max = rand
    sum = sum + rand
  }

  println("Min: "+min+", Max: "+max+", Mean: "+(sum/10000))

  // println(((Random.nextDouble()*1000)%560) + 20)
}
