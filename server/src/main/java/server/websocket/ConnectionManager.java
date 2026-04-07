package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public class Connection{
        private String username;
        private Session session;

        public Connection(String username, Session session) {
            this.username = username;
            this.session = session;
        }
    }

    public final ConcurrentHashMap<Integer, Connection> connections = new ConcurrentHashMap<>();

    public HashMap<String,Session> userInfo = new HashMap();

    public void add(Integer gameId, String username, Session session) {
        if(!connections.containsKey(gameId)){
            Connection connection = new Connection(username,session);
            connections.put(gameId,connection);
        }

    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcast(Session excludeSession, ServerMessage notification) throws IOException {
        String msg = notification.toString();
        for (Connection c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.session.getRemote().sendString(msg);
                }
            }
        }
    }
}
