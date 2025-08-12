package dao;

import db.DBUtil;
import model.Train;

import java.sql.*;
import java.util.*;

public class TrainDAO {
    public List<Train> listAll() throws SQLException {
        String sql = "SELECT * FROM trains";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<Train> list = new ArrayList<>();
            while (rs.next()) {
                Train t = new Train(rs.getInt("train_id"), rs.getString("train_name"), rs.getString("train_type"), rs.getInt("total_seats"), rs.getDouble("fare_per_km"));
                list.add(t);
            }
            return list;
        }
    }

    public Train findById(int id) throws SQLException {
        String sql = "SELECT * FROM trains WHERE train_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Train(rs.getInt("train_id"), rs.getString("train_name"), rs.getString("train_type"), rs.getInt("total_seats"), rs.getDouble("fare_per_km"));
                return null;
            }
        }
    }

    public void add(Train t) throws SQLException {
        String sql = "INSERT INTO trains (train_name, train_type, total_seats, fare_per_km) VALUES (?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getTrainName()); ps.setString(2, t.getTrainType()); ps.setInt(3, t.getTotalSeats()); ps.setDouble(4, t.getFarePerKm());
            ps.executeUpdate();
        }
    }

    public void update(Train t) throws SQLException {
        String sql = "UPDATE trains SET train_name=?, train_type=?, total_seats=?, fare_per_km=? WHERE train_id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getTrainName()); ps.setString(2, t.getTrainType()); ps.setInt(3, t.getTotalSeats()); ps.setDouble(4, t.getFarePerKm()); ps.setInt(5, t.getTrainId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM trains WHERE train_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id); ps.executeUpdate();
        }
    }
}