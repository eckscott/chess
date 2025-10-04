package chess;

@FunctionalInterface
public interface PieceFinder {
    ChessPosition find(ChessGame.TeamColor teamColor, ChessPiece.PieceType pieceType);
}
