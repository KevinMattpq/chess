package client;

import model.*;
import server.ServerFacade;
import server.service.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private State state = State.SIGNEDOUT;
    ServerFacade server;
    AuthData userInfo;

    public ChessClient(String serverUrl) throws ResponseException{
        server = new ServerFacade(serverUrl);
    }

    public void run(){
        System.out.println("♕ Welcome to Chess. Sign in to start.");
        System.out.print(help());

        //Reading the input
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();
            //Processing part
            try{
                //Saving the result of eval
                result = eval(line);
                //Printing the result in the console
                System.out.print(result);
            }catch(Throwable e){
                //Handleing the error
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    public String eval(String input) {
        try {
            //Converts everything into lowercase and grabs each value separated by a space
            String[] tokens = input.toLowerCase().split(" ");
            //Grabbing the command or help
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            //Getting parameter after the fist word
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (state) {
                case SIGNEDOUT ->
                switch(cmd) {
                    case "login", "l" -> login(params);
                    case "register", "r" -> register(params);
                    case "quit", "q" -> quit();
                    case "help", "h" -> help();
                    default -> help();
                };
                case SIGNEDIN ->
                    switch (cmd){
                        case "l", "list" -> listOfGames();
                        case "c", "create" -> createGame(params);
                        case "j","join" -> joinGame(params);
                        case "w","watch" -> watchGame();
                        case "logout" -> logout();
                        default -> help();
                    };
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    //SIGNEDOUT METHODS
    public String login(String... params) throws ResponseException {
        if (!(params.length == 2)) {
            throw new ResponseException("Username and Password are required");
        }
        String username = params[0];
        String password = params[1];
        LoginRequest loginRequest = new LoginRequest(username,password);
        //Calling function from service PENDING
        userInfo = server.login(loginRequest);
        state = State.SIGNEDIN;
        return "Logged in successfully";
    }

    public String register(String... params) throws ResponseException {
        if (!(params.length == 3)){
            throw new ResponseException("Username, password and email are required");
        }
        String username = params[0];
        String password = params[1];
        String email = params[2];
        UserData newUser = new UserData(username,password,email);
        //Calling function from server PENDING
        server.register(newUser);
        return "Successfully registered.";
    }

    public String quit(){
       System.exit(1);
       return "Successfully quit";
    }


    //SIGNEDIN Methods
    public String listOfGames()throws ResponseException{
        assertSignedIn();
        ListOfGamesResult result = server.listOfGames(userInfo.authToken());
        return result.toString();
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (!(params.length == 1)){
            throw new ResponseException("Error");
        }
        String gameName = params[0];
        GameData gameData = new GameData(0,null,null,gameName,null);
        //Calling function from serverFacade
        CreateGameResult finalResult = server.createGame(gameData,userInfo.authToken());
        //Returning GameID as String
        String gameId = Integer.toString(finalResult.gameID());
        return gameId;

    }

    public String joinGame(String... params) throws ResponseException{
        if(!(params.length == 2)){
            throw new ResponseException("Game Id & Color are required");
        }
        String gameID = params[0];
        Integer gameId = Integer.parseInt(gameID);
        String color = params[1];
        JoinRequest joinRequest = new JoinRequest(color,gameId);
        server.joinGame(joinRequest,userInfo.authToken());
        return "Successfully joined game";
    }

    public String watchGame(String... params) throws ResponseException{
        if(!(params.length == 1)){
            throw new ResponseException("Game Id is required");
        }
        String gameID = params[0];
        return "Successfully you are now watching a game";
    }

    public String logout()throws ResponseException{
        assertSignedIn();
        //Call function from serverFacade
        server.logout(userInfo.authToken());
        //Updatind the state
        state = State.SIGNEDOUT;
        return "Logged out successfully";

    }


    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    Options:
                    Login as an existing user: "l", "login" <USERNAME> <PASSWORD>
                    Register a new user: "r", "register" <USERNAME> <PASSWORD> <EMAIL>
                    Exit the program: "q", "quit"
                    Print this message: "h", "help"
                    """;
        }
        if (state == State.SIGNEDIN){
            return """
                    Options:
                    List current games: "l", "list"
                    Create new game: "c", "create" <GAME NAME>
                    Join a game: "j", "join" <GAME ID> <COLOR>
                    Watch a game: "w", "watch" <GAME ID>
                    Logout: "logout"
                    """;
        }
        return """
                - list
                - adopt <pet id>
                - rescue <name> <CAT|DOG|FROG|FISH>
                - adoptAll
                - signOut
                - quit
                """;
    }

    //It will let the user know where to type
    private void printPrompt() {
        System.out.print("\n>>> ");
    }
    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException("You must sign in");
        }
    }
}
