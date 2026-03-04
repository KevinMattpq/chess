package dataaccess;

import model.GameData;

public interface DAOGamesInterface {
    public GameData createGame(String gameName);
    public void clearGames();
    public GameData readGame(int gameID);
    //public GameData readAllGames();
}
