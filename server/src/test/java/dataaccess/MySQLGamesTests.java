package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MySQLGamesTests {
    public MySQLGames sqlGames;

    //Unit test for User
    @BeforeEach
    public void createSetUp(){
        try {
            sqlGames = new MySQLGames();
            sqlGames.clearGames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void clearTest() throws DataAccessException {
        assertDoesNotThrow(()->
                sqlGames.createGame("Game1"));
                sqlGames.clearGames();
    }


    @Test
    public void createGame(){
        String gameNameTest = "GameTest1";
        assertDoesNotThrow(()->
                sqlGames.createGame(gameNameTest));
    }
    @Test
    public void createGameFail() {
        String gameNameTest = null;
        Assertions.assertThrows(DataAccessException.class, () -> {
            sqlGames.createGame(gameNameTest);
        });
    }


    @Test
    public void readGameTest(){
        assertDoesNotThrow(()-> {
                    GameData testGame = sqlGames.createGame("KevinGame");
                    GameData readResponse = sqlGames.readGame(testGame.gameID());
                    assertEquals("KevinGame",readResponse.gameName());
                });
    }

    @Test
    public void readGameFail(){
        int gameId = -1;
        Assertions.assertThrows(DataAccessException.class, () -> {
            sqlGames.readGame(gameId);
        });
    }

    @Test
    public void readAllGamesTest(){
        assertDoesNotThrow(()-> {
            Collection<GameData> myList = new ArrayList<>();
            GameData testGame1 = sqlGames.createGame("Game1");
            GameData testGame2 = sqlGames.createGame("Game2");
            myList.add(testGame1);
            myList.add(testGame2);
            Collection<GameData> readResponse = sqlGames.readAllGames();
            assertEquals(myList,readResponse);
        });
    }

    @Test
    public  void readAllGamesTestFail(){
        assertDoesNotThrow(()-> {
            Collection<GameData> myList = new ArrayList<>();
            Collection<GameData> readResponse = sqlGames.readAllGames();
            assertEquals(myList,readResponse);
        });
    }



    @Test
    public void updateGameTest() {
        assertDoesNotThrow(()-> {
            GameData oldGame = sqlGames.createGame("OldGame");
            GameData newGame = new GameData(oldGame.gameID(),"Kevin","Alondra","NewGame",new ChessGame());
            GameData updatedGame = sqlGames.updateGame(newGame);
            GameData testGame = sqlGames.readGame(newGame.gameID());

            assertEquals(updatedGame,testGame);
        });
    }

    @Test void updateGameTestFail(){
        Assertions.assertThrows(DataAccessException.class, () -> {
            sqlGames.updateGame(null);
        });
    }
}
