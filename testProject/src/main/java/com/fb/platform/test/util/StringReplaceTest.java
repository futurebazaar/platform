/**
 * 
 */
package com.fb.platform.test.util;

/**
 * @author vinayak
 *
 */
public class StringReplaceTest {

	public static String ztinladelivery = "<ZTINLA_DLVRY><IDOC BEGIN=\"1\"><ZTINLA_DLVRY SEGMENT=\"1\"><T_CODE>VNA07</T_CODE><MATNR>000000000100315457</MATNR><I_WERKS>2786</I_WERKS><R_WERKS>2786</R_WERKS><I_LGORT>10</I_LGORT>" +
			"<R_LGORT>90</R_LGORT><BWART>311</BWART><MEINS>EA</MEINS><TRNSFR_QUAN>2.000</TRNSFR_QUAN></ZTINLA_DLVRY>" +
			"<ZTINLA_DLVRY SEGMENT=\"1\"><T_CODE>VNA07</T_CODE><MATNR>000000000100315457</MATNR><I_WERKS>2786</I_WERKS><R_WERKS>2786</R_WERKS><I_LGORT>10</I_LGORT><R_LGORT>90</R_LGORT><BWART>311</BWART>" +
			"<MEINS>EA</MEINS><TRNSFR_QUAN>2.000</TRNSFR_QUAN></ZTINLA_DLVRY></IDOC></ZTINLA_DLVRY>";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String tempidocXml = ztinladelivery.replaceFirst("ZTINLA_DLVRY", "ZTINLADLVRY_TOP");
		int index = tempidocXml.lastIndexOf("ZTINLA_DLVRY");
		StringBuffer sb = new StringBuffer();
		sb.append(tempidocXml.substring(0, index));
		sb.append("ZTINLADLVRY_TOP");
		sb.append(tempidocXml.substring(index + 12));
		//ztinladelivery = sb.toString();

		System.out.println(ztinladelivery);
		System.out.println(sb.toString());

	}

}
