package Queries

object Query {

  def apply(languages: List[String], repoType: String, first: Int, commitComments: (String, Int),
            pullRequests: (String, Int), issues: (String, Int)): String = {

    val query = "query{search(query: \\\"language:" + languages.foldRight("")((x, y) => x + "," + y) + "sort:stars\\\", type:" + repoType + ", first:" + first + "){repositoryCount edges{node{..." +
      " on Repository{url forkCount name description owner{url ... on User{followers{totalCount}}} releases{totalCount}" +
      " commitComments(" + commitComments._1 + ":" + commitComments._2 + "){totalCount edges{node{commit{author{name} message status{state} pushedDate} " +
      "author{login avatarUrl url} createdAt bodyText}}} pullRequests(" + pullRequests._1 + ":" + pullRequests._2 +
      "){totalCount edges{node{bodyText createdAt title state author{login}}}} " +
      "stargazers{totalCount} watchers{totalCount} issues(" + issues._1 + ":" + issues._2 + "){totalCount }}}}}}"

    query

  }
}
