package display

import scalafx.animation.AnimationTimer

object Time {
  type PeriodicAction = Long => Unit
  class PeriodicEvent(event: PeriodicAction) {
    private var running: Boolean = false
    private val timer: AnimationTimer = AnimationTimer(t => event(t))

    def start(): Unit = {
      timer.start()
      running = true
    }

    def stop(): Unit = {
      running = false
      timer.stop()
    }

    def isRunning: Boolean = running
  }
}
