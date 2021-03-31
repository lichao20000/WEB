package com.linkage.module.gwms.obj.tabquery;

/**
 * @author Jason(3412)
 * @date 2009-9-4
 */
public class ExpertOBJ {

	private String id;
	private String exName;
	private String exRegular;
	private String exBias;
	private String exFaultDesc;
	private String exSuggest;
	private String exColumn;
	private String exFiled;
	private String exDesc;
	
	
	/*** getter and setter **/
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getExName() {
		return exName;
	}
	public void setExName(String exName) {
		this.exName = exName;
	}
	public String getExRegular() {
		return exRegular;
	}
	public void setExRegular(String exRegular) {
		this.exRegular = exRegular;
	}
	public String getExBias() {
		return exBias;
	}
	public void setExBias(String exBias) {
		this.exBias = exBias;
	}
	public String getExFaultDesc() {
		return exFaultDesc;
	}
	public void setExFaultDesc(String exFaultDesc) {
		this.exFaultDesc = exFaultDesc;
	}
	public String getExSuggest() {
		return exSuggest;
	}
	public void setExSuggest(String exSuggest) {
		this.exSuggest = exSuggest;
	}
	public String getExColumn() {
		return exColumn;
	}
	public void setExColumn(String exColumn) {
		this.exColumn = exColumn;
	}
	public String getExFiled() {
		return exFiled;
	}
	public void setExFiled(String exFiled) {
		this.exFiled = exFiled;
	}
	public String getExDesc() {
		return exDesc;
	}
	public void setExDesc(String exDesc) {
		this.exDesc = exDesc;
	}
	
	/**
	 * 覆盖父类
	 */
	public String toString(){
		return id + ";" + exFiled + ";" + exFaultDesc;
	}
}
