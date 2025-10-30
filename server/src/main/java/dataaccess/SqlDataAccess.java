package dataaccess;

import com.google.gson.Gson;
import model.*;

import java.sql.*;
import java.util.Collection;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlDataAccess implements DataAccess{

    public SqlDataAccess() throws DataAccessException{
        configureDatabase();
    }

    @Override
    public void clear() {

    }

    @Override
    public void createUser(UserData user) {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        }

        statement.setString()



    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createAuth(AuthData authorization) {

    }

    @Override
    public String getAuth(String authToken) {
        return "";
    }

    @Override
    public void deleteAuth(String authToken) throws Exception {

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


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
                'username' varchar(256) NOT NULL,
                'email' varchar(256) NOT NULL,
                'password' varchar(256) NOT NULL,
                'json' TEXT DEFAULT NULL,
            PRIMARY KEY ('username'))
            """
    };

    public void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}

