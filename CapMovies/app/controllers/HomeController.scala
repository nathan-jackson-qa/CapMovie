package controllers

import javax.inject._
import play.api.mvc._
import persistence.domain.{Movie, MovieTemp, MovieForm, Search, SearchForm}
import persistence.connector.MovieConnector
import play.api.i18n.I18nSupport
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONObjectID
import scala.concurrent.ExecutionContext.Implicits.global


@Singleton
class HomeController @Inject()(cc: ControllerComponents, mc: MovieConnector) extends AbstractController(cc) with I18nSupport {

  def index = Action.async {
    mc.list().map {
      result => Ok(views.html.index(result))
    }

  }

  def moviePage(id: BSONObjectID) = Action.async { // should take a movie? object id? as a parameter
    mc.read(id).map { result =>
      Ok(views.html.moviePage(result))
    }
  }

  def deleteMovie(id: BSONObjectID) = Action.async {
    mc.delete(id).map{ results =>
     results match {
        case 1 => Ok(views.html.delete("SUCCESS"))
        case 0 => Ok(views.html.delete("FAIL"))
      }
    }
  }

  def updateMovie(id: BSONObjectID) = Action { implicit request =>// should take a movie object id? as a parameter

    MovieForm.submitForm.bindFromRequest().fold( { formWithErrors =>
      BadRequest(views.html.update(id, MovieForm.submitForm.fill(MovieTemp("N/A", "N/A","N/A", "N/A", "N/A", "N/A"))))
    }, { updatedMovie =>
      val newMov = Movie (id,updatedMovie.title,updatedMovie.director,updatedMovie.actors, updatedMovie.rating,updatedMovie.genre,updatedMovie.img)
      mc.update(newMov)
      Redirect("/movie/"+id.stringify)
    })
  }

  def search() = Action async { implicit request =>
    SearchForm.submitForm.bindFromRequest().fold( { formWithErrors =>
      Future { BadRequest(views.html.search(formWithErrors, null)) }
    }, { s =>
      mc.search(s.searchTerm).map { results =>
        Ok(views.html.search(SearchForm.submitForm.fill(s), results))
      }
    })
  }

  def filter(genre: String) = Action async { implicit request =>
    mc.filter(genre).map { movies =>
      Ok(views.html.index(movies))
    }
  }

  def testPrint() = Action{ implicit request =>
    Ok(views.html.blank())
  }
}

