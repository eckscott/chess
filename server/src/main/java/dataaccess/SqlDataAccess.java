package dataaccess;

import com.google.gson.Gson;
import model.*;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import dataaccess.SqlHelperMethods;

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

}

