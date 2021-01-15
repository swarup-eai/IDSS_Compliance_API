package com.eai.idss.vo;

public class MandatoryReportsResponseVo {

	private EWasteVo eWaste;
	private BatteryVo battery;
	private PlasticVo plastic;
	private BioMedWasteVo bioMedWaste;
	public EWasteVo geteWaste() {
		return eWaste;
	}
	public void seteWaste(EWasteVo eWaste) {
		this.eWaste = eWaste;
	}
	public BatteryVo getBattery() {
		return battery;
	}
	public void setBattery(BatteryVo battery) {
		this.battery = battery;
	}
	public PlasticVo getPlastic() {
		return plastic;
	}
	public void setPlastic(PlasticVo plastic) {
		this.plastic = plastic;
	}
	public BioMedWasteVo getBioMedWaste() {
		return bioMedWaste;
	}
	public void setBioMedWaste(BioMedWasteVo bioMedWaste) {
		this.bioMedWaste = bioMedWaste;
	}
	
	
}
