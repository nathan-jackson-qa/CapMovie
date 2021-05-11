package persistence.domain

import play.api.data.Forms._
import play.api.data.Form
import play.api.data.format.Formats.doubleFormat
import play.api.libs.json.{Json, OFormat}
import play.api.libs.json.__
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json._

case class Movie(_id: BSONObjectID, title: String, director: String, rating: String, genre: String, img: String)

case class MovieTemp(title: String, director: String, rating: String, genre: String, img: String)

object JsonFormat {
  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val movieFormat: OFormat[Movie] = Json.format[Movie]
}

object MovieForm {
  val submitForm: Form[MovieTemp] =
    Form(
      mapping(
        "title" -> nonEmptyText,
        "director" -> nonEmptyText,
        "rating" -> nonEmptyText,
        "genre" -> nonEmptyText,
        "img" -> nonEmptyText
      )(MovieTemp.apply)(MovieTemp.unapply)
    )
}