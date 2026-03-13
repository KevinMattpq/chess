package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static dataaccess.DatabaseManager.executeUpdate;

public class MySQLGames implements DAOGamesInterface{
    private HashSet<Integer> usedId = new HashSet<>();
    public MySQLGames() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }


    @Override
    public GameData createGame(String userGameName) throws DataAccessException {
        String whiteUsername = null;
        String blackUsername = null;
        ChessGame newGame = new ChessGame();
        var statement = "INSERT INTO listOfGames (whiteUsername, blackUsername, gameName, game) values (?, ?, ?, ?)";
        int gameId = executeUpdate(statement,whiteUsername,blackUsername,userGameName,new Gson().toJson(newGame));
        GameData gameData = new GameData(gameId,whiteUsername,blackUsername,userGameName,newGame);
        return gameData;
    }

    @Override
    public void clearGames() throws DataAccessException{
        var statement = "TRUNCATE TABLE users";
        executeUpdate(statement);
    }

    @Override
    public GameData readGame(int gameID) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameId,whiteUsername,blackUsername,gameName,game FROM listOfGames WHERE gameId = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        //Getting the value of column
                        return readResult(rs);
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
    public GameData updateGame(GameData newGameData) throws DataAccessException {
        var statement = "UPDATE listOfGames SET whiteUsername=?, blackUsername=?, gameName=?, game=?";
        executeUpdate(statement,newGameData.whiteUsername(),newGameData.blackUsername(),newGameData.gameName(),new Gson().toJson(newGameData.game()));
        return newGameData;
    }

//    private int sqlIdGenerator(){
//        int newID = usedId.size() + 1;
//        usedId.add(newID);
//        return newID;
//    }

    private  GameData readResult (ResultSet rs) throws SQLException {
        int id = rs.getInt("gameId");
        String whiteUsename = rs.getString("whiteUsername");
        String blackUsename = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        ChessGame game = new Gson().fromJson(rs.getString("game"),ChessGame.class);
        GameData result = new GameData(id,whiteUsename,blackUsename,gameName,game);
        return result;
    }
}
