package ui;

import dao.TrainDAO;
import dao.StationDAO;
import dao.BookingDAO;
import model.Train;
import model.Booking;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class MainApp {
    private JFrame frame;
    private TrainDAO trainDAO = new TrainDAO();
    private StationDAO stationDAO = new StationDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private JTabbedPane tabs;
    private boolean isAdminLoggedIn = false;
    
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color DARK_GRAY = new Color(52, 58, 64);

    private static void applyThemedOptionPane() {
        // Backgrounds
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);

        // Fonts
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 13));

        // Borders and spacing
        UIManager.put("OptionPane.border", BorderFactory.createEmptyBorder(15, 15, 15, 15));
        UIManager.put("Panel.border", BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons
        UIManager.put("Button.background", PRIMARY_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        

        UIManager.put("Label.background", Color.WHITE);
        UIManager.put("Label.opaque", false);
    }

    public static void main(String[] args) {
    	try {
    	    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
    	        if ("Nimbus".equals(info.getName())) {
    	            UIManager.setLookAndFeel(info.getClassName());
    	            break;
    	        }
    	    }
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}

    	applyThemedOptionPane(); // Make all JOptionPane dialogs match theme

        
        SwingUtilities.invokeLater(() -> new MainApp().createAndShowGUI());
    }
        

    private void createAndShowGUI() {
    	
    	frame = new JFrame("🚄 Railway Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(LIGHT_GRAY);

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(Color.WHITE);
        tabs.addTab("Passenger", createPassengerPanel());
        tabs.addTab("Admin", createAdminLoginPanel());

        // Set passenger tab as default
        tabs.setSelectedIndex(0);

        // Add listener to handle admin tab click
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 1 && !isAdminLoggedIn) {
                showAdminLogin();
            }
        });

        frame.add(tabs);
        frame.setVisible(true);
    }
    
    public JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    public JTable createStyledTable() {
        JTable table = new JTable();
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(PRIMARY_COLOR.brighter());
        table.setSelectionForeground(Color.WHITE);
        return table;
    }

    public JPanel createTitledPanel(String title, String icon) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(icon + " " + title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(DARK_GRAY);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        return panel;
    }
    
    
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    public void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }


    // ---------- Admin Login Panel ----------
    private JPanel createAdminLoginPanel() {
        JPanel mainPanel = createTitledPanel("Admin Access Required", "");
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel messageLabel = new JLabel("<html><center>Please authenticate to access<br>administrative features</center></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(DARK_GRAY);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(messageLabel, gbc);

        JButton loginButton = createStyledButton("Login as Admin", PRIMARY_COLOR, Color.WHITE);
        loginButton.setPreferredSize(new Dimension(180, 45));
        loginButton.addActionListener(e -> showAdminLogin());

        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(loginButton, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    private void showAdminLogin() {
        JDialog loginDialog = new JDialog(frame, "Admin Login", true);
        loginDialog.setLayout(new BorderLayout());
        loginDialog.setSize(350, 250);
        loginDialog.setLocationRelativeTo(frame);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel(" Administrator Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(DARK_GRAY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        content.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0; gbc.gridy = 1;
        content.add(userLabel, gbc);

        JTextField userField = new JTextField(15);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        content.add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        content.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(15);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        content.add(passField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton loginBtn = createStyledButton("Login", SUCCESS_COLOR, Color.WHITE);
        JButton cancelBtn = createStyledButton("Cancel", DANGER_COLOR, Color.WHITE);

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("admin") && pass.equals("admin123")) {
                isAdminLoggedIn = true;
                tabs.setComponentAt(1, createAdminPanel());
                tabs.setTitleAt(1, " Admin Panel");
                loginDialog.dispose();
                showSuccessMessage("Welcome Admin! Login successful.");
            } else {
                showErrorMessage("Invalid credentials. Please try again.");
                passField.setText("");
            }
        });

        cancelBtn.addActionListener(e -> {
            loginDialog.dispose();
            tabs.setSelectedIndex(0);
        });

        buttonPanel.add(loginBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        content.add(buttonPanel, gbc);

        loginDialog.add(content);
        loginDialog.setVisible(true);
    }

    // ---------- Admin Panel ----------
    private JPanel createAdminPanel() {
        JPanel mainPanel = createTitledPanel("Administration Dashboard", "");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnRefresh = createStyledButton(" Refresh", PRIMARY_COLOR, Color.WHITE);
        JButton btnAddTrain = createStyledButton(" Add Train", SUCCESS_COLOR, Color.WHITE);
        JButton btnAddStation = createStyledButton(" Add Station", WARNING_COLOR, Color.WHITE);
        JButton btnViewBookings = createStyledButton(" View Bookings", PRIMARY_COLOR, Color.WHITE);
        JButton btnLogout = createStyledButton("Logout", DANGER_COLOR, Color.WHITE);
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAddTrain);
        buttonPanel.add(btnAddStation);
        buttonPanel.add(btnViewBookings);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(btnLogout);

        JTable table = createStyledTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder("Train Management"));

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(sp, BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadTrainsToTable(table));
        btnAddTrain.addActionListener(e -> showAddTrainDialog(table));
        btnAddStation.addActionListener(e -> showAddStationDialog());
        btnViewBookings.addActionListener(e -> showAllBookingsDialog());
        btnLogout.addActionListener(e -> {
            isAdminLoggedIn = false;
            tabs.setComponentAt(1, createAdminLoginPanel());
            tabs.setTitleAt(1, " Admin");
            tabs.setSelectedIndex(0);
            showInfoMessage("Logged out successfully");
        });

        loadTrainsToTable(table);
        return mainPanel;
    }

    private void loadTrainsToTable(JTable table) {
        try {
            List<Train> list = trainDAO.listAll();
            String[] cols = {"ID", "Train Name", "Type", "Total Seats", "Fare/km (₹)"};
            DefaultTableModel m = new DefaultTableModel(cols, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            for (Train t : list) {
                m.addRow(new Object[]{t.getTrainId(), t.getTrainName(), t.getTrainType(), t.getTotalSeats(), "₹" + t.getFarePerKm()});
            }
            table.setModel(m);
            
            // Center align numeric columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        } catch (Exception ex) {
            showErr(ex);
        }
    }

    private void showAddTrainDialog(JTable table) {
        JDialog dialog = new JDialog(frame, "Add New Train", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(frame);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField name = new JTextField(20);
        JTextField type = new JTextField(20);
        JTextField seats = new JTextField(20);
        JTextField fare = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        content.add(new JLabel("Train Name:"), gbc);
        gbc.gridx = 1;
        content.add(name, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        content.add(new JLabel("Train Type:"), gbc);
        gbc.gridx = 1;
        content.add(type, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        content.add(new JLabel("Total Seats:"), gbc);
        gbc.gridx = 1;
        content.add(seats, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        content.add(new JLabel("Fare per km:"), gbc);
        gbc.gridx = 1;
        content.add(fare, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addBtn = createStyledButton("Add Train", SUCCESS_COLOR, Color.WHITE);
        JButton cancelBtn = createStyledButton("Cancel", DANGER_COLOR, Color.WHITE);

        addBtn.addActionListener(e -> {
            try {
                Train t = new Train();
                t.setTrainName(name.getText().trim());
                t.setTrainType(type.getText().trim());
                t.setTotalSeats(Integer.parseInt(seats.getText().trim()));
                t.setFarePerKm(Double.parseDouble(fare.getText().trim()));
                trainDAO.add(t);
                loadTrainsToTable(table);
                dialog.dispose();
                showSuccessMessage("Train added successfully!");
            } catch (NumberFormatException ex) {
                showErrorMessage("Please enter valid numbers for seats and fare.");
            } catch (Exception ex) {
                showErr(ex);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        content.add(buttonPanel, gbc);

        dialog.add(content);
        dialog.setVisible(true);
    }

    private void showAddStationDialog() {
        JDialog dialog = new JDialog(frame, "Add New Station", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(frame);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField name = new JTextField(20);
        JTextField city = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        content.add(new JLabel("Station Name:"), gbc);
        gbc.gridx = 1;
        content.add(name, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        content.add(new JLabel("City:"), gbc);
        gbc.gridx = 1;
        content.add(city, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addBtn = createStyledButton("Add Station", SUCCESS_COLOR, Color.WHITE);
        JButton cancelBtn = createStyledButton("Cancel", DANGER_COLOR, Color.WHITE);

        addBtn.addActionListener(e -> {
            try {
                stationDAO.add(name.getText().trim(), city.getText().trim());
                dialog.dispose();
                showSuccessMessage("Station added successfully!");
            } catch (Exception ex) {
                showErr(ex);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        content.add(buttonPanel, gbc);

        dialog.add(content);
        dialog.setVisible(true);
    }

    private void showAllBookingsDialog() {
        try {
            List<Map<String, Object>> rows = bookingDAO.listAllBookings();
            
            JDialog dialog = new JDialog(frame, "All Bookings Management", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(900, 600);
            dialog.setLocationRelativeTo(frame);

            JPanel content = new JPanel(new BorderLayout());
            content.setBackground(Color.WHITE);
            content.setBorder(new EmptyBorder(15, 15, 15, 15));

            String[] cols = {"ID", "Train", "Source", "Destination", "Date", "Seats", "Fare", "Passenger"};
            DefaultTableModel m = new DefaultTableModel(cols, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            
            for (Map<String, Object> r : rows) {
                m.addRow(new Object[]{r.get("booking_id"), r.get("train_name"), r.get("source"), 
                    r.get("destination"), r.get("date"), r.get("seats"), "₹" + r.get("fare"), r.get("passenger")});
            }
            
            JTable table = createStyledTable();
            table.setModel(m);
            JScrollPane sp = new JScrollPane(table);
            sp.setBorder(BorderFactory.createTitledBorder("Booking Records"));

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setBackground(Color.WHITE);
            
            JButton cancelBtn = createStyledButton("	Cancel Selected Booking", DANGER_COLOR, Color.WHITE);
            JButton closeBtn = createStyledButton("Close", PRIMARY_COLOR, Color.WHITE);

            cancelBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int id = (int) table.getValueAt(row, 0);
                    String passenger = (String) table.getValueAt(row, 7);
                    
                    int confirm = JOptionPane.showConfirmDialog(dialog,
                        "Cancel booking for " + passenger + "?\nBooking ID: " + id,
                        "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            bookingDAO.cancelBooking(id);
                            m.removeRow(row);
                            showSuccessMessage("Booking cancelled successfully!");
                        } catch (Exception ex) {
                            showErr(ex);
                        }
                    }
                } else {
                    showWarningMessage("Please select a booking to cancel.");
                }
            });

            closeBtn.addActionListener(e -> dialog.dispose());

            buttonPanel.add(cancelBtn);
            buttonPanel.add(closeBtn);

            content.add(sp, BorderLayout.CENTER);
            content.add(buttonPanel, BorderLayout.SOUTH);

            dialog.add(content);
            dialog.setVisible(true);
        } catch (Exception ex) {
            showErr(ex);
        }
    }

    // ---------- Passenger Panel ----------
    private JPanel createPassengerPanel() {
    	JPanel panel = createTitledPanel("Passenger Services", "🎫");
        panel.setLayout(new BorderLayout(12, 12));

        // ---- Top search card ----
        JPanel searchCard = new JPanel(new GridBagLayout());
        searchCard.setBackground(Color.WHITE);
        searchCard.setBorder(new EmptyBorder(12, 12, 12, 12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0; gbc.gridy = 0;
        searchCard.add(nameLabel, gbc);

        JTextField txtName = new JTextField(12);
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1; gbc.gridy = 0;
        searchCard.add(txtName, gbc);

        JLabel srcLabel = new JLabel("Source:");
        srcLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 2; gbc.gridy = 0;
        searchCard.add(srcLabel, gbc);

        JComboBox<String> cbSource = new JComboBox<>();
        cbSource.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 3; gbc.gridy = 0;
        searchCard.add(cbSource, gbc);

        JLabel destLabel = new JLabel("Destination:");
        destLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0; gbc.gridy = 1;
        searchCard.add(destLabel, gbc);

        JComboBox<String> cbDest = new JComboBox<>();
        cbDest.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1; gbc.gridy = 1;
        searchCard.add(cbDest, gbc);

        JButton btnSearch = createStyledButton("Search Trains", PRIMARY_COLOR, Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(140, 36));
        gbc.gridx = 2; gbc.gridy = 1;
        searchCard.add(btnSearch, gbc);

        JButton btnBookNow = createStyledButton("Book Now", SUCCESS_COLOR, Color.WHITE);
        btnBookNow.setPreferredSize(new Dimension(120, 36));
        gbc.gridx = 3; gbc.gridy = 1;
        searchCard.add(btnBookNow, gbc);

        panel.add(searchCard, BorderLayout.NORTH);

        // ---- Results table ----
        JTable table = createStyledTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(sp, BorderLayout.CENTER);

        // ---- Load stations into combos (logic unchanged) ----
        try {
            List<String> stations = stationDAO.listAllNames();
            cbSource.removeAllItems();
            cbDest.removeAllItems();
            for (String s : stations) {
                cbSource.addItem(s);
                cbDest.addItem(s);
            }
        } catch (Exception ex) {
            showErr(ex);
        }


        // Load stations
        try {
            List<String> stations = stationDAO.listAllNames();
            cbSource.removeAllItems();
            cbDest.removeAllItems();
            for (String s : stations) {
                cbSource.addItem(s);
                cbDest.addItem(s);
            }
        } catch (Exception ex) {
            showErr(ex);
        }

        btnSearch.addActionListener(e -> {
            String src = (String) cbSource.getSelectedItem();
            String dest = (String) cbDest.getSelectedItem();
            if (src == null || dest == null || src.equals(dest)) {
                JOptionPane.showMessageDialog(frame, "Please choose different source and destination");
                return;
            }
            try {
                List<Train> trains = trainDAO.listAll();
                String[] cols = {"ID", "Name", "Type", "Total Seats", "Fare/km", "Available Today"};
                DefaultTableModel m = new DefaultTableModel(cols, 0) {
                    public boolean isCellEditable(int r, int c) { return false; }
                };
                Date today = Date.valueOf(LocalDate.now());
                for (Train t : trains) {
                    int booked = bookingDAO.seatsBooked(t.getTrainId(), today);
                    int avail = t.getTotalSeats() - booked;
                    m.addRow(new Object[]{t.getTrainId(), t.getTrainName(), t.getTrainType(), 
                        t.getTotalSeats(), t.getFarePerKm(), avail});
                }
                table.setModel(m);
            } catch (Exception ex) {
                showErr(ex);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row < 0) return;
                    int trainId = (int) table.getValueAt(row, 0);
                    String name = txtName.getText().trim();
                    String src = (String) cbSource.getSelectedItem();
                    String dest = (String) cbDest.getSelectedItem();
                    
                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter your name first");
                        return;
                    }
                    if (src == null || dest == null || src.equals(dest)) {
                        JOptionPane.showMessageDialog(frame, "Please choose different source and destination first");
                        return;
                    }
                    
                    showQuickBookingDialog(trainId, name, src, dest, btnSearch);
                }
            }
        });

        btnBookNow.addActionListener(e -> {
            String name = txtName.getText().trim();
            String src = (String) cbSource.getSelectedItem();
            String dest = (String) cbDest.getSelectedItem();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your name");
                return;
            }
            if (src == null || dest == null || src.equals(dest)) {
                JOptionPane.showMessageDialog(frame, "Please choose different source and destination");
                return;
            }
            
            showDirectBookingDialog(name, src, dest, btnSearch);
        });

        return panel;
    }

    private void showDirectBookingDialog(String passengerName, String source, String destination, JButton btnSearch) {
    	 JDialog dialog = new JDialog(frame, "Book Ticket", true);
    	    dialog.setSize(450, 320);
    	    dialog.setLocationRelativeTo(frame);
    	    dialog.getContentPane().setBackground(Color.WHITE);
    	    dialog.setLayout(new BorderLayout(12, 12));
    	    dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    	    JPanel formPanel = new JPanel(new GridBagLayout());
    	    formPanel.setBackground(Color.WHITE);
    	    GridBagConstraints gbc = new GridBagConstraints();
    	    gbc.insets = new Insets(8, 8, 8, 8);
    	    gbc.anchor = GridBagConstraints.WEST;
    	    gbc.fill = GridBagConstraints.HORIZONTAL;

    	    
    	    JLabel lblName = new JLabel("Passenger: " + passengerName);
    	    lblName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    	    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
    	    formPanel.add(lblName, gbc);

    	    JLabel lblSource = new JLabel("Source: " + source);
    	    lblSource.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    	    gbc.gridy = 1;
    	    formPanel.add(lblSource, gbc);

    	    JLabel lblDest = new JLabel("Destination: ");
    	    lblDest.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    	    gbc.gridy = 2;
    	    formPanel.add(lblDest, gbc);

    	    JLabel lblTrain = new JLabel("Select Train:");
    	    gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 1;
    	    formPanel.add(lblTrain, gbc);

    	    JComboBox<String> cbTrain = new JComboBox<>();
    	    cbTrain.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    	    gbc.gridx = 1;
    	    formPanel.add(cbTrain, gbc);

    	    JLabel lblDate = new JLabel("Travel Date:");
    	    gbc.gridy = 4; gbc.gridx = 0;
    	    formPanel.add(lblDate, gbc);

    	    JTextField txtDate = new JTextField(10);
    	    txtDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    	    gbc.gridx = 1;
    	    formPanel.add(txtDate, gbc);

    	    dialog.add(formPanel, BorderLayout.CENTER);

    	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    	    buttonPanel.setBackground(Color.WHITE);
    	    JButton btnBook = createStyledButton("Book", SUCCESS_COLOR, Color.WHITE);
    	    JButton btnCancel = createStyledButton("Cancel", Color.GRAY, Color.WHITE);
    	try {
            // Get all trains for selection
            List<Train> trains = trainDAO.listAll();
            if (trains.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No trains available");
                return;
            }
            
            // Create train selection dropdown
            String[] trainOptions = new String[trains.size()];
            for (int i = 0; i < trains.size(); i++) {
                Train t = trains.get(i);
                trainOptions[i] = t.getTrainId() + " - " + t.getTrainName() + " (" + t.getTrainType() + ")";
            }
            
            JComboBox<String> trainCombo = new JComboBox<>(trainOptions);
            JTextField txtContact = new JTextField();
            JTextField txtSeats = new JTextField("1");
            JTextField txtDate1 = new JTextField(LocalDate.now().toString()); // yyyy-mm-dd

            Object[] fields = {
                "Passenger:", new JLabel(passengerName),
                "Source:", new JLabel(source),
                "Destination:", new JLabel(destination),
                "Select Train:", trainCombo,
                "Contact:", txtContact,
                "Date (yyyy-mm-dd):", txtDate1,
                "Seats:", txtSeats
            };
            
            int ok = JOptionPane.showConfirmDialog(frame, fields, "Book Ticket", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                int selectedIndex = trainCombo.getSelectedIndex();
                Train selectedTrain = trains.get(selectedIndex);
                int requestedSeats = Integer.parseInt(txtSeats.getText());
                Date bookingDate = Date.valueOf(txtDate1.getText());
                
                // Check seat availability
                int bookedSeats = bookingDAO.seatsBooked(selectedTrain.getTrainId(), bookingDate);
                int availableSeats = selectedTrain.getTotalSeats() - bookedSeats;
                
                if (requestedSeats > availableSeats) {
                    JOptionPane.showMessageDialog(frame, "Not enough seats available!\nRequested: " + requestedSeats + "\nAvailable: " + availableSeats, "Booking Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Booking b = new Booking();
                b.setTrainId(selectedTrain.getTrainId());
                b.setPassengerName(passengerName);
                b.setPassengerContact(txtContact.getText());
                b.setSource(source);
                b.setDestination(destination);
                b.setDateOfJourney(bookingDate);
                b.setSeatCount(requestedSeats);
                
                // Simple distance calculation
                double approxDistance = Math.abs(source.length() - destination.length()) * 10 + 50;
                double totalFare = approxDistance * selectedTrain.getFarePerKm() * requestedSeats;
                b.setTotalFare(Math.round(totalFare * 100.0) / 100.0);

                bookingDAO.createBooking(b);
                JOptionPane.showMessageDialog(frame, "Ticket booked successfully!\nTrain: " + selectedTrain.getTrainName() + "\nSeats Booked: " + requestedSeats + "\nTotal Fare: ₹" + b.getTotalFare());
                
                // Refresh the search results to show updated availability
                btnSearch.doClick();
            }
        } catch (Exception ex) {
            showErr(ex);
        }
    }

    private void showQuickBookingDialog(int trainId, String passengerName, String source, String destination, JButton btnSearch) {
    	
    	try {
            Train t = trainDAO.findById(trainId);
            if (t == null) return;

            JTextField txtContact = new JTextField();
            JTextField txtSeats = new JTextField("1");
            JTextField txtDate = new JTextField(LocalDate.now().toString()); // yyyy-mm-dd

            Object[] fields = {
                "Train:", new JLabel(t.getTrainName() + " (" + t.getTrainType() + ")"),
                "Passenger:", new JLabel(passengerName),
                "Source:", new JLabel(source),
                "Destination:", new JLabel(destination),
                "Contact:", txtContact,
                "Date (yyyy-mm-dd):", txtDate,
                "Seats:", txtSeats
            };
            
            int ok = JOptionPane.showConfirmDialog(frame, fields, "Quick Book - " + t.getTrainName(), JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                int requestedSeats = Integer.parseInt(txtSeats.getText());
                Date bookingDate = Date.valueOf(txtDate.getText());
                
                // Check seat availability
                int bookedSeats = bookingDAO.seatsBooked(t.getTrainId(), bookingDate);
                int availableSeats = t.getTotalSeats() - bookedSeats;
                
                if (requestedSeats > availableSeats) {
                    JOptionPane.showMessageDialog(frame, "Not enough seats available!\nRequested: " + requestedSeats + "\nAvailable: " + availableSeats, "Booking Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Booking b = new Booking();
                b.setTrainId(trainId);
                b.setPassengerName(passengerName);
                b.setPassengerContact(txtContact.getText());
                b.setSource(source);
                b.setDestination(destination);
                b.setDateOfJourney(bookingDate);
                b.setSeatCount(requestedSeats);
                
                // Simple distance calculation
                double approxDistance = Math.abs(source.length() - destination.length()) * 10 + 50;
                double totalFare = approxDistance * t.getFarePerKm() * requestedSeats;
                b.setTotalFare(Math.round(totalFare * 100.0) / 100.0);

                bookingDAO.createBooking(b);
                JOptionPane.showMessageDialog(frame, "Ticket booked successfully!\nTrain: " + t.getTrainName() + "\nSeats Booked: " + requestedSeats + "\nTotal Fare: ₹" + b.getTotalFare());
                
                // Refresh the search results to show updated availability
                btnSearch.doClick();
            }
        } catch (Exception ex) {
            showErr(ex);
        }
    }

    private void showErr(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}