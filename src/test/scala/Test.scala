import Builder_pattern.Command.QueryCommand
import Builder_pattern.{Command, GHQLRespone, GitHub, Repository, User}
import Builder_pattern.GitHub.GitHubObj.EmptyObj
import Filter_object.{Forks, Stars, Watchers}
import Parser.ParseRepoResponse
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils
import org.scalatest.FlatSpec

class Test extends FlatSpec {
  val token = "00f3cbc4f54fd97c3c422fdab6f50fb1606eb16a"
  val authorizationName = "Bearer "
  val headerName = "Accept"
  val jsonValue = "application/json"

  val gitHubObject: Option[GHQLRespone] = new GitHub[EmptyObj].setHttp().setAuthorization(authorizationName, token).setHeader(headerName, jsonValue).build

  val query = TestConstant.testQuery

  val gqlReq = new StringEntity("{\"query\":\"" + query + "\"}")

  gitHubObject.get.httpUriRequest.setEntity(gqlReq)

  val resp = gitHubObject.get.client.execute(gitHubObject.get.httpUriRequest)

  val response: String = EntityUtils.toString(resp.getEntity)


  //Testing the GitHub object
  "Github object" should "not be None" in {
    assert(gitHubObject != None)
  }

  //Testing the query response
  "Query response" should "not be null!" in {
    assert(response != null)
  }

  //Testing the query if the repo name is the same
  "Query" should "generate result" in {
    val result: Seq[Repository] = (new ParseRepoResponse).processResult(response)

    assert(result.exists(x => x.name == "\"CS-Notes\""))
  }

  //Testing if the fork count is greater than 21584
  "Fork count" should "be greater than 21584" in {
    val result = gitHubObject.flatMap(new Command[QueryCommand].setRepo(TestConstant.QUERY_TYPE).setLanguages(TestConstant.languageList).setFirst(TestConstant.setFirst).setCommitComments("first", 2).build())
      .get.filter(Forks("_>21584"))

    assert(result.exists(x => x.forkCount > 21584))
  }

  //Testing if the watcher count is greater than 796
  "Watcher count" should "be greater than 796" in {
    val result = gitHubObject.flatMap(new Command[QueryCommand].setRepo(TestConstant.QUERY_TYPE).setLanguages(TestConstant.languageList).setFirst(TestConstant.setFirst).setCommitComments("first", 2).build())
      .get.filter(Watchers("_>796"))

    assert(result.exists(x => x.watchersCount > 796))
  }

  //Testing if the stars count is greater than 25882
  "Stars count" should "be greater than 25882" in {
    val result = gitHubObject.flatMap(new Command[QueryCommand].setRepo(TestConstant.QUERY_TYPE).setLanguages(TestConstant.languageList).setFirst(TestConstant.setFirst).setCommitComments("first", 2).build())
      .get.filter(Stars("_>25882"))

    assert(result.exists(x => x.starsCount > 25882))
  }

  //Testing if the commits count is greater than 1056
  "Commits count" should "be greater than 1056" in {
    val result = gitHubObject.flatMap(new Command[QueryCommand].setRepo(TestConstant.QUERY_TYPE).setLanguages(TestConstant.languageList).setFirst(TestConstant.setFirst).setCommitComments("first", 2).build())
      .get.filter(Stars("_>1056"))

    assert(result.exists(x => x.commits > 1056))
  }

  //Testing repository instantiation
  "Repsitory" should "have correct fields" in {
    val repo = Repository("COURSE PROJECT", Option("474 final project"), Option("www.dummyweb.com"))

    assert(repo.name == "COURSE PROJECT")
    assert(repo.watchersCount == 0)
    assert(repo.forkCount == 0)
    assert(repo.starsCount == 0)
    assert(repo.releaseCount == 0)
    assert(repo.commits == 0)
    assert(repo.issuesCount == 0)
    assert(repo.commitComments == List())
    assert(repo.description != None)
    assert(repo.githubUrl != None)
    assert(repo.pullRequest == List())
  }

  //Testing user instantiation
  "User" should "have correct fields" in {
    val user = User("Emily", null, 10)

    assert(user.username == "Emily")
    assert(user.url == null)
    assert(user.reposCount == 10)
    assert(user.followersCount == 0)
    assert(user.followingCount == 0)
    assert(user.contribReposCount == 0)
  }
}
