package dataaccess;

import com.google.gson.Gson;
import model.*;

import java.sql.*;
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

    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void joinGame(String authToken, JoinGameData joinGameReq) throws Exception {

    }

}

