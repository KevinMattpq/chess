package service;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import server.Server;

import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.util.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyUnitTest {

    private static TestUser existingUser;
    private static TestUser newUser;
    private static TestCreateRequest createRequest;
    private static TestServerFacade serverFacade;
    private static Server server;
    private String existingAuth;

    // ### TESTING SETUP/CLEANUP ###

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new TestServerFacade("localhost", Integer.toString(port));
        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");
        newUser = new TestUser("NewUser", "newUserPassword", "nu@mail.com");
        createRequest = new TestCreateRequest("testGame");
    }

    @BeforeEach
    public void setup() {
        serverFacade.clear();

        //one user already logged in
        TestAuthResult regResult = serverFacade.register(existingUser);
        existingAuth = regResult.getAuthToken();
    }



    //My UNIT TESTS

    //REGISTER Postive
    @Test
    @DisplayName("Register Success")
    public void registerSuccess(){
        //submit register request
        TestAuthResult registerResult = serverFacade.register(newUser);
        Assertions.assertEquals(registerResult.getUsername(), registerResult.getUsername(),
                "Response did not have the same username as was registered");
        Assertions.assertNotNull(registerResult.getAuthToken(), "Response did not contain an authentication string");
    }
    //Register NEGATIVE (No username provided)
    @Test
    @DisplayName("Register Fail")
    public void registerFail(){
        TestUser incompleteUser = new TestUser(null,"passwordTest","email@test.com");
        //submit register request
        TestAuthResult registerResult = serverFacade.register(incompleteUser);
        Assertions.assertNull(registerResult.getUsername());
    }

    //Login POSITIVE
    @Test
    @DisplayName("Login Success")
    public void loginSuccess(){
        TestUser completeUser = new TestUser("username","PasswordTest");
        Assertions.assertEquals(completeUser.getUsername(), completeUser.getUsername());
    }

    //Login NEGATIVE
    @Test
    @DisplayName("Login Fail")
    public void loginFail(){
        TestUser incompleteUser = new TestUser("Kevin",null);
        Assertions.assertNull(incompleteUser.getPassword());
    }

    //logout POSITIVE
    @Test
    @DisplayName("logout Success")
    public void logoutSuccess(){
        TestResult logoutResult = serverFacade.logout(existingAuth);
    }
    //logout NEGATIVE (Unauthorized)
    @Test
    @DisplayName("logout Fail")
    public void logoutFail(){
        TestResult logoutResult = serverFacade.logout(null);
        Assertions.assertEquals("Error: unauthorized",logoutResult.getMessage());
    }


    //Create game POSITIVE
    @Test
    @DisplayName("CreateGame Success")
    public void createGameSuccess(){
        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);
        Assertions.assertNotNull(createResult.getGameID());
    }
    //Create Game NEGATIVE
    @Test
    @DisplayName("CreateGame Success")
    public void createGameFail(){
        TestCreateResult createResult = serverFacade.createGame(createRequest, null);
        Assertions.assertNull(createResult.getGameID());
    }

    //List Games POSITIVE
    @Test
    @DisplayName("ListGames Success")
    public void listGamesSuccess(){
        TestAuthResult userLogin = serverFacade.login(existingUser);
        TestListResult listResult = serverFacade.listGames(userLogin.getAuthToken());
        Assertions.assertNotNull(listResult);

    }

    //List Games NEGATIVE
    @Test
    @DisplayName("ListGames Fail")
    public void listGamesFail(){
        TestListResult listResult = serverFacade.listGames(null);
        Assertions.assertEquals("Error: Unauthorized",listResult.getMessage());

    }


    //Join Game
    @Test
    @DisplayName("Join Game Success")
    public void joinGameSuccess() {
        //create game
        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);
        //join as Black
        TestJoinRequest joinRequest = new TestJoinRequest(ChessGame.TeamColor.BLACK, createResult.getGameID());

        //try join
        TestResult joinResult = serverFacade.joinPlayer(joinRequest, existingAuth);

        TestListResult listResult = serverFacade.listGames(existingAuth);

        Assertions.assertNotNull(listResult.getGames(), "List result did not contain games");
    }

    //Join Game FAIL
    @Test
    @DisplayName("Join Game Fail")
    public void joinGameNegative(){
        //join as Black
        TestJoinRequest joinRequest = new TestJoinRequest(ChessGame.TeamColor.BLACK, null);
        TestResult joinResult = serverFacade.joinPlayer(joinRequest,null);
        Assertions.assertEquals("Error: Unauthorized",joinResult.getMessage());
    }

    //Clear SUCCESS
    @Test
    @DisplayName("Clear function")
    public void clearSuccess(){
        //create filler games
        serverFacade.createGame(new TestCreateRequest("Test1"), existingAuth);
        serverFacade.createGame(new TestCreateRequest("Test2"), existingAuth);

        //do clear
        TestResult clearResult = serverFacade.clear();
        TestAuthResult authData = serverFacade.register(newUser);
        TestListResult list = serverFacade.listGames(authData.getAuthToken());

        //check listResult
        Assertions.assertNotNull(list.getGames(), "List result did not contain an empty list of games");
        Assertions.assertEquals(0, list.getGames().length, "did not return 0 games after clear");
    }


}



