package chess;

import java.util.ArrayList;
import java.util.Collection;

public class calcKnight extends pieceMovesCalculator {
    //moveChecker FUNCTION - I used this one for all of the possible moves (topRight,topLeft,bottomRight,bottomLeft)
    public void moveChecker(Collection listOfMoves, ChessBoard board, ChessPosition currentPosition, ChessPosition positionX, int row, int col) {
        //Possible New Position
        ChessPiece availableSpotX = board.getPiece(positionX);

        if (availableSpotX == null) {
            //Possible Move
            ChessMove pmoveX = new ChessMove(currentPosition, positionX, null);
            //Adding it to the final list
            listOfMoves.add(pmoveX);

            //Update Section
            //TOP RIGHT
            if (row == 2 && col == 1) {
                positionX = new ChessPosition(positionX.getRow() + 2, positionX.getColumn() + 1);
            }
            //TOP LEFT
            if (row == 2 && col == -1) {
                positionX = new ChessPosition(positionX.getRow() + 2, positionX.getColumn() - 1);
            }

            //RIGHT-UP
            if (row == 1 && col == 2) {
                positionX = new ChessPosition(positionX.getRow() + 1, positionX.getColumn() + 2);
            }
            //RIGHT-DOWN
            if (row == -1 && col == 2) {
                positionX = new ChessPosition(positionX.getRow() -1, positionX.getColumn() + 2);
            }
            //LEFT-UP
            if (row == 1 && col == -2) {
                positionX = new ChessPosition(positionX.getRow() + 1, positionX.getColumn() - 2);
            }
            //lEFT-DOWN
            if (row == -1 && col == -2) {
                positionX = new ChessPosition(positionX.getRow() -1, positionX.getColumn() - 2);
            }
            //BOTTOM RIGHT
            if (row == -2 && col == 1) {
                positionX = new ChessPosition(positionX.getRow() - 2, positionX.getColumn() + 1);
            }
            //BOTTOM LEFT
            if (row == -2 && col == -1) {
                positionX = new ChessPosition(positionX.getRow() - 2, positionX.getColumn() - 1);
            }
        }

        //IN CASE A PIECE IS BLOCKING
        if (availableSpotX != null && availableSpotX.getTeamColor() != board.getPiece(currentPosition).getTeamColor()) {
            ChessMove pmoveXK = new ChessMove(currentPosition, positionX, null);
            listOfMoves.add(pmoveXK);
        }
    }

    //Checking that position is inside board
    public boolean isInside(ChessPosition position) {
        if (position.getRow() > 0 && position.getRow() < 9 && position.getColumn() > 0 && position.getColumn() < 9) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position){
        //Final List
        Collection finalList = new ArrayList();


        //Variable that has the position TOP RIGHT
        ChessPosition topRight = new ChessPosition(position.getRow()+2, position.getColumn()+1);
        //Variable that has the position TOP LEFT
        ChessPosition topLeft = new ChessPosition(position.getRow()+2, position.getColumn()-1);
        //Variable that has the position  RIGHT-UP
        ChessPosition rightUp = new ChessPosition(position.getRow()+1, position.getColumn()+2);
        //Variable that has the position  RIGHT-DOWN
        ChessPosition rightDown = new ChessPosition(position.getRow()-1, position.getColumn()+2);
        //Variable that has the position  LEFT UP
        ChessPosition leftUp = new ChessPosition(position.getRow()+1, position.getColumn()-2);
        //Variable that has the position  LEFT-DOWN
        ChessPosition leftDown = new ChessPosition(position.getRow()-1, position.getColumn()-2);
        //Variable that has the position BOTTOM RIGHT
        ChessPosition bottomRight = new ChessPosition(position.getRow()-2, position.getColumn()+1);
        //Variable that has the position BOTTOM LEFT
        ChessPosition bottomLeft = new ChessPosition(position.getRow()-2, position.getColumn()-1);


        //TOP RIGHT
        //Checkins is inside board
        if (isInside(topRight)){
            moveChecker(finalList,board,position,topRight,2,1);
        }
        //TOP LEFT
        if (isInside(topLeft)){
            moveChecker(finalList,board,position,topLeft,2,-1);
        }


        //TOP RIGHT UP
        if(isInside(rightUp)){
            moveChecker(finalList,board,position,rightUp,1,2);
        }

        //RIGHT DOWN
        if(isInside(rightDown)){
            moveChecker(finalList,board,position,rightDown,-1,2);
        }

        // LEFT UP
        if(isInside(leftUp)){
            moveChecker(finalList,board,position,leftUp,1,-2);
        }

        // LEFT DOWN
        if (isInside(leftDown)){
            moveChecker(finalList,board,position,leftDown,-1,-2);
        }

        //BOTTOM RIGHT
        if(isInside(bottomRight)){
            moveChecker(finalList,board,position,bottomRight,-2,1);
        }
        //BOTTOM LEFT
        if (isInside(bottomLeft)){
            moveChecker(finalList,board,position,bottomLeft,-2,-1);
        }
        return finalList;
    }
}
