package server.dataaccess;

import model.AuthData;

public interface DAOAuthDataInterface {
    public AuthData createAuthToken ();
    public void deleteAuthToken();
}
