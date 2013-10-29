package com.rogeriolino.jexporter.db.out;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rogerio Lino <rogeriolino@gmail.com>
 */
public class CsvOutput implements Output {
    
    public static final int MAX_ROWS = 65536;
    private static final String NEW_LINE = "\r\n";
    
    private int fileId;
    private int currentRow;
    private final String filename;
    private String separator = ";";
    private String delimiter = "\"";
    private File file;
    private BufferedWriter writer;

    public CsvOutput(String filename) {
        this.filename = filename;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
    
    @Override
    public void begin() {
        fileId = 1;
        create(filename);
    }

    @Override
    public void end() {
        close();
    }

    @Override
    public void header(ResultSetMetaData rsmd, Hydrator hydrator) throws SQLException {
        StringBuilder row = new StringBuilder();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            if (i > 1) {
                row.append(";");
            }
            String columnName = rsmd.getColumnName(i);
            row.append(csvColumn(hydrator.hydrate(columnName)));
        }
        try {
            writer.write(row.toString() + NEW_LINE);
        } catch (IOException ex) {
            Logger.getLogger(CsvOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.currentRow++;
    }
    
    @Override
    public void row(ResultSet rs, Hydrator hydrator) throws SQLException {
        if (this.currentRow >= MAX_ROWS) {
            // spliting file in parts (Excel limit exceded)
            split(rs, hydrator);
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        StringBuilder row = new StringBuilder();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            if (i > 1) {
                row.append(separator);
            }
            String columnName = rsmd.getColumnName(i);
            String columnValue = rs.getString(i);
            row.append(csvColumn(hydrator.hydrate(columnName, columnValue)));
        }
        try {
            writer.write(row.toString() + NEW_LINE);
        } catch (IOException ex) {
            Logger.getLogger(CsvOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.currentRow++;
    }
    
    private void create(String filename) {
        try {
            this.currentRow = 0;
            this.file = new File(filename);
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void close() {
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(CsvOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void split(ResultSet rs, Hydrator hydrator) throws SQLException {
        fileId++;
        close();
        create(filename.replace(".csv", fileId + ".csv"));
        header(rs.getMetaData(), hydrator);
    }
    
    private String csvColumn(String value) {
        return delimiter + value + delimiter;
    }
    
}
