package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalc implements PieceMovesCalc {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currPiece = board.getPiece(myPosition);

        // White Team
        if (currPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            // Check Block
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if (board.getPiece(newPosition) == null){
                // start
                if (myPosition.getRow() == MIN_ROW + 1){
                    ChessPosition startMovePos = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                    if (board.getPiece(startMovePos) == null)
                        moves.add(new ChessMove(myPosition, startMovePos, null));
                }
                // promotion
                if (newPosition.getRow() == MAX_ROW){
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));

                }
                else
                    moves.add(new ChessMove(myPosition, newPosition, null));
            }
            // Take opponent piece
            if (myPosition.getColumn() < MAX_COL){
                ChessPosition enemyPiecePos1 = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                if (board.getPiece(enemyPiecePos1) != null){
                    ChessPiece enemyPiece1 = board.getPiece(enemyPiecePos1);
                    if (currPiece.getTeamColor() != enemyPiece1.getTeamColor())
                        moves.add(new ChessMove(myPosition, enemyPiecePos1, null));
                }
            }
            if (myPosition.getColumn() > MIN_COL){
                ChessPosition enemyPiecePos2 = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                if (board.getPiece(enemyPiecePos2) != null){
                    ChessPiece enemyPiece2 = board.getPiece(enemyPiecePos2);
                    if (currPiece.getTeamColor() != enemyPiece2.getTeamColor())
                        moves.add(new ChessMove(myPosition, enemyPiecePos2, null));
                }
            } 
        }

        // Black Team
        else {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if (board.getPiece(newPosition) == null){
                //start
                if (myPosition.getRow() == MAX_ROW - 1){
                    ChessPosition startMovePos = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                    if (board.getPiece(startMovePos) == null)
                        moves.add(new ChessMove(myPosition, startMovePos, null));
                }
                // promotion
                if (newPosition.getRow() == MIN_ROW){

                }
                else
                    moves.add(new ChessMove(myPosition, newPosition, null));
            }
            // Take opponent piece
            if (myPosition.getColumn() < MAX_COL){
                ChessPosition enemyPiecePos1 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                if (board.getPiece(enemyPiecePos1) != null){
                    ChessPiece enemyPiece1 = board.getPiece(enemyPiecePos1);
                    if (currPiece.getTeamColor() != enemyPiece1.getTeamColor())
                        moves.add(new ChessMove(myPosition, enemyPiecePos1, null));
                }
            }
            if (myPosition.getColumn() > MIN_COL){
                ChessPosition enemyPiecePos2 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                if (board.getPiece(enemyPiecePos2) != null){
                    ChessPiece enemyPiece2 = board.getPiece(enemyPiecePos2);
                    if (currPiece.getTeamColor() != enemyPiece2.getTeamColor())
                        moves.add(new ChessMove(myPosition, enemyPiecePos2, null));
                }
            }
        }
        return moves;
    }
}
