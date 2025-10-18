package service;

import dataaccess.MemoryDataAccess;
import datamodel.UserData;
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
        var db = new MemoryDataAccess();
        var user = new UserData("joe", null, "123");
        var service = new UserService(db);
        var authData = service.login(user);
        assertNotNull(authData);
    }
}