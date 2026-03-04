package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.Random;

public class DataAccessGames implements DAOGamesInterface {
    private HashMap<Integer, GameData> listOfGames = new HashMap<>();


    @Override
    public GameData createGame(String userGameName) {
        String gameName = userGameName;
        ChessGame newGame = new ChessGame();
        int gameId = Math.abs(new Random().nextInt());
        listOfGames.put(gameId,new GameData(gameId , null,null,gameName, newGame));
        return listOfGames.get(gameId);
    }

    @Override
    //Clear Games
    public void clearGames(){
        listOfGames.clear();
    }


    @Override
    public GameData readGame(int gameID) {
        if(listOfGames.containsKey(gameID)){
            return listOfGames.get(gameID);
        }
        return null;
    }



}
