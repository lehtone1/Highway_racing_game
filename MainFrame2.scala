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

import java.awt.Robot;
import java.net.URL
import javax.sound.sampled._

 
object MainFrame2 extends SimpleSwingApplication{
  
  //MUSIC VARIABLES
  //-------------------------------------------------------------------------------------------------------------------
  val url = new URL("http://www.music.helsinki.fi/tmt/opetus/uusmedia/esim/a2002011001-e02.wav")
  val audioIn = AudioSystem.getAudioInputStream(url)
  val clip = AudioSystem.getClip
  clip.open(audioIn)
  clip.start()
  clip.loop(5)
  //-------------------------------------------------------------------------------------------------------------------
  
  
  //IMAGE VARIABLES
  //-------------------------------------------------------------------------------------------------------------------
  var background_IMG = ImageIO.read(new File("images/highway.png"))
  var playerIMG = ImageIO.read(new File("images/Player_Car_RED_50pix_SIZE.png"))
  var enemyIMG = ImageIO.read(new File("images/Enemy_Car_GREEN_50pix_SIZE.png"))
  //-------------------------------------------------------------------------------------------------------------------
  
  
  //GLOBAL VARIABLES
  //-------------------------------------------------------------------------------------------------------------------
  var count = 0
  val FRAME_W=background_IMG.getWidth
  val FRAME_H=background_IMG.getHeight
  var progress_in_meters = 0
  val cellSizeX = 50
  val cellSizeY = 74
  val width = 20           //amount of squares horizontally (width*cellSizeX = 1000)
  val height = 7           //amount of squares vertically (height*cellSizeY = 518)
  
  //val possibleXcoordinates = List.tabulate(width)(x=>x) //=(0,1,2,3,4,5,6,7,8,9,...,19)
  //var world: Array[Array[Spot]] = Array.fill(width, height)(Floor) <------NOT USED------>
  //val testWorld = List.tabulate(height, width)((y,x)=>Floor) <------NOT USED------>
  
  val possibleXpositions = List.tabulate(width)(x => x*cellSizeX)
  val possibleYpositions = List.tabulate(width)(y => y*cellSizeY)
  
  //Creates a player, enemy and a background
  //Enemy is a class defined in Enemy.scala, backgroundClass is a class defined in Background.scala
  //Enemy class has functions getImage(), animateEnemyMovement() and enemyOutOfBounds()
  //enemyOutOfBounds function returns true if enemy's x an y coordinate are not inside of the 'world'
  
  val player = new Player(width/2, 6)
  
  var enemy = new Enemy(Random.shuffle(possibleXpositions).head,0-1000,"images/Enemy_Car_GREEN_50pix_SIZE.png")
  var backgroundClass = new Background(0,0,"images/highway.png")
  //-------------------------------------------------------------------------------------------------------------------
  
  
  //THE CANVAS/GRID/CONTENT OF FRAME
  //-------------------------------------------------------------------------------------------------------------------
  val canvas = new GridPanel(rows0 = height, cols0 = width){
    preferredSize= new Dimension(width * cellSizeX, height * cellSizeY)
    
    override def paintComponent(g: Graphics2D){
      
      g.drawImage(background_IMG, backgroundClass.x, backgroundClass.y, backgroundClass.x2, backgroundClass.y2, 
          backgroundClass.srcx1, backgroundClass.srcy2, backgroundClass.srcx2, backgroundClass.srcy1, null);
      
      g.drawImage(background_IMG, backgroundClass.x, backgroundClass.y, backgroundClass.x2, backgroundClass.y2, 
          backgroundClass.srcx1, backgroundClass.srcy2a, backgroundClass.srcx2, backgroundClass.srcy1a, null);
      
      
      g.drawImage(enemy.getImage(), enemy.x, enemy.y, enemy.x2, enemy.y2, 0, 0,cellSizeX, cellSizeY, null);
      g.drawImage(playerIMG, null, player.x * cellSizeX, player.y* cellSizeY)

      g.setColor(Color.RED)
      g.setFont(new Font("Arial", Font.PLAIN, 20))
      g.drawString(progress_in_meters.toString()+" m", 5, 18)
      }
    }
  //-------------------------------------------------------------------------------------------------------------------
  
  
  //THE TOP DEF CREATES A MAIN FRAME AND FILLS IT WITH THE CANVAS
  //-------------------------------------------------------------------------------------------------------------------
  def top = new MainFrame{
    
    var speed = 10
    
    val timer = new javax.swing.Timer(speed,Swing.ActionListener { e =>
      enemy.animateEnemyMovement()
      backgroundClass.animateBackgroundMovement()
      repaint()
      progress_in_meters+=1
      })
    timer.start()
    
    //Timer for gradually increasing the delay in timer, makes the animations faster by decreasing variable 'speed'
    //Aims to create an illusion that the enemy car is accelerating 
    val speedTimer = new javax.swing.Timer(5000, Swing.ActionListener { x => 
      speed-=1
      if(speed>=0)timer.setDelay(speed)
      })
    speedTimer.start()
    
    
    title = "Main"
    preferredSize = new Dimension(width * cellSizeX+18, height * cellSizeY+60)
    canvas.focusable = true
    canvas.requestFocus()
    contents = canvas
    listenTo(canvas.keys)
    listenTo(canvas.mouse.clicks)
    
    val aboutText = """In this game your mission is to survive
                      |as long as possible. You will loose if your 
                      |game figure will drop off the gamefield. The game
                      |is moving the gamefield all the time downwards in an
                      |increasing speed so it is all the time harder to stay on
                      |the gamefield. The game creates obstacles on your path
                      |to make it harder for you to move forward. You can 
                      |jump over one obstacle, but if there is more that one
                      |obstacle after the other you cannot move through it""".stripMargin
   
    val keyCommandsText = """Go Up - Up Key
                        |Go Down - Down Key
                        |Go Left - Left Key
                        |Go Right - Right Key
                        |Jump Up - Space + Up Key
                        |Jump Down - Space + Down Key
                        |Jump Left - Space + Left Key
                        |Jump Right - Space + Right Key""".stripMargin

    //Popup windows
    def about() {
      Dialog.showMessage(contents.head, aboutText, title="About")
    }
    
    def keyCommands() {
      Dialog.showMessage(contents.head, keyCommandsText, title="Key Commands")
    }
    
    def highScore() {
      Dialog.showMessage(contents.head, "Thank you!", title="High score")
    }
    
    //Gane menu functionalities About, High Score and Exit
    menuBar = new MenuBar {
      contents += new Menu("Menu") {
        contents += new MenuItem(Action("About") { about()})
        contents += new MenuItem(Action("Key commands") { keyCommands()})
        contents += new MenuItem(Action("High Score") { highScore()})       
        contents += new Separator        
        contents += new MenuItem(Action("Exit") {
          dispose()
          clip.close() //MUSIC OFF
          })
        }
      }
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