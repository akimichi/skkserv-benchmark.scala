import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }

import akka.actor._
//import akka.actor.{Actor, ActorSystem, ActorLogging, Props}
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import java.net.InetSocketAddress

class ClientSpec extends FunSpec with org.scalatest.Matchers with BeforeAndAfterAll {
  import net.homeunix.akimichi.skkserv_benchmark.connection._
  
  describe("client") {
    it("ClientDriverを使う") {
	  ClientDriver.connect("localhost", 1178)
	}
    // it("clientを使う") {
	//   ConnectTCP.main(Array())
    //   // val system = ActorSystem("MySystem")
    //   // val remoteAddress = new InetSocketAddress("plato.local", 1178)
    //   // // 新しいClientアクターをシステムに追加
    //   // system.actorOf(Props(classOf[Client], remoteAddress))
	// }
  }
}

