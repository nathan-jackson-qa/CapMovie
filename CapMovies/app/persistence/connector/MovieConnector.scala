package persistence.connector

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

import persistence.domain.Movie
import javax.inject.Inject

import scala.concurrent.Future
import scala.concurrent.duration._
import play.api.mvc._
import play.api.libs.ws._
import play.api.http.HttpEntity
import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.util.ByteString




class MovieConnector @Inject()(ws: WSClient, val controllerComponents: ControllerComponents, val ec: ExecutionContext) extends BaseController  {

  def read(): Future[String] = {
    ws.url("http://localhost:9001/list").withRequestTimeout(5000.millis).get().map { response =>
      (response.json \ "title").as[String]
    }
  }

}