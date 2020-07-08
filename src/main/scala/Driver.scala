import Builder_pattern.Command.QueryCommand
import Builder_pattern.CommandUser.QueryUserCommand
import Builder_pattern.GitHub.GitHubObj.EmptyObj
import Builder_pattern.{Command, CommandUser, GHQLRespone, GitHub, Repository, User}
import Filter_object.{ContribRepos, Forks, Repos, Stars, Watchers}
import com.typesafe.scalalogging.LazyLogging
import com.typesafe.config.{Config, ConfigFactory}
import scala.jdk.CollectionConverters


object Driver extends App with LazyLogging{
  //For loading the config file
  val conf: Config = ConfigFactory.load()

  //For Github object
  val token = conf.getString("token")
  val authorizationName = conf.getString("authorizationName")
  val headerName = conf.getString("headerName")
  val jsonValue = conf.getString("jsonValue")

  //For building the query -> REPOSITORY
  val queryType = conf.getString("queryType")
  val language = CollectionConverters.ListHasAsScala(conf.getStringList("language")).asScala.toList
  val first = conf.getInt("first")
  val commitCommentsFirst = conf.getString("commitCommentsFirst")
  val commitCommentsValue = conf.getInt("commitCommentsValue")

  //For building the query -> USER
  val qType = conf.getString("qType")
  val name = conf.getString("name")

  val gitHubObject: Option[GHQLRespone] = new GitHub[EmptyObj].setHttp().setAuthorization(authorizationName, token).setHeader(headerName, jsonValue).build

  logger.info("GitHub object built")

  val result = gitHubObject.flatMap(new Command[QueryCommand].setRepo(queryType).setLanguages(language).setFirst(first).setCommitComments(commitCommentsFirst, commitCommentsValue).build())
    .get.filter(Stars("_>60000")).filter(Forks("_>32000")).filter(Watchers("_>2000"))

  val result2 = gitHubObject.flatMap(new CommandUser[QueryUserCommand].setUser(qType).setName(name).setFirst(first).build())
    .get.filter(Repos("_>100")).filter(ContribRepos("_>40"))

  logger.info("Repo result generated")

  FancyPrinter.printRepoResult(result)
  FancyPrinter.printUserResult(result2)
}
