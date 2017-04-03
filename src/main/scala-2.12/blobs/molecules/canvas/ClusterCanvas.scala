package blobs.molecules.canvas

import blobs.molecules.objects
import blobs.molecules.objects.Cluster

import scalafx.Includes._
import scalafx.animation.AnimationTimer
import scalafx.beans.property.DoubleProperty
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color

/**
  * A canvas containing a cluster of particles that follow the mouse cursor.
  *
  * @param builder A builder that contains each of the required values
  */
class ClusterCanvas(builder: CCBuilder) extends Canvas(builder.containerWidth, builder.containerHeight) {
  require(!builder.cluster.equals(null))
  require(builder.containerHeight>0)
  require(builder.containerWidth>0)

  // == Fields ==

  val cluster: Cluster = builder.cluster
  val particles: Array[objects.Particle] = cluster.particles
  val colors:Array[Color] = builder.colors

  var lastTime = 0L /* Last time animation ticked */
  val particleSpeed = 300 /* Pixels per Second */
  var timer:AnimationTimer = _
  var mouseX: Double = 0
  var mouseY: Double = 0
  var sign = 1

  var clickedX: Double = _
  var clickedY: Double = _

  val frameTimes: Array[Double] = new Array[Double](200)
  var lastUpdate:Long = _
  var frameIndex:Int = 0

  // == Constructor ==

  constructor()
  def constructor(): Unit = {

    // Keep track of mouse position
    onMouseMoved = (m: MouseEvent) => { mouseX = m.getX; mouseY = m.getY }
    // Mouse click changes sign and magnitude of attraction
    //onMouseClicked = (m: MouseEvent) => if (sign > 0) sign = -2 * sign else sign = -sign / 2
    /* // MousePress and Release listeners for drawing lines
    onMousePressed = (m: MouseEvent) => {
      clickedX = m.getX
      clickedY = m.getY
    }

    onMouseReleased = (m: MouseEvent) => {
      val secondX = m.getX
      val secondY = m.getY
      if(0<secondX && secondX<width.value && 0<secondY && secondY<height.value) {
        graphicsContext2D.strokeLine(clickedX, clickedY, secondX, secondY)
      }
    }*/

    //cluster.bindBoundariesToDimensionProperties(super.height, super.width)
    cluster.upperXBound <== super.width - cluster.getParticleRadius
    cluster.upperYBound <== super.height - cluster.getParticleRadius

    // Define the animation timer
    timer = createTimer()
    timer.start()
  }

  // == Methods ==

