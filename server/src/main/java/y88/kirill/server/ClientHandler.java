package y88.kirill.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


public class ClientHandler {

    private Server server;
    private Socket socket;
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private String login;
    private String password;

    private ExecutorService executorService;
    private Future future;


    public ClientHandler(Server server, Socket socket, ExecutorService executorService) {
        this.server = server;
        this.socket = socket;
        this.executorService = executorService;
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        startNewThread();
    }


    private void startNewThread(){
       // executorService.submit(() -> System.out.println("oooee"));
       future = executorService.submit(()->{
            String[] msg;
            while (true){
                try {
                    String fullMsg = dataInputStream.readUTF();
                 msg = fullMsg.split(ParseMessage.DELIMITER.getTitle());
                 if(msg[0].equals(ParseMessage.EXIT.getTitle())){
                     server.disconnectClient(this);
                     break;
                 }else if(msg[0].equals(ParseMessage.SIGNIN.getTitle())){
                     login = msg[1];
                     password = msg[2];
                     server.addClient(this);
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
                     server.sendMsgAll(this, msgAll);
                 }else {
                     System.out.println("запрос нераспознан + login = " + login);
                 server.viewMsg(msg[0]);}
                } catch (IOException e) {
                    log.error("Connection reset ");
                    break;
                }
            }
        });


//        new Thread(()->{
//            String[] msg;
//            while (true){
//                try {
//                    String fullMsg = dataInputStream.readUTF();
//                 msg = fullMsg.split(ParseMessage.DELIMITER.getTitle());
//                 if(msg[0].equals(ParseMessage.EXIT.getTitle())){
//                     break;
//                 }else if(msg[0].equals(ParseMessage.SIGNIN.getTitle())){
//                     login = msg[1];
//                     password = msg[2];
//                     server.addClient(this);
//                     log.info("signin login = " + login);
//                 }else if(msg[0].equals(ParseMessage.SIGNOUT.getTitle())){
//                     server.disconnectClient(this);
//                     log.info("signout login = " + login);
//                 }else if(msg[0].equals(ParseMessage.SENDTO.getTitle())){
//                     String recipientLogin = msg[1];
//                     String msgTo = msg[2];
//                     server.sendMsgTo(this,recipientLogin,msgTo);
//                 }else if(msg[0].equals(ParseMessage.SENDALL.getTitle())){
//                     String msgAll = msg[1];
//                     server.sendMsgAll(this, msgAll);
//                 }else {
//                     System.out.println("запрос нераспознан + login = " + login);
//                 server.viewMsg(msg[0]);}
//                } catch (IOException e) {
//                    log.error("Connection reset ");
//                    break;
//                }
//            }
//        }).start();
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

    public void closeTread(){
        future.cancel(true);
    }

}
