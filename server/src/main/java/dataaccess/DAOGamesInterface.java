package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public interface DAOGamesInterface {
    public GameData createGame(String gameName) throws DataAccessException;
    public void clearGames() throws DataAccessException;
    public GameData readGame(int gameID) throws  DataAccessException;
    public Collection<GameData> readAllGames() throws DataAccessException;
    public GameData updateGame(GameData newGameData) throws DataAccessException;
}
