package y88.kirill.server;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServerController {

    Server server;
    private static final Logger log = LoggerFactory.getLogger(ServerController.class);

    public ServerController() {
        server = new Server(this);
        serverStart();
    }

    //todo выпилить
    @FXML
    protected void onHelloButtonClick() {
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
        textArea.appendText(fieldMsg.getText() + "\n");
        fieldMsg.setText("");
    }

    public void incomingMsg(String msg) {
        textArea.appendText(msg + "\n");
    }

    public void serverStart(){
      //  server.start(Integer.parseInt(fieldPort.getText()));
        log.info("Controller start");
        server.start();
    }

    public void serverStop(){
        server.stop();
    }

    public TextArea getTextArea() {
        return textArea;
    }
}