package com.fb.sap.bapi;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoFunction;

public class InventoryService {

	public static String SAP_SERVER = "qabapi";

	public float getInventoryLevel(String dcCode, String storageLoc,
			String articleId) throws Exception {
		// JCoDestination is the logic address of an ABAP system and ...
		JCoDestination destination = JCoDestinationManager
				.getDestination(SAP_SERVER);
		// ... it always has a reference to a metadata repository
		JCoFunction function = destination.getRepository().getFunction(
				"ZBAPI_FM_TINLASTKQTY");
		if (function == null)
			throw new RuntimeException(
					"ZBAPI_FM_TINLASTKQTY not found in SAP.");

		// JCoFunction is container for function values. Each function contains
		// separate
		// containers for import, export, changing and table parameters.
		// To set or get the parameters use the APIS setValue() and getXXX().
		function.getImportParameterList().setValue("PLANT", dcCode);
		function.getImportParameterList().setValue("MATERIAL", articleId);
		function.getImportParameterList().setValue("STGE_LOC", storageLoc);

		try {
			// execute, i.e. send the function to the ABAP system addressed
			// by the specified destination, which then returns the function
			// result.
			// All necessary conversions between Java and ABAP data types
			// are done automatically.
			function.execute(destination);
		} catch (AbapException e) {
			System.out.println(e.toString());
			throw new Exception(e);
		}

		System.out.println("ZBAPI_FM_TINLASTKQTY finished:");
		System.out.println(" Echo: "
				+ function.getExportParameterList().getString("SITE"));
		System.out.println(" Response: "
				+ function.getExportParameterList().getString("ARTICAL"));
		System.out.println(" Response: "
				+ function.getExportParameterList().getString("STRGE_LOC"));
		System.out.println(" Response: "
				+ function.getExportParameterList().getString("STOCK_QTY"));
		System.out.println(" Response: "
				+ function.getExportParameterList().getString("UNIT"));
		System.out.println();

		return Float.parseFloat(function.getExportParameterList().getString("STOCK_QTY"));
	}

}
