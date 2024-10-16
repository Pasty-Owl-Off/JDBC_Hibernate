package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final String USERS_TABLE = "UsersTable";
    private static final String DB_NAME = "UsersDB";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String LAST_NAME = "lastName";
    private static final String AGE = "age";


    public UserDaoJDBCImpl() {
        String createDatabase = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createDatabase);
        } catch (SQLException e) {
            System.err.println("Не удалось установить состояние базы данных");
        }
    }

    public void createUsersTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS " + USERS_TABLE + " (" +
                ID + " BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                NAME + " VARCHAR(2048), " +
                LAST_NAME + " VARCHAR(2048), " +
                AGE + " TINYINT CHECK(age >= 0)" +
                ")";
        try (Connection connection = Util.getConnection();
              Statement statement = connection.createStatement()) {
            statement.execute(createTable);
        } catch (SQLException e) {
            System.err.println("Не удалось создать таблицу");
        }
    }

    public void dropUsersTable() {
        String dropTable = "DROP TABLE IF EXISTS " + USERS_TABLE;
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(dropTable);
        } catch (SQLException e) {
            System.err.println("Не удалось удалить таблицу");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String addUser = "INSERT INTO " + USERS_TABLE + "(" + NAME + ", " + LAST_NAME + ", " + AGE + ")" +
                "VALUES ('" + name + "', '" + lastName + "', " + age + ")";
        try (Connection connection = Util.getConnection();
              Statement statement = connection.createStatement()) {
            statement.execute(addUser);
        } catch (SQLException e) {
            System.err.println("Не удалось добавить пользователя");
        }
    }

    public void removeUserById(long id) {
        String deleteUser = "DELETE FROM " + USERS_TABLE +
                " WHERE " + ID + " = " + id;
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteUser);
        } catch (SQLException e) {
            System.err.println("Не удалось удалить пользователя");
        }
    }

    public List<User> getAllUsers() {
        String getUsers = "SELECT * FROM " + USERS_TABLE;
        List<User> allUsers = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(getUsers);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(ID));
                user.setName(resultSet.getString(NAME));
                user.setLastName(resultSet.getString(LAST_NAME));
                user.setAge(resultSet.getByte(AGE));
                allUsers.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Не удалось получить список всех пользователей");
        }
        return allUsers;
    }

    public void cleanUsersTable() {
        String cleanTable = "TRUNCATE TABLE " + USERS_TABLE;
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(cleanTable);
        } catch (SQLException e) {
            System.err.println("Не удалось зачистить таблицу");
        }
    }
}
