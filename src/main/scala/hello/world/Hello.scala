package hello.world

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html._

@react object Hello {
  case class Props(name: String)
  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    h1(s"Hello, ${props.name}!")
  }
}

