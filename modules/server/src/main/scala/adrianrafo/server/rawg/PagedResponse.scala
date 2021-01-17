package adrianrafo.server.rawg

import io.circe._
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.Uri
import org.http4s.circe._

final case class PagedResponse[A](count: Int, next: Option[Uri], previous: Option[Uri], results: List[A])

object PagedResponse {
  implicit def pagedResponseDecoder[A: Decoder]: Decoder[PagedResponse[A]] = deriveDecoder
}
