package org.tracker.gui;

import org.tracker.calculator.PresumedCalculator;
import org.tracker.util.ProgramDirectory;
import org.tracker.util.RoundedButton;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class TrackerFrame {
    private static RoundedButton fafButton = null;
    private static RoundedButton presumedButton = null;
    private static RoundedButton selectButton = null;
    private static boolean selected;
    private static String fileName;
    private static JFrame jFrame = null;
    private static JLabel expectedLabel;
    private static JLabel resultLabel;

    public TrackerFrame() {
        JPanel jPanel = new JPanel();
        jFrame = new JFrame();
        TrackerListener trackerListener = new TrackerListener();

        jFrame.setSize(250, 210);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.add(jPanel);

        jPanel.setLayout(null);

        JLabel fafLabel = new JLabel("FAF");
        fafLabel.setBounds(55, 20, 80, 10);
        jPanel.add(fafLabel);

        JLabel presumedLabel = new JLabel("CRP");
        presumedLabel.setBounds(163, 20, 80, 10);
        jPanel.add(presumedLabel);

        expectedLabel = new JLabel("Esperado: " + PresumedCalculator.getExpectedValue());
        expectedLabel.setBounds(10, 105, 180, 70);
        jPanel.add(expectedLabel);

        resultLabel = new JLabel("Resultado: " + PresumedCalculator.getResultValue());
        resultLabel.setBounds(10, 143, 180, 30);
        jPanel.add(resultLabel);

        fafButton = new RoundedButton("Execute");
        fafButton.addActionListener(trackerListener);
        fafButton.setFocusPainted(false);
        fafButton.setBounds(30, 50, 70, 25);
        jPanel.add(fafButton);

        presumedButton = new RoundedButton("Execute");
        presumedButton.addActionListener(trackerListener);
        presumedButton.setFocusPainted(false);
        presumedButton.setBounds(140, 50, 70, 25);
        jPanel.add(presumedButton);

        selectButton = new RoundedButton("Select");
        selectButton.addActionListener(trackerListener);
        selectButton.setFocusPainted(false);
        selectButton.setBounds(85, 95, 70, 25);
        jPanel.add(selectButton);

        jFrame.setVisible(true);
    }

    public static void copyFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(jFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile == null) return;
            fileName = selectedFile.getName();
            Path sourcePath = selectedFile.toPath();
            Path destinationPath = Paths.get(ProgramDirectory.getProgramDirectory() + "/input/" + selectedFile.getName());
            try {
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                TrackerFrame.setSelected(true);
                getSelectButton().setText("Selected");
                getSelectButton().setBounds(80, 95, 80, 25);
                PresumedCalculator.reloadValues();
                reloadTextValues();
            } catch (IOException ex) {
                ex.printStackTrace();
                sendAlert("Alerta!", "Erro ao copiar o arquivo!");
            }
        }
    }

    public static void sendAlert(String title, String arg1) {
        JOptionPane jOptionPane = new JOptionPane();
        JOptionPane.showMessageDialog(jFrame, arg1, title, JOptionPane.INFORMATION_MESSAGE);
        jOptionPane.setFocusable(false);
    }

    public static void lockedOutput() {
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public static void unLockedOutPut() {
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static File getFileDirectory() {
        return new File(ProgramDirectory.getProgramDirectory() + "/input/" + fileName);
    }

    public static RoundedButton getFafButton() {
        return fafButton;
    }

    public static RoundedButton getPresumedButton() {
        return presumedButton;
    }

    public static RoundedButton getSelectButton() {
        return selectButton;
    }

    public static boolean isSelected() {
        return selected;
    }

    public static JLabel getExpectedLabel() {
        return expectedLabel;
    }

    public static JLabel getResultLabel() {
        return resultLabel;
    }

    public static void setSelected(boolean selected) {
        TrackerFrame.selected = selected;
    }

    private static void reloadTextValues() {
        expectedLabel.setText("Esperado: " + PresumedCalculator.getExpectedValue());
        resultLabel.setText("Resultado: " + PresumedCalculator.getResultValue());
    }
}
