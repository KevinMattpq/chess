package chess;

import java.util.Collection;

public class HelperFunctions {
    public void blockingPieceCheck(ChessPiece availableSpotX, ChessPosition positionX, ChessPosition currentPosition, ChessBoard board, Collection listOfMoves){
        if (availableSpotX != null && availableSpotX.getTeamColor() != board.getPiece(currentPosition).getTeamColor()){
            ChessMove pmoveX = new ChessMove(currentPosition,positionX,null);
            listOfMoves.add(pmoveX);
        }
    }
}
