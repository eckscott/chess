package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.*;

import java.util.UUID;

public class UserService {


    private final MemoryDataAccess dataAccess;

    public UserService(MemoryDataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData user) throws Exception {
        if (dataAccess.getUser(user.username()) != null)
            throw new Exception("already exists");
        if (user.password() == null || user.username() == null || user.email() == null)
            throw new Exception("Not enough fields");

        dataAccess.createUser(user);

        return new AuthData(user.username(), generateAuthToken());
    }

    public AuthData login(UserData loginCreds) throws Exception{
        if (dataAccess.getUser(loginCreds.username()) == null)
            throw new Exception("User does not exist");

        String authToken = generateAuthToken();
        dataAccess.createAuth(new AuthData(loginCreds.username(), authToken));
        return new AuthData(loginCreds.username(), authToken);
    }

    public void logout(AuthData logoutRequest) throws Exception{
        if (logoutRequest.authToken() == null)
            throw new Exception("Not authorized");

        dataAccess.deleteAuth(logoutRequest);
    }

    private String generateAuthToken(){
        return UUID.randomUUID().toString();
    }
}
