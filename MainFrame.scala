package game
import scala.swing._
import scala.swing.event._
import java.awt.Color
import java.awt.event._
import java.awt.{geom}
import game.Wall
import game.Spot
import game.Player
import game.Floor
import game.Timer
import scala.collection.mutable.Buffer

import java.io.File
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

object MainFrame extends SimpleSwingApplication  {
  //val background = game.BackgroundAnimation
  //val flowpanel = background.canvas
  val width = 40
  val height = 20
  val cellSize = 25
  var world: Array[Array[Spot]] = Array.fill(width, height)(Floor)
  val r = scala.util.Random
  val player = new Player(width/2, height/2)
  var pressedKeys = Buffer[Key.Value]() //A buffer that holds all keys that are pressed at the same time
  var trueFalseTable = Buffer[Boolean]()
  

  // Moves everything in the world one space downward
  def moveWorldDown = {
    var row = 0
    player.move(player.x, player.y + 1)
    while(row < width){
      var line = world(0).size
      while(line >= 0){
        if(line < height - 1) {
          world(row)(line + 1) = world(row)(line)
        }
        line -= 1
      }
      row += 1
    }
  }
  
  //Makes a true false array where the value is true if there is a wall and false if there is a wall
  def makeTrueFalseArray = {
    trueFalseTable = Buffer[Boolean]()
    var row = 0
    while(row < width){
       if((world(row)(1) == Wall && world(row)(2) == Wall) || (world(row)(2) == Wall && world(row)(3) == Wall) || (world(row)(3) == Wall && world(row)(4) == Wall)  ){
          trueFalseTable += false
       }else{
         trueFalseTable += true
       }
       row += 1
    }
  }
  
  //Checks if the first line of holed wall is acceptable and does not cause an impassable line
  //WORK IN PROGRES
  def checkIfAcceptable = {
    var acceptable = false
    for(number <- 0 to width -1 ){
      if(trueFalseTable(number) && world(number)(0) == Floor){
        acceptable = true
      }
    }
    acceptable
  }
  
  //Creates a line of walls and floors
  def createHoledLine = {
  
    //Creates a row full of wall objects
    def createWall(line: Int) = {
      world.map(_(line) = Wall)
    }
    
    //Creates holes marked with Floor-objects to the row.
    def createHoles(line: Int) = {
      do{ 
        createWall(0)
        val randomNum =  10 + r.nextInt(width  - 10)
        var crawler = 0
        while(crawler < randomNum){
          val randomNum2 = r.nextInt(width - 1)
          world(randomNum2)(line) = Floor
          crawler += 1
          }   
      }while(checkIfAcceptable == false)
      
      
    }
    createHoles(0)
  
  }
  
  var background_IMG = ImageIO.read(new File("images/csroad.png"))
  
