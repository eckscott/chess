package dataaccess;

import datamodel.*;

public interface DataAccess {
    void clear();
    void createUser(UserData user);
    UserData getUser(String username);
    void createAuth(AuthData authorization);
    String getAuth(String username);
    void deleteAuth(AuthData authorization);
}
