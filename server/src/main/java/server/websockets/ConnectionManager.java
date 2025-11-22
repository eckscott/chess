package server.websockets;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Collection<Session>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Collection<Session> sessions){
        connections.put(gameID, sessions);
    }

    public boolean contains(int gameID){
        return connections.containsKey(gameID);
    }

    public Collection<Session> alreadyConnected(int gameID){
        return connections.get(gameID);
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcast(Session excludeSession, ServerMessage notification, int gameID) throws IOException {
        Collection<Session> connectedSessions = connections.get(gameID);
        String msg = notification.toString();
        for (Session c : connectedSessions) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
