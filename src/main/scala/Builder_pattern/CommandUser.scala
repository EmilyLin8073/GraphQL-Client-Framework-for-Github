package Builder_pattern

import CommandUser.QueryUserCommand.{CompleteQueryUserCommand, FirstC, UserC, UserN}
import Parser.ParseUserResponse
import Queries.QueryUser
import com.typesafe.scalalogging.LazyLogging
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils

object CommandUser{
  trait QueryUserCommand
  object QueryUserCommand {
    trait UserC extends QueryUserCommand
    trait UserN extends QueryUserCommand
    trait FirstC extends QueryUserCommand

    type CompleteQueryUserCommand = UserC with UserN with FirstC
  }
}

class CommandUser[QueryUserCommand <: CommandUser.QueryUserCommand](private val utype : String = "",
                                                                    private val uname: String = "",
                                                                    private val first : Int = 0) extends LazyLogging{
  //Method for set up the user for the query
  def setUser(userIn: String)(implicit ev: QueryUserCommand =:= QueryUserCommand): (CommandUser[QueryUserCommand with UserC]) = {
    new CommandUser[QueryUserCommand with UserC](userIn)
  }

  //Method for set up the user name for the query
  def setName(un: String)(implicit ev: QueryUserCommand =:= QueryUserCommand with UserC): (CommandUser[QueryUserCommand with UserC with UserN]) = {
    new CommandUser[QueryUserCommand with UserC with UserN](utype, un)
  }

  //Method for set up the number of repo in the query
  def setFirst(j: Int)(implicit ev: QueryUserCommand =:= QueryUserCommand with UserC with UserN): (CommandUser[QueryUserCommand with UserC with UserN with FirstC]) = {
    new CommandUser[QueryUserCommand with UserC with UserN with FirstC](utype, uname, j)
  }

  //Method for building the commandUser object
  def build()(implicit ev: QueryUserCommand =:= CompleteQueryUserCommand): GHQLRespone => Option[List[User]] = y => {
    val queryU = QueryUser(uname, utype, first)

    val gqlReqC = new StringEntity("{\"query\":\"" + queryU + "\"}")

    y.httpUriRequest.setEntity(gqlReqC)

    val resp = y.client.execute(y.httpUriRequest)

    val response: String = EntityUtils.toString(resp.getEntity)

    val result: List[User] = (new ParseUserResponse).processUserResult(response)

    logger.info("Generating the user repo")

    Option(result)
  }
}
