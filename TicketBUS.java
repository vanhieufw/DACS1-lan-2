package com.movie.bus;

import com.movie.dao.BookingHistoryDAO;
import com.movie.dao.TicketDAO;
import com.movie.model.BookingHistory;
import com.movie.model.Seat;
import com.movie.model.Ticket;
import com.movie.network.SocketClient;
import com.movie.network.ThreadManager;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TicketBUS {
    private final TicketDAO ticketDAO = new TicketDAO();
    private final BookingHistoryDAO bookingHistoryDAO = new BookingHistoryDAO();

    public String processPayment(int customerID, int showtimeID, List<Seat> seats, double totalPrice) throws SQLException {
        for (Seat seat : seats) {
            if (ticketDAO.isSeatBooked(seat.getSeatID(), showtimeID)) {
                return "Ghế " + seat.getSeatNumber() + " đã được đặt!";
            }
        }

        for (Seat seat : seats) {
            Ticket ticket = new Ticket();
            ticket.setCustomerID(customerID);
            ticket.setShowtimeID(showtimeID);
            ticket.setSeatID(seat.getSeatID());
            ticket.setPrice(totalPrice / seats.size());
            ticketDAO.bookTicket(ticket);

            BookingHistory history = new BookingHistory();
            history.setCustomerID(customerID);
            history.setTicketID(ticket.getTicketID());
            history.setBookingDate(new Date());
            bookingHistoryDAO.addBookingHistory(history);
        }

        ThreadManager.execute(() -> {
            try {
                SocketClient client = new SocketClient("localhost", 8080); // Thay bằng địa chỉ và cổng thực tế
                client.sendMessage("Đặt vé thành công cho khách hàng ID: " + customerID);
            } catch (Exception e) {
                System.err.println("Lỗi khi gửi tin nhắn: " + e.getMessage());
            }
        });

        return "Thanh toán thành công!";
    }

    public List<BookingHistory> getBookingHistory(int customerID) throws SQLException {
        return bookingHistoryDAO.getBookingHistoryByCustomerId(customerID);
    }
}