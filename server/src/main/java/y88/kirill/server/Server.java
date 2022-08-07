package y88.kirill.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Server {
    private final int PORT = 8188;
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean startOn = false;
    private ServerController serverController;
    private static final Logger log = LoggerFactory.getLogger(Server.class);


   // private Vector<ClientHandler> clients;
    private Map<String, ClientHandler> clients = new ConcurrentHashMap<>();


    public Server(ServerController serverController) {
        this.serverController = serverController;
        this.clients = new ConcurrentHashMap<>();;
    }

    public void start(){
        startOn = true;
        new Thread( () -> {
        try {
            serverSocket = new ServerSocket(PORT);
           log.info("Server start");
            while (startOn){
                socket = serverSocket.accept();
                log.info("Server: client Accept");
                new ClientHandler(this, socket);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }).start();
    }

    public void stop(){
       // startOn = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewMsg(String msg){
        serverController.incomingMsg(msg);
    }

    public void addClient(ClientHandler clientHandler){
        clients.put(clientHandler.getLogin(), clientHandler);
        sendMsgAll();
    }

    public void disconnectClient(ClientHandler clientHandler){
        clients.remove(clientHandler.getLogin());
        sendMsgAll();
    }

    public void sendMsgTo(ClientHandler clientHandler, String recipientLogin , String msgTo){
        ClientHandler recipient = clients.get(recipientLogin);
        String msg = String.format("%s%s%s send: %s",
                ParseMessage.SENDTO.getTitle(),
                ParseMessage.DELIMITER.getTitle(),
                clientHandler.getLogin(),
                msgTo );
        recipient.sendMessage(msg);
        log.info(clientHandler.getLogin() + " send to " + recipientLogin + ": " + msgTo);
    }

    public void sendMsgAll(ClientHandler clientHandler, String msg){
        String msgAll = String.format("%s send: %s \n",clientHandler.getLogin(), msg );
        for (Map.Entry<String, ClientHandler> c: clients.entrySet()  ) {
            c.getValue().sendMessage(msgAll);
            serverController.getTextArea().appendText(msgAll);
        }
        log.info(clientHandler.getLogin() + " send all: " + msg);
    }

    private void sendMsgAll( ){
        StringBuilder sb = new StringBuilder();
        sb.append(ParseMessage.CLIENTLIST.getTitle());
        sb.append(ParseMessage.DELIMITER.getTitle());
        for (Map.Entry<String, ClientHandler> c: clients.entrySet()  ) {
            sb.append(c.getKey());
            sb.append(ParseMessage.DELIMITER.getTitle());
        }
        for (Map.Entry<String, ClientHandler> c: clients.entrySet()  ) {
            c.getValue().sendMessage(sb.toString());
            serverController.getTextArea().appendText(sb.toString());
        }
    }


}
