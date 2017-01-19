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
import java.io.File
import scala.collection.mutable.ArrayBuffer

import java.awt.Font
import scala.collection.mutable.Buffer
import scala.util.Random

import java.awt.Rectangle
import java.awt.Robot;
import java.net.URL
import javax.sound.sampled._
import scala.collection.mutable.Buffer
import scala.swing.BorderPanel.Position._

 
object MainFrame2a extends SimpleSwingApplication{
  //MUSIC VARIABLES
  //-------------------------------------------------------------------------------------------------------------------
  val music = new Music(new URL("http://www.music.helsinki.fi/tmt/opetus/uusmedia/esim/a2002011001-e02.wav"))
  music.musicStart()
  music.musicLoop(5)
  //-------------------------------------------------------------------------------------------------------------------
  
  //IMAGE VARIABLES
  //-------------------------------------------------------------------------------------------------------------------
  var background_IMG = ImageIO.read(new File("images/highwayInMiddle1.png"))
  var playerIMG = ImageIO.read(new File("images/Player_Car_RED_50x80.png"))
  var enemyIMG = ImageIO.read(new File("images/Enemy_Car_GREEN_50x80.png"))
  var myColor = new Color(255,255,255,0)
  //-------------------------------------------------------------------------------------------------------------------
  
  //GLOBAL VARIABLES
  //-------------------------------------------------------------------------------------------------------------------
  //IMMUTABLE VARIABLES
  //-------------------------------------------------------------------------------------------------------------------
  val cellSizeX = 5
  val cellSizeY = 8
  val squareSize = 10
  val width = 10
  val height = 8
  val possibleXpositions = List.tabulate(width)(x => x*cellSizeX)
  val possibleYpositions = List.tabulate(height)(y => y*cellSizeY)
  val r = scala.util.Random
  val FRAME_W=background_IMG.getWidth
  val FRAME_H=background_IMG.getHeight
  val player = new Player(squareSize*width/2, 0)
  //-------------------------------------------------------------------------------------------------------------------
  
  //MUTABLE VARIABLES
  //-------------------------------------------------------------------------------------------------------------------
  var trueFalseTable = Buffer[Boolean]()
  var finalScore = 0
  var count = 0
  var counter = -1
  var progress_in_meters = 0
  var world = new World(width, height)
  var pressedKeys = Buffer[Key.Value]() //A buffer that holds all keys that are pressed at the same time
  //var enemy = new Enemy(Random.shuffle(possibleXpositions).head,0-1000)
  var backgroundClass = new Background(0,0,"images/highway.png")
  //-------------------------------------------------------------------------------------------------------------------
  //-------------------------------------------------------------------------------------------------------------------
  
