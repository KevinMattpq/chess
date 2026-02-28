package server.service;

import server.dataaccess.DataAccessGames;

import java.util.Collection;

public class Service {
    public  void clearAll(){
        DataAccessGames data = new DataAccessGames();
        data.clearGames();
    }
}
