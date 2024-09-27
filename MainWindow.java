import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    public MainWindow() {
        setTitle("File Packer/Unpacker");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JButton packButton = new JButton("Pack");
        JButton unpackButton = new JButton("Unpack");

        packButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Pack Window (To be implemented)
                new PackWindow(); // Placeholder
            }
        });

        unpackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Unpack Window (To be implemented)
                new UnpackWindow(); // Placeholder
            }
        });

        panel.add(packButton);
        panel.add(unpackButton);
        add(panel);
        setVisible(true);
    }
}
