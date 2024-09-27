import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    private int attempts = 3;

    public LoginWindow() {
        setTitle("Authentication");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        JLabel labelUser = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        JLabel labelPass = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        JButton submit = new JButton("Submit");

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                if (attempts > 0 && username.equals("omkar") && password.equals("9114")) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    new MainWindow(); // Opens the Main Window
                    dispose(); // Closes the Login Window
                } else {
                    attempts--;
                    if (attempts == 0) {
                        JOptionPane.showMessageDialog(null, "Too many attempts. Exiting.");
                        System.exit(0);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid credentials! Attempts left: " + attempts);
                    }
                }
            }
        });

        panel.add(labelUser);
        panel.add(userField);
        panel.add(labelPass);
        panel.add(passField);
        panel.add(submit);
        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginWindow();
    }
}
