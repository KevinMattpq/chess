package chess;

import java.util.ArrayList;
import java.util.Collection;

public class calcKing extends pieceMovesCalculator {
    //moveChecker FUNCTION - I used this one for all of the possible moves (topRight,topLeft,bottomRight,bottomLeft)
    public void moveChecker (Collection listOfMoves, ChessBoard board, ChessPosition currentPosition, ChessPosition positionX, int row, int col){
        //Possible New Position
        ChessPiece availableSpotX = board.getPiece(positionX);

        if (availableSpotX == null) {
            //Possible Move
            ChessMove pmoveX = new ChessMove(currentPosition, positionX, null);
            //Adding it to the final list
            listOfMoves.add(pmoveX);

            //Update Section
            //TOP
            if (row == 1 && col == 0) {
                positionX = new ChessPosition(positionX.getRow() + 1, positionX.getColumn() + 0);
            }
            //TOP RIGHT
            if (row == 1 && col == 1) {
                positionX = new ChessPosition(positionX.getRow() + 1, positionX.getColumn() + 1);
            }
            //TOP LEFT
            if (row == 1 && col == -1) {
                positionX = new ChessPosition(positionX.getRow() + 1, positionX.getColumn() - 1);
            }
            //BOTTOM
            if (row == -1 && col == 0) {
                positionX = new ChessPosition(positionX.getRow() - 1, positionX.getColumn() + 0);
            }
            //BOTTOM RIGHT
            if (row == -1 && col == 1) {
                positionX = new ChessPosition(positionX.getRow() - 1, positionX.getColumn() + 1);
            }
            //BOTTOM LEFT
            if (row == -1 && col == -1) {
                positionX = new ChessPosition(positionX.getRow() - 1, positionX.getColumn() - 1);
            }
            //RIGHT
            if (row == 0 && col == 1) {
                positionX = new ChessPosition(positionX.getRow() + 0, positionX.getColumn() + 1);
            }
            //LEFT
            if (row == 0 && col == -1) {
                positionX = new ChessPosition(positionX.getRow() + 0, positionX.getColumn() - 1);
            }
        }

        //IN CASE A PIECE IS BLOCKING
        if (availableSpotX != null && availableSpotX.getTeamColor() != board.getPiece(currentPosition).getTeamColor()){
            ChessMove pmoveXK = new ChessMove(currentPosition,positionX,null);
            listOfMoves.add(pmoveXK);
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

        //Variable that has the position TOP
        ChessPosition top = new ChessPosition(position.getRow()+1, position.getColumn()+0);
        //Variable that has the position TOP RIGHT
        ChessPosition topRight = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        //Variable that has the position TOP LEFT
        ChessPosition topLeft = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        //Variable that has the position BOTTOM
        ChessPosition  bottom = new ChessPosition(position.getRow()-1, position.getColumn()+0);
        //Variable that has the position BOTTOM RIGHT
        ChessPosition bottomRight = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        //Variable that has the position BOTTOM LEFT
        ChessPosition bottomLeft = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        //Variable that has the position  RIGHT
        ChessPosition right = new ChessPosition(position.getRow()+0, position.getColumn()+1);
        //Variable that has the position  LEFT
        ChessPosition left = new ChessPosition(position.getRow()+0, position.getColumn()-1);

        //TOP
        //Checkins is inside board
        if (isInside(top)){
            moveChecker(finalList,board,position,top,1,0);
        }
        //TOP RIGHT
        if (isInside(topRight)){
            moveChecker(finalList,board,position,topRight,1,1);
        }

        //TOP LEFT
        if(isInside(topLeft)){
            moveChecker(finalList,board,position,topLeft,1,-1);
        }

        //BOTTOM
        if(isInside(bottom)){
            moveChecker(finalList,board,position,bottom,-1,0);
        }
        //BOTTOM RIGHT
        if(isInside(bottomRight)){
            moveChecker(finalList,board,position,bottomRight,-1,1);
        }

        //BOTTOM LEFT
        if (isInside(bottomLeft)){
            moveChecker(finalList,board,position,bottomLeft,-1,-1);
        }

        //RIGHT
        if(isInside(right)){
            moveChecker(finalList,board,position,right,0,1);
        }

        //LEFT
        if (isInside(left)){
            moveChecker(finalList,board,position,left,0,-1);
        }
        return finalList;
    }
}
