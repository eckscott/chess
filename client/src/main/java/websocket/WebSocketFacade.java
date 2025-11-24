package websocket;

import com.google.gson.Gson;
import jakarta.websocket.*;
import model.AuthData;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

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
                    notificationHandler.notify(notification);
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

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
