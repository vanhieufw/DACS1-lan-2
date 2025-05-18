package com.movie.dao;

import com.movie.model.BookingHistory;
import com.movie.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingHistoryDAO {
    public void addBookingHistory(BookingHistory history) throws SQLException {
        String query = "INSERT INTO BookingHistory (CustomerID, TicketID, BookingDate) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, history.getCustomerID());
            stmt.setInt(2, history.getTicketID());
            stmt.setTimestamp(3, new java.sql.Timestamp(history.getBookingDate().getTime()));
            stmt.executeUpdate();
        }
    }

    public List<BookingHistory> getBookingHistoryByCustomerId(int customerID) throws SQLException {
        List<BookingHistory> historyList = new ArrayList<>();
        String query = "SELECT bh.*, m.Title, r.RoomName, s.SeatNumber, t.Price " +
                "FROM BookingHistory bh " +
                "JOIN Ticket t ON bh.TicketID = t.TicketID " +
                "JOIN Showtime st ON t.ShowtimeID = st.ShowtimeID " +
                "JOIN Movie m ON st.MovieID = m.MovieID " +
                "JOIN Room r ON st.RoomID = r.RoomID " +
                "JOIN Seat s ON t.SeatID = s.SeatID " +
                "WHERE bh.CustomerID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BookingHistory history = new BookingHistory();
                    history.setHistoryID(rs.getInt("HistoryID"));
                    history.setCustomerID(rs.getInt("CustomerID"));
                    history.setTicketID(rs.getInt("TicketID")); // Corrected from setTicket tedavID
                    history.setBookingDate(rs.getTimestamp("BookingDate"));
                    history.setMovieTitle(rs.getString("Title"));
                    history.setRoomName(rs.getString("RoomName"));
                    history.setSeatNumber(rs.getString("SeatNumber"));
                    history.setPrice(rs.getDouble("Price"));
                    historyList.add(history);
                }
            }
        }
        return historyList;
    }
}