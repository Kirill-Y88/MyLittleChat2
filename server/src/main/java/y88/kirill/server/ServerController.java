package y88.kirill.server;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;


public class ServerController implements Initializable {

    Server server;
    private static final Logger log = LoggerFactory.getLogger(ServerController.class);

    public ServerController() {
        server = new Server(this);
      //  serverStart();
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
        server.start(Integer.parseInt(fieldPort.getText()));
        log.info("Server start in port: " + fieldPort.getText());
      //  server.start();
    }

    public void serverStop(){
        server.stop();
    }

    public TextArea getTextArea() {
        return textArea;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        server.start(8188);
        log.info("Server start in port 8188");
    }
}