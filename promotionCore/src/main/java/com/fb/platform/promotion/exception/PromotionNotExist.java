package com.fb.platform.promotion.exception;


/**
*
* @author Tuhin
*/
public class PromotionNotExist extends DataAccessException{
   
   private static final long serialVersionUID = 7663648500246675024L;

   public PromotionNotExist(int id) {
       super("Promotion Id doesnot exist from promotion "+id);
   }
   
}
