package game

import scala.swing._
import scala.swing.event._
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.File
import scala.swing.BorderPanel.Position._




object Instructions extends SimpleSwingApplication {
  
  var background_IMG = ImageIO.read(new File("images/thehighway_green1.png"))
  var myColor = new Color(255,255,255,0)
  
  val FRAME_W=background_IMG.getWidth
  val FRAME_H=background_IMG.getHeight
  //new BoxPanel(Orientation.Vertical)
  val canvas = new BorderPanel{
    
    override def paintComponent(g: Graphics2D){
      g.drawImage(background_IMG,0,0,null)
    }
    val headerLabel = new Label("Instructions",null,Alignment.Center){
      foreground = Color.RED
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 40,40)

    }
    
    val grid = new GridPanel(rows0=6, cols0=1){
      preferredSize = new Dimension(500,600)
      background = myColor
      val contentLabel = new Label("Press LEFT arrow to dodge incoming cars to left"){
      foreground = Color.RED
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 20,20)
    }
    
    val contentLabel1 = new Label("Press RIGHT arrow to dodge incoming cars to right"){
      foreground = Color.RED
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 20,20)
    }
    val contentLabel2 = new Label("To jump & avoid random road obstacles press SPACE"){
      foreground = Color.RED
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 20,20)
    }
    val contentLabel2a = new Label("and ANY ARROW to choose direction"){
      foreground = Color.RED
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 20,20)
    }
    val contentLabel3 = new Label("Your objective is to drive as far as possible"){
      foreground = Color.RED
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 20,20)
    }
    val contentLabel4 = new Label("But remember, stay on the Game Field!"){
      foreground = Color.RED
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 20,20)
    }
    contents+=contentLabel
    contents+=contentLabel1
    contents+=contentLabel2
    contents+=contentLabel2a
    contents+=contentLabel3
    contents+=contentLabel4
      
    }
    
    
    
    
    
    layout(headerLabel) = North
    layout(grid) = Center
    
    
  }
  
  
   
  
  def top = new Frame{
    title = "Menu"
    visible = true
    preferredSize = new Dimension(FRAME_W+15,FRAME_H+25)
    resizable = false
    contents = canvas
    
  }
  
  
  
}