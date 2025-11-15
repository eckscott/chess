import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import client.ClientContext;
import ui.EscapeSequences;
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


        while (currState != States.QUIT){
            switch (currState){
                case SIGNEDOUT -> {
                    try {
                        currState = preLogin.run();
                    }
                    catch (BadRequestException e){
                        System.out.printf(EscapeSequences.SET_TEXT_COLOR_RED + "ERROR: Bad request -- %s%n", e.getMessage());
                    }
                    catch (UnauthorizedException e){
                        System.out.printf(EscapeSequences.SET_TEXT_COLOR_RED + "ERROR: Unauthorized -- %s%n", e.getMessage());
                    }
                    catch (AlreadyTakenException e){
                        System.out.printf(EscapeSequences.SET_TEXT_COLOR_RED + e.getMessage());
                    }
                    catch (Exception e){
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + e.getMessage());
                    }
                }
                case SIGNEDIN -> {
                    try {
                        currState = postLogin.run();
                    }
                    catch (BadRequestException e){
                        System.out.printf(EscapeSequences.SET_TEXT_COLOR_RED + "ERROR: Bad request -- %s%n", e.getMessage());
                    }
                    catch (UnauthorizedException e){
                        System.out.printf(EscapeSequences.SET_TEXT_COLOR_RED + "ERROR: Unauthorized -- %s%n", e.getMessage());
                    }
                    catch (AlreadyTakenException e) {
                        System.out.printf(EscapeSequences.SET_TEXT_COLOR_RED + e.getMessage());
                    }
                    catch (Exception e){
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + e.getMessage());
                    }
                }
                case INGAME -> {
                    try {
                        currState = inGame.run();
                    }
                    catch (Exception e){
                        System.out.print("Just a catch for now");
                    }
                }
                default -> {
                    currState = States.QUIT;
                }
            }
        }
    }
}