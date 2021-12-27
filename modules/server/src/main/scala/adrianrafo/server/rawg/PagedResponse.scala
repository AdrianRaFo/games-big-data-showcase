package adrianrafo.server.rawg

import adrianrafo.server.rawg.game.Game
import io.circe._
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.Uri
import org.http4s.circe.decodeUri

final case class PagedResponse[A](count: Int, next: Option[Uri], previous: Option[Uri], results: List[A])

object PagedResponse {
  implicit val gamePagedResponseDecoder: Decoder[PagedResponse[Game]] = deriveDecoder
}
