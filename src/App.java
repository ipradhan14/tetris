import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class App extends Application {

    private TetrisGame game;
    private Rectangle[][] tileRectangles;
    private Group nextBrickGroup;
    private AtomicBoolean isLocked;
    private Text scoreText;
    private boolean isPlaying;
    private Group tiles;
    private Button pauseButton;
    private Image pauseImage;
    private Image playImage;
    private Text gameOverText;
    private int currentColErasing;
    private int[] rowsErasing;
    private boolean musicPlaying;
    
    private static final Color BG_COLOR = Color.rgb(55, 84, 96);
    private static final Color BOARD_COLOR = Color.rgb(18, 30, 35);
    private static final int TILE_SIDE_LENGTH = 30;
    private static final int BOARD_PADDING = 50;

    private static final String PAUSE_BUTTON = "res/pause.png";
    private static final String PLAY_BUTTON = "res/play.png";
    private static final String MUSIC_FILE = "src/res/music.mp3";
    private static final String MUSIC_ON = "res/musicon.png";
    private static final String MUSIC_OFF = "res/musicoff.png";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tetris");

        Group root = new Group();
        nextBrickGroup = new Group();
        root.getChildren().add(nextBrickGroup);

        Scene scene = new Scene(root,
                                2 * BOARD_PADDING + TILE_SIDE_LENGTH * Board.NUM_COLUMNS + 200,
                                2 * BOARD_PADDING + TILE_SIDE_LENGTH * Board.NUM_ROWS, BG_COLOR);
        primaryStage.setScene(scene);
        
        game = new TetrisGame();
        
        tiles = new Group();
        initializeTileRectangles();
        root.getChildren().add(tiles);
        
        isPlaying = false;
        pauseImage = new Image(getClass().getResourceAsStream(PAUSE_BUTTON));
        playImage = new Image(getClass().getResourceAsStream(PLAY_BUTTON));
        pauseButton = new Button();
        pauseButton.setLayoutX(2 * BOARD_PADDING + Board.NUM_COLUMNS * TILE_SIDE_LENGTH);
        pauseButton.setLayoutY(BOARD_PADDING + 2 * TILE_SIDE_LENGTH + 50);
        pauseButton.setGraphic(new ImageView(playImage));
        pauseButton.setOnAction(e -> {
            if (!isPlaying) {
                isPlaying = true;
                pauseButton.setGraphic(new ImageView(pauseImage));
                if (isLocked.get()) {
                    eraseRows(rowsErasing, currentColErasing);
                } else {
                    if (game.isGameOver()) {
                        game = new TetrisGame();
                        gameOverText.setText("");
                        initializeTileRectangles();
                    }
                    render();
                    renderNextBrick();
                    runGame();
                }
            } else {
                pauseButton.setGraphic(new ImageView(playImage));
                isPlaying = false;
            }
        });
        root.getChildren().add(pauseButton);

        Media sound = new Media(new File(MUSIC_FILE).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        
        musicPlaying = false;
        Image musicOffImage = new Image(getClass().getResourceAsStream(MUSIC_OFF));
        Image musicOnImage = new Image(getClass().getResourceAsStream(MUSIC_ON));
        Button musicButton = new Button();
        musicButton.setLayoutX(2 * BOARD_PADDING + Board.NUM_COLUMNS * TILE_SIDE_LENGTH);
        musicButton.setLayoutY(BOARD_PADDING + 2 * TILE_SIDE_LENGTH + 100);
        musicButton.setGraphic(new ImageView(musicOffImage));
        musicButton.setOnAction(e -> {
            if (!musicPlaying) {
                musicPlaying = true;
                musicButton.setGraphic(new ImageView(musicOnImage));
                mediaPlayer.play();
            } else {
                musicPlaying = false;
                musicButton.setGraphic(new ImageView(musicOffImage));
                mediaPlayer.stop();
            }
        });
        root.getChildren().add(musicButton);
        
        scoreText = new Text(2 * BOARD_PADDING + TILE_SIDE_LENGTH * Board.NUM_COLUMNS,
                            BOARD_PADDING + TILE_SIDE_LENGTH * Board.NUM_ROWS,
                            "0");
        scoreText.setFont(new Font(35));
        scoreText.setFill(Color.WHITE);
        root.getChildren().add(scoreText);
        
        gameOverText = new Text(BOARD_PADDING, BOARD_PADDING - 10, "");
        gameOverText.setFont(new Font(35));
        gameOverText.setFill(Color.WHITE);
        root.getChildren().add(gameOverText);
        
        EventHandler<KeyEvent> keyListener = event -> {
            if (isLocked.get() || game.isGameOver() || !isPlaying) {
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
        };
        isLocked = new AtomicBoolean(false);
        scene.setOnKeyPressed(keyListener);
        primaryStage.show();
        
        //runGame();
    }
    private void initializeTileRectangles() {
        tiles.getChildren().clear();
        tileRectangles = new Rectangle[Board.NUM_ROWS][Board.NUM_COLUMNS];
        for (int i = 0; i < Board.NUM_ROWS; i++) {
            for (int j = 0; j < Board.NUM_COLUMNS; j++) {
                Rectangle r = new Rectangle(BOARD_PADDING + j * TILE_SIDE_LENGTH, BOARD_PADDING + i * TILE_SIDE_LENGTH,
                    TILE_SIDE_LENGTH, TILE_SIDE_LENGTH);
                tileRectangles[i][j] = r;
                setEmptyTileRectangle(r);
                tiles.getChildren().add(r);
            }
        }
    }
    private void runGame() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(300),
                ae -> {
                    if (isPlaying) game.tick();
                    int[] deletedRows = game.deleteRows();
                    if (deletedRows.length > 0) { // LOCK CONTROLS
                        isLocked.set(true);
                        eraseRows(deletedRows, (game.getDisplay().getCols() - 1) / 2);
                    }
                    else {
                        render();
                        renderNextBrick();
                        if (game.isGameOver()) {
                            isPlaying = false;
                            pauseButton.setGraphic(new ImageView(playImage));
                        } else if (isPlaying) runGame();
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
                    setEmptyTileRectangle(r);
                }
                else {
                    drawTileRectangleOutline(r);
                    if (t.getColor() == 0) r.setFill(Color.BLUE);
                    else if (t.getColor() == 1) r.setFill(Color.RED);
                    else if (t.getColor() == 2) r.setFill(Color.GREEN);
                    else if (t.getColor() == 3) r.setFill(Color.PURPLE);
                }
            }
        }
        if (game.isGameOver()) {
            gameOverText.setText("GAME OVER");
        }
    }

    private void drawTileRectangleOutline(Rectangle r) {
        r.setArcWidth(3);
        r.setArcHeight(3);
        r.setStrokeType(StrokeType.INSIDE);
        r.setStroke(Color.WHITE);
        r.setStrokeWidth(1);
    }

    private void setEmptyTileRectangle(Rectangle r) {
        r.setFill(BOARD_COLOR);
        r.setStroke(BOARD_COLOR);
        r.setStrokeWidth(0);
        r.setArcWidth(0);
        r.setArcHeight(0);
    }

    private void renderNextBrick() {
        nextBrickGroup.getChildren().clear();
        int color = game.getNextColor();
        for (Position p : game.getNextBrickPositions()) {
            Rectangle r = new Rectangle(2 * BOARD_PADDING + (Board.NUM_COLUMNS + 1) * TILE_SIDE_LENGTH
                + p.getCol() * TILE_SIDE_LENGTH,
                                        BOARD_PADDING + p.getRow() * TILE_SIDE_LENGTH,
                TILE_SIDE_LENGTH,
                TILE_SIDE_LENGTH);
            drawTileRectangleOutline(r);
            if (color == 0) r.setFill(Color.BLUE);
            else if (color == 1) r.setFill(Color.RED);
            else if (color == 2) r.setFill(Color.GREEN);
            else if (color == 3) r.setFill(Color.PURPLE);
            nextBrickGroup.getChildren().add(r);
        }
    }

    private void eraseRows(int[] rows, int currentCol) {
        if (!isPlaying) {
            currentColErasing = currentCol;
            rowsErasing = rows;
            return;
        }
        if (currentCol == -1) {
            isLocked.set(false);
            render();
            renderNextBrick();
            runGame();
            return;
        }
        for (int row : rows) {
            Rectangle r = tileRectangles[row][currentCol];
            r.setFill(BOARD_COLOR);
            r.setStroke(BOARD_COLOR);
            r.setArcWidth(0);
            r.setArcHeight(0);

            r = tileRectangles[row][game.getDisplay().getCols() - currentCol - 1];
            r.setFill(BOARD_COLOR);
            r.setStroke(BOARD_COLOR);
            r.setArcWidth(0);
            r.setArcHeight(0);
        }
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(50),
                ae -> {
                    if (isPlaying) {
                        eraseRows(rows, currentCol - 1);
                    } else {
                        currentColErasing = currentCol - 1;
                        rowsErasing = rows;
                    }
                }));
        timeline.play();
    }

}
