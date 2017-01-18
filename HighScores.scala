package game

import scala.collection.mutable.ArrayBuffer

class HighScores(var score:Int){
  var highScoreArray = new ArrayBuffer[Int]()
  
  def update(newScore:Int): Int={
    score = newScore
    return score
    
  }
  def getScore():Int={
    return score
  }
  
  def addScoreToArray(latestScore:Int)={
    highScoreArray.+=(latestScore)
  }
  
  def getFinalScore()={
    highScoreArray.last
  }
}