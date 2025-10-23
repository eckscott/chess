package dataaccess;

import chess.ChessGame;
import model.*;
import service.UnauthorizedException;

import java.util.Collection;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {

    private final HashMap<String, UserData> users = new HashMap<>();
    private final HashMap<String, String> auth = new HashMap<>();
    private final HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
        auth.clear();
        games.clear();
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
        if (auth.get(authToken) != null) {
            return authToken;
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        if (auth.get(authToken) == null) {
            throw new UnauthorizedException("unauthorized");
        }
        auth.remove(authToken);
    }

    @Override
    public void createGame(GameData gameData){
        games.put(gameData.gameID(), gameData);
    }

    @Override
    public GameData getGame(int gameID){
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    @Override
    public void joinGame(String authToken, JoinGameData joinGameReq) throws Exception {
        if (joinGameReq.playerColor() == ChessGame.TeamColor.WHITE){
            if (games.containsKey(joinGameReq.gameID())) {
                if (games.get(joinGameReq.gameID()).whiteUsername() != null) {
                    throw new Exception("already taken");
                }
                var newGame = new GameData(joinGameReq.gameID(), auth.get(authToken),
                        games.get(joinGameReq.gameID()).blackUsername(),
                        games.get(joinGameReq.gameID()).gameName(),
                        games.get(joinGameReq.gameID()).game());
                games.put(joinGameReq.gameID(), newGame);
            } else {
                throw new DataAccessException("game does not exist");
            }
        }
        if (joinGameReq.playerColor() == ChessGame.TeamColor.BLACK){
            if (games.containsKey(joinGameReq.gameID())) {
                if (games.get(joinGameReq.gameID()).blackUsername() != null) {
                    throw new Exception("already taken");
                }
                var newGame = new GameData(joinGameReq.gameID(), games.get(joinGameReq.gameID()).whiteUsername(),
                        auth.get(authToken),
                        games.get(joinGameReq.gameID()).gameName(),
                        games.get(joinGameReq.gameID()).game());
                games.put(joinGameReq.gameID(), newGame);
            } else {
                throw new Exception("game does not exist");
            }
        }
    }
}
