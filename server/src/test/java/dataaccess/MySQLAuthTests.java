package dataaccess;

import com.sun.source.tree.AssertTree;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLAuthTests {
    public MySQLAuthData sqlAuth;

    //Unit test for Autho
    @BeforeEach
    public void createSetUp() {
        try {
            sqlAuth = new MySQLAuthData();
            sqlAuth.deleteAllAuthTokens();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createToken (){
        AuthData test = new AuthData("bce93eb4-dc4d-4225-a723-d9e11d09cbb8","Kevin");
        assertDoesNotThrow(()->
                sqlAuth.createAuthToken(test.username()));
        try (Connection conn = DatabaseManager.getConnection()) {

            var statement = "SELECT authToken,username FROM listOfAuthTokens WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, "Kevin");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String readUsername = rs.getString("username");
                        assertEquals(test.username(),readUsername);
                        String readAuthToken = rs.getString("authToken");
                        assertEquals(test.authToken(),readAuthToken);
                    }
                }
            }
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

}



