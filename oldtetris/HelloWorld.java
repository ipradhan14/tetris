package oldtetris;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
//import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class HelloWorld extends Application {

    private Tile[][] board;

    public static final String[] types = {"T", "J", "Z", "O", "S", "L", "I"};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tetris");


        Group root = new Group();
        Scene scene = new Scene(root, 800, 600, Color.BLACK);
        primaryStage.setScene(scene);


        // BEGIN TIMER STUFF
		Runnable task = new Runnable() {
			@Override
			public void run() {
				System.out.println("Hello World!");
			}
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		long initialDelay = 0L;
		long period = 1000L;

		executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
        //END TIMER STUFF


        scene.setFill(Color.rgb(50, 0, 0));



        Group tiles = new Group();
        for (int i = 0; i < 10; i++) {
            Tile t = new Tile(50 + i * 20, 50, Color.hsb(i * 36, 1.0, 1.0));
            tiles.getChildren().add(t);
        }
        root.getChildren().add(tiles);

        primaryStage.show();
    }
}
