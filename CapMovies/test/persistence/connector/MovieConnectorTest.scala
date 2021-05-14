package CapMovies.test.persistence.connector

import CapMovies.test.AbstractTest
import persistence.connector.MovieConnector
import play.api.libs.ws._
import org.mockito.Mockito._
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc.ControllerComponents
import play.api.libs.json._
import scala.concurrent.duration._
import scala.concurrent.Future

class MovieConnectorTest extends AbstractTest {
  val backend = "http://localhost:9001"


//  val ws = mock(classOf[WSClient])
//  val cc = mock(classOf[ControllerComponents])
//  val ec = mock(classOf[ExecutionContext])
//  val wsresponse = mock(classOf[WSResponse])
//  val mc = new MovieConnector(ws, cc, ec)


  // ws.url(backend+"/list").withRequestTimeout(5000.millis).get().map { response =>
  // [{"_id":{"$oid":"609a678ce1a52451685d793f"},"title":"Gladiator","director":"Ridley Scott","actors":"Russell Crowe","rating":"R","genre":"Action","img":"images/posters/gladiator.jpg"}]

  "MovieConnector" can {
    "read a list of movies" should {
      "return a list of movie objects" in {
//        when(wsresponse.json).thenReturn(Json.toJson("[{\"_id\":{\"$oid\":\"609a678ce1a52451685d793f\"},\"title\":\"Gladiator\",\"director\":\"Ridley Scott\",\"actors\":\"Russell Crowe\",\"rating\":\"R\",\"genre\":\"Action\",\"img\":\"images/posters/gladiator.jpg\"}]"))
//
//        when(ws.url(backend+"/list").withRequestTimeout(5000.millis).get()).thenReturn(Future {wsresponse} )
//
//        print(mc.list())
//        assert(true)
      }
    }
  }


}
