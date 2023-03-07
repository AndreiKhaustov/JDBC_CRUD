package org.example.repository;

import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudRepository {
    private static final String CREATE_CLIENT = "INSERT INTO Clients.Consumers(name, age) VALUES(?,?)";
    private static final String UPDATE_CLIENT = "UPDATE Clients.Consumers SET name=?, age=? WHERE id=?";
    private static final String DELETE_CLIENT = "DELETE FROM "Clients".Consumers WHERE id=?";

    public boolean create(String name, int age) throws SQLException {
        if (!(name.isEmpty()) && age > 0) {
            Connection connection = ConnectionManager.get();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CLIENT)) {
                preparedStatement.setString(1, name);
                preparedStatement.setLong(2, age);
                preparedStatement.execute();
            }
            ConnectionManager.add(connection);
            return true;
        }
        return false;
    }

    public boolean update(int id, String name, int age) throws SQLException {
        if (!(name.isEmpty()) && age > 0 && id > 0) {
            Connection connection = ConnectionManager.get();
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLIENT)) {
                preparedStatement.setString(1, name);
                preparedStatement.setLong(2, age);
                preparedStatement.setLong(3, id);
                preparedStatement.execute();
            }
            ConnectionManager.add(connection);
            return true;
        }
        return false;
    }

    public boolean delete(int id) throws SQLException {
        if (id > 0) {
            Connection connection = ConnectionManager.get();
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CLIENT)) {
                preparedStatement.setLong(1, id);
                preparedStatement.execute();
            }
            ConnectionManager.add(connection);
            return true;
        }
        return false;

    }
}
