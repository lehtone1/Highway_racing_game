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
import game.Player
import java.awt.Font
import scala.collection.mutable.Buffer
import scala.util.Random

object MainFrame2 extends SimpleSwingApplication{
  
  
  var background_IMG = ImageIO.read(new File("images/highway.png"))
  var playerIMG = ImageIO.read(new File("images/Player_Car_RED_50pix_SIZE.png"))
  var enemyIMG = ImageIO.read(new File("images/Enemy_Car_GREEN_50pix_SIZE.png"))
  var pressedKeys = Buffer[Key.Value]()
  val cellSizeX = 50
  val cellSizeY = 74
  val width = 20
  val height = 7
  val player = new Player(width/2, 6)
  
  var possibleXpositions = new ArrayBuffer[Int]()
  for(i<-0 until width){
    possibleXpositions+=(i*cellSizeX)
  }
  var possibleYpositions = new ArrayBuffer[Int]()
  for(i<-0 until height){
    possibleYpositions+=(i*cellSizeY)
  }
  
  
  
  
  val FRAME_W=background_IMG.getWidth
  val FRAME_H=background_IMG.getHeight
  var progress_in_meters = 0

  var dx1=0  
  var dy1=0
  var dx2=FRAME_W
  var dy2=FRAME_H
  
  var srcx1=0
  var srcy1=0
  var srcx2=FRAME_W
  var srcy2=FRAME_H
  
  var srcy1a=0-FRAME_H
  var srcy2a=0
  var speed = 1
  var count = 0
  var INCREMENT = 1 
  var rand_dx1_enemy =Random.shuffle(possibleXpositions.toList).head
  var rand_dx2_enemy = rand_dx1_enemy+cellSizeX
  var rand_dy1_enemy = 0-1000
  var rand_dy2_enemy = rand_dy1_enemy+cellSizeY
  
  
  
  
  val canvas = new GridPanel(rows0 = height, cols0 = width){
    preferredSize= new Dimension(width * cellSizeX, height * cellSizeY)
    
    override def paintComponent(g: Graphics2D){
      g.drawImage(background_IMG, dx1, dy1, dx2, dy2, srcx1, srcy2, srcx2, srcy1, null);
      g.drawImage(background_IMG, dx1, dy1, dx2, dy2, srcx1, srcy2a,srcx2, srcy1a, null);
      
      g.drawImage(enemyIMG, rand_dx1_enemy, rand_dy1_enemy, rand_dx2_enemy, rand_dy2_enemy, 0, 0,cellSizeX, cellSizeY, null);

      
      g.drawImage(playerIMG, null, player.x * cellSizeX, player.y* cellSizeY)
      
      
      
      g.setColor(Color.RED)
      g.setFont(new Font("Arial", Font.PLAIN, 20))
      g.drawString(progress_in_meters.toString()+" m", 5, 18)
    }
    
    val timer = new javax.swing.Timer(speed,Swing.ActionListener { e =>
      rand_dy1_enemy+=1
      rand_dy2_enemy+=1
      if(enemyOutOfBounds(rand_dy1_enemy)){
        rand_dx1_enemy =Random.shuffle(possibleXpositions.toList).head
        rand_dx2_enemy = rand_dx1_enemy+cellSizeX
        rand_dy1_enemy = 0-1000
        rand_dy2_enemy = rand_dy1_enemy+cellSizeY
      }
      
      println(rand_dy1_enemy,rand_dy2_enemy)
      moveBackground()
      repaint()
      progress_in_meters+=1
      })
    timer.start()
    
    
    def enemyOutOfBounds(dy:Int): Boolean = {
      var returnVal = false
      if(dy>=FRAME_H){
        returnVal = true
      }
      returnVal
    }
    //taustaa liikuttava mekanismi/metodi ja vihollinen
    def moveBackground(){
      
      if (srcy1 == FRAME_H) {
        srcy1 = 1-FRAME_H
        srcy2 = 1
        //srcy2 = FRAME_H
        //srcy1 = FRAME_H - srcy2-srcy1
        srcy1a += INCREMENT;
        srcy2a += INCREMENT;
        
        }
      else if(srcy1a == FRAME_H){
        
        srcy1a = 1-FRAME_H
        srcy2a = 1
        srcy1 += INCREMENT;
        srcy2 += INCREMENT;
      }
      else{
        srcy1a += INCREMENT;
        srcy2a += INCREMENT;
        srcy1 += INCREMENT;
        srcy2 += INCREMENT;
        
        } 
    }

    
    
    
  }
  
  
  def top = new Frame{
    title = "BackgroundAnimation"
    preferredSize = new Dimension(width * cellSizeX+18, height * cellSizeY+37)
    canvas.focusable = true
    
    contents = canvas
    listenTo(canvas.keys)
    reactions +={
      case KeyPressed(canvas,key,_,_) =>{
        if(key == Key.Left){
          if(player.x* cellSizeX<=0){
            player.move(player.x,player.y)
            }
          else{
            player.move(player.x-1,player.y)
            }
          }
        else if(key==Key.Right){
          if(player.x* cellSizeX>=950){
            player.move(player.x, player.y)
            }
          else{
            player.move(player.x+1,player.y)
            }
          }
        else if(key==Key.Up){
          if(player.y*cellSizeY<=0){
            player.move(player.x,player.y)
            }
          else{
            player.move(player.x,player.y-1)
            }
          }
        else if(key==Key.Down){
          if(player.y*cellSizeY>=444){
            player.move(player.x,player.y)
            }
          else{
            player.move(player.x,player.y+1)
            }
          }
      }
      
    }
    
    
  }
}