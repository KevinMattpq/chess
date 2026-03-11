package dataaccess;

import model.UserData;

public class MySQLUsers {

    public void addUser(UserData userData) throws ResponseException {
        var statement = "INSERT INTO user (name, type, json) VALUES (?, ?, ?)";
        String json = new Gson().toJson(pet);
        int id = executeUpdate(statement, pet.name(), pet.type(), json);
        return new Pet(id, pet.name(), pet.type());
    }

    public void getUser(String username) throws ResponseException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, json FROM pet WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readPet(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    public void deleteAllUsers() throws ResponseException {
        var statement = "DELETE FROM users";
    }

}
