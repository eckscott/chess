import chess.*;
import ui.PreLogin;
import ui.States;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        States currState = States.SIGNEDOUT;

        try{
            while (currState != States.QUIT){
                switch (currState){
                    case SIGNEDOUT -> {
                        PreLogin preLogin = new PreLogin(port);
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