package Builder_pattern

import Builder_pattern.Command.QueryCommand

case class Repository(name: String = "",
                       description: Option[String],
                       githubUrl: Option[String],
                       forkCount: Int = 0,
                       starsCount: Int = 0,
                       issuesCount: Int = 0,
                       watchersCount: Int = 0,
                       pullRequests: Int = 0,
                       releaseCount: Int = 0,
                       commits: Int = 0,
                       pullRequest: List[PullRequest] = Nil,
                       commitComments: List[CommitComments] = Nil) extends QueryCommand
