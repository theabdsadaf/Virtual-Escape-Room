import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;

public class GameGrid {

    public static final int TILE_SIZE = 100;

    private final int width = 8;
    private final int height = 5;
    private boolean[][] doors;
    private boolean[][] solvedDoors;
    private GridPane gridPane;
    private Player player;

    public GameGrid(String selectedCharacter) {
        this.doors = new boolean[width][height];
        this.solvedDoors = new boolean[width][height];
        this.gridPane = new GridPane();
        this.player = new Player(selectedCharacter);

        setupDoors();
        initializeGrid();
        addPlayer();
        placeEndTileImage();
    }


    public GridPane getGridPane() {
        return gridPane;
    }

    private void placeEndTileImage() {
        Image image = new Image("Door.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(TILE_SIZE);
        imageView.setFitHeight(TILE_SIZE);
        StackPane endTile = new StackPane();
        endTile.getChildren().add(imageView);

        gridPane.add(endTile, 7, 4);
    }

    public void setupDoors() {
        Random random = new Random();
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                doors[i][j] = random.nextBoolean();
            }
        }
    }

    public void initializeGrid() {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setGridLinesVisible(true);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(TILE_SIZE, TILE_SIZE);

                Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
                if (doors[j][i]) {
                    rect.setFill(Color.web("#EEA47FFF"));
                } else {
                    rect.setFill(Color.web("#A8DADC"));
                }
                rect.setStroke(Color.web("#1D3557"));

                if (doors[j][i]) {
                    Text doorText = new Text("☢");
                    doorText.setFont(Font.font(40));
                    doorText.setFill(Color.BLACK);
                    cell.getChildren().addAll(rect, doorText);
                } else {
                    cell.getChildren().add(rect);
                }

                gridPane.add(cell, j, i);
            }
        }
    }

    public void addPlayer() {
        gridPane.add(player.getNode(), player.getX(), player.getY());
    }

    public void movePlayer(int dx, int dy) {
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;

        if (newX < 0 || newX >= width || newY < 0 || newY >= height) return;

        if (doors[newX][newY] && !solvedDoors[newX][newY]) {
            DoorPuzzle doorPuzzle = new DoorPuzzle(newX, newY, solvedDoors);
            if (doorPuzzle.showPuzzle()) {
                animateDoorOpening(newX, newY);
            }
            return;
        }

        if (newX == width - 1 && newY == height - 1) {
            if (!allDoorsOpened()) {
                DoorPuzzle.showInfo("Doors Locked", "You must unlock all doors before reaching the end!");
                return;
            } else {
                terminateGame();
            }
        }

        player.moveTo(newX, newY);
        GridPane.setConstraints(player.getNode(), player.getX(), player.getY());
    }

    public boolean allDoorsOpened() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (doors[i][j] && !solvedDoors[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void animateDoorOpening(int x, int y) {
        StackPane cell = (StackPane) gridPane.getChildren().get(y * width + x);
        Rectangle rect = (Rectangle) cell.getChildren().get(0);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), rect);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(e -> rect.setFill(Color.web("#A8DADC")));
        fadeTransition.play();
    }

    public void terminateGame() {
        DoorPuzzle.showCongratulations();
        Stage primaryStage = (Stage) gridPane.getScene().getWindow();
        EscapeRoomApp escapeRoomApp = new EscapeRoomApp();
        escapeRoomApp.showEndScreen(primaryStage);
    }
}