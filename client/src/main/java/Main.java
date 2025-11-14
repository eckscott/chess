import client.ClientContext;
import ui.InGame;
import ui.PostLogin;
import ui.PreLogin;
import client.States;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        var ctx = new ClientContext();
        States currState = States.SIGNEDOUT;

        PreLogin preLogin = new PreLogin(port, ctx);
        PostLogin postLogin = new PostLogin(port, ctx);
        InGame inGame = new InGame(port, ctx);

        try{
            while (currState != States.QUIT){
                switch (currState){
                    case SIGNEDOUT -> {
                        currState = preLogin.run();
                    }
                    case SIGNEDIN -> {
                        currState = postLogin.run();
                    }
                    case INGAME -> {
                        currState = inGame.run();
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