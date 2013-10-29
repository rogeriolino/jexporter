package com.rogeriolino.jexporter.db.out;

/**
 *
 * @author Rogerio Lino <rogeriolino@gmail.com>
 */
public interface Hydrator {
    
    public String hydrate(String columnName);
    public String hydrate(String columnName, String columnValue);
    
}
