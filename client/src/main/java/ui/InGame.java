package ui;

import chess.*;
import client.ClientContext;
import client.States;
import exceptions.InvalidInputException;
import model.*;
import server.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class InGame implements NotificationHandler {

    private final ServerFacade server;
    private final WebSocketFacade ws;
    private final ClientContext ctx;

    public InGame(int port, ClientContext ctx) {
        server = new ServerFacade(port);
        ws = new WebSocketFacade(port, this);
        this.ctx = ctx;
    }

    public States run() throws Exception {
        ws.joinGame(ctx.getCurrUser(), ctx.getCurrGame());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (ctx.getCurrState() == States.INGAME){
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
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }


    public String eval(String input) throws Exception {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd){
            case "quit" -> "quit";
            case "l" -> {
                ws.leaveGame(ctx.getCurrUser(), ctx.getCurrGame());
                ctx.setCurrState(States.SIGNEDIN);
                yield "You've exited the game! ";
            }
            case "board" -> {
                if (ctx.getCurrRole() == ChessGame.TeamColor.WHITE){
                    drawWhite(server.getGame(ctx.getCurrUser(), ctx.getCurrGame()).game().getBoard());
                }
                else if (ctx.getCurrRole() == ChessGame.TeamColor.BLACK){
                    drawBlack(server.getGame(ctx.getCurrUser(), ctx.getCurrGame()).game().getBoard());
                }
                yield "Current board ^^^\n";
            }
            case "m" -> makeMove(params);
            case "r" -> {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + "Are you sure you want to resign? (y/n):\n");
                printPrompt();
                Scanner scanner = new Scanner(System.in);
                String confirmation = scanner.nextLine();
                if (confirmation.equals("y")) {
                    ws.resign(ctx.getCurrUser(), ctx.getCurrGame());
                    yield "";
                }
                else {
                    yield "The game goes on!\n";
                }
            }
            case "h" -> highlightLegalMoves(params);
            default -> help();
        };
    }

    public String help() {
        return """
                help - lists command options
                board - Redraws the chess board
                l - Leave the game without resigning
                m <[start,position]> <[end,position]> - Moves a piece from start position to end position
                r - Forfeit the game and the game is over
                h <[start,position]> - highlight legal moves for the piece at the indicated position
                quit - exits the program
                """;
    }

    private String makeMove(String... params) throws IOException {
        try {
            if (params.length != 2) {
                throw new InvalidInputException("Invalid number of parameters provide! Please provide [start,position] [end,position]");
            }
            String pos1s = params[0].substring(1, params[0].length() - 1);
            String pos2s = params[1].substring(1, params[1].length() - 1);

            ChessPosition oldPos = moveConverter(pos1s);
            ChessPosition newPos = moveConverter(pos2s);
            var move = new ChessMove(oldPos, newPos, null);

            Collection<ChessMove> promoteMoves = new ArrayList<>();
            promoteMoves.add(new ChessMove(oldPos, newPos, ChessPiece.PieceType.ROOK));
            promoteMoves.add(new ChessMove(oldPos, newPos, ChessPiece.PieceType.KNIGHT));
            promoteMoves.add(new ChessMove(oldPos, newPos, ChessPiece.PieceType.BISHOP));
            promoteMoves.add(new ChessMove(oldPos, newPos, ChessPiece.PieceType.QUEEN));

            if (server.getGame(ctx.getCurrUser(), ctx.getCurrGame()).game().validMoves(oldPos).containsAll(promoteMoves)) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + "Please type which piece you would like to promote to:\n" +
                        "queen, bishop, knight, rook");
                printPrompt();
                Scanner scanner = new Scanner(System.in);
                String inputPiece = scanner.nextLine();
                move = new ChessMove(oldPos, newPos, promote(inputPiece));
            }
            ws.makeMove(ctx.getCurrUser(), ctx.getCurrGame(), move);
            return "";
        } catch (InvalidInputException e){
            System.out.printf(EscapeSequences.SET_TEXT_COLOR_RED + "ERROR: " + e.getMessage() + "\n");
            return "";
        } catch (NumberFormatException e){
            System.out.printf(EscapeSequences.SET_TEXT_COLOR_RED + "ERROR: Incorrect move format\n");
            return "";
        }
    }

    private ChessPiece.PieceType promote(String desiredPiece){
        return switch(desiredPiece){
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            case "rook" -> ChessPiece.PieceType.ROOK;
            default -> ChessPiece.PieceType.QUEEN;
        };
    }

    private String highlightLegalMoves(String... params){
        try {
            if (params.length != 1){
                throw new InvalidInputException("Invalid number of parameters provided! Please provide [chess,position]");
            }
            String posString = params[0].substring(1, params[0].length() - 1);
            ChessPosition pos = moveConverter(posString);
            ChessBoard board = server.getGame(ctx.getCurrUser(), ctx.getCurrGame()).game().getBoard();
            if (board.getPiece(pos) == null){
                throw new InvalidInputException("There's no piece there!");
            }
            Collection<ChessMove> legalMoves = server.getGame(ctx.getCurrUser(), ctx.getCurrGame()).game().validMoves(pos);
            if (ctx.getCurrRole() == ChessGame.TeamColor.BLACK) {
                System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                        "   " + " h " + "  g  " + " f " + " e " + "  d " + " c " + "  b " + "  a  " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");

                for (int i = 1; i <= 8; i = i + 2) {
                    System.out.printf(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " %d " +
                            EscapeSequences.SET_BG_COLOR_LIGHT_GREY, i);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 8), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 8)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 8)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 7), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 7)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 7)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 6), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 6)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 6)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 5), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 5)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 5)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 4), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 4)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 4)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 3), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 3)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 3)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 2), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 2)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 2)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 1), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 1)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 1)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
                    System.out.printf(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " %d " +
                            EscapeSequences.SET_BG_COLOR_RED, i + 1);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i + 1, 8), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i + 1, 8)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i + 1, 8)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i + 1, 7), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i + 1, 7)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i + 1, 7)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i + 1, 6), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i + 1, 6)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i + 1, 6)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i + 1, 5), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i + 1, 5)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i + 1, 5)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i + 1, 4), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i + 1, 4)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i + 1, 4)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i + 1, 3), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i + 1, 3)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i + 1, 3)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i + 1, 2), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i + 1, 2)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i + 1, 2)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i + 1, 1), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i + 1, 1)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i + 1, 1)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
                }

                System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                        "   " + "   " + "     " + "   " + "   " + "    " + "   " + "    " + "     " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
            } else {
                System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                        "   " + " a " + "  b  " + " c " + " d " + "  e " + " f " + "  g " + "  h  " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");

                for (int i = 8; i >= 1; i = i - 2) {
                    System.out.printf(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " %d " +
                            EscapeSequences.SET_BG_COLOR_LIGHT_GREY, i);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 1), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 1)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 1)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 2), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 2)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 2)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 3), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 3)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 3)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 4), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 4)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 4)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 5), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 5)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 5)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 6), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 6)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 6)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 7), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 7)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 7)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i, 8), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i, 8)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i, 8)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
                    System.out.printf(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " %d " +
                            EscapeSequences.SET_BG_COLOR_RED, i - 1);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i - 1, 1), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i - 1, 1)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i - 1, 1)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i - 1, 2), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i - 1, 2)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i - 1, 2)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i - 1, 3), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i - 1, 3)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i - 1, 3)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i - 1, 4), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i - 1, 4)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i - 1, 4)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i - 1, 5), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i - 1, 5)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i - 1, 5)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i - 1, 6), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i - 1, 6)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i - 1, 6)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i - 1, 7), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i - 1, 7)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i - 1, 7)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (legalMoves.contains(new ChessMove(pos, new ChessPosition(i - 1, 8), null))) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    if (board.getPiece(new ChessPosition(i - 1, 8)) != null) {
                        drawPiece(board.getPiece(new ChessPosition(i - 1, 8)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                    System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
                }

                System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                        "   " + "   " + "     " + "   " + "   " + "    " + "   " + "    " + "     " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
            }
            return "";
        } catch (InvalidInputException e){
            System.out.printf(EscapeSequences.SET_TEXT_COLOR_RED + "ERROR: " + e.getMessage() + "\n");
            return "";
        } catch (NumberFormatException e){
        System.out.printf(EscapeSequences.SET_TEXT_COLOR_RED + "ERROR: Incorrect move format\n");
        return "";
        }
    }

    private void drawWhite(ChessBoard board) {
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "   " + " a " + "  b  "  + " c " + " d " + "  e " + " f " + "  g " + "  h  " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");

        for (int i = 8; i >=1; i=i-2){
            System.out.printf(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " %d " +
                    EscapeSequences.SET_BG_COLOR_LIGHT_GREY, i);
            if (board.getPiece(new ChessPosition(i, 1)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 1)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i, 2)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 2)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i, 3)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 3)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i, 4)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 4)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i, 5)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 5)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i, 6)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 6)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i, 7)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 7)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i, 8)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 8)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
            System.out.printf(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " %d " +
                    EscapeSequences.SET_BG_COLOR_RED, i - 1);
            if (board.getPiece(new ChessPosition(i - 1, 1)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i - 1, 1)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i - 1, 2)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i - 1, 2)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i - 1, 3)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i - 1, 3)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i - 1, 4)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i - 1, 4)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i - 1, 5)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i - 1, 5)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i - 1, 6)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i - 1, 6)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i - 1, 7)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i - 1, 7)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i - 1, 8)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i - 1, 8)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
        }

        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "   " + "   " + "     "  + "   " + "   " + "    " + "   " + "    " + "     " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
    }

    private void drawBlack(ChessBoard board){
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "   " + " h " + "  g  "  + " f " + " e " + "  d " + " c " + "  b " + "  a  " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");

        for (int i = 1; i <=8; i=i+2){
            System.out.printf(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " %d " +
                    EscapeSequences.SET_BG_COLOR_LIGHT_GREY, i);
            if (board.getPiece(new ChessPosition(i, 8)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 8)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i, 7)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 7)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i, 6)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 6)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i, 5)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 5)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i, 4)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 4)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i, 3)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 3)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i, 2)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 2)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i, 1)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i, 1)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
            System.out.printf(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " %d " +
                    EscapeSequences.SET_BG_COLOR_RED, i + 1);
            if (board.getPiece(new ChessPosition(i + 1, 8)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i + 1, 8)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i + 1, 7)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i + 1, 7)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i + 1, 6)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i + 1, 6)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i + 1, 5)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i + 1, 5)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i + 1, 4)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i + 1, 4)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i + 1, 3)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i + 1, 3)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (board.getPiece(new ChessPosition(i + 1, 2)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i + 1, 2)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (board.getPiece(new ChessPosition(i + 1, 1)) != null) {
                drawPiece(board.getPiece(new ChessPosition(i + 1, 1)));
            }
            else{
                System.out.print(EscapeSequences.EMPTY);
            }

            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
        }

        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "   " + "   " + "     "  + "   " + "   " + "    " + "   " + "    " + "     " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
    }

    private void drawPiece(ChessPiece piece){
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            if (piece.getPieceType() == ChessPiece.PieceType.ROOK){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.BLACK_ROOK);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.BLACK_KNIGHT);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.BLACK_BISHOP);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.QUEEN){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.BLACK_QUEEN);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KING){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.BLACK_KING);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.BLACK_PAWN);
            }
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            if (piece.getPieceType() == ChessPiece.PieceType.ROOK){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_ROOK);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_KNIGHT);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_BISHOP);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.QUEEN){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_QUEEN);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KING){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_KING);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_PAWN);
            }
        }
    }

    private ChessPosition moveConverter(String posString){
        String[] pos = posString.split(",");
        int r = Integer.parseInt(pos[0]);
        char colChar = pos[1].charAt(0);
        int c = Character.toLowerCase(colChar) - 'a'+ 1;
        return new ChessPosition(r, c);
    }

    @Override
    public void notify(NotificationMessage message) {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_BLUE + message.getMessage());
        printPrompt();
    }

    @Override
    public void errorMessage(ErrorMessage message) {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_RED + message.getMessage());
        printPrompt();
    }

    @Override
    public void loadGame(LoadGameMessage message) {
        System.out.print("\n");
        if (ctx.getCurrRole() == ChessGame.TeamColor.WHITE){
            drawWhite(server.getGame(ctx.getCurrUser(), ctx.getCurrGame()).game().getBoard());
        }
        else if (ctx.getCurrRole() == ChessGame.TeamColor.BLACK){
            drawBlack(server.getGame(ctx.getCurrUser(), ctx.getCurrGame()).game().getBoard());
        }
        printPrompt();
    }
}

