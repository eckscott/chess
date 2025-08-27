package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalc implements PieceMovesCalc {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currPiece = board.getPiece(myPosition);

        // right
        if (myPosition.getColumn() < MAX_COL){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }

        // up right
        if (myPosition.getColumn() < MAX_COL && myPosition.getRow() < MAX_ROW){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }

        // up
        if (myPosition.getRow() < MAX_ROW){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }

        // up left
        if (myPosition.getColumn() > MIN_COL && myPosition.getRow() < MAX_ROW){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }

        // Left
        if (myPosition.getColumn() > MIN_COL){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }

        // down left
        if (myPosition.getColumn() > MIN_COL && myPosition.getRow() > MIN_ROW){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }

        // down
        if (myPosition.getRow() > MIN_ROW){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else {
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }

        // down right
        if (myPosition.getColumn() < MAX_COL && myPosition.getRow() > MIN_ROW){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
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