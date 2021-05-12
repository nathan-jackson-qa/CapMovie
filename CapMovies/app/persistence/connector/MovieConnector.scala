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


class MovieConnector @Inject()(ws: WSClient, val controllerComponents: ControllerComponents, val ec: ExecutionContext) extends BaseController  {

  val backend = "http://localhost:9001"

  def test(): Future[String] = {
    ws.url(backend+"/read/60992b3a946c653550889f5c").withRequestTimeout(5000.millis).get().map { response =>
      ((response.json \ "_id") \ "$oid").as[String]
    }
  }

  def create(movie: MovieTemp) = {
    val newMov = Json.obj(
      "title" -> movie.title,
      "director" -> movie.director,
      "actors" -> "yes boy im an actor",
      "rating" -> movie.rating,
      "genre" -> movie.genre,
      "img" -> movie.img
    )
    val FutureResponse: Future[WSResponse] = ws.url(backend+"/create").post(newMov)
  }

  def read(id: BSONObjectID): Future[Movie] = {
    ws.url(backend+"/read/"+id.stringify).withRequestTimeout(5000.millis).get().map { response =>
      jsValueToMovie(response.json)
    }
  }

  def list(): Future[Seq[Movie]] = {
    var movies: Seq[Movie] = Seq.empty[Movie]
    ws.url(backend+"/list").withRequestTimeout(5000.millis).get().map { response =>

      for (movie <- response.json.as[JsArray].value) {
        val tryId = BSONObjectID.parse(((movie \ "_id") \ "$oid").as[String])
        tryId match {
          case Success(objectId) => movies = movies :+ (Movie(objectId,
            (movie \ "title").as[String],
            (movie \ "director").as[String],
            (movie \ "rating").as[String],
            (movie \ "genre").as[String],
            (movie \ "img").as[String]))
          case Failure(_) =>
        }
      }
      movies
    }
  }

  def update(movie: Movie) = {
    val newMov = Json.obj(
      "title" -> movie.title,
      "director" -> movie.director,
      "actors" -> "yes boy im an actor",
      "rating" -> movie.rating,
      "genre" -> movie.genre,
      "img" -> movie.img
    )
      ws.url(backend+"/update/"+ movie._id.stringify).withRequestTimeout(5000.millis).put(newMov)
    }

  def delete() = ???

  def search(searchTerm: String): Future[Seq[Movie]] = {
    var movies: Seq[Movie] = Seq.empty[Movie]
    ws.url(backend+"/search/"+searchTerm).withRequestTimeout(5000.millis).get().map { response =>
      for (value <- response.json.as[JsArray].value) {
        jsValueToMovie(value) match {
          case movie: Movie => movies = movies :+ movie
          case null =>
        }
      }
      movies
    }
  }

  def jsValueToMovie(value: JsValue): Movie = {
    val tryId = BSONObjectID.parse(((value \ "_id") \ "$oid").as[String])
    tryId match {
      case Success(objectId) => Movie(objectId,
        (value \ "title").as[String],
        (value \ "director").as[String],
        (value \ "rating").as[String],
        (value \ "genre").as[String],
        (value \ "img").as[String])
      case Failure(_) => null
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