package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static dataaccess.DatabaseManager.executeUpdate;

public class MySQLGames implements DAOGamesInterface{
    public MySQLGames() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }


    @Override
    public GameData createGame(String userGameName) throws DataAccessException {
        if(userGameName == null){
            throw new DataAccessException("Game name is required");
        }
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
        if(gameID == 0 || gameID <= 0){
            throw new DataAccessException("ID must be greater than 0");
        }

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
        Collection <GameData> listOfGames = new ArrayList<>();
        executeUpdate(statement);
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                     while(rs.next()){
                        int gameId = rs.getInt("gameId");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String game = rs.getString("game");
                        ChessGame gameChess = new Gson().fromJson(game,ChessGame.class);
                        GameData finalGame = new GameData(gameId,whiteUsername,blackUsername,gameName,gameChess);
                        listOfGames.add(finalGame);
                    }
                }
            }
            return listOfGames;
        } catch (Exception e) {
            throw new DataAccessException("Can't read from database",null);
        }
    }

    @Override
    public GameData updateGame(GameData newGameData) throws DataAccessException {
        if(newGameData == null){
            throw new DataAccessException("Game Data can not be null");
        }
        var statement = "UPDATE listOfGames SET whiteUsername=?, blackUsername=?, gameName=?, game=?";
        executeUpdate(statement,newGameData.whiteUsername(),newGameData.blackUsername(),
                newGameData.gameName(),new Gson().toJson(newGameData.game()));
        return newGameData;
    }

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
