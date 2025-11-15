package service;

import Exceptions.BadRequestException;
import Exceptions.UnauthorizedException;
import dataaccess.DataAccessException;
import dataaccess.SqlDataAccess;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {


    //private final MemoryDataAccess dataAccess;
    private final SqlDataAccess dataAccess;

    public UserService(SqlDataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public void clear() throws DataAccessException {
        dataAccess.clear();
    }

    public AuthData register(UserData user) throws Exception {
        if (user.password() == null || user.username() == null || user.email() == null) {
            throw new BadRequestException("bad request");
        }
        if (dataAccess.getUser(user.username()) != null) {
            throw new Exception("already taken");
        }
        try{
            String hashPass = BCrypt.hashpw(user.password(), BCrypt.gensalt());
            var storedUser = new UserData(user.username(), user.email(), hashPass);
            dataAccess.createUser(storedUser);
            var auth = new AuthData(user.username(), generateAuthToken());
            dataAccess.createAuth(auth);

            return auth;
        } catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData login(UserData loginCreds) throws Exception {
        try{
            dataAccess.getUser(loginCreds.username());
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

        if (loginCreds.username() == null || loginCreds.password() == null) {
            throw new BadRequestException("bad request");
        }
        if (dataAccess.getUser(loginCreds.username()) == null ||
            dataAccess.getUser(loginCreds.username()).password() == null ||
            !verifyPassword(loginCreds.username(), loginCreds.password())) {
            throw new UnauthorizedException("unauthorized");
        }

        String authToken = generateAuthToken();
        dataAccess.createAuth(new AuthData(loginCreds.username(), authToken));
        return new AuthData(loginCreds.username(), authToken);
    }

    public void logout(String authToken) throws UnauthorizedException, DataAccessException {
        dataAccess.deleteAuth(authToken);
    }

    private String generateAuthToken(){
        return UUID.randomUUID().toString();
    }

    boolean verifyPassword(String username, String providedClearTextPass) throws DataAccessException {
        var storedHashedPass = dataAccess.getUser(username).password();
        return BCrypt.checkpw(providedClearTextPass, storedHashedPass);
    }
}
