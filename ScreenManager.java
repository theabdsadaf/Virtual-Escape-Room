import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScreenManager {

    private Stage primaryStage;

    public ScreenManager(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setMaximized(true);
    }

    public void showStartScreen() {
        VBox startScreen = createStartScreen();

        // Set the scene on the primary stage
        Scene scene = new Scene(startScreen, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Escape Room");
        primaryStage.show();
    }

    private VBox createStartScreen() {
        // Title
        Label title = new Label("Welcome to the Escape Room!");
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Start Button
        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 18px; -fx-background-color: lightblue; -fx-text-fill: black;");
        startButton.setOnAction(e -> showLevelScreen(1)); // Start at Level 1

        // Layout
        VBox layout = new VBox(20, title, startButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: black;");
        return layout;
    }

    public void showLevelScreen(int level) {
        Label levelLabel = new Label("Entering Room " + level + "...");
        levelLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox levelScreen = new VBox(20, levelLabel);
        levelScreen.setAlignment(Pos.CENTER);
        levelScreen.setStyle("-fx-background-color: black;");
        levelScreen.setPadding(new Insets(20));

        // Set the scene
        Scene levelScene = new Scene(levelScreen, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(levelScene);

        // After 2 seconds, show the game screen
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Pause for 2 seconds
                Platform.runLater(() -> showGameScreen(level));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showGameScreen(int level) {
        GameUI gameUI = new GameUI(level, this);

        // Set the game scene
        Scene gameScene = gameUI.getScene(primaryStage);
        primaryStage.setScene(gameScene);
    }

    public void showLevelCompleteScreen(int currentLevel) {
        if (currentLevel == getFinalLevel()) {
            // If the current level is the final level, show the exit screen
            showExitScreen();
            return;
        }

        // Otherwise, show the congratulations message
        Label congratsLabel = new Label("Congratulations! You've completed Room " + currentLevel + "!");
        congratsLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: green;");

        // Next Level Button
        Button nextLevelButton = new Button("Next Level");
        nextLevelButton.setStyle("-fx-font-size: 18px; -fx-background-color: green; -fx-text-fill: black;");
        nextLevelButton.setOnAction(e -> showLevelScreen(currentLevel + 1));

        // Exit Button
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 18px; -fx-background-color: red; -fx-text-fill: white;");
        exitButton.setOnAction(e -> showExitScreen());

        VBox layout = new VBox(20, congratsLabel, nextLevelButton, exitButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: black;");
        layout.setPadding(new Insets(20));

        // Set the scene
        Scene scene = new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(scene);
    }

    // Helper method to get the final level
    private int getFinalLevel() {
        return 5; // Adjust this value based on the total number of levels in your game
    }


    public void showExitScreen() {
        Label thankYouLabel = new Label("Thank you for playing!");
        thankYouLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label goodbyeLabel = new Label("We hope to see you again!");
        goodbyeLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: gray;");

        VBox layout = new VBox(20, thankYouLabel, goodbyeLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: black;");
        layout.setPadding(new Insets(20));

        // Set the scene
        Scene scene = new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(scene);

        // Close the application after 5 seconds
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 5-second delay
                Platform.runLater(primaryStage::close);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}