  //THE CANVAS/GRID/CONTENT OF FRAME
  //-------------------------------------------------------------------------------------------------------------------
  val canvas = new GridPanel(rows0 = height, cols0 = width+10){
    preferredSize= new Dimension(width * cellSizeX*squareSize, height * cellSizeY*squareSize)
    
    override def paintComponent(g: Graphics2D){
      super.paintComponent(g)
      g.drawImage(background_IMG, backgroundClass.x, backgroundClass.y, backgroundClass.x2, backgroundClass.y2, 
          backgroundClass.srcx1, backgroundClass.srcy2, backgroundClass.srcx2, backgroundClass.srcy1, null);
      g.drawImage(background_IMG, backgroundClass.x, backgroundClass.y, backgroundClass.x2, backgroundClass.y2, 
          backgroundClass.srcx1, backgroundClass.srcy2a, backgroundClass.srcx2, backgroundClass.srcy1a, null);
      
      for (i <- 0 until width*squareSize) {
          for (k <- 0 until height*squareSize - 1) {                           // Loop through the world grid
            world.getWorld(k)(i) match {                                       // Match what is found in every position
              case Wall => {                                                   // If a wall is there, paint enemy
                if(i==0 || i%10==0){
                  var crawler = 0
                  if(counter == -1){
                    counter = counter
                  }
                  else{
                    while(crawler<height){
                      if(world.getWorld(counter+crawler*squareSize)(i)==Wall){
                        g.drawImage(enemyIMG, null, i * cellSizeX, counter*cellSizeY+crawler*squareSize*cellSizeY - 80)
                      }
                      crawler += 1
                    }
                  }
                }
                
                
              }
              case Floor => {                        // If a floor is there, change color to TRANSPARENT and paint Floor
                g.setColor(myColor)
                g.fillRect(i * cellSizeX, k * cellSizeY, cellSizeX, cellSizeY)
              }
            }
          }
        }
      
      g.drawImage(playerIMG, null, player.x * cellSizeX, player.y* cellSizeY)
      g.setColor(Color.RED)
      g.setFont(new Font("Arial", Font.PLAIN, 20))
      g.drawString(progress_in_meters.toString()+" m", 5, 18)
      }
    }
  //-------------------------------------------------------------------------------------------------------------------
  val canvasWest = new GridPanel(rows0 = height, cols0 = 5){
    preferredSize = new Dimension(5* cellSizeX*squareSize, height * cellSizeY*squareSize)
    override def paintComponent(g:Graphics2D){
      g.setColor(new Color(34,177,76))
      g.fillRect(0, 0, 5* cellSizeX*squareSize, height * cellSizeY*squareSize)
      
    }
    
  }
  val canvasEast = new GridPanel(rows0 = height, cols0 = 5){
    preferredSize = new Dimension(5 * cellSizeX*squareSize, height * cellSizeY*squareSize)
    override def paintComponent(g:Graphics2D){
      g.setColor(new Color(34,177,76))
      g.fillRect(0, 0, 5* cellSizeX*squareSize, height * cellSizeY*squareSize)
      
    }
    
  }
  
