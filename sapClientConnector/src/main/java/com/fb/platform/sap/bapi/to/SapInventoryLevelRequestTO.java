package com.fb.platform.sap.bapi.to;

public class SapInventoryLevelRequestTO {
	
	private String plant;
	private String material;
	private int storageLocation;

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
	
	@Override
	public String toString() {
		return "SapInventoryLevelRequestTO [plant=" + plant + ", material="
				+ material + ", storageLocation=" + storageLocation + "]";
	}

}
