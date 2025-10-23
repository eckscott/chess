package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalc implements PieceMovesCalc {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currPiece = board.getPiece(myPosition);

        int[] directions = {-1, 1};
        for(int i = 0; i < directions.length; i++){
            for(int j = 0; j < directions.length; j++){
                int r = myPosition.getRow();
                int c = myPosition.getColumn();
                while (true){
                    r = r + directions[i];
                    c = c + directions[j];

                    if (r > 8 || r < 1 || c > 8 || c < 1) {
                        break;
                    }

                    ChessPosition newPosition = new ChessPosition(r, c);
                    if (board.getPiece(newPosition) == null) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    else if (board.getPiece(newPosition).getTeamColor() != currPiece.getTeamColor()){
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