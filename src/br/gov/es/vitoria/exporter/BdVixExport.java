package br.gov.es.vitoria.exporter;

import com.rogeriolino.jexporter.db.TableExporter;
import com.rogeriolino.jexporter.db.jdbc.MsSqlConnection;
import com.rogeriolino.jexporter.db.out.CsvOutput;
import com.rogeriolino.jexporter.db.out.Output;


/**
 *
 * @author Rogerio Lino <rogeriolino@gmail.com>
 */
public class BdVixExport {
    
    public static String SQL = "" + 
            "   SELECT  " +
            "      t.cod_tipo_cadastro, t.inscricao_cadastral, t.tipo_pessoa, t.num_doc_pessoa, t.nome_pessoa, " +
            "      o.descricao, t.numero_termo_inscricao, t.ano_termo_inscricao, s.Descricao_Situacao_Documento, " +
            "      t.prncipal, t.multa, t.juros " +
            "   FROM  " +
            "      bdpmv.dbo.temp_debitos_da_camara t " +
            "      INNER JOIN origens_debito o " +
            "           ON t.cod_origem_debito = o.cod_origem_debito " +
            "      INNER JOIN situacoes_documentos_pmv s  " +
            "           ON t.cod_situacao_documento = s.cod_situacao_documento " + 
            "   WHERE " + 
            "     nome_pessoa LIKE ':nome%'" +
            "   ORDER BY  " +
            "      t.nome_pessoa,  " +
            "      t.cod_tipo_cadastro,  " +
            "      t.inscricao_cadastral,  " +
            "      t.ano_termo_inscricao,  " +
            "      t.numero_termo_inscricao";
    
    
    public static void main(String[] args) throws Exception {
        String chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZ";
        String path = "C:\\Users\\PC391108\\Desktop\\export\\";
        MsSqlConnection conn = new MsSqlConnection();
        conn.connect("sqlvix.pmv.local", 1433, "bdpmv", "usrImobiliario", "9ijn2wsx");
        TableExporter te = new TableExporter(conn);
        te.setHydrator(new PmvHydrator());
        long total = 0;
        StringBuilder exceto = new StringBuilder();
        System.out.println("Exportando...");
        for (int i = 0; i < chars.length(); i++) {
            char c = chars.charAt(i);
            Output out = new CsvOutput(path + "debitoDA-" + c + ".csv");
            long rows = te.sql(SQL.replace(":nome", String.valueOf(c))).export(out).rows();
            if (rows > 0) {
                System.out.println("- " + c + ": " + rows);
            }
            if (i > 0) {
                exceto.append(",");
            }
            exceto.append("'").append(c).append("'");
            total += rows;
        }
        // outros
        Output out = new CsvOutput(path + "debitoDA-OUTROS.csv");
        long rows = te.sql(SQL.replace("nome_pessoa LIKE ':nome%'", "SUBSTRING (nome_pessoa, 1, 1) NOT IN (" + exceto.toString() + ")")).export(out).rows();
        if (rows > 0) {
            System.out.println("- OUTROS: " + rows);
            total += rows;
        }
        System.out.println("Total: " + total);
        conn.close();
    }
    
}
