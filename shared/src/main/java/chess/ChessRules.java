package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class ChessRules extends ChessGame{

    /**
     * Get the King's position
     * @return the position
     * @param teamColor the team
     */
    public ChessPosition getKing(TeamColor teamColor){
        for (int r = 1; r <= 8; r++){
            for (int c = 1; c <= 8; c++){
                ChessPosition kingPos = new ChessPosition(r, c);
                if (board.getPiece(kingPos).getPieceType() == ChessPiece.PieceType.KING &&
                        board.getPiece(kingPos).getTeamColor() == teamColor)
                    return kingPos;
            }
        }
        // out of bounds but needs a return statement
        return new ChessPosition(0, 0);
    }

    /**
     * Get Team Moves
     * @return a collection of ChessMoves
     * @param teamColor the team
     */
    public Collection<ChessMove> teamMoves(TeamColor teamColor){
        // parse through each cell on the board collecting the moves of each player with
        // teamColor
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
     * Return end positions of each ChessMove
     * @param teamMoves
     * @return Collection<ChessPosition> endPositions
     */
    public Collection<ChessPosition> endPositions(Collection<ChessMove> teamMoves){
        Collection<ChessPosition> endPositions = new ArrayList<>();
        for (ChessMove move : teamMoves)
            endPositions.add(move.getEndPosition());
        return endPositions;
    }

    /**
     * Blocker help function
     * @param startPosition
     * @param endPosition
     * @return collection of block positions
     */
    public Collection<ChessPosition> blockPositions(ChessPosition startPosition, ChessPosition endPosition){

        Collection<ChessPosition> blockPositions = new ArrayList<>();
        int rDiff = endPosition.getRow() - startPosition.getRow();
        int cDiff = endPosition.getColumn() - startPosition.getColumn();

        // absolute difference
        if (rDiff < 0) rDiff = rDiff * (-1);
        if (cDiff < 0) cDiff = cDiff * (-1);

    }
    /**
     * Check if teamColor is in check
     * @param teamColor
     * @param teamMoves
     * @return String with game status
     */

    public String teamStatus(TeamColor teamColor, Collection<ChessMove> teamMoves){
        for (int r = 1; r <= 8; r++){
            for (int c = 1; c <= 8; c++){
                ChessPosition enemyPos = new ChessPosition(r, c);
                // if there is an enemy piece at enemyPos
                if (board.getPiece(enemyPos) != null && board.getPiece(enemyPos).getTeamColor() == teamColor){
                    ChessPiece piece = board.getPiece(enemyPos);
                    // all the possible moves for this one piece
                    Collection<ChessMove> enemyPieceMoves = piece.pieceMoves(board, enemyPos);
                    // if this piece can move to the king we are in check
                    if (endPositions(enemyPieceMoves).contains(getKing(teamColor))){
                        /*
                        3 conditions to differentiate check and checkmate
                        1. enemy piece can be taken
                        2. enemy move can be blocked
                        3. king can move
                         */
                        ChessPiece king = board.getPiece(getKing(teamColor));
                        Collection<ChessMove> kingMoves = king.pieceMoves(board, getKing(teamColor));
                        if (!endPositions(teamMoves).contains(enemyPos) ||

                            enemyPieceMoves.containsAll(kingMoves))
                        return "check";
                    }
                }
            }
        }

        return "nothing";
    }

}
