package server.service;

import model.AuthData;
import model.UserData;
import server.dataaccess.DataAccessGames;
import server.dataaccess.DataAccessUsers;
import server.dataaccess.DataAccessAuthData;

import java.util.Collection;

public class Service {
    DataAccessGames dataGames = new DataAccessGames();
    DataAccessUsers dataUsers = new DataAccessUsers();
    DataAccessAuthData dataAuth = new DataAccessAuthData();

    public  void clearAll(){
        dataGames.clearGames();
    }

    //
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
        AuthData registerResult = dataAuth.createAuthToken(userData.username());
        return registerResult;
    }
}
