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
import reactivemongo.bson.{BSONDocumentHandler, BSONObjectID}
import test.AsyncAbstractTest
import org.jsoup.Jsoup
import java.net.URI
import scala.concurrent.duration.{Duration, DurationInt}
import scala.language.postfixOps
import scala.xml.Elem




class HomeControllerTest extends AsyncAbstractTest {

  val mc = mock(classOf[MovieConnector])
  val controller = new HomeController(Helpers.stubControllerComponents(), mc)
  def await[T](arg: Future[T]): T = Await.result(arg, Duration.Inf)
  val movie = Movie(BSONObjectID.parse("609a678ce1a52451685d793f").get, "Gladiator", "Ridley Scott", "Russell Crowe", "R", "Action", "images/posters/gladiator.jpg")


  val movie1 = Movie(BSONObjectID.parse("609a678ce1a52451685d793f").get, "Gladiator", "Ridley Scott", "Russell Crowe", "R", "Action", "images/posters/gladiator.jpg")

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

    "UpdateMovie" should {
      "update the movie object" in {
        when(mc.update(any())) thenReturn Future.successful(true)

        val result: Future[Result] = controller.updateMovie(BSONObjectID.parse("609a678ce1a52451685d793f").get).apply(FakeRequest().withFormUrlEncodedBody("title"-> "gg", "director" -> "gg", "actors" -> "gg", "rating" -> "gg", "genre" -> "gg", "img" -> "gg.jpg"))
        result.map{
          x => assert(x.header.status.equals(303))
        }
      }
    }

    "moviePage" should {
      "OK" in {
        when(mc.read(any())) thenReturn Future.successful(movie1)

        val result: Future[Result] = controller.moviePage(movie1._id).apply(FakeRequest())
        result.map(_.header.status shouldBe 200)
      }
    }

    "DeleteMovie" should {
      "Delete the movie object" in {
        when(mc.delete(any())) thenReturn Future.successful(true)

        val result: Future[Result] = controller.deleteMovie(BSONObjectID.parse("609a678ce1a52451685d793f").get).apply(FakeRequest())
        result.map{
          x => assert(x.header.status.equals(200))
        }
      }
    }

    "Search" should {
      "Search for a movie with no parameters" in {
        when(mc.search(any())) thenReturn(Future.successful(Nil))

        val result: Result = await(controller.search().apply(FakeRequest()))

        result.header.status shouldBe 400

      }
      "Search for a movie with parameters" in {
        when(mc.search(any())) thenReturn(Future.successful(Nil))

        val result: Future[Result] = controller.search().apply(FakeRequest().withFormUrlEncodedBody("searchTerm" -> "Gladiator"))

        await(result).header.status shouldBe 200

        val doc = Jsoup.parse(contentAsString(result))

        doc.getElementsByClass("card-body").size() shouldBe 0

      }
      "Search for a movie with parameter and returns a movie" in {
        when(mc.search(any())) thenReturn(Future.successful(Seq(movie, movie)))

        val result: Future[Result] = controller.search().apply(FakeRequest().withFormUrlEncodedBody("searchTerm" -> "Gladiator"))

        await(result).header.status shouldBe 200

        val doc = Jsoup.parse(contentAsString(result))

        doc.getElementsByClass("card-body").size() shouldBe 2
      }

    }
  }
}
