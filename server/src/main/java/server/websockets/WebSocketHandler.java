package server.websockets;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SqlDataAccess;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final UserService userService;
    private final GameService gameService;

    public WebSocketHandler(){
        try {
            var dataAccess = new SqlDataAccess();
            userService = new UserService(dataAccess);
            gameService = new GameService(dataAccess);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand cmd = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (cmd.getCommandType()){
                case CONNECT -> playerJoin(cmd, ctx.session);
                case LEAVE -> playerLeave(cmd, ctx.session);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void playerJoin(UserGameCommand cmd, Session session) throws DataAccessException, IOException {
        if (connections.contains(cmd.getGameID())){
            Collection<Session> connectedUsers = connections.alreadyConnected(cmd.getGameID());
            connectedUsers.add(session);
            connections.add(cmd.getGameID(), connectedUsers);
        }
        else {
            Collection<Session> connectedUsers = new ArrayList<>();
            connectedUsers.add(session);
            connections.add(cmd.getGameID(), connectedUsers);
        }

        String message;
        if (gameService.getGame(cmd.getGameID()) == null){
            message = "ERROR that game does not exist";
            connections.sendToSelf(session, new ErrorMessage(message), cmd.getGameID());
        }
        else if (userService.getUsername(cmd.getAuthToken()) == null){
            message = "ERROR that user is not authorized";
            connections.sendToSelf(session, new ErrorMessage(message), cmd.getGameID());
        }
        else {
            if (userService.getUsername(cmd.getAuthToken()).equals(gameService.getGame(cmd.getGameID()).blackUsername())) {
                message = String.format("%s has joined the game as black", userService.getUsername(cmd.getAuthToken()));
            } else if (userService.getUsername(cmd.getAuthToken()).equals(gameService.getGame(cmd.getGameID()).whiteUsername())) {
                message = String.format("%s has joined the game as white", userService.getUsername(cmd.getAuthToken()));
            } else {
                message = String.format("%s has joined the game as a spectator", userService.getUsername(cmd.getAuthToken()));
            }
            var notificationMsg = new NotificationMessage(message);
            var loadGameMsg = new LoadGameMessage(gameService.getGame(cmd.getGameID()).game());
            connections.sendToSelf(session, loadGameMsg, cmd.getGameID());
            connections.broadcast(session, notificationMsg, cmd.getGameID());
        }
    }

    private void playerLeave(UserGameCommand cmd, Session session) throws DataAccessException, IOException {
        String message = String.format("%s has left the game", userService.getUsername(cmd.getAuthToken()));
        if (userService.getUsername(cmd.getAuthToken()).equals(gameService.getGame(cmd.getGameID()).whiteUsername()) ||
            userService.getUsername(cmd.getAuthToken()).equals(gameService.getGame(cmd.getGameID()).blackUsername())){
            gameService.leaveGame(cmd.getAuthToken(), gameService.getGame(cmd.getGameID()));
        }
        connections.broadcast(session, new NotificationMessage(message), cmd.getGameID());
        connections.remove(session, cmd.getGameID());
    }
}
