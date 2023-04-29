package org.tracker.calculator;

import org.tracker.object.Invoice;
import org.tracker.reports.FaFReport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FaFCalculator {

    public void calculateDuplicateValuesSum(List<Invoice> oldList) {
        for (Invoice invoice : oldList) {
            if (!"00 - Documento regular".equals(invoice.getSituation()) ||
                    invoice.getPartner().isEmpty()) {
                continue;
            }
            Optional<Invoice> optionalInvoice = FaFReport.getInvoices().stream()
                    .filter(invoiceFromList -> Objects.equals(invoiceFromList.getPartner(), invoice.getPartner()) &&
                            Objects.equals(invoiceFromList.getNumber(), invoice.getNumber())).findAny();
            if (optionalInvoice.isPresent()) {
                Invoice newInvoiceValue = optionalInvoice.get();
                BigDecimal value = newInvoiceValue.getProductAmount().add(invoice.getProductAmount()).setScale(2, RoundingMode.HALF_EVEN);
                newInvoiceValue.setProductAmount(value);
            } else {
                FaFReport.getInvoices().add(invoice);
            }
        }
    }
}
