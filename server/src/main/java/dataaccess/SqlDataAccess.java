package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import dataaccess.SqlHelperMethods;
import org.eclipse.jetty.server.Authentication;
import service.UnauthorizedException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlDataAccess implements DataAccess{

    private static final SqlHelperMethods helper = new SqlHelperMethods();

    public SqlDataAccess() throws DataAccessException{
        helper.configureDatabase();
    }

    @Override
    public void clear() {
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
            helper.configureDatabase();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(UserData user) {
        try {
            var statement = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            helper.executeUpdate(statement, user.username(), user.email(), user.password());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserData getUser(String username) {
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
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void createAuth(AuthData authorization) {
        try{
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            helper.executeUpdate(statement, authorization.authToken(), authorization.username());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getAuth(String authToken) {
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
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws UnauthorizedException {
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
    public void createGame(GameData gameData) {
        try{
            var statement = "INSERT INTO games (gameID, gameName) VALUES (?, ?)";
            helper.executeUpdate(statement, gameData.gameID(), gameData.gameName());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameData getGame(int gameID) {
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
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
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
            throw new RuntimeException(e);
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
            helper.executeUpdate(statement, getAuth(authToken), joinGameReq.gameID());
        }
        if (joinGameReq.playerColor() == ChessGame.TeamColor.BLACK){
            if (getGame(joinGameReq.gameID()).blackUsername() != null){
                throw new Exception("already taken");
            }
            var statement = "UPDATE games SET blackUsername = (?) WHERE gameID = (?)";
            helper.executeUpdate(statement, getAuth(authToken), joinGameReq.gameID());
        }
    }

}

