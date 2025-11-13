package client;

import model.*;

public class ClientContext {

    private AuthData currUser;

    public AuthData getCurrUser() {
        return currUser;
    }

    public void setCurrUser(AuthData currUser){
        this.currUser = currUser;
    }
}
