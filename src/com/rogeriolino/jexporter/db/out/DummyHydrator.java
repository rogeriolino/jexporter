package com.rogeriolino.jexporter.db.out;

/**
 *
 * @author Rogerio Lino <rogeriolino@gmail.com>
 */
public class DummyHydrator implements Hydrator {

    @Override
    public String hydrate(String columnName) {
        return columnName;
    }
    
    @Override
    public String hydrate(String columnName, String columnValue) {
        return columnValue;
    }
    
}
