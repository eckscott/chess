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
     * Check if enemy moves have an endPosition on the King Position (check)
     * @param  enemyMoves
     * @param kingPosition
     */
    public boolean check(Collection<ChessMove> enemyMoves, ChessPosition kingPosition){
        Collection<ChessPosition> endPositions = endPositions(enemyMoves);
        if (endPositions.contains(kingPosition)) return true;
        return false;
    }

    /**
     * Get Game Status
     * @return a string indicating the status of the game
     * e.g
     * "check-white", "checkmate-white", "stalemate-white", or "nothing"
     * Same for black
     */
    public String gameStatus(){
        Collection<ChessMove> whiteMoves = teamMoves(WHITE);
        Collection<ChessMove> blackMoves = teamMoves(BLACK);

        // check for white trouble first
        if (teamTurn == WHITE){
            if (check(blackMoves, getKing(WHITE))){

            }
        }
    }
}
