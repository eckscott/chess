package ui;

import client.ClientContext;
import client.States;
import model.*;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class PostLogin {

    private final ServerFacade server;
    private final ClientContext ctx;

    public PostLogin(int port, ClientContext ctx) {
        server = new ServerFacade(port);
        this.ctx = ctx;
    }

    public States run() throws Exception {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "type <help> for options");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (ctx.getCurrState() == States.SIGNEDIN){
            printPrompt();
            String line = scanner.nextLine();
            result = eval(line);
            if (result.equals("quit")){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + "Thanks for playing!");
                ctx.setCurrState(States.QUIT);
                return ctx.getCurrState();
            }
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
        }
        return ctx.getCurrState();
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
            case "creategame" -> createGame(params);
            case "listgames" -> listGames(params);
            default -> help();
        };
    }

    public String help() {
        return """
                help - lists command options
                quit - exits the program
                logout - logs out
                creategame <NAME> - create a new chess game
                listgames - list all the games
                playgame <ID> [WHITE|BLACK] - join a game as a player
                observegame <ID> - spectate a game
                """;
    }

    private String logout(String... params) throws Exception {
        if (params.length != 0){
            throw new Exception("Provided parameters and expected none");
        }
        server.logout(ctx.getCurrUser());
        ctx.setCurrState(States.SIGNEDOUT);
        return "Thanks for playing!\n";
    }

    private String createGame(String... params) throws Exception {
        if (params.length == 1){
            var createGameReq = new GameData(0, null, null, params[0], null);
            var createGameResult = server.createGame(ctx.getCurrUser(), createGameReq);
            return String.format("%s created successfully!\n", createGameResult.gameName());
        }
        throw new Exception("Wrong amount of parameters provided");
    }

    private String listGames(String... params) throws Exception {
        if (params.length != 0){
            throw new Exception("Provided parameters and expected none");
        }
        ListGamesResponse response = server.listGames(ctx.getCurrUser());
        StringBuilder sb = new StringBuilder();
        for (GameData game : response.games()) {
            sb.append("Name: ").append(game.gameName())
                    .append(", whitePlayer: ").append(game.whiteUsername())
                    .append(", blackPlayer: ").append(game.blackUsername())
                    .append("\n");
        }
        return sb.toString();
    }

}
