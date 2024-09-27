import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnpackWindow extends JFrame {
    private JTextField packedFileField, unpackDirectoryField;

    public UnpackWindow() {
        setTitle("Unpack Files");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JLabel packFileLabel = new JLabel("Packed File:");
        packedFileField = new JTextField(20);
        JButton browsePackFileButton = new JButton("Browse");

        browsePackFileButton.addActionListener(e -> chooseFile(packedFileField));

        JLabel unpackDirLabel = new JLabel("Unpack to:");
        unpackDirectoryField = new JTextField(20);
        JButton browseUnpackDirButton = new JButton("Browse");

        browseUnpackDirButton.addActionListener(e -> chooseDirectory(unpackDirectoryField));

        JButton submitButton = new JButton("Unpack");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String packedFileName = packedFileField.getText();
                String unpackDir = unpackDirectoryField.getText();
                if (!packedFileName.isEmpty() && !unpackDir.isEmpty()) {
                    try {
                        unpack(packedFileName, unpackDir);
                        JOptionPane.showMessageDialog(null, "Unpacking Completed!");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error during unpacking: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill in both fields.");
                }
            }
        });

        panel.add(packFileLabel);
        panel.add(packedFileField);
        panel.add(browsePackFileButton);
        panel.add(unpackDirLabel);
        panel.add(unpackDirectoryField);
        panel.add(browseUnpackDirButton);
        panel.add(submitButton);
        add(panel);

        setVisible(true);
    }

    private void chooseFile(JTextField field) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            field.setText(selectedFile.getAbsolutePath());
        }
    }

    private void chooseDirectory(JTextField field) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedDir = chooser.getSelectedFile();
            field.setText(selectedDir.getAbsolutePath());
        }
    }

    private void unpack(String zipFilePath, String destDir) throws IOException {
        File destDirFile = new File(destDir);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDirFile, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    private File newFile(File destDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destDir, zipEntry.getName());

        String destDirPath = destDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
