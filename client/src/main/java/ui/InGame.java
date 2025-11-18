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
            drawBlack(ctx.getCurrGame().getBoard());
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


    public String eval(String input) throws Exception {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd){
            case "quit" -> "quit";
            case "leave" -> {
                ctx.setCurrState(States.SIGNEDIN);
                yield "You've exited the game! ";
            }
            case "redrawchessboard" -> {
                if (ctx.getCurrRole() == ChessGame.TeamColor.WHITE){
                    drawWhite(ctx.getCurrGame().getBoard());
                }
                else if (ctx.getCurrRole() == ChessGame.TeamColor.BLACK){
                    drawBlack(ctx.getCurrGame().getBoard());
                }
                yield "Current board ^^^\n";
            }
            default -> help();
        };
    }

    public String help() {
        return """
                help - lists command options
                RedrawChessBoard - Redraws the chess board
                Leave - Leave the game without resigning
                MakeMove <[start, position]>, <[end, position]> - Moves a piece from start position to end position
                Resign - Forfeit the game and the game is over
                HighlightLegalMoves <[start, position]> - highlight legal moves for the piece at the indicated position
                quit - exits the program
                """;
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

}

