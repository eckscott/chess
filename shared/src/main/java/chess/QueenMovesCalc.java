package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalc implements PieceMovesCalc {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currPiece = board.getPiece(myPosition);

        int[] rowDirections = {1, 1, 1, 0, -1, -1, -1, 0};
        int[] colDirections = {1, 0, -1, -1, -1, 0, 1, 1};
        for(int i = 0; i < rowDirections.length; i++){
            int r = myPosition.getRow();
            int c = myPosition.getColumn();
            while (true) {
                r = r + rowDirections[i];
                c = c + colDirections[i];

                if (r > MAX_ROW || r < MIN_ROW || c > MAX_COL || c < MIN_COL) {
                    break;
                }

                ChessPosition newPosition = new ChessPosition(r, c);
                if (board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
                else{
                    ChessPiece newPositionPiece = board.getPiece(newPosition);
                    if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                        moves.add(new ChessMove(myPosition, newPosition, null));
                        break;
                    }
                    else {
                        break;
                    }
                }
            }
        }
        return moves;
    }
}
