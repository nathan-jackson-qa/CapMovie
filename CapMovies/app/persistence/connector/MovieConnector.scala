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
      val tryId = BSONObjectID.parse(((response.json \ "_id") \ "$oid").as[String])
      tryId match {
        case Success(objectId) =>Movie(objectId,
          (response.json \ "title").as[String],
          (response.json \ "director").as[String],
          (response.json \ "rating").as[String],
          (response.json \ "genre").as[String],
          (response.json \ "img").as[String])
        case Failure(_) => null
      }
    }
  }

  def list() = {
    ws.url(backend+"/list").withRequestTimeout(5000.millis).get().map { response =>
      
    }
  }

  def update() = ???

  def delete() = ???

  def search() = ???
}