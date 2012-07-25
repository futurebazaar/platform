/**
 * 
 */
package com.fb.commons.mom.to;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

/**
 * @author nehaga
 *
 * @XmlType(name = "", propOrder = {
    "mandt",
    "sovbeln",
    "soposnr",
    "vbelv",
    "posnv",
    "vbeln",
    "posnn",
    "vbtypn",
    "rfmng",
    "meins",
    "rfwrt",
    "bwart",
    "waers",
    "matnr",
    "erzet",
    "erdat",
    "plmin",
    "vbtypv",
    "aedat",
    "brgew",
    "volum",
    "gbstk",
    "abstk",
    "lfgsk",
    "lfstk",
    "cmgst",
    "wbstk",
    "fkstk",
    "kostk",
    "pkstk",
    "fksak",
    "completeness",
    "material",
    "site",
    "audat",
    "auart",
    "zmeins",
    "pstyv",
    "lfart",
    "erdatdel",
    "ernamdel"
})
 */
public class OrderTO implements Serializable {
	private SapMomTO sapIdoc;
	
	public SapMomTO getSapIdoc() {
		return sapIdoc;
	}
	public void setSapIdoc(SapMomTO sapIdoc) {
		this.sapIdoc = sapIdoc;
	}
	
	@Override
	public String toString() {
		String order = "";
		if(sapIdoc != null) {
			order += "\n" + sapIdoc.toString();
		}
		return order;
		
	}
		   
		   
}
