package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import exceptions.UnauthorizedException;

public class SqlDataAccess implements DataAccess{

    private static final SqlHelperMethods HELPER_METHODS = new SqlHelperMethods();

    public SqlDataAccess() throws DataAccessException{
        HELPER_METHODS.configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "DROP table users";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
            var statement2 = "DROP table auth";
            try (PreparedStatement ps = conn.prepareStatement(statement2)){
                ps.executeUpdate();
            }
            var statement3 = "DROP table games";
            try (PreparedStatement ps = conn.prepareStatement(statement3)){
                ps.executeUpdate();
            }
            HELPER_METHODS.configureDatabase();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(String.format("Unable to clear database: %s", e.getMessage()));
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        try {
            var statement = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            HELPER_METHODS.executeUpdate(statement, user.username(), user.email(), user.password());
        } catch (DataAccessException e) {
            throw new DataAccessException(String.format("Unable to create user in database: %s", e.getMessage()));
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        var user = rs.getString("username");
                        var pass = rs.getString("password");
                        var email = rs.getString("email");
                        return new UserData(user, email, pass);
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(String.format("Unable to get user from database: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void createAuth(AuthData authorization) throws DataAccessException {
        try{
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            HELPER_METHODS.executeUpdate(statement, authorization.authToken(), authorization.username());
        } catch (DataAccessException e) {
            throw new DataAccessException(String.format("Unable to create auth in database: %s", e.getMessage()));
        }
    }

    @Override
    public String getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username FROM auth WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return rs.getString("username");
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(String.format("Unable to get auth from database: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws UnauthorizedException, DataAccessException {
        if (getAuth(authToken) == null) {
            throw new UnauthorizedException("unauthorized");
        }
        try (Connection conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement("DELETE FROM auth WHERE authToken=?")){
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        try{
            var statement = "INSERT INTO games (gameID, gameName, game) VALUES (?, ?, ?)";
            String gameString = new Gson().toJson(gameData.game());
            HELPER_METHODS.executeUpdate(statement, gameData.gameID(), gameData.gameName(), gameString);
        } catch (DataAccessException e) {
            throw new DataAccessException(String.format("Unable to create game in database: %s", e.getMessage()));
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM games WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        var returnedID = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var gameJson = rs.getString("game");
                        ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);
                        return new GameData(returnedID, whiteUsername, blackUsername, gameName, game);
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(String.format("Unable to get auth from database: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException{
        Collection<GameData> games = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                try (ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        var returnedID = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var gameJson = rs.getString("game");
                        ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);
                        games.add(new GameData(returnedID, whiteUsername, blackUsername, gameName, game));
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(String.format("Unable to list games from database: %s", e.getMessage()));
        }
        return games;
    }

    @Override
    public void joinGame(String authToken, JoinGameData joinGameReq) throws Exception {
        if (joinGameReq.playerColor() == ChessGame.TeamColor.WHITE){
            if (getGame(joinGameReq.gameID()).whiteUsername() != null){
                throw new Exception("already taken");
            }
            var statement = "UPDATE games SET whiteUsername = (?) WHERE gameID = (?)";
            HELPER_METHODS.executeUpdate(statement, getAuth(authToken), joinGameReq.gameID());
        }
        if (joinGameReq.playerColor() == ChessGame.TeamColor.BLACK){
            if (getGame(joinGameReq.gameID()).blackUsername() != null){
                throw new Exception("already taken");
            }
            var statement = "UPDATE games SET blackUsername = (?) WHERE gameID = (?)";
            HELPER_METHODS.executeUpdate(statement, getAuth(authToken), joinGameReq.gameID());
        }
    }

    @Override
    public void removePlayer(String authToken, GameData game) {
        try {
            if (getAuth(authToken).equals(getGame(game.gameID()).whiteUsername())){
                var statement = "UPDATE games SET whiteUsername = (?) WHERE gameID = (?)";
                HELPER_METHODS.executeUpdate(statement, null, game.gameID());
            }
            else if (getAuth(authToken).equals(getGame(game.gameID()).blackUsername())){
                var statement = "UPDATE games SET blackUsername = (?) WHERE gameID = (?)";
                HELPER_METHODS.executeUpdate(statement, null, game.gameID());
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}

