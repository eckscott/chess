package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.*;

import java.util.UUID;

public class UserService {


    private final MemoryDataAccess dataAccess;

    public UserService(MemoryDataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public void clear(){
        dataAccess.clear();
    }

    public AuthData register(UserData user) throws Exception {
        if (user.password() == null || user.username() == null || user.email() == null)
            throw new BadRequestException("bad request");
        if (dataAccess.getUser(user.username()) != null)
            throw new Exception("already taken");

        dataAccess.createUser(user);
        var auth = new AuthData(user.username(), generateAuthToken());
        dataAccess.createAuth(auth);

        return auth;
    }

    public AuthData login(UserData loginCreds) throws Exception {
        if (loginCreds.username() == null || loginCreds.password() == null)
            throw new BadRequestException("bad request");
        if (dataAccess.getUser(loginCreds.username()) == null ||
            dataAccess.getUser(loginCreds.username()).password() == null ||
            !dataAccess.getUser(loginCreds.username()).password().equals(loginCreds.password()))
            throw new UnauthorizedException("unauthorized");

        String authToken = generateAuthToken();
        dataAccess.createAuth(new AuthData(loginCreds.username(), authToken));
        return new AuthData(loginCreds.username(), authToken);
    }

    public void logout(String authToken) {
        dataAccess.deleteAuth(authToken);
    }

    private String generateAuthToken(){
        return UUID.randomUUID().toString();
    }
}
