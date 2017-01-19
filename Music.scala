package game

import java.net.URL
import javax.sound.sampled._

class Music(var src: URL) {
  
  val audioIn = AudioSystem.getAudioInputStream(src)
  val clip = AudioSystem.getClip
  clip.open(audioIn)
  
  def musicStart()={
    clip.start()
  }
  def musicLoop(time: Int)={
    clip.loop(time)
  }
  
  def musicClose()={
    clip.close()
  }
  
}