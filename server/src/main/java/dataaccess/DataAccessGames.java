package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class DataAccessGames implements DAOGamesInterface {
    private HashMap<Integer, GameData> listOfGames = new HashMap<>();
    private HashSet<Integer> usedId = new HashSet<>();


    @Override
    public GameData createGame(String userGameName) {
        String gameName = userGameName;
        ChessGame newGame = new ChessGame();
        int gameId = idGenerator();
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

    @Override
    public Collection<GameData> readAllGames() {
        return listOfGames.values();
    }

    @Override
    public GameData updateGame(GameData newGameData) {
        if(listOfGames.containsKey(newGameData.gameID())){
            listOfGames.replace(newGameData.gameID(),newGameData);
        }
        return null;
    }

    private int idGenerator(){
        int newID = usedId.size() + 1;
        usedId.add(newID);
        return newID;
    }


}
