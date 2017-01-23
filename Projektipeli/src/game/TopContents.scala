package game

import scala.swing.BorderPanel.Position._
import scala.swing._
import scala.swing.event._

import java.io.File
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



class TopContents(height:Int,width:Int,cellSizeX:Int,cellSizeY:Int,squareSize:Int){
  var visibility = false
  var background_IMG = ImageIO.read(new File("images/highwayInMiddle1.png"))
  val world: Array[Array[Spot]] = Array.fill(height*squareSize, (width*squareSize))(Road)
  val myColor = new Color(255,255,255,0)
  
 
  def endCanvasEast(): Button={
    
    val quitButton = new Button("Quit"){
      
      visible = visibility
      preferredSize = new Dimension(5 * cellSizeX*squareSize, height * cellSizeY*squareSize)
      foreground = Color.WHITE
      border = null
      background = new Color(34,177,76)
      font = new Font("Arial", 20,20)
      borderPainted = false
      }
    quitButton
    
    
    
    }
 
    
  
  def endCanvasWest(): Button={
    
    val restartButton = new Button("Restart"){
      visible = false
      preferredSize = new Dimension(5 * cellSizeX*squareSize, height * cellSizeY*squareSize)
      foreground = Color.WHITE
      border = null
      background = new Color(34,177,76)
      font = new Font("Arial", 20,20)
      borderPainted = false
      }
    restartButton
    
    
    
  }
  def endCanvasCenter(): GridPanel={
    
    val endCanvasCenter = new GridPanel(rows0 = height, cols0 = width/**+10**/){
    preferredSize= new Dimension(width * cellSizeX*squareSize, height * cellSizeY*squareSize)
    font = new Font("Arial", 40,40)
    println("kakka")
    override def paintComponent(g:Graphics2D){
      
      g.drawImage(background_IMG, null, 0, 0)
      g.drawString("Game Ended", 27*cellSizeX, 35*cellSizeY)

      }
    repaint()
    }
    
    
    
    endCanvasCenter
    
  }
  
}