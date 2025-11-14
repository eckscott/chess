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
        drawBoard();
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
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "   " + " a " + "  b  "  + " c " + " d " + "  e " + " f " + "  g " + "  h  " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 8 " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY  + EscapeSequences.BLACK_ROOK  + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_KNIGHT + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_BISHOP +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_QUEEN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_KING + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_BISHOP +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_KNIGHT + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_ROOK + EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 7 " + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_PAWN +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_PAWN +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 6 " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY  + EscapeSequences.EMPTY  + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 5 " + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 4 " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY  + EscapeSequences.EMPTY  + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 3 " + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 2 " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY  + EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_PAWN  + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_PAWN +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_PAWN +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " 1 " + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_ROOK + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_KNIGHT + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_BISHOP +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_QUEEN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_KING + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_BISHOP +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_KNIGHT + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_ROOK + EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "   " + "   " + "     "  + "   " + "   " + "    " + "   " + "    " + "     " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
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

