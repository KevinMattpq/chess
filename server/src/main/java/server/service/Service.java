package server.service;

import server.dataaccess.DataAccessGames;
import server.dataaccess.DataAccessUsers;

import java.util.Collection;

public class Service {
    DataAccessGames dataGames = new DataAccessGames();
    DataAccessUsers dataUsers = new DataAccessUsers();

    public  void clearAll(){
        dataGames.clearGames();
    }

    public void register(){
        dataUsers.registerUser();
    }
}
