package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SqlDataAccess;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;
import model.*;
import io.javalin.*;
import io.javalin.http.Context;
import model.ListGamesResponse;
import exceptions.BadRequestException;
import org.eclipse.jetty.websocket.api.Session;
import server.websockets.ConnectionManager;
import service.GameService;
import exceptions.UnauthorizedException;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class Server {

    private final Javalin server;
    private final UserService userService;
    private final GameService gameService;
    private final ConnectionManager connections = new ConnectionManager();

    public Server() {
        //var dataAccess = new MemoryDataAccess();
        try {
            var dataAccess = new SqlDataAccess();
            userService = new UserService(dataAccess);
            gameService = new GameService(dataAccess);
            server = Javalin.create(config -> config.staticFiles.add("web"));

            // clear
            server.delete("db", ctx -> clear(ctx));

            // users
            server.post("user", ctx -> register(ctx));
            server.post("session", ctx -> login(ctx));
            server.delete("session", ctx -> logout(ctx));

            // games
            server.post("game", ctx -> createGame(ctx));
            server.get("game", ctx -> listGames(ctx));
            server.put("game", ctx -> joinGame(ctx));

            // websockets
            server.ws("/ws", ws -> ws.onConnect(ctx -> connect(ctx)));
            server.ws("/ws", ws -> ws.onMessage(ctx -> wsMessage(ctx)));

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private void clear(Context ctx){
        try{
            userService.clear();
            ctx.result("{}");
        } catch (DataAccessException e) {
            var msg = String.format("{ \"message\": \"Error: %s\" }", e.getMessage());
            ctx.status(500).result(msg);
        }
    }

    private void register(Context ctx){
        try{
            var serializer = new Gson();
            String requestJson = ctx.body();
            UserData userRequest = serializer.fromJson(requestJson, UserData.class);

            // call to the service and register
            AuthData authData = userService.register(userRequest);

            ctx.result(serializer.toJson(authData));
        } catch (BadRequestException ex){
            var msg = String.format("{ \"message\": \"Error: %s\" }", ex.getMessage());
            ctx.status(400).result(msg);
        } catch (DataAccessException ex){
            var msg = String.format("{ \"message\": \"Error: %s\" }", ex.getMessage());
            ctx.status(500).result(msg);
        } catch (Exception ex){
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
        catch (BadRequestException ex){
            var msg = String.format("{ \"message\": \"Error: %s\" }", ex.getMessage());
            ctx.status(400).result(msg);
        }
        catch (UnauthorizedException ex){
            var msg = String.format("{ \"message\": \"Error: %s\" }", ex.getMessage());
            ctx.status(401).result(msg);
        }
        catch (Exception ex){
            var msg = String.format("{ \"message\": \"Error: %s\" }", ex.getMessage());
            ctx.status(500).result(msg);
        }
    }

    private void logout(Context ctx){
        try{
            String token = ctx.header("Authorization");

            // call to the service and register
            userService.logout(token);

            ctx.result("{}");
        }
        catch (UnauthorizedException ex){
            var msg = String.format("{ \"message\": \"Error: %s\" }", ex.getMessage());
            ctx.status(401).result(msg);
        }
        catch (Exception ex){
            var msg = String.format("{ \"message\": \"Error: %s\" }", ex.getMessage());
            ctx.status(500).result(msg);
        }
    }

    private void createGame(Context ctx){
        try {
            var serializer = new Gson();
            String token = ctx.header("Authorization");
            String requestJson = ctx.body();
            GameData gameRequest = serializer.fromJson(requestJson, GameData.class);

            GameData createGameResult = gameService.createGame(token, gameRequest);
            ctx.result(serializer.toJson(createGameResult));
        }
        catch (BadRequestException e) {
            var msg = String.format("{ \"message\": \"Error: %s\" }", e.getMessage());
            ctx.status(400).result(msg);
        }
        catch (UnauthorizedException e) {
            var msg = String.format("{ \"message\": \"Error: %s\" }", e.getMessage());
            ctx.status(401).result(msg);
        } catch (DataAccessException e) {
            var msg = String.format("{ \"message\": \"Error: %s\" }", e.getMessage());
            ctx.status(500).result(msg);
        }
    }

    private void listGames(Context ctx){
        try {
            var serializer = new Gson();
            String token = ctx.header("Authorization");
            ListGamesResponse games = gameService.listGames(token);

            ctx.result(serializer.toJson(games));
        } catch (DataAccessException e) {
            var msg = String.format("{ \"message\": \"Error: %s\" }", e.getMessage());
            ctx.status(500).result(msg);
        } catch (UnauthorizedException e) {
            var msg = String.format("{ \"message\": \"Error: %s\" }", e.getMessage());
            ctx.status(401).result(msg);
        }
    }

    private void joinGame(Context ctx){
        try {
            var serializer = new Gson();
            String token = ctx.header("Authorization");
            String requestJson = ctx.body();
            JoinGameData joinGameRequest = serializer.fromJson(requestJson, JoinGameData.class);

            gameService.joinGame(token, joinGameRequest);

            ctx.result("{}");
        }
        catch (BadRequestException e){
            var msg = String.format("{ \"message\": \"Error: %s\" }", e.getMessage());
            ctx.status(400).result(msg);
        }
        catch (UnauthorizedException e){
            var msg = String.format("{ \"message\": \"Error: %s\" }", e.getMessage());
            ctx.status(401).result(msg);
        }
        catch (DataAccessException e) {
            var msg = String.format("{ \"message\": \"Error: %s\" }", e.getMessage());
            ctx.status(500).result(msg);
        }
        catch (Exception e) {
            var msg = String.format("{ \"message\": \"Error: %s\" }", e.getMessage());
            ctx.status(403).result(msg);
        }
    }

    private void connect(WsConnectContext ctx){
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    private void wsMessage(WsMessageContext ctx){
        try {
            UserGameCommand cmd = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (cmd.getCommandType()){
                case CONNECT -> playerJoin(cmd.getAuthToken(), ctx.session);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void playerJoin(String authToken, Session session) throws DataAccessException, IOException {
        connections.add(session);
        var message = String.format("%s has joined the game", userService.getUsername(authToken));
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.setServerMessageContent(message);
        connections.broadcast(session, notification);
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
