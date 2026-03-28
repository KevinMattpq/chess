package client;

import model.LoginRequest;
import model.UserData;
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
    public  void loginTest() throws ResponseException {
        sFTest.clear();
        sFTest.register(new UserData("kevin","kevin1", "kevin@byu.edu"));
        LoginRequest request = new LoginRequest("kevin","kevin1");
        Assertions.assertDoesNotThrow(()-> sFTest.login(request));
    }

    @Test
    public  void loginTestFail()throws ResponseException{
        sFTest.clear();
        sFTest.register(new UserData("kevin","kevin1","kevin@byu.edu"));
        LoginRequest request = new LoginRequest("kevin","kevin2");
        Assertions.assertThrows(ResponseException.class,() -> sFTest.login(request));

    }
}