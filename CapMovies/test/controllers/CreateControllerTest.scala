package controllers

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.mockito.Mock._
import persistence.connector.MovieConnector
import persistence.domain.{Movie, MovieTemp}
import play.api.mvc.Result
import play.api.test.{FakeRequest, Helpers}
import reactivemongo.bson.BSONObjectID
import test.AsyncAbstractTest

import scala.concurrent.Future
import scala.util.Success

class CreateControllerTest extends AsyncAbstractTest {
  val mc = mock(classOf[MovieConnector])
  val controller = new Create(Helpers.stubControllerComponents(), mc)

  "CreateController" can {
    "Add Movie" should {
      "Add a movie to the database" in {
        //val movie = MovieTemp("DaveMcDave", "Davey", "McDavidsion", "BoatyMcBoatface", "asdasd", "asdasd")
        when(mc.create(any())) thenReturn Future.successful(true)
        val notResult: Future[Result] = controller.AddMovie.apply(FakeRequest().withFormUrlEncodedBody("title"->"gg", "director" -> "ridley", "actors" -> "gg", "rating" -> "18", "genre" -> "yes", "img" -> "asdasdasd"))
        notResult.map{
          x => assert(x.header.status.equals(303))
        }
      }
    }
  }
}
