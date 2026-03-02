package server.dataaccess;


import model.UserData;

public interface DAOUsersInterface {
    void createUser(UserData userData);
    UserData readUser(String userName);
    void  deleteUser(String userName);
}
