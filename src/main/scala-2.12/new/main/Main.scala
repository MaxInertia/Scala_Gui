package main

import java.util.concurrent.Executors

import display._
import main.objects.{Particle, SinParticle}

import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.Includes._

/**
  * Created by Dorian Thiessen on 2018-03-28.
  */
object Main extends App {
  def setupEnvironments(): Unit = {

    def addStuff(): Unit = {
      var env: Environment[Particle] = new Environment[Particle]()

      val p = new Particle(20, Position(100,100), Color.Blue)
      env.addObject(p)

      var pIn: Boolean = false
      var pBound: Boolean = false

      var mouseX: Double = 0
      var mouseY: Double = 0
      env.getCanvas.onMouseMoved = (m: MouseEvent) => {
        mouseX = m.getX
        mouseY = m.getY
        if(pBound) {
          p.pos.x = mouseX
          p.pos.y = mouseY
        }
      }
      env.getCanvas.onMouseClicked = (m: MouseEvent) => {
        println(s"Mouse press at ($mouseX, $mouseY)")
        if(!pBound && p.isInside(mouseX, mouseY)) {
          pBound = true
          println("Point bound")
        }
        else if(pBound) {
          pBound = false
          println("Point unbound")
        } else {
          println(s"Point location: (${p.pos.x}, ${p.pos.y})")
        }
      }

      //addEnvironment(env, i)
      Storage.addEnv(env)

      // Setup time
      val time: Time.PeriodicEvent = new Time.PeriodicEvent(t => {
        env.tick()
        env.draw()
      })
      time.start()
    }

    def listEnv(): Unit = {
      import ViewableImplicits._
      val listEnv: Environment[List[_]] = new Environment[List[_]]()
      //val numList: List[Int] = Storage.getData.asInstanceOf[List[Int]]
      val numList: List[Int] = List(1, 2, 3, 4)
      listEnv.addObject(numList)
      //addEnvironment(listEnv, 0)
      listEnv.draw()
    }

    def orbitEnv(): Unit = {
      import SinParticle._
      val env: Environment[SinParticle] = new Environment[SinParticle]()
      val ps: Array[SinParticle] = SinParticle.genMany(1.0)
      for (p <- ps) env.addObject(p)
      Storage.addEnv(env)

      val time: Time.PeriodicEvent = new Time.PeriodicEvent(t => {
        env.tick()
        env.draw()
      })
      time.start()
    }
    def orbitEnv2(): Unit = {
      import SinParticle._
      val env: Environment[SinParticle] = new Environment[SinParticle]()
      var ps: Array[SinParticle] = Array()

      val center = Position(150,150)
      val c = 300
      var sign = 1
      for(amp <- 10 to 100 by 10) {
        ps = ps :+ SinParticle(5, center, amp, 100.0/amp*sign)
        sign = -1*sign
      }

      var pIn: Boolean = false
      var pBound: Boolean = false

      var mouseX: Double = 0
      var mouseY: Double = 0
      env.getCanvas.onMouseMoved = (m: MouseEvent) => {
        mouseX = m.getX
        mouseY = m.getY
        if(pBound) {
          center.x = mouseX
          center.y = mouseY
        }
      }
      def distFrom(x: Double, y: Double, pos: Position): Double = math.sqrt(
        math.pow(x - pos.x, 2) + math.pow(y - pos.y, 2)
      )
      env.getCanvas.onMouseClicked = (m: MouseEvent) => {
        println(s"Mouse press at ($mouseX, $mouseY)")
        if(!pBound && distFrom(mouseX, mouseY, center) < 100) {
          pBound = true
          println("Cluster bound")
        }
        else if(pBound) {
          pBound = false
          println("Cluster unbound")
        } else {
          println(s"Cluster location: (${center.x}, ${center.y})")
        }
      }

      val time: Time.PeriodicEvent = new Time.PeriodicEvent(t => {
        env.tick()
        env.draw()
      })
      time.start()

      for(p <- ps) env.addObject(p)
      Storage.addEnv(env)
    }
    def clusterEnv(): Unit = {

      val env: Environment[] = new Environment[]()
      val ps: Array[] = SinParticle.genMany(1.0)
      for (p <- ps) env.addObject(p)
      Storage.addEnv(env)

      val time: Time.PeriodicEvent = new Time.PeriodicEvent(t => {
        env.tick()
        env.draw()
      })
      time.start()
    }
    //addStuff()
    //orbitEnv()
    orbitEnv2()
    //addStuff()
  }
  def startSFX(): Unit = {
    val pool = Executors.newFixedThreadPool(1)
    println("Before pool.submit")
    pool.submit(new Runnable {
      override def run(): Unit = {
        Launcher.main(Array())
        pool.shutdown()
      }
    })
    println("After pool.submit")
  }

  setupEnvironments()
  startSFX()
}