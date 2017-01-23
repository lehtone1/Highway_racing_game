package game


import javax.sound.sampled._
import javax.sound.sampled._
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File

class Music(var src: String) {
  
  var audioInputStream = AudioSystem.getAudioInputStream(new File(src).getAbsoluteFile());
  val clip = AudioSystem.getClip
  clip.open(audioInputStream)
  
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