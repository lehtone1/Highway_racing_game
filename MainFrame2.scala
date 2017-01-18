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

import java.awt.Robot;
import java.net.URL
import javax.sound.sampled._
import scala.collection.mutable.Buffer
 
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
  var background_IMG = ImageIO.read(new File("images/finalhighway.png"))
  var playerIMG = ImageIO.read(new File("images/Player_Car_RED_50x80.png"))
  var enemyIMG = ImageIO.read(new File("images/Enemy_Car_GREEN_50x80.png"))
  var myColor = new Color(255,255,255,0)
  //-------------------------------------------------------------------------------------------------------------------
  
  //GLOBAL VARIABLES
  //-------------------------------------------------------------------------------------------------------------------
  var trueFalseTable = Buffer[Boolean]()
  var finalScore = 0
  var count = 0
  var counter = -1
  val r = scala.util.Random
  val FRAME_W=background_IMG.getWidth
  val FRAME_H=background_IMG.getHeight
  var progress_in_meters = 0
  val cellSizeX = 5
  val cellSizeY = 8
  val squareSize = 10
  val width = 20          //amount of squares horizontally (width*cellSizeX = 1000)
  val height = 8           //amount of squares vertically (height*cellSizeY = 518)
  
  val possibleXpositions = List.tabulate(width)(x => x*cellSizeX)
  val possibleYpositions = List.tabulate(height)(y => y*cellSizeY)
  
  var world: Array[Array[Spot]] = Array.fill(height*squareSize, (width*squareSize))(Floor)
  var firstSquares: Array[Array[Spot]] = Array.fill(squareSize,(width*squareSize))(Wall)
  
  //var world :List[List[Enemy]]= List.tabulate(height,width)((x,y)=>new Enemy(Random.shuffle(possibleXpositions).head,Random.shuffle(possibleYpositions).head-1000))
  //var bgworld : List[List[EmptyFloor]] = List.tabulate(height,width)((x,y)=>new EmptyFloor(myColor,y*50,x*74,width,height))
  
  //Creates a player, enemy and a background
  //Enemy is a class defined in Enemy.scala, backgroundClass is a class defined in Background.scala
  //Enemy class has functions getImage(), animateEnemyMovement() and enemyOutOfBounds()
  //enemyOutOfBounds function returns true if enemy's x an y coordinate are not inside of the 'world'
  
  var pressedKeys = Buffer[Key.Value]() //A buffer that holds all keys that are pressed at the same time
  val player = new Player(squareSize*width/2, 0)
  //var enemy = new Enemy(Random.shuffle(possibleXpositions).head,0-1000)
  var backgroundClass = new Background(0,0,"images/highway.png")
  //var enemyArray = Array.fill(8)(new Enemy(Random.shuffle(possibleXpositions).head,0-1000,"images/Enemy_Car_GREEN_50pix_SIZE.png"))
  //-------------------------------------------------------------------------------------------------------------------
  
  
  //ESSENTIAL FUNCTIONS
  //-------------------------------------------------------------------------------------------------------------------
  def moveWorldDown = {
    var line = height * squareSize - 1
    player.move(player.x, player.y + 1)
    while(line > 0){
      world(line) = world(line - 1)
      line -=1
    }
      
  }
  
  def createHoledLine = {
      
      do{
        println("loop")
        val randomNum = 8 + r.nextInt(width - 8)
        var crawler = 0
        while(crawler < randomNum){
          var randomNumTreeni = 1 + r.nextInt(width)
          var randomNum2 = (squareSize * randomNumTreeni)
          var squareLowerLimit = randomNum2- squareSize
          for(line <- 0  until squareSize){
            for(place <- squareLowerLimit until randomNum2-1){
              println(firstSquares(0).length)
              firstSquares(line)(place) = Floor
            }
          }
          crawler += 1
          }   
      }while(checkIfAcceptable == false)
  
  }
  
  def makeTrueFalseArray = {
    trueFalseTable = Buffer[Boolean]()
    var row = 0
    while(row < width * squareSize){
       if((world(1)(row) == Wall && world(1 + squareSize)(row) == Wall) || (world(1 + squareSize)(row) == Wall && world(1 + 2 * squareSize)(row) == Wall) || (world(1 + 2 * squareSize)(row) == Wall && world(1 + 3 * squareSize)(row) == Wall)  ){
          trueFalseTable += false
       }else{
         trueFalseTable += true
       }
       row += 1
    }
  }
  
  def checkIfAcceptable = {
    var acceptable = false
    for(number <- 0 to width * squareSize -1 ){
      if(trueFalseTable(number) && world(1)(number) == Floor){
        acceptable = true
      }
    }
    
    acceptable
  }
  //-------------------------------------------------------------------------------------------------------------------
  //THE CANVAS/GRID/CONTENT OF FRAME
  //-------------------------------------------------------------------------------------------------------------------
  val canvas = new GridPanel(rows0 = height, cols0 = width){
    preferredSize= new Dimension(width * cellSizeX*squareSize, height * cellSizeY*squareSize)
    
    override def paintComponent(g: Graphics2D){
      g.drawImage(background_IMG, backgroundClass.x, backgroundClass.y, backgroundClass.x2, backgroundClass.y2, 
          backgroundClass.srcx1, backgroundClass.srcy2, backgroundClass.srcx2, backgroundClass.srcy1, null);
      g.drawImage(background_IMG, backgroundClass.x, backgroundClass.y, backgroundClass.x2, backgroundClass.y2, 
          backgroundClass.srcx1, backgroundClass.srcy2a, backgroundClass.srcx2, backgroundClass.srcy1a, null);
      
      for (i <- 0 until width*squareSize) {
          for (k <- 0 until height*squareSize - 1) { // Loop through the world grid
            world(k)(i) match {       // Match what is found in every position
              case Wall => {
                // If a wall is there, change color to black and paint a black tile representing a wall
                if(i==0 || i%10==0){
                  var crawler = 0
                  if(counter == -1){
                    counter = counter
                  }
                  else{
                    while(crawler<height){
                      if(world(counter+crawler*squareSize)(i)==Wall){
                        g.drawImage(enemyIMG, null, i * cellSizeX, counter*cellSizeY+crawler*squareSize*cellSizeY - 80)
                      }
                      crawler += 1
                    }
                  }
                }
                
                
              }
              case Floor => {         // If a floor is there, change color to cyan and paint a cyan tile representing floor
                g.setColor(myColor)
                g.fillRect(i * cellSizeX, k * cellSizeY, cellSizeX, cellSizeY)
              }
            }
          }
        }
      
      
      
      //g.drawImage(enemy.getImage(), enemy.x, enemy.y, enemy.x2, enemy.y2, 0, 0,cellSizeX, cellSizeY, null);
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
    
    var speed = 100
    var highScores = new HighScores(progress_in_meters)
    var bgspeed = 1
    val backGroundTimer = new javax.swing.Timer(bgspeed,Swing.ActionListener { e =>
      backgroundClass.animateBackgroundMovement()
    })
    
    backGroundTimer.start()
    /**
    val bgspeedTimer = new javax.swing.Timer(500,Swing.ActionListener { e =>
      bgspeed -=1
      if(bgspeed>=0)backGroundTimer.setDelay(bgspeed)
      else backGroundTimer.setDelay(0)
    })
    bgspeedTimer.start()
    * 
    */
    var firstSquaresCrawler = 0
    val timer = new javax.swing.Timer(speed,Swing.ActionListener { e =>
      
      if(firstSquaresCrawler == 0){
          firstSquares = Array.fill(squareSize ,width * squareSize )(Wall)
          makeTrueFalseArray
          createHoledLine
        }
        progress_in_meters+=1
        moveWorldDown
        world(0) = firstSquares(firstSquaresCrawler)
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
      highScores.addScoreToArray(highScores.update(progress_in_meters))
      
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
    
    title = "Main"
    preferredSize = new Dimension(width * cellSizeX*squareSize+18, (height-1) * cellSizeY*squareSize+60)
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
        contents += new MenuItem(Action("High Score") { 
          highScore()
          println(highScores.getFinalScore())
          })       
        contents += new Separator        
        contents += new MenuItem(Action("Exit") {
          
          close()
          clip.close() //MUSIC OFF
          })
        }
      }
    
    //val test = (world.reduceLeft(_++_)).filter(_.x==(player.x-1)* cellSizeX).isEmpty
    //val test3 = world.reduceLeft(_++_)
    //|| (world.reduceLeft(_++_)).filter(_.x==(player.x-1)*cellSizeX).isEmpty
    
    def hasGameEnded(x:Int,y:Int):Boolean={
      if(y>=FRAME_H) {
        timer.stop()
        println("Your score: "+highScores.getScore())
        println("Well played")
        true
        }
      else
        false
      
    }
    
    //Determines the game reactions for keyboards and mouse events
    reactions += {
      case KeyPressed(canvas, key, _, _) => { //When any key is pressed
        println(player.x + " * " + player.y)
        pressedKeys += key //the key is added to the pressed keys
        if(pressedKeys contains Key.Space){ //When space is one of the keys
          if(pressedKeys contains Key.Left){//the player moves two spaces to 
            if(player.jumpCanMoveTo(player.x - squareSize * 2, player.y) && world(player.y)(player.x  - squareSize * 2) != Wall){//chosen direction
              player.move(player.x - squareSize * 2  , player.y)
              repaint()
            }
          }else if(pressedKeys contains Key.Right ) {
            if(player.jumpCanMoveTo(player.x + squareSize * 2, player.y ) && world(player.y)(player.x + squareSize * 2 ) != Wall){
              player.move(player.x + squareSize * 2 , player.y)
              repaint()
            }
          }else if(pressedKeys contains Key.Up) {
            if(player.jumpCanMoveTo(player.x , player.y - squareSize * 2) && world(player.y - squareSize * 2)(player.x) != Wall ){
              player.move(player.x, player.y - squareSize * 2)
              repaint()
            }
          }else if(pressedKeys contains Key.Down){
            if(player.jumpCanMoveTo(player.x, player.y + squareSize * 2) && world(player.y + squareSize * 2)(player.x) != Wall ){
              player.move(player.x, player.y + squareSize * 2 )
              repaint()
            }
          }   
        }else if(pressedKeys contains Key.Left ){
          if(player.canMoveTo(player.x - squareSize, player.y) && world(player.y)(player.x - squareSize) != Wall){
            player.move(player.x - squareSize, player.y)
            repaint()
            }
        }else if(pressedKeys contains Key.Right) {
          if(player.canMoveTo(player.x + squareSize, player.y) && world(player.y)(player.x + squareSize ) != Wall ){
            player.move(player.x + squareSize, player.y)
            repaint()
            }
        }else if(pressedKeys contains Key.Up) {
         if(player.canMoveTo(player.x , player.y - squareSize) && world(player.y - squareSize)(player.x) != Wall){
            player.move(player.x, player.y - squareSize)
            repaint()
            
          }
        }else if(pressedKeys contains Key.Down){
          if(player.canMoveTo(player.x , player.y + squareSize) && world(player.y + squareSize)(player.x) != Wall ){
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