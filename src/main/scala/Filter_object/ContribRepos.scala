package Filter_object

import Builder_pattern.User

object ContribRepos extends FilterObjects {
  def apply(userInput : String) : User => Boolean = x => predicateForFilter(userInput, x.contribReposCount)
}
