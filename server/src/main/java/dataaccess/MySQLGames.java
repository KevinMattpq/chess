package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

import static dataaccess.DatabaseManager.executeUpdate;

public class MySQLGames implements DAOGamesInterface{
    public MySQLGames() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }


    @Override
    public GameData createGame(String userGameName) throws DataAccessException {
        String gameName = userGameName;
        //ChessGame = new ChessGame();
        return null;
    }

    @Override
    public void clearGames() throws DataAccessException{
        var statement = "TRUNCATE TABLE users";
        executeUpdate(statement);
    }

    @Override
    public GameData readGame(int gameID) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameId FROM listOfGames WHERE gameID = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        //Getting the value of column
                        return readGame(rs.getInt("gameID"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Can't read from database");
        }
        return null;
    }

    @Override
    public Collection<GameData> readAllGames() throws DataAccessException {
        var statement = "SELECT * FROM listOfGames";
        executeUpdate(statement);
        return List.of();
    }

    @Override
    public GameData updateGame(GameData newGameData) {
        return null;
    }
}
