package display

/**
  * Created by Dorian Thiessen on 2018-03-28.
  */
object Storage {
  private[display] var environments: Array[Environment[_]] = Array()
  def addEnv[T](env: Environment[T]): Unit = environments = environments :+ env
}
