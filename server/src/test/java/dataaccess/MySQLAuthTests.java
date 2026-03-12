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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLAuthTests {
    public MySQLAuthData sqlAuth;

    //Unit test for Auth
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
        String authToken = UUID.randomUUID().toString();
        AuthData test = new AuthData(authToken,"Kevin");
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
                        assertNotNull(readAuthToken);
                    }
                }
            }
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    public void readAuthToken() throws DataAccessException {
        String username = "John";
        AuthData test = sqlAuth.createAuthToken(username);
        AuthData readResponse = sqlAuth.readAuthToken(test.authToken());
        String readUsername = readResponse.username();
        assertEquals(test.username(),readUsername);
        String readAuthToken = readResponse.authToken();
        assertEquals(test.authToken(),readAuthToken);
    }

}



