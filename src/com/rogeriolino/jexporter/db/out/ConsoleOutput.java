package com.rogeriolino.jexporter.db.out;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author Rogerio Lino <rogeriolino@gmail.com>
 */
public class ConsoleOutput implements Output {

    @Override
    public void header(ResultSetMetaData rsmd, Hydrator hydrator) throws SQLException {
        StringBuilder row = new StringBuilder();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            String columnName = rsmd.getColumnName(i);
            row.append(hydrator.hydrate(columnName)).append(", ");
        }
        System.out.println(row.toString());
    }
    
    @Override
    public void row(ResultSet rs, Hydrator hydrator) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        StringBuilder row = new StringBuilder();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            String columnName = rsmd.getColumnName(i);
            String columnValue = rs.getString(i);
            row.append(hydrator.hydrate(columnName, columnValue)).append(", ");
        }
        System.out.println(row.toString());
    }

    @Override
    public void begin() {
        System.out.println("=====================");
    }

    @Override
    public void end() {
        System.out.println("=====================");
    }
    
}
