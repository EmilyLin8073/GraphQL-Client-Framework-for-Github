package Builder_pattern

import Command.QueryCommand.{CompleteQueryCommand, First, Languages, Repo}
import Parser.ParseRepoResponse
import Queries.Query
import com.typesafe.scalalogging.LazyLogging
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils


object Command{
  trait QueryCommand
  object QueryCommand{
    trait Repo extends QueryCommand
    trait Languages extends QueryCommand
    trait First extends QueryCommand

    type CompleteQueryCommand = Repo with Languages with First
  }
}

class Command[QueryCommand <: Command.QueryCommand](private val repoType : String = "",
                                                    private val languages : List[String] = null,
                                                    private val first: Int = 0,
                                                    private val commitComments: (String, Int)  = ("last", 5),
                                                    private val pullRequests: (String, Int)  = ("last", 5),
                                                    private val issues: (String, Int)  = ("last", 5)) extends LazyLogging{

  //Method for set up the repo
  def setRepo(repo : String)(implicit ev : QueryCommand =:= QueryCommand) : (Command[QueryCommand with Repo]) = {
    logger.debug("Building repo with setRepo")
    new Command[QueryCommand with Repo](repo)
  }

  //Method for set up the language for the query
  def setLanguages(language : List[String])(implicit ev : QueryCommand =:= QueryCommand with Repo) : (Command[QueryCommand with Repo with Languages]) = {
    logger.debug("Building repo with setLanguages")
    new Command[QueryCommand with Repo with Languages](repoType, language)
  }

  //Method for set up the number of repo in the query
  def setFirst(i : Int)(implicit ev : QueryCommand =:= QueryCommand with Repo with Languages) : (Command[QueryCommand with Repo with Languages with First]) = {
    logger.debug("Building repo with setFirst")
    new Command[QueryCommand with Repo with Languages with First](repoType, languages, i)
  }

  //Method for set up the commit comments number in the query
  def setCommitComments(i : (String, Int)): (Command[QueryCommand with Repo with Languages with First]) = {
    logger.debug("Building repo with setCommitComments")
    new Command[QueryCommand with Repo with Languages with First](repoType, languages, first, i)
  }

  //Method for set up the pull request number in the query
  def setPullRequests(i: (String, Int)): (Command[QueryCommand with Repo with Languages with First]) = {
    logger.debug("Building repo with setPullRequests")
    new Command[QueryCommand with Repo with Languages with First](repoType, languages, first, commitComments, i)
  }

  //Method for set up the issues number in the query
  def setIssues(i: (String, Int)): (Command[QueryCommand with Repo with Languages with First]) = {
    logger.debug("Building repo with setIssues")
    new Command[QueryCommand with Repo with Languages with First](repoType, languages, first, commitComments, pullRequests, i)
  }

  //Method for building the command object
  def build()(implicit ev: QueryCommand =:= CompleteQueryCommand): GHQLRespone => Option[List[Repository]] = x => {

    //Set up the query
    val query = Query(languages, repoType, first, commitComments, pullRequests, issues)

    //Get the repo
    val gqlReq = new StringEntity("{\"query\":\"" + query + "\"}")

    //Set the entity for the repo
    x.httpUriRequest.setEntity(gqlReq)

    //Request the httpUri
    val resp = x.client.execute(x.httpUriRequest)

    //Get the response as string
    val response: String = EntityUtils.toString(resp.getEntity)

    //Get the parsed result
    val result: List[Repository] = (new ParseRepoResponse).processResult(response)

    logger.debug("Generating the repo")

    Option(result)
  }
}

/*
val query = "{" +
  "search(query: \"language:" + language + "sort:stars\", type: REPOSITORY, first: " + number + ") {" +
  "repositoryCount" +
  "edges {" +
  " node {" +
  "  ... on Repository {" +
  "  url" +
  "  forkCount" +
  "  name" +
  "  description" +
  "  owner {" +
  "  url" +
  "  ... on User {" +
  "     followers {" +
  "     totalCount" +
  "    }" +
  "   }" +
  "  }" +
  "  releases {" +
  "   totalCount" +
  "  }" +
  "  commitComments(last: 5) {" +
  "   totalCount" +
  "   edges {" +
  "    node {" +
  "     commit {" +
  "      author {" +
  "       name" +
  "      }" +
  "      message" +
  "      status {" +
  "        state" +
  "       }" +
  "       pushedDate" +
  "      }" +
  "      author {" +
  "       login" +
  "       avatarUrl" +
  "       url" +
  "      }" +
  "      createdAt" +
  "      bodyText" +
  "     }" +
  "    }" +
  "   }" +
  "   pullRequests(last: 5) {" +
  "    totalCount" +
  "    edges {" +
  "     node {" +
  "      bodyText" +
  "      createdAt" +
  "      url" +
  "      title" +
  "      state" +
  "      merged" +
  "      author {" +
  "       login" +
  "       avatarUrl" +
  "       url" +
  "      }" +
  "     }" +
  "    }" +
  "   }" +
  "   stargazers {" +
  "    totalCount" +
  "   }" +
  "   watchers {" +
  "    totalCount" +
  "   }" +
  "   issues(last: 5) {" +
  "    totalCount" +
  "    edges {" +
  "     node {" +
  "      __typename" +
  "      ... on Issue {" +
  "         title" +
  "         body" +
  "         url" +
  "         createdAt" +
  "         author {" +
  "          url" +
  "          resourcePath" +
  "          __typename" +
  "          ... on Actor {" +
  "             url" +
  "             login" +
  "             avatarUrl" +
  "            }" +
  "           }" +
  "          }" +
  "         }" +
  "        }" +
  "       }" +
  "      }" +
  "     }" +
  "    }" +
  "   }" +
  "  }"
 */
