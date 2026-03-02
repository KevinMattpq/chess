package server.dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.HashMap;

public class DataAccessUsers implements DAOUsersInterface {
    private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData userData){
        String username = userData.username();
        users.put(username,userData);
        System.out.print("Working");
    }

    @Override
    public void readUser() {

    }

    @Override
    public void deleteUser() {

    }

}
