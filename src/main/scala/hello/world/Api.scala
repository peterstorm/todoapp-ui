package hello.world

import io.circe.Decoder
import io.circe.parser.parse
import io.circe.syntax._
import org.scalajs.dom

import scala.scalajs.js
import scala.concurrent.Future
import org.scalajs.dom.experimental._
import java.util.UUID

import hello.world.data._

object Api {
  implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  val host = "http://localhost:3000"

  def applicationJson: Headers = {
    val h = new Headers()
    h.append("Content-Type", "application/json")
    h
  }

  def getTasks: Future[Tasks] = {
    val ri = defaultRequest
    ri.method = HttpMethod.GET
    ri.mode = RequestMode.cors
    Fetch
      .fetch(s"${host}/api/tasks", ri)
      .toFuture
      .flatMap(response => response.text().toFuture)
      .map{ string =>
        Decoder[Tasks].decodeJson(parse(string).toOption.get) match {
          case Left(err) => throw err
          case Right(tasks) => tasks
        }
      }
  }

  def getTasksFromTag(tag: Tag): Future[Tasks] = {
    val ri = defaultRequest
    ri.method = HttpMethod.GET
    ri.mode = RequestMode.cors

    Fetch
      .fetch(s"${host}/api/tasks/${tag.tag}", ri)
      .toFuture
      .flatMap(response => response.text().toFuture)
      .map{ string =>
        Decoder[Tasks].decodeJson(parse(string).toOption.get) match {
          case Left(err) => throw err
          case Right(tasks) => tasks
        }
      }
  }

  def getTags: Future[Tags] = {
    val ri = defaultRequest
    ri.method = HttpMethod.GET
    ri.mode = RequestMode.cors

    Fetch
      .fetch(s"${host}/api/tags", ri)
      .toFuture
      .flatMap(response => response.text().toFuture)
      .map{ string =>
        Decoder[Tags].decodeJson(parse(string).toOption.get) match {
          case Left(err) => throw err
          case Right(tags) => tags
        }
      }
  }

  def createTask(task: Task): Future[UUID] = {
    val ri = defaultRequest
    ri.method = HttpMethod.POST
    ri.mode = RequestMode.cors
    ri.headers = applicationJson
    ri.body = task.asJson.spaces2

    val r = new Request(s"${host}/api/task", ri)

    Fetch
      .fetch(r)
      .toFuture
      .flatMap(response => response.text().toFuture)
      .map{ string =>
        Decoder[UUID].decodeJson(parse(string).toOption.get) match {
          case Left(err) => throw err
          case Right(id) => id 
        }
      }
  }

  def completeTask(id: UUID): Future[Task] = {
    val ri = defaultRequest
    ri.method = HttpMethod.POST
    ri.mode = RequestMode.cors

    val r = new Request(s"${host}/api/task/${id.toString}/complete", ri)

    Fetch
      .fetch(r)
      .toFuture
      .flatMap(response => response.text().toFuture)
      .map{string =>
        Decoder[Task].decodeJson(parse(string).toOption.get) match {
          case Left(err) => throw err
          case Right(task) => task
        }
      }
  }

  def updateTask(id: UUID, task: Task): Future[Option[Task]] = {
    val ri = defaultRequest
    ri.method = HttpMethod.POST
    ri.mode = RequestMode.cors
    ri.headers = applicationJson
    ri.body = task.asJson.spaces2

    val r = new Request(s"${host}/api/task/${id.toString}", ri)

    Fetch
      .fetch(r)
      .toFuture
      .flatMap(response => response.text().toFuture)
      .map{string =>
        Decoder[Option[Task]].decodeJson(parse(string).toOption.get) match {
          case Left(err) => throw err
          case Right(optTask) => optTask
        }
      }
  }

  def deleteTask(id: UUID): Future[Unit] = {
    val ri = defaultRequest
    ri.method = HttpMethod.DELETE
    ri.mode = RequestMode.cors

    val r = new Request(s"${host}/api/task/${id.toString}")

    Fetch
      .fetch(r)
      .toFuture
      .map(response => if(response.ok) () else throw new Exception("Delete failed"))
  }

  def defaultRequest: RequestInit =
    new RequestInit {
      var method: js.UndefOr[HttpMethod] = js.undefined
      var headers: js.UndefOr[HeadersInit] = js.undefined
      var body: js.UndefOr[BodyInit] = js.undefined
      var referrer: js.UndefOr[String] = js.undefined
      var referrerPolicy: js.UndefOr[ReferrerPolicy] = js.undefined
      var mode: js.UndefOr[RequestMode] = js.undefined
      var credentials: js.UndefOr[RequestCredentials] = js.undefined
      var cache: js.UndefOr[RequestCache] = js.undefined
      var redirect: js.UndefOr[RequestRedirect] = js.undefined
      var integrity: js.UndefOr[String] = js.undefined
      var keepalive: js.UndefOr[Boolean] = js.undefined
      var signal: js.UndefOr[AbortSignal] = js.undefined
      var window: js.UndefOr[Null] = js.undefined
    }
}
