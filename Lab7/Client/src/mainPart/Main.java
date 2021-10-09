package mainPart;


/**
 * This is main
 */
public class Main {
    public static void main(String[] args) {
            ServerConnect serverConnect = new ServerConnect("127.0.0.1", 2456);
            ClientPart clientPart = new ClientPart(serverConnect, System.in);
            clientPart.authorization();
            clientPart.understanding();
    }
}
