package client;

import chess.ChessGame;
import model.*;

public class ClientContext {

    private AuthData currUser;
    private States currState;
    private ChessGame.TeamColor currRole;
    private int currGame;

    public AuthData getCurrUser() {
        return currUser;
    }

    public void setCurrUser(AuthData currUser){
        this.currUser = currUser;
    }

    public States getCurrState() {
        return currState;
    }

    public void setCurrState(States newState){
        currState = newState;
    }

    public ChessGame.TeamColor getCurrRole() {
        return currRole;
    }

    public void setCurrRole(ChessGame.TeamColor teamColor){
        currRole = teamColor;
    }

    public int getCurrGame() {return currGame;}

    public void setCurrGame(int gameID){
        currGame = gameID;
    }
}
