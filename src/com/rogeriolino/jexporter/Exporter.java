package com.rogeriolino.jexporter;

import com.rogeriolino.jexporter.db.out.Output;

/**
 * 
 * @author Rogerio Lino <rogeriolino@gmail.com>
 */
public interface Exporter {
    
    public void export(Output out);
    
}
