package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessRules extends ChessGame{

    public ChessRules(ChessBoard board){
        this.board = board;
    }
    /**
     * Get the King's position
     * @return the position
     * @param teamColor the team
     */
    public ChessPosition getKing(TeamColor teamColor){
        for (int r = 1; r <= 8; r++){
            for (int c = 1; c <= 8; c++){
                ChessPosition kingPos = new ChessPosition(r, c);
                if (board.getPiece(kingPos) != null &&
                        board.getPiece(kingPos).getPieceType() == ChessPiece.PieceType.KING &&
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
     * Blocker help function
     * @param startPosition start position of attacking piece
     * @param endPosition end position of
     * @return collection of block positions
     */
    public Collection<ChessPosition> blockPositions(ChessPosition startPosition, ChessPosition endPosition){

        Collection<ChessPosition> blockPositions = new ArrayList<>();
        int rDiff = endPosition.getRow() - startPosition.getRow();
        int cDiff = endPosition.getColumn() - startPosition.getColumn();

        // up move
        if (rDiff > 0 && cDiff == 0){
            for (int i = 1; i <= rDiff; i++){
                ChessPosition blockPos = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn());
                blockPositions.add(blockPos);
            }
        }
        // down move
        if (rDiff < 0 && cDiff == 0){
            for (int i = -1; i >= rDiff; i--){
                ChessPosition blockPos = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn());
                blockPositions.add(blockPos);
            }
        }
        // left move
        if (cDiff < 0 && rDiff == 0){
            for (int i = -1; i >= cDiff; i--){
                ChessPosition blockPos = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + i);
                blockPositions.add(blockPos);
            }
        }
        // right move
        if (cDiff > 0 && rDiff == 0){
            for (int i = 1; i <= cDiff; i++){
                ChessPosition blockPos = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + i);
                blockPositions.add(blockPos);
            }
        }
        // up right
        if (rDiff > 0 && cDiff > 0){
            for (int i = 1; i <= cDiff; i++){
                ChessPosition blockPos = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() + i);
                blockPositions.add(blockPos);
            }
        }
        // up left
        if (rDiff > 0 && cDiff < 0){
            for (int i = 1; i <= rDiff; i++){
                int j = i * -1;
                ChessPosition blockPos = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() + j);
                blockPositions.add(blockPos);
            }
        }
        // down left
        if (rDiff < 0 && cDiff < 0){
            for (int i = -1; i >= rDiff; i--){
                ChessPosition blockPos = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() + i);
                blockPositions.add(blockPos);
            }
        }
        // down right
        if (rDiff < 0 && cDiff > 0){
            for (int j = 1; j <= cDiff; j++){
                int i = j * -1;
                ChessPosition blockPos = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() + j);
                blockPositions.add(blockPos);
            }
        }

        return blockPositions;
    }
    /**
     * Boolean to see if teamMoves can Block
     * @param teamMoves teammates moves
     * @param blockPositions the positions the teammate has to land in to block
     * @return true or false
     */
    public boolean canBlock(Collection<ChessMove> teamMoves, Collection<ChessPosition> blockPositions){
        for(ChessMove move : teamMoves){
            if (blockPositions.contains(move.getEndPosition())) return true;
        }
        return false;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE){
            Collection<ChessMove> teamMoves = teamMoves(teamColor);
            Collection<ChessMove> enemyMoves = teamMoves(TeamColor.BLACK);
            ChessPiece king = board.getPiece(getKing(teamColor));
            Collection<ChessMove> kingMoves = king.pieceMoves(board, getKing(teamColor));
            if (!endPositions(enemyMoves).contains(getKing(teamColor)) &&
                endPositions(enemyMoves).containsAll(endPositions(kingMoves)) &&
                teamMoves == kingMoves)
                return true;
        }
        if (teamColor == TeamColor.BLACK){
            Collection<ChessMove> teamMoves = teamMoves(teamColor);
            Collection<ChessMove> enemyMoves = teamMoves(TeamColor.WHITE);
            ChessPiece king = board.getPiece(getKing(teamColor));
            Collection<ChessMove> kingMoves = king.pieceMoves(board, getKing(teamColor));
            if (!endPositions(enemyMoves).contains(getKing(teamColor)) &&
                    endPositions(enemyMoves).containsAll(endPositions(kingMoves)) &&
                    teamMoves == kingMoves)
                return true;
        }
        return false;
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition enemyPos = new ChessPosition(r, c);
                if (board.getPiece(enemyPos) != null && board.getPiece(enemyPos).getTeamColor() != teamColor) {
                    ChessPiece piece = board.getPiece(enemyPos);
                    Collection<ChessMove> enemyPieceMoves = piece.pieceMoves(board, enemyPos);
                    if (endPositions(enemyPieceMoves).contains(getKing(teamColor)))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor){
        try {
            if (isInCheck(teamColor) && !escapeCheck(teamColor, teamMoves(teamColor))) return true;
        } catch (InvalidMoveException | CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Checks into the future if I can make a move. Used to differentiate between check and checkmate
     * @param teamColor team checking if in check
     * @param teamMoves moves of this team
     * @return true if making the move no longer puts you in check, false if it keeps you in check
     * @throws InvalidMoveException
     */
    public boolean escapeCheck(TeamColor teamColor, Collection<ChessMove> teamMoves) throws InvalidMoveException, CloneNotSupportedException {
        ChessBoard copyBoard = board.clone();
        for (ChessMove move : teamMoves){
            board = copyBoard.clone();
            makeMove(move);
            if (!isInCheck(teamColor)) return true;
        }
        return false;
    }

}
