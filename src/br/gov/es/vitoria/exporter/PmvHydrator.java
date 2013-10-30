package br.gov.es.vitoria.exporter;

import com.rogeriolino.jexporter.db.out.Hydrator;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Rogerio Lino <rogeriolino@gmail.com>
 */
public class PmvHydrator implements Hydrator {
    
    private Locale locale = new Locale("pt", "BR");

    @Override
    public String hydrate(String columnName) {
        return columnName;
    }

    @Override
    public String hydrate(String columnName, String columnValue) {
        // is numeric
        if (columnName.equals("prncipal") || columnName.equals("multa") || columnName.equals("juros")) {
            try {
                BigDecimal decimal = new BigDecimal(columnValue);
                NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
                return nf.format(decimal);
            } catch (NumberFormatException e) {
            }
        } else if (columnName.equals("dt_processamento")) {
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
            return df.format(java.sql.Timestamp.valueOf(columnValue));
        } else if (columnName.equals("num_doc_pessoa")) {
            // cpf
            if (columnValue.length() == 11) {
                return columnValue.substring(0, 3) + "."
                        + columnValue.substring(3, 6) + "."
                        + columnValue.substring(6, 9) + "-"
                        + columnValue.substring(9);
            } 
            // cnpj
            else if (columnValue.length() == 14) {
                return columnValue.substring(0, 2) + "."
                        + columnValue.substring(2, 5) + "."
                        + columnValue.substring(5, 8) + "/"
                        + columnValue.substring(8, 12) + "-"
                        + columnValue.substring(12);
            }
        }
        return columnValue;
    }

}