  val endCanvasCenter = new GridPanel(rows0 = 2, cols0 = 2){
    
  }
  val endCanvasEast = new GridPanel(rows0 = 2, cols0 = 2)
  val endCanvasWest = new GridPanel(rows0 = 2, cols0 = 2)
  //THE TOP DEF CREATES A MAIN FRAME AND FILLS IT WITH THE CANVAS
  //-------------------------------------------------------------------------------------------------------------------
  def top = new MainFrame{
    
    title = "Main"
    preferredSize = new Dimension((width+10) * cellSizeX*squareSize+15, (height-1) * cellSizeY*squareSize+25)
    canvas.focusable = true
    canvas.requestFocus()
    contents = new BorderPanel{
      
      layout(canvasWest)=West
      layout(canvas)=Center
      layout(canvasEast)=East
      
    }
    val endCanvas = new BorderPanel{
      layout(endCanvasWest) = West
      layout(endCanvasEast) = East
      layout(endCanvasCenter) = Center
    }
    
    //contents = canvas
    
    listenTo(canvas.keys)
    listenTo(canvas.mouse.clicks)
    
    var speed = 100
    //var highScores = new HighScores(progress_in_meters)
    /**var bgspeed = 1
    val backGroundTimer = new javax.swing.Timer(bgspeed,Swing.ActionListener { e =>
      backgroundClass.animateBackgroundMovement()
    })
    
    backGroundTimer.start()**/

    var firstSquaresCrawler = 0
    val timer = new javax.swing.Timer(speed,Swing.ActionListener { e =>
      backgroundClass.animateBackgroundMovement()
      if(firstSquaresCrawler == 0){
        world.firstSquares = Array.fill(squareSize ,width * squareSize )(Wall)
        world.makeTrueFalseArray
        world.createHoledLine
        }
      progress_in_meters+=1
      world.moveWorldDown
      player.move(player.x, player.y + 1)
      world.getWorld(0) = world.firstSquares(firstSquaresCrawler)
      firstSquaresCrawler += 1
      if(firstSquaresCrawler == squareSize){
        firstSquaresCrawler = 0
        }
      repaint()
      counter+=1
      if(counter == squareSize){
        counter = 0
        }
      progress_in_meters+=1
      //highScores.addScoreToArray(highScores.update(progress_in_meters))
      
      })
    timer.start()
    
    def hasGameEnded(y:Int):Boolean={
      if(y>FRAME_H) {
        timer.stop()
        //println("Your score: "+highScores.getScore())
        println("Well played")
        true
        }
      else
        false
      
    }
    
    //Timer for gradually increasing the delay in timer, makes the animations faster by decreasing variable 'speed'
    //Aims to create an illusion that the enemy car is accelerating 
    val speedTimer = new javax.swing.Timer(2000, Swing.ActionListener { x => 
      speed-=1
      if(speed>=0)timer.setDelay(speed)
      else timer.setDelay(0)
      })
    speedTimer.start()
    
    
    
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
        contents += new MenuItem(Action("High Score") { 
          highScore()
          //println(highScores.getFinalScore())
          })       
        contents += new Separator        
        contents += new MenuItem(Action("Exit") {
          
          close()
          music.musicClose() //MUSIC OFF
          })
        }
      }
    //Determines the game reactions for keyboards and mouse events
    reactions += {
      case KeyPressed(canvas, key, _, _) => { //When any key is pressed
        println(player.x + " * " + player.y)
        pressedKeys += key //the key is added to the pressed keys
        if(pressedKeys contains Key.Space){ //When space is one of the keys
          if(pressedKeys contains Key.Left){//the player moves two spaces to 
            if(player.jumpCanMoveTo(player.x - squareSize * 2, player.y) && world.getWorld(player.y)(player.x  - squareSize * 2) != Wall){//chosen direction
              player.move(player.x - squareSize * 2  , player.y)
              repaint()
            }
          }else if(pressedKeys contains Key.Right ) {
            if(player.jumpCanMoveTo(player.x + squareSize * 2, player.y ) && world.getWorld(player.y)(player.x + squareSize * 2 ) != Wall){
              player.move(player.x + squareSize * 2 , player.y)
              repaint()
            }
          }else if(pressedKeys contains Key.Up) {
            if(player.jumpCanMoveTo(player.x , player.y - squareSize * 2) && world.getWorld(player.y - squareSize * 2)(player.x) != Wall ){
              player.move(player.x, player.y - squareSize * 2)
              repaint()
            }
          }else if(pressedKeys contains Key.Down){
            if(player.jumpCanMoveTo(player.x, player.y + squareSize * 2) && world.getWorld(player.y + squareSize * 2)(player.x) != Wall ){
              player.move(player.x, player.y + squareSize * 2 )
              repaint()
            }
          }   
        }else if(pressedKeys contains Key.Left ){
          if(player.canMoveTo(player.x - squareSize, player.y) && world.getWorld(player.y)(player.x - squareSize) != Wall){
            player.move(player.x - squareSize, player.y)
            repaint()
            }
        }else if(pressedKeys contains Key.Right) {
          if(player.canMoveTo(player.x + squareSize, player.y) && world.getWorld(player.y)(player.x + squareSize ) != Wall ){
            player.move(player.x + squareSize, player.y)
            repaint()
            }
        }else if(pressedKeys contains Key.Up) {
         if(player.canMoveTo(player.x , player.y - squareSize) && world.getWorld(player.y - squareSize)(player.x) != Wall){
            player.move(player.x, player.y - squareSize)
            repaint()
            
          }
        }else if(pressedKeys contains Key.Down){
          if(player.canMoveTo(player.x , player.y + squareSize) && world.getWorld(player.y + squareSize)(player.x) != Wall ){
            player.move(player.x, player.y + squareSize )
            repaint()
          }
        }
        
      }
      
          
  
      case KeyReleased(canvas, key, _, _) => {
        pressedKeys = Buffer[Key.Value]()
      }
       
    }
    
  }
  
}