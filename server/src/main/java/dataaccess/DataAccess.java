package dataaccess;

import datamodel.*;

import java.util.Collection;

public interface DataAccess {
    void clear();
    void createUser(UserData user);
    UserData getUser(String username);
    void createAuth(AuthData authorization);
    String getAuth(String authToken);
    void deleteAuth(String authToken) throws DataAccessException;
    Collection<GameData> listGames();
}
