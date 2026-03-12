package dataaccess;

import model.AuthData;

public interface DAOAuthDataInterface {
    public AuthData createAuthToken (String username) throws DataAccessException;
    public AuthData readAuthToken(String authToken) throws DataAccessException;
    public void deleteAuthToken(String username) throws  DataAccessException;
    public void deleteAllAuthTokens() throws DataAccessException;
}
