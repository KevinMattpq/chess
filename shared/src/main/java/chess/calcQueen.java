package chess;

import java.util.ArrayList;
import java.util.Collection;

public class calcQueen extends pieceMovesCalculator {

    //moveChecker FUNCTION - I used this one for all of the possible moves (top,topRight,topLeft,bottom,bottomRight,bottomLeft,right, left)
    public void moveChecker (Collection listOfMoves,ChessBoard board, ChessPiece availableSpotX,ChessPosition currentPosition, ChessPosition positionX, int row, int col){

        while(availableSpotX == null) {
            //Possible Move
            ChessMove pmoveX = new ChessMove(currentPosition, positionX, null);
            //Adding it to the final list
            listOfMoves.add(pmoveX);

            //Update Section
            //TOP
            if (row == 1 && col == 0) {
                positionX = new ChessPosition(positionX.getRow() + 1, positionX.getColumn() + 0);
                if (isInside(positionX)) {
                    availableSpotX = board.getPiece(positionX);
                } else {
                    break;
                }
            }

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

            //BOTTOM
            if(row == -1 && col == 0){
                positionX = new ChessPosition(positionX.getRow() -1, positionX.getColumn() + 0);
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

            //RIGHT
            if(row == 0 && col == 1){
                positionX = new ChessPosition(positionX.getRow()+0,positionX.getColumn()+1);
                if (isInside(positionX)) {
                    availableSpotX = board.getPiece(positionX);
                } else {
                    break;
                }
            }
            //LEFT
            if(row == 0 && col == -1){
                positionX = new ChessPosition(positionX.getRow()+0,positionX.getColumn()-1);
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
        //Variable that has the position TOP
        ChessPosition top = new ChessPosition(position.getRow()+1, position.getColumn()+0);
        //Variable that has the position BOTTOM
        ChessPosition  bottom = new ChessPosition(position.getRow()-1, position.getColumn()+0);
        //Variable that has the position  RIGHT
        ChessPosition right = new ChessPosition(position.getRow()+0, position.getColumn()+1);
        //Variable that has the position  LEFT
        ChessPosition left = new ChessPosition(position.getRow()+0, position.getColumn()-1);


        //TOP
        //Checkins is inside board
        if (isInside(top)){

            ChessPiece availableSpotTR = board.getPiece(top);
            moveChecker(finalList,board, availableSpotTR,position,top,1,0);
        }

        //TOP RIGHT
        //Checkins is inside board
        if (isInside(topRight)){

            ChessPiece availableSpotTR = board.getPiece(topRight);
            moveChecker(finalList,board, availableSpotTR,position,topRight,1,1);
        }

        //TOP LEFT
        if(isInside(topLeft)){
            ChessPiece availableSpotTL = board.getPiece(topLeft);
            moveChecker(finalList,board, availableSpotTL,position,topLeft,1,-1);
        }

        //BOTTOM
        if(isInside(bottom)){
            ChessPiece availableSpotTL = board.getPiece(bottom);
            moveChecker(finalList,board, availableSpotTL,position,bottom,-1,0);
        }

        //BOTTOM RIGHT
        if(isInside(bottomRight)){
            ChessPiece availableSpotBR = board.getPiece(bottomRight);
            moveChecker(finalList,board, availableSpotBR,position,bottomRight,-1,1);
        }

        //BOTTOM LEFT
        if (isInside(bottomLeft)){
            ChessPiece availableSpotBL = board.getPiece(bottomLeft);
            moveChecker(finalList,board, availableSpotBL,position,bottomLeft,-1,-1);
        }

        //RIGHT
        if(isInside(right)){
            ChessPiece availableSpotBR = board.getPiece(right);
            moveChecker(finalList,board, availableSpotBR,position,right,0,1);
        }

        //LEFT
        if (isInside(left)){
            ChessPiece availableSpotBL = board.getPiece(left);
            moveChecker(finalList,board, availableSpotBL,position,left,0,-1);
        }

        return finalList;
    }
}
