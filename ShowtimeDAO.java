package com.movie.dao;

import com.movie.model.Showtime;
import com.movie.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeDAO {
    public void addShowtime(Showtime showtime) throws SQLException {
        String query = "INSERT INTO Showtime (MovieID, RoomID, ShowDate) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, showtime.getMovieID());
            stmt.setInt(2, showtime.getRoomID());
            stmt.setTimestamp(3, new java.sql.Timestamp(showtime.getShowDate().getTime()));
            stmt.executeUpdate();
        }
    }

    public List<Showtime> getShowtimesByRoomId(int roomID) throws SQLException {
        List<Showtime> showtimes = new ArrayList<>();
        String query = "SELECT s.*, m.Title, m.StartDate, m.EndDate, m.Duration " +
                "FROM Showtime s " +
                "JOIN Movie m ON s.MovieID = m.MovieID " +
                "WHERE s.RoomID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roomID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Showtime showtime = new Showtime();
                    showtime.setShowtimeID(rs.getInt("ShowtimeID"));
                    showtime.setMovieID(rs.getInt("MovieID"));
                    showtime.setRoomID(rs.getInt("RoomID"));
                    showtime.setShowDate(rs.getTimestamp("ShowDate"));
                    showtimes.add(showtime);
                }
            }
        }
        return showtimes;
    }
}