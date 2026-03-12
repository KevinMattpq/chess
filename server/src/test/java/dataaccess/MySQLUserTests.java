package dataaccess;

import com.sun.source.tree.AssertTree;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLUserTests {
    public MySQLUsers sqlUsers;

    //Unit test for User
    @BeforeEach
    public void createSetUp(){
        try {
            sqlUsers = new MySQLUsers();
            sqlUsers.deleteAllUsers();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createUser (){
        UserData test = new UserData("Kevin","Admin","kpAdmin");
        assertDoesNotThrow(()->
                sqlUsers.createUser(test));
        try (Connection conn = DatabaseManager.getConnection()) {

            var statement = "SELECT username,password,email FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, "Kevin");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String readUsername = rs.getString("username");
                        assertEquals(test.username(),readUsername);
                        String readPassword = rs.getString("password");
                        assertTrue(BCrypt.checkpw("Admin", readPassword));
                        String readEmail = rs.getString("email");
                        assertEquals(test.email(),readEmail);
                    }
                }
            }
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    public void readUser() throws DataAccessException {
        UserData test = new UserData("Kevin","Admin","kpAdmin");
        sqlUsers.createUser(test);
        UserData readResponse = sqlUsers.readUser(test.username());
        String readUsername = readResponse.username();
        assertEquals(test.username(),readUsername);
        String readPassword = readResponse.password();
        assertTrue(BCrypt.checkpw("Admin", readPassword));
        String readEmail = readResponse.email();
        assertEquals(test.email(),readEmail);
    }

}
