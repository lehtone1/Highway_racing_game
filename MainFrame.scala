
package o1.game
import scala.swing._
import scala.swing.event._
import java.awt.Color
import java.awt.event._
import java.awt.{geom}
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

import java.net.URL
import javax.sound.sampled._


object MainFrame extends SimpleSwingApplication {
  println("go")
  /**
  val url = new URL("http://www.music.helsinki.fi/tmt/opetus/uusmedia/esim/a2002011001-e02.wav")
  val audioIn = AudioSystem.getAudioInputStream(url)
  val clip = AudioSystem.getClip
  clip.open(audioIn)
  clip.start()
  clip.loop(5)
  */
  val squareSize = 10
  val width = 200
  val height = 104
  val cellSize = 5
  var world: Array[Array[Spot]] = Array.fill(height, width)(Floor)
  var firstSquares: Array[Array[Spot]] = Array.fill(squareSize,width )(Wall)
  val r = scala.util.Random
  val player = new Player(0, 0)
  var pressedKeys = Buffer[Key.Value]() //A buffer that holds all keys that are pressed at the same time
  var trueFalseTable = Buffer[Boolean]()
  
  
  // Moves everything in the world one space downward
  def moveWorldDown = {
    var line = height - 1
    player.move(player.x, player.y + 1)
    while(line > 0){
      world(line) = world(line - 1)
      line -=1
    }
      
  }
  
   //Makes a true false array where the value is true if there is a wall and false if there is a wall
  def makeTrueFalseArray = {
    trueFalseTable = Buffer[Boolean]()
    var row = 0
    while(row < width){
       if((world(1)(row) == Wall && world(1 + squareSize)(row) == Wall) || (world(1 + squareSize)(row) == Wall && world(1 + 2 * squareSize)(row) == Wall) || (world(1 + 2 * squareSize)(row) == Wall && world(1 + 3 * squareSize)(row) == Wall)  ){
          trueFalseTable += false
       }else{
         trueFalseTable += true
       }
       row += 1
    }
  }
  
  //Checks if the first line of holed wall is acceptable and does not cause an impassable line
  def checkIfAcceptable = {
    var acceptable = false
    for(number <- 0 to width -1 ){
      if(trueFalseTable(number) && world(1)(number) == Floor){
        acceptable = true
      }
    }
    
    acceptable
  }
  
   //Creates a line of walls and floors
  def createHoledLine = {
      
      do{
        println("loop")
        val randomNum = 8 + r.nextInt(width/squareSize - 8)
        var crawler = 0
        while(crawler < randomNum){
          var randomNumTreeni = 1 +  r.nextInt(width/squareSize - 1)
          var randomNum2 = (squareSize * randomNumTreeni)
          var squareLowerLimit = randomNum2- squareSize
          for(line <- 0  until squareSize){
            for(place <- squareLowerLimit until randomNum2){
              firstSquares(line)(place) = Floor
            }
          }
          crawler += 1
          }   
      }while(checkIfAcceptable == false)
  
  }
  
