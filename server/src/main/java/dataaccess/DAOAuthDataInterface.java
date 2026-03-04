package dataaccess;

import model.AuthData;

public interface DAOAuthDataInterface {
    public AuthData createAuthToken (String username);
    public AuthData readAuthToken(String authToken);
    public void deleteAuthToken(String username);
    public void deleteAllAuthTokens();
}
