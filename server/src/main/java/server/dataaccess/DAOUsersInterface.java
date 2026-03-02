package server.dataaccess;


import model.UserData;

public interface DAOUsersInterface {
    public void createUser(UserData userData);
    public void readUser();
    public  void  deleteUser();
}
