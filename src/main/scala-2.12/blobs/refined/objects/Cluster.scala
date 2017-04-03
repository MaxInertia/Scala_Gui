package blobs.refined.objects

import java.util.concurrent.ThreadLocalRandom

import scala.util.Random
import scalafx.beans.property.DoubleProperty
import scalafx.scene.paint.Color

/**
  * Cluster is a collection of Particles.
  *
  * @author Created by MaxInertia on 2017-03-31.
  *
  * @param containerWidth   The width of the parent Node
  * @param containerHeight  The height of the parent Node
  * @param clusterSize      The number of particles in the Cluster
  * @param particleRadius   The radius of each particle in the Cluster
  * @param particleColors   The colors in the Cluster
  */
private[refined] class Cluster(containerWidth: Double,
              containerHeight: Double,
              clusterSize: Int = 10,
              particleRadius: Double = 20,
              particleColors: Array[Color] = Array(Color.Gray)) {

  // == Fields ==

  val lowerXBound: Double = particleRadius
  val lowerYBound: Double = particleRadius
  val upperXBound: DoubleProperty = DoubleProperty(containerWidth - particleRadius)
  val upperYBound: DoubleProperty = DoubleProperty(containerHeight - particleRadius)

  def getParticleRadius: Double = this.particleRadius
  val speedProperty: DoubleProperty = DoubleProperty(300)
  val particles: Array[Particle] = createParticles()

  var movesX:Int = 0
  var movesY:Int = 0

  // == Methods ==

  /**
    * Binds the boundaries of the particles motion to the dimensions of the container the particles will be in.
    * @param widthProperty    Width property of the container
    * @param heightProperty   Height propert of the container
    */
  def bindBoundariesToDimensionProperties(widthProperty: DoubleProperty, heightProperty: DoubleProperty): Unit = {
    //upperXBound <== widthProperty - particleRadius
    //upperYBound <== heightProperty - particleRadius
  }

  /**
    * Binds the |speedProperty| of the particles to another DoubleProperty.
    * When the value of |valueProperty| changes, so will the speed of the particles.
    * @param valueProperty Property to bind the particle speed to
    */
  def bindParticleSpeedTo(valueProperty: DoubleProperty): Unit = speedProperty <== valueProperty

  /**
    * Checks if the position, specified by |x| and |y|, results in particles overlapping.
    * @param x              The x-coordinate
    * @param y              The y-coordinate
    * @param refParticle    The circle at the provided coordinates (if it exists)
    * @return               True if the position is not blocked by any particles or boundaries, else false
    */
  def isValidPosition(x: Double, y: Double, refParticle: Particle = null, particles:Array[Particle] = particles): Boolean = {

    def withinBounds(): Boolean = {
      if(x <= upperXBound.value && x >= lowerXBound &&
        y <= upperYBound.value && y >= lowerYBound) true
      else false
    }

    def isOverlapping(p: Particle): Boolean = {
      val dx = math.abs(p.getX - x)
      val dy = math.abs(p.getY - y)
      val distance = math.sqrt(dx * dx + dy * dy)
      if (!p.equals(refParticle) && distance <= 2*particleRadius) true
      else false
    }

    withinBounds() /* Checks if inside window*/ &&
      !particles.exists(p => isOverlapping(p)) /* Checks if overlapping with any other blobs */
  }

  def move(newX: Double, newY: Double, refP: Particle): Unit = {
    def withinBounds(): Boolean = {
      if(newX <= upperXBound.value && newX >= lowerXBound &&
        newY <= upperYBound.value && newY >= lowerYBound) true
      else false
    }

    //Helper method
    def separationDistance(x1: Double, y1: Double, x2: Double, y2: Double): Double = {
      val dx = math.abs(x2 - x1)
      val dy = math.abs(y2 - y1)
      math.sqrt(dx * dx + dy * dy)
    }

    def isOverlapping(p: Particle): Boolean = {
      val distance = separationDistance(p.getX, p.getY, newX, newY)
      if (!p.equals(refP) && distance <= 2*particleRadius) true
      else false
    }

    if(withinBounds()) {
      var blockers: Array[Particle] = Array()
      for (p <- particles if isOverlapping(p))  blockers = blockers :+ p

      if(blockers.length == 1) {
        val p:Particle = blockers(0)

        if (math.abs(p.getX - newX) > 2 *particleRadius) {
          p.xCoord = newX
          movesX += 1 }
        else if (math.abs(p.getY - newY) > 2 *particleRadius) {
          p.yCoord = newY
          movesY += 1 }

      } else if(blockers.length == 0) {
        refP.xCoord = newX
        refP.yCoord = newY
      }
    }


  } // end of move()




  /**
    * Checks if the position, specified by |x| and |y|, results in particles overlapping.
    * @param x              The x-coordinate
    * @param y              The y-coordinate
    * @param refParticle    The circle at the provided coordinates (if it exists)
    * @return               True if the position is not blocked by any particles or boundaries, else false
    */
  def move_accel(x: Double, y: Double, vX: Double, vY: Double, refParticle: Particle): Unit = {

    def withinBounds(): Boolean = {
      if (x <= upperXBound.value && x >= lowerXBound && y <= upperYBound.value && y >= lowerYBound)
        true
      else false
    }

    def isOverlapping(p: Particle): Boolean = {
      val dx = math.abs(p.getX - x)
      val dy = math.abs(p.getY - y)
      val distance = math.sqrt(dx * dx + dy * dy)
      if (!p.equals(refParticle) && distance <= 2 * particleRadius) true
      else false
    }

    if(!withinBounds()) {
      if (x < lowerXBound || x > upperXBound.value) refParticle.velocityX = -vX
      if (y < lowerYBound || y > upperYBound.value) refParticle.velocityY = -vY
    }
    /*} else if( !particles.exists(p => isOverlapping(p)) ) {
      refParticle.velocityX = vX // delta is time interval from now to last move
      refParticle.velocityY = vY
      refParticle.xCoord = x
      refParticle.yCoord = y
      refParticle.stuck = false
    } else {
      refParticle.stuck = true
    }*/

    var blocked:Boolean = false
    for(p <- particles if isOverlapping(p)) {
      blocked = true
      if(p.stuck) refParticle.stuck = true
    }

    if(!blocked) {
      refParticle.velocityX = vX
      refParticle.velocityY = vY
      refParticle.xCoord = x
      refParticle.yCoord = y
    } else {
      refParticle.velocityX = 0
      refParticle.velocityY = 0
    }
  }





  /*def touchCancellation(): Unit = {
    for(i <- particles.indices) {
      val currentP =  particles(i)
      val dx = math.abs(currentP.getX - x)
      val dy = math.abs(currentP.getY - y)
      val distance = math.sqrt(dx * dx + dy * dy)
      if (!currentP.equals(refParticle) && distance <= 2*particleRadius) true
      else false
    }
  }*/

  /**
    * Populates the cluster with |particleCount| Particles.
    * @return   The Array of |n| Particles
    */
  private def createParticles(): Array[Particle] = { 
    // TODO Make method tail-recursive
    var tempPoints: Array[Particle] = Array[Particle]()

    for (i <- 1 to clusterSize) {
      var x: Double = 0
      var y: Double = 0

      do {
        //x = ((Random.nextDouble() * Boundaries.upperX.value - 2 * particleRadius) + particleRadius // TODO Change to this form
        x = ((Random.nextDouble() * 1000) % upperXBound.value - particleRadius) + particleRadius
        y = ((Random.nextDouble() * 1000) % upperYBound.value - particleRadius) + particleRadius
      } while (!isValidPosition(x, y, particles = tempPoints))//particles = tempParticles))


      val colorCount = particleColors.length
      tempPoints = tempPoints :+ new Particle(
        xCoord = x,
        yCoord = y,
        color = if (colorCount > 1) particleColors(ThreadLocalRandom.current().nextInt(0, colorCount))
        else particleColors(0)
      )
    }

    tempPoints
  }

}

class Particle(var xCoord: Double, var yCoord: Double, var color:Color = Color.Gray) {
  var velocityX: Double = 0
  var velocityY: Double = 0
  var stuck:Boolean = false

  def getX: Double = xCoord
  def getY: Double = yCoord
  def getColor: Color = color

  def setX(newX: Double): Unit = {xCoord = newX}
  def setY(newY: Double): Unit = {yCoord = newY}
  def setColor(color: Color): Unit = {
    this.color = color
  }
}