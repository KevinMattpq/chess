package client;

import chess.ChessBoard;
import chess.ChessGame;
import model.*;
import server.Notify;
import server.ServerFacade;
import networking.ResponseException;
import server.WebsocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient implements Notify {
    private State state = State.SIGNEDOUT;
    ServerFacade server;
    AuthData userInfo;
    ChessBoard board = new ChessBoard();
    BoardPrinter boardPrinter = new BoardPrinter();
    ListOfGamesResult gameslist;
    WebsocketFacade webSocket;



    public ChessClient(String serverUrl) throws ResponseException{
        server = new ServerFacade(serverUrl);
        board.resetBoard();
        try{
            webSocket= new WebsocketFacade(this);
        }catch (Exception e){
            System.out.println("Something went wrong with Websocket");
        }
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
                        case "w","watch" -> watchGame(params);
                        case "g", "show board" -> printBoard();
                        case "logout" -> logout();
                        default -> help();
                    };
                case GAMEPLAY ->
                    switch (cmd){
                        case "h","help" -> help();
                        case "rd","redraw" -> redrawBoard();
                        case "l","leave" -> leave();
                        case "mk","move" -> makeMove();
                        case "rs","resign" -> resign();
                        case "lm","Higlight" -> highlightMoves();

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
        userInfo = server.register(newUser);
        state = State.SIGNEDIN;
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
        gameslist = result;
        StringBuilder listOfGames = new StringBuilder();
        for(GameData game: gameslist.games()){
            listOfGames.append("Game Id: " + game.gameID()+ "\n");
            listOfGames.append("White Username: "+ game.whiteUsername()+ "\n");
            listOfGames.append("Black Usename: "+game.blackUsername()+ "\n");
            listOfGames.append("Game Name: "+game.gameName()+ "\n\n");
        }
        return listOfGames.toString();
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
        try{
            Integer gameId = Integer.parseInt(gameID);
            String color = params[1].toUpperCase();
            ChessGame.TeamColor uColor = color.equals("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            JoinRequest joinRequest = new JoinRequest(color,gameId);
            server.joinGame(joinRequest,userInfo.authToken());
            System.out.println(boardPrinter.draw(board,uColor));
        }catch (NumberFormatException e){
            throw new ResponseException("Make sure to input a number for Game ID");
        }
            state = State.GAMEPLAY;
        return "Successfully joined game";
    }

    public String watchGame(String... params) throws ResponseException{
        if(!(params.length == 1)){
            throw new ResponseException("Game Id is required");
        }
        try{
            if(gameslist != null){
                String gameID = params[0];
                int userId = Integer.parseInt(gameID);
                for (GameData game: gameslist.games()){
                    if (game.gameID() == userId){
                        System.out.println(boardPrinter.draw(board, ChessGame.TeamColor.WHITE));
                        //webSocket.connect(userInfo.authToken(), Integer.parseInt(gameID));
                        return "Successfully you are now watching a game";
                    }
                }
            }
        }catch (NumberFormatException e){
            throw new ResponseException("Make sure to input a Number for Game Id");
        }

        return "You must list the games OR create Games first";
    }

    public String logout()throws ResponseException{
        assertSignedIn();
        //Call function from serverFacade
        server.logout(userInfo.authToken());
        //Updatind the state
        state = State.SIGNEDOUT;
        return "Logged out successfully";

    }

    public String printBoard(){
      ChessBoard board = new ChessBoard();
      board.resetBoard();
      String result = boardPrinter.draw(board,ChessGame.TeamColor.WHITE);
      return result;
    };


    //INGAME

    public String leave(){
        state = State.SIGNEDIN;
        return "Back to home";
    }

    private String highlightMoves() {
        return "Test";
    }

    private String resign() {
        return "Test";
    }

    private String makeMove() {
        return "Test";
    }

    private String redrawBoard() {
        return "Test";
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

        if(state == State.GAMEPLAY){
            return """
                    Options:
                    Help: "h", "help"
                    Redraw Chess board: "rd", "redraw"
                    Leave: "l", "leave"
                    Make move: "mk", "move" <Start Poistion> <End Position>
                    Resign: "r", "resign"
                    Legal move: "lm","highlight"
                    """;
        }
        return """
                - Pending
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


    @Override
    public void notifyError(ErrorMessage message) {
        System.out.println(message.errorMessage);
    }

    @Override
    public void notifyNotification(NotificationMessage message) {
        System.out.println(message.message);
    }

    @Override
    public void notifyLoadGame(LoadMessage game) {

    }
}
