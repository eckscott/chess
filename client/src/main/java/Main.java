import chess.*;
import client.ClientContext;
import ui.PostLogin;
import ui.PreLogin;
import ui.States;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        var ctx = new ClientContext();
        States currState = States.SIGNEDOUT;

        PreLogin preLogin = new PreLogin(port, ctx);
        PostLogin postLogin = new PostLogin(port, ctx);

        try{
            while (currState != States.QUIT){
                switch (currState){
                    case SIGNEDOUT -> {
                        currState = preLogin.run();
                    }
                    case SIGNEDIN -> {
                        System.out.print("Signed in place holder");
                        currState = States.QUIT;
                    }
                    case INGAME -> {
                        System.out.print("In game place holder");
                        currState = States.QUIT;
                    }
                    default -> {
                        currState = States.QUIT;
                    }
                }
            }
        }
        catch (Exception e){
            System.out.printf("Unable to start the server: %s%n", e.getMessage());
        }
    }
}