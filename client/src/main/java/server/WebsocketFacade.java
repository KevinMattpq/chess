package server;

import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import networking.ResponseException;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;

public class WebsocketFacade extends Endpoint {
    public Session session;
    public Notify notify;
    public WebsocketFacade(Notify notify) throws Exception {
        this.notify = notify;
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                ServerMessage sMessage = new Gson().fromJson(message,ServerMessage.class);
                if(sMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
                    messageNotification(message);
                }
                if(sMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
                    messageErrorMessage(message);
                }
                if(sMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
                    messageLoadGame(message);
                }
                System.out.println(message);
            }
        });
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void messageNotification(String message){
        NotificationMessage newNotification = new Gson().fromJson(message, NotificationMessage.class);
        notify.notifyNotification(newNotification);
    }
    public void messageLoadGame(String message){
        LoadMessage newLoadGameNotification = new Gson().fromJson(message,LoadMessage.class);
        notify.notifyLoadGame(newLoadGameNotification);
    }
    public void messageErrorMessage(String message){
        ErrorMessage newErrorMessage = new Gson().fromJson(message,ErrorMessage.class);
        notify.notifyError(newErrorMessage);
    }

    //COMMANDS
    public void connect(String autToken, int gameId ) throws ResponseException {
        try{
            ConnectCommand command = new ConnectCommand(autToken,gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }catch (IOException e){
            System.out.println("Error while creating CONNECT Command");
        }
    }

    public void leave(String authToken, int gameId) throws IOException {
        try{
            LeaveCommand command = new LeaveCommand(authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }catch (IOException e){
            System.out.println("Error while creating LEAVE command");
        }

    }

    public void resign(String authToken, int gameId) throws IOException {
        try {
            ResignCommand command = new ResignCommand(authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }catch (IOException e){
            System.out.println("Error creating RESIGN command");
        }
    }

    public void makeMove(String authToken, int gameId, ChessMove move){
        try{
            MakeMoveCommand command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE,authToken,gameId,move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }catch (IOException e){
            System.out.println("Error while creating MAKE_MOVE command");
        }
    }



    public void send(String message){
        try{
            session.getBasicRemote().sendText(message);
        }catch (IOException e){
            System.out.println("Error on Send Message");
        }
    }
}
