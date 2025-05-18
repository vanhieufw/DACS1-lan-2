package com.movie.ui;

import com.movie.bus.MovieBUS;
import com.movie.bus.RoomBUS;
import com.movie.bus.ShowtimeBUS;
import com.movie.model.Movie;
import com.movie.model.Room;
import com.movie.model.Seat;
import com.movie.dao.MovieDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class AdminFrame extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private MovieBUS movieBUS = new MovieBUS();
    private RoomBUS roomBUS = new RoomBUS();
    private ShowtimeBUS showtimeBUS = new ShowtimeBUS();
    private MovieDAO movieDAO = new MovieDAO();
    private JPanel movieListPanel;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField durationField;
    private JTextField directorField;
    private JTextField genreField;
    private JTextField posterField;
    private JLabel posterPreview;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField productionYearField;
    private JTextField countryField;
    private JTextField ageRestrictionField;
    private JButton updateButton;
    private JButton deleteButton;
    private JPanel formPanel;
    private JLabel timeLabel;

    public AdminFrame() {
        initUI();
        startClock();
    }

    private void initUI() {
        setTitle("Quản lý bán vé xem phim - Hiếu");
        setSize(1200, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel mainView = createMainView();
        mainPanel.add(mainView, "MainView");

        add(mainPanel);
        setVisible(true);
    }

    private void startClock() {
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(Color.BLACK);
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        timePanel.add(timeLabel);
        add(timePanel, BorderLayout.NORTH);

        new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            while (true) {
                timeLabel.setText(sdf.format(new java.util.Date()));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private JPanel createMainView() {
        JPanel mainView = new JPanel(new BorderLayout());
        mainView.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(40, 40, 40));

        JButton homeButton = new JButton("Trang chủ");
        JButton infoButton = new JButton("Thông tin phim");
        JButton roomButton = new JButton("Phòng chiếu");
        JButton staffButton = new JButton("Nhân viên");
        JButton customerButton = new JButton("Khách hàng");
        JButton statsButton = new JButton("Thống kê");
        JButton logoutButton = new JButton("Đăng xuất");

        styleButton(homeButton);
        styleButton(infoButton);
        styleButton(roomButton);
        styleButton(staffButton);
        styleButton(customerButton);
        styleButton(statsButton);
        styleButton(logoutButton);

        homeButton.addActionListener(e -> showPanel("Trang chủ"));
        infoButton.addActionListener(e -> showPanel("Thông tin phim"));
        roomButton.addActionListener(e -> showPanel("Phòng chiếu"));
        staffButton.addActionListener(e -> showPanel("Nhân viên"));
        customerButton.addActionListener(e -> showPanel("Khách hàng"));
        statsButton.addActionListener(e -> showPanel("Thống kê"));
        logoutButton.addActionListener(e -> dispose());

        sidebar.add(Box.createVerticalStrut(30));
        sidebar.add(homeButton);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(infoButton);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(roomButton);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(staffButton);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(customerButton);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(statsButton);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(logoutButton);

        // Content area
        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(createHomePanel(), "Trang chủ");
        contentPanel.add(createInfoPanel(), "Thông tin phim");
        contentPanel.add(createRoomPanel(), "Phòng chiếu");
        contentPanel.add(createStaffPanel(), "Nhân viên");
        contentPanel.add(createCustomerPanel(), "Khách hàng");
        contentPanel.add(createStatsPanel(), "Thống kê");

        mainView.add(sidebar, BorderLayout.WEST);
        mainView.add(contentPanel, BorderLayout.CENTER);

        return mainView;
    }

    private void styleButton(JButton button) {
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(80, 80, 80));
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(60, 60, 60));
            }
        });
    }

    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, panelName);
    }

    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        JLabel titleLabel = new JLabel("Phòng chiếu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new BorderLayout(10, 10));
        mainContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel roomListPanel = new JPanel();
        roomListPanel.setLayout(new BoxLayout(roomListPanel, BoxLayout.Y_AXIS));
        JScrollPane roomScrollPane = new JScrollPane(roomListPanel);
        mainContent.add(roomScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Thêm phòng");
        buttonPanel.add(addButton);
        mainContent.add(buttonPanel, BorderLayout.SOUTH);

        loadRooms(roomListPanel);

        addButton.addActionListener(e -> showAddRoomDialog(roomListPanel));

        panel.add(mainContent, BorderLayout.CENTER);
        return panel;
    }

    private void loadRooms(JPanel roomListPanel) {
        try {
            roomListPanel.removeAll();
            List<Room> rooms = roomBUS.getAllRooms();
            for (Room room : rooms) {
                JPanel roomPanel = new JPanel(new BorderLayout());
                roomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                roomPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

                String priceStr = room.getPrice() > 0 ? String.format("%,.0f VND", room.getPrice()) : "Không có";
                String movieTitle = room.getMovieTitle() != null ? room.getMovieTitle() : "Không có";
                JLabel roomLabel = new JLabel(String.format("%02d - %s - Phim: %s - Trạng thái: %s - Giá vé: %s",
                        room.getRoomID(), room.getRoomName(), movieTitle, room.getStatus(), priceStr));
                roomLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                roomPanel.add(roomLabel, BorderLayout.CENTER);

                JPanel actionPanel = new JPanel(new FlowLayout());
                JButton editButton = new JButton("Sửa");
                JButton deleteButton = new JButton("Xóa");
                JButton viewSeatsButton = new JButton("Xem ghế");
                JButton addMovieButton = new JButton("Thêm phim");
                actionPanel.add(editButton);
                actionPanel.add(deleteButton);
                actionPanel.add(viewSeatsButton);
                actionPanel.add(addMovieButton);
                roomPanel.add(actionPanel, BorderLayout.EAST);

                editButton.addActionListener(e -> showEditRoomDialog(room, roomListPanel));
                deleteButton.addActionListener(e -> {
                    try {
                        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa phòng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            roomBUS.deleteRoom(room.getRoomID());
                            loadRooms(roomListPanel);
                            JOptionPane.showMessageDialog(this, "Xóa phòng thành công");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Không thể xóa phòng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                });
                viewSeatsButton.addActionListener(e -> showSeatStatusDialog(room));
                addMovieButton.addActionListener(e -> showAddMovieToRoomDialog(room, roomListPanel));

                roomListPanel.add(roomPanel);
            }
            roomListPanel.revalidate();
            roomListPanel.repaint();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách phòng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddRoomDialog(JPanel roomListPanel) {
        JDialog dialog = new JDialog(this, "Thêm phòng", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Tên phòng:"), gbc);
        JTextField nameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Số ghế:"), gbc);
        JTextField capacityField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(capacityField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Giá vé (VND):"), gbc);
        JTextField priceField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        inputPanel.add(priceField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            try {
                String roomName = nameField.getText().trim();
                int capacity = Integer.parseInt(capacityField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                if (roomName.isEmpty()) throw new IllegalArgumentException("Tên phòng không được để trống");
                roomBUS.addRoom(roomName, capacity, price);
                loadRooms(roomListPanel);
                JOptionPane.showMessageDialog(this, "Thêm phòng thành công");
                dialog.dispose();
            } catch (SQLException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Không thể thêm phòng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showEditRoomDialog(Room room, JPanel roomListPanel) {
        JDialog dialog = new JDialog(this, "Sửa phòng", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Tên phòng:"), gbc);
        JTextField nameField = new JTextField(room.getRoomName(), 20);
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Số ghế:"), gbc);
        JTextField capacityField = new JTextField(String.valueOf(room.getCapacity()), 20);
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(capacityField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Giá vé (VND):"), gbc);
        JTextField priceField = new JTextField(String.valueOf(room.getPrice()), 20);
        gbc.gridx = 1; gbc.gridy = 2;
        inputPanel.add(priceField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            try {
                room.setRoomName(nameField.getText().trim());
                room.setCapacity(Integer.parseInt(capacityField.getText().trim()));
                room.setPrice(Double.parseDouble(priceField.getText().trim()));
                roomBUS.updateRoom(room);
                loadRooms(roomListPanel);
                JOptionPane.showMessageDialog(this, "Cập nhật phòng thành công");
                dialog.dispose();
            } catch (SQLException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật phòng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showSeatStatusDialog(Room room) {
        JDialog dialog = new JDialog(this, "Trạng thái ghế - " + room.getRoomName(), true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JTextArea seatArea = new JTextArea();
        seatArea.setEditable(false);
        seatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        try {
            List<Seat> seats = room.getSeats();
            if (seats == null || seats.isEmpty()) {
                seatArea.append("Không có dữ liệu ghế cho phòng này.\n");
            } else {
                for (Seat seat : seats) {
                    seatArea.append("Ghế " + seat.getSeatNumber() + ": " + seat.getStatus() + "\n");
                }
            }
        } catch (Exception ex) {
            seatArea.append("Không thể tải trạng thái ghế: " + ex.getMessage());
        }
        dialog.add(new JScrollPane(seatArea), BorderLayout.CENTER);

        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showAddMovieToRoomDialog(Room room, JPanel roomListPanel) {
        JDialog dialog = new JDialog(this, "Thêm phim vào phòng " + room.getRoomName(), true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Phim:"), gbc);
        JComboBox<String> movieCombo = new JComboBox<>();
        try {
            List<Movie> movies = movieBUS.getAllMovies();
            for (Movie movie : movies) {
                movieCombo.addItem(movie.getTitle());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách phim: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(movieCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Ngày giờ chiếu (yyyy-MM-dd HH:mm):"), gbc);
        JTextField showDateField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(showDateField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            try {
                String movieTitle = (String) movieCombo.getSelectedItem();
                String showDateStr = showDateField.getText().trim();
                if (movieTitle == null || showDateStr.isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng chọn phim và nhập ngày giờ chiếu");
                }
                Movie selectedMovie = movieBUS.getAllMovies().stream()
                        .filter(m -> m.getTitle().equals(movieTitle))
                        .findFirst()
                        .orElse(null);
                if (selectedMovie == null) {
                    throw new IllegalArgumentException("Phim không tồn tại");
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                java.util.Date showDate = sdf.parse(showDateStr);
                roomBUS.addMovieToRoom(room.getRoomID(), selectedMovie.getMovieID(), showDate);
                loadRooms(roomListPanel);
                JOptionPane.showMessageDialog(this, "Thêm phim vào phòng thành công");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Không thể thêm phim: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        JLabel titleLabel = new JLabel("Thông tin phim", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new BorderLayout(10, 10));
        mainContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        movieListPanel = new JPanel();
        movieListPanel.setLayout(new BoxLayout(movieListPanel, BoxLayout.Y_AXIS));
        JScrollPane movieScrollPane = new JScrollPane(movieListPanel);
        mainContent.add(movieScrollPane, BorderLayout.CENTER);

        formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Tên phim:"), gbc);
        titleField = new JTextField(20);
        titleField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titleField, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Mô tả:"), gbc);
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Thời lượng (phút):"), gbc);
        durationField = new JTextField(20);
        durationField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(durationField, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Đạo diễn:"), gbc);
        directorField = new JTextField(20);
        directorField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(directorField, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Thể loại:"), gbc);
        genreField = new JTextField(20);
        genreField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(genreField, gbc);
        JButton selectGenreButton = new JButton("Chọn thể loại");
        selectGenreButton.setEnabled(false);
        gbc.gridx = 2; gbc.gridy = 4;
        formPanel.add(selectGenreButton, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Áp phích:"), gbc);
        posterField = new JTextField(20);
        posterField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 5;
        formPanel.add(posterField, gbc);
        JButton choosePosterButton = new JButton("Chọn hình ảnh");
        choosePosterButton.setEnabled(false);
        gbc.gridx = 2; gbc.gridy = 5;
        formPanel.add(choosePosterButton, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Ngày bắt đầu (yyyy-MM-dd):"), gbc);
        startDateField = new JTextField(20);
        startDateField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 6;
        formPanel.add(startDateField, gbc);
        JButton selectStartDateButton = new JButton("Chọn ngày");
        selectStartDateButton.setEnabled(false);
        gbc.gridx = 2; gbc.gridy = 6;
        formPanel.add(selectStartDateButton, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Ngày kết thúc (yyyy-MM-dd):"), gbc);
        endDateField = new JTextField(20);
        endDateField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 7;
        formPanel.add(endDateField, gbc);
        JButton selectEndDateButton = new JButton("Chọn ngày");
        selectEndDateButton.setEnabled(false);
        gbc.gridx = 2; gbc.gridy = 7;
        formPanel.add(selectEndDateButton, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("Năm sản xuất:"), gbc);
        productionYearField = new JTextField(20);
        productionYearField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 8; gbc.gridwidth = 2;
        formPanel.add(productionYearField, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 9;
        formPanel.add(new JLabel("Quốc gia:"), gbc);
        countryField = new JTextField(20);
        countryField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 9;
        formPanel.add(countryField, gbc);
        JButton selectCountryButton = new JButton("Chọn quốc gia");
        selectCountryButton.setEnabled(false);
        gbc.gridx = 2; gbc.gridy = 9;
        formPanel.add(selectCountryButton, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 10;
        formPanel.add(new JLabel("Giới hạn tuổi:"), gbc);
        ageRestrictionField = new JTextField(20);
        ageRestrictionField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 10; gbc.gridwidth = 2;
        formPanel.add(ageRestrictionField, gbc);

        posterPreview = new JLabel();
        posterPreview.setPreferredSize(new Dimension(150, 200));
        posterPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gbc.gridx = 3; gbc.gridy = 0; gbc.gridheight = 11; gbc.gridwidth = 1;
        formPanel.add(posterPreview, gbc);

        formPanel.setVisible(false);
        mainContent.add(formPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Thêm phim");
        buttonPanel.add(addButton);
        updateButton = new JButton("Cập nhật");
        updateButton.setEnabled(false);
        deleteButton = new JButton("Xóa");
        deleteButton.setEnabled(false);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        mainContent.add(buttonPanel, BorderLayout.SOUTH);

        loadMovies();

        addButton.addActionListener(e -> showAddMovieDialog());
        choosePosterButton.addActionListener(e -> choosePoster());
        selectGenreButton.addActionListener(e -> selectGenre());
        selectCountryButton.addActionListener(e -> selectCountry());
        selectStartDateButton.addActionListener(e -> selectDate(startDateField));
        selectEndDateButton.addActionListener(e -> selectDate(endDateField));
        updateButton.addActionListener(e -> updateMovie());
        deleteButton.addActionListener(e -> deleteMovie());

        panel.add(mainContent, BorderLayout.CENTER);
        return panel;
    }

    private void choosePoster() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String posterPath = selectedFile.getAbsolutePath();
            posterField.setText(posterPath);
            posterPreview.setIcon(new ImageIcon(new ImageIcon(posterPath).getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH)));
        }
    }

    private void selectGenre() {
        try {
            List<String> genres = movieBUS.getAllGenres();
            String selectedGenre = (String) JOptionPane.showInputDialog(
                    this, "Chọn thể loại:", "Lựa chọn thể loại",
                    JOptionPane.PLAIN_MESSAGE, null, genres.toArray(), genres.get(0));
            if (selectedGenre != null) {
                genreField.setText(selectedGenre);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách thể loại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectCountry() {
        try {
            List<String> countries = movieBUS.getAllCountries();
            String selectedCountry = (String) JOptionPane.showInputDialog(
                    this, "Chọn quốc gia:", "Lựa chọn quốc gia",
                    JOptionPane.PLAIN_MESSAGE, null, countries.toArray(), countries.get(0));
            if (selectedCountry != null) {
                countryField.setText(selectedCountry);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách quốc gia: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectDate(JTextField dateField) {
        JDialog dateDialog = new JDialog(this, "Chọn ngày", true);
        dateDialog.setSize(300, 150);
        dateDialog.setLocationRelativeTo(this);
        dateDialog.setLayout(new BorderLayout());

        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateDialog.add(dateSpinner, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Hủy");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        dateDialog.add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateField.setText(sdf.format(dateSpinner.getValue()));
            dateDialog.dispose();
        });
        cancelButton.addActionListener(e -> dateDialog.dispose());

        dateDialog.setVisible(true);
    }

    private void showAddMovieDialog() {
        JDialog dialog = new JDialog(this, "Thêm phim", true);
        dialog.setSize(600, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setBackground(new Color(245, 245, 245));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Tên phim:"), gbc);
        JTextField tempTitleField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        inputPanel.add(tempTitleField, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Mô tả:"), gbc);
        JTextArea tempDescriptionArea = new JTextArea(3, 20);
        tempDescriptionArea.setLineWrap(true);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        inputPanel.add(new JScrollPane(tempDescriptionArea), gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Thời lượng (phút):"), gbc);
        JTextField tempDurationField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        inputPanel.add(tempDurationField, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Đạo diễn:"), gbc);
        JTextField tempDirectorField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        inputPanel.add(tempDirectorField, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Thể loại:"), gbc);
        JTextField tempGenreField = new JTextField(20);
        tempGenreField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 4;
        inputPanel.add(tempGenreField, gbc);
        JButton tempSelectGenreButton = new JButton("Chọn thể loại");
        gbc.gridx = 2; gbc.gridy = 4;
        inputPanel.add(tempSelectGenreButton, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 5;
        inputPanel.add(new JLabel("Áp phích:"), gbc);
        JTextField tempPosterField = new JTextField(20);
        tempPosterField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 5;
        inputPanel.add(tempPosterField, gbc);
        JButton tempChoosePosterButton = new JButton("Chọn hình ảnh");
        gbc.gridx = 2; gbc.gridy = 5;
        inputPanel.add(tempChoosePosterButton, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 6;
        inputPanel.add(new JLabel("Ngày bắt đầu (yyyy-MM-dd):"), gbc);
        JTextField tempStartDateField = new JTextField(20);
        tempStartDateField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 6;
        inputPanel.add(tempStartDateField, gbc);
        JButton tempSelectStartDateButton = new JButton("Chọn ngày");
        gbc.gridx = 2; gbc.gridy = 6;
        inputPanel.add(tempSelectStartDateButton, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 7;
        inputPanel.add(new JLabel("Ngày kết thúc (yyyy-MM-dd):"), gbc);
        JTextField tempEndDateField = new JTextField(20);
        tempEndDateField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 7;
        inputPanel.add(tempEndDateField, gbc);
        JButton tempSelectEndDateButton = new JButton("Chọn ngày");
        gbc.gridx = 2; gbc.gridy = 7;
        inputPanel.add(tempSelectEndDateButton, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 8;
        inputPanel.add(new JLabel("Năm sản xuất:"), gbc);
        JTextField tempProductionYearField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 8; gbc.gridwidth = 2;
        inputPanel.add(tempProductionYearField, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 9;
        inputPanel.add(new JLabel("Quốc gia:"), gbc);
        JTextField tempCountryField = new JTextField(20);
        tempCountryField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 9;
        inputPanel.add(tempCountryField, gbc);
        JButton tempSelectCountryButton = new JButton("Chọn quốc gia");
        gbc.gridx = 2; gbc.gridy = 9;
        inputPanel.add(tempSelectCountryButton, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 10;
        inputPanel.add(new JLabel("Giới hạn tuổi:"), gbc);
        JTextField tempAgeRestrictionField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 10; gbc.gridwidth = 2;
        inputPanel.add(tempAgeRestrictionField, gbc);

        JLabel tempPosterPreview = new JLabel();
        tempPosterPreview.setPreferredSize(new Dimension(150, 200));
        tempPosterPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gbc.gridx = 3; gbc.gridy = 0; gbc.gridheight = 11;
        inputPanel.add(tempPosterPreview, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        tempChoosePosterButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String posterPath = selectedFile.getAbsolutePath();
                tempPosterField.setText(posterPath);
                tempPosterPreview.setIcon(new ImageIcon(new ImageIcon(posterPath).getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH)));
            }
        });

        tempSelectGenreButton.addActionListener(e -> {
            try {
                List<String> genres = movieBUS.getAllGenres();
                String selectedGenre = (String) JOptionPane.showInputDialog(
                        dialog, "Chọn thể loại:", "Lựa chọn thể loại",
                        JOptionPane.PLAIN_MESSAGE, null, genres.toArray(), genres.get(0));
                if (selectedGenre != null) {
                    tempGenreField.setText(selectedGenre);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Không thể tải danh sách thể loại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        tempSelectCountryButton.addActionListener(e -> {
            try {
                List<String> countries = movieBUS.getAllCountries();
                String selectedCountry = (String) JOptionPane.showInputDialog(
                        dialog, "Chọn quốc gia:", "Lựa chọn quốc gia",
                        JOptionPane.PLAIN_MESSAGE, null, countries.toArray(), countries.get(0));
                if (selectedCountry != null) {
                    tempCountryField.setText(selectedCountry);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Không thể tải danh sách quốc gia: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        tempSelectStartDateButton.addActionListener(e -> selectDate(tempStartDateField));
        tempSelectEndDateButton.addActionListener(e -> selectDate(tempEndDateField));

        saveButton.addActionListener(e -> {
            try {
                Movie movie = new Movie();
                movie.setTitle(tempTitleField.getText().trim());
                movie.setDescription(tempDescriptionArea.getText().trim());
                String durationStr = tempDurationField.getText().trim();
                movie.setDuration(durationStr.isEmpty() ? 0 : Integer.parseInt(durationStr));
                movie.setDirector(tempDirectorField.getText().trim());
                String genreName = tempGenreField.getText().trim();
                movie.setGenreID(genreName.isEmpty() ? 0 : movieDAO.getGenreIdByName(genreName));
                movie.setPoster(tempPosterField.getText().trim());
                String startDateStr = tempStartDateField.getText().trim();
                movie.setStartDate(startDateStr.isEmpty() ? null : Date.valueOf(startDateStr));
                String endDateStr = tempEndDateField.getText().trim();
                movie.setEndDate(endDateStr.isEmpty() ? null : Date.valueOf(endDateStr));
                String prodYearStr = tempProductionYearField.getText().trim();
                movie.setProductionYear(prodYearStr.isEmpty() ? 0 : Integer.parseInt(prodYearStr));
                String countryName = tempCountryField.getText().trim();
                movie.setCountryID(countryName.isEmpty() ? 0 : movieDAO.getCountryIdByName(countryName));
                String ageRestrictionStr = tempAgeRestrictionField.getText().trim();
                movie.setAgeRestriction(ageRestrictionStr.isEmpty() ? 0 : Integer.parseInt(ageRestrictionStr));

                if (movie.getTitle().isEmpty()) throw new IllegalArgumentException("Tên phim không được để trống");
                if (!movie.getPoster().isEmpty() && !movie.getPoster().matches(".*\\.(jpg|jpeg|png|gif)$")) {
                    throw new IllegalArgumentException("Định dạng áp phích không hợp lệ");
                }

                movieBUS.addMovie(movie);
                JOptionPane.showMessageDialog(this, "Thêm phim thành công");
                clearForm();
                loadMovies();
                dialog.dispose();
            } catch (SQLException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Không thể thêm phim: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void updateMovie() {
        try {
            Movie movie = new Movie();
            movie.setMovieID(Integer.parseInt(formPanel.getName()));
            movie.setTitle(titleField.getText().trim());
            movie.setDescription(descriptionArea.getText().trim());
            String durationStr = durationField.getText().trim();
            movie.setDuration(durationStr.isEmpty() ? 0 : Integer.parseInt(durationStr));
            movie.setDirector(directorField.getText().trim());
            String genreName = genreField.getText().trim();
            movie.setGenreID(genreName.isEmpty() ? 0 : movieDAO.getGenreIdByName(genreName));
            movie.setPoster(posterField.getText().trim());
            String startDateStr = startDateField.getText().trim();
            movie.setStartDate(startDateStr.isEmpty() ? null : Date.valueOf(startDateStr));
            String endDateStr = endDateField.getText().trim();
            movie.setEndDate(endDateStr.isEmpty() ? null : Date.valueOf(endDateStr));
            String prodYearStr = productionYearField.getText().trim();
            movie.setProductionYear(prodYearStr.isEmpty() ? 0 : Integer.parseInt(prodYearStr));
            String countryName = countryField.getText().trim();
            movie.setCountryID(countryName.isEmpty() ? 0 : movieDAO.getCountryIdByName(countryName));
            String ageRestrictionStr = ageRestrictionField.getText().trim();
            movie.setAgeRestriction(ageRestrictionStr.isEmpty() ? 0 : Integer.parseInt(ageRestrictionStr));

            if (movie.getTitle().isEmpty()) throw new IllegalArgumentException("Tên phim không được để trống");
            if (!movie.getPoster().isEmpty() && !movie.getPoster().matches(".*\\.(jpg|jpeg|png|gif)$")) {
                throw new IllegalArgumentException("Định dạng áp phích không hợp lệ");
            }

            movieBUS.updateMovie(movie);
            JOptionPane.showMessageDialog(this, "Cập nhật phim thành công");
            clearForm();
            loadMovies();
        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Không thể cập nhật phim: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMovie() {
        try {
            int movieID = Integer.parseInt(formPanel.getName());
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa phim này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                movieBUS.deleteMovie(movieID);
                JOptionPane.showMessageDialog(this, "Xóa phim thành công");
                clearForm();
                loadMovies();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Không thể xóa phim: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMovies() {
        try {
            movieListPanel.removeAll();
            List<Movie> movies = movieBUS.getAllMovies();
            for (Movie movie : movies) {
                JPanel moviePanel = new JPanel(new BorderLayout());
                moviePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                moviePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

                JLabel posterLabel = new JLabel();
                if (movie.getPoster() != null && !movie.getPoster().isEmpty()) {
                    posterLabel.setIcon(new ImageIcon(new ImageIcon(movie.getPoster()).getImage().getScaledInstance(200, 140, Image.SCALE_SMOOTH)));
                }
                posterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                moviePanel.add(posterLabel, BorderLayout.WEST);

                JPanel infoPanel = new JPanel(new BorderLayout());
                JLabel titleLabel = new JLabel(movie.getTitle());
                titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                infoPanel.add(titleLabel, BorderLayout.NORTH);

                JTextArea descriptionArea = new JTextArea(movie.getDescription());
                descriptionArea.setLineWrap(true);
                descriptionArea.setWrapStyleWord(true);
                descriptionArea.setEditable(false);
                descriptionArea.setBackground(moviePanel.getBackground());
                descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                infoPanel.add(descriptionArea, BorderLayout.CENTER);

                moviePanel.add(infoPanel, BorderLayout.CENTER);

                moviePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        formPanel.setVisible(true);
                        formPanel.setName(String.valueOf(movie.getMovieID()));
                        titleField.setText(movie.getTitle());
                        AdminFrame.this.descriptionArea.setText(movie.getDescription());
                        durationField.setText(String.valueOf(movie.getDuration()));
                        directorField.setText(movie.getDirector());
                        genreField.setText(movie.getGenreName());
                        posterField.setText(movie.getPoster());
                        posterPreview.setIcon(movie.getPoster() == null || movie.getPoster().isEmpty() ? null :
                                new ImageIcon(new ImageIcon(movie.getPoster()).getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH)));
                        startDateField.setText(movie.getStartDate() != null ? movie.getStartDate().toString() : "");
                        endDateField.setText(movie.getEndDate() != null ? movie.getEndDate().toString() : "");
                        productionYearField.setText(String.valueOf(movie.getProductionYear()));
                        countryField.setText(movie.getCountryName());
                        ageRestrictionField.setText(String.valueOf(movie.getAgeRestriction()));
                        titleField.setEditable(true);
                        AdminFrame.this.descriptionArea.setEditable(true);
                        durationField.setEditable(true);
                        directorField.setEditable(true);
                        ageRestrictionField.setEditable(true);
                        updateButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                        for (Component c : formPanel.getComponents()) {
                            if (c instanceof JButton) {
                                c.setEnabled(true);
                            }
                        }
                    }
                });

                movieListPanel.add(moviePanel);
            }
            movieListPanel.revalidate();
            movieListPanel.repaint();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách phim: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        formPanel.setVisible(false);
        formPanel.setName(null);
        titleField.setText("");
        descriptionArea.setText("");
        durationField.setText("");
        directorField.setText("");
        genreField.setText("");
        posterField.setText("");
        posterPreview.setIcon(null);
        startDateField.setText("");
        endDateField.setText("");
        productionYearField.setText("");
        countryField.setText("");
        ageRestrictionField.setText("");
        titleField.setEditable(false);
        descriptionArea.setEditable(false);
        durationField.setEditable(false);
        directorField.setEditable(false);
        ageRestrictionField.setEditable(false);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        for (Component c : formPanel.getComponents()) {
            if (c instanceof JButton) {
                c.setEnabled(false);
            }
        }
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        JLabel titleLabel = new JLabel("Trang chủ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JLabel welcomeLabel = new JLabel("Chào mừng đến với Hệ thống Quản lý Bán vé xem phim!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0;
        mainContent.add(welcomeLabel, gbc);

        JLabel infoLabel = new JLabel("<html>Sử dụng menu bên trái để quản lý phim, phòng chiếu, nhân viên, khách hàng và thống kê.<br>" +
                "Hệ thống hỗ trợ cập nhật trạng thái phòng chiếu và ghế theo thời gian thực.</html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        mainContent.add(infoLabel, gbc);

        panel.add(mainContent, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStaffPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        JLabel titleLabel = new JLabel("Quản lý nhân viên", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JLabel placeholderLabel = new JLabel("Chức năng quản lý nhân viên đang được phát triển.");
        placeholderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0;
        mainContent.add(placeholderLabel, gbc);

        panel.add(mainContent, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        JLabel titleLabel = new JLabel("Quản lý khách hàng", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JLabel placeholderLabel = new JLabel("Chức năng quản lý khách hàng đang được phát triển.");
        placeholderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0;
        mainContent.add(placeholderLabel, gbc);

        panel.add(mainContent, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        JLabel titleLabel = new JLabel("Thống kê", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JLabel placeholderLabel = new JLabel("Chức năng thống kê doanh thu và vé đang được phát triển.");
        placeholderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0;
        mainContent.add(placeholderLabel, gbc);

        panel.add(mainContent, BorderLayout.CENTER);
        return panel;
    }
}