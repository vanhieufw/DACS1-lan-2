package com.movie.bus;

import com.movie.dao.CustomerDAO;
import com.movie.model.Customer;
import com.movie.util.PasswordEncrypter;
import java.sql.SQLException;

public class CustomerBUS {
    private CustomerDAO customerDAO = new CustomerDAO();

    public void registerCustomer(Customer customer) throws SQLException {
        if (customer.getFullName() == null || customer.getPassword() == null || customer.getEmail() == null) {
            throw new IllegalArgumentException("Thông tin không được để trống");
        }
        customer.setUsername(customer.getFullName()); // Giả định username = fullName (có thể thay đổi logic)
        customerDAO.insertCustomer(customer); // Thêm hàm insertCustomer vào CustomerDAO
    }

    public boolean validateUserPlain(String username, String password) {
        try {
            Customer customer = customerDAO.getCustomerByUsernameAndPassword(username, password);
            return customer != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}