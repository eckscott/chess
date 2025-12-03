package websocket;

import websocket.messages.*;

public interface NotificationHandler {
    void notify(NotificationMessage message);
    void errorMessage(ErrorMessage message);
    void loadGame(LoadGameMessage message);
}
