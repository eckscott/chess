package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalc implements PieceMovesCalc {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        //up right
        int r = myPosition.getRow();
        int c = myPosition.getColumn();
        while (true) {
            r++;
            c++;
            if (r > MAX_ROW || r < MIN_ROW || c > MAX_COL || c < MIN_COL)
                break;

            ChessPosition newPosition = new ChessPosition(r, c);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else
                break;
        }

        // down right
        r = myPosition.getRow();
        c = myPosition.getColumn();
        while (true) {
            r--;
            c++;
            if (r > MAX_ROW || r < MIN_ROW || c > MAX_COL || c < MIN_COL)
                break;

            ChessPosition newPosition = new ChessPosition(r, c);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else
                break;
        }

        // down left
        r = myPosition.getRow();
        c = myPosition.getColumn();
        while (true) {
            r--;
            c--;
            if (r > MAX_ROW || r < MIN_ROW || c > MAX_COL || c < MIN_COL)
                break;

            ChessPosition newPosition = new ChessPosition(r, c);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else
                break;
        }

        // up left
        r = myPosition.getRow();
        c = myPosition.getColumn();
        while (true) {
            r++;
            c--;
            if (r > MAX_ROW || r < MIN_ROW || c > MAX_COL || c < MIN_COL)
                break;

            ChessPosition newPosition = new ChessPosition(r, c);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else
                break;
        }
        return moves;
    }

}