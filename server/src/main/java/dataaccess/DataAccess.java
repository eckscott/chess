package dataaccess;

import model.*;

import java.util.Collection;

public interface DataAccess {
    void clear() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void createAuth(AuthData authorization) throws DataAccessException;
    String getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws Exception;
    void createGame(GameData gameData) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void joinGame(String authToken, JoinGameData joinGameReq) throws Exception;
    void removePlayer(String authToken, GameData game);
}
