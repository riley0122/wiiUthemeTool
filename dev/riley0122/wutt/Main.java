package dev.riley0122.wutt;

import javax.swing.*;

public class Main {
    public static final String VERSION = "1.0.0";
    public static String ip = "127.0.0.1";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Wii U Themeing tool");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);

            createUI(frame);
            
            frame.setVisible(true);
        });
    }

    public static void createUI(JFrame frame) {
        JTextField ipField = new JTextField(ip, 20);
        JButton setIpButton = new JButton("Connect");
        setIpButton.addActionListener(e -> {
            ip = ipField.getText();
            JOptionPane.showMessageDialog(frame, "IP Address set to: " + ip + "\n\nIniializing connection...");
            
            // TODO: connect to the wii u using ftp
        });
        // Add it to the frame
        JPanel ipPanel = new JPanel();
        ipPanel.add(new JLabel("IP Address:"));
        ipPanel.add(ipField);
        ipPanel.add(setIpButton);

        // Add 4 file input fields for the 4 files, 'cafe_barista_men.bfsar', Something starting with 'Men.', Something starting with 'Men2.', and 'Splash.png'. The Men files can be of these formats: .ips, .bps, .ups, .ppf, .aps, .rup
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel cafeLabel = new JLabel("cafe_barista_men.bfsar:");
        JTextField cafeField = new JTextField(20);
        JButton cafeButton = new JButton("Browse");
        cafeButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            cafeField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        JLabel menLabel = new JLabel("Men.* (.ips, .bps, .ups, .ppf, .aps, .rup):");
        JTextField menField = new JTextField(20);
        JButton menButton = new JButton("Browse");
        menButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            menField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        JLabel men2Label = new JLabel("Men2.* (.ips, .bps, .ups, .ppf, .aps, .rup):");
        JTextField men2Field = new JTextField(20);
        JButton men2Button = new JButton("Browse");
        men2Button.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            men2Field.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        JLabel splashLabel = new JLabel("Splash.png:");
        JTextField splashField = new JTextField(20);
        JButton splashButton = new JButton("Browse");
        splashButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            splashField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        panel.add(cafeLabel);
        panel.add(cafeField);
        panel.add(cafeButton);
        panel.add(menLabel);
        panel.add(menField);
        panel.add(menButton);
        panel.add(men2Label);
        panel.add(men2Field);
        panel.add(men2Button);
        panel.add(splashLabel);
        panel.add(splashField);
        panel.add(splashButton);

        frame.add(panel);
        
    }
}
