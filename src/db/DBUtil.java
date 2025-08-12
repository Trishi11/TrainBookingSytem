package db;

import java.sql.*;
import ui.ConfigLoader;

public class DBUtil {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            ConfigLoader.get("db.url"),
            ConfigLoader.get("db.username"),
            ConfigLoader.get("db.password")
        );
    }
}
