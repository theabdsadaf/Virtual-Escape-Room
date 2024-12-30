import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<String> questions;
    private List<String> answers;
    private int currentQuestionIndex;

    public Game(int level) {
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        loadLevelData(level); // Load questions and answers from the file
        currentQuestionIndex = 0;
    }

    private void loadLevelData(int level) {
        String fileName = "level" + level + ".txt"; // File name based on the level
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    questions.add(parts[0].trim());
                    answers.add(parts[1].trim());
                } else {
                    System.err.println("Invalid question format in file: " + fileName);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading level data from " + fileName + ": " + e.getMessage());
        }
    }

    public void reset() {
        currentQuestionIndex = 0;
    }

    public boolean hasNextQuestion() {
        return currentQuestionIndex < questions.size();
    }

    public String getNextQuestion() {
        if (hasNextQuestion()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean checkAnswer(String answer) {
        if (currentQuestionIndex < answers.size()) {
            boolean isCorrect = answers.get(currentQuestionIndex).equalsIgnoreCase(answer.trim());
            if (isCorrect) {
                currentQuestionIndex++; // Move to the next question
            }
            return isCorrect;
        }
        return false;
    }
}