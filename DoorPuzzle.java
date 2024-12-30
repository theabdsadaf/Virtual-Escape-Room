import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.*;
import java.util.*;

public class DoorPuzzle {
    private int x;
    private int y;
    private boolean[][] solvedDoors;

    public DoorPuzzle(int x, int y, boolean[][] solvedDoors) {
        this.x = x;
        this.y = y;
        this.solvedDoors = solvedDoors;
    }

    public boolean showPuzzle() {
        List<String[]> questions = loadQuestions();

        if (questions.isEmpty()) {
            Alert noQuestionsAlert = new Alert(Alert.AlertType.ERROR);
            noQuestionsAlert.setTitle("Error");
            noQuestionsAlert.setHeaderText(null);
            noQuestionsAlert.setContentText("No questions available. Please check the questions file.");
            noQuestionsAlert.showAndWait();
            return false;
        }

        Random rand = new Random();
        String[] questionData = questions.get(rand.nextInt(questions.size()));

        String questionText = questionData[0];
        String option1 = questionData[1];
        String option2 = questionData[2];
        String correctAnswer = questionData[3];

        Alert puzzleAlert = new Alert(Alert.AlertType.CONFIRMATION);
        puzzleAlert.setTitle("Puzzle");
        puzzleAlert.setHeaderText("Solve the Puzzle to Open the Door");
        puzzleAlert.setContentText(questionText);

        ButtonType answer1 = new ButtonType(option1);
        ButtonType answer2 = new ButtonType(option2);

        puzzleAlert.getButtonTypes().setAll(answer1, answer2);

        Optional<ButtonType> result = puzzleAlert.showAndWait();
        if (result.isPresent() && result.get().getText().equals(correctAnswer)) {
            solvedDoors[x][y] = true;
            return true;
        } else {
            Alert wrongAnswerAlert = new Alert(Alert.AlertType.ERROR);
            wrongAnswerAlert.setTitle("Wrong Answer");
            wrongAnswerAlert.setHeaderText(null);
            wrongAnswerAlert.setContentText("Wrong answer! Try again.");
            wrongAnswerAlert.showAndWait();
            return false;
        }
    }

    public List<String[]> loadQuestions() {
        List<String[]> questions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("questions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    questions.add(parts);
                } else {
                    System.out.println("Skipping invalid question format: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return questions;
    }

    public static void showCongratulations() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congratulations");
        alert.setHeaderText("You Escaped!");
        alert.setContentText("Congratulations! You've successfully escaped the maze.");
        alert.showAndWait();
    }

    public static void showInfo(String title, String message) {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle(title);
        infoAlert.setHeaderText(null);
        infoAlert.setContentText(message);
        infoAlert.showAndWait();
    }
}