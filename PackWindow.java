import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PackWindow extends JFrame {
    private JTextField directoryField, packedFileField;

    public PackWindow() {
        setTitle("Pack Files");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JLabel dirLabel = new JLabel("Directory to Pack:");
        directoryField = new JTextField(20);
        JButton browseDirButton = new JButton("Browse");

        browseDirButton.addActionListener(e -> chooseDirectory(directoryField));

        JLabel packFileLabel = new JLabel("Packed File Name:");
        packedFileField = new JTextField(20);
        JButton submitButton = new JButton("Pack");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = directoryField.getText();
                String packedFileName = packedFileField.getText();
                if (!directory.isEmpty() && !packedFileName.isEmpty()) {
                    try {
                        pack(directory, packedFileName);
                        JOptionPane.showMessageDialog(null, "Packing Completed!");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error during packing: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill in both fields.");
                }
            }
        });

        panel.add(dirLabel);
        panel.add(directoryField);
        panel.add(browseDirButton);
        panel.add(packFileLabel);
        panel.add(packedFileField);
        panel.add(submitButton);
        add(panel);

        setVisible(true);
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

    private void pack(String sourceDir, String outputFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputFile + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(sourceDir);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }

    private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}
