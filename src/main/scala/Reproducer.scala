import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// Request defining its response type
trait Request {
  type Response
}

// Client for requests of type Req returning responses of type Req#Response
trait Client[Req <: Request] {
  type BaseRequest = Req

  def request[Request <: BaseRequest](req: Request): Future[req.Response] // Request must be a subtype of Client#BaseRequest
}

// Base type for all api requests
sealed trait APIRequest extends Request {
  override type Response <: APIResponse
}
sealed trait APIResponse

// Wraps the request
case class WrappedRequest[+R <: Request](wrapped: R) extends Request {
  override type Response = WrappedResponse[wrapped.Response]
}

case class WrappedResponse[+Req]()

object Main {
  val wrappedApiClient: Client[WrappedRequest[APIRequest]] = ???

  val request: APIRequest = ???
  val wrappedRequest: WrappedRequest[APIRequest] = WrappedRequest(request)

  val result /*: Future[WrappedResponse[APIResponse]]*/ = for { // Uncommenting the type fixes case 1
    _ <- wrappedApiClient.request(wrappedRequest)

    // Case 1
    newWrappedRequest = WrappedRequest(request) // Creating a new request inside the for comprehension
    response <- wrappedApiClient.request(newWrappedRequest) // Error, unless the type of result is made explicit

    // Case 2
    // response <- wrappedApiClient.request(WrappedRequest(request)) // Error, even when type of result is explicit

    // No error:
    //response <- wrappedApiClient.request(wrappedRequest) // Reusing the request defined above. No error

  } yield response
}