package org.tracker;

import org.tracker.gui.TrackerFrame;

import javax.swing.*;
import java.io.File;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Main {

    public static void main(String[] args) {

        File inputFolder = new File("input");
        File outFolder = new File("out");
        if (!inputFolder.exists()) inputFolder.mkdirs();
        if (!outFolder.exists()) outFolder.mkdirs();
        SwingUtilities.invokeLater(TrackerFrame::new);

    }
}
