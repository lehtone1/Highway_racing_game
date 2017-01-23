package game

import scala.collection.mutable.Buffer
import scala.util.Random

class World(width: Int, height: Int) {
  
  val squareSize = 10
  
  
  var world: Array[Array[Spot]] = Array.fill(height*squareSize, (width*squareSize))(Road)
  var firstSquares: Array[Array[Spot]] = Array.fill(squareSize,(width*squareSize))(Enemy)
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
        val randomNum = 8 + r.nextInt(width - 8)
        var crawler = 0
        while(crawler < randomNum){
          var randomNumTreeni = 1 + r.nextInt(width)
          var randomNum2 = (squareSize * randomNumTreeni)
          var squareLowerLimit = randomNum2- squareSize
          for(line <- 0  until squareSize){
            for(place <- squareLowerLimit until randomNum2-1){
              firstSquares(line)(place) = Road
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
       if((world(0)(row) == Enemy && world(1 + squareSize)(row) == Enemy) 
           || (world(1 + squareSize)(row) == Enemy && world(1 + 2 * squareSize)(row) == Enemy) 
           || (world(1 + 2 * squareSize)(row) == Enemy && world(1 + 3 * squareSize)(row) == Enemy)  ){
          trueFalseTable += false
       }else{
         trueFalseTable += true
       }
       row += squareSize
    }
  }
  
  def checkIfAcceptable = {
    var acceptable = false
    for(number <- 0 until width){
      if(trueFalseTable(number) && firstSquares(0)(number*squareSize) == Road){
        acceptable = true
      }
    }
    
    acceptable
  }
  
  def getWorld = world
}