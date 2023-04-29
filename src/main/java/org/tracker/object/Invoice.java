package org.tracker.object;

import java.math.BigDecimal;

public class Invoice {

    private final String number;
    private final String series;
    private final String model;
    private final String partner;
    private final String branch;
    private final String issuanceDate;
    private final String type;
    private final int cfop;
    private BigDecimal productAmount;
    private final int productNumber;
    private final String productID;
    private final String cstICMS;
    private final int taxRate;
    private final BigDecimal icms;
    private final String situation;
    private double percentage;

    public Invoice(String partner, String number, String situation, String branch, String type,
                   String issuanceDate, String model, String series,
                   int cfop, BigDecimal productAmount, int productNumber,
                   String productID, String cstICMS, int taxRate, BigDecimal icms, double percentage) {
        this.partner = partner;
        this.number = number;
        this.situation = situation;
        this.branch = branch;
        this.type = type;
        this.issuanceDate = issuanceDate;
        this.model = model;
        this.series = series;
        this.cfop = cfop;
        this.productAmount = productAmount;
        this.productNumber = productNumber;
        this.productID = productID;
        this.cstICMS = cstICMS;
        this.taxRate = taxRate;
        this.icms = icms;
        this.percentage = percentage;
    }

    public String getType() {
        if (type.equals("ENTRADA")) return "0";
        return "1";
    }

    public boolean getEntryOrExit() {
        return type.equals("ENTRADA");
    }

    public String getNumber() {
        return number;
    }

    public String getSeries() {
        return series;
    }

    public String getModel() {
        return model;
    }

    public String getPartner() {
        return partner;
    }

    public String getBranch() {
        return branch;
    }

    public String getIssuanceDate() {
        return issuanceDate;
    }

    public int getCfop() {
        return cfop;
    }

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    public int getProductNumber() {
        return productNumber;
    }

    public String getProductID() {
        return productID;
    }

    public String getCstICMS() {
        return cstICMS;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getIcms() {
        return icms;
    }

    public String getSituation() {
        return situation;
    }
}
