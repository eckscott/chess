package dataaccess;

import datamodel.AuthData;
import datamodel.UserData;

import javax.xml.crypto.Data;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {

    private final HashMap<String, UserData> users = new HashMap<>();
    private final HashMap<String, String> auth = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
        auth.clear();
    }

    @Override
    public void createUser(UserData user) {
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }


    @Override
    public void createAuth(AuthData authorization){
        auth.put(authorization.authToken(), authorization.username());
    }

    @Override
    public String getAuth(String authToken){
        if (auth.get(authToken) != null)
            return authToken;
        return null;
    }

    @Override
    public void deleteAuth(AuthData authorization) throws DataAccessException{
        if (auth.get(authorization.authToken()) == null)
            throw new DataAccessException("Error: unauthorized");
        auth.remove(authorization.authToken());
    }
}
