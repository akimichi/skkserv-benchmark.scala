import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Specsの基本的な用法を示す
 *
 * c.f. http://www.scalatest.org/user_guide/using_matchers
 * 
 */
class BasicUsageSpec extends FunSpec with org.scalatest.Matchers with BeforeAndAfterAll {
  
  describe("Specsにて") {
    it("should matcher を使う") {
      1 should equal(1)
	}
  }
  describe("should matcher の用法を確認する") {
    describe("should matcher") {
      it("equal"){
        "hello" should equal{
          "hello"
        }
      }
      it("Greater and less than"){
        1 should be > 0
      }
    }
  }
}

