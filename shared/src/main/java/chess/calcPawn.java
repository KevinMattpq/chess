package chess;

import java.util.ArrayList;
import java.util.Collection;

public class calcPawn extends pieceMovesCalculator{
    private static final ChessPiece.PieceType[] promotionTypes = {
            ChessPiece.PieceType.QUEEN,
            ChessPiece.PieceType.ROOK,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.KNIGHT
    };

    //moveChecker FUNCTION - I used this one for all of the possible moves (topRight,topLeft,bottomRight,bottomLeft)
    public void moveChecker (Collection listOfMoves, ChessBoard board, ChessPosition currentPosition, int row, int col) {
        ChessPosition positionX = new ChessPosition(currentPosition.getRow()+row, currentPosition.getColumn()+col);
        if (!isInside(positionX)) {
            return;
        }
        ChessPiece availableSpotX = board.getPiece(positionX);
        ChessPiece pawn = board.getPiece(currentPosition);
        ChessGame.TeamColor pawnColor = pawn.getTeamColor();
        int initialRow = pawnColor == ChessGame.TeamColor.WHITE ? 2 : 7;

        if (col==0){
            if (availableSpotX != null) {
                return;
            }
            if(Math.abs(row) == 2 &&
                    (currentPosition.getRow() != initialRow ||
                    board.getPiece(new ChessPosition(currentPosition.getRow()+(row/2), currentPosition.getColumn())) != null)){
                return;
            }
        }else {
            if (availableSpotX == null || availableSpotX.getTeamColor() == board.getPiece(currentPosition).getTeamColor()){
                return;
            }
        }
        int opponentIrow = pawnColor == ChessGame.TeamColor.BLACK ? 2 : 7;
        if(currentPosition.getRow() == opponentIrow){
            for (ChessPiece.PieceType pType : promotionTypes ){
                ChessMove pmoveX = new ChessMove(currentPosition, positionX, pType);
                listOfMoves.add(pmoveX);
            }
        }else {
            ChessMove pmoveX = new ChessMove(currentPosition, positionX, null);
            listOfMoves.add(pmoveX);
        }
    }


    //Checking that position is inside board
    public boolean isInside(ChessPosition position){
        return position.getRow() > 0 && position.getRow() < 9 && position.getColumn() > 0 && position.getColumn() < 9;
    }

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position){
        //Final List
        Collection finalList = new ArrayList();

        ChessPiece pawn = board.getPiece(position);
        ChessGame.TeamColor pawnColor = pawn.getTeamColor();

        int rowMod = pawnColor == ChessGame.TeamColor.WHITE ? +1 : -1;


        //TOP
        moveChecker(finalList,board, position,rowMod,0);
        //TOP2
        moveChecker(finalList,board, position,rowMod*2,0);
        //TOP RIGHT
        moveChecker(finalList,board, position,rowMod,1);
        //TOP LEFT
        moveChecker(finalList,board, position,rowMod,-1);

        return finalList;
    }
}
