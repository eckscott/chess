package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalc implements PieceMovesCalc {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currPiece = board.getPiece(myPosition);

        // shallow left up
        if ((myPosition.getColumn() > MIN_COL + 2) && (myPosition.getRow() < MAX_ROW - 1)){
            System.out.printf("Shallow left up -- entered\n");
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        // tall left up
        if ((myPosition.getColumn() > MIN_COL + 1) && (myPosition.getRow() < MAX_ROW - 2)){
            System.out.printf("Tall left up -- entered\n");
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        // shallow right up
        if ((myPosition.getColumn() < MAX_COL - 2) && (myPosition.getRow() < MAX_ROW - 1)){
            System.out.printf("Shallow right up -- entered\n");
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        // tall right up
        if ((myPosition.getColumn() < MAX_COL - 1) && (myPosition.getRow() < MAX_ROW - 2)){
            System.out.printf("Tall right up -- entered\n");
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }

        // shallow left down
        if ((myPosition.getColumn() > MIN_COL + 2) && (myPosition.getRow() > MIN_ROW + 1)){
            System.out.printf("Shallow left down -- entered\n");
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        // tall left down
        System.out.printf("Get Row returns: %s\n Get Column returns: %s\n", myPosition.getRow(), myPosition.getColumn());
        if ((myPosition.getColumn() > MIN_COL + 1) && (myPosition.getRow() > MIN_ROW + 2)){
            System.out.printf("Tall left down -- entered\n");
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        // shallow right down
        if ((myPosition.getColumn() < MAX_COL - 2) && (myPosition.getRow() > MIN_ROW + 1)){
            System.out.printf("Shallow right down -- entered\n");
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        // tall right down
        if ((myPosition.getColumn() < MAX_COL - 1) && (myPosition.getRow() > MIN_ROW + 2)){
            System.out.printf("Tall right down -- entered\n");
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }

        return moves;
    }
}
