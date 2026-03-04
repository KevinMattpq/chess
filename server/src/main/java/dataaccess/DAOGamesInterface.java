package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public interface DAOGamesInterface {
    public GameData createGame(String gameName);
    public void clearGames();
    public GameData readGame(int gameID);
    public Collection<GameData> readAllGames();
    public GameData updateGame(GameData newGameData);
}