  def createTimer():  AnimationTimer = {
    val graphicsContext:GraphicsContext = graphicsContext2D

    val timer = AnimationTimer( t => {
      if (lastTime > 0) {

        def clusterMotion(): Unit = {
          redraw()
          val delta = (t - lastTime) / 1e9

          //Measuring Frame Rate
          tickFPS(t)

          def motionLoop() {
            for (p <- particles) {
              val dx = p.getX - mouseX
              val dy = p.getY - mouseY
              val dist2Mouse = math.sqrt(dx * dx + dy * dy)
              // Calculate new positions, Moves if not blocked
              val newX = p.getX - sign * (dx / dist2Mouse) * cluster.speedProperty.value * delta
              val newY = p.getY - sign * (dy / dist2Mouse) * cluster.speedProperty.value * delta
              if (cluster.isValidPosition(newX, newY, p)) {
                p.setX(newX)
                p.setY(newY)
              }

              val radius = cluster.getParticleRadius
              graphicsContext.fill = p.getColor
              graphicsContext.fillOval(p.getX-radius, p.getY-radius, 2*radius, 2*radius)
              if(radius>3) graphicsContext.strokeOval(p.getX-radius, p.getY-radius, 2*radius, 2*radius)
            }
          }

          def motionLoop_45d() {
            for (p <- particles) {
              val dx = p.getX - mouseX
              val dy = p.getY - mouseY
              val dist2Mouse = math.sqrt(dx * dx + dy * dy)
              // Calculate new positions, Moves if not blocked
              val newX = p.getX - sign * (dx / dist2Mouse) * cluster.speedProperty.value * delta
              val newY = p.getY - sign * (dy / dist2Mouse) * cluster.speedProperty.value * delta
              cluster.move(newX, newY, p)

              val radius = cluster.getParticleRadius
              graphicsContext.fill = p.getColor
              graphicsContext.fillOval(p.getX-radius, p.getY-radius, 2*radius, 2*radius)
              if(radius>3) graphicsContext.strokeOval(p.getX-radius, p.getY-radius, 2*radius, 2*radius)
            }
          }

          val AccelConst: DoubleProperty = DoubleProperty(0.1)

          def motionLoop_accel() {
            for (p <- particles) {

              // Distance
              val distX = mouseX - p.getX
              val distY = mouseY - p.getY
              val dist2Mouse = math.sqrt(distX * distX + distY * distY)

              // Acceleration
              val accel = AccelConst.value / math.sqrt(dist2Mouse) //(dist2Mouse*dist2Mouse)
              val accelX = if(distX>0) accel*math.cos(distX/dist2Mouse) else -accel*math.cos(distX/dist2Mouse)
              val accelY = /*if(distY>0)*/ accel*math.sin(distY/dist2Mouse) //else -accel*math.sin(distY/dist2Mouse)
              //println("aX: "+accelX+", aY: "+accelY)

              // Calculate new positions, Moves if not blocked
              val newX = p.xCoord + p.velocityX + accelX/2
              val newY = p.yCoord + p.velocityY + accelY/2
              //println(p.xCoord+" -> "+newX + " ... Delta: "+delta)

              cluster.move_accel(newX, newY, p.velocityX+accelX, p.velocityY+accelY, p)

              // Draw Particle
              val radius = cluster.getParticleRadius
              graphicsContext.fill = if(p.stuck) Color.LightBlue else p.getColor
              graphicsContext.fillOval(p.getX - radius, p.getY - radius, 2 * radius, 2 * radius)
              if (radius > 3) graphicsContext.strokeOval(p.getX - radius, p.getY - radius, 2 * radius, 2 * radius)
            }
          }
          //motionLoop()
          //motionLoop_45d()
            motionLoop_accel()
        }

        graphicsContext.save()
        clusterMotion()
        graphicsContext.restore()
      }

      lastTime = t
    })

    timer
  }

  def tickFPS(now: Long): Unit = {
    if(frameIndex == frameTimes.length) {
      val frameRate = frameTimes.length*1000 / (frameTimes(frameTimes.length - 1) - frameTimes(0))
      println("Frame Rate: " + frameRate)
      //println("extra moves in x: "+cluster.movesX)
      //println("extra moves in y: "+cluster.movesY)
      frameIndex = 0
    }
    if(lastUpdate > 0) {
      frameTimes(frameIndex) = System.currentTimeMillis()
      frameIndex+=1
    }
    lastUpdate = now
  }

  def redraw(): Unit = {
    val gc:GraphicsContext = graphicsContext2D
    gc.save()
    gc.setFill(Color.White)
    gc.fillRect(0, 0, super.width.value, super.height.value)
    gc.restore()
  }

}

/**
  * Builder class for the Canvas Cluster
  */
class CCBuilder{

  var colors: Array[Color] = Array(Color.Gray)
  var cluster: Cluster = _
  var containerWidth: Double = _
  var containerHeight: Double = _

  // == Methods ==

  def withCluster(cluster: Cluster): CCBuilder = {
    this.cluster = cluster
    this
  }

  def withWidth(containerWidth: Double): CCBuilder = {
    this.containerWidth = containerWidth
    this
  }

  def withHeight(containerHeight: Double): CCBuilder = {
    this.containerHeight = containerHeight
    this
  }

  def withColors(colors: Array[Color]): CCBuilder = {
    this.colors = colors
    this
  }

  def build: ClusterCanvas = new ClusterCanvas(this)

}


