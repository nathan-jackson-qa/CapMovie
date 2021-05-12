package controllers

import javax.inject._
import play.api.mvc._
import persistence.domain.{Movie, MovieTemp, MovieForm, Search, SearchForm}
import persistence.connector.MovieConnector
import play.api.i18n.I18nSupport
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONObjectID
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject()(cc: ControllerComponents, mc: MovieConnector) extends AbstractController(cc) with I18nSupport {

  def index = Action {
    Ok(views.html.index())
  }

  def moviePage(id: BSONObjectID) = Action.async { // should take a movie? object id? as a parameter
    mc.read(id).map { result =>
      Ok(views.html.moviePage(result))
    }
  }

  def deleteMovie(id: Int) = Action {
    // Do delete functions here
    Ok(views.html.index())
  }

  def updatePage() = Action { implicit request =>
    Ok(views.html.update(id: BSONObjectID,MovieForm.submitForm))
  }

  def updateMovie(id: BSONObjectID) = Action { implicit request =>// should take a movie object id? as a parameter

    MovieForm.submitForm.bindFromRequest().fold( { formWithErrors =>
      BadRequest(views.html.update(id, MovieForm.submitForm.fill(MovieTemp("N/A", "N/A", "N/A", "N/A", "N/A"))))
    }, { updatedMovie =>
      val newMov = Movie (id,updatedMovie.title,updatedMovie.director,updatedMovie.rating,updatedMovie.genre,updatedMovie.img)
      mc.update(newMov)
      Redirect("/movie/"+id.stringify)
    })
  }

  def search() = Action { implicit request =>
    SearchForm.submitForm.bindFromRequest().fold( { formWithErrors =>
      BadRequest(views.html.search(formWithErrors))
    }, { searchTerm =>
      println(searchTerm)
      Redirect("/search")
    })
  }

  def testPrint() = Action{ implicit request =>
    Ok(views.html.index())
  }
}

