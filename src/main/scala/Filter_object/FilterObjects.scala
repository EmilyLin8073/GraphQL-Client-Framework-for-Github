package Filter_object

class FilterObjects {
  //Method for matching the symbol case
  def predicateForFilter(userInput: String, value: Int) : Boolean = {
    //Parse the user input into a list of character
    val parse: List[Char] = userInput.toList

    //Get the greater or less symbol
    val symbol = getGreaterOrLesser(parse)

    //Get the integer
    val intVal = getInt(parse)

    symbol match{
      case ">" => if(value > intVal) true else false
      case "<" => if(value < intVal) true else false
    }
  }

  //Parsing to get the greater or less sign
  private def getGreaterOrLesser(parsedInput: List[Char]): String = {
    parsedInput.filter(x => x == '>' || x == '<') .mkString
  }

  //Parsing to get the integer
  private def getInt(parsedInput: List[Char]) = {
    parsedInput.filter(x => x != '_' && x != '>' && x != '<').mkString.toInt
  }
}


