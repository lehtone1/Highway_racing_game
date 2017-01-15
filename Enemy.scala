package game

import scala.swing._
import scala.swing.event._

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File

import scala.util.Random

class Enemy(var x: Int, var y: Int, img_src: String) {
  val width = game.MainFrame2.width
  val cellSizeX = game.MainFrame2.cellSizeX
  val cellSizeY = game.MainFrame2.cellSizeY
  
  var possibleXpositions = List.tabulate(width)(x => x*cellSizeX)
  
 
  var x2 = x+cellSizeX
  var y2 = y+cellSizeY
  
  def getImage()={
    ImageIO.read(new File(img_src))
  }
  
  def animateEnemyMovement()={
    y+=1
    y2+=1
    if(enemyOutOfBounds){
      x=Random.shuffle(possibleXpositions).head
      x2 = x+cellSizeX
      y=0-1000
      y2 = y+cellSizeY
      
    }
    
    
  }
  
  def enemyOutOfBounds():Boolean ={
    var returnVal = false
    if(y>=game.MainFrame2.FRAME_H){
      returnVal = true
    }
    return returnVal
  }
  
}