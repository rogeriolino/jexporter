package com.rogeriolino.jexporter.db;

import com.rogeriolino.jexporter.Exporter;
import com.rogeriolino.jexporter.db.jdbc.JdbcConnection;
import com.rogeriolino.jexporter.db.out.DummyHydrator;
import com.rogeriolino.jexporter.db.out.Hydrator;
import com.rogeriolino.jexporter.db.out.Output;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Database table exporter
 * 
 * @author Rogerio Lino <rogeriolino@gmail.com>
 */
public class TableExporter implements Exporter {

    private String sql;
    private boolean showHeader = true;
    private final JdbcConnection conn;
    private Hydrator hydrator;
    private long rows;
    
    public TableExporter(JdbcConnection conn) {
        this.conn = conn;
        this.hydrator = new DummyHydrator();
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    public String sql() {
        return sql;
    }
    
    public long rows() {
        return rows;
    }

    public TableExporter sql(String sql) {
        this.sql = sql;
        return this;
    }
    
    public Hydrator getHydrator() {
        return hydrator;
    }

    public void setHydrator(Hydrator hydrator) {
        this.hydrator = hydrator;
    }
    
    public TableExporter exportAll(String tableName, Output out) {
        return sql("SELECT * FROM " + tableName).export(out);
    }
    
    @Override
    public TableExporter export(Output out) {
        try {
            rows = 0;
            PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                if (rows == 0) {
                    out.begin();
                    if (showHeader) {
                        out.header(rs.getMetaData(), hydrator);
                    }
                }
                out.row(rs, hydrator);
                rows++;
            }
            if (rows > 0) {
                out.end();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public void flush() {
        try {
            conn.getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(TableExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
