package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.*;

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

        return new AuthData(loginCreds.username(), generateAuthToken());
    }

    private String generateAuthToken(){
        return "xyz";
    }
}
