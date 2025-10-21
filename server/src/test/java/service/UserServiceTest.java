package service;

import dataaccess.MemoryDataAccess;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    @DisplayName("Good registration")
    void register() throws Exception {
        var db = new MemoryDataAccess();
        var user = new UserData("joe", "j@j.com", "123");
        var service = new UserService(db);
        var authData = service.register(user);
        assertNotNull(authData);
        assertEquals(user.username(), authData.username());
        assertTrue(!authData.authToken().isEmpty());
    }

    @Test
    @DisplayName("Good login")
    void login() throws Exception {
        // register
        var db = new MemoryDataAccess();
        var user = new UserData("joe", "j@j.com", "123");
        var service = new UserService(db);
        var authData = service.register(user);

        // login
        var loginCreds = new UserData("joe", null, "123");
        var newAuth = service.login(loginCreds);
        assertNotNull(newAuth);
        assertEquals(authData.username(), newAuth.username());
    }
}