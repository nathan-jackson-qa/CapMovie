package persistence.domain

import play.api.data.Forms._
import play.api.data.Form

case class Search(searchTerm: String)

object SearchForm {
  val submitForm =
    Form(
      mapping(
        "searchTerm" -> nonEmptyText
      )(Search.apply)(Search.unapply)
    )
}