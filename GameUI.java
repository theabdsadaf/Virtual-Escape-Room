import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameUI {
    private final int currentLevel;
    private final ScreenManager screenManager;

    private final List<Pair<String, String>> questionsAndAnswers = new ArrayList<>();
    private int correctAnswers = 0;

    private Label roomLabel;
    private Label roomNameLabel;
    private VBox layout;

    public GameUI(int level, ScreenManager screenManager) {
        this.currentLevel = level;
        this.screenManager = screenManager;

        loadLevelData(level); // Load level-specific data from a file
        createUI(); // Create the UI for the level
    }

    // Load questions and room details from a text file
    private void loadLevelData(int level) {
        String fileName = "level" + level + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Read room number and name
            String[] roomInfo = reader.readLine().split("\\|");
            String roomNumber = "Room " + roomInfo[0];
            String roomName = roomInfo[1];

            roomLabel = new Label(roomNumber);
            roomLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: white;");

            roomNameLabel = new Label(roomName);
            roomNameLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: gray;");

            // Read questions and answers
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    questionsAndAnswers.add(new Pair<>(parts[0].trim(), parts[1].trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create the UI for the level
    private void createUI() {
        layout = new VBox(40); // Add spacing between elements
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Add room number and room name labels
        layout.getChildren().addAll(roomLabel, roomNameLabel);

        // Add each question as a new HBox
        for (Pair<String, String> qa : questionsAndAnswers) {
            HBox questionBox = createQuestionBox(qa.getKey(), qa.getValue());
            layout.getChildren().add(questionBox);
        }

        layout.setStyle("-fx-background-color: black;"); // Set the background color to black
    }

    // Create an HBox containing the question, answer field, and submit button
    private HBox createQuestionBox(String question, String answer) {
        Label questionLabel = new Label("Q: " + question);
        questionLabel.setFont(new Font("Arial", 24)); // Larger font size for the question
        questionLabel.setTextFill(Color.WHITE);

        TextField answerField = new TextField();
        answerField.setPromptText("Enter your answer");
        answerField.setStyle("-fx-font-size: 20px; -fx-border-radius: 5px;");
        answerField.setPrefWidth(300);

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-font-size: 20px; -fx-background-color: lightblue; -fx-text-fill: black;");
        submitButton.setPrefWidth(120);

        // Status indicator (✔, ✘, ⚠)
        Text statusText = new Text();
        statusText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        submitButton.setOnAction(e -> {
            String userAnswer = answerField.getText().trim();

            if (userAnswer.isEmpty()) {
                statusText.setText("⚠");
                statusText.setFill(Color.YELLOW);
                answerField.setStyle("-fx-border-color: yellow; -fx-border-width: 2; -fx-font-size: 20px;");
            } else if (userAnswer.equalsIgnoreCase(answer)) {
                statusText.setText("✔");
                statusText.setFill(Color.GREEN);
                answerField.setStyle("-fx-border-color: green; -fx-border-width: 2; -fx-font-size: 20px;");
                correctAnswers++;
                answerField.setDisable(true);
                submitButton.setDisable(true);

                // Check if all answers are correct
                if (correctAnswers == questionsAndAnswers.size()) {
                    showLevelCompleteMessage();
                }
            } else {
                statusText.setText("✘");
                statusText.setFill(Color.RED);
                answerField.setStyle("-fx-border-color: red; -fx-border-width: 2; -fx-font-size: 20px;");
            }
        });

        // Reset the border when the user starts typing
        answerField.setOnKeyTyped(e -> {
            answerField.setStyle("-fx-border-color: none; -fx-font-size: 20px;");
        });

        HBox questionBox = new HBox(20, questionLabel, answerField, submitButton, statusText); // Adjust spacing
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setPadding(new Insets(10));
        return questionBox;
    }

    // Show a level complete message and proceed to the next level
    private void showLevelCompleteMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Level Complete");
        alert.setHeaderText("Congratulations!");
        alert.setContentText("You have completed Room " + currentLevel + "!");
        alert.showAndWait();

        // Notify ScreenManager to handle level transition
        screenManager.showLevelCompleteScreen(currentLevel);
    }


    // Create the scene for the current level
    public Scene getScene(Stage primaryStage) {
        // Create an Exit button at the top-right corner
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 18px; -fx-background-color: red; -fx-text-fill: white; -fx-border-color: black; -fx-border-width: 2;");
        exitButton.setOnAction(e -> primaryStage.close());

        HBox topBar = new HBox(exitButton);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: black;");

        // Set up the root layout
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(layout);
        root.setStyle("-fx-background-color: black;");

        return new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
    }
}