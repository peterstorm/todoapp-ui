package hello.world

import slinky.core._
import slinky.core.facade._
import slinky.core.annotations.react
import slinky.web.html._
import hello.world.data.{Active}
import hello.world.components._
import java.util.UUID

@react object TaskEditor {
  val component = FunctionalComponent[App.AddTask] { addTask =>
    val descriptionRef = React.createRef[org.scalajs.dom.html.Input]
    val notesRef = React.createRef[org.scalajs.dom.html.TextArea]
    val (tags, updateTags) = Hooks.useState(List.empty[hello.world.data.Tag])

    def clickHandler(): Unit = {
      val description = descriptionRef.current.value
      if(description == "") ()
      else {
        val state = Active
        val notes = {
          val n = notesRef.current.value
          if(n == "") None else Some(n)
        }

        val id = UUID.randomUUID()

        val task = hello.world.data.Task(id, state, description, notes, tags)

        descriptionRef.current.value = ""
        notesRef.current.value = ""
        updateTags(List.empty)

        addTask(task)
      }
    }

    div(className := "task-editor mx-auto bg-white p-8")(
      h4(className := "mb-4 text-lg font-bold text-gray-700")("Create new task"),
      div(className := "mb-4")(
        div(
          TextInput(id = "description", description = "Description", ref = descriptionRef),
          TextArea(id = "notes", description = "Notes", ref = notesRef),
          TagsEditor(tags = tags, updateTags = updateTags)
        ),
        Button(title = "Create Task", handler = (clickHandler _))
      )
    )
  }
}
