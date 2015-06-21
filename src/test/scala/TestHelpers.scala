import org.scalatest.{ Suite, BeforeAndAfterAll }
import akka.testkit.TestKit

// c.f. Akka in Action
trait StopSystemAfterAll extends BeforeAndAfterAll {
  this: TestKit with Suite =>
  override protected def afterAll() {
	super.afterAll()
	system.shutdown()
  }
}


