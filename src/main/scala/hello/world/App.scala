package hello.world

import hello.world.data.Task
import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.Hooks._
import slinky.web.html._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

import hello.world.data._
import hello.world.components._

@JSImport("resources/App.css", JSImport.Default)
@js.native
object AppCSS extends js.Object

@JSImport("resources/logo.svg", JSImport.Default)
@js.native
object ReactLogo extends js.Object

@react object App {

  implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  type UpdateState = () => Unit
  type AddTask = Task => Unit
  type DeleteTask = Task => Unit
  type CompleteTask = Task => Unit
  type UncompleteTask = Task => Unit
  type UpdateFilter = Option[hello.world.data.Tag] => Unit

  type Props = Unit

  private val css = AppCSS

  val component: FunctionalComponent[Props] = FunctionalComponent[Unit] { _ =>
    val (tasks, updateTasks) = useState(Tasks.empty)
    val (tags, updateTags) = useState(Tags.empty)
    val (tagFilter, updateTagFilter) = useState(Option.empty[hello.world.data.Tag])

    useEffect(updateState, List(tagFilter))

    def updateState(): UpdateState =
      () => {
        tagFilter.fold(Api.getTasks)(tag => Api.getTasksFromTag(tag)).foreach{ tasks =>
          updateTasks(tasks)
        }
        Api.getTags.foreach(tags => updateTags(tags))
      }

    def addTask: AddTask =
      task => Api.createTask(task).foreach(_ => updateState())

    def deleteTask: DeleteTask =
      task => Api.deleteTask(task).foreach(_ => updateState())

    def completeTask: CompleteTask =
      task => Api.completeTask(task).foreach(_ => updateState())

    def uncompleteTask: UncompleteTask =
      task => Api.updateTask(task).foreach(_ => updateState())

    def updateFilter: UpdateFilter =
      filter => { updateTagFilter(filter)}

    div(className := "App")(
      div(className := "md:container mx-auto p-8")(
        div(className := "flex mb-4")(
          div(className := "w-1/4 bg-white")(
            TagsPane(
              tags = tags,
              currentFilter = tagFilter,
              updateFilter = updateTagFilter
            )
          ),
          div(className := "w-3/4 bg-white")(
            TaskListView(
              tasks = tasks,
              complete = completeTask,
              uncomplete = uncompleteTask,
              updateFilter = updateFilter
            ),
            TaskEditor(addTask)
          )
        )
      )
    )
  }
}
