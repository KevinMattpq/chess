package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class DataAccessAuthData implements DAOAuthDataInterface {
    private HashMap<String, AuthData> listAuthTokens = new HashMap<>();

    @Override
    public AuthData createAuthToken(String userName) {
        String authToken = UUID.randomUUID().toString();
        String username = userName;
        AuthData result = new AuthData(authToken,username);
        listAuthTokens.put(result.authToken(),result);
        return result;
    }

    @Override
    public AuthData readAuthToken(String authToken) {
        if(listAuthTokens.containsKey(authToken)){
            return listAuthTokens.get(authToken);
        }
        return null;
    }


    @Override
    public void deleteAuthToken(String username) {
        //I want to delete the AuthToken
        listAuthTokens.remove(username);
    }

    @Override
    public void deleteAllAuthTokens() {
        listAuthTokens.clear();
    }
}
