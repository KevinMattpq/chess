package dataaccess;


import model.UserData;
import server.service.ResponseException;

public interface DAOUsersInterface {
    void createUser(UserData userData) throws ResponseException;
    UserData readUser(String userName) throws ResponseException;
    void deleteAllUsers() throws ResponseException;
}
