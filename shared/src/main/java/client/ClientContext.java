package client;

import model.*;

public class ClientContext {

    private AuthData currUser;
    private States currState;

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
}
