package game


import scala.swing._
import scala.swing.event._
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

object Main{
  def main(args:Array[String]) = {
    MenuWindow.main(args)
    MenuWindow.canvas.playButton.peer.addActionListener(new ActionListener(){
      def actionPerformed(e:ActionEvent)={
        
        if(e.getActionCommand()=="Play"){
          MenuWindow.top.dispose()
          Game.main(args)
        } 
          
        
      }
    })
    MenuWindow.canvas.instructionButton.peer.addActionListener(new ActionListener(){
      def actionPerformed(e:ActionEvent)={
 
        if(e.getActionCommand()=="Instructions"){
          //MenuWindow.top.close()
          Instructions.main(args)
        }
        
      }
    })
  }
}
















