package ui;

import chess.ChessGame;
import client.ClientContext;
import client.States;
import model.*;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class InGame {

    private final ServerFacade server;
    private final ClientContext ctx;

    public InGame(int port, ClientContext ctx) {
        server = new ServerFacade(port);
        this.ctx = ctx;
    }

    public States run() throws Exception {

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

    private void drawBoard() {
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK +
                EscapeSequences.EMPTY + "a" + EscapeSequences.EMPTY + "b" + EscapeSequences.EMPTY + "c" + EscapeSequences.EMPTY + "d" + EscapeSequences.EMPTY +
                "e" + EscapeSequences.EMPTY + "f" + EscapeSequences.EMPTY + "g" + EscapeSequences.EMPTY + "h" + EscapeSequences.EMPTY + "\n" +

                " 8 " + EscapeSequences.BLACK_ROOK + EscapeSequences.EMPTY + EscapeSequences.BLACK_KNIGHT + EscapeSequences.EMPTY + EscapeSequences.BLACK_BISHOP + EscapeSequences.EMPTY +
                EscapeSequences.BLACK_QUEEN + EscapeSequences.EMPTY + EscapeSequences.BLACK_KING + EscapeSequences.EMPTY + EscapeSequences.BLACK_BISHOP + EscapeSequences.EMPTY +
                EscapeSequences.BLACK_KNIGHT + EscapeSequences.EMPTY + EscapeSequences.BLACK_ROOK + EscapeSequences.EMPTY + "\n");
    }

    public String eval(String input) throws Exception {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd){
            case "quit" -> "quit";
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


}

