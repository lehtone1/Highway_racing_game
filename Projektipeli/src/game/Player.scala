package game

class Player(var x: Int, var y: Int) {
  def move(newX: Int, newY: Int) = {
    x = newX
    y = newY
  }

  def canMoveTo(xPos: Int, yPos: Int)= {
    //Math.abs(xPos - x) <= 10 && Math.abs(yPos - y) <= 10
    !((xPos < 0 || xPos >= game.Game.width*game.Game.squareSize)
        || (yPos < 0 || yPos >= game.Game.height*game.Game.squareSize))
  }
  
  def jumpCanMoveTo(xPos: Int, yPos: Int)= {
    //Math.abs(xPos - x) <= 10 && Math.abs(yPos - y) <= 10
    !((xPos < 0 || xPos >= game.Game.width*game.Game.squareSize)
        || (yPos < 0 || yPos >= game.Game.height*game.Game.squareSize))
  }
  

}


