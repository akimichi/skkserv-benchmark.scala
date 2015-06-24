package net.homeunix.akimichi.skkserv_benchmark.connection

import akka.actor._
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import java.net.InetSocketAddress
 
class ClientHandler extends Actor {
  import Tcp._
  import context.system

  def receive: Receive = {
    case Connected(remote, local) =>
	  sender ! Write(ByteString("1a "))
    case Tcp.Received(data) =>
      sender ! Tcp.Write(data)
    case Tcp.PeerClosed =>
      context stop self
  }
}


class Client(remoteAddress: InetSocketAddress) extends Actor with ActorLogging {
  import Tcp._
  import context.system

  import scala.concurrent.Await
  import akka.pattern.ask
  import akka.util.Timeout
  import scala.concurrent.duration._
 
  // サーバーに接続する
  IO(Tcp) ! Connect(remoteAddress)

  // 受け取ったメッセージを処理する
  def receive = {
    case Connected(remote, local) =>
	  val connection = sender
      log.info("{}に接続しました", remote)
	  implicit val timeout = Timeout(5 seconds)
	  val future = sender ? Write(ByteString("1a // "))
	  val result = Await.result(future, timeout.duration).asInstanceOf[String]
	  log.info(result)
      //connection ! Register(self)
	  // val clientHandler = context.actorOf(Props[ClientHandler])
      // connection ! Tcp.Register(clientHandler)
	  //connection ! Write(ByteString("1a "))
    case CommandFailed(_) =>
      log.error("接続に失敗しました")
      context stop self
    case _: Tcp.ConnectionClosed =>
      //log.info("Stopping, because connection for remote address {} closed", remote)
      context.stop(self)
    // case Terminated(`connection`) =>
    //   //log.info("Stopping, because connection for remote address {} died", remote)
    //   context.stop(self)
  }
}

object ClientDriver {
  // import Tcp._
  // import context.system

  // val manager = IO(Tcp)

  def connect(host:String, port:Int):Unit = {
    // アクターシステム作成
    implicit val system = ActorSystem("ClientActor")
    val remoteAddress = new java.net.InetSocketAddress(host, port)
    // 新しいClientアクターをシステムに追加
    // system.actorOf(Props(classOf[Client], remoteAddress))
	IO(Tcp) ! Tcp.Connect(remoteAddress)
  }
}

object ConnectTCP {
  def main (args: Array[String]): Unit = {
    // アクターシステム作成
    val system = ActorSystem("MySystem")
    val remoteAddress = new java.net.InetSocketAddress("plato.local", 1178)
    // 新しいClientアクターをシステムに追加
    system.actorOf(Props(classOf[Client], remoteAddress))
  }
}
/*
object Client {
  def props(remote: InetSocketAddress, replies: ActorRef) =
    Props(classOf[Client], remote, replies)
}

class Client(remote: InetSocketAddress, listener: ActorRef) extends Actor {
  
  import Tcp._
  import context.system

  //val manager = IO(Tcp)
  IO(Tcp) ! Connect(remote)
	
  def receive = {
    case CommandFailed(_: Connect) =>
      listener ! "connect failed"
      context stop self
		
    case c @ Connected(remote, local) =>
      listener ! c
      val connection = sender()
	  connection ! Register(self)
      context become {
        case data: ByteString =>
          connection ! Write(data)
        case CommandFailed(w: Write) =>
			// O/S buffer was full
          listener ! "write failed"
        case Received(data) =>
			listener ! data
        case "close" =>
			connection ! Close
        case _: ConnectionClosed =>
          listener ! "connection closed"
			context stop self
      }
  }
}

*/
