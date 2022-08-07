package y88.kirill.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientGUI.class.getResource("MyLightChat1Client.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Client!");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}