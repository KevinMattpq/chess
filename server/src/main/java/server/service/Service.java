package server.service;

import server.dataaccess.DataAccessGames;
import server.dataaccess.DataAccessUsers;

import java.util.Collection;

public class Service {
    public  void clearAll(){
        DataAccessGames data = new DataAccessGames();
        data.clearGames();
    }

    public void register(){
        DataAccessUsers data = new DataAccessUsers();
        data.registerUser();
    }
}
