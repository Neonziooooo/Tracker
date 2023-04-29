package org.tracker.excel;

import com.aspose.cells.Cell;
import com.aspose.cells.Row;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import org.tracker.object.Invoice;
import org.tracker.util.ExcelUtil;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ExcelDataReader extends ExcelUtil {

    private final File file;

    public ExcelDataReader(File file) {
        this.file = file;
    }

    /**
     *  Lê os valores das células de todas as linhas da planilha e cria objetos Invoice com os valores lidos.
     *
     * @return Lista de objetos Invoice
     */

    public List<Invoice> readRowsValues() {
        try {
            // Abre o arquivo Excel usando a biblioteca Aspose Cells
            Workbook workbook = new Workbook(file.getAbsolutePath());
            // Seleciona a primeira planilha do arquivo
            Worksheet worksheet = workbook.getWorksheets().get(0);
            // Calcula o número total de linhas da planilha
            int totalRows = worksheet.getCells().getMaxDataRow() + 1;
            // Cria uma lista vazia para armazenar os objetos Invoice criados a partir dos valores lidos
            List<Invoice> invoiceList = new ArrayList<>();
            // Define o tamanho do bloco de linhas a ser processado de uma vez
            int blockSize = 2000;
            // Cria uma lista vazia para armazenar os objetos Invoice criados para cada bloco de linhas
            List<Invoice> blockInvoices;
            // Percorre as linhas da planilha em blocos de tamanho blockSize
            for (int i = 4; i < totalRows; i += blockSize) {
                // Calcula o índice final do bloco atual
                int endIndex = Math.min(i + blockSize, totalRows);
                // Processa o bloco atual de linhas e cria objetos Invoice com os valores lidos
                blockInvoices = processData(worksheet, i, endIndex);
                // Adiciona os objetos Invoice criados para o bloco atual na lista de objetos Invoice geral
                invoiceList.addAll(blockInvoices);
                // Libera memória do sistema
                System.gc();
            }
            // Retorna a lista de objetos Invoice criados a partir dos valores lidos
            return invoiceList;

        } catch (Exception e) {
            // Captura qualquer exceção lançada durante o processamento do arquivo Excel e relança uma exceção RuntimeException
            throw new RuntimeException(e);
        }
    }

    /**
     * Processa um bloco de linhas da planilha e cria objetos Invoice com os valores lidos.
     *
     * @param sheet Planilha a ser processada
     * @param startIndex Índice da primeira linha a ser processada (inclusivo)
     * @param endIndex Índice da última linha a ser processada (exclusivo)
     * @return Lista de objetos Invoice criados a partir dos valores lidos
     */

    private List<Invoice> processData(Worksheet sheet, int startIndex, int endIndex) {
        // Cria uma lista vazia para armazenar os objetos Invoice criados a partir dos valores lidos
        List<Invoice> blockInvoices = new ArrayList<>();
        // Percorre as linhas do bloco atual de linhas
        for (int i = startIndex; i < endIndex; i++) {
            // Seleciona a linha definida por i que será lida
            Row row = sheet.getCells().checkRow(i);
            // Caso a linha seja nula, pula para a próxima
            if (row == null) continue;

            // Cria o objeto Invoice com as informações da linha coletada
            Invoice invoice = createInvoiceObject(row);
            // Adiciona o objeto Invoice criado a lista de objetos Invoice
            blockInvoices.add(invoice);
        }
        // Retorna a lista de objetos Invoice criados
        return blockInvoices;
    }

    /**
     *  Utiliza os valores obtidos pelos métodois auxiliares para instanciar e retornar um objeto Invoice
     *  preenchido com as informações da linha da planilha.
     *
     * @param row Linha a ser processada
     * @return Objeto invoice criado a partir dos valores lidos na linha
     */

    private Invoice createInvoiceObject(Row row) {
        String number = getStringValue(row, ColumnUtil.NUMBER_COLUMN);
        String branch = getStringValue(row, ColumnUtil.BRANCH_COLUMN);
        String type = getStringValue(row, ColumnUtil.TYPE_COLUMN);
        String date = getStringValue(row, ColumnUtil.DATE_COLUMN);
        String model = getStringValue(row, ColumnUtil.MODEL_COLUMN);
        String series = getStringValue(row, ColumnUtil.SERIES_COLUMN);
        String situation = getStringValue(row, ColumnUtil.SITUATION);
        String partner = getStringValue(row, ColumnUtil.PARTNER_COLUMN);
        int cfop = getIntValue(row, ColumnUtil.CFOP_COLUMN);
        BigDecimal amount = getDecimalValue(row, ColumnUtil.AMOUNT_COLUMN);
        int productNumber = getIntValue(row, ColumnUtil.PRODUCTNUMBER_COLUMN);
        String productID = getStringValue(row, ColumnUtil.PRODUCTID_COLUMN);
        String icms_cst = getStringValue(row, ColumnUtil.ICMSCST_COLUMN);
        int taxRate = getIntValue(row, ColumnUtil.TAXRATE_COLUMN);
        BigDecimal icms = getDecimalValue(row, ColumnUtil.ICMS);
        return new Invoice(partner, number, situation, branch,
                type, date, model, series, cfop, amount, productNumber,
                productID, icms_cst, taxRate, icms, 00.00);
    }

    /**
     * Retorna um valor decimal BigDecimal obtido da célula correspondente na linha da planilha.
     *
     * @param row Linha a ser processada
     * @param ColumnUtil Coluna a ser processada
     * @return Retorna o valor lido em decimal BigDecimal
     */

    private BigDecimal getDecimalValue(Row row, ColumnUtil ColumnUtil) {
        return BigDecimal.valueOf(getColumn(row, ColumnUtil).getFloatValue()).setScale(2, RoundingMode.HALF_EVEN);
    }

    /**
     *  Retorna um valor inteiro int obtido da célula correspondente na linha da planilha.
     *
     * @param row Linha a ser processada
     * @param ColumnUtil Coluna a ser processada
     * @return Retorna o número Integer lido
     */

    private int getIntValue(Row row, ColumnUtil ColumnUtil) {
        return getColumn(row, ColumnUtil).getIntValue();
    }

    /**
     * Retorna um valor de string String obtido da célula correspondente na linha da planilha.
     *
     * @param row Linha a ser processada
     * @param ColumnUtil Coluna a ser processada
     * @return Retorna o valor de String obtido
     */

    private String getStringValue(Row row, ColumnUtil ColumnUtil) {
        return getColumn(row, ColumnUtil).getStringValue();
    }

    /**
     * Retorna uma célula de acordo com o enumerador ColumnUtil.
     *
     * @param row Linha a ser processada
     * @param ColumnUtil Coluna a ser processada
     * @return Coluna a ser lida
     */

    private Cell getColumn(Row row, ColumnUtil ColumnUtil) {
        ExcelUtil excelUtil = new ExcelUtil();
        return row.getCellOrNull(excelUtil.getColumn(ColumnUtil));
    }
}
