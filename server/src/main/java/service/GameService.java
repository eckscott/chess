package service;

import dataaccess.MemoryDataAccess;
import model.GameData;
import model.JoinGameData;
import model.ListGamesResponse;

import java.util.Collection;
import java.util.Random;

public class GameService {

    private final MemoryDataAccess dataAccess;

    public GameService(MemoryDataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public GameData createGame(String authToken, GameData createGameRequest){
        if (createGameRequest.gameName() == null)
            throw new BadRequestException("bad request");
        if (authToken == null || dataAccess.getAuth(authToken) == null)
            throw new UnauthorizedException("unauthorized");

        int gameID = generateGameID();
        GameData createGameData = new GameData(gameID, null, null, createGameRequest.gameName(), null);
        dataAccess.createGame(createGameData);

        return createGameData;
    }

    public ListGamesResponse listGames(String authToken) throws Exception {
        if (authToken == null || dataAccess.getAuth(authToken) == null)
            throw new UnauthorizedException("unauthorized");

        Collection<GameData> gamesList = dataAccess.listGames();
        return new ListGamesResponse(gamesList);
    }

    public void joinGame(String authToken, JoinGameData joinGameReq) throws Exception{
        if (joinGameReq.gameID() <= 0 || joinGameReq.playerColor() == null)
            throw new BadRequestException("bad request");
        if (authToken == null || dataAccess.getAuth(authToken) == null)
            throw new UnauthorizedException("unauthorized");

        dataAccess.joinGame(authToken, joinGameReq);
    }

    public int generateGameID(){
        Random randNum = new Random();
        return randNum.nextInt(1, 1000);
    }
}
