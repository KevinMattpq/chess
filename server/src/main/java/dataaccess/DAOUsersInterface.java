package dataaccess;


import model.UserData;

public interface DAOUsersInterface {
    void createUser(UserData userData) throws DataAccessException;
    UserData readUser(String userName) throws DataAccessException;
    void deleteAllUsers() throws DataAccessException;
}
