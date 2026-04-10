package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand2 extends UserGameCommand{
    public MakeMoveCommand2(String authToken, Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
    }
}
