package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard board = new ChessBoard();
    TeamColor teamTurn;

    public ChessGame() {
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    public ChessGame(ChessBoard board){
        this.board = board;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        ChessGame.TeamColor team = board.getPiece(startPosition).getTeamColor();
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> removeMoves = new ArrayList<>();
        try{
            ChessBoard copyBoard = board.clone();
            for (ChessMove illegalMove : moves){
                board = copyBoard.clone();
                forceMove(illegalMove);
                if(isInCheck(team))
                    removeMoves.add(illegalMove);
            }
            board = copyBoard.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        moves.removeAll(removeMoves);
        return moves;
    }

    public void forceMove(ChessMove move){
        if (move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
            board.addPiece(move.getStartPosition(), null);
        }
        else {
            board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(),
                    move.getPromotionPiece()));
            board.addPiece(move.getStartPosition(), null);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()) == null)
            throw new InvalidMoveException("Nothing here");
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves.contains(move) && (teamTurn == board.getPiece(move.getStartPosition()).getTeamColor())){
            forceMove(move);
            teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        }
        else
            throw new InvalidMoveException("Invalid Move!");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition enemyPos = new ChessPosition(r, c);
                if (board.getPiece(enemyPos) != null && board.getPiece(enemyPos).getTeamColor() != teamColor) {
                    ChessPiece piece = board.getPiece(enemyPos);
                    Collection<ChessMove> enemyPieceMoves = piece.pieceMoves(board, enemyPos);
                    ChessPosition king = finder.find(teamColor, ChessPiece.PieceType.KING);
                    if (endPositions(enemyPieceMoves).contains(king))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        try {
            if (isInCheck(teamColor) && !escapeCheck(teamColor, teamMoves(teamColor))) return true;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> teamMoves = teamMoves(teamColor);
        try {
            return !isInCheck(teamColor) && !escapeCheck(teamColor, teamMoves);
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks into the future if I can make a move. Used to differentiate between check and checkmate
     * @param teamColor team checking if in check
     * @param teamMoves moves of this team
     * @return true if making the move no longer puts you in check, false if it keeps you in check
     */
    public boolean escapeCheck(TeamColor teamColor, Collection<ChessMove> teamMoves) throws CloneNotSupportedException {
        ChessBoard copyBoard = board.clone();
        for (ChessMove move : teamMoves){
            board = copyBoard.clone();
            forceMove(move);
            if (!isInCheck(teamColor)) return true;
        }
        return false;
    }

    /**
     * Return end positions of each ChessMove
     * @param teamMoves the teamsMoves
     * @return Collection<ChessPosition> endPositions
     */
    public Collection<ChessPosition> endPositions(Collection<ChessMove> teamMoves){
        Collection<ChessPosition> endPositions = new ArrayList<>();
        for (ChessMove move : teamMoves)
            endPositions.add(move.getEndPosition());
        return endPositions;
    }

    /**
     * parse through each cell on the board collecting the moves of each player with
     * teamColor
     * @return a collection of ChessMoves
     * @param teamColor the team
     */
    public Collection<ChessMove> teamMoves(TeamColor teamColor){
        Collection<ChessMove> teamMoves = new ArrayList<>();
        for(int r = 1; r <= 8; r++){
            for(int c = 1; c <= 8; c++){
                ChessPosition pos = new ChessPosition(r, c);
                if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == teamColor){
                    ChessPiece piece = board.getPiece(pos);
                    teamMoves.addAll(piece.pieceMoves(board, pos));
                }
            }
        }
        return teamMoves;
    }

    /**
     * Find any piece on the board
     *
     * @param teamColor
     * @param piece
     */
    PieceFinder finder = (teamColor, pieceType) -> {
        for (int r = 1; r <= 8; r++){
            for (int c = 1; c <= 8; c++){
                ChessPosition piecePos = new ChessPosition(r, c);
                if (board.getPiece(piecePos) != null &&
                    board.getPiece(piecePos).getPieceType() == pieceType &&
                    board.getPiece(piecePos).getTeamColor() == teamColor)
                    return piecePos;
            }
        }
        return null;
    };

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessGame chessGame)) {
            return false;
        }
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }
}
