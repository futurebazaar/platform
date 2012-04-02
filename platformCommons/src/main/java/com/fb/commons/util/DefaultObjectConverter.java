/**
 * 
 */
package com.fb.commons.util;

/**
 * Default object conversion implementation which does no conversion
 * 
 * @author vinayak
 *
 */
public class DefaultObjectConverter implements ObjectConverter {

    /**
     * Logic for applying conversion
     * @param type the type of object to convert 
     * @param obj the object to convert
     * @return the converted object
     */
    public Object convert(Class type, Object o) {
        return o; // The default is to do nothing
    }

    /**
     * Logic for applying conversion to an array 
     * @param type the type of object to convert
     * @param o the object to convert
     * @return the converted object
     */
    public Object[] convert(Class type, Object[] o) {
        return o; // The default is to do nothing
    }

}
