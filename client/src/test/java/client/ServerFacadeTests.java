package client;

import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import server.service.ResponseException;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade sFTest;
    private static String url;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        url = "http://localhost:"+port;
        sFTest = new ServerFacade(url);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerTest()throws ResponseException{
        sFTest.clear();
        Assertions.assertNotNull( sFTest.register(new UserData("kevin","kevin1","kevin@byu.edu")));
    }

    @Test
    public void registerTestFail()throws ResponseException{
        sFTest.clear();
        UserData userInfo= new UserData(null,"test","test@byu.edu");
        Assertions.assertThrows(ResponseException.class,() -> sFTest.register(userInfo));
    }

    @Test
    public void loginTest() throws ResponseException {
        sFTest.clear();
        sFTest.register(new UserData("kevin","kevin1", "kevin@byu.edu"));
        LoginRequest request = new LoginRequest("kevin","kevin1");
        Assertions.assertDoesNotThrow(()-> sFTest.login(request));
    }

    @Test
    public void loginTestFail() throws ResponseException{
        sFTest.clear();
        sFTest.register(new UserData("kevin","kevin1","kevin@byu.edu"));
        LoginRequest request = new LoginRequest("kevin","kevin2");
        Assertions.assertThrows(ResponseException.class,() -> sFTest.login(request));

    }

    @Test
    public void createGameTest() throws ResponseException{
        sFTest.clear();
        sFTest.register(new UserData("kevin","kevin1","kevin@byu.edu"));
        AuthData userInfo = sFTest.login(new LoginRequest("kevin","kevin1"));
        GameData gameData = new GameData(0,null,null,"FirstGame",null);
        Assertions.assertDoesNotThrow(()-> sFTest.createGame(gameData, userInfo.authToken()));
    }

    @Test
    public void createGameTestFail() throws ResponseException{
        sFTest.clear();
        sFTest.register(new UserData("kevin","kevin1","kevin@byu.edu"));
        AuthData userInfo = sFTest.login(new LoginRequest("kevin","kevin1"));
        GameData gameData = new GameData(0,null,null,null,null);
        Assertions.assertThrows(ResponseException.class,()-> sFTest.createGame(gameData, userInfo.authToken()));
    }

    @Test
    public void logoutTest()throws ResponseException{
        sFTest.clear();
        sFTest.register(new UserData("kevin","kevin1","kevin@byu.edu"));
        AuthData userInfo = sFTest.login(new LoginRequest("kevin","kevin1"));
        Assertions.assertDoesNotThrow(() -> sFTest.logout(userInfo.authToken()));
    }

    @Test
    public void logoutTestFail()throws ResponseException{
        sFTest.clear();
        sFTest.register(new UserData("kevin","kevin1","kevin@byu.edu"));
        AuthData userInfo = sFTest.login(new LoginRequest("kevin","kevin1"));
        sFTest.logout(userInfo.authToken());
        //Throwing an error once the user is logged out
        Assertions.assertThrows(ResponseException.class, ()-> sFTest.logout(userInfo.authToken()));
    }

    @Test
    public void joinGameTest() throws ResponseException{
        sFTest.clear();
        //Creating a user
        sFTest.register(new UserData("kevin","kevin1","kevin@byu.edu"));
        //Login
        AuthData userInfo = sFTest.login(new LoginRequest("kevin","kevin1"));
        //Creating a gameData to create a new game
        GameData firstGame = new GameData(0,null,null,"1stGame",null);
        GameData secondGame = new GameData(0,null,null,"2ndGame",null);

        //Creating the actual game
        CreateGameResult resultId = sFTest.createGame(firstGame, userInfo.authToken());
        sFTest.createGame(secondGame, userInfo.authToken());
        //listing Games
        ListOfGamesResult result = sFTest.listOfGames(userInfo.authToken());
        //Creating the JoinRequest - Needs Color and GameId
        JoinRequest request = new JoinRequest("White",resultId.gameID());
        Assertions.assertDoesNotThrow(()-> sFTest.joinGame(request, userInfo.authToken()));
    }
    @Test
    public void joinGameTestFail()throws ResponseException{
        sFTest.clear();
        UserData request = sFTest.register(new UserData("kevin","kevin1","kevin@byu.edu"));
        //Login
        AuthData userInfo = sFTest.login(new LoginRequest("kevin","kevin1"));
        //Logout
        sFTest.logout(userInfo.authToken());
        JoinRequest joinRequest = new JoinRequest("white",1);
        Assertions.assertThrows(ResponseException.class,() ->sFTest.joinGame(joinRequest, userInfo.authToken()));
    }
}