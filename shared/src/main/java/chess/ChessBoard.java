package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable{

    ChessPiece[][] board = new ChessPiece[8][8];
    final byte BOARD_TO_ARRAY = 1;

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - BOARD_TO_ARRAY]
             [position.getColumn() - BOARD_TO_ARRAY] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - BOARD_TO_ARRAY]
                    [position.getColumn() - BOARD_TO_ARRAY];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // white team
        board[1 - BOARD_TO_ARRAY]
             [1 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board[1 - BOARD_TO_ARRAY]
             [2 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[1 - BOARD_TO_ARRAY]
             [3 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[1 - BOARD_TO_ARRAY]
             [4 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board[1 - BOARD_TO_ARRAY]
             [5 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board[1 - BOARD_TO_ARRAY]
             [6 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[1 - BOARD_TO_ARRAY]
             [7 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[1 - BOARD_TO_ARRAY]
             [8 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < 8; i++) {
            board[2 - BOARD_TO_ARRAY][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        // black team
        board[8 - BOARD_TO_ARRAY]
             [1 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        board[8 - BOARD_TO_ARRAY]
             [2 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board[8 - BOARD_TO_ARRAY]
             [3 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board[8 - BOARD_TO_ARRAY]
             [4 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        board[8 - BOARD_TO_ARRAY]
             [5 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        board[8 - BOARD_TO_ARRAY]
             [6 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board[8 - BOARD_TO_ARRAY]
             [7 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board[8 - BOARD_TO_ARRAY]
             [8 - BOARD_TO_ARRAY] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < 8; i++) {
            board[7 - BOARD_TO_ARRAY][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }

    @Override
    public String toString() {
        String boardToString = "";
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (board[i][j] != null){
                    ChessPosition pos = new ChessPosition(i + 1, j + 1);
                    boardToString += String.format("%s = [%s]\n", board[i][j], pos);
                }
            }
        }
        return boardToString;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessBoard that)) {
            return false;
        }
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    public ChessBoard copy() {
        ChessBoard cloneBoard = new ChessBoard();
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition pos = new ChessPosition(i, j);
                cloneBoard.addPiece(pos, getPiece(pos));
            }
        }
        return cloneBoard;
    }
}