  val FRAME_W=background_IMG.getWidth
  val FRAME_H=background_IMG.getHeight
  var progress_in_meters = 0

  
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
  var myColor = new Color(255,255,255,0)
  var srcy1a=0-FRAME_H
  var srcy2a=0
  var speed = 10
  var INCREMENT = 1 // määrittää nopeuden jolla taustakuva liikkuu
                     // saman tekee myös timer arvo
                     // voidaan esimerkiksi tehdä niin, että lisätään moveBackground-metodiin
                     // if lause, että jos timer%50 niin INCREMENT kasvaa 5, eli siis jos
                     // timer on jaollinen 50 niin vauhti kasvaa 5 !ESIM!:p
    
  
  val canvas = new GridPanel(rows0 = height, cols0 = width) {
    
  preferredSize = new Dimension(width * cellSize, height * cellSize)
  
  

  
  //Paints the world again after some event
  
  override def paintComponent(g: Graphics2D) {
      g.drawImage(background_IMG, dx1, dy1, dx2, dy2, srcx1, srcy2,
                    srcx2, srcy1, null);
      g.drawImage(background_IMG, dx1, dy1, dx2, dy2, srcx1, srcy2a,srcx2, srcy1a, null);
      
      g.drawString(progress_in_meters.toString()+"m", 10, 10)
      for (i <- 0 until width) {
        for (k <- 0 until height) { // Loop through the world grid
          world(i)(k) match {       // Match what is found in every position
            case Wall => {          // If a wall is there, change color to black and paint a black tile representing a wall
              g.setColor(Color.BLACK)
              g.fillRect(i * cellSize, k * cellSize, cellSize, cellSize)
            }
            case Floor => {         // If a floor is there, change color to cyan and paint a cyan tile representing floor
              g.setColor(myColor)
              g.fillRect(i * cellSize, k * cellSize, cellSize, cellSize)
            }
          }
        }
      }
      g.setColor(Color.ORANGE)      // Set color for the player to be drawn
      g.fillOval(player.x * cellSize, player.y * cellSize, cellSize, cellSize) // Draw player to its location
      g.setColor(Color.GRAY)
      
      
    }
  
  val timer = new javax.swing.Timer(speed,Swing.ActionListener { e =>
      
      moveBackground()
      repaint()
      progress_in_meters+=1
      })
    timer.start()
    
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
  
  
  def top = new MainFrame {
    title = "Game"
    preferredSize = new Dimension(width * cellSize, height * cellSize)

    contents = canvas
    
    listenTo(canvas.mouse.clicks)
    listenTo(canvas.keys)
    canvas.focusable = true
    canvas.requestFocus
    
    var timeInterval = 1500
    
    //Creates Timer and the timer event
    val timeOut = new javax.swing.AbstractAction() {
      def actionPerformed(e : java.awt.event.ActionEvent) ={
        moveWorldDown
        makeTrueFalseArray
        createHoledLine
        repaint()
      }
    }
    val t = new javax.swing.Timer(timeInterval, timeOut)
    t.setRepeats(true)
    t.start()
    
    
    

    val aboutText = """In this game your mission is to survive
                      |as long as possible. You will loose if your 
                      |game figure will drop off the gamefield. The game
                      |is moving the gamefield all the time downwards in an
                      |increasing speed so it is all the time harder to stay on
                      |the gamefield. The game creates obstacles on your path
                      |to make it harder for you to move forward. You can 
                      |jump over one obstacle, but if there is more that one
                      |obstacle after the other you cannot move through it""".stripMargin
                      
   
    val keyCommandsText = """Go Uo - Up Key
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
          contents += new MenuItem(Action("Exit") { dispose() })  
        }
    }
    //Determines the game reactions for keyboards and mouse events
    reactions += {
      case KeyPressed(canvas, key, _, _) => { //When any key is pressed
        timeInterval -= 2 //Decreases the Time Interval between events
        t.setDelay(timeInterval) //Sets the new time interval to the Timer
        pressedKeys += key //the key is added to the pressed keys
        if(pressedKeys contains Key.Space){ //When space is one of the keys
          if(pressedKeys contains Key.Left){//the player moves two spaces to 
            if(world(player.x - 2)(player.y) != Wall){//chosen direction
              player.move(player.x - 2, player.y)
              repaint()
            }
          }else if(pressedKeys contains Key.Right) {
            if(world(player.x + 2)(player.y) != Wall){
              player.move(player.x + 2, player.y)
              repaint()
            }
          }else if(pressedKeys contains Key.Up) {
            if(world(player.x)(player.y - 2) != Wall){
              player.move(player.x, player.y - 2)
              repaint()
            }
          }else if(pressedKeys contains Key.Down){
            if(world(player.x)(player.y + 2) != Wall){
              player.move(player.x, player.y + 2)
              repaint()
            }
          }   
        }else if(pressedKeys contains Key.Left){
          if(world(player.x - 1)(player.y) != Wall){
            player.move(player.x - 1, player.y)
            repaint()
            }
        }else if(pressedKeys contains Key.Right) {
          if(world(player.x + 1)(player.y) != Wall){
            player.move(player.x + 1, player.y)
            repaint()
            }
        }else if(pressedKeys contains Key.Up) {
          if(world(player.x)(player.y - 1) != Wall){
            player.move(player.x, player.y - 1)
            repaint()
            }
        }else if(pressedKeys contains Key.Down){
          if(world(player.x)(player.y + 1) != Wall){
            player.move(player.x, player.y + 1)
            repaint()
          }
        }else if(pressedKeys contains Key.Enter){ //moves the world downwards and creates a new line to the first line
          moveWorldDown
          makeTrueFalseArray
          createHoledLine
          repaint()
          }
      }
      
          
  
      case KeyReleased(canvas, key, _, _) => {
        pressedKeys = Buffer[Key.Value]()
      }
       
      case MouseClicked(canvas, point, _, clicks, _) => {  
        if(clicks == 2){
          world(point.x/cellSize)(point.y/cellSize) = Floor
          repaint()
        }else{
          world(point.x/cellSize)(point.y/cellSize) = Wall
          repaint()
        }
      }
    }

  }
}