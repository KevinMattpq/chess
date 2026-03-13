package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
    public void createGame(){
        String gameNameTest = "GameTest1";
        assertDoesNotThrow(()->
                sqlGames.createGame(gameNameTest));
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
    public void updateGameTest() {
        assertDoesNotThrow(()-> {
            GameData oldGame = sqlGames.createGame("OldGame");
            GameData newGame = new GameData(oldGame.gameID(),"Kevin","Alondra","NewGame",new ChessGame());
            GameData updatedGame = sqlGames.updateGame(newGame);
            GameData testGame = sqlGames.readGame(newGame.gameID());

            assertEquals(updatedGame,testGame);
        });

    }
}
