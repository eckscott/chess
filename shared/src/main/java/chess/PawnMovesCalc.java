package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalc implements PieceMovesCalc {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currPiece = board.getPiece(myPosition);

        /*
        ////////   WHITE ///////////
         */
        if (currPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            // standard
            if (board.getPiece(newPosition) == null){
                if (newPosition.getRow() == 8){
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
                }
                else {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
            // special start
            if (myPosition.getRow() == 2){
                ChessPosition newStartPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                if (board.getPiece(newPosition) == null && board.getPiece(newStartPosition) == null) {
                    moves.add(new ChessMove(myPosition, newStartPosition, null));
                }
            }
            // take pieces
            if (myPosition.getColumn() + 1 < 9){
                ChessPosition enemyPos1 = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                if (board.getPiece(enemyPos1) != null && board.getPiece(enemyPos1).getTeamColor() != ChessGame.TeamColor.WHITE){
                    if (enemyPos1.getRow() == 8){
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.QUEEN));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, enemyPos1, null));
                    }
                }
            }
            if (myPosition.getColumn() - 1 > 0){
                ChessPosition enemyPos1 = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                if (board.getPiece(enemyPos1) != null && board.getPiece(enemyPos1).getTeamColor() != ChessGame.TeamColor.WHITE){
                    if (enemyPos1.getRow() == 8){
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.QUEEN));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, enemyPos1, null));
                    }
                }
            }
        }

        /*
        ///////// BLACK ///////////
         */
        if (currPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            // standard
            if (board.getPiece(newPosition) == null){
                if (newPosition.getRow() == 1){
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
                }
                else {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
            // special start
            if (myPosition.getRow() == 7){
                ChessPosition newStartPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                if (board.getPiece(newPosition) == null && board.getPiece(newStartPosition) == null) {
                    moves.add(new ChessMove(myPosition, newStartPosition, null));
                }
            }
            // take pieces
            if (myPosition.getColumn() + 1 < 9){
                ChessPosition enemyPos1 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                if (board.getPiece(enemyPos1) != null && board.getPiece(enemyPos1).getTeamColor() != ChessGame.TeamColor.BLACK){
                    if (enemyPos1.getRow() == 1){
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.QUEEN));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, enemyPos1, null));
                    }
                }
            }
            if (myPosition.getColumn() - 1 > 0){
                ChessPosition enemyPos1 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                if (board.getPiece(enemyPos1) != null && board.getPiece(enemyPos1).getTeamColor() != ChessGame.TeamColor.BLACK){
                    if (enemyPos1.getRow() == 1){
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, enemyPos1, ChessPiece.PieceType.QUEEN));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, enemyPos1, null));
                    }
                }
            }
        }
        return moves;
    }
}