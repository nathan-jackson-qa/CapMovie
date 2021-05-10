package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class Create @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def CreatePage = Action { implicit request=>
    Ok(views.html.createPage())
  }

  def AddMovie = Action {
    request => val postVals = request.body.asFormUrlEncoded
      postVals.map{ args =>
        val name = args("name").head
        val description = args("description").head
        Ok(s"The movie name: $name and description: $description")
      }.getOrElse(Ok("Oops"))
  }
}