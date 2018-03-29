package main.objects

import display._

import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color

class Particle(val rad: Double, val pos: Position, val color: Color = Color.Black) extends Boundaries {
  def top: Double = pos.y - rad
  def bottom: Double = pos.y + rad
  def right: Double = pos.x + rad
  def left: Double = pos.x - rad

  def isInside(x: Double, y: Double): Boolean = {
    val dx = math.abs(x - pos.x)
    val dy = math.abs(y - pos.y)
    val distance = math.sqrt(dx * dx + dy * dy)
    if (distance <= rad) true
    else false
  }
}

object Particle {
  implicit object ParticleIsViewable extends Viewable[Particle]  {
    override def draw(p: Particle, gc: GraphicsContext): Unit = {
      gc.setFill(p.color)
      gc.fillOval(p.pos.x - p.rad, p.pos.y - p.rad, p.rad*2, p.rad*2)
    }
  }

  implicit object ParticleIsTemporal extends Temporal[Particle]{
    override def update(o: Particle): Unit = {}
  }
}

class SinParticle(override val rad: Double, center: Position, amp: Double, freq: Double)
  extends Particle(rad, Position(center.x, center.y)){
  def getColor: Color = color

  private var t: Double = 0
  def move(): Unit = {
    val angle = t * math.Pi
    t += 0.01
    pos.x = center.x + amp * math.sin(angle * freq)
    pos.y = center.y + amp * math.cos(angle * freq)
  }
}

object SinParticle {

  def apply(rad: Double, center: Position, amp: Double, freq: Double): SinParticle = new SinParticle(rad, center, amp, freq)

  implicit object SinParticleIsViewable extends Viewable[SinParticle]  {
    override def draw(p: SinParticle, gc: GraphicsContext): Unit = {
      gc.setFill(p.color)
      gc.fillOval(p.pos.x - p.rad, p.pos.y - p.rad, p.rad*2, p.rad*2)
    }
  }

  implicit object SinParticleIsTemporal extends Temporal[SinParticle]{
    override def update(o: SinParticle): Unit = {
      o.move()
    }
  }

  def genPair(amp: Double, freq: Double, position: Position = Position(300,300)): (SinParticle, SinParticle) = {
    val p1: SinParticle = SinParticle(5, position, amp, freq)
    val p2: SinParticle = SinParticle(5, position, amp, -freq)
    (p1 ,p2)
  }

  def genMany(freq: Double): Array[SinParticle] = {
    var ps: Array[SinParticle] = Array()
    val c = 300
    for(amp <- 10 to 100 by 10) {
      ps = ps :+ SinParticle(5, Position(150,150), amp, 100.0/amp)
    }
    ps
  }

  def genManyPairs(freq: Double): Array[(SinParticle, SinParticle)] = {
    var ps: Array[(SinParticle, SinParticle)] = Array()
    val c = 300
    for(amp <- 10 to 100 by 10) {
      val f = if(amp%20==0) freq else -freq
      ps = ps :+ genPair(amp, 100.0/amp)
    }
    ps
  }
}
