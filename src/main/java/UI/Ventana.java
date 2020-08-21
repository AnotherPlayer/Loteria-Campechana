package UI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Ventana extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/UI/Ventana.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        Platform.setImplicitExit(false);
        stage.setOnCloseRequest(event -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Platform.exit();
                    System.exit(0);
                }
            });
        });

        stage.setScene(scene);
        stage.setTitle("Loteria Google TTS - rev 21");
        stage.show();
    }
}
