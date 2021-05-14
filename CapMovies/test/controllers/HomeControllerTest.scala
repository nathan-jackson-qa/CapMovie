package CapMovies.test.controllers

import CapMovies.test.AbstractTest
import controllers.HomeController

import play.api.libs.json.Json
import play.api.mvc.{Result, Results}
import play.api.test.{FakeRequest, Helpers}
import play.api.test.Helpers.{POST, contentAsString, defaultAwaitTimeout}

import play.api.libs.ws._
import org.mockito.Mockito._

import scala.concurrent.Future

import persistence.connector.MovieConnector

class HomeControllerTest extends AbstractTest with Results {

  val mc = mock(classOf[MovieConnector])
  val controller = new HomeController(Helpers.stubControllerComponents(), mc)



  "HomeController" can {
    "index" should {
      "do something" in {
        val result: Future[Result] = controller.index().apply(FakeRequest())
        val bodyText = contentAsString(result)
        println(bodyText)
      }
    }
  }

}
