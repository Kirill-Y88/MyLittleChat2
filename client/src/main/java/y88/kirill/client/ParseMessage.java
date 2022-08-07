package y88.kirill.client;

public enum ParseMessage {
    DELIMITER("±"),
    LOGIN("login"),
    PASSWORD("password"),
    SENDALL("sendAll"),
    SENDTO("sendTo"),
    SIGNIN("signIn"),
    SIGNOUT("signOut"),
    REGISTER("register"),
    CLIENTLIST("clientList"),
    EXIT("exit");


    private String title;

    ParseMessage(String title){
        this.title = title;
    }

    ParseMessage(){
    }

    public String getTitle(){
        return title;
    }
}
