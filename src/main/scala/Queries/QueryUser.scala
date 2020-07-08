package Queries

object QueryUser {
  def apply(name: String, uType: String, first: Int): String = {

    val query = "query{search(query: \\\""  + name + "\\\", type:" + uType + ", first:" + first + "){edges{node{..." +
      " on User{login url repositories{totalCount} repositoriesContributedTo{totalCount} followers{totalCount} following{totalCount}}}}}}"

    query
  }
}
