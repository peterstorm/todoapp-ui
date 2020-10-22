package hello.world

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html._
import java.util.UUID

import hello.world.components._
import hello.world.data._

@react object TaskView {
  import ClassName._

  val completeClasses = """border
                          |border-gray-700
                          |text-gray-700
                          |rounded
                          |mr-2
                          |p-2
                          |inline-block
                          |hover:border-teal-500
                          |hover:text-teal-500""".asClassNames

  val uncompleteClasses = """border
                            |border-gray-600
                            |text-gray-600
                            |rounded
                            |mr-2
                            |inline-block
                            |hover:border-teal-500
                            |hover:text-teal-500""".asClassNames

  case class Props(
                  id: UUID,
                  task: Task,
                  complete: App.CompleteTask,
                  uncomplete: App.UncompleteTask,
                  updateFilter: App.UpdateFilter
                  )

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] {
    case Props(id, task, complete, uncomplete, updateFilter) =>

      def clickHandler(): Unit =
        complete(id)

      task.state match {
        case Active =>
          li(
            key := id.toString,
            className := "task bg-white mb-8 bp-2 border-b"
          )(
            div(className := "mb-2 text-gray-700 block flex items-center")(
              div(className := completeClasses, onClick := clickHandler _),
              h4(className := "text-lg font-bold inline-block")(task.description)
            ),
            div(className := "flex flex-wrap gap-1 mb-4")(
              task.tags.map { t =>
                hello.world.components.Tag(
                  tag = t,
                  status = hello.world.components.Tag.Inactive,
                  clickHandler = Some(() => updateFilter(Some(t))),
                  deleteHandler = None
                )
              }
            ),
            task.notes
              .map(s => p(className := "text-gray-600")(s))
              .getOrElse(div())
          )
        case c: Completed =>
          li(
            key := id.toString,
            className := "task bg-white mb-8 pb-2 border-b"
          )(
            div(className := "flex items-center")(
              MaterialIcons.MdCheck(className := uncompleteClasses, onClick := (() => uncomplete(id, task))),
              p(className := "description strikethrough text-gray-500")(task.description)
            )
          )
      }
  }
}
