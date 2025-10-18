package service;

import dataaccess.MemoryDataAccess;
import datamodel.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class GameService {

    private final MemoryDataAccess dataAccess;

    public GameService(MemoryDataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public Collection<GameData> listGames(String authToken) throws Exception {
        if (authToken == null || dataAccess.getAuth(authToken) == null)
            throw new UnauthorizedException("Error: unauthorized");

        return dataAccess.listGames();
    }
}
