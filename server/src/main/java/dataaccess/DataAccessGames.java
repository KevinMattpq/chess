package dataaccess;

import model.GameData;

import java.util.HashMap;

public class DataAccessGames implements DAOGamesInterface {
    private HashMap<Integer, GameData> listOfGames = new HashMap<>();

    @Override
    //Clear Games
    public void clearGames(){
        listOfGames.clear();
    }
}
