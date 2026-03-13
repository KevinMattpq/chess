package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import static dataaccess.DatabaseManager.executeUpdate;

public class MySQLAuthData implements DAOAuthDataInterface {
    public MySQLAuthData() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }

    @Override
    public AuthData createAuthToken(String usernameInput) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        String username = usernameInput;
        AuthData result = new AuthData(authToken,username);
        var statement = "INSERT INTO listOfAuthTokens (authToken , username) values (?, ?)";
        executeUpdate(statement, result.authToken(),result.username());
        return result;
    }

    @Override
    public AuthData readAuthToken(String inputAuthToken) throws DataAccessException {
        if(inputAuthToken == null){
            throw new DataAccessException("AuthToken can not be null");
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken,username FROM listOfAuthTokens WHERE authToken = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, inputAuthToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        //Getting the value of column
                        AuthData responseData = new AuthData(rs.getString("authToken"),rs.getString("username"));
                        return responseData;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Can't read from database");
        }
        return null;
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {
        if(authToken == null){
            throw new DataAccessException("AuthToken can not be null");
        }
        var statement = "DELETE FROM listOfAuthTokens WHERE authToken=?";
        executeUpdate(statement,authToken);
    }

    @Override
    public void deleteAllAuthTokens() throws DataAccessException {
        var statement = "TRUNCATE TABLE listOfAuthTokens";
        executeUpdate(statement);
    }
}
