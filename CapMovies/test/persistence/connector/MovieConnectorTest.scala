package persistence.connector

import akka.stream.scaladsl.Source
import akka.stream.testkit.NoMaterializer.executionContext
import akka.util.ByteString
import persistence.connector.MovieConnector
import play.api.libs.ws._
import org.mockito.Mockito._
import persistence.domain.{Movie, MovieTemp}

import scala.concurrent.ExecutionContext
import play.api.mvc.{ControllerComponents, MultipartFormData}
import play.api.libs.json._
import play.libs.ws.WSBody
import reactivemongo.bson.BSONObjectID
import test.{AbstractTest, AsyncAbstractTest}

import java.io.File
import java.net.URI
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.xml.Elem

class MovieConnectorTest extends AsyncAbstractTest {
  val backend = "http://localhost:9001"


  val ws = mock(classOf[WSClient])
  val cc = mock(classOf[ControllerComponents])
  val ec = mock(classOf[ExecutionContext])
  val mc = new MovieConnector(ws, cc, ec){
    override def wsget(url: String): Future[WSResponse] = Future {
      new TestResponse() {
        override def json = Json.parse("""[{"_id":{"$oid":"609a678ce1a52451685d793f"},"title":"Gladiator","director":"Ridley Scott","actors":"Russell Crowe","rating":"R","genre":"Action","img":"images/posters/gladiator.jpg"}]""")
      }
    }

    override def wspost(url: String, jsObject: JsObject): Future[TestResponse] = Future {
      new TestResponse() {
        override def status: Int = 201
      }
    }
  }
  val mc2 = new MovieConnector(ws, cc, ec){
    override def wsget(url: String): Future[WSResponse] = Future {
      new TestResponse() {
        override def json = Json.parse("""{"_id":{"$oid":"609a678ce1a52451685d793f"},"title":"Gladiator","director":"Ridley Scott","actors":"Russell Crowe","rating":"R","genre":"Action","img":"images/posters/gladiator.jpg"}""")
      }
    }

  }

  class TestResponse extends WSResponse {
    override def status: Int = ???

    override def statusText: String = ???

    override def headers: Map[String, collection.Seq[String]] = ???

    override def underlying[T]: T = ???

    override def cookies: collection.Seq[WSCookie] = ???

    override def cookie(name: String): Option[WSCookie] = ???

    override def body: String = ???

    override def bodyAsBytes: ByteString = ???

    override def bodyAsSource: Source[ByteString, _] = ???

    override def allHeaders: Map[String, collection.Seq[String]] = ???

    override def xml: Elem = ???

    override def json: JsValue = ???

    override def uri: URI = ???
  }
//   ws.url(backend+"/list").withRequestTimeout(5000.millis).get().map { response =>
//   [{"_id":{"$oid":"609a678ce1a52451685d793f"},"title":"Gladiator","director":"Ridley Scott","actors":"Russell Crowe","rating":"R","genre":"Action","img":"images/posters/gladiator.jpg"}]

  "MovieConnector" can {
    "read a list of movies" should {
      "return a list of movie objects" in {
        val tryId = BSONObjectID.parse("609a678ce1a52451685d793f")
        tryId match {
          case Success(value) => {
            mc.list().map { response =>
              assert(response.equals(Seq(Movie(value, "Gladiator", "Ridley Scott", "Russell Crowe", "R", "Action", "images/posters/gladiator.jpg"))))
            }
          }
          case Failure(exception) => assert(false)
        }

      }
    }

    "create a movie" should {
      "return a " in {
        mc.create(MovieTemp("Gladiator", "Ridley Scott", "Russell Crowe", "R", "Action", "images/posters/gladiator.jpg")).map { response =>
          assert(response.equals(true))
        }
      }
    }

    "read a movie" should {
      "return an individual movie" in {
        val tryId = BSONObjectID.parse("609a678ce1a52451685d793f")
        tryId match {
          case Success(value) =>
            mc2.read(value).map { response =>
              assert(response.equals(Seq(Movie(value, "Gladiator", "Ridley Scott", "Russell Crowe", "R", "Action", "images/posters/gladiator.jpg"))))
            }
          case Failure(exception) => assert(false)
        }
      }
    }

    "search for a term" should {
      "return a list of movies matching the term" in {
        val tryId = BSONObjectID.parse("609a678ce1a52451685d793f")
        tryId match {
          case Success(value) =>
            mc.search("gladiator").map { response =>
              assert(response.equals(Seq(Movie(value, "Gladiator", "Ridley Scott", "Russell Crowe", "R", "Action", "images/posters/gladiator.jpg"))))
            }
          case Failure(exception) => assert(false)
        }
      }
    }
    "filter by a genre" should {
      "give a list of movies matching that genre" in {
        val tryId = BSONObjectID.parse("609a678ce1a52451685d793f")
        tryId match {
          case Success(value) =>
            mc.filterGenre("Action").map { response =>
              assert(response.equals(Seq(Movie(value, "Gladiator", "Ridley Scott", "Russell Crowe", "R", "Action", "images/posters/gladiator.jpg"))))
            }
          case Failure(exception) => assert(false)
        }
      }
    }
  }


}
