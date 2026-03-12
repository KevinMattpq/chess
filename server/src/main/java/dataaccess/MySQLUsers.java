package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static dataaccess.DatabaseManager.executeUpdate;

public class MySQLUsers implements DAOUsersInterface{
    public MySQLUsers() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }

    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(),userData.password(),userData.email());
    }

    public UserData readUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        //Getting the value of column
                        return readUser(rs.getString("username"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Can't read from database");
        }
        return null;
    }

    public void deleteAllUsers() throws DataAccessException {
        var statement = "TRUNCATE TABLE users";
        executeUpdate(statement);
    }


}
