package game

import scala.collection.mutable.Buffer
import scala.util.Random

class World(width: Int, height: Int) {
  
  val squareSize = 10
  
  
  var world: Array[Array[Spot]] = Array.fill(height*squareSize, (width*squareSize))(Floor)
  var firstSquares: Array[Array[Spot]] = Array.fill(squareSize,(width*squareSize))(Wall)
  var trueFalseTable = Buffer[Boolean]()
  val r = scala.util.Random
  
  //ESSENTIAL FUNCTIONS
  //-------------------------------------------------------------------------------------------------------------------
  def moveWorldDown = {
    var line = height * squareSize - 1
    while(line > 0){
      world(line) = world(line - 1)
      line -=1
    }
      
  }
  
  def createHoledLine = {
      
      do{
        println("loop")
        val randomNum = 8 + r.nextInt(width - 8)
        var crawler = 0
        while(crawler < randomNum){
          var randomNumTreeni = 1 + r.nextInt(width)
          var randomNum2 = (squareSize * randomNumTreeni)
          var squareLowerLimit = randomNum2- squareSize
          for(line <- 0  until squareSize){
            for(place <- squareLowerLimit until randomNum2-1){
              println(firstSquares(0).length)
              firstSquares(line)(place) = Floor
            }
          }
          crawler += 1
          }   
      }while(checkIfAcceptable == false)
  
  }
  
  def makeTrueFalseArray = {
    trueFalseTable = Buffer[Boolean]()
    var row = 0
    while(row < width * squareSize){
       if((world(1)(row) == Wall && world(1 + squareSize)(row) == Wall) || (world(1 + squareSize)(row) == Wall && world(1 + 2 * squareSize)(row) == Wall) || (world(1 + 2 * squareSize)(row) == Wall && world(1 + 3 * squareSize)(row) == Wall)  ){
          trueFalseTable += false
       }else{
         trueFalseTable += true
       }
       row += 1
    }
  }
  
  def checkIfAcceptable = {
    var acceptable = false
    for(number <- 0 to width * squareSize -1 ){
      if(trueFalseTable(number) && world(1)(number) == Floor){
        acceptable = true
      }
    }
    
    acceptable
  }
  
  def getWorld = world
}