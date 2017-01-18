package game

class Player(var x: Int, var y: Int) {
  def move(newX: Int, newY: Int) = {
    x = newX
    y = newY
  }

  def canMoveTo(xPos: Int, yPos: Int)= {
    //Math.abs(xPos - x) <= 10 && Math.abs(yPos - y) <= 10
    !((xPos < 0 || xPos >= game.MainFrame2.width*game.MainFrame2.squareSize)|| (yPos < 0 || yPos > game.MainFrame2.height*game.MainFrame2.squareSize))
  }
  
  def jumpCanMoveTo(xPos: Int, yPos: Int)= {
    //Math.abs(xPos - x) <= 10 && Math.abs(yPos - y) <= 10
    !((xPos < 0 || xPos >= game.MainFrame2.width*game.MainFrame2.squareSize)|| (yPos < 0 || yPos > game.MainFrame2.height*game.MainFrame2.squareSize))
  }
  

}


