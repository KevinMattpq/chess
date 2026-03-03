package dataaccess;

import model.AuthData;

public interface DAOAuthDataInterface {
    public AuthData createAuthToken (String username);
    public void deleteAuthToken(String username);
}
