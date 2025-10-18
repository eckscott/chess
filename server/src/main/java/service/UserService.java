package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import datamodel.*;

import java.util.UUID;

public class UserService {


    private final MemoryDataAccess dataAccess;

    public UserService(MemoryDataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData user) throws Exception {
        if (user.password() == null || user.username() == null || user.email() == null)
            throw new BadRequestException("Error: bad request");
        if (dataAccess.getUser(user.username()) != null)
            throw new Exception("Error: already taken");

        dataAccess.createUser(user);
        var auth = new AuthData(user.username(), generateAuthToken());
        dataAccess.createAuth(auth);

        return auth;
    }

    public AuthData login(UserData loginCreds) throws Exception {
        if (loginCreds.username() == null || loginCreds.password() == null)
            throw new BadRequestException("Error: bad request");
        if (dataAccess.getUser(loginCreds.username()) == null ||
            dataAccess.getUser(loginCreds.username()).password() == null ||
            !dataAccess.getUser(loginCreds.username()).password().equals(loginCreds.password()))
            throw new UnauthorizedException("Error: unauthorized");

        String authToken = generateAuthToken();
        dataAccess.createAuth(new AuthData(loginCreds.username(), authToken));
        return new AuthData(loginCreds.username(), authToken);
    }

    public void logout(String authToken){
        try {
            dataAccess.deleteAuth(authToken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateAuthToken(){
        return UUID.randomUUID().toString();
    }
}
