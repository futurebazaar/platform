package com.fb.platform.shipment.to;

import org.joda.time.DateTime;


public class GatePassItem {
	
    private int deece;
    private String delno;
    private DateTime deldt;
    private int sonum;
    private int invno;
    private DateTime invdt;
    private int gtpas;
    private String awbno;
    private ShipmentLSPEnum lspcode;
    
	public int getDeece() {
		return deece;
	}
	public void setDeece(int deece) {
		this.deece = deece;
	}
	public String getDelno() {
		return delno;
	}
	public void setDelno(String delno) {
		this.delno = delno;
	}
	public DateTime getDeldt() {
		return deldt;
	}
	public void setDeldt(DateTime deldt) {
		this.deldt = deldt;
	}
	public int getSonum() {
		return sonum;
	}
	public void setSonum(int sonum) {
		this.sonum = sonum;
	}
	public int getInvno() {
		return invno;
	}
	public void setInvno(int invno) {
		this.invno = invno;
	}
	public DateTime getInvdt() {
		return invdt;
	}
	public void setInvdt(DateTime invdt) {
		this.invdt = invdt;
	}
	public int getGtpas() {
		return gtpas;
	}
	public void setGtpas(int gtpas) {
		this.gtpas = gtpas;
	}
	public String getAwbno() {
		return awbno;
	}
	public void setAwbno(String awbno) {
		this.awbno = awbno;
	}
	public ShipmentLSPEnum getLspcode() {
		return lspcode;
	}
	public void setLspcode(ShipmentLSPEnum lspcode) {
		this.lspcode = lspcode;
	}
    
	@Override
	public String toString() {
		return 	"deece : " + getDeece() + "\n" +
				"delno : " + getDelno() + "\n" +
				"deldt : " + getDeldt() + "\n" +
				"sonum : " + getSonum() + "\n" +
				"invno : " + getInvno() + "\n" +
				"invdt : " + getInvdt() + "\n" +
				"gtpas : " + getGtpas() + "\n" +
				"awbno : " + getAwbno() + "\n" +
				"lspcode : " + getLspcode() ;
		
	}
    

}
