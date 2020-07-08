package Builder_pattern

import Builder_pattern.CommandUser.QueryUserCommand

case class User(username: String = "",
                url: String = "",
                reposCount: Int = 0,
                contribReposCount: Int = 0,
                followersCount: Int = 0,
                followingCount: Int = 0
               ) extends QueryUserCommand
