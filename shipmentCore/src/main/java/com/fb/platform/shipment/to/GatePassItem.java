package com.fb.platform.shipment.to;

import org.joda.time.DateTime;


public class GatePassItem {
	
    private int deece;
    private String delno;
    private DateTime deldt;
    private long sonum;
    private long invno;
    private DateTime invdt;
    private long gtpas;
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
	public long getSonum() {
		return sonum;
	}
	public void setSonum(long sonum) {
		this.sonum = sonum;
	}
	public long getInvno() {
		return invno;
	}
	public void setInvno(long invno) {
		this.invno = invno;
	}
	public DateTime getInvdt() {
		return invdt;
	}
	public void setInvdt(DateTime invdt) {
		this.invdt = invdt;
	}
	public long getGtpas() {
		return gtpas;
	}
	public void setGtpas(long gtpas) {
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
