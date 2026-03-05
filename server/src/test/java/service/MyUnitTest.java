package service;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import server.Server;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyUnitTest {

    private static TestUser existingUserTest;
    private static TestUser newUserTest;
    private static TestCreateRequest createRequestTest;
    private static TestServerFacade serverFacadeTest;
    private static Server serverTest;
    private String existingAuthTest;

    // ### TESTING SETUP/CLEANUP ###

    @AfterAll
    static void stopServer() {
        serverTest.stop();
    }

    @BeforeAll
    public static void initTest() {
        serverTest = new Server();
        var port = serverTest.run(0);
        serverFacadeTest = new TestServerFacade("localhost", Integer.toString(port));
        existingUserTest = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");
        newUserTest = new TestUser("NewUser", "newUserPassword", "nu@mail.com");
        createRequestTest = new TestCreateRequest("testGame");
    }

    @BeforeEach
    public void setup() {
        serverFacadeTest.clear();

        //one user already logged in
        TestAuthResult regResult = serverFacadeTest.register(existingUserTest);
        existingAuthTest = regResult.getAuthToken();
    }



    //My UNIT TESTS

    //REGISTER Postive
    @Test
    @DisplayName("Register Success")
    public void registerSuccess(){
        //submit register request
        TestAuthResult registerResult = serverFacadeTest.register(newUserTest);
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
        TestAuthResult registerResult = serverFacadeTest.register(incompleteUser);
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
        TestResult logoutResult = serverFacadeTest.logout(existingAuthTest);
    }
    //logout NEGATIVE (Unauthorized)
    @Test
    @DisplayName("logout Fail")
    public void logoutFail(){
        TestResult logoutResult = serverFacadeTest.logout(null);
        Assertions.assertEquals("Error: unauthorized",logoutResult.getMessage());
    }


    //Create game POSITIVE
    @Test
    @DisplayName("CreateGame Success")
    public void createGameSuccess(){
        TestCreateResult createResult = serverFacadeTest.createGame(createRequestTest, existingAuthTest);
        Assertions.assertNotNull(createResult.getGameID());
    }
    //Create Game NEGATIVE
    @Test
    @DisplayName("CreateGame Success")
    public void createGameFail(){
        TestCreateResult createResult = serverFacadeTest.createGame(createRequestTest, null);
        Assertions.assertNull(createResult.getGameID());
    }

    //List Games POSITIVE
    @Test
    @DisplayName("ListGames Success")
    public void listGamesSuccess(){
        TestAuthResult userLogin = serverFacadeTest.login(existingUserTest);
        TestListResult listResult = serverFacadeTest.listGames(userLogin.getAuthToken());
        Assertions.assertNotNull(listResult);

    }

    //List Games NEGATIVE
    @Test
    @DisplayName("ListGames Fail")
    public void listGamesFail(){
        TestListResult listResult = serverFacadeTest.listGames(null);
        Assertions.assertEquals("Error: Unauthorized",listResult.getMessage());

    }


    //Join Game
    @Test
    @DisplayName("Join Game Success")
    public void joinGameSuccess() {
        //create game
        TestCreateResult createResult = serverFacadeTest.createGame(createRequestTest, existingAuthTest);
        //join as Black
        TestJoinRequest joinRequest = new TestJoinRequest(ChessGame.TeamColor.BLACK, createResult.getGameID());

        //try join
        TestResult joinResult = serverFacadeTest.joinPlayer(joinRequest, existingAuthTest);

        TestListResult listResult = serverFacadeTest.listGames(existingAuthTest);

        Assertions.assertNotNull(listResult.getGames(), "List result did not contain games");
    }

    //Join Game FAIL
    @Test
    @DisplayName("Join Game Fail")
    public void joinGameNegative(){
        //join as Black
        TestJoinRequest joinRequest = new TestJoinRequest(ChessGame.TeamColor.BLACK, null);
        TestResult joinResult = serverFacadeTest.joinPlayer(joinRequest,null);
        Assertions.assertEquals("Error: Unauthorized",joinResult.getMessage());
    }

    //Clear SUCCESS
    @Test
    @DisplayName("Clear function")
    public void clearSuccess(){
        //create filler games
        serverFacadeTest.createGame(new TestCreateRequest("Test1"), existingAuthTest);
        serverFacadeTest.createGame(new TestCreateRequest("Test2"), existingAuthTest);

        //do clear
        TestResult clearResult = serverFacadeTest.clear();
        TestAuthResult authData = serverFacadeTest.register(newUserTest);
        TestListResult list = serverFacadeTest.listGames(authData.getAuthToken());

        //check listResult
        Assertions.assertNotNull(list.getGames(), "List result did not contain an empty list of games");
        Assertions.assertEquals(0, list.getGames().length, "did not return 0 games after clear");
    }


}



