package org.tracker.util;

public class ExcelUtil {

    public int getColumn(ColumnUtil typeUtil) {
        switch (typeUtil) {
            case BRANCH_COLUMN -> {
                return 1;
            }
            case TYPE_COLUMN -> {
                return 6;
            }
            case DATE_COLUMN -> {
                return 7;
            }
            case MODEL_COLUMN -> {
                return 9;
            }
            case NUMBER_COLUMN -> {
                return 10;
            }
            case SERIES_COLUMN -> {
                return 11;
            }
            case PARTNER_COLUMN -> {
                return 14;
            }
            case CFOP_COLUMN -> {
                return 21;
            }
            case AMOUNT_COLUMN -> {
                return 29;
            }
            case PRODUCTNUMBER_COLUMN -> {
                return 24;
            }
            case PRODUCTID_COLUMN -> {
                return 25;
            }
            case ICMSCST_COLUMN -> {
                return 35;
            }
            case TAXRATE_COLUMN -> {
                return 37;
            }
            case ICMS -> {
                return 38;
            }
            case SITUATION -> {
                return 12;
            }
        }
        return 0;
    }

    public enum ColumnUtil {
        BRANCH_COLUMN,
        TYPE_COLUMN,
        DATE_COLUMN,
        MODEL_COLUMN,
        NUMBER_COLUMN,
        SERIES_COLUMN,
        PARTNER_COLUMN,
        CFOP_COLUMN,
        AMOUNT_COLUMN,
        PRODUCTNUMBER_COLUMN,
        PRODUCTID_COLUMN,
        ICMSCST_COLUMN,
        TAXRATE_COLUMN,
        ICMS,
        SITUATION
    }
}

