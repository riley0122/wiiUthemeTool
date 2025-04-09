package dev.riley0122.wutt;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Properties;

public class Main {
    public static final String VERSION = "1.0.0";
    public static String ip = "127.0.0.1";
    public static String themeName = "";
    public static Properties properties = new Properties();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Wii U Theme tool");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 300);

            createUI(frame);
            
            frame.setResizable(false);
            frame.setAlwaysOnTop(true);
            frame.setVisible(true);
        });
    }

    public static void createUI(JFrame frame) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components
    
        // IP Address Section
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("IP Address:"), gbc);
    
        JTextField ipField = new JTextField(ip, 20);
        gbc.gridx = 1;
        panel.add(ipField, gbc);
    
        JButton setIpButton = new JButton("Set");
        gbc.gridx = 2;
        panel.add(setIpButton, gbc);
    
        setIpButton.addActionListener(e -> {
            ip = ipField.getText();
            JOptionPane.showMessageDialog(frame, "IP Address set to: " + ip);
        });
    
        // Theme Name Section
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Theme Name:"), gbc);
        JTextField themeNameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(themeNameField, gbc);

        JButton setThemeNameButton = new JButton("Set");
        gbc.gridx = 2;
        panel.add(setThemeNameButton, gbc);

        setThemeNameButton.addActionListener(e -> {
            String themeName = themeNameField.getText();
            if (!themeName.isEmpty()) {
                Main.themeName = themeName;
                JOptionPane.showMessageDialog(frame, "Theme Name set to: " + themeName);
            } else {
                JOptionPane.showMessageDialog(frame, "Theme Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // File Input Fields
        addFileInputField(panel, gbc, 2, "cafe_barista_men.bfsar:", "Browse", "music");
        addFileInputField(panel, gbc, 3, "Men.* (.ips, .bps, .ups, .ppf, .aps, .rup):", "Browse", "men1");
        addFileInputField(panel, gbc, 4, "Men2.* (.ips, .bps, .ups, .ppf, .aps, .rup):", "Browse", "men2");
        addFileInputField(panel, gbc, 5, "Splash.png:", "Browse", "splash");

        // Submit button
        JButton submitButton = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        submitButton.addActionListener(e -> {
            submit();
        });
        panel.add(submitButton, gbc);
    
        frame.add(panel);
    }

    public static void submit() {
        // Ask for region (USA/EUR/JPN)
        String[] regions = {"USA", "EUR", "JPN"};
        String region = (String) JOptionPane.showInputDialog(
            null,
            "Select your region:",
            "Region Selection",
            JOptionPane.QUESTION_MESSAGE,
            null,
            regions,
            regions[0]
        );

        if (region == null) {
            JOptionPane.showMessageDialog(null, "Region selection is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String regionCode = region.equals("USA") ? "10040100" : region.equals("EUR") ? "10040200" : "10040000";

        // Download the original Men.pack, Men2.pack, and cafe_barista_men.bfsar files
        FTP.getFile(ip, "storage_mlc/sys/title/00050010/" + regionCode + "/content/Common/Package/Men.pack");
        FTP.getFile(ip, "storage_mlc/sys/title/00050010/" + regionCode + "/content/Common/Package/Men2.pack");
        FTP.getFile(ip, "storage_mlc/sys/title/00050010/" + regionCode + "/content/Common/Sound/Men/cafe_barista_men.bfsar");
    }

    private static void addFileInputField(JPanel panel, GridBagConstraints gbc, int row, String labelText, String buttonText, String id) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);
    
        JTextField textField = new JTextField(20);
        gbc.gridx = 1;
        textField.setEditable(false);
        panel.add(textField, gbc);
    
        JButton button = new JButton(buttonText);
        gbc.gridx = 2;
        panel.add(button, gbc);

        button.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                textField.setText(path);
                properties.setProperty(id, path);
            }
        });
    }
}
