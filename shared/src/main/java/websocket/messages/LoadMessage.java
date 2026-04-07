package websocket.messages;

import chess.ChessGame;

public class LoadMessage extends ServerMessage{
    ChessGame game;

    public LoadMessage(ServerMessageType type) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
}
