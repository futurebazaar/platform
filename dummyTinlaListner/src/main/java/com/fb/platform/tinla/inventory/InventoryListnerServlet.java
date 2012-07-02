/**
 * 
 */
package com.fb.platform.tinla.inventory;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author vinayak
 *
 */
public class InventoryListnerServlet extends HttpServlet {

	private static Log log = LogFactory.getLog(InventoryListnerServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("Received Tinla inventory message.");
		resp.setContentType("text/html");
	    PrintWriter out = resp.getWriter();

	    out.println("<title>Success</title>");
	    log.info("====================== PARAMETERES ======================");
	    log.info("transactioncode : " + req.getParameter("transactioncode"));
	    log.info("articleid : " + req.getParameter("articleid"));
	    log.info("issuingsite : " + req.getParameter("issuingsite"));
	    log.info("receivingsite : " + req.getParameter("receivingsite"));
	    log.info("issuingstorageloc : " + req.getParameter("issuingstorageloc"));
	    log.info("receivingstorageloc : " + req.getParameter("receivingstorageloc"));
	    log.info("movementtype : " + req.getParameter("movementtype"));
	    log.info("sellingunit : " + req.getParameter("sellingunit"));
	    log.info("quantity : " + req.getParameter("quantity"));

	    out.close();
	    log.info("Sent success response to inventoryReceive module.");
	}
}
