package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalc implements PieceMovesCalc {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int newRow = myPosition.getRow();
        int newCol = myPosition.getColumn();

        for(int i = 1; i < 8; i++){
            if (myPosition.getRow() < 8 && myPosition.getColumn() < 8){ 
                newRow = myPosition.getRow() + 1;
                newCol = myPosition.getColumn() + 1;
            }
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            moves.add(new ChessMove(myPosition, newPosition, null));
        }    
          
        return moves;
    }

}