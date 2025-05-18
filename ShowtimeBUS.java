package com.movie.bus;

import com.movie.dao.ShowtimeDAO;
import com.movie.model.Showtime;

import java.sql.SQLException;
import java.util.List;

public class ShowtimeBUS {
    private ShowtimeDAO showtimeDAO = new ShowtimeDAO();

    public void updateShowtimes() {
        // Logic cập nhật trạng thái suất chiếu (nếu cần)
        // Hiện tại chỉ gọi để trigger DataUpdater
    }

    public void addShowtime(int movieID, int roomID, java.util.Date showDate) throws SQLException {
        Showtime showtime = new Showtime();
        showtime.setMovieID(movieID);
        showtime.setRoomID(roomID);
        showtime.setShowDate(showDate);
        showtimeDAO.addShowtime(showtime);
    }

    public List<Showtime> getShowtimesByRoomId(int roomID) throws SQLException {
        return showtimeDAO.getShowtimesByRoomId(roomID);
    }
}