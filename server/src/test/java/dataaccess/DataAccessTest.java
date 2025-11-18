package dataaccess;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTest {

    private static SqlDataAccess db;
    private static UserData user;
    private static AuthData auth;

    @BeforeEach
    public void setup() throws Exception {
        db = new SqlDataAccess();
        user = new UserData("joe", "j@j.com", "123");
        auth = new AuthData("joe", "xyz");
        db.clear();
    }

    @Test
    void clear() throws DataAccessException {
        db.createUser(user);
        db.clear();
        assertNull(db.getUser("joe"));
    }

    @Test
    @DisplayName("Good createUser")
    void createUser() throws DataAccessException {
        db.createUser(user);
        assertEquals(user, db.getUser(user.username()));
    }

    @Test
    @DisplayName("Bad createUser")
    void createBadUser() throws DataAccessException {
        db.createUser(user);
    }

    @Test
    @DisplayName("Good getUser")
    void getUser() {

    }

    @Test
    @DisplayName("Bad getUser")
    void getUserBad() {

    }

    @Test
    @DisplayName("Good createAuth")
    void createAuth() throws DataAccessException {
        db.createAuth(auth);
        assertEquals(auth.username(), db.getAuth(auth.authToken()));
    }

    @Test
    @DisplayName("Bad createAuth -- ")
    void createBadAuth() {

    }

    @Test
    @DisplayName("Good getAuth")
    void getAuth() {

    }

    @Test
    @DisplayName("Bad getAuth -- ")
    void getAuthBad() {

    }

    @Test
    @DisplayName("Good deleteAuth")
    void deleteAuth() {

    }

    @Test
    @DisplayName("Bad deleteAuth")
    void deleteAuthBad() {

    }

    @Test
    @DisplayName("Good createGame")
    void createGame() throws DataAccessException {
        db.createUser(user);
        GameData createGameReq = new GameData(100, null, null, "gameName", new ChessGame());
        db.createGame(createGameReq);
        assertNotNull(db.getGame(100).game());

    }

    @Test
    @DisplayName("Bad createGame -- ")
    void createBadGame() {

    }

    @Test
    @DisplayName("Good getGame")
    void getGame() {

    }

    @Test
    @DisplayName("Bad getGame -- ")
    void getGameBad() {

    }

    @Test
    @DisplayName("Good listGames")
    void listGames() {

    }

    @Test
    @DisplayName("Bad listGames -- ")
    void listGamesBad() {

    }

    @Test
    @DisplayName("Good joinGame")
    void joinGame() {

    }

    @Test
    @DisplayName("Bad joinGame -- ")
    void joinGameBad() {

    }


}