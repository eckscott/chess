package server;

import com.google.gson.Gson;
import dataaccess.MemoryDataAccess;
import datamodel.AuthData;
import datamodel.UserData;
import io.javalin.*;
import io.javalin.http.Context;
import service.UserService;

import java.util.Map;

public class Server {

    private final Javalin server;
    private final UserService userService;

    public Server() {
        var dataAccess = new MemoryDataAccess();
        userService = new UserService(dataAccess);
        server = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        server.delete("db", ctx -> ctx.result("{}"));
        server.post("user", ctx -> register(ctx));
        server.post("session", ctx -> login(ctx));
        server.delete("session", ctx -> ctx.result("{}"));

    }

    private void register(Context ctx){
        try{
            var serializer = new Gson();
            String requestJson = ctx.body();
            UserData userRequest = serializer.fromJson(requestJson, UserData.class);

            // call to the service and register
            AuthData authData = userService.register(userRequest);

            ctx.result(serializer.toJson(authData));
        }
        catch (Exception ex){
            var msg = String.format("{ \"message\": \"Error: %s\" }", ex.getMessage());
            ctx.status(403).result(msg);
        }
    }

    private void login(Context ctx){
        try{
            var serializer = new Gson();
            String requestJson = ctx.body();
            UserData loginRequest = serializer.fromJson(requestJson, UserData.class);

            // call to the service and login
            AuthData loginData = userService.login(loginRequest);

            ctx.result(serializer.toJson(loginData));
        }
        catch (Exception ex){
            var msg = String.format("{ \"message\": \"Error: %s\" }", ex.getMessage());
            ctx.status(400).result(msg);
        }
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
