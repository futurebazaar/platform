package com.fb.exception;

/**
*
* @author Tuhin
*/
import java.io.PrintStream;
import java.io.PrintWriter;

public class DataAccessException extends Exception {

   private static final long serialVersionUID = 1L;
   /** A wrapped Throwable */
   protected Throwable cause;

   public DataAccessException() {
       super("A data exception occurred");
   }

   public DataAccessException(String message) {
       super(message);
   }

   public DataAccessException(String message, Throwable cause) {
       super(message);
       this.cause = cause;
   }

   public Throwable getCause() {
       return cause;
   }

   public Throwable initCause(Throwable cause) {
       this.cause = cause;
       return cause;
   }

   /** Builds the message off of its own message + all nested exception messages */
   public String getMessage() {
       // Get the message for this exception
       String msg = super.getMessage();


       // Get the message for each nested exception
       Throwable parent = this;
       Throwable child;
       while ((child = parent.getCause()) != null) {
           String msg2 = child.getMessage();

           if (msg2 != null) {
               if (msg != null) {
                   msg += ": " + msg2;
               } else {
                   msg = msg2;
               }
           }

           if (child instanceof DataAccessException) {
               break;
           }

           parent = child;
       }
       return msg;
   }

   /** Prints the stack trace + all nested stack traces */
   public void printStackTrace() {
       // Print the stack trace for this exception.
       super.printStackTrace();

       // Print the stack trace for each nested exception.
       Throwable parent = this;
       Throwable child;
       while ((child = parent.getCause()) != null) {
           if (child != null) {
               System.err.print("Caused by: ");
               child.printStackTrace();

               if (child instanceof DataAccessException) {
                   break;
               }

               parent = child;
           }
       }
   }

   /** Prints the stack trace + all nested stack traces */
   public void printStackTrace(PrintStream s) {
       // Print the stack trace for this exception.
       super.printStackTrace(s);

       Throwable parent = this;
       Throwable child;

       // Print the stack trace for each nested exception.
       while ((child = parent.getCause()) != null) {
           if (child != null) {
               s.print("Caused by: ");
               child.printStackTrace(s);

               if (child instanceof DataAccessException) {
                   break;
               }

               parent = child;
           }
       }
   }

   /** Prints the stack trace + all nested stack traces */
   public void printStackTrace(PrintWriter w) {
       // Print the stack trace for this exception.
       super.printStackTrace(w);

       Throwable parent = this;
       Throwable child;

       // Print the stack trace for each nested exception.
       while ((child = parent.getCause()) != null) {
           if (child != null) {
               w.print("Caused by: ");
               child.printStackTrace(w);

               if (child instanceof DataAccessException) {
                   break;
               }
               parent = child;
           }
       }
   }
}
