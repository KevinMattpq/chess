package client;

import chess.ChessGame;
import ui.EscapeSequences;

public class BoardPrinter {

    public String printHeader(ChessGame.TeamColor color){
        StringBuilder row = new StringBuilder();
        if(color == ChessGame.TeamColor.WHITE){
            row.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            row.append("   ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" a ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" b ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" c ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" d ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" e ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" f ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" g ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" h ");
            row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            row.append("   ");
        }else {
            row.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            row.append("   ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" h ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" g ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" f ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" e ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" d ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" c ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" b ");
            row.append(EscapeSequences.SET_TEXT_BOLD).append(" a ");
            row.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            row.append("   ");
        }
        return row.toString();
    }

    public String printRow(ChessGame.TeamColor color, Integer rowNumUser){
        String rowNum = rowNumUser.toString();
        StringBuilder row = new StringBuilder();
            row.append(EscapeSequences.SET_BG_COLOR_DARK_GREY).append(" "+ rowNum +" ");
                if(rowNumUser % 2 == 0){
                    for(int col = 0;col < 8; col++){
                        if (col % 2 == 0){
                            row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY).append("   ");
                        }else{
                            row.append(EscapeSequences.SET_BG_COLOR_BLACK).append("   ");
                        }
                    }
                }else{
                    for(int col = 0;col < 8; col++){
                        if (col % 2 == 0){
                            row.append(EscapeSequences.SET_BG_COLOR_BLACK).append("   ");
                        }else{
                            row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY).append("   ");
                        }
                    }
                }
            row.append(EscapeSequences.SET_BG_COLOR_DARK_GREY).append(" "+rowNum+ " ");
        return row.toString();
        }
    }

//    public String draw(ChessGame.TeamColor color){
//        printHeader(color);
//
//    }

