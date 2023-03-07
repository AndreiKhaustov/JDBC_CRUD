package org.example.util;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public final class ConnectionManager {
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String POOL_SIZE_KEY = "sb.pool.size";
    private static BlockingQueue<Connection> pool;

    private ConnectionManager() {

    }

    static {
        loadPostgresqlDriver();
        initConnectionPool();
    }

    private static void loadPostgresqlDriver() {
        try {
            Class.forName("org.postgresql.Driver");
            // загружаем класс в память жвм и не будет исключения при создании конекшена (актуально для джавы до 1.8)
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initConnectionPool() {
        String string = PropertiesUtil.getProperties(POOL_SIZE_KEY);
        pool = new ArrayBlockingQueue<>(Integer.parseInt(string));
        for (int i = 0; i < Integer.parseInt(string); i++) {
            pool.add(openConnection());
        }
    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean add(Connection connection) {
      return pool.add(connection);
    }

    private static Connection openConnection() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.getProperties(URL_KEY), PropertiesUtil.getProperties(USERNAME_KEY), PropertiesUtil.getProperties(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
