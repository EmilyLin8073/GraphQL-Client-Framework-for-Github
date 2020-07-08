package Filter_object

import Builder_pattern.Repository

object Watchers extends FilterObjects {
  def apply(userInput : String) : Repository => Boolean = x => predicateForFilter(userInput, x.watchersCount)
}
