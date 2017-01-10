package o1.game

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

object BackgroundAnimation extends SimpleSwingApplication{
  var background_IMG = ImageIO.read(new File("images/csroad.png"))
  
  val FRAME_W=background_IMG.getWidth
  val FRAME_H=background_IMG.getHeight
  //var progress_in_meters = 0 <-----SCORE----> näkyy yläkulmassa!

  
  //dx ja dy määrittää mihin cohtaan GridPanelia kuva piirretään
  //srcx ja srcy määrittää mikä osa kuvasta piirretään
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
  
  var INCREMENT = 1 // määrittää nopeuden jolla taustakuva liikkuu
                     // saman tekee myös timer arvo
                     // voidaan esimerkiksi tehdä niin, että lisätään moveBackground-metodiin
                     // if lause, että jos timer%50 niin INCREMENT kasvaa 5, eli siis jos
                     // timer on jaollinen 50 niin vauhti kasvaa 5 !ESIM!:p
  
  
  
  
  val canvas = new FlowPanel{
    
    override def paintComponent(g: Graphics2D){
      
      g.drawImage(background_IMG, dx1, dy1, dx2, dy2, srcx1, srcy2,
                    srcx2, srcy1, null);
      g.drawImage(background_IMG, dx1, dy1, dx2, dy2, srcx1, srcy2a,srcx2, srcy1a, null);
      
      //g.drawString(progress_in_meters.toString()+"m", 10, 10)
    }
    
    val timer = new javax.swing.Timer(10,Swing.ActionListener { e =>
      
      moveBackground()
      repaint()
      //progress_in_meters+=1
      })
    timer.start()
    //taustaa liikuttava mekanismi/metodi
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
    preferredSize = new Dimension(FRAME_W,FRAME_H)
    resizable = false
    contents = canvas
    
    
  }
}