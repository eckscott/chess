package client;

import dataaccess.MemoryDataAccess;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

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
    @DisplayName("Positive login Test")
    public void loginTest() {
        AuthData registerAuthData = facade.register("player1", "password1", "p1@email.com");
        AuthData loginAuthData = facade.login("player1", "password1");
        assertTrue(loginAuthData.authToken().length() > 10);
    }

}
