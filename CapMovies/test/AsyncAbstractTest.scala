package test

import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AsyncWordSpec}

trait AsyncAbstractTest extends AsyncWordSpec with BeforeAndAfter with Matchers {

}
