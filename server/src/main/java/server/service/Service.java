package server.service;

import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Arrays;
import java.util.List;

public class Service {
    DAOUsersInterface dataUsers;
    DAOGamesInterface dataGames;
    DAOAuthDataInterface dataAuth;
   public Service(){
       try{
            dataGames = new MySQLGames();
            dataUsers = new MySQLUsers();
            dataAuth = new MySQLAuthData();
       }catch(DataAccessException e){
           throw new RuntimeException("Error: Creating DAO");
       }
   }



    public  void clearAll() throws ResponseException{
       try{
           dataGames.clearGames();
           dataUsers.deleteAllUsers();
           dataAuth.deleteAllAuthTokens();
       } catch (DataAccessException e){
           throw new ResponseException("Error: Database Error");

       }

    }

    public void logout(String authToken) throws  ResponseException{
       try{
           //Checking if there is an authToken - "Login"
           if(dataAuth.readAuthToken(authToken) == null){
               throw new ResponseException("Error: unauthorized");
           }
           dataAuth.deleteAuthToken(authToken);
       }catch (DataAccessException e){
           throw new ResponseException("Error: Database Error");

       }

    }
    public AuthData register(UserData userData) throws ResponseException{
       try{
           //Bad Request
           if(userData.username() == null || userData.password() == null || userData.email() == null){
               throw new ResponseException("Error: Bad Request");
           }
           //Checking if a username exist already
           if(dataUsers.readUser(userData.username()) != null){
               throw new ResponseException("Error: Username already taken");
           }
           dataUsers.createUser(userData);
           return dataAuth.createAuthToken(userData.username());
       }catch (DataAccessException e){
           throw new ResponseException("Error: Database Error");
       }

    }

    public AuthData login(UserData userData) throws  ResponseException{
       try{
           //Bad Request
           if(userData.username() == null || userData.password() == null){
               throw new ResponseException("Error: Bad Request");
           }
           //Checking if user exits
           if(dataUsers.readUser(userData.username()) == null){
               throw new ResponseException("Error: unauthorized");
           }
           //Getting the information of the User witht that username
           UserData user = dataUsers.readUser(userData.username());
           //Password Check
           if(!BCrypt.checkpw(userData.password(), user.password())){
               throw  new ResponseException("Error: unauthorized");
           }

           dataUsers.readUser(userData.username());
           return dataAuth.createAuthToken(userData.username());
       }catch (DataAccessException e){
           throw new ResponseException("Error: Database Error");
       }

    }

    public CreateGameResult createGame(GameData gameData) throws ResponseException{
       try{
           //Checking if gameName was provided
           if(gameData.gameName()  == null){
               throw new ResponseException("Error: Bad Request");
           }
           GameData newGame = dataGames.createGame(gameData.gameName());
           return new CreateGameResult(newGame.gameID());
       } catch (DataAccessException e){
           throw new ResponseException("Error: Database Error");
       }
    }

    public AuthData isUserLogin(String authToken)throws ResponseException{
       try{
           //Checking if the authToken has a value
           if(dataAuth.readAuthToken(authToken) == null){
               throw new ResponseException("Error: Unauthorized");
           }
           return dataAuth.readAuthToken(authToken);
       } catch (DataAccessException e){
           throw new ResponseException("Error: Database Error");
       }

    }


    public JoinRequest joinGame(JoinRequest userData, String username) throws  ResponseException {
       try{
           List<String> colors = Arrays.asList("WHITE", "BLACK");

           if (userData.playerColor() == null || !colors.contains(userData.playerColor()) || userData.gameID() == 0 ) {
               throw new ResponseException("Error: Bad Request");
           }

           if(dataGames.readGame(userData.gameID()).whiteUsername() != null &&
                   userData.playerColor().equals("WHITE")||
                   dataGames.readGame(userData.gameID()).blackUsername() != null &&
                           userData.playerColor().equals("BLACK")){
               throw new ResponseException("Error: AlreadyTaken");
           }

           //Old game (before update)
           GameData game = dataGames.readGame(userData.gameID());

           //Updating username based on color
           if (userData.playerColor().equals("WHITE")) {
               dataGames.updateGame(new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game()));
           } else {
               dataGames.updateGame(new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game()));
           }
           JoinRequest newJoinGame = new JoinRequest(userData.playerColor(), userData.gameID());
           return newJoinGame;
       } catch (DataAccessException e){
           throw new ResponseException("Error: Database Error");
       }

    }

    public ListOfGamesResult listOfGames(String authToken) throws ResponseException{
       try{
           if(dataAuth.readAuthToken(authToken) == null){
               throw new ResponseException("Error: Unauthorized");
           }
           return new ListOfGamesResult(dataGames.readAllGames());

       } catch (DataAccessException e){
           throw new ResponseException("Error: Database Error");
       }
    }
}
