package websocket;

import com.google.gson.Gson;
import jakarta.websocket.*;
import model.AuthData;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import chess.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(int port, NotificationHandler notificationHandler){
        try {
            this.notificationHandler = notificationHandler;
            URI socketURI = new URI("ws://localhost:" + port + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    if (notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                        notificationHandler.notify(new Gson().fromJson(message, NotificationMessage.class));
                    }
                    if (notification.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                        notificationHandler.errorMessage(new Gson().fromJson(message, ErrorMessage.class));
                    }
                    if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        notificationHandler.loadGame(new Gson().fromJson(message, LoadGameMessage.class));
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinGame(AuthData auth, int gameID) throws IOException {
        var cmd = new UserGameCommand(UserGameCommand.CommandType.CONNECT, auth.authToken(), gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
    }

    public void leaveGame(AuthData auth, int gameID) throws IOException {
        var cmd = new UserGameCommand(UserGameCommand.CommandType.LEAVE, auth.authToken(), gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
    }

    public void makeMove(AuthData auth, int gameID, ChessMove move) throws IOException {
        var cmd = new MakeMoveCommand(move, auth.authToken(), gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
