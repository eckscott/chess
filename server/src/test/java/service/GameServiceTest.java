package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.SqlDataAccess;
import model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private static UserData user;
    private static GameData createGameData;
    private static GameData existingGameData;
    private static GameService service;
    private static UserService userService;
    //private static MemoryDataAccess db;
    private static SqlDataAccess db;
    private static AuthData auth;

    @BeforeEach
    public void setup() throws Exception{
        db = new SqlDataAccess();
        userService = new UserService(db);
        service = new GameService(db);
        userService.clear();
        user = new UserData("joe", "j@jmail.com", "123");
        auth = userService.register(user);

        createGameData = new GameData(0, null, null, "gameName", null);
    }

    @Test
    @DisplayName("Clear")
    void clear() throws Exception{
        existingGameData = service.createGame(auth.authToken(), createGameData);
        userService.clear();
        assertNull(db.getUser("joe"));
        assertNull(db.getGame(existingGameData.gameID()));
    }

    @Test
    @DisplayName("Good Create Game")
    void createGame() throws DataAccessException {
        existingGameData = service.createGame(auth.authToken(), createGameData);
        assertNotNull(db.listGames());
        assertEquals(existingGameData, db.getGame(existingGameData.gameID()));
    }

    @Test
    @DisplayName("Bad Create Game -- unauthorized")
    void badCreateGame() throws DataAccessException {
        userService.logout(auth.authToken());
        Exception ex = assertThrows(Exception.class, () -> {service.createGame(auth.authToken(), createGameData);});
        assertEquals("unauthorized", ex.getMessage());
    }

    @Test
    @DisplayName("Good list Games")
    void listGames() throws Exception {
        existingGameData = service.createGame(auth.authToken(), createGameData);
        Collection<GameData> games = new ArrayList<>();
        games.add(existingGameData);
        var response = service.listGames(auth.authToken());
        var compResponse = new ListGamesResponse(db.listGames());

        assertEquals(compResponse, response);
    }

    @Test
    @DisplayName("Bad list Games -- unauthorized")
    void badListGames() throws DataAccessException {
        userService.logout(auth.authToken());
        Exception ex = assertThrows(Exception.class, () -> {service.listGames(auth.authToken());});
        assertEquals("unauthorized", ex.getMessage());
    }

    @Test
    @DisplayName("Good join Game")
    void joinGame() throws Exception{
        existingGameData = service.createGame(auth.authToken(), createGameData);
        var joinReq = new JoinGameData(ChessGame.TeamColor.WHITE, existingGameData.gameID());
        service.joinGame(auth.authToken(), joinReq);
        assertNotNull(db.getGame(existingGameData.gameID()).whiteUsername());
        assertEquals(auth.username(), db.getGame(existingGameData.gameID()).whiteUsername());
    }

    @Test
    @DisplayName("Bad join Game -- already taken")
    void badJoinGame() throws Exception{
        // fill the white spot
        existingGameData = service.createGame(auth.authToken(), createGameData);
        var joinReq = new JoinGameData(ChessGame.TeamColor.WHITE, existingGameData.gameID());
        service.joinGame(auth.authToken(), joinReq);

        // create new user and try to join same spot
        var newAuth = userService.register(new UserData("frank", "happy@hotmail", "swag"));
        var badJoinReq = new JoinGameData(ChessGame.TeamColor.WHITE, existingGameData.gameID());
        Exception ex = assertThrows(Exception.class, () -> {service.joinGame(newAuth.authToken(), badJoinReq);});
        assertEquals("already taken", ex.getMessage());
    }
}