package chess;

import java.util.ArrayList;
import java.util.Collection;

public class calcBishop extends pieceMovesCalculator {

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
            ChessPiece availableSpotTR = board.getPiece(topRight);

            while (availableSpotTR == null && topRight.getRow() > 0 && topRight.getRow() < 9 && topRight.getColumn() < 9 && topRight.getColumn() > 0 ){
                //Possible move
                ChessMove pmoveTR = new ChessMove(position,topRight,null);
                //Adding it to the final list
                finalList.add(pmoveTR);

                //Updating Process
                topRight = new ChessPosition(topRight.getRow()+1, topRight.getColumn()+1 );
                if(isInside(topRight)){
                    availableSpotTR = board.getPiece(topRight);
                }else{
                    break;
                }

            }
            //Checking if there is a piece blocking (Top right)
            if (availableSpotTR != null && availableSpotTR.getTeamColor() != board.getPiece(position).getTeamColor()){
                ChessMove pmoveTR = new ChessMove(position,topRight,null);
                finalList.add(pmoveTR);
            }
        }
        //TOP LEFT
        if(isInside(topLeft)){
            ChessPiece availableSpotTL = board.getPiece(topLeft);

            while (availableSpotTL == null && topLeft.getRow() > 0 && topLeft.getRow() < 9 && topLeft.getColumn() < 9 && topLeft.getColumn() > 0 ){
                //Possible move
                ChessMove pmoveTL = new ChessMove(position,topLeft,null);
                //Adding it to the final list
                finalList.add(pmoveTL);

                //Updating Process
                topLeft = new ChessPosition(topLeft.getRow()+1, topLeft.getColumn()-1 );
                if(isInside(topLeft)){
                    availableSpotTL = board.getPiece(topLeft);
                }else{
                    break;
                }
            }
            //Checking if there is a piece blocking (Top LEFT)
            if (availableSpotTL != null && availableSpotTL.getTeamColor() != board.getPiece(position).getTeamColor()){
                ChessMove pmoveTL = new ChessMove(position,topLeft,null);
                finalList.add(pmoveTL);
            }
        }

        //BOTTOM RIGHT
        if(isInside(bottomRight)){
            ChessPiece availableSpotBR = board.getPiece(bottomRight);

            while (availableSpotBR == null && bottomRight.getRow() > 0 && bottomRight.getRow() < 9 && bottomRight.getColumn() < 9 && bottomRight.getColumn() > 0 ){
                //Possible move
                ChessMove pmoveBR = new ChessMove(position,bottomRight,null);
                //Adding it to the final list
                finalList.add(pmoveBR);
                //Updating Process
                bottomRight = new ChessPosition(bottomRight.getRow()-1, bottomRight.getColumn()+1 );
                if(isInside(bottomRight)){
                    availableSpotBR = board.getPiece(bottomRight);
                }else{
                    break;
                }
            }
            //Checking if there is a piece blocking (BottomRight)
            if (availableSpotBR != null && availableSpotBR.getTeamColor() != board.getPiece(position).getTeamColor()){
                ChessMove pmoveBR = new ChessMove(position,bottomRight,null);
                finalList.add(pmoveBR);
            }
        }

        //BOTTOM LEFT
        if (isInside(bottomLeft)){
            ChessPiece availableSpotBL = board.getPiece(bottomLeft);

            while (availableSpotBL == null && bottomLeft.getRow() > 0 && bottomLeft.getRow() < 9 && bottomLeft.getColumn() < 9 && bottomLeft.getColumn() > 0 ){
                //Possible move
                ChessMove pmoveBL = new ChessMove(position,bottomLeft,null);
                //Adding it to the final list
                finalList.add(pmoveBL);

                //Updating Process
                bottomLeft = new ChessPosition(bottomLeft.getRow()-1, bottomLeft.getColumn()-1 );
                if(isInside(bottomLeft)){
                    availableSpotBL = board.getPiece(bottomLeft);
                }else{
                    break;
                }
            }
            //Checking if there is a piece blocking (Top right)
            if (availableSpotBL != null && availableSpotBL.getTeamColor() != board.getPiece(position).getTeamColor()){
                ChessMove pmoveBL = new ChessMove(position,bottomLeft,null);
                finalList.add(pmoveBL);
            }
        };
        return finalList;
    }
}
