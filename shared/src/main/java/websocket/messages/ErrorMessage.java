package websocket.messages;

public class ErrorMessage extends ServerMessage{
    private String message;

    public ErrorMessage(ServerMessageType type) {
        super(ServerMessageType.ERROR);
        this.message = message;
    }
}
