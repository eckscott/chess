import chess.*;
import ui.PreLogin;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        PreLogin preLogin = new PreLogin(port);
        try{
            preLogin.run();
        }
        catch (Exception e){
            System.out.printf("Unable to start the server: %s%n", e.getMessage());
        }
    }
}