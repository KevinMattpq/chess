package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public static class Connection{
        private String username;
        private Session session;
        private int gameId;

        public Connection(Integer gameId,String username, Session session) {
            this.username = username;
            this.session = session;
            this.gameId = gameId;
        }
    }

    public final ConcurrentHashMap<Connection, Integer> connections = new ConcurrentHashMap<>();

    public HashMap<String,Session> userInfo = new HashMap();

    public void add(Integer gameId, String username, Session session) {
        if(!connections.containsKey(gameId)){
            Connection connection = new Connection(gameId,username,session);
            connections.put(connection,gameId);
        }

    }

    public void remove(Session session) {
        for (Connection c : connections.keySet()){
            if(c.session.equals(session)){
                connections.remove(c);
            }
        }
    }

    public void broadcast(Integer gameId,Session excludeSession, ServerMessage notification) throws IOException {
        String msg = new Gson().toJson(notification);
        for (Connection c : connections.keySet()) {
            if (c.gameId == gameId){
                if (c.session.isOpen()) {
                    if (!c.session.equals(excludeSession)) {
                        c.session.getRemote().sendString(msg);
                    }
                }
            }

        }
    }
}