  var background_IMG = ImageIO.read(new File("images/highway.png"))
  var playerIMG = ImageIO.read(new File("images/Player_Car_RED_updated.png"))
  var enemyIMG = ImageIO.read(new File("images/cartire50x50.png"))
  
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
  var speed = 1
  var INCREMENT = 1 // määrittää nopeuden jolla taustakuva liikkuu
                     // saman tekee myös timer arvo
                     // voidaan esimerkiksi tehdä niin, että lisätään moveBackground-metodiin
                     // if lause, että jos timer%50 niin INCREMENT kasvaa 5, eli siis jos
                     // timer on jaollinen 50 niin vauhti kasvaa 5 !ESIM!:p
    
  
  
  
  val canvas = new GridPanel(rows0 = width, cols0 = height) {

    
  preferredSize = new Dimension(width * cellSize, height * cellSize)
  
  //Paints the world again after some event
  
  
  
    override def paintComponent(g: Graphics2D) {
        println("paint")
        g.drawImage(background_IMG, dx1, dy1, dx2, dy2, srcx1, srcy2,
                      srcx2, srcy1, null);
        g.drawImage(background_IMG, dx1, dy1, dx2, dy2, srcx1, srcy2a,srcx2, srcy1a, null);
        
        g.drawString(progress_in_meters.toString()+"m", 10, 10)
        for (i <- 0 until width) {
          for (k <- 0 until height - 10) { // Loop through the world grid
            world(k)(i) match {       // Match what is found in every position
              case Wall => {          // If a wall is there, change color to black and paint a black tile representing a wall
                if((i  == 0 || i%10 == 0) && (world(k + 1)(i) == Floor || world(k + squareSize)(i) == Wall)) g.drawImage(enemyIMG, null, i * cellSize, k * cellSize - 50)
                //if(((i -1) == 0 && (k - 1) == 0) ||  ((i - 1)%10 == 0 && (k - 1)%10 == 0)) g.drawImage(enemyIMG, null, i * cellSize, k * cellSize)
              }
              case Floor => {         // If a floor is there, change color to cyan and paint a cyan tile representing floor
                g.setColor(myColor)
                g.fillRect(i * cellSize, k * cellSize, cellSize, cellSize)
              }
            }
          }
        }
        g.drawImage(playerIMG, null, player.x * cellSize, player.y* cellSize)
        
        
      }
  
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
    
    var timeInterval = 500
    var firstSquaresCrawler = 0
    
    //Creates Timer and the timer event
    val timeOut = new javax.swing.AbstractAction() {
      def actionPerformed(e : java.awt.event.ActionEvent) ={
        println("timer")
        if(firstSquaresCrawler == 0){
          firstSquares = Array.fill(height,width )(Wall)
          makeTrueFalseArray
          createHoledLine
        }
        canvas.moveBackground()
        repaint()
        progress_in_meters+=1
        moveWorldDown
        world(0) = firstSquares(firstSquaresCrawler)
        firstSquaresCrawler += 1
        if(firstSquaresCrawler == squareSize){
          firstSquaresCrawler = 0
        }
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
          contents += new MenuItem(Action("Exit") { 
            dispose()
            //clip.close()
            })  
        }
    }
    
    //Determines the game reactions for keyboards and mouse events
    reactions += {
      case KeyPressed(canvas, key, _, _) => { //When any key is pressed
        println(player.x + " * " + player.y)
        timeInterval -= 1 //Decreases the Time Interval between events
        t.setDelay(timeInterval) //Sets the new time interval to the Timer
        pressedKeys += key //the key is added to the pressed keys
        if(pressedKeys contains Key.Space){ //When space is one of the keys
          if(pressedKeys contains Key.Left){//the player moves two spaces to 
            if(world(player.y)(player.x  - squareSize * 2) != Wall){//chosen direction
              player.move(player.x - squareSize * 2  , player.y)
              repaint()
            }
          }else if(pressedKeys contains Key.Right) {
            if(world(player.y)(player.x + squareSize * 2 ) != Wall){
              player.move(player.x + squareSize * 2 , player.y)
              repaint()
            }
          }else if(pressedKeys contains Key.Up) {
            if(world(player.y - squareSize * 2)(player.x) != Wall){
              player.move(player.x, player.y - squareSize * 2)
              repaint()
            }
          }else if(pressedKeys contains Key.Down){
            if(world(player.y + squareSize * 2)(player.x) != Wall){
              player.move(player.x, player.y + squareSize * 2 )
              repaint()
            }
          }   
        }else if(pressedKeys contains Key.Left){
          if(world(player.y)(player.x - squareSize) != Wall){
            player.move(player.x - squareSize, player.y)
            repaint()
            }
        }else if(pressedKeys contains Key.Right) {
          if(world(player.y)(player.x + squareSize ) != Wall){
            player.move(player.x + squareSize, player.y)
            repaint()
            }
        }else if(pressedKeys contains Key.Up) {
          if(world(player.y - squareSize)(player.x) != Wall){
            player.move(player.x, player.y - squareSize)
            repaint()
            }
        }else if(pressedKeys contains Key.Down){
          if(world(player.y + squareSize)(player.x) != Wall){
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
  
  
  