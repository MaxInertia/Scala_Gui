package main.objects

import scalafx.beans.binding.NumberExpression
import scalafx.beans.property.DoubleProperty

/**
  * Created by Dorian Thiessen on 2018-03-29.
  */
class Cluster {
  val lowerXBound: Double = 0
  val lowerYBound: Double = 0
  val upperXBound: DoubleProperty = DoubleProperty(0)
  val upperYBound: DoubleProperty = DoubleProperty(0)
  def bindWidthTo(doubleProperty: NumberExpression): Unit =
    upperXBound <== doubleProperty
  def bindHeightTo(doubleProperty: NumberExpression): Unit =
    upperYBound <== doubleProperty
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
}
