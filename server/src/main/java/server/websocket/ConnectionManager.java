package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Map<String, Session>> connections = new ConcurrentHashMap<>();

    public HashMap<String,Session> userInfo = new HashMap();

    public void add(Integer gameId, String username, Session session) {
        if(!connections.containsKey(gameId)){
            userInfo.put(username,session);
            connections.put(gameId,userInfo);
        }

    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcast(Session excludeSession, Notification notification) throws IOException {
        String msg = notification.toString();
        for (Session c : connections.values()) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
