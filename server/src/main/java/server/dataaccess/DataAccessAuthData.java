package server.dataaccess;

import model.AuthData;
import java.util.UUID;

public class DataAccessAuthData implements DAOAuthDataInterface{
    @Override
    public AuthData createAuthToken(String userName) {
        String authToken = UUID.randomUUID().toString();
        String username = userName;
        AuthData result = new AuthData(authToken,username);
        return result;
    }


    @Override
    public void deleteAuthToken(String username) {
        //I want to delete the AuthToken
        //authToken.authToken().
    }
}
