package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.SqlDataAccess;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static UserData user;
    private static UserService service;
    //private static MemoryDataAccess db;
    private static SqlDataAccess db;
    private static AuthData authData;

    @BeforeEach
    public void setup() throws Exception {
        db = new SqlDataAccess();
        user = new UserData("joe", "j@j.com", "123");
        service = new UserService(db);
        authData = service.register(user);
    }

    @Test
    @DisplayName("Clear")
    void clear() throws Exception{
        service.clear();
        assertNull(db.getUser("joe"));
    }

    @Test
    @DisplayName("Good registration")
    void register(){
        assertNotNull(authData);
        assertEquals(user.username(), authData.username());
        assertTrue(!authData.authToken().isEmpty());
    }

    @Test
    @DisplayName("Bad registration -- existing username")
    void badRegister() {
        Exception ex = assertThrows(Exception.class, () -> {
            service.register(new UserData("joe", "abc@gmail.com", "happy"));
        });
        assertEquals("already taken", ex.getMessage());
    }

    @Test
    @DisplayName("Good login")
    void login() throws Exception {
        var loginCreds = new UserData("joe", null, "123");
        var newAuth = service.login(loginCreds);
        assertNotNull(newAuth);
        assertEquals(authData.username(), newAuth.username());
    }

    @Test
    @DisplayName("Bad login -- bad request")
    void badLogin(){
        Exception ex = assertThrows(Exception.class, () -> {
            service.login(new UserData("joe", null, null));
        });
        assertEquals("bad request", ex.getMessage());
    }

    @Test
    @DisplayName("Good logout")
    void logout(){
        service.logout(authData.authToken());
        assertNull(db.getAuth(authData.authToken()));
    }

    @Test
    @DisplayName("Bad logout -- unauthorized")
    void badLogout() {
        String diffAuthToken = "xyz";
        Exception ex = assertThrows(Exception.class, () -> {service.logout(diffAuthToken);});
        assertEquals("unauthorized", ex.getMessage());
    }

}
