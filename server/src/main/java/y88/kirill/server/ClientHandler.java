package y88.kirill.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private Server server;
    private Socket socket;
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private String login;
    private String password;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        startNewThread();
    }


    private void startNewThread(){
        new Thread(()->{
            String[] msg;
            while (true){
                try {
                    String fullMsg = dataInputStream.readUTF();
                 msg = fullMsg.split(ParseMessage.DELIMITER.getTitle());
                 if(msg[0].equals(ParseMessage.EXIT.getTitle())){
                     break;
                 }else if(msg[0].equals(ParseMessage.SIGNIN.getTitle())){
                     login = msg[1];
                     password = msg[2];
                     server.addClient(this);
                  //   System.out.println("signin login = " + login + " pass = " + password);
                     log.info("signin login = " + login);
                 }else if(msg[0].equals(ParseMessage.SIGNOUT.getTitle())){
                     server.disconnectClient(this);
                     log.info("signout login = " + login);
                 }else if(msg[0].equals(ParseMessage.SENDTO.getTitle())){
                     String recipientLogin = msg[1];
                     String msgTo = msg[2];
                     server.sendMsgTo(this,recipientLogin,msgTo);
                 }else if(msg[0].equals(ParseMessage.SENDALL.getTitle())){
                     String msgAll = msg[1];
                   //  System.out.println("sendall user = " + this.login +  " msg = " + msgAll);
                     server.sendMsgAll(this, msgAll);
                 }else {
                     System.out.println("запрос нераспознан + login = " + login);
                 server.viewMsg(msg[0]);}
                } catch (IOException e) {
                    log.error("Connection reset ");
                    //e.printStackTrace();
                    break;
                }
            }
        }).start();
    }


    public void sendMessage(String msg){
        try {
            dataOutputStream.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getLogin() {
        return login;
    }

}
