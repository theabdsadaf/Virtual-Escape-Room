import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EscapeRoomApp extends Application {

    public List<String> loadCharacterList() {
        List<String> characters = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("characterlist.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                characters.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error reading character list: " + e.getMessage());
        }
        return characters;
    }

    @Override
    public void start(Stage primaryStage) {
        VBox homeScreen = new VBox(20);
        homeScreen.setStyle("-fx-alignment: center; -fx-padding: 40; -fx-background-color: #A8DADC;");

        Text welcomeMessage = new Text("Virtual Escape Room!");
        welcomeMessage.setFont(Font.font("OCR A Extended", 40));
        welcomeMessage.setStyle("-fx-font-weight: bold; "
                + "-fx-fill: #1D3557; ");

        Button playButton = new Button("Play");
        playButton.setStyle("-fx-font-size: 18px; -fx-background-color: #457B9D; -fx-text-fill: white; "
                + "-fx-padding: 10px 20px; -fx-font-weight: bold; -fx-border-radius: 10px;");
        playButton.setOnAction(e -> startGame(primaryStage));

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 18px; -fx-background-color: #1D3557; -fx-text-fill: white; "
                + "-fx-padding: 10px 20px; -fx-font-weight: bold; -fx-border-radius: 10px;");
        exitButton.setOnAction(e -> primaryStage.close());

        HBox buttonBox = new HBox(20);
        buttonBox.setStyle("-fx-alignment: center;");
        buttonBox.getChildren().addAll(playButton, exitButton);

        homeScreen.getChildren().addAll(welcomeMessage, buttonBox);

        Scene homeScene = new Scene(homeScreen, 800, 500);

        primaryStage.setTitle("Escape Room - Home Screen");
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }

    public void startGame(Stage primaryStage) {
        List<String> characters = loadCharacterList();

        if (characters.isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("No Characters Found");
            errorAlert.setContentText("The character list is empty. Please check the characterlist.txt file.");
            errorAlert.showAndWait();
            return;
        }

        Alert startAlert = new Alert(Alert.AlertType.CONFIRMATION);
        startAlert.setTitle("Choose Your Character");
        startAlert.setHeaderText("Select a character to start the game:");
        startAlert.getDialogPane().setStyle("-fx-background-color: #A8DADC;");

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Choose Your Character");
        dialogStage.initOwner(primaryStage);

        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20px; -fx-background-color: #A8DADC;");

        List<ButtonType> characterButtons = new ArrayList<>();
        for (String character : characters) {
            ButtonType button = new ButtonType(character);
            characterButtons.add(button);
        }

        for (ButtonType button : characterButtons) {
            Button characterButton = new Button(button.getText());
            characterButton.setStyle("-fx-font-size: 14px; -fx-background-color: #457B9D; -fx-text-fill: white; "
                    + "-fx-border-radius: 5px; -fx-padding: 10px;");
            characterButton.setMinWidth(200);
            characterButton.setOnAction(e -> {
                startAlert.setResult(button);
                startAlert.close();
            });
            vbox.getChildren().add(characterButton);
        }

        startAlert.getDialogPane().setContent(vbox);

        Optional<ButtonType> result = startAlert.showAndWait();
        if (result.isPresent()) {
            String selectedCharacter = result.get().getText();

            Alert instructionsAlert = new Alert(Alert.AlertType.INFORMATION);
            instructionsAlert.setTitle("Game Instructions");
            instructionsAlert.setHeaderText("Important Information");
            instructionsAlert.setContentText("There is a secret pattern to unlock the doors. If you miss the sequence, the doors disappear.");

            ButtonType playButton = new ButtonType("Play");
            instructionsAlert.getButtonTypes().setAll(playButton);

            instructionsAlert.getDialogPane().setStyle("-fx-background-color: #A8DADC;");
            instructionsAlert.getButtonTypes().forEach(button -> {
                instructionsAlert.getDialogPane().lookupButton(button).setStyle("-fx-font-size: 14px; -fx-background-color: #457B9D; -fx-text-fill: white; -fx-border-radius: 5px;");
            });

            Optional<ButtonType> instructionsResult = instructionsAlert.showAndWait();
            if (instructionsResult.isPresent() && instructionsResult.get() == playButton) {
                GameGrid gameGrid = new GameGrid(selectedCharacter);
                Scene gameScene = new Scene(gameGrid.getGridPane(), 800, 500);

                gameScene.setOnKeyPressed(e -> {
                    switch (e.getCode()) {
                        case UP -> gameGrid.movePlayer(0, -1);
                        case DOWN -> gameGrid.movePlayer(0, 1);
                        case LEFT -> gameGrid.movePlayer(-1, 0);
                        case RIGHT -> gameGrid.movePlayer(1, 0);
                        case W -> gameGrid.movePlayer(0, -1);
                        case S -> gameGrid.movePlayer(0, 1);
                        case A -> gameGrid.movePlayer(-1, 0);
                        case D -> gameGrid.movePlayer(1, 0);
                    }
                });

                gameScene.setFill(javafx.scene.paint.Color.web("#F1FAEE"));

                primaryStage.setTitle("Escape Room");
                primaryStage.setScene(gameScene);
            }
        }
    }

    public void showEndScreen(Stage primaryStage) {
        VBox endScreen = new VBox(20);
        endScreen.setStyle("-fx-alignment: center; -fx-padding: 40; -fx-background-color: #A8DADC");

        Text gratitudeMessage = new Text("Thank You For Playing");
        gratitudeMessage.setStyle("-fx-font-size: 28px; "
                + "-fx-font-family: 'OCR A Extended'; "
                + "-fx-font-weight: bold; "
                + "-fx-fill: #1D3557; ");

        Text endMessage = new Text("Hope to see you again!");
        endMessage.setStyle("-fx-font-size: 20px; "
                + "-fx-font-family: 'OCR A Extended'; "
                + "-fx-font-weight: bold; "
                + "-fx-fill: #457B9D; ");

        Button playAgainButton = new Button("Play Again");
        playAgainButton.setStyle("-fx-font-size: 18px; -fx-background-color: #457B9D; -fx-text-fill: white; "
                + "-fx-padding: 10px 20px; -fx-font-weight: bold; -fx-border-radius: 10px;");
        playAgainButton.setOnAction(e -> startGame(primaryStage));

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 18px; -fx-background-color: #1D3557; -fx-text-fill: white; "
                + "-fx-padding: 10px 20px; -fx-font-weight: bold; -fx-border-radius: 10px;");
        exitButton.setOnAction(e -> primaryStage.close());

        HBox buttonBox = new HBox(20);
        buttonBox.setStyle("-fx-alignment: center;");
        buttonBox.getChildren().addAll(playAgainButton, exitButton);

        endScreen.getChildren().addAll(gratitudeMessage, endMessage, buttonBox);

        Scene endScene = new Scene(endScreen, 800, 500);

        primaryStage.setTitle("Escape Room - End Screen");
        primaryStage.setScene(endScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}