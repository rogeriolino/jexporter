package com.rogeriolino.jexporter.db.out;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author Rogerio Lino <rogeriolino@gmail.com>
 */
public interface Output {
    
    public void begin();
    public void end();
    
    public void header(ResultSetMetaData rsmd, Hydrator hydrator) throws SQLException;
    public void row(ResultSet rs, Hydrator hydrator) throws SQLException;
    
}
