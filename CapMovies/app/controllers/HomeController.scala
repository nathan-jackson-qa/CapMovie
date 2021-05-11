package controllers

import javax.inject._
import play.api.mvc._
import persistence.domain.{Movie, MovieForm, Search, SearchForm}
import play.api.i18n.I18nSupport

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def index = Action {
    Ok(views.html.index())
  }

  def moviePage(id: Int) = Action { // should take a movie? object id? as a parameter
    Ok(views.html.moviePage(id))
  }

  def deleteMovie(id: Int) = Action {
    // Do delete functions here
    Ok(views.html.index())
  }

  def updateMovie(id: Int) = Action { implicit request =>// should take a movie object id? as a parameter
    MovieForm.submitForm.bindFromRequest().fold( { formWithErrors =>
      BadRequest(views.html.update(id, MovieForm.submitForm.fill(Movie(1, "Nemo", "human"))))
    }, { updatedMovie =>
      Redirect("/movie/"+updatedMovie.id)
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
}

