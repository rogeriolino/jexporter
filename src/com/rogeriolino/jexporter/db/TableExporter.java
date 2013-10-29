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
    
    public void exportAll(String tableName, Output out) {
        sql("SELECT * FROM " + tableName).export(out);
    }
    
    @Override
    public void export(Output out) {
        try {
            PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            out.begin();
            if (showHeader) {
                out.header(rs.getMetaData(), hydrator);
            }
            while (rs.next()) {
                out.row(rs, hydrator);
            }
            out.end();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void flush() {
        try {
            conn.getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(TableExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
