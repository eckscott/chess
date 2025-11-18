package client;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void reset() {
        facade.clear();
    }

    @Test
    @DisplayName("clear test")
    public void clearTest() {
        facade.clear();
        //assertNull()
    }

    @Test
    @DisplayName("Positive register Test")
    public void registerTest() {
        AuthData authData = facade.register("player1", "password1", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    @DisplayName("Negative register Test")
    public void badRegister() {

    }

    @Test
    @DisplayName("Positive login Test")
    public void loginTest() {
        AuthData registerAuthData = facade.register("player1", "password1", "p1@email.com");
        AuthData loginAuthData = facade.login("player1", "password1");
        assertTrue(loginAuthData.authToken().length() > 10);
    }

    @Test
    @DisplayName("Negative login Test")
    public void badLogin() {

    }

    @Test
    @DisplayName("Positive logout Test")
    public void logoutTest() {
        AuthData authData = facade.register("player1", "password1", "p1@email.com");
        facade.logout(authData);
    }

    @Test
    @DisplayName("Negative logout Test")
    public void badLogout() {

    }

    @Test
    @DisplayName("Positive create Game Test")
    public void createGameTest() {
        AuthData authData = facade.register("player1", "password1", "p1@email.com");
        GameData createGameResult = facade.createGame(authData, new GameData(0, null, null, "newGame", null));
        assertNotEquals(0, createGameResult.gameID());
    }

    @Test
    @DisplayName("Negative create Game Test")
    public void badCreateGame() {

    }

    @Test
    @DisplayName("Positive list games Test")
    public void listGamesTest() {
        AuthData authData = facade.register("player1", "password1", "p1@email.com");
        GameData createGameResult1 = facade.createGame(authData, new GameData(0, null, null, "newGame1", null));
        GameData createGameResult2 = facade.createGame(authData, new GameData(0, null, null, "newGame2", null));
        GameData createGameResult3 = facade.createGame(authData, new GameData(0, null, null, "newGame3", null));
        Collection<GameData> gameRequests = new ArrayList<>();
        gameRequests.add(createGameResult1);
        gameRequests.add(createGameResult2);
        gameRequests.add(createGameResult3);
        ListGamesResponse listResult = facade.listGames(authData);
        assertTrue(listResult.games().containsAll(gameRequests));
    }

    @Test
    @DisplayName("Negative list games Test")
    public void badListGames() {

    }

    @Test
    @DisplayName("Positive join game Test")
    public void joinGameTest() {
        AuthData authData = facade.register("player1", "password1", "p1@email.com");
        GameData createGameResult1 = facade.createGame(authData, new GameData(0, null, null, "newGame1", null));
        facade.joinGame(authData, new JoinGameData(ChessGame.TeamColor.WHITE, createGameResult1.gameID()));
    }

    @Test
    @DisplayName("Negative join game Test")
    public void badJoinGame() {

    }

}
