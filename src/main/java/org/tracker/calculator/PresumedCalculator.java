package org.tracker.calculator;

import org.tracker.object.Invoice;
import org.tracker.reports.PresumedReport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PresumedCalculator {

    private static BigDecimal expectedValue = BigDecimal.ZERO;
    private static BigDecimal resultValue = BigDecimal.ZERO;

    public void calculateDuplicateValuesSum(List<Invoice> oldList) {
        for (Invoice invoice : oldList) {
            if (!"1000".equals(invoice.getBranch()) &&
                    !isValidDocument(invoice) &&
                    !isValidCFOP(invoice.getCfop()) &&
                    invoice.getPartner().isEmpty()) {
                continue;
            }
            Optional<Invoice> optionalInvoice = PresumedReport.getFrontInvoices().stream()
                    .filter(frontInvoice -> Objects.equals(frontInvoice.getPartner(), invoice.getPartner()) &&
                            Objects.equals(frontInvoice.getNumber(), invoice.getNumber())).findAny();
            if (optionalInvoice.isEmpty()) PresumedReport.getFrontInvoices().add(invoice);
        }
        PresumedReport.getBackInvoices().addAll(oldList);
    }

    private boolean isValidDocument(Invoice invoice) {
        return "00 - Documento regular".equals(invoice.getSituation()) ||
                "01 - Documento Regular Extemporâneo".equals(invoice.getSituation()) ||
                "06 - Documento Fiscal Complementar".equals(invoice.getSituation()) ||
                "07 - Documento Fiscal Complementar Extemporâneo".equals(invoice.getSituation()) ||
                "08 - Documento Fiscal emitido com base em Regime Especial ou Norma Específica".equals(invoice.getSituation());
    }

    public BigDecimal gradeCalculation(Invoice invoice) {

        BigDecimal presumedValue = calculatePresumedTaxValue(invoice);
        BigDecimal presumedValueFromICMS = calculatePresumedTaxValueFromICMS(invoice);
        expectedValue = expectedValue.add(presumedValueFromICMS);

        if (invoice.getTaxRate() == 12 && Objects.equals(invoice.getCstICMS(), "000")) {
            resultValue = resultValue.add(presumedValue);
            return presumedValue;

        } else if (invoice.getTaxRate() == 12 && Objects.equals(invoice.getCstICMS(), "051")) {
            resultValue = resultValue.add(presumedValue);
            return presumedValue;

        } else if (invoice.getTaxRate() == 17 && Objects.equals(invoice.getCstICMS(), "000")) {
            presumedValue = calculatePresumedTaxValue17WithCST000(invoice, presumedValue, presumedValueFromICMS);
            resultValue = resultValue.add(presumedValue);
            return presumedValue;

        } else if (invoice.getTaxRate() == 17 && Objects.equals(invoice.getCstICMS(), "051")) {
            resultValue = resultValue.add(presumedValue);
            return presumedValue;

        } else if (invoice.getTaxRate() == 7 && Objects.equals(invoice.getCstICMS(), "000")) {
            presumedValue = calculatePresumedTaxValueWith4286(invoice);
            resultValue = resultValue.add(presumedValue);
            return presumedValue;
        }
        return null;
    }

    private BigDecimal calculatePresumedTaxValue17WithCST000(Invoice invoice, BigDecimal presumedValue,
                                                       BigDecimal presumedValueFromICMS) {
        if (presumedValue.subtract(presumedValueFromICMS).abs()
                .compareTo(new BigDecimal("0.06")) <= 0) {
            return presumedValue;
        }
        invoice.setPercentage(76.47);
        return invoice.getIcms().multiply(new BigDecimal("0.7647"))
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculatePresumedTaxValue(Invoice invoice) {
        invoice.setPercentage(66.67);
        return invoice.getIcms().multiply(new BigDecimal("0.6667"))
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculatePresumedTaxValueWith4286(Invoice invoice) {
        invoice.setPercentage(42.86);
        return invoice.getIcms().multiply(new BigDecimal("0.4286"))
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculatePresumedTaxValueFromICMS(Invoice invoice) {
        return invoice.getIcms().subtract(invoice.getProductAmount().
                multiply(new BigDecimal("0.04")))
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private boolean isValidCFOP(int cfop) {
        return cfop == 1201 || cfop == 2201 ||
                cfop == 2208 || cfop == 5101 || cfop == 6101 || cfop == 6107;
    }

    public static void reloadValues() {
        expectedValue = BigDecimal.ZERO;
        resultValue = BigDecimal.ZERO;
    }

    public static BigDecimal getExpectedValue() {
        return expectedValue;
    }

    public static BigDecimal getResultValue() {
        return resultValue;
    }
}
