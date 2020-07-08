package Parser

import Builder_pattern.User
import play.api.libs.json.{JsArray, JsValue, Json}

import scala.collection.mutable.ListBuffer

class ParseUserResponse {

  //Method for putting the repo in a list
  def processUserResult(queryResult: String) : List[User] = {
    //Parse the json string to json object
    val responseJson = Json.parse(queryResult)

    //Extract all the repository nodes
    val userNodes = (responseJson \ "data" \ "search" \ "edges").get.as[JsArray]

    val topUsers = new ListBuffer[User]

    for (userNode <- userNodes.value) {
      topUsers += createUser(userNode)
    }

    topUsers.toList
  }

  private def createUser(jsValue: JsValue): User = {
    val value = jsValue \ "node"

    User(username = (value \ "login").get.toString,
      url = (value \ "url").get.toString,
      reposCount = (value \ "repositories" \ "totalCount").as[Int],
      contribReposCount = (value \ "repositoriesContributedTo" \ "totalCount").as[Int],
      followersCount = (value \ "followers" \ "totalCount").as[Int],
      followingCount = (value \ "following" \ "totalCount").as[Int]
    )
  }
}
