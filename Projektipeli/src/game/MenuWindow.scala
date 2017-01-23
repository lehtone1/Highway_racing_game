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




object MenuWindow extends SimpleSwingApplication {
  var myColor = new Color(255,255,255,0)
  
  var background_IMG = ImageIO.read(new File("images/menu_cover_image.png"))
  
  val FRAME_W=background_IMG.getWidth
  val FRAME_H=background_IMG.getHeight
  
  val canvas = new BorderPanel{
    override def paintComponent(g: Graphics2D){
      g.drawImage(background_IMG,0,0,null)
    }
    val playButton = new Button("Play"){
      foreground = Color.WHITE
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 40,40)
      borderPainted = false
    }
    
    
    
    val instructionButton = new Button("Instructions"){
      foreground = Color.WHITE
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 20,20)
      borderPainted = false
      
      
    }
    
    
    layout(playButton) = North
    layout(instructionButton) = South
    
  }
  
  
   
  
  def top = new Frame{
    title = "Menu"
    visible = true
    preferredSize = new Dimension(FRAME_W+15,FRAME_H+25)
    resizable = false
    contents = canvas
    
  }
  
  
  
}