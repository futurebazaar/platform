package com.fb.platform.sap.bapi.to;

public class SapInventoryLevelRequestTO {
	
	private String plant;
	private String material;
	private int storageLocation;
	private String client;

	public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public int getStorageLocation() {
		return storageLocation;
	}
	public void setStorageLocation(int storageLocation) {
		this.storageLocation = storageLocation;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getClient() {
		return client;
	}

	@Override
	public String toString() {
		return "SapInventoryLevelRequestTO [plant=" + plant + ", material="
				+ material + ", storageLocation=" + storageLocation + ", client=" + client + "]";
	}
	
}
