/**
 * 
 */
package com.fb.commons.util;

/**
 * @author vinayak
 *
 */
public interface ObjectConverter {

    /**
     * Logic for applying conversion
     * @param type the type of object to convert 
     * @param obj the object to convert
     * @return the converted object
     */
    public Object convert(Class type, Object obj);
    
    /**
     * Logic for applying conversion to an array 
     * @param type the type of object to convert
     * @param o the object to convert
     * @return the converted object
     */
    public Object[] convert(Class type, Object[] obj);
}
