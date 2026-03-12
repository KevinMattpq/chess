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
        int response = executeUpdate(statement, result.authToken(),result.username());
        return null;
    }

    @Override
    public AuthData readAuthToken(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken FROM listOfAuthTokens WHERE authToken = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        //Getting the value of column
                        return readAuthToken(rs.getString("authToken"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Can't read from database");
        }
        return null;
    }

    @Override
    public void deleteAuthToken(String username) throws DataAccessException {
        var statement = "DELETE FROM listOfAuthTokens WHERE username=?";
        executeUpdate(statement,username);
    }

    @Override
    public void deleteAllAuthTokens() throws DataAccessException {
        var statement = "TRUNCATE TABLE listOfAuthTokens";
        executeUpdate(statement);
    }
}
