package chess;

import java.util.ArrayList;
import java.util.Collection;

public class calcBishop extends pieceMovesCalculator {

    //moveChecker FUNCTION - I used this one for all of the possible moves (topRight,topLeft,bottomRight,bottomLeft)
    public void moveChecker (Collection listOfMoves,ChessBoard board,ChessPosition currentPosition, ChessPosition positionX, int row, int col){
        //Possible New Position
        ChessPiece availableSpotX = board.getPiece(positionX);


        while(availableSpotX == null) {
            //Possible Move
            ChessMove pmoveX = new ChessMove(currentPosition, positionX, null);
            //Adding it to the final list
            listOfMoves.add(pmoveX);

            //Update Section
            //TOP RIGHT
            if (row == 1 && col == 1) {
                positionX = new ChessPosition(positionX.getRow() + 1, positionX.getColumn() + 1);
                if (isInside(positionX)) {
                    availableSpotX = board.getPiece(positionX);
                } else {
                    break;
                }
            }
            //TOP LEFT
            if(row == 1 && col == -1){
                positionX = new ChessPosition(positionX.getRow() + 1, positionX.getColumn() - 1);
                if (isInside(positionX)) {
                    availableSpotX = board.getPiece(positionX);
                } else {
                    break;
                }
            }
            //BOTTOM RIGHT
            if(row == -1 && col == 1){
                positionX = new ChessPosition(positionX.getRow()-1,positionX.getColumn()+1);
                if (isInside(positionX)) {
                    availableSpotX = board.getPiece(positionX);
                } else {
                    break;
                }
            }
            //BOTTOM LEFT
            if(row == -1 && col == -1){
                positionX = new ChessPosition(positionX.getRow()-1,positionX.getColumn()-1);
                if (isInside(positionX)) {
                    availableSpotX = board.getPiece(positionX);
                } else {
                    break;
                }
            }

        }
            //IN CASE A PIECE IS BLOCKING
            if (availableSpotX != null && availableSpotX.getTeamColor() != board.getPiece(currentPosition).getTeamColor()){
                ChessMove pmoveX = new ChessMove(currentPosition,positionX,null);
                listOfMoves.add(pmoveX);
            }
    }

    //Checking that position is inside board
    public boolean isInside(ChessPosition position){
        if(position.getRow() > 0 && position.getRow() < 9 && position.getColumn() > 0 && position.getColumn() < 9 ){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position){
        //Final List
        Collection finalList = new ArrayList();

        //Variable that has the position TOP RIGHT from bishop
        ChessPosition topRight = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        //Variable that has the position TOP LEFT from bishop
        ChessPosition topLeft = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        //Variable that has the position BOTTOM RIGHT from bishop
        ChessPosition bottomRight = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        //Variable that has the position BOTTOM LEFT from bishop
        ChessPosition bottomLeft = new ChessPosition(position.getRow()-1, position.getColumn()-1);

        //TOP RIGHT
        //Checkins is inside board
        if (isInside(topRight)){
            moveChecker(finalList,board,position,topRight,1,1);
        }

        //TOP LEFT
        if(isInside(topLeft)){
            moveChecker(finalList,board,position,topLeft,1,-1);
        }

        //BOTTOM RIGHT
        if(isInside(bottomRight)){
            moveChecker(finalList,board,position,bottomRight,-1,1);
        }

        //BOTTOM LEFT
        if (isInside(bottomLeft)){
            moveChecker(finalList,board,position,bottomLeft,-1,-1);
        }
        return finalList;
    }
}
