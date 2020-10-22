package hello.world
package data

import io.circe._
import io.circe.syntax._
import java.util.UUID

final case class Tasks(tasks: List[(UUID, Task)]) {
  def add(t: (UUID, Task)): Tasks =
    Tasks(tasks :+ t)
 }

object Tasks {
  val empty: Tasks = Tasks(List.empty)

  val elementDecoder = new Decoder[(UUID, Task)] {
    def apply(c: HCursor): Decoder.Result[(UUID, Task)] =
      for {
        id <- c.downField("id").as[UUID]
        task <- c.downField("task").as[Task]
      } yield (id, task)
  }

  implicit val tasksCodec: Codec[Tasks] = new Codec[Tasks] {
    def apply(c: HCursor): Decoder.Result[Tasks] = 
      c.as(Decoder.decodeList(elementDecoder)).map(t => Tasks(t))
  

    def apply(t: Tasks): Json = {
      Json.arr(
        t.tasks.map {
          case (id, task) =>
            Json.obj("id" -> id.asJson, "task" -> task.asJson)
        }: _*
      )
    }
  }
}
