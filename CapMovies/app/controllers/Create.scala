package controllers

import javax.inject._
import play.api.mvc._
import play.api.i18n.I18nSupport
import persistence.domain._
import persistence.connector.MovieConnector
import views._

@Singleton
class Create @Inject()(cc: ControllerComponents, mc:MovieConnector) extends AbstractController(cc) with I18nSupport {

  def AddMovie = Action { implicit request =>
    MovieForm.submitForm.bindFromRequest().fold({ formWithErrors =>
      BadRequest(views.html.createPage(formWithErrors))
    }, { movie =>
      mc.create(movie)
      Redirect("/")
    })
  }
}