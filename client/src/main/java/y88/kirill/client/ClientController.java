package y88.kirill.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    private NetWork netWork;
//    private String login;
//    private String password;
    private ObservableList<String> clientsList;

    private String loginRecipient = "";

    public ClientController() {
        netWork = new NetWork(this);

    }

    @FXML
    protected void onHelloButtonClick() {
        System.out.println("button press");
        //   welcomeText.setText("Welcome to JavaFX Application!");
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "выбрана отправка сообщений для общего чата", ButtonType.OK);
            loginRecipient = "";
            alert.showAndWait();
        });
    }

    @FXML
    private TextArea textArea;

    @FXML
    private TextField fieldMsg;

    @FXML
    private TextField host;

    @FXML
    private TextField port;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    @FXML
    ListView<String> clientsView;




//    @FXML
//    protected void sendMsg(){
//        System.out.println("button press");
//        textArea.appendText(fieldMsg.getText() + "\n");
//        fieldMsg.setText("");
//    }

    @FXML
    protected void connect(){
        String host = this.host.getText();
        int port = Integer.parseInt(this.port.getText());
        netWork.connect(host, port);
    }

    @FXML
    public void disconnect(){
        String msg = String.format("%s%s",
                ParseMessage.EXIT.getTitle(),
                ParseMessage.DELIMITER.getTitle());
        netWork.disconnect(msg);
    }

    @FXML
    protected void sendMsg(){
//        System.out.println("button press");
//        textArea.appendText(fieldMsg.getText() + "\n");
//        fieldMsg.setText("");
        String msg;
        if(loginRecipient.equals("")){
            msg = String.format("%s%s%s",
                    ParseMessage.SENDALL.getTitle(),
                    ParseMessage.DELIMITER.getTitle(),
                    fieldMsg.getText());
        }else {
            msg = String.format("%s%s%s%s%s%s",
                    ParseMessage.SENDTO.getTitle(),
                    ParseMessage.DELIMITER.getTitle(),
                    loginRecipient,
                    ParseMessage.DELIMITER.getTitle(),
                    fieldMsg.getText(),
                    ParseMessage.DELIMITER.getTitle());
        }
        netWork.sendMessage(msg);
        System.out.println("login recipient =" + loginRecipient);
        //    loginRecipient = "";
    }

    @FXML
    public void signIn(){
        String msg = String.format("%s%s%s%s%s",
                ParseMessage.SIGNIN.getTitle(),
                ParseMessage.DELIMITER.getTitle(),
                login.getText(),
                ParseMessage.DELIMITER.getTitle(),
                password.getText());
        netWork.sendMessage(msg);
    }

    @FXML
    public void signOut(){
        String msg = String.format("%s",
                ParseMessage.SIGNOUT.getTitle());
        netWork.sendMessage(msg);
    }

    public void receivedMsg(String msg){
        String addMsg = String.format("%s \n", msg);
        textArea.appendText(addMsg);
    }

    public void receivingPrivateMsg(String msg){
        System.out.println("msg =" + msg);

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
            alert.showAndWait();
        });
    }

    public void updateClientList(List<String> clients){
        Platform.runLater(()->{
            clientsList.clear();
            clientsList.addAll(clients);
        });

     //   clientsView.setItems(clientsList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientsView.setVisible(true);
        clientsList = FXCollections.observableArrayList();
        clientsView.setItems(clientsList);
    }

    @FXML
    public void clickClientList(MouseEvent mouseEvent){
        if(mouseEvent.getClickCount() == 2){
             loginRecipient = clientsView.getSelectionModel().getSelectedItem();
             fieldMsg.requestFocus();
        }


    }


}