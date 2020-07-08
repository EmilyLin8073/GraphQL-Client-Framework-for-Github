package Builder_pattern

import GitHub.GitHubObj._
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.{CloseableHttpClient, HttpClientBuilder}


case class GHQLRespone(httpUriRequest: HttpPost, client: CloseableHttpClient )

object GitHub{
  //Phantom type of Github object
  sealed trait GitHubObj
  object GitHubObj {
    sealed trait EmptyObj extends GitHubObj
    sealed trait SetHttp extends GitHubObj
    sealed trait SetAuthorization extends GitHubObj
    sealed trait SetHeader extends GitHubObj
    type CompleteGitHubObj = EmptyObj with SetHttp with SetAuthorization with SetHeader
  }
}

class GitHub[GitHubObj <: GitHub.GitHubObj](private val httpUriRequest : HttpPost = null, private val client : CloseableHttpClient = HttpClientBuilder.create.build) extends LazyLogging{
  //Method for set up the Http
  def setHttp()(implicit ev: GitHubObj =:= GitHubObj with EmptyObj):(GitHub[GitHubObj with EmptyObj with SetHttp])= {
    //For loading the config file
    val conf: Config = ConfigFactory.load()

    val BASE_GHQL_URL = conf.getString("BASE_GHQL_URL")
    val httpUriRequest = new HttpPost(BASE_GHQL_URL)

    logger.debug("Building GitHub object with setHttp")
    new GitHub[GitHubObj with EmptyObj with SetHttp](httpUriRequest)
  }

  //Method for set up the authorization
  def setAuthorization(value : String, token : String)(implicit ev: GitHubObj =:= GitHubObj with EmptyObj with SetHttp): (GitHub[GitHubObj with EmptyObj with SetHttp with SetAuthorization]) = {
    httpUriRequest.addHeader("Authorization", value + token)
    logger.debug("Building GitHub object with setAuthorization")
    new GitHub[GitHubObj with EmptyObj with SetHttp with SetAuthorization](httpUriRequest,client)
  }

  //Method for set up the header
  def setHeader(name : String, value : String)(implicit ev: GitHubObj =:= GitHubObj with EmptyObj with SetHttp with SetAuthorization) : (GitHub[GitHubObj with EmptyObj with SetHttp with SetAuthorization with SetHeader]) = {
    httpUriRequest.addHeader(name, value)
    logger.debug("Building GitHub object with setHeader")
    new GitHub[GitHubObj with EmptyObj with SetHttp with SetAuthorization with SetHeader](httpUriRequest,client)
  }

  def build(implicit ev: GitHubObj =:= CompleteGitHubObj) : Option[GHQLRespone] = Option(GHQLRespone(httpUriRequest, client))
}