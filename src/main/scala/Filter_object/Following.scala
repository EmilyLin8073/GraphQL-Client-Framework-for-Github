package Filter_object

import Builder_pattern.User

object Following extends FilterObjects {
  def apply(userInput : String) : User => Boolean = x => predicateForFilter(userInput, x.followingCount)
}
