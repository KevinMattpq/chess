package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(),hashedPassword,userData.email());
    }

    public UserData readUser(String username) throws DataAccessException {
        if(username == null){
            throw new DataAccessException("Username is necessary to read from database");
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username,password,email FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        //Getting the value of column
                        UserData readResponse = new UserData(rs.getString("username"),rs.getString("password"),rs.getString("email"));
                        return readResponse;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Can't read from database",null);
        }
        return null;
    }

    public void deleteAllUsers() throws DataAccessException {
        var statement = "TRUNCATE TABLE users";
        executeUpdate(statement);
    }


}
