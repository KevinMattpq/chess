package dataaccess;

import model.UserData;

import java.util.HashMap;

public class DataAccessUsers implements DAOUsersInterface {
    private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData userData){
        String username = userData.username();
        users.put(username,userData);
    }



    @Override
    public UserData readUser(String userName) {
        if(users.containsKey(userName)){
            return users.get(userName);
        }
        return null;
    }


    @Override
    public void deleteUser(String userName) {
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }


}
