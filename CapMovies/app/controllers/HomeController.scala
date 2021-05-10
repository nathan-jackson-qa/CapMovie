package controllers

import javax.inject._
import play.api.mvc._
import persistence.domain.{Movie, MovieForm}
import play.api.i18n.I18nSupport

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def index = Action {
    Ok(views.html.index())
  }

  def moviePage(id: Int) = Action {
    Ok(views.html.moviePage(id))
  }

  def deleteMovie(id: Int) = Action {
    // Do delete functions here
    Ok(views.html.index())
  }

  def updateMovie(id: Int) = Action { implicit request =>// should take a movie as a parameter
    MovieForm.submitForm.bindFromRequest().fold( { formWithErrors =>
      BadRequest(views.html.update(id, MovieForm.submitForm.fill(Movie(1, "Nemo", "human"))))
    }, { widget =>
      Redirect("/movie/"+id)
    })
  }
}

