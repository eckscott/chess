package server.websockets;

import com.google.gson.Gson;
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

    public void remove(Session session, int gameID) {
        Collection<Session> connectedSessions = connections.get(gameID);
        connectedSessions.remove(session);
        connections.remove(gameID);
        connections.put(gameID, connectedSessions);
    }

    public void broadcast(Session excludeSession, ServerMessage notification, int gameID) throws IOException {
        Collection<Session> connectedSessions = connections.get(gameID);
        String msg = new Gson().toJson(notification);
        for (Session c : connectedSessions) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

    public void sendToSelf(Session rootSession, ServerMessage notification, int gameID) throws IOException {
        Collection<Session> connectedSessions = connections.get(gameID);
        String msg = new Gson().toJson(notification);
        for (Session c : connectedSessions) {
            if (c.isOpen()) {
                if (c.equals(rootSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
