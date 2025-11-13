package server;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

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
            throw new RuntimeException("Bad response: " + response.statusCode());
        }
    }

    public GameData createGame(AuthData authorization, GameData gameData){
        var req = buildReq("POST", "/game", gameData, authorization.authToken());
        var response = sendReq(req);
        if (response.statusCode() != 200){
            throw new RuntimeException("Bad response: " + response.statusCode());
        }
        var jsonResponse = response.body();
        return new Gson().fromJson(jsonResponse, GameData.class);
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
        else {
            throw new RuntimeException("Bad response: " + response.statusCode());
        }
    }

}
