package controllers

import akka.stream.scaladsl.Source
import akka.stream.testkit.NoMaterializer.executionContext
import akka.util.ByteString
import controllers.HomeController
import org.mockito.ArgumentMatchers.any
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Result, Results}
import play.api.test.{FakeRequest, Helpers}
import play.api.test.Helpers.{POST, contentAsString, defaultAwaitTimeout, redirectLocation}
import play.api.libs.ws._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AsyncWordSpec}

import scala.concurrent.{Await, Future}
import persistence.connector.MovieConnector
import persistence.domain.Movie
import reactivemongo.bson.BSONObjectID
import test.AsyncAbstractTest

import java.net.URI
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.xml.Elem


class HomeControllerTest extends AsyncAbstractTest {

  val mc = mock(classOf[MovieConnector])
  val controller = new HomeController(Helpers.stubControllerComponents(), mc)

  "HomeController" can {
    "index" should {
      "do something" in {
        when(mc.list()).thenReturn(Future.successful(Nil))

        val notResult: Future[Result] = controller.index().apply(FakeRequest())
        notResult.map{ result =>
          assert(result.header.status.equals(200))
        }
      }
    }

    "updateMovie" should {
      "update the movie object" in {
        when(mc.update(any())) thenReturn Future.successful(true)

        val result: Future[Result] = controller.updateMovie(BSONObjectID.parse("609a678ce1a52451685d793f").get).apply(FakeRequest().withFormUrlEncodedBody("title"-> "gg", "director" -> "gg", "actors" -> "gg", "rating" -> "gg", "genre" -> "gg", "img" -> "gg.jpg"))
        result.map{
          x => assert(x.header.status.equals(303))
        }
      }
    }

    "deleteMovie" should {
      "OK" in {
        when(mc.delete(any())) thenReturn Future.successful(true)

        val result: Future[Result] = controller.deleteMovie(BSONObjectID.parse("609a678ce1a52451685d793f").get).apply(FakeRequest())
        result.map {
          x => assert(x.header.status.equals(300))
        }
      }
    }
  }


}
