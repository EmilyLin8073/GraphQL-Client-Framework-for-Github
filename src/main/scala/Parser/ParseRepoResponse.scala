package Parser

import java.util.{Date, NoSuchElementException}

import Builder_pattern.{CommitComments, PullRequest, Repository}
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json._

import scala.collection.mutable.ListBuffer


class ParseRepoResponse extends LazyLogging{

  //Method for putting the repo in a list
  def processResult(queryResult : String) : List[Repository] = {
    //Parse the json string to json object
    val responseJson = Json.parse(queryResult)

    //Extract all the repository nodes
    val repoNodes = (responseJson \ "data" \ "search" \ "edges").get.as[JsArray]

    val repositories = new ListBuffer[Repository]

    for (repoNode <- repoNodes.value) {
      val listPullRequest = createPullRequest(repoNode)
      val listCommitComments = createCommitComment(repoNode)
      repositories += createRepo(repoNode, listPullRequest, listCommitComments)
    }

    logger.info("Generating the repo result")
    repositories.toList
  }

  //Method for creating the pull request
  private def createPullRequest(jValue: JsValue) = {
    val repos = new ListBuffer[PullRequest]

    try {
      val repoNode = jValue \ "node"
      val pullRequest = (repoNode \ "pullRequests" \ "edges").get.as[JsArray]

      for (pr <- pullRequest.value) {
        val prNode = pr \ "node"

        val repPr = PullRequest(authorUserName = (prNode \ "author" \ "login").asOpt[String],
          createdAt = (prNode \ "createdAt").asOpt[Date], state = (prNode \ "state").asOpt[String], title = (prNode \ "title").asOpt[String])

        repos += repPr

      }
      logger.info("")
      repos.toList
    }
    catch {
      case e: NoSuchElementException => repos.toList
    }
  }

  private def createCommitComment(jValue: JsValue) = {
    val repos = new ListBuffer[CommitComments]

    try{
      val repoNode = jValue \ "node"
      val commitComments = (repoNode\ "commitComments"\ "edges").get.as[JsArray]

      for(cc <- commitComments.value) {
        val ccNode = cc \ "node"

        val repComment = CommitComments(authorName = (ccNode \ "commit"\ "author"\ "name" ).asOpt[String],
          message = (ccNode \ "commit"\ "message").asOpt[String],
          state = (ccNode \ "commit"\ "status"\ "state").getOrElse(JsString("null")).toString(),
          pushedDate = (ccNode \ "commit"\ "pushedDate").asOpt[Date])


        repos += repComment
      }
      repos.toList
    }
    catch {
      case e: NoSuchElementException => repos.toList
    }

    }

  private def createRepo(jValue: JsValue, listPullRequest: List[PullRequest], listCommitComments: List[CommitComments]): Repository = {
    val value = jValue \ "node"

    Repository(name = (value \ "name").get.toString(),
      description = (value \ "description").asOpt[String],
      githubUrl = (value \ "url").asOpt[String],
      forkCount = (value \ "forkCount").as[Int],
      starsCount = (value \ "stargazers" \ "totalCount").getOrElse(JsNumber(0)).as[Int],
      issuesCount = (value \ "issues" \ "totalCount").getOrElse(JsNumber(0)).as[Int],
      watchersCount = (value \ "watchers" \ "totalCount").getOrElse(JsNumber(0)).as[Int],
      pullRequests = (value \ "pullRequests" \ "totalCount").getOrElse(JsNumber(0)).as[Int],
      releaseCount = (value \ "releases" \ "totalCount").getOrElse(JsNumber(0)).as[Int],
      commits = (value \ "commitComments" \ "totalCount").getOrElse(JsNumber(0)).as[Int],
      pullRequest = listPullRequest,
      commitComments = listCommitComments)
  }
}
