package server.service;

import model.AuthData;
import model.CreateGameResult;
import model.GameData;
import model.UserData;
import dataaccess.DataAccessGames;
import dataaccess.DataAccessUsers;
import dataaccess.DataAccessAuthData;

public class Service {
    DataAccessGames dataGames = new DataAccessGames();
    DataAccessUsers dataUsers = new DataAccessUsers();
    DataAccessAuthData dataAuth = new DataAccessAuthData();

    public  void clearAll(){
        dataGames.clearGames();
        dataUsers.deleteAllUsers();
        dataAuth.deleteAllAuthTokens();
    }

    public void logout(String authToken) throws  ResponseException{
        if(dataAuth.readAuthToken(authToken) == null){
            throw new ResponseException("Error: unauthorized");
        }
        dataAuth.deleteAuthToken(authToken);
    }
    public AuthData register(UserData userData) throws ResponseException{
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
    }

    public AuthData login(UserData userData) throws  ResponseException{
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
        if(!user.password().equals(userData.password())){
            throw  new ResponseException("Error: unauthorized");
        }

        dataUsers.readUser(userData.username());
        return dataAuth.createAuthToken(userData.username());
    }

    public CreateGameResult createGame(GameData userData) throws ResponseException{

        if(userData.gameName()  == null){
            throw new ResponseException("Error: Bad Request");
        }

        GameData newGame = dataGames.createGame(userData.gameName());
        return new CreateGameResult(newGame.id());
    }
}
