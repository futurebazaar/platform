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
		log.info("Received Tinla message.");
		resp.setContentType("text/html");
	    PrintWriter out = resp.getWriter();

	    out.println("<title>Success</title>");
	    log.info("====================== PARAMETERES ======================");
	    log.info("sender : " + req.getParameter("sender"));
	    log.info("idocnumber : " + req.getParameter("idocnumber"));
	    log.info("transactioncode : " + req.getParameter("transactioncode"));
	    log.info("articleid : " + req.getParameter("articleid"));
	    log.info("issuingsite : " + req.getParameter("issuingsite"));
	    log.info("receivingsite : " + req.getParameter("receivingsite"));
	    log.info("issuingstorageloc : " + req.getParameter("issuingstorageloc"));
	    log.info("receivingstorageloc : " + req.getParameter("receivingstorageloc"));
	    log.info("movementtype : " + req.getParameter("movementtype"));
	    log.info("sellingunit : " + req.getParameter("sellingunit"));
	    log.info("quantity : " + req.getParameter("quantity"));
	    //log.info("" + req.getParameter(""));
	    log.info("sapDocumentId : " + req.getParameter("sapDocumentId"));
	    log.info("orderHeaderDelBlock : " + req.getParameter("orderHeaderDelBlock"));
	    log.info("header : " + req.getParameter("header"));
	    log.info("deliveryDate : " + req.getParameter("deliveryDate"));
	    log.info("UOM : " + req.getParameter("UOM"));
	    log.info("shipComments : " + req.getParameter("shipComments"));
	    log.info("orderType : " + req.getParameter("orderType"));
	    log.info("deliveryNumber : " + req.getParameter("deliveryNumber"));
	    log.info("blockMsg : " + req.getParameter("blockMsg"));
	    log.info("itemCategory : " + req.getParameter("itemCategory"));
	    log.info("orderId : " + req.getParameter("orderId"));
	    log.info("deliveryType : " + req.getParameter("deliveryType"));
	    log.info("lspName : " + req.getParameter("lspName"));
	    log.info("awbNumber : " + req.getParameter("awbNumber"));
	    log.info("createdBy : " + req.getParameter("createdBy"));
	    log.info("createdDate : " + req.getParameter("createdDate"));
	    log.info("skuID : " + req.getParameter("skuID"));
	    log.info("lspUpdDescr : " + req.getParameter("lspUpdDescr"));
	    log.info("plantId : " + req.getParameter("plantId"));
	    log.info("itemState : " + req.getParameter("itemState"));
	    log.info("orderDate : " + req.getParameter("orderDate"));
	    log.info("atgDocumentId : " + req.getParameter("atgDocumentId"));
	    log.info("cancelInvoiceNumber : " + req.getParameter("cancelInvoiceNumber"));
	    log.info("invoiceNumber : " + req.getParameter("invoiceNumber"));
	    log.info("invoiceDate : " + req.getParameter("invoiceDate"));
	    log.info("pgiCreationDate : " + req.getParameter("pgiCreationDate"));
	    log.info("pgrCreationDate : " + req.getParameter("pgrCreationDate"));
	    log.info("returndeliveryno : " + req.getParameter("returndeliveryno"));
	    log.info("returndeliveryType : " + req.getParameter("returndeliveryType"));
	    log.info("returnCreatedDate : " + req.getParameter("returnCreatedDate"));
	    log.info("returnCreatedBy : " + req.getParameter("returnCreatedBy"));
	    log.info("returnorderInvoiceno : " + req.getParameter("returnorderInvoiceno"));
	    log.info("returnInvoiceType : " + req.getParameter("returnInvoiceType"));
	    log.info("returnInvoiceDate : " + req.getParameter("returnInvoiceDate"));
	    log.info("returnInvoiceNet : " + req.getParameter("returnInvoiceNet"));
	    log.info("returnOrderId : " + req.getParameter("returnOrderId"));
	    log.info("returnQuantity : " + req.getParameter("returnQuantity"));
	    log.info("returnStorageLocation : " + req.getParameter("returnStorageLocation"));
	    log.info("returnCategory : " + req.getParameter("returnCategory"));

	    out.close();
	    log.info("Sent success response to MoM module.");
	}
}
