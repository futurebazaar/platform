package com.fb.exception.promotion;

import com.fb.exception.DataAccessException;

/**
*
* @author Tuhin
*/
public class UnableDbUpdate extends DataAccessException {
   private static final long serialVersionUID = -3656795630429106768L;

   public UnableDbUpdate(int id) {
       super("Unable to update the database whose promotion code is "+id);
   }
   
}
