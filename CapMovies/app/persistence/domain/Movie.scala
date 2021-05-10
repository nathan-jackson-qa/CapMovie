package persistence.domain

import play.api.data.Forms._
import play.api.data.Form

case class Movie(id: Int, name: String, director: String)

object MovieForm {
  val submitForm =
    Form(
      mapping(
        "id" -> default(number, 0),
        "name" -> nonEmptyText,
        "director" -> nonEmptyText
      )(Movie.apply)(Movie.unapply)
    )
}