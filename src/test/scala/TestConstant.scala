object TestConstant {
  val languageList = List("scala")

  val QUERY_TYPE = "REPOSITORY"

  val setFirst = 10

  val testQuery = "{search(query:\\\"language:java, sort:stars\\\", type: REPOSITORY, first: 1) " +
    "{repositoryCount edges { node {... on Repository { url forkCount name description owner " +
    "{ url... on User { name avatarUrl email followers { totalCount}}}}}}}}"
}