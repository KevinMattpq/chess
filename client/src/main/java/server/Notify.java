package server;


import websocket.messages.ErrorMessage;
import websocket.messages.LoadMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public interface Notify {
    void notifyError(ErrorMessage message);
    void  notifyNotification(NotificationMessage message);
    void notifyLoadGame(LoadMessage game);
}
