package server.service;

import model.AuthData;
import model.UserData;
import server.dataaccess.DataAccessGames;
import server.dataaccess.DataAccessUsers;

import java.util.Collection;

public class Service {
    DataAccessGames dataGames = new DataAccessGames();
    DataAccessUsers dataUsers = new DataAccessUsers();

    public  void clearAll(){
        dataGames.clearGames();
    }

    //
    public AuthData register(UserData userData) throws ResponseException{
        if(dataUsers.readUser(userData.username()) != null){
            throw new ResponseException("Username already exist");
        }
        dataUsers.createUser(userData);
        return null;
    }
}
