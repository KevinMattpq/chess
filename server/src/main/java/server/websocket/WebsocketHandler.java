package server.websocket;
import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager conections = new ConnectionManager();

    @Override
    public void handleMessage( WsMessageContext ctx) throws Exception {
        //Handle each of the Four command the clients can send
        UserGameCommand userCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
        switch (userCommand.getCommandType()){
            case CONNECT -> connect(userCommand, ctx.session);
        }
    }
    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    public void connect(UserGameCommand userInfo, Session session) throws IOException, DataAccessException {
        //Getting GameId and authToken come the UserGameCommand
        int gameId = userInfo.getGameID();
        String authToken = userInfo.getAuthToken();
        //Creating instance to get the info based on the auth token
        DAOAuthDataInterface daoAuth = new MySQLAuthData();
        AuthData info = daoAuth.readAuthToken(authToken);
        //Checking if auth token is not equal to null
        if (info == null){
            ErrorMessage msg = new ErrorMessage("Error: Auth token is null");
            String errorMsg = new Gson().toJson(msg);
            session.getRemote().sendString(errorMsg);
        }
        //Getting the username
        String username = info.username();
        conections.add(gameId,username,session);

        DAOGamesInterface daoGames = new MySQLGames();
        GameData gameData = daoGames.readGame(gameId);
        if (gameData == null){
            ErrorMessage msg = new ErrorMessage("Error: game data is null");
            String errorMsg = new Gson().toJson(msg);
            session.getRemote().sendString(errorMsg);
        } else{
            ChessGame game = gameData.game();

            //Sending LoadGame message
            LoadMessage lgMessage = new LoadMessage(game);
            String message = new Gson().toJson(lgMessage);
            session.getRemote().sendString(message);

            //Notification
            String notificationMsg;
            if(Objects.equals(username, "white")){
                notificationMsg = username + "joined as white";
            } else if (Objects.equals(username, "black")) {
                notificationMsg = username + "joined as black";
            }else{
                notificationMsg = username + "joined as an observer";
            }
            NotificationMessage notification = new NotificationMessage(notificationMsg);
            conections.broadcast(gameId,session,notification);
        }

    }


}
