package ui;

import chess.*;
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
        if (ctx.getCurrRole() == ChessGame.TeamColor.WHITE){
            drawWhite(ctx.getCurrGame().getBoard());
        }
        else if (ctx.getCurrRole() == ChessGame.TeamColor.BLACK){
            drawBoardBlack();
        }


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

    private void drawBoardWhite() {
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "   " + " a " + "  b  "  + " c " + " d " + "  e " + " f " + "  g " + "  h  " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 8 " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY  + EscapeSequences.BLACK_ROOK  +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_KNIGHT + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.BLACK_BISHOP + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_QUEEN +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_KING + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.BLACK_BISHOP + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_KNIGHT +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_ROOK + EscapeSequences.SET_BG_COLOR_WHITE + "   " +
                EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 7 " + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_PAWN +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_PAWN +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY+
                EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 6 " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY  + EscapeSequences.EMPTY  +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_WHITE + "   " +
                EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 5 " + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_WHITE + "   " +
                EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 4 " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY  + EscapeSequences.EMPTY  +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_WHITE + "   " +
                EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 3 " + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_WHITE + "   " +
                EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + " 2 " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY  + EscapeSequences.SET_TEXT_COLOR_WHITE +
                EscapeSequences.WHITE_PAWN  + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY+
                EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY+
                EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY+
                EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_WHITE +
                "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " 1 " + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_ROOK + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.WHITE_KNIGHT + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_BISHOP +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_QUEEN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_KING+
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_BISHOP + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.WHITE_KNIGHT + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_ROOK +
                EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "   " + "   " + "     "  + "   " + "   " + "    " + "   " + "    " + "     " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
    }

    private void drawBoardBlack() {
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "   " + "   " + "     "  + "   " + "   " + "    " + "   " + "    " + "     " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " 1 " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_ROOK + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.WHITE_KNIGHT + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_BISHOP +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_KING + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_QUEEN+
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_BISHOP + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.WHITE_KNIGHT + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_ROOK +
                EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " 2 " + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_PAWN +EscapeSequences.SET_BG_COLOR_LIGHT_GREY+EscapeSequences.WHITE_PAWN+
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_PAWN +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_PAWN +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.WHITE_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_PAWN +
                EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " 3 " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_WHITE + "   " +
                EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " 4 " + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.EMPTY  + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_WHITE + "   " +
                EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " 5 " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_WHITE +
                "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " 6 " + EscapeSequences.SET_BG_COLOR_RED  +
                EscapeSequences.EMPTY  + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_WHITE + "   " +
                EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " 7 " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.BLACK_PAWN + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_PAWN +
                EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " 8 " + EscapeSequences.SET_BG_COLOR_RED +
                EscapeSequences.BLACK_ROOK  + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_KNIGHT +
                EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_BISHOP + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.BLACK_KING + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_QUEEN + EscapeSequences.SET_BG_COLOR_LIGHT_GREY+
                EscapeSequences.BLACK_BISHOP + EscapeSequences.SET_BG_COLOR_RED + EscapeSequences.BLACK_KNIGHT +
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_ROOK + EscapeSequences.SET_BG_COLOR_WHITE +
                "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n" +

                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                        "   " + " h " + "  g  "  + " f " + " e " + "  d " + " c " + "  b " + "  a  " + "  " +
                EscapeSequences.SET_BG_COLOR_BLACK + "\n");
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
                """;
    }

    private void drawWhite(ChessBoard board) {
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK +
                "   " + " a " + "  b  "  + " c " + " d " + "  e " + " f " + "  g " + "  h  " + "  " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");

        for (int i = 8; i >=1; i=i-2){
            System.out.printf(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " %d " +
                    EscapeSequences.SET_BG_COLOR_LIGHT_GREY, i);
            drawPiece(board.getPiece(new ChessPosition(i, 1)));

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            drawPiece(board.getPiece(new ChessPosition(i, 2)));

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            drawPiece(board.getPiece(new ChessPosition(i, 3)));

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            drawPiece(board.getPiece(new ChessPosition(i, 4)));

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            drawPiece(board.getPiece(new ChessPosition(i, 5)));

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            drawPiece(board.getPiece(new ChessPosition(i, 6)));

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            drawPiece(board.getPiece(new ChessPosition(i, 7)));

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            drawPiece(board.getPiece(new ChessPosition(i, 8)));

            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
            System.out.printf(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + " %d " +
                    EscapeSequences.SET_BG_COLOR_RED, i - 1);
            drawPiece(board.getPiece(new ChessPosition(i - 1, 1)));

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            drawPiece(board.getPiece(new ChessPosition(i - 1, 2)));

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            drawPiece(board.getPiece(new ChessPosition(i - 1, 3)));

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            drawPiece(board.getPiece(new ChessPosition(i - 1, 4)));

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            drawPiece(board.getPiece(new ChessPosition(i - 1, 5)));

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            drawPiece(board.getPiece(new ChessPosition(i - 1, 6)));

            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            drawPiece(board.getPiece(new ChessPosition(i - 1, 7)));

            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            drawPiece(board.getPiece(new ChessPosition(i - 1, 8)));

            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.SET_BG_COLOR_BLACK + "\n");
        }

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

}

