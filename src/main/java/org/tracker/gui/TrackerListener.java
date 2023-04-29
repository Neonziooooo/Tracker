package org.tracker.gui;

import org.tracker.calculator.PresumedCalculator;
import org.tracker.excel.ExcelDataReader;
import org.tracker.reports.FaFReport;
import org.tracker.reports.PresumedReport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrackerListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == TrackerFrame.getFafButton() && TrackerFrame.isSelected()) {

            TrackerFrame.sendAlert("FaF", "Relatório iniciado!");
            TrackerFrame.lockedOutput();

            FaFReport faFReport = new FaFReport();
            faFReport.initialize(new ExcelDataReader(TrackerFrame.getFileDirectory()).readRowsValues());
            faFReport.finish();

            TrackerFrame.setSelected(false);
            TrackerFrame.getSelectButton().setText("Select");
            TrackerFrame.getSelectButton().setBounds(85, 95, 70, 25);
            TrackerFrame. unLockedOutPut();

        } else if (e.getSource() == TrackerFrame.getPresumedButton() && TrackerFrame.isSelected()) {

            TrackerFrame.sendAlert("Presumido", "Relatório iniciado!");
            TrackerFrame.lockedOutput();

            PresumedReport presumedReport = new PresumedReport();
            presumedReport.initialize(new ExcelDataReader(TrackerFrame.getFileDirectory()).readRowsValues());
            presumedReport.finish();

            TrackerFrame.getExpectedLabel().setText("Esperado: " + PresumedCalculator.getExpectedValue());
            TrackerFrame.getResultLabel().setText("Resultado: " + PresumedCalculator.getResultValue());
            TrackerFrame.setSelected(false);
            TrackerFrame.getSelectButton().setText("Select");
            TrackerFrame.getSelectButton().setBounds(85, 95, 70, 25);
            TrackerFrame.unLockedOutPut();

        } else if (e.getSource() == TrackerFrame.getSelectButton() && !TrackerFrame.isSelected()) {

            TrackerFrame.copyFile();
        }
    }
}
