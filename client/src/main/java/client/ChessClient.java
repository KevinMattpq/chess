package client;

import chess.*;
import model.*;
import server.Notify;
import server.ServerFacade;
import networking.ResponseException;
import server.WebsocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadMessage;
import websocket.messages.NotificationMessage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ChessClient implements Notify {
    private State state = State.SIGNEDOUT;
    ServerFacade server;
    AuthData userInfo;
    ChessBoard boardGame = new ChessBoard();
    BoardPrinter boardPrinter = new BoardPrinter();
    ListOfGamesResult gameslist;
    WebsocketFacade webSocket;
    int unGameId;
    ChessGame.TeamColor uColor;
    private Scanner scanner = new Scanner(System.in);




    public ChessClient(String serverUrl) throws ResponseException{
        server = new ServerFacade(serverUrl);
        boardGame.resetBoard();
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
                        case "mk","move" -> makeMove(params);
                        case "rs","resign" -> resign();
                        case "lm","valid" -> highlightMoves();
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
            unGameId = gameId;
            String color = params[1].toUpperCase();
            uColor = color.equals("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            JoinRequest joinRequest = new JoinRequest(color,gameId);
            server.joinGame(joinRequest,userInfo.authToken());
            webSocket.connect(userInfo.authToken(), Integer.parseInt(gameID));
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
                        System.out.println(boardPrinter.draw(boardGame, ChessGame.TeamColor.WHITE));
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
        server.logout(userInfo.authToken());
        state = State.SIGNEDOUT;
        return "Logged out successfully";

    }

    public String printBoard(){
      ChessBoard board = new ChessBoard();
      board.resetBoard();
      String result = boardPrinter.draw(board,ChessGame.TeamColor.WHITE);
      return result;
    };

    //Helper map for make Move and legal moves.
    public Map<Character,Integer> charsNums = Map.of(
        'a',1,
        'b',2,
        'c',3,
        'd',4,
        'e',5,
        'f',6,
        'g',7,
        'h',8);

    //INGAME

    public String leave(){
        try{
            webSocket.leave(userInfo.authToken(), unGameId);
        }catch (IOException e){
            System.out.println("Error when leaving");
        }
        state = State.SIGNEDIN;
        return "Back to home";
    }

    private String highlightMoves() {
        //Variables
        int rowS;
        int colS;
        ChessPosition hPosition;
        Collection<ChessMove> legalMoves;

        System.out.println("Input the starting position of the piece that you want to know the valid moves <LETTER><NUMBER>");
        printPrompt();
        String location = scanner.nextLine().toLowerCase();
        if(charsNums.containsKey(location.charAt(0))){
            if(Character.isDigit(location.charAt(1))){
                colS = charsNums.get(location.charAt(0));
                rowS = Integer.parseInt(String.valueOf(location.charAt(1)));
                hPosition = new ChessPosition(rowS,colS);
                ChessGame game = new ChessGame();
                legalMoves = game.validMoves(hPosition);
                System.out.println(boardPrinter.draw(boardGame,uColor,
                    legalMoves.stream().map(ChessMove::getEndPosition).collect(Collectors.toCollection(ArrayList::new))));
            }
        }

        return "Test";
    }

    private String resign(){
        try{
            webSocket.resign(userInfo.authToken(), unGameId);
        }catch (IOException e){
            System.out.println("Error when resign");
        }
        return "You resigned from the game";
    }

    private String makeMove(String...params) {
        if(!(params.length == 2)){
            System.out.println("You need <Command> <start position> <end position>");
        }
        String start = params[0];
        String end = params[1];
        ChessMove move;
        ChessPosition sPosition;
        ChessPosition ePosition;
        int rowStart;
        int colStart;
        int rowEnd;
        int colEnd;


        if(start.length() == 2 && end.length() == 2){
            //Checking if it is a char and if char is in map
            if(charsNums.containsKey(start.charAt(0)) && charsNums.containsKey(end.charAt(0))){
                    if(Character.isDigit(start.charAt(1)) && Character.isDigit(end.charAt(1))){
                        //Starting Position
                            colStart = charsNums.get(start.charAt(0));
                            rowStart = Integer.parseInt(String.valueOf(start.charAt(1)));
                            sPosition = new ChessPosition(rowStart,colStart);
                        // End position
                            colEnd = charsNums.get(end.charAt(0));
                            rowEnd = Integer.parseInt(String.valueOf(end.charAt(1)));
                            ePosition = new ChessPosition(rowEnd,colEnd);
                            ChessPiece.PieceType promotionType = null;
                            ChessPiece.PieceType pieceType = boardGame.getPiece(sPosition).getPieceType();
                            //Promotion
                            if((rowEnd == 1 || rowEnd == 8) && (pieceType == ChessPiece.PieceType.PAWN)){
                                System.out.println("Which piece type do you want for promotion?");
                                printPrompt();
                                String promotion = scanner.nextLine().toLowerCase();
                                //Validating the input
                                switch (promotion){
                                    case "queen" -> promotionType = ChessPiece.PieceType.QUEEN;
                                    case "knight" -> promotionType = ChessPiece.PieceType.KNIGHT;
                                    case "bishop"-> promotionType = ChessPiece.PieceType.BISHOP;
                                    case "rook"-> promotionType = ChessPiece.PieceType.ROOK;
                                    default -> System.out.println("Acceptable input: queen,knight,bishop,rook");
                                }
                            }
                            // Creating Chess move
                            move = new ChessMove(sPosition,ePosition,promotionType);
                            //Updating my board
                            webSocket.makeMove(userInfo.authToken(),unGameId,move);

                }else{
                        System.out.println("An Integer is required after letter -> <LETTER><NUMBER>");
                    }
            }else{
                System.out.println("Character is required as first element <LETTER><NUMBER>");
            }
        }else{
            System.out.println("You need a <LETTER><NUMBER> for Starting Position & for End Position");
        }
        return "You made a move";
    }

    private String redrawBoard() {
        System.out.println(boardPrinter.draw(boardGame,uColor));
        return "Current Board";
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
                    Make move: "mk", "move" <Start Position> <End Position>
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
        boardGame = game.game.getBoard();
        System.out.println("\n");
        System.out.println(boardPrinter.draw(boardGame,uColor));
        printPrompt();
    }
}
