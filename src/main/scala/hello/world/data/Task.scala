package hello.world
package data

import io.circe._
import io.circe.syntax._
import java.util.UUID

final case class Task(
                     id: UUID,
                     state: State,
                     description: String,
                     notes: Option[String],
                     tags: List[Tag]
                     ) {
  def uncomplete: Task =
    this.copy(state = Active)
}

object Task {
  implicit val taskCodec: Codec[Task] = new Codec[Task] {
    def apply(c: HCursor): Decoder.Result[Task] =
      for {
        id <- c.downField("id").as[UUID]
        state <- c.downField("state").as[State]
        description <- c.downField("description").as[String]
        notes <- c.downField("notes").as[Option[String]]
        tags <- c.downField("tags").as[List[Tag]]
      } yield Task(id, state, description, notes, tags)

    def apply(t: Task): Json =
      Json.obj(
        "id" -> t.id.asJson,
        "state" -> t.state.asJson,
        "description" -> Json.fromString(t.description),
        "notes" -> t.notes.asJson,
        "tags" -> t.tags.asJson
      )
  }
}