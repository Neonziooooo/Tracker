package org.tracker.reports;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.DuplicateHeaderMode;
import org.tracker.calculator.FaFCalculator;
import org.tracker.object.Invoice;
import org.tracker.util.ProgramDirectory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FaFReport {

    private static List<Invoice> invoices;
    private CSVPrinter csvPrinter;

    /**
     *
     *
     */

    public void initialize(List<Invoice> list) {
        invoices = new ArrayList<>();
        FaFCalculator faFCalculator = new FaFCalculator();
        faFCalculator.calculateDuplicateValuesSum(invoices);
        writeInvoicesToCsv();
    }

    /**
     *
     */

    public void finish() {
        invoices.clear();
    }

    /**
     *
     */

    protected void writeInvoices() {
        invoices.stream().forEachOrdered(invoice -> {
            writeFrontInvoiceRecord(invoice);
            writeBackInvoiceRecord(invoice);
        });
    }

    /**
     *
     */

    protected void writeFrontInvoiceRecord(Invoice invoice) {
        try {
            csvPrinter.printRecord(
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

    protected void writeBackInvoiceRecord(Invoice invoice) {
        try {
            csvPrinter.printRecord(
                    "OBSDET", "1000", invoice.getBranch(),
                    invoice.getPartner() + "|" + invoice.getType() + "|" + invoice.getModel() + "|" +
                            invoice.getSeries() + "|" + invoice.getNumber() + "|" + invoice.getIssuanceDate(),
                    "AMAI99", "BR|RS", "RS99013007", invoice.getCfop(), "", "", "0", "0", "0", invoice.getProductAmount()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     */

    protected void writeInvoicesToCsv() {
        CSVFormat csvFormat = CSVFormat.Builder.create()
                .setDelimiter(';')
                .setQuote('"')
                .setRecordSeparator("\r\n")
                .setIgnoreEmptyLines(true)
                .setDuplicateHeaderMode(DuplicateHeaderMode.ALLOW_ALL)
                .build();

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(
                ProgramDirectory.getProgramDirectory() + "/out/FAF.csv"))) {

            csvPrinter = new CSVPrinter(writer,
                    csvFormat);

            writeInvoices();
            csvPrinter.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Invoice> getInvoices() {
        return invoices;
    }
}

