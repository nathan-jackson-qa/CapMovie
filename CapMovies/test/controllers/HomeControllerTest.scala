package controllers

import akka.stream.testkit.NoMaterializer.executionContext
import controllers.HomeController
import play.api.libs.json.Json
import play.api.mvc.{Result, Results}
import play.api.test.{FakeRequest, Helpers}
import play.api.test.Helpers.{POST, contentAsString, defaultAwaitTimeout}
import play.api.libs.ws._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AsyncWordSpec}

import scala.concurrent.{Await, Future}
import persistence.connector.MovieConnector
import test.AsyncAbstractTest

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps


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
  }

}
