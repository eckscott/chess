package service;

import java.util.Map;
import dataaccess.DataAccess;
import datamodel.*;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }
    public AuthData register(UserData user){
        return new AuthData(user.username(), generateAuthToken());
    }

    private String generateAuthToken(){
        return "xyz";
    }
}
