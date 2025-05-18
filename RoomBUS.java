package com.movie.bus;

import com.movie.dao.RoomDAO;
import com.movie.dao.ShowtimeDAO;
import com.movie.model.Room;
import com.movie.model.Showtime;

import java.sql.SQLException;
import java.util.List;

public class RoomBUS {
    private RoomDAO roomDAO = new RoomDAO();
    private ShowtimeDAO showtimeDAO = new ShowtimeDAO();

    public void addRoom(String roomName, int capacity, double price) throws SQLException {
        Room room = new Room();
        room.setRoomName(roomName);
        room.setCapacity(capacity);
        room.setPrice(price);
        roomDAO.addRoom(room);
    }

    public void updateRoom(Room room) throws SQLException {
        roomDAO.updateRoom(room);
    }

    public void deleteRoom(int roomID) throws SQLException {
        roomDAO.deleteRoom(roomID);
    }

    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAllRooms();
    }

    public Room getRoomById(int roomID) throws SQLException {
        return roomDAO.getRoomById(roomID);
    }

    public void addMovieToRoom(int roomID, int movieID, java.util.Date showDate) throws SQLException {
        Showtime showtime = new Showtime();
        showtime.setRoomID(roomID);
        showtime.setMovieID(movieID);
        showtime.setShowDate(showDate);
        showtimeDAO.addShowtime(showtime);
    }
}