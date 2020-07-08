package Builder_pattern

import java.util.Date


case class CommitComments(authorName: Option[String],
                           message: Option[String],
                           state: String = "",
                           pushedDate: Option[Date])
