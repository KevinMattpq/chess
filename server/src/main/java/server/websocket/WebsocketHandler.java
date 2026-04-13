package server.websocket;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager conections = new ConnectionManager();

    @Override
    public void handleMessage( WsMessageContext ctx) throws Exception {
        //Handle each of the Four command the clients can send
        UserGameCommand userCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
        MakeMoveCommand makeMoveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
        switch (userCommand.getCommandType()){
            case CONNECT -> connect(userCommand, ctx.session);
            case LEAVE -> leave(userCommand, ctx.session);
            case MAKE_MOVE -> makeMove(makeMoveCommand, ctx.session);
            case RESIGN -> resign(userCommand,ctx.session);
        }
    }
    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    public void connect(UserGameCommand userInfo, Session session) throws IOException, DataAccessException {
        //Getting GameId and authToken come the UserGameCommand
        int gameId = userInfo.getGameID();
        String authToken = userInfo.getAuthToken();
        //Creating instance to get the info based on the auth token
        DAOAuthDataInterface daoAuth = new MySQLAuthData();
        AuthData info = daoAuth.readAuthToken(authToken);
        //Checking if auth token is not equal to null
        if (info == null){
            ErrorMessage msg = new ErrorMessage("Error: Auth token is null");
            String errorMsg = new Gson().toJson(msg);
            session.getRemote().sendString(errorMsg);
        }
        //Getting the username color
        String username = info.username();
        conections.add(gameId,username,session);

        DAOGamesInterface daoGames = new MySQLGames();
        GameData gameData = daoGames.readGame(gameId);
        if (gameData == null){
            ErrorMessage msg = new ErrorMessage("Error: game data is null");
            String errorMsg = new Gson().toJson(msg);
            session.getRemote().sendString(errorMsg);
        } else{
            ChessGame game = gameData.game();

            //Sending LoadGame message
            LoadMessage lgMessage = new LoadMessage(game);
            String message = new Gson().toJson(lgMessage);
            session.getRemote().sendString(message);
            //Notification
            String notificationMsg;
            if(Objects.equals(username, gameData.whiteUsername())){
                notificationMsg = username + " joined as white";
            } else if (Objects.equals(username, gameData.blackUsername())) {
                notificationMsg = username + " joined as black";
            }else{
                notificationMsg = username + " joined as an observer";
            }

            NotificationMessage notification = new NotificationMessage(notificationMsg);
            conections.broadcast(gameId,session,notification);
        }

    }

    public void leave(UserGameCommand userInfo, Session session) throws DataAccessException, IOException {
        String authToken = userInfo.getAuthToken();
        DAOAuthDataInterface daoAuth = new MySQLAuthData();
        AuthData info = daoAuth.readAuthToken(authToken);
        //Authtoken Validation
        if (info == null){
            errorMessage(session,"Error: Auth token is null");
        }
        //Session Validation
        if(!conections.sessionPresent(session)){
            System.out.println("Error not in session");
            return;
        }
        int gameId = userInfo.getGameID();
        String username = info.username();
        if(gameId != 0){
            DAOGamesInterface daoGame = new MySQLGames();
            GameData game = daoGame.readGame(userInfo.getGameID());

            if (Objects.equals(game.whiteUsername(), username) || Objects.equals(game.blackUsername(), username)){
                if(Objects.equals(game.whiteUsername(), username)){
                    game = new GameData(userInfo.getGameID(), null, game.blackUsername(), game.gameName(), game.game());
                    daoGame.updateGame(game);
                }else{
                    game = new GameData(userInfo.getGameID(), game.whiteUsername(), null, game.gameName(), game.game());
                    daoGame.updateGame(game);
                }
            }
        }

        //Removing connection
        conections.remove(session);
        //Sending Notification
        String msg = username + " left the session";
        NotificationMessage notificationMessage = new NotificationMessage(msg);
        conections.broadcast(gameId,session,notificationMessage);
    }

    public void resign(UserGameCommand userInfo, Session session) throws IOException, DataAccessException {
        String authToken = userInfo.getAuthToken();
        DAOAuthDataInterface dao = new MySQLAuthData();
        AuthData info = dao.readAuthToken(authToken);
        if (info == null){
            errorMessage(session,"Information is null");
        }
        //User Validation
        DAOGamesInterface daoGame = new MySQLGames();
        GameData game = daoGame.readGame(userInfo.getGameID());
        int gameId = game.gameID();
        String username = info.username();
        if(!Objects.equals(game.whiteUsername(), username) && !Objects.equals(game.blackUsername(), username)){
            errorMessage(session,"Not a player (WHITE OR BLACK)");
            return;
        }
        //Game
        if(game.game().isGameOver()){
            errorMessage(session,"Game is over");
            return;
        }
        game.game().gameState = true;
        //Updating Game
       GameData newGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
       daoGame.updateGame(newGame);
       //Sending Notification
        String msg = username + " resigned";
        NotificationMessage notificationMessage = new NotificationMessage(msg);
        conections.broadcast(gameId,null,notificationMessage);

    }
    public Map<Integer,String> numsChar = Map.of(
            1,"a",
            2,"b",
            3,"c",
            4,"d",
            5,"e",
            6,"f",
            7,"g",
            8,"h");


    public void makeMove(MakeMoveCommand userInfo, Session session) throws IOException, DataAccessException, InvalidMoveException {
        //Validatin Auth Token
        if(!authTokenValidation(userInfo)){
            errorMessage(session,"Invalid Auth token");
        }
        //Validating User
        if(!playerValidation(userInfo)){
            errorMessage(session,"Observers can not make a move");
        }
        //Game validation
        if(!gameValidation(userInfo)){
            errorMessage(session,"Game is over");
            return;

        }
        //Turn Validation
        if(!turnValidation(userInfo)){
            errorMessage(session,"Incorrect Turn");
            return;
        }

        DAOAuthDataInterface daoA = new MySQLAuthData();
        AuthData userInformation = daoA.readAuthToken(userInfo.getAuthToken());
        String username = userInformation.username();

        //Chess Logic
        ChessMove move = userInfo.getMove();
        //Game from data Base
        DAOGamesInterface daoGame = new MySQLGames();
        GameData gameData = daoGame.readGame(userInfo.getGameID());
        ChessGame game = gameData.game();

        try{
            game.makeMove(move);
        }catch (InvalidMoveException e){
            errorMessage(session,"Invalid Move Try Again");
            return;
        }
        //Updating game
        daoGame.updateGame(gameData);
        //Info to let the user know where the other user moved
        ChessPosition startingPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        int sRow = startingPosition.getRow();
        int sCol = startingPosition.getColumn();
        int eRow = endPosition.getRow();
        int ecol = endPosition.getColumn();

        String sR = numsChar.get(sCol);
        String eR = numsChar.get(ecol);

        //Sending Notification
        String msg = username + " move from "+sR+sRow +" to "+eR+eRow;
        NotificationMessage notificationMessage = new NotificationMessage(msg);
        conections.broadcast(userInfo.getGameID(), session,notificationMessage);

        //Sending Load Game
        LoadMessage newGame = new LoadMessage(game);
        conections.broadcast(userInfo.getGameID(), null,newGame);
        //Check Mate
        if(game.isInCheckmate(game.getTeamTurn())){
            game.gameState = true;
            daoGame.updateGame(gameData);
            //Sending Notification
            String msg2 = "Game Over";
            NotificationMessage notificationMessage2 = new NotificationMessage(msg);
            conections.broadcast(userInfo.getGameID(), null,notificationMessage2);
        }
    }


    //Helper functions
    public void errorMessage(Session session, String message) throws IOException {
        ErrorMessage msg = new ErrorMessage(message);
        String errorMsg = new Gson().toJson(msg);
        session.getRemote().sendString(errorMsg);
    }

    public boolean playerValidation(UserGameCommand userInfo) throws DataAccessException {
        String authToken = userInfo.getAuthToken();
        DAOAuthDataInterface daoA = new MySQLAuthData();
        AuthData userInformation = daoA.readAuthToken(authToken);
        String username = userInformation.username();
        DAOGamesInterface daoGame = new MySQLGames();
        GameData game = daoGame.readGame(userInfo.getGameID());
        if(!Objects.equals(game.whiteUsername(), username) && !Objects.equals(game.blackUsername(), username)){
            return false;
        }
        return true;
    }

    public boolean gameValidation(UserGameCommand userInfo) throws DataAccessException {
        DAOGamesInterface daoGame = new MySQLGames();
        GameData game = daoGame.readGame(userInfo.getGameID());
        if(game.game().isGameOver()){
            return false;
        }
        return true;
    }

    public boolean authTokenValidation(UserGameCommand userInfo) throws DataAccessException {
        String authToken = userInfo.getAuthToken();
        DAOAuthDataInterface dao = new MySQLAuthData();
        AuthData info = dao.readAuthToken(authToken);
        //Validating Auth token
        if (info == null){
            return false;
        }
        return true;
    }

    public boolean turnValidation(UserGameCommand userInfo) throws DataAccessException {
        DAOGamesInterface daoGame = new MySQLGames();
        DAOAuthDataInterface daoA = new MySQLAuthData();
        GameData game = daoGame.readGame(userInfo.getGameID());
        AuthData userInformation = daoA.readAuthToken(userInfo.getAuthToken());
        String username = userInformation.username();
        ChessGame.TeamColor color;
        //Comparing usernames instead of colors
        if(Objects.equals(game.blackUsername(), username)){
            color = ChessGame.TeamColor.BLACK;
        }else{
            color = ChessGame.TeamColor.WHITE;
        }
        if(game.game().getTeamTurn() != color){
            return false;
        }
        return true;
    }

}
