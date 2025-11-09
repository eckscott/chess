package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.net.URI;
import java.net.http.HttpRequest;

public class ServerFacade {
    private final int port;

    public ServerFacade(int port){
        this.port = port;
    }


    public AuthData register(String username, String password, String email) {
        var registerData = new UserData(username, email, password);
        var jsonBody = new Gson().toJson(registerData);
        var req = buildReq("POST", "/user", jsonBody);
    }

    private HttpRequest buildReq(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }
}
