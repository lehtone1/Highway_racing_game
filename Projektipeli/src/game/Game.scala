package game

import scala.swing._
import scala.swing.event._
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


import javax.swing.SwingUtilities;
import java.io.File
import scala.collection.mutable.ArrayBuffer

import java.awt.Font
import scala.collection.mutable.Buffer
import scala.util.Random



import javax.sound.sampled._
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import scala.swing.BorderPanel.Position._

 
object Game extends SimpleSwingApplication{
  //MUSIC VARIABLES
  //-------------------------------------------------------------------------------------------------------------------
  
  
 
  val music = new Music("sounds/arcade.wav")
  
  music.musicStart()
  music.musicLoop(5)
  //-------------------------------------------------------------------------------------------------------------------
  
  //IMAGE VARIABLES
  //-------------------------------------------------------------------------------------------------------------------
  var background_IMG = ImageIO.read(new File("images/highwayInMiddle.png"))
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
  
  val r = scala.util.Random

  val player = new Player(squareSize*width/2, 0)
  var endGameTopContent = new TopContents(height,width,cellSizeX,cellSizeY,squareSize)
  //-------------------------------------------------------------------------------------------------------------------
  
  //MUTABLE VARIABLES
  //-------------------------------------------------------------------------------------------------------------------
  var trueFalseTable = Buffer[Boolean]()
  
  var counter = -1
  var progress_in_meters = 0
  var world = new World(width, height)
  var pressedKeys = Buffer[Key.Value]() //A buffer that holds all keys that are pressed at the same time
  
  var backgroundClass = new Background(0,0)
  
  var restartButton = endGameTopContent.endCanvasWest()
  
