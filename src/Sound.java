import javafx.application.*;
import javafx.stage.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import javafx.animation.*;
import javafx.util.Duration;

public class Sound extends Application {

  public static void main(String[] args) {
    launch(args);
  }  //end of main

  public void start(Stage primaryStage) {
    String musicFile = "tetris/res/music.mp3";     // For example

    Media sound = new Media(new File(musicFile).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(sound);
    mediaPlayer.play();

    Timeline timeline = new Timeline(new KeyFrame(
        Duration.millis(1000),
        ae -> mediaPlayer.pause()));
    timeline.play();

  }


} // end of class