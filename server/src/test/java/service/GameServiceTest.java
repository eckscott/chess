package service;

import chess.ChessGame;
import dataaccess.MemoryDataAccess;
import model.*;
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
    private static MemoryDataAccess db;
    private static AuthData auth;

    @BeforeEach
    public void setup() throws Exception{
        db = new MemoryDataAccess();
        userService = new UserService(db);
        service = new GameService(db);
        user = new UserData("joe", "j@jmail.com", "123");
        auth = userService.register(user);

        createGameData = new GameData(0, null, null, "gameName", null);
    }

    @Test
    @DisplayName("Good Create Game")
    void createGame() {
        existingGameData = service.createGame(auth.authToken(), createGameData);
        assertNotNull(db.listGames());
        assertEquals(existingGameData, db.getGame(existingGameData.gameID()));
    }

    @Test
    @DisplayName("Bad Create Game -- unauthorized")
    void badCreateGame() {
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
        var compResponse = new ListGamesResponse(games);

        assertEquals(compResponse, response);
    }

    @Test
    @DisplayName("Bad list Games -- unauthorized")
    void badListGames() {
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