  var quitButton = endGameTopContent.endCanvasEast()
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
              case Enemy => {                                                   // If a wall is there, paint enemy
                if(i==0 || i%10==0){
                  var crawler = 0
                  if(counter == -1){
                    counter = counter
                  }
                  else{
                    while(crawler<height){
                      if(world.getWorld(counter+crawler*squareSize)(i)==Enemy){
                        g.drawImage(enemyIMG, null, i * cellSizeX, counter*cellSizeY+crawler*squareSize*cellSizeY - 80)
                      }
                      crawler += 1
                    }
                  }
                }
                
                
              }
              case Road => {                        // If a floor is there, change color to TRANSPARENT and paint Floor
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
      font = new Font("Arial", 40,40)
      if(quitButton.visible){
        g.drawString("Game Ended",27*cellSizeX, 35*cellSizeY)
        g.drawString("Your score:  "+progress_in_meters.toString()+" m", 27*cellSizeX, 38*cellSizeY)
        
        }
      }
    }
  //-------------------------------------------------------------------------------------------------------------------
  val canvasWest = new GridPanel(rows0 = height, cols0 = 5){
    preferredSize = new Dimension(5* cellSizeX*squareSize, height * cellSizeY*squareSize)
    override def paintComponent(g:Graphics2D){
      g.setColor(new Color(34,177,76))
      g.fillRect(0, 0, 5* cellSizeX*squareSize, height * cellSizeY*squareSize)
      
    }
    
    
    
    contents+= restartButton
    
    
  }
  val canvasEast = new GridPanel(rows0 = height, cols0 = 5){
    preferredSize = new Dimension(5 * cellSizeX*squareSize, height * cellSizeY*squareSize)
    override def paintComponent(g:Graphics2D){
      g.setColor(new Color(34,177,76))
      g.fillRect(0, 0, 5* cellSizeX*squareSize, height * cellSizeY*squareSize)
      
    }
    contents+=quitButton
    
  }
  

  //THE TOP DEF CREATES A MAIN FRAME AND FILLS IT WITH THE CANVAS
  //-------------------------------------------------------------------------------------------------------------------
  def top = new MainFrame{
    
    title = "Main"
    preferredSize = new Dimension((width+10) * cellSizeX*squareSize+15, (height-1) * cellSizeY*squareSize+25)
    canvas.focusable = true
    canvas.requestFocus()
    var canvas1 = new BoxPanel(Orientation.Horizontal){
      contents += canvasWest
      contents += canvas
      contents += canvasEast
      
    }
    contents = canvas1
    
    
    listenTo(canvas.keys)
    listenTo(canvas.mouse.clicks)
    listenTo(quitButton)
    
    var speed = 100
    var firstSquaresCrawler = 0
    val timer = new javax.swing.Timer(speed,Swing.ActionListener { e =>
      backgroundClass.animateBackgroundMovement()
      if(firstSquaresCrawler == 0){
        world.firstSquares = Array.fill(squareSize ,width * squareSize )(Enemy)
        world.makeTrueFalseArray
        world.createHoledLine
        }
      world.moveWorldDown
      progress_in_meters+=1
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
      if(hasGameEnded(player.y))println("")
      
      
      })
    timer.start()
    
    
    //Timer for gradually increasing the delay in timer, makes the animations faster by decreasing variable 'speed'
    //Aims to create an illusion that the enemy car is accelerating 
   val speedTimer = new javax.swing.Timer(2000, Swing.ActionListener { x => 
      speed-=1
      if(speed>=0)timer.setDelay(speed)
      else timer.setDelay(0)
      })
    speedTimer.start()
    
    
    
    def hasGameEnded(y:Int):Boolean={
      if(y>(height-1)*squareSize){
        timer.stop()
        speedTimer.stop()
        quitButton.visible_=(true)
        repaint()
        true
      }
      
      else false
    }
    
    
    val aboutText = """In this game your mission is to survive
                      |as long as possible. You will loose if your 
                      |game figure drops off the gamefield. 
                      |The gamefield and incoming cars are moving downwards in an
                      |increasing speed which makes it harder to stay on
                      |the gamefield. The game creates obstacles on your path
                      |to make it harder for you to move forward. You can 
                      |jump over one obstacle, but not over multiple obstacles""".stripMargin
   
    val keyCommandsText = """Go Up - Up Key
                        |Go Down - Down Key
                        |Go Left - Left Key
                        |Go Right - Right Key
                        |Jump Up - Space + Up Key
                        |Jump Down - Space + Down Key
                        |Jump Left - Space + Left Key
                        |Jump Right - Space + Right Key""".stripMargin
                        
    def quit = {
      close()
      music.musicClose()
    }

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
        pressedKeys += key //the key is added to the pressed keys
        if(pressedKeys contains Key.Space){ //When space is one of the keys
          if(pressedKeys contains Key.Left){//the player moves two spaces to 
            if(player.jumpCanMoveTo(player.x - squareSize * 2, player.y) 
                && world.getWorld(player.y)(player.x  - squareSize * 2) != Enemy){//chosen direction
              player.move(player.x - squareSize * 2  , player.y)
              repaint()
            }
          }else if(pressedKeys contains Key.Right ) {
            if(player.jumpCanMoveTo(player.x + squareSize * 2, player.y ) 
                && world.getWorld(player.y)(player.x + squareSize * 2 ) != Enemy){
              player.move(player.x + squareSize * 2 , player.y)
              repaint()
            }
          }else if(pressedKeys contains Key.Up) {
            if(player.jumpCanMoveTo(player.x , player.y - squareSize * 2) 
                && world.getWorld(player.y - squareSize * 2)(player.x) != Enemy ){
              player.move(player.x, player.y - squareSize * 2)
              repaint()
            }
          }else if(pressedKeys contains Key.Down){
            if(player.jumpCanMoveTo(player.x, player.y + squareSize * 2) 
                && world.getWorld(player.y + squareSize * 2)(player.x) != Enemy ){
              player.move(player.x, player.y + squareSize * 2 )
              repaint()
            }
          }   
        }else if(pressedKeys contains Key.Left ){
          if(player.canMoveTo(player.x - squareSize, player.y) 
              && world.getWorld(player.y)(player.x - squareSize) != Enemy){
            var music3 = new Music("sounds/vehicle156.wav")
            music3.musicStart()
            player.move(player.x - squareSize, player.y)
            repaint()
            }
        }else if(pressedKeys contains Key.Right) {
          if(player.canMoveTo(player.x + squareSize, player.y) 
              && world.getWorld(player.y)(player.x + squareSize ) != Enemy ){
            var music2 = new Music("sounds/vehicle156.wav")
            music2.musicStart()
            player.move(player.x + squareSize, player.y)
            repaint()
            }
        }else if(pressedKeys contains Key.Up) {
         if(player.canMoveTo(player.x , player.y - squareSize) 
             && world.getWorld(player.y - squareSize)(player.x) != Enemy){
            player.move(player.x, player.y - squareSize)
            repaint()
            
          }
        }else if(pressedKeys contains Key.Down){
          if(player.canMoveTo(player.x , player.y + squareSize) 
              && world.getWorld(player.y + squareSize)(player.x) != Enemy ){
            player.move(player.x, player.y + squareSize )
            repaint()
          }
        }
        
      }
      
          
  
      case KeyReleased(canvas, key, _, _) => {
        pressedKeys = Buffer[Key.Value]()
      }
      case ButtonClicked(quitButton)=>{
        quit
      }
       
    }
    
  }
  
}