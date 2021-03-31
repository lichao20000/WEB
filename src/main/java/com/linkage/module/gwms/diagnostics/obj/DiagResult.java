package com.linkage.module.gwms.diagnostics.obj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jason(3412)
 * @date 2009-7-6
 */
public class DiagResult {

	//诊断状态：1表示诊断成功,且没有故障; -1表示诊断成功,有故障,无记录; -2表示诊断失败; -3表示多条记录诊断，诊断的故障描述和专家建议直接置于每条记录中 
	private int pass;
	
	private List<Map<String,String>> rList;
	
	//pass==-1时候，专家建议
	private String suggest;
	//pass==-1时候，故障描述
	private String faultDesc;
	
	//pass==-2时候，描述失败原因
	private String failture;
	

	
	public DiagResult(){
		super();
		//默认诊断通过
		pass = 1;
		rList = new ArrayList<Map<String,String>>();
	}

	
	/**
	 * 诊断是否成功
	 * 	true 成功
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-7-6
	 * @return boolean
	 */
	public boolean isSuccess(){
		if(-2 == pass){
			return false;
		}
		return true;
	}
	
	public int getPass() {
		return pass;
	}

	public void setPass(int pass) {
		this.pass = pass;
	}

	public List<Map<String, String>> getRList() {
		return rList;
	}

	public void setRList(List<Map<String, String>> list) {
		rList = list;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public String getFaultDesc() {
		return faultDesc;
	}


	public void setFaultDesc(String faultDesc) {
		this.faultDesc = faultDesc;
	}


	public String getFailture() {
		return failture;
	}


	public void setFailture(String failture) {
		this.failture = failture;
	}
	
}
