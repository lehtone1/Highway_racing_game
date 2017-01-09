package game

import scala.swing._
import scala.swing.event._
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.io.File
import scala.collection.mutable.ArrayBuffer
import scala.swing.Font
import scala.swing.Point
import scala.swing.Alignment




object MenuWindow extends SimpleSwingApplication {
  
  var background_IMG = ImageIO.read(new File("images/finalmenu.png"))
  
  val FRAME_W=background_IMG.getWidth
  val FRAME_H=background_IMG.getHeight
  
  val canvas = new FlowPanel{
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
    
    
    contents+=playButton
    contents+=instructionButton
    
  }
  
  
   
  
  def top = new Frame{
    title = "Menu"
    visible = true
    preferredSize = new Dimension(FRAME_W,FRAME_H)
    resizable = false
    contents = canvas
    
  }
  
  
  
}