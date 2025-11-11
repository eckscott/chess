package server;

import com.google.gson.Gson;
import model.AuthData;
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


    public AuthData register(String username, String password, String email) {
        var registerData = new UserData(username, email, password);
        var req = buildReq("POST", "/user", registerData);
        var response = sendReq(req);
        var status = response.statusCode();
        if (status == 200){
            var jsonResponse = response.body();
            return new Gson().fromJson(jsonResponse, AuthData.class);
        }
        else {
            throw new RuntimeException("Bad response" + status);
        }
    }

    private HttpRequest buildReq(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
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



}
