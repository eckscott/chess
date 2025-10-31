package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import static java.sql.Types.NULL;

public class SqlHelperMethods {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
                username varchar(256) NOT NULL,
                email varchar(256) NOT NULL,
                password varchar(256) NOT NULL,
            PRIMARY KEY (username))
            """
            ,
            """
            CREATE TABLE IF NOT EXISTS auth (
                authToken varchar(256) NOT NULL,
                username varchar(256) NOT NULL,
            PRIMARY KEY (authToken))
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

    public void executeUpdate(String statement, Object... params) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()){
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                for (int i = 0; i < params.length; i++){
                    Object currParam = params[i];
                    if (currParam instanceof String p){
                        ps.setString(i + 1, p);
                    }
                    else if (currParam instanceof Integer p){
                        ps.setInt(i + 1, p);
                    }
                    else if (currParam instanceof ChessGame p){
                        var jsonGame = new Gson().toJson(p);
                        ps.setString(i + 1, jsonGame);
                    }
                    else {
                        ps.setNull(i + 1, NULL);
                    }
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to update database: %s", e.getMessage()));
        }
    }
}
