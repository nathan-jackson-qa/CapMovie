package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

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

  def updateMovie(id: Int) = Action {

    Ok(views.html.update())
  }
}

