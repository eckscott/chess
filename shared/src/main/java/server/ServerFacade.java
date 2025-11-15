package server;

import Exceptions.AlreadyTakenException;
import Exceptions.BadRequestException;
import Exceptions.UnauthorizedException;
import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final int port;

    public ServerFacade(int port){
        this.port = port;
    }

    public void clear() {
        var req = buildReq("DELETE", "/db", null, null);
        requestHandler(req);
    }

    public AuthData register(String username, String password, String email) {
        var registerData = new UserData(username, email, password);
        var req = buildReq("POST", "/user", registerData, null);
        return requestHandler(req);
    }

    public AuthData login(String username, String password) {
        var loginData = new UserData(username, null, password);
        var req = buildReq("POST", "/session", loginData, null);
        return requestHandler(req);
    }

    public void logout(AuthData authorization){
        var req = buildReq("DELETE", "/session", null, authorization.authToken());
        var response = sendReq(req);
        if (response.statusCode() != 200){
            throw new UnauthorizedException("Make sure you are logged in before attempting to logout\n");
        }
    }

    public GameData createGame(AuthData authorization, GameData gameData){
        var req = buildReq("POST", "/game", gameData, authorization.authToken());
        var response = sendReq(req);
        if (response.statusCode() == 400){
            throw new BadRequestException("Please provide a game name\n");
        }
        else if (response.statusCode() == 401){
            throw new UnauthorizedException("Please make sure you are logged in before attempting to create a game\n");
        }
        var jsonResponse = response.body();
        return new Gson().fromJson(jsonResponse, GameData.class);
    }

    public ListGamesResponse listGames(AuthData authorization){
        var req = buildReq("GET", "/game", null, authorization.authToken());
        var response = sendReq(req);
        if (response.statusCode() == 401){
            throw new UnauthorizedException("Please make sure you are logged in before attempting to see games\n");
        }
        var jsonResponse = response.body();
        return new Gson().fromJson(jsonResponse, ListGamesResponse.class);
    }

    public GameData findGame(AuthData authorization, String gameIndexString){
        ListGamesResponse listOfGames = listGames(authorization);
        int gameIndex = Integer.parseInt(gameIndexString);
        int i = 0;
        for (GameData game : listOfGames.games()){
            if (i == gameIndex - 1){
                return game;
            }
            i++;
        }
        return null;
    }

    public void joinGame(AuthData authorization, JoinGameData joinGameReq){
        var req = buildReq("PUT", "/game", joinGameReq, authorization.authToken());
        var response = sendReq(req);
        if (response.statusCode() == 400){
            throw new BadRequestException("Please provide a valid game to join\n");
        }
        if (response.statusCode() == 401){
            throw new UnauthorizedException("Please make sure you are logged in before attempting to join a game\n");
        }
        if (response.statusCode() == 403){
            throw new AlreadyTakenException("Sorry! Somebody is already there. Please try joining a different position\n");
        }
    }

    private HttpRequest buildReq(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("Authorization", authToken);
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendReq(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthData requestHandler(HttpRequest req){
        var response = sendReq(req);
        if (response.statusCode() == 200){
            var jsonResponse = response.body();
            return new Gson().fromJson(jsonResponse, AuthData.class);
        }
        else if (response.statusCode() == 400){
            throw new BadRequestException("Please try different inputs\n");
        }
        else if (response.statusCode() == 401){
            throw new UnauthorizedException("Please make sure you are already registed and that" +
                                            " your username and password are correct\n");
        }
        else if (response.statusCode() == 403){
            throw new AlreadyTakenException("Sorry! That username is taken. Please provide unique credentials\n");
        }
        else {
            throw new RuntimeException("Bad response: " + response.statusCode());
        }
    }

}
