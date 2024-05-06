package playscala

import slinky.core.annotations.react
import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.web.html.div

import org.scalajs.dom.document
import org.scalajs.dom.html
import org.scalajs.dom.window
import slinky.web.html._
import models.UserChats
import models.ReadsAndWrites._
import org.scalajs.dom.WebSocket

@react class HomePageComponent extends Component {
  type Props = Unit
  case class State(page: String, socket: WebSocket)
  def initialState: State = State("Chat", new WebSocket(document.getElementById("ws-route").asInstanceOf[html.Input].value.replace("http", "ws")))

  implicit val ec = scala.concurrent.ExecutionContext.global
  
  lazy val chatPageComponent: ReactElement = ChatPageComponent(state.socket)

  val pages: Map[String, ReactElement] =  Map("Matching" -> chatPageComponent, "Profile" -> chatPageComponent, "Chat" -> chatPageComponent)
  def selectPage(): ReactElement = pages.get(state.page)

  window.onunload = { event =>
    state.socket.send("close")
    state.socket.close()
  }
  def render(): ReactElement = {
    div( id:="homePage",
      div( id:="navbar",
        div( id := "pageButtonsContainer",
          pages.keySet.zipWithIndex.map { case (pageName, i) =>
            button(key := i.toString(), className := "pageButton", id := {if (pageName == state.page) "selectedButton" else ""}, onClick := (e => {setState(state.copy(page = pageName))}), pageName)
          }
        )
      ),
      selectPage()
    )
  }

}