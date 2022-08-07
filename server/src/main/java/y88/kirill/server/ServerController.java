package y88.kirill.server;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class ServerController {

    Server server;



    public ServerController() {
        server = new Server(this);
        serverStart();
    }

    //
    @FXML
    protected void onHelloButtonClick() {
        System.out.println("button press");
     //   welcomeText.setText("Welcome to JavaFX Application!");
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, fieldMsg.getText(), ButtonType.OK);
            alert.showAndWait();
        });
    }

    @FXML
    private TextArea textArea;

    @FXML
    private TextField fieldMsg;

    @FXML
    private TextField fieldPort;

    @FXML
    protected void sendMsg(){
        System.out.println("button press");
        textArea.appendText(fieldMsg.getText() + "\n");
        fieldMsg.setText("");
    }

    public void incomingMsg(String msg) {
        textArea.appendText(msg + "\n");
    }

    public void serverStart(){
      //  server.start(Integer.parseInt(fieldPort.getText()));
        server.start();
    }

    public void serverStop(){
        server.stop();
    }

    public TextArea getTextArea() {
        return textArea;
    }
}