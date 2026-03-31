package client;

import chess.*;
import networking.ResponseException;

public class ClientMain {
    public static void main(String[] args) throws ResponseException {
        var chessClient = new ChessClient("http://localhost:8080");
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        //Calling my functions
        chessClient.run();
    }
}
