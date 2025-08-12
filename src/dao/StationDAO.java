package dao;
import db.DBUtil;
import java.sql.*;
import java.util.*;

public class StationDAO {
    public List<String> listAllNames() throws SQLException {
        String sql = "SELECT station_name FROM stations";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<String> list = new ArrayList<>();
            while (rs.next()) list.add(rs.getString("station_name"));
            return list;
        }
    }

    public void add(String name, String city) throws SQLException {
        String sql = "INSERT INTO stations (station_name, city) VALUES (?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name); ps.setString(2, city); ps.executeUpdate();
        }
    }
}
