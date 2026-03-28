//package client;
//
//import chess.ChessGame;
//import ui.EscapeSequences;
//
//public class BoardPrinter {
//
//    public String printHeader(ChessGame.TeamColor color){
//        StringBuilder row = new StringBuilder();
//        if(color == ChessGame.TeamColor.WHITE){
//            row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
//            row.append("   ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" a ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" b ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" c ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" d ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" e ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" f ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" g ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" h ");
//            row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
//            row.append("   ");
//        }else {
//            row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
//            row.append("   ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" h ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" g ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" f ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" e ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" d ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" c ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" b ");
//            row.append(EscapeSequences.SET_TEXT_BOLD).append(" a ");
//            row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
//            row.append("   ");
//        }
//        return row.toString();
//    }
//
//    public printRow(ChessGame.TeamColor color){
//        StringBuilder row = new StringBuilder();
//        if(color == ChessGame.TeamColor.WHITE){
//            row.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY).append(" 8 ");
//            for(int i = 8;i >= 0; i--){
//            }
//        }
//    }
//
////    public String draw(ChessGame.TeamColor color){
////        printHeader(color);
////
////    }
////
////    public String board(){
////
////    }
//
//}
