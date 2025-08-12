package dao;

import db.DBUtil;
import model.Booking;
import model.Train;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class BookingDAO {
    // For this simplified model, we compute available seats by counting already booked seats for the train on that date
    public int seatsBooked(int trainId, Date date) throws SQLException {
        String sql = "SELECT COALESCE(SUM(seat_count),0) as s FROM bookings WHERE train_id = ? AND date_of_journey = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, trainId); ps.setDate(2, date);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("s");
                return 0;
            }
        }
    }

    public void createBooking(Booking b) throws SQLException {
        // transaction: check seats then insert
        Connection c = null;
        try {
            c = DBUtil.getConnection();
            c.setAutoCommit(false);

            TrainDAO tdao = new TrainDAO();
            Train t = tdao.findById(b.getTrainId());
            if (t == null) throw new SQLException("Train not found");

            int booked = seatsBooked(b.getTrainId(), b.getDateOfJourney());
            int available = t.getTotalSeats() - booked;
            if (b.getSeatCount() > available) throw new SQLException("Not enough seats. Available: " + available);

            String sql = "INSERT INTO bookings (train_id, source_station, destination_station, date_of_journey, seat_count, total_fare, passenger_name, passenger_contact) VALUES (?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, b.getTrainId());
                ps.setString(2, b.getSource());
                ps.setString(3, b.getDestination());
                ps.setDate(4, b.getDateOfJourney());
                ps.setInt(5, b.getSeatCount());
                ps.setDouble(6, b.getTotalFare());
                ps.setString(7, b.getPassengerName());
                ps.setString(8, b.getPassengerContact());
                ps.executeUpdate();
            }

            c.commit();
        } catch (SQLException ex) {
            if (c != null) try { c.rollback(); } catch (SQLException ignore) {}
            throw ex;
        } finally {
            if (c != null) try { c.setAutoCommit(true); c.close(); } catch (SQLException ignore) {}
        }
    }

    public List<Booking> listByPassenger(String passengerName) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE passenger_name = ? ORDER BY date_of_journey DESC";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, passengerName);
            try (ResultSet rs = ps.executeQuery()) {
                List<Booking> list = new ArrayList<>();
                while (rs.next()) {
                    Booking b = new Booking();
                    b.setBookingId(rs.getInt("booking_id"));
                    b.setTrainId(rs.getInt("train_id"));
                    b.setSource(rs.getString("source_station"));
                    b.setDestination(rs.getString("destination_station"));
                    b.setDateOfJourney(rs.getDate("date_of_journey"));
                    b.setSeatCount(rs.getInt("seat_count"));
                    b.setTotalFare(rs.getDouble("total_fare"));
                    b.setPassengerName(rs.getString("passenger_name"));
                    b.setPassengerContact(rs.getString("passenger_contact"));
                    list.add(b);
                }
                return list;
            }
        }
    }

    public List<Map<String,Object>> listAllBookings() throws SQLException {
        String sql = "SELECT b.*, t.train_name FROM bookings b JOIN trains t ON b.train_id = t.train_id ORDER BY date_of_journey DESC";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<Map<String,Object>> rows = new ArrayList<>();
            while (rs.next()) {
                Map<String,Object> m = new HashMap<>();
                m.put("booking_id", rs.getInt("booking_id"));
                m.put("train_name", rs.getString("train_name"));
                m.put("source", rs.getString("source_station"));
                m.put("destination", rs.getString("destination_station"));
                m.put("date", rs.getDate("date_of_journey"));
                m.put("seats", rs.getInt("seat_count"));
                m.put("fare", rs.getDouble("total_fare"));
                m.put("passenger", rs.getString("passenger_name"));
                rows.add(m);
            }
            return rows;
        }
    }
    
    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Booking b = new Booking();
                b.setBookingId(rs.getInt("booking_id"));
                b.setTrainId(rs.getInt("train_id"));
                b.setSource(rs.getString("source"));
                b.setDestination(rs.getString("destination"));
                b.setDateOfJourney(rs.getDate("date_of_journey"));
                b.setSeatCount(rs.getInt("seat_count"));
                b.setTotalFare(rs.getDouble("total_fare"));
                b.setPassengerName(rs.getString("passenger_name"));
                b.setPassengerContact(rs.getString("passenger_contact"));
                list.add(b);
            }
        }
        return list;
    }


    public void cancelBooking(int bookingId) throws SQLException {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingId); ps.executeUpdate();
        }
    }
}