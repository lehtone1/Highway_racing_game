package game

class Background(var x: Int, var y: Int){
  
  val FRAME_W=game.Game.background_IMG.getWidth
  val FRAME_H=game.Game.background_IMG.getHeight
  var INCREMENT = 1
  
  var x2=FRAME_W
  var y2=FRAME_H
  
  var srcx1=0
  var srcy1=0
  var srcx2=FRAME_W
  var srcy2=FRAME_H
  
  var srcy1a=0-FRAME_H
  var srcy2a=0
  
  def animateBackgroundMovement(){
    if (srcy1 == FRAME_H) {
      srcy1 = 1-FRAME_H
      srcy2 = 1
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


