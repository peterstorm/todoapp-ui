package hello.world
package components

import slinky.core._
import slinky.core.facade._
import slinky.core.annotations.react
import slinky.web._
import slinky.web.html._
import org.scalajs.dom
import hello.world.data.{Tag => DTag}

@react object TagsEditor {
  import ClassName._

  case class Props(
                  tags: List[DTag],
                  updateTags: SetStateHookCallback[List[DTag]]
                  )

  val spanClasses = """w-full
                      |border-gray-500
                      |border-b border-t-0 border-l-0 border-r-0
                      |flex
                      |items-center
                      |focus-within:outline-none
                      |focus-within:border-teal-400""".asClassNames

  val inputClasses = """flex-grow
                       |p-2
                       |focus:outline-none""".asClassNames

  val tagClasses = """border
                     |border-gray-700
                     |rounded
                     |flex-grow-0
                     |text-gray-700
                     |p-2
                     |text-xs""".asClassNames

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val tagRef = React.createRef[dom.html.Input]

    def createTag(tag: String): Unit = {
      val t = DTag(tag)
      if (tag == "" || props.tags.contains(t)) ()
      else {
        tagRef.current.value = ""
        props.updateTags(_ :+ t)
      }
    }

    def keyHandler(event: SyntheticKeyboardEvent[dom.html.Input]): Unit = {
      val tag = tagRef.current.value

      if(event.key == "Enter") createTag(tag)
      else ()
    }

    def clickHandler(event: SyntheticMouseEvent[dom.raw.Element]): Unit = {
      val tag = tagRef.current.value
      createTag(tag)
    }

    def makeDeleteHandler(tag: DTag): () => Unit =
      () => props.updateTags(_.filterNot(t => t == tag))

    div(className := "mb-2")(
      Label(id = "tag", description = "tags"),
      div(className := "flex flew-wrap gap-1")(
        props.tags.zipWithIndex.map { case (t, idx) =>
          val handler = makeDeleteHandler(t)
          Tag(tag = t,
              status = Tag.Inactive,
              clickHandler = None,
              deleteHandler = Some(handler)
          )
        },
        span(className := spanClasses)(
          input(className := inputClasses,
            `type` := "text",
            name := "tag",
            id := "tag",
            ref := tagRef,
            onKeyDown := (event => keyHandler(event))),
          MaterialIcons.MdAdd(className := "text-teal-400 border-teal-400 hover:border-teal-500 hover:text-teal-500 mx-2 border rounded",
            onClick := (event => clickHandler(event)))
        )
      )
    )
  }
}
