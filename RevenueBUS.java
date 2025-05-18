package com.movie.bus;

import com.movie.dao.RevenueDAO;
import java.sql.SQLException;

public class RevenueBUS {
    private RevenueDAO revenueDAO = new RevenueDAO();

    public String getTotalRevenue() {
        try {
            double revenue = revenueDAO.getTotalRevenue();
            return String.format("%.2f VND", revenue);
        } catch (SQLException e) {
            e.printStackTrace();
            return "0.00 VND";
        }
    }
}
