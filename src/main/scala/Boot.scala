import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object Boot extends App with Route {
  implicit lazy val system = ActorSystem("my-system")
  implicit lazy val materializer = ActorMaterializer()
  override implicit val db = MockDB
  implicit val ec = system.dispatcher
  val interface = Configuration.interface
  val port = Configuration.port
  val logger = Logging(system, getClass)
  val binding = Http().bindAndHandle(routes, interface, port)

  info()

  binding.onFailure {
    case err: Exception =>
      logger.error(err, s"Failed to bind to $interface $port")
  }

  private def info(): Unit = {
    logger.info(s"  - Configuration: Start server at $interface $port on ActorSystem(${system.name})")
  }
}
