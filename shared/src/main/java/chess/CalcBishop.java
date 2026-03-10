package chess;

import java.util.ArrayList;
import java.util.Collection;

public class CalcBishop extends PieceMovesCalculator {

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

        //Position TOP RIGHT
        ChessPosition topRight = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        //Position TOP LEFT
        ChessPosition topLeft = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        //Position BOTTOM RIGHT
        ChessPosition bottomRight = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        //Position BOTTOM LEFT
        ChessPosition bottomLeft = new ChessPosition(position.getRow()-1, position.getColumn()-1);

        //TOP RIGHT
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
    //moveChecker FUNCTION - I used this one for all of the possible moves (topRight,topLeft,bottomRight,bottomLeft)
    public void moveChecker (Collection listOfMoves,ChessBoard board,ChessPosition currentPosition, ChessPosition positionX, int row, int col){
        //Possible Position
        ChessPiece availableSpotX = board.getPiece(positionX);

        while(availableSpotX == null) {
            //Creating Move
            ChessMove pmoveX = new ChessMove(currentPosition, positionX, null);
            //Adding move to the final list
            listOfMoves.add(pmoveX);

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
            //PIECE IS BLOCKING
            if (availableSpotX != null && availableSpotX.getTeamColor() != board.getPiece(currentPosition).getTeamColor()){
                ChessMove pmoveX = new ChessMove(currentPosition,positionX,null);
                listOfMoves.add(pmoveX);
            }
    }
}
