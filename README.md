# GraphQL-Client-Framework-for-Github

## Description

object-oriented pure functional design and implementation of a [GraphQL](https://graphql.org) client framework for [Github](https://github.com/) as an I/O monad

## Functionality

In this project, we will be creating a pure functional object-oriented framework for composing and executing external GraphQL commands using Scala.
Users will be using our framework for generating the information of GitHub repositories.
Users can define the languages, how many repositories they want to check, set how many commit comments they want to see and search the repositories based on REPOSITORY or USER.
The result will be saved in the output.txt file in a format that is easy for users to see. 

## How to use

**_Here is the query we are using_**


```scala
  val query = "query{" +
      "search(query: \\\"language:" + languages.foldRight("")((x, y) => x + "," + y) + "sort:stars\\\", type:" + repoType + ", first: " + first + ") {" +
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
      "       totalCount" +
      "    }" +
      "   }" +
      "  }" +
      "  releases {" +
	  "   totalCount" +
      "  }" +
      "   stargazers {" +
      "    totalCount" +
      "   }" +
      "   watchers {" +
      "    totalCount" +
      "   }" +
      "  commitComments(" + commitComments._1 + ":" + commitComments._2 + ") {" +
      "   totalCount" +
      "   edges {" +
      "    node {" +
      "     commit {" +
      "      author {" +
      "       name" + //The name in the git commit
      "      }" +
      "      message" +
      "      status {" +
      "        state" +
      "       }" +
      "       pushedDate" +
      "      }" +
      "      author {" +
      "       login" +                  //The name of the actor
      "      }" +
      "      createdAt" +
      "      bodyText" +
      "     }" +
      "    }" +
      "   }" +
      "   pullRequests(" + pullRequests._1 + ":" + pullRequests._2 + ") {" +
      "    totalCount" +
      "    edges {" +
      "     node {" +
      "      createdAt" +
      "      title" +
      "      state" +
      "      author {" +
      "       login" +
      "      }" +
      "     }" +
      "    }" +
      "   }" +
      "   issues(" + issues._1 + ":" + issues._2 + ") {" +
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
  
```

**1.Instantiate a GitHub object**

We used builder pattern with Phantom types to type check at compile-time. 
In order to pass the type check methods must called in the following order:

1. def setHttp()(implicit ev: GitHubObj =:= GitHubObj with EmptyObj):(GitHub[GitHubObj with EmptyObj with SetHttp])
1. def setAuthorization(value : String, token : String)(implicit ev: GitHubObj =:= GitHubObj with EmptyObj with SetHttp): (GitHub[GitHubObj with EmptyObj with SetHttp with SetAuthorization])
1. def setHeader(name : String, value : String)(implicit ev: GitHubObj =:= GitHubObj with EmptyObj with SetHttp with SetAuthorization) : (GitHub[GitHubObj with EmptyObj with SetHttp with SetAuthorization with SetHeader])
1. def build(implicit ev: GitHubObj =:= CompleteGitHubObj) : Option[GHQLRespone] = Option(GHQLRespone(httpUriRequest, client))

The first method sets up the httpUriRequest and adds it to the GitHub object.
The second adds the authorization token.
The third adds a header to accept the JSON value.
The build() returns a tuple of (httpUriRequest : HttpPost,client : CloseableHttpClient)

**2.Create Command object, set Repo, Languages, First x results and CommitComments**  

This object will build a query with given inputs and returns GHQLRespone => Option[List[Repository]]  
This allows users to be able to call monadic functions like flatMap and modify the type of query.


