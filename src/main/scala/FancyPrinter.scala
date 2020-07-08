import java.io.{File, PrintWriter}
import Builder_pattern.{Repository, User}
import com.typesafe.config.{Config, ConfigFactory}

object FancyPrinter {
  //For loading the config file
  val conf: Config = ConfigFactory.load()

  //Printing out the result if the repo type is REPOSITORY
  def printRepoResult(rps: Seq[Repository]): Unit = {
    //For writing the output to a file
    val writer = new PrintWriter(new File(conf.getString("fileName")))

    for(i <- rps) {
      writer.write("Repository name: " + i.name + "\n")
      writer.write("Description: " + i.description.getOrElse() + "\n")
      writer.write("URL: " + i.githubUrl.getOrElse() + "\n")
      writer.write("Forks: " + i.forkCount + "\n")
      writer.write("Stars: " + i.starsCount + "\n")
      writer.write("Issues: " + i.issuesCount + "\n")
      writer.write("Watchers: " + i.watchersCount + "\n")
      writer.write("Pull requests: " + i.pullRequests + "\n")
      writer.write("Releases: " + i.releaseCount + "\n")
      writer.write("Commits: " + i.commits + "\n\n")
      for(j <- i.pullRequest) {
        writer.write("Pull request author: " + j.authorUserName.getOrElse() + "\n")
        writer.write("Pull request creation date: " + j.createdAt.getOrElse() + "\n")
        writer.write("Pull request state: " + j.state.getOrElse() + "\n")
        writer.write("Pull request title: " + j.title.getOrElse() + "\n\n")
      }
      for(k <- i.commitComments) {
        writer.write("Commit comment author: " + k.authorName.getOrElse() + "\n")
        writer.write("Commit comment message: " + k.message.getOrElse() + "\n")
        writer.write("Commit comment state: " + k.state + "\n")
        writer.write("Commit comment push date: " + k.pushedDate.getOrElse() + "\n\n")
      }
    }
    writer.close()
  }

  //Printing out the result if the repo type is USER
  def printUserResult(usrs: Seq[User]): Unit = {
    //For writing the output to a file
    val writer = new PrintWriter(new File(conf.getString("fileName2")))

    for (i <- usrs) {
      writer.write("Username: " + i.username + "\n")
      writer.write("URL: " + i.url + "\n")
      writer.write("Repositories: " + i.reposCount + "\n")
      writer.write("Contributed repositories: " + i.contribReposCount + "\n")
      writer.write("Followers: " + i.followersCount + "\n")
      writer.write("Following: " + i.followingCount + "\n\n")
    }
    writer.close()
  }
}
