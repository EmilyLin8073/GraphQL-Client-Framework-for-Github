package Builder_pattern

import java.util.Date

import Builder_pattern.Command.QueryCommand

case class PullRequest(authorUserName: Option[String],
                        createdAt: Option[Date],
                        state: Option[String],
                        title: Option[String]) extends QueryCommand
