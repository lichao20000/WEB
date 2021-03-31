package com.linkage.litms.other;

//内部类（存储数据用）
public class TableStruct {
	private String dxbh;
	private String sxbh;
	private String slmc;
	private String csz;
	private String maxvalue;
	private String minvalue;
	private String avgvalue;
	private String cysj;
	private String cyjg;
	private String totalcyjg;
	private String ip;

	public TableStruct() {

	}

	public TableStruct(
		String dxbh,
		String sxbh,
		String slmc,
		String csz,
		String maxvalue,
		String minvalue,
		String avgvalue,
		String cysj,
		String cyjg,
		String totalcyjg,
		String ip) {
		this.dxbh = dxbh;
		this.sxbh = sxbh;
		this.slmc = slmc;
		this.csz = csz;
		this.maxvalue = maxvalue;
		this.minvalue = minvalue;
		this.avgvalue = avgvalue;
		this.cysj = cysj;
		this.cyjg = cyjg;
		this.totalcyjg = totalcyjg;
		this.ip = ip;
	}

	/**
	 * @return
	 */
	public String getAvgvalue() {
		return avgvalue;
	}

	/**
	 * @return
	 */
	public String getCysj() {
		return cysj;
	}

	/**
	 * @return
	 */
	public String getDxbh() {
		return dxbh;
	}

	/**
	 * @return
	 */
	public String getMaxvalue() {
		return maxvalue;
	}

	/**
	 * @return
	 */
	public String getMinvalue() {
		return minvalue;
	}

	/**
	 * @return
	 */
	public String getSlmc() {
		return slmc;
	}

	/**
	 * @return
	 */
	public String getSxbh() {
		return sxbh;
	}

	/**
	 * @return
	 */
	public String getTotalcyjg() {
		return totalcyjg;
	}

	/**
	 * @param string
	 */
	public void setAvgvalue(String string) {
		avgvalue = string;
	}

	/**
	 * @param string
	 */
	public void setCysj(String string) {
		cysj = string;
	}

	/**
	 * @param string
	 */
	public void setDxbh(String string) {
		dxbh = string;
	}

	/**
	 * @param string
	 */
	public void setMaxvalue(String string) {
		maxvalue = string;
	}

	/**
	 * @param string
	 */
	public void setMinvalue(String string) {
		minvalue = string;
	}

	/**
	 * @param string
	 */
	public void setSlmc(String string) {
		slmc = string;
	}

	/**
	 * @param string
	 */
	public void setSxbh(String string) {
		sxbh = string;
	}

	/**
	 * @param string
	 */
	public void setTotalcyjg(String string) {
		totalcyjg = string;
	}

	/**
	 * @return
	 */
	public String getCsz() {
		return csz;
	}

	/**
	 * @return
	 */
	public String getCyjg() {
		return cyjg;
	}

	/**
	 * @param string
	 */
	public void setCsz(String string) {
		csz = string;
	}

	/**
	 * @param string
	 */
	public void setCyjg(String string) {
		cyjg = string;
	}

	/**
	 * @return
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param string
	 */
	public void setIp(String string) {
		ip = string;
	}

}
