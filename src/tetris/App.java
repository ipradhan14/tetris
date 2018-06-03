package tetris;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;

import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class App extends Application {

    private TetrisGame game;
    private Rectangle[][] tileRectangles;
    private Group root;
    private Group nextBrickGroup;
    private AtomicBoolean isLocked;
    private Text scoreText;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tetris");
        root = new Group();

        nextBrickGroup = new Group();
        root.getChildren().add(nextBrickGroup);

        Scene scene = new Scene(root, 800, 600, Color.BLACK);
        primaryStage.setScene(scene);
        game = new TetrisGame();
        Group tiles = new Group();
        tileRectangles = new Rectangle[20][10];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle r = new Rectangle(50 + j * 20, 50 + i * 20, 20, 20);
                tileRectangles[i][j] = r;
                tiles.getChildren().add(r);
            }
        }
        root.getChildren().add(tiles);
        scoreText = new Text(270, 450, "0");
        scoreText.setFont(new Font(35));
        scoreText.setFill(Color.WHITE);
        root.getChildren().add(scoreText);
        render();
        renderNextBrick();
        primaryStage.show();
        EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (isLocked.get()) {
                    event.consume();
                    return;
                }
                if (event.getCode() == KeyCode.DOWN) {
                    boolean succeeded = game.handleInput(new Position(1, 0), 0);
                    if (succeeded) render();
                } else if (event.getCode() == KeyCode.LEFT) {
                    boolean succeeded = game.handleInput(new Position(0, -1), 0);
                    if (succeeded) render();
                } else if (event.getCode() == KeyCode.RIGHT) {
                    boolean succeeded = game.handleInput(new Position(0, 1), 0);
                    if (succeeded) render();
                } else if (event.getCode() == KeyCode.X) {
                    boolean succeeded = game.handleInput(new Position(0, 0), 1);
                    if (succeeded) render();
                } else if (event.getCode() == KeyCode.Z) {
                    boolean succeeded = game.handleInput(new Position(0, 0), -1);
                    if (succeeded) render();
                }
                event.consume();
            }
        };
        isLocked = new AtomicBoolean(false);
        scene.setOnKeyPressed(keyListener);

        runGame();
    }

    private void runGame() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(300),
                ae -> {
                    game.tick();
                    int[] deletedRows = game.deleteRows();
                    if (deletedRows.length > 0) { // LOCK CONTROLS
                        isLocked.set(true);
                        eraseRows(deletedRows, (game.getDisplay().getCols() - 1) / 2);
                    }
                    else {
                        render();
                        renderNextBrick();
                        if (!game.isGameOver()) runGame();
                    }
                }));
        timeline.play();
    }


    private void render() {
        Board board = game.getDisplay();
        scoreText.setText(Integer.toString(game.getScore()));
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                Tile t = board.getTile(new Position(i, j));
                Rectangle r = tileRectangles[i][j];
                if (t == Tile.AIR) {
                    r.setFill(Color.GREY);
                    r.setStroke(Color.GREY);
                    r.setArcWidth(0);
                    r.setArcHeight(0);
                }
                else {
                    r.setArcWidth(3);
                    r.setArcHeight(3);
                    r.setStrokeType(StrokeType.INSIDE);
                    r.setStroke(Color.WHITE);
                    r.setStrokeWidth(1);
                    if (t.getColor() == 0) r.setFill(Color.BLUE);
                    else if (t.getColor() == 1) r.setFill(Color.RED);
                    else if (t.getColor() == 2) r.setFill(Color.GREEN);
                    else if (t.getColor() == 3) r.setFill(Color.PURPLE);
                }
            }
        }
        if (game.isGameOver()) {
            Text t = new Text(50, 40, "GAME OVER");
            t.setFont(new Font(35));
            t.setFill(Color.WHITE);
            root.getChildren().add(t);
        }
    }

    private void renderNextBrick() {
        nextBrickGroup.getChildren().clear();
        int color = game.getNextColor();
        for (Position p : game.getNextBrickPositions()) {
            Rectangle r = new Rectangle(450 + p.getRow() * 20, 50 + p.getCol() * 20, 20, 20);
            r.setArcWidth(3);
            r.setArcHeight(3);
            r.setStrokeType(StrokeType.INSIDE);
            r.setStroke(Color.WHITE);
            r.setStrokeWidth(1);
            if (color == 0) r.setFill(Color.BLUE);
            else if (color == 1) r.setFill(Color.RED);
            else if (color == 2) r.setFill(Color.GREEN);
            else if (color == 3) r.setFill(Color.PURPLE);
            nextBrickGroup.getChildren().add(r);
        }
    }

    public void eraseRows(int[] rows, int currentCol) {
        if (currentCol == -1) {
            isLocked.set(false);
            runGame();
            return;
        }
        for (int row : rows) {
            Rectangle r = tileRectangles[row][currentCol];
            r.setFill(Color.GREY);
            r.setStroke(Color.GREY);
            r.setArcWidth(0);
            r.setArcHeight(0);

            r = tileRectangles[row][game.getDisplay().getCols() - currentCol - 1];
            r.setFill(Color.GREY);
            r.setStroke(Color.GREY);
            r.setArcWidth(0);
            r.setArcHeight(0);
        }
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(100),
                ae -> { eraseRows(rows, currentCol - 1);
                }));
        timeline.play();
    }

}
