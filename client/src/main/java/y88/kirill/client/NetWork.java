package y88.kirill.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class NetWork {
    private ClientController clientController;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private boolean isConnect;
    private List<String> clientList;

    public NetWork(ClientController clientController) {
        this.clientController = clientController;
    }

    public void connect(String host, int port){
        try {

            socket = new Socket(host,port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            isConnect = true;
            new Thread( ()->{
                String [] msg;
                while (true){
                    try {
                        String fullMsg = dataInputStream.readUTF();
                        System.out.println(" Full msg = " + fullMsg);
                        msg = (fullMsg).split(ParseMessage.DELIMITER.getTitle());
                        if(msg[0].equals(ParseMessage.SENDTO.getTitle())){
                            System.out.println("Network send to msg" + msg[0] + msg[1]);
                        clientController.receivingPrivateMsg("Message from " + msg[1]);
                        }else if(msg[0].equals(ParseMessage.CLIENTLIST.getTitle())){

                          //  List<String> clientList = List.of(msg);
                            List<String> clientList = Arrays.asList(msg);
//                            clientList.remove(0);
                            clientController.updateClientList(clientList);

                        }else {  //это сообщение "для всех"
                            clientController.receivedMsg(msg[0]);
                        }
                        System.out.println("received msg = " + msg[0]);
                        clientController.receivedMsg(msg[0] + "test ");
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }/*finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/


                }

                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }).start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void disconnect(String msg){
        try {
          //  isConnect = false;
            sendMessage(msg);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(String msg){
        try {
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
