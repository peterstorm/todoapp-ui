package hello.world
package data

import io.circe._
import io.circe.syntax._

final case class Tasks(tasks: List[Task]) {
  def add(t: Task): Tasks =
    Tasks(tasks :+ t)
 }

object Tasks {
  val empty: Tasks = Tasks(List.empty)

  implicit val tasksCodec: Codec[Tasks] = new Codec[Tasks] {
    def apply(c: HCursor): Decoder.Result[Tasks] =
      c.as[List[Task]].map(task => Tasks(task))

    def apply(t: Tasks): Json = {
      Json.arr(
        t.tasks.map {
          task => Json.obj("task" -> task.asJson)
        }: _*
      )
    }
  }
}
