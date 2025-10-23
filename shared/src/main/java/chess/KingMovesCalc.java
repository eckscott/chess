package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalc implements PieceMovesCalc {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currPiece = board.getPiece(myPosition);

        int[] rowDirections = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] colDirections = {1, 1, 0, -1, -1, -1, 0, 1};
        for(int i = 0; i < rowDirections.length; i++){
            // initial checks to not go out of bounds
            if (myPosition.getRow() + rowDirections[i] <= MAX_ROW &&
                myPosition.getRow() + rowDirections[i] >= MIN_ROW &&
                myPosition.getColumn() + colDirections[i] <= MAX_COL &&
                myPosition.getColumn() + colDirections[i] >= MIN_COL){
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + rowDirections[i],
                                                              myPosition.getColumn() + colDirections[i]);

                if (board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
                else {
                    ChessPiece newPositionPiece = board.getPiece(newPosition);
                    if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
        }
        return moves;
    }

}