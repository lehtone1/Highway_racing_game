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


//MAIN OHJELMA MISSÄ AJETAAN VUOROLLAAN .scala filui. Pitää varmaan kyllä tehä niin että päivittää sitä Frame eikä luo
//uutta ikkunaa
object Main{
  def main(args:Array[String]) = {
    MenuWindow.main(args)
    MenuWindow.canvas.playButton.peer.addActionListener(new ActionListener(){
      def actionPerformed(e:ActionEvent)={
        
        if(e.getActionCommand()=="Play"){
          MenuWindow.top.dispose()
          BackgroundAnimation.main(args)
        } 
          
        
      }
    })
    MenuWindow.canvas.instructionButton.peer.addActionListener(new ActionListener(){
      def actionPerformed(e:ActionEvent)={
 
        if(e.getActionCommand()=="Instructions"){
          MenuWindow.top.close()
          Instructions.main(args)
        }
        
      }
    })
  }
}