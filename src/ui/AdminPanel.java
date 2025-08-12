package ui;
import javax.swing.*;
import java.awt.*;


public class AdminPanel extends JFrame {
    public AdminPanel() {
    	setTitle("Admin Panel");
    	        setSize(800, 600);
    	        setLocationRelativeTo(null);
    	        setTitle("Admin Panel");
    	        setSize(500, 400);
    	        setLocationRelativeTo(null);
    	        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    	        // Add train, view bookings, etc.
    	        JLabel llabel = new JLabel("Welcome to Admin Panel", SwingConstants.CENTER);
    	        

    	        
    	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

    	        JPanel mainPanel = new JPanel(new BorderLayout());
    	        mainPanel.setBackground(new Color(240, 240, 240));

    	        JLabel header = new JLabel("Admin Dashboard", SwingConstants.CENTER);
    	        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
    	        header.setOpaque(true);
    	        header.setBackground(Color.WHITE);
    	        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    	        JPanel contentPanel = new JPanel();
    	        contentPanel.setBackground(new Color(240, 240, 240));
    	        contentPanel.setLayout(new GridLayout(2, 2, 15, 15));
    	        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    	        String[] sections = {"Manage Users", "View Reports", "Settings", "Logout"};
    	        for (String section : sections) {
    	            JPanel card = new JPanel(new BorderLayout());
    	            card.setBackground(Color.WHITE);
    	            card.setBorder(BorderFactory.createCompoundBorder(
    	                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
    	                    BorderFactory.createEmptyBorder(20, 20, 20, 20)));
    	            JLabel label = new JLabel(section, SwingConstants.CENTER);
    	            label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    	            card.add(label, BorderLayout.CENTER);
    	            contentPanel.add(card);
    	        }
    	        
    	        mainPanel.add(header, BorderLayout.NORTH);
    	        mainPanel.add(contentPanel, BorderLayout.CENTER);
    	        add(mainPanel);
    	        setVisible(true);
    	    }
    	
        
    }


