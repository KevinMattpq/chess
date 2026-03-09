package chess;

import java.util.Collection;

public class HelperFunctions {
    public static boolean blockingPieceCheck(ChessPiece availableSpotX, ChessPosition currentPosition, ChessBoard board){
        if (availableSpotX != null && availableSpotX.getTeamColor() != board.getPiece(currentPosition).getTeamColor()){
            return true;
        }
        return false;
    }
}
