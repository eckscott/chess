package dataaccess;

import com.google.gson.Gson;
import model.*;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import dataaccess.SqlHelperMethods;
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
            var statement = "SELECT username FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return rs.getString("username");
                    }
                }
            }
        } catch (SQLException e){

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
        return "";
    }

    @Override
    public void deleteAuth(String authToken) throws UnauthorizedException {

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

