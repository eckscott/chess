package dataaccess;

import model.*;

import java.util.Collection;

public interface DataAccess {
    void clear();
    void createUser(UserData user);
    UserData getUser(String username);
    void createAuth(AuthData authorization);
    String getAuth(String authToken);
    void deleteAuth(String authToken) throws DataAccessException;
    void createGame(GameData gameData);
    GameData getGame(int gameID);
    Collection<GameData> listGames();
    void joinGame(String authToken, JoinGameData joinGameReq) throws DataAccessException;
}
