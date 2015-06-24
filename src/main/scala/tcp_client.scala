package net.homeunix.akimichi.skkserv_benchmark.connection

import akka.actor._
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await

object TCPClient {
  case class DataReceived(buffer: ByteString)
  case class ConnectionClosed()
  case class CloseConnection()
  case class SendData(bytes: ByteString)
  def apply(ip: String, port: Int, btProtocol: ActorRef) = new TCPClient(ip: String, port: Int, btProtocol: ActorRef)
}

class TCPClient(ip: String, port: Int, btProtocol: ActorRef) extends Actor with ActorLogging {
  import Tcp._
  import context.system
  import TCPClient._

  implicit val timeout = Timeout(5.seconds)
  // val socket = IOManager(context.system) connect (ip, port) //Ip, port
  var buffer: ByteString = akka.util.ByteString()

  def receive = {
    case _: Tcp.ConnectionClosed =>
	  val connection = sender
      btProtocol ! ConnectionClosed
	  connection ! Close
      // socket.close
    case IO.Read(_, bytes) =>
      buffer = buffer ++ bytes
      var bytesRead = 0
      do {
        bytesRead = parseFrame(buffer) match {
          case (0, _) => 0
          case (n, None) => n // this case can happen (keep-alive message)
          case (n, Some(m)) => btProtocol ! DataReceived(m); n
        }
        buffer = buffer.drop(bytesRead)
      } while (bytesRead > 0)
    case SendData(bytes) =>
	  val connection = sender
	  connection ! Write(bytes)
      // socket write bytes
    case CloseConnection =>
	  val connection = sender
      btProtocol ! ConnectionClosed
	  connection ! Close
      // socket.close
  }
}

trait TCPClientProvider {
  def newTCPClient(ip: String, port: Int, peer: ActorRef): Actor = TCPClient(ip, port, peer)
}
