package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences;

public class BoardPrinter {

    public String printHeader(ChessGame.TeamColor color){
        StringBuilder row = new StringBuilder();
        if(color == ChessGame.TeamColor.WHITE){
            row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY).append("   ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" a ");
            row.append(" b ");
            row.append(" c ");
            row.append(" d ");
            row.append(" e ");
            row.append(" f ");
            row.append(" g ");
            row.append(" h ");
            row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY).append("   ");
        }else {
            row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY).append("   ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" h ");
            row.append(" g ");
            row.append(" f ");
            row.append(" e ");
            row.append(" d ");
            row.append(" c ");
            row.append(" b ");
            row.append(" a ");
            row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY).append("   ");
        }
        row.append(EscapeSequences.RESET_BG_COLOR);
        return row.toString();
    }

    public String printRow(ChessBoard board, ChessGame.TeamColor color, Integer rowNumUser){
        String rowNum = rowNumUser.toString();
        //This will hold the final row
        StringBuilder row = new StringBuilder();
        //Printing the Row Number
        row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY).append(" "+ rowNum +" ");
        row.append(EscapeSequences.RESET_BG_COLOR);


        //Checking the color to determine the orientation
        if(color == ChessGame.TeamColor.WHITE){
            for(int col = 1;col < 9; col++){
                ChessPosition position = new ChessPosition(rowNumUser,col);
                ChessPiece piece = board.getPiece(position);
                if ((rowNumUser + col) % 2 == 0){
                    if(piece == null ){
                        row.append(EscapeSequences.SET_BG_COLOR_WHITE).append("   ");
                    }else{
                        String pieceLetter = pieceLetter(piece);
                        row.append(EscapeSequences.SET_BG_COLOR_WHITE).append(" ");
                        row.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        row.append(pieceLetter);
                        row.append(" ");
                        row.append(EscapeSequences.RESET_TEXT_COLOR);
                    }
                }else{
                    if(piece == null){
                        row.append(EscapeSequences.SET_BG_COLOR_BLACK).append("   ");
                    }else{
                        String pieceLetter = pieceLetter(piece);
                        row.append(EscapeSequences.SET_BG_COLOR_BLACK).append(" ");
                        row.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        row.append(pieceLetter);
                        row.append(" ");
                        row.append(EscapeSequences.RESET_TEXT_COLOR);
                    }
                }
            }
        }else{
            for(int col = 8;col > 0; col--){
                ChessPosition position = new ChessPosition(rowNumUser,col);
                ChessPiece piece = board.getPiece(position);
                if ((rowNumUser + col) % 2 == 0){
                    if(piece == null ){
                        row.append(EscapeSequences.SET_BG_COLOR_WHITE).append("   ");
                    }else{
                        String pieceLetter = pieceLetter(piece);
                        row.append(EscapeSequences.SET_BG_COLOR_WHITE).append(" ");
                        row.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        row.append(pieceLetter);
                        row.append(" ");
                        row.append(EscapeSequences.RESET_TEXT_COLOR);
                    }
                }else{
                    if(piece == null){
                        row.append(EscapeSequences.SET_BG_COLOR_BLACK).append("   ");
                    }else{
                        String pieceLetter = pieceLetter(piece);
                        row.append(EscapeSequences.SET_BG_COLOR_BLACK).append(" ");
                        row.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        row.append(pieceLetter);
                        row.append(" ");
                        row.append(EscapeSequences.RESET_TEXT_COLOR);
                    }
                }
            }
        }

            row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY).append(" "+rowNum+ " ");
            row.append(EscapeSequences.RESET_BG_COLOR);
        return row.toString();
        }

    public String pieceLetter(ChessPiece piece){
        String letter = "";
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            letter += EscapeSequences.SET_TEXT_COLOR_RED;
        }else{
            letter += EscapeSequences.SET_TEXT_COLOR_BLUE;

        }
        if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
            letter += "P";
        }
        if(piece.getPieceType() == ChessPiece.PieceType.ROOK){
            letter +=  "R";
        }
        if(piece.getPieceType() == ChessPiece.PieceType.BISHOP){
            letter +=  "B";
        }
        if(piece.getPieceType() == ChessPiece.PieceType.KING){
            letter +=  "K";
        }
        if(piece.getPieceType() == ChessPiece.PieceType.QUEEN){
            letter +=  "Q";
        }
        if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
            letter +=  "N";
        }
        return letter;
    }

    public String draw(ChessBoard board, ChessGame.TeamColor color){
        StringBuilder rowResult = new StringBuilder();
        String  resultHeader = printHeader(color);
        if(color == ChessGame.TeamColor.WHITE){
            for(int i = 8; i>0;i-- ){
                rowResult.append(printRow(board,color,i));
                rowResult.append("\n");
            }
        }else{
            for(int i = 1; i< 9;i++ ){
                rowResult.append(printRow(board,color,i));
                rowResult.append("\n");
            }
        }
        return resultHeader+"\n"+rowResult.toString()+resultHeader;
    }
}




