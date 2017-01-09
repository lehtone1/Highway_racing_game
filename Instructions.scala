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
import java.awt.Point




object Instructions extends SimpleSwingApplication {
  
  var background_IMG = ImageIO.read(new File("images/themeimage.png"))
  
  val FRAME_W=background_IMG.getWidth
  val FRAME_H=background_IMG.getHeight
  
  val canvas = new BoxPanel(Orientation.Vertical){
    
    override def paintComponent(g: Graphics2D){
      g.drawImage(background_IMG,0,0,null)
    }
    val headerLabel = new Label("Instructions",null,Alignment.Center){
      foreground = Color.WHITE
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 40,40)

    }
    
    val contentLabel = new Label("Press LEFT arrow to dodge incoming cars to left"){
      foreground = Color.WHITE
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 20,20)
    }
    
    val contentLabel1 = new Label("Press RIGHT arrow to dodge incoming cars to right"){
      foreground = Color.WHITE
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 20,20)
    }
    val contentLabel2 = new Label("To jump & avoid random road obstacles press UP arrow"){
      foreground = Color.WHITE
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 20,20)
    }
    val contentLabel3 = new Label("Your objective is to drive as far as possible"){
      foreground = Color.WHITE
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 20,20)
    }
    val contentLabel4 = new Label("But remember, the gas pedal is jammed!"){
      foreground = Color.WHITE
      visible = true
      border = null
      background = Color.BLACK
      font = new Font("Arial", 20,20)
    }
    
    println(headerLabel.location)
    
    
    contents+=headerLabel
    contents+=contentLabel
    contents+=contentLabel1
    contents+=contentLabel2
    contents+=contentLabel3
    contents+=contentLabel4
    
  }
  
  
   
  
  def top = new Frame{
    title = "Menu"
    visible = true
    preferredSize = new Dimension(FRAME_W,FRAME_H)
    resizable = false
    contents = canvas
    
  }
  
  
  
}