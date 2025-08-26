package chess;

import java.util.Collection;

public interface PieceMovesCalc {
    final int MAX_ROW = 8;
    final int MIN_ROW = 1;
    final int MAX_COL = 8;
    final int MIN_COL = 1;


    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
