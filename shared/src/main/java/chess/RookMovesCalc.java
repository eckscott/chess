package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalc implements PieceMovesCalc{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currPiece = board.getPiece(myPosition);

        // up
        int r = myPosition.getRow();
        int c = myPosition.getColumn();
        while (true) { 
            r++;
            if (r > MAX_ROW)
                break;
            
            ChessPosition newPosition = new ChessPosition(r, c);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else{
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
                else
                    break;
            }
        }
        // down
        r = myPosition.getRow();
        while (true) { 
            r--;
            if (r < MIN_ROW)
                break;
            
            ChessPosition newPosition = new ChessPosition(r, c);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else{
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
                else
                    break;
            }
        }
        // left
        r = myPosition.getRow();
        while (true) { 
            c--;
            if (c < MIN_COL)
                break;
            
            ChessPosition newPosition = new ChessPosition(r, c);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else{
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
                else
                    break;
            }
        }
        //right
        c = myPosition.getColumn();
        while (true) { 
            c++;
            if (c > MAX_COL)
                break;
            
            ChessPosition newPosition = new ChessPosition(r, c);
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else{
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
                else
                    break;
            }
        }
        return moves;
    }
}
