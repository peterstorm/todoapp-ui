package hello.world
package components

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html._

@react object Label {
  import ClassName._

  case class Props(id: String, description: String)

  val labelClasses: String = """w-full
                       |text-sm
                       |text-gray-500""".asClassNames

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    label(className := labelClasses,
          htmlFor := props.id)(props.description)
  }
}
