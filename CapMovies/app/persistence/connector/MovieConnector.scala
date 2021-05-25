package persistence.connector

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

import persistence.domain.Movie
import persistence.domain.MovieTemp
import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.duration._
import play.api.mvc._
import play.api.libs.ws._
import play.api.http.HttpEntity
import play.api.libs.json._
import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.util.ByteString
import scala.util.{Failure, Success}
import play.api.libs.json._

import reactivemongo.bson.{BSONDocument, BSONObjectID}


class MovieConnector @Inject()(ws: WSClient, val controllerComponents: ControllerComponents, val ec: ExecutionContext) extends BaseController with Connector  {

  val backend = "http://localhost:9001"

  def wsget(url: String): Future[WSResponse] = {
    ws.url(backend+url).withRequestTimeout(5000.millis).get()
  }

  def wspost(url: String, jsObject: JsObject): Future[WSResponse] = {
    ws.url(backend+url).post(jsObject)
  }

  def wsput(url: String, jsObject: JsObject): Future[WSResponse] = {
    ws.url(backend+url).put(jsObject)
  }

  def create(movie: MovieTemp) = {
    val newMov = Json.obj(
      "title" -> movie.title,
      "director" -> movie.director,
      "actors" -> movie.actors,
      "rating" -> movie.rating,
      "genre" -> movie.genre,
      "img" -> movie.img
    )
    wspost("/create", newMov)
  }

  def read(id: BSONObjectID): Future[Movie] = {
    ws.url(backend+"/read/"+id.stringify).withRequestTimeout(5000.millis).get().map { response =>
      jsValueToMovie(response.json).get
    }
  }

  def list(): Future[Seq[Movie]] = {
    var movies: Seq[Movie] = Seq.empty[Movie]
    wsget("/list").map(_.json.as[JsArray].value.flatMap(jsValueToMovie).toSeq)
  }

  def update(movie: Movie) = {
    val newMov = Json.obj(
      "title" -> movie.title,
      "director" -> movie.director,
      "actors" -> movie.actors,
      "rating" -> movie.rating,
      "genre" -> movie.genre,
      "img" -> movie.img
    )
      wsput("/create", newMov).map(_ => true).recover{case _ => false}
    }

  def delete(id: BSONObjectID) = {
    ws.url(backend+"/delete/"+ id.stringify).withRequestTimeout(5000.millis).delete() map{
      _.status match {
        case 200 => 1
        case _ =>
      }
    }
  }

  def search(searchTerm: String): Future[Seq[Movie]] = {
    ws.url(backend+"/search/"+searchTerm).withRequestTimeout(5000.millis).get().map { response =>
      response.json.as[JsArray].value.flatMap(jsValueToMovie).toSeq
    }
  }

  def jsValueToMovie(value: JsValue): Option[Movie] = {
    val tryId = BSONObjectID.parse(((value \ "_id") \ "$oid").as[String])
    tryId match {
      case Success(objectId) => Some(Movie(objectId,
        (value \ "title").as[String],
        (value \ "director").as[String],
        (value \ "actors").as[String],
        (value \ "rating").as[String],
        (value \ "genre").as[String],
        (value \ "img").as[String]))
      case Failure(_) => None
    }
  }

  def filter(genre: String): Future[Seq[Movie]] = {
    genre match {
      case "All" => list()
      case _ => filterGenre(genre)
    }
  }

  def filterGenre(genre: String): Future[Seq[Movie]] = {
    var movies: Seq[Movie] = Seq.empty[Movie]
    ws.url(backend+"/filter/"+genre).withRequestTimeout(5000.millis).get().map { response =>
      for (movie <- response.json.as[JsArray].value) {
        val tryId = BSONObjectID.parse(((movie \ "_id") \ "$oid").as[String])
        tryId match {
          case Success(objectId) => movies = movies :+ (Movie(objectId,
            (movie \ "title").as[String],
            (movie \ "director").as[String],
            (movie \ "actors").as[String],
            (movie \ "rating").as[String],
            (movie \ "genre").as[String],
            (movie \ "img").as[String]))
          case Failure(_) =>
        }
      }
      movies
    }
  }
}