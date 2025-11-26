package ui;

import chess.ChessGame;
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
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Type <help> for options");

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
            case "playgame" -> playGame(params);
            case "observegame" -> observeGame(params);
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
            throw new Exception(String.format("ERROR: Wanted 0 parameters and was provided %d\n", params.length));
        }
        server.logout(ctx.getCurrUser());
        ctx.setCurrState(States.SIGNEDOUT);
        return "Thanks for playing!\n";
    }

    private String createGame(String... params) throws Exception {
        if (params.length == 1){
            var createGameReq = new GameData(0, null, null, params[0], new ChessGame());
            var createGameResult = server.createGame(ctx.getCurrUser(), createGameReq);
            return String.format("%s created successfully!\n", createGameResult.gameName());
        }
        throw new Exception(String.format("ERROR: Wanted 1 parameter <GAMENAME> and was provided %d\n", params.length));
    }

    private String listGames(String... params) throws Exception {
        if (params.length != 0){
            throw new Exception(String.format("ERROR: Wanted 0 parameters and was provided %d\n", params.length));
        }
        ListGamesResponse response = server.listGames(ctx.getCurrUser());
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (GameData game : response.games()) {
            sb.append(String.format("%d. Name: %s, whitePlayer: %s, blackPlayer: %s%n",
                            i, game.gameName(),game.whiteUsername(), game.blackUsername()));
            i++;
        }
        if (response.games().isEmpty()){
            return "There are no games! Feel free to create one with the 'creategame' method\n";
        }
        return sb.toString();
    }

    private String playGame(String... params) throws Exception {
        if (params.length == 2){
            String gameIndex = params[0];
            String teamColor = params[1];
            GameData gameToJoin = server.findGame(ctx.getCurrUser(), gameIndex);
            if (teamColor.equals("white")){
                JoinGameData joinReq = new JoinGameData(ChessGame.TeamColor.WHITE, gameToJoin.gameID());
                server.joinGame(ctx.getCurrUser(), joinReq);
                ctx.setCurrState(States.INGAME);
                ctx.setCurrRole(ChessGame.TeamColor.WHITE);
                ctx.setCurrGame(server.findGame(ctx.getCurrUser(), gameIndex));
                return String.format("Joined game %s as white player\n", gameIndex);
            }
            else if (teamColor.equals("black")){
                JoinGameData joinReq = new JoinGameData(ChessGame.TeamColor.BLACK, gameToJoin.gameID());
                server.joinGame(ctx.getCurrUser(), joinReq);
                ctx.setCurrState(States.INGAME);
                ctx.setCurrRole(ChessGame.TeamColor.BLACK);
                ctx.setCurrGame(server.findGame(ctx.getCurrUser(), gameIndex));
                return String.format("Joined game %s as black player\n", gameIndex);
            }
            return "Couldn't join game";
        }
        throw new Exception(String.format("ERROR: Wanted 2 parameters <GAMEID> [WHITE|BLACK] and was provided %d\n", params.length));
    }

    private String observeGame(String... params) throws Exception{
        if (params.length == 1){
            String gameIndex = params[0];
            GameData gameToJoin = server.findGame(ctx.getCurrUser(), gameIndex);
            ctx.setCurrState(States.INGAME);
            ctx.setCurrRole(ChessGame.TeamColor.WHITE);
            ctx.setCurrGame(server.findGame(ctx.getCurrUser(), gameIndex));
            return String.format("Now observing game %s\n", gameIndex);
        }
        throw new Exception(String.format("ERROR: Wanted 1 parameters <GAMEID> and was provided %d\n", params.length));
    }
}
