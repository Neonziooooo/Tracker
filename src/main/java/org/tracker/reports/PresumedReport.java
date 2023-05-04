package org.tracker.reports;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.DuplicateHeaderMode;
import org.tracker.calculator.PresumedCalculator;
import org.tracker.object.Invoice;
import org.tracker.util.ProgramDirectory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PresumedReport {

    private final String path = ProgramDirectory.getProgramDirectory() + "/out/";
    private PresumedCalculator presumedCalculator;
    private static List<Invoice> frontInvoices;
    private static List<Invoice> backInvoices;
    private CSVPrinter csvPrinterPresumed;

    /**
     *
     */

    public void initialize(List<Invoice> list) {
        frontInvoices = new ArrayList<>();
        backInvoices = new ArrayList<>();
        presumedCalculator = new PresumedCalculator();
        presumedCalculator.calculateDuplicateValuesSum(list);
        writeInvoicesToCsv();
    }

    /**
     *
     */

    public void finish() {
        frontInvoices.clear();
        backInvoices.clear();
    }

    /**
     *
     */

    private void writeInvoices() {
        frontInvoices.forEach(this::writeFrontInvoiceRecord);
        for (Invoice invoice : backInvoices) {
            String code = invoice.getEntryOrExit() ? "RS99980003" : "RS99980001";
            BigDecimal value = presumedCalculator.gradeCalculation(invoice);
            if (value == null) continue;
            writeBackInvoiceRecord(invoice, code, value);
        }
    }

    /**
     *
     */

    private void writeFrontInvoiceRecord(Invoice invoice) {
        try {
            csvPrinterPresumed.printRecord(
                    "OBS", "1000", invoice.getBranch(),
                    invoice.getPartner() + "|" + invoice.getType() + "|" + invoice.getModel() + "|" +
                            invoice.getSeries() + "|" + invoice.getNumber() + "|" + invoice.getIssuanceDate(),
                    "AMAI99", "AM - APURACAO INCENTIVADA"
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     */

    private void writeBackInvoiceRecord(Invoice invoice, String code,
                                          BigDecimal value) {
        try {
            csvPrinterPresumed.printRecord(
                    "OBSDET", "1000", invoice.getBranch(),
                    invoice.getPartner() + "|" + invoice.getType() + "|" + invoice.getModel() + "|" +
                            invoice.getSeries() + "|" + invoice.getNumber() + "|" + invoice.getIssuanceDate(),
                    "AMAI99", "BR|RS", code, "189", invoice.getProductNumber(), invoice.getProductID(),
                    invoice.getIcms(), invoice.getPercentage(), value, "0");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     */

    private void writeInvoicesToCsv() {
        try {
            System.out.println(createPresumedFileWithNumber());
            CSVFormat csvFormat = CSVFormat.Builder.create()
                    .setDelimiter(';')
                    .setQuote('"')
                    .setRecordSeparator("\r\n")
                    .setIgnoreEmptyLines(true)
                    .setDuplicateHeaderMode(DuplicateHeaderMode.ALLOW_ALL)
                    .build();

            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path + createPresumedFileWithNumber()))) {

                csvPrinterPresumed = new CSVPrinter(writer, csvFormat);

                writeInvoices();
                csvPrinterPresumed.flush();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Invoice> getFrontInvoices() {
        return frontInvoices;
    }

    public static List<Invoice> getBackInvoices() {
        return backInvoices;
    }

    private String createPresumedFileWithNumber() {

        String fileName0 = "Presumed.csv";
        String fileName1 = "Presumed_1.csv";

        File file0 = new File(path + fileName0);
        File file1 = new File(path + fileName1);

        if (file1.exists() && !file0.exists()) {

            return fileName0;

        } else if (file1.exists() && file0.exists()) {

            File[] files = new File(path).listFiles();
            if (files == null) return fileName0;
            Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));

            int number = -1;
            for (File file : files) {
                Matcher matcher = Pattern.compile("^Presumed_(\\d+)\\.csv$")
                        .matcher(file.getName());
                if (matcher.find()) {
                    number = Integer.parseInt(matcher.group(1));
                    break;
                }
            }

            return "Presumed_" + (number + 1) + ".csv";

        } else if (!file1.exists() && file0.exists()) {

            return fileName1;
        }
        return fileName0;
    }
}
