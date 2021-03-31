/**
 * @(#)Object97orginal.java 2006-1-19
 * 
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.netcutover;

import java.util.List;

/**
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public class Object97orginal {
	/**采集点ID*/
	public List gather_id;
	/**工单唯一编号*/
	public String work97id = "";
	/**属地标识*/
	public String work97areaid = "";
	/**业务唯一标识*/
	public String productid = "";
	/**发送时间*/
	public String sendtime = "";
	
	/**
	 * Constrator
	 *
	 */
	public Object97orginal() {
	}

	/**
	 * 采集点ID
	 * @return Returns the gather_id.
	 */
	public List getGather_id() {
		return gather_id;
	}

	/**
	 * 采集点ID
	 * @param gather_id The gather_id to set.
	 */
	public void setGather_id(List gather_id) {
		this.gather_id = gather_id;
	}

	/**
	 * 业务唯一标识
	 * @return Returns the productid.
	 */
	public String getProductid() {
		return productid;
	}

	/**
	 * 业务唯一标识
	 * @param productid The productid to set.
	 */
	public void setProductid(String productid) {
		this.productid = productid;
	}

	/**
	 * 发送时间
	 * @return Returns the sendtime.
	 */
	public String getSendtime() {
		return sendtime;
	}

	/**
	 * 发送时间
	 * @param sendtime The sendtime to set.
	 */
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	/**
	 * 属地标识
	 * @return Returns the work97areaid.
	 */
	public String getWork97areaid() {
		return work97areaid;
	}

	/**
	 * 属地标识
	 * @param work97areaid The work97areaid to set.
	 */
	public void setWork97areaid(String work97areaid) {
		this.work97areaid = work97areaid;
	}

	/**
	 * 工单唯一编号
	 * @return Returns the work97id.
	 */
	public String getWork97id() {
		return work97id;
	}

	/**
	 * 工单唯一编号
	 * @param work97id The work97id to set.
	 */
	public void setWork97id(String work97id) {
		this.work97id = work97id;
	}
	

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//
//	}

}
