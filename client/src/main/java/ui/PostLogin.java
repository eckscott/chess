package ui;

import client.ClientContext;
import model.*;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class PostLogin {

    private final ServerFacade server;
    private States currState;
    private final ClientContext ctx;

    public PostLogin(int port, ClientContext ctx) {
        server = new ServerFacade(port);
        currState = States.SIGNEDIN;
        this.ctx = ctx;
    }

    public States run() throws Exception {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (currState == States.SIGNEDIN){
            printPrompt();
            String line = scanner.nextLine();
            result = eval(line);
            if (result.equals("quit")){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + "Thanks for playing!");
                currState = States.QUIT;
                return currState;
            }
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
        }
        return currState;
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + "[LOGGED IN] >>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) throws Exception {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd){
            case "quit" -> "quit";
            case "logout" -> logout(params);
            case "create game" -> createGame(params);
            default -> help();
        };
    }

    public String help() {
        return """
                help - lists command options
                quit - exits the program
                logout - logs out
                create game <NAME> - create a new chess game
                list games - list all the games
                play game <ID> [WHITE|BLACK] - join a game as a player
                observe game <ID> - spectate a game
                """;
    }

    private String logout(String... params) throws Exception {
        if (params.length != 0){
            throw new Exception("Provided parameters and expected none");
        }
        server.logout(ctx.getCurrUser());
        currState = States.QUIT;
        return "Thanks for playing!\n";
    }

    private String createGame(String... params) throws Exception {
        if (params.length == 1){
            var createGameReq = new GameData(0, null, null, params[0], null);
            var createGameResult = server.createGame(ctx.getCurrUser(), createGameReq);
            return String.format("gameID: %d\n", createGameResult.gameID());
        }
        throw new Exception("Wrong amount of parameters provided");
    }

}
