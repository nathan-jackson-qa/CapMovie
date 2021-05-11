package controllers

import javax.inject._
import play.api.mvc._
import persistence.domain.{Movie, MovieTemp, MovieForm, Search, SearchForm}
import persistence.connector.MovieConnector
import play.api.i18n.I18nSupport
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONObjectID

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

  def deleteMovie(id: Int) = Action {
    // Do delete functions here
    Ok(views.html.index(""))
  }

  def updateMovie(id: Int) = Action { implicit request =>// should take a movie object id? as a parameter

    MovieForm.submitForm.bindFromRequest().fold( { formWithErrors =>
      BadRequest(views.html.update(id, MovieForm.submitForm.fill(MovieTemp("Nemo", "human", "18", "Horror", "scary.jpg"))))
    }, { updatedMovie =>
      Redirect("/movie/"+id)
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

