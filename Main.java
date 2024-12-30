import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize the ScreenManager and pass the primary stage
        ScreenManager screenManager = new ScreenManager(primaryStage);

        // Show the start screen
        screenManager.showStartScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

