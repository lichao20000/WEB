/**
 * @(#)WorkSheet.java 2006-1-17
 * 
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.netcutover;

import java.util.List;

/**
 * 工单类JavaBean,相关get,set方法
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public class ObjectWorkSheet {

	/**
	 * 系统内部工单ID	String
	 */
	public String worksheet_id = "";

	/**
	 * 采集点ID
	 */
	public List gather_id;

	/**
	 * 工单执行次数,从1开始计数
	 */
	public int worksheet_exec_time = 0;

	/**
	 * 来自何处0: 97系统1: 手工录入		String
	 */
	public int worksheet_source = 0;

	/**
	 * 97属地标识
	 */
	public String system_id = "";

	/**
	 * 97工单唯一标识		String
	 */
	public String sheet_id = "";

	/**
	 * 业务唯一标识	String
	 */
	public String product_id = "";

	/**
	 * 工单接收时间	String
	 */
	public String worksheet_receive_time = "";

	/**
	 * 业务类型(1：客户，2：窄带，3：adsl，4：lan) int
	 */
	public int producttype = 0;

	/**
	 * 操作类型(1：打开 2： 关闭)	int
	 */
	public int servtype = 0;

	/**
	 * 用户帐号	String
	 */
	public String username = "";

	/**
	 * 处理优先级	int
	 */
	public int worksheet_priority = 0;

	/**
	 * 工单本次状态(0: 未处理1: 正在处理) String
	 */
	public String worksheet_status = "";

	/**
	 * 工单本次开始执行时间
	 */
	public String worksheet_start_time = "";

	/**
	 * 工单本次执行完成的时间 
	 */
	public String worksheet_end_time = "";

	/**
	 * 工单出错原因(0: 无错.1: 因为其子工单执行错误而中止)	int
	 */
	public int worksheet_error_no = 0;

	/**
	 * 工单执行结果描述
	 */
	public String worksheet_error_desc = "";

	/**
	 * 显示13位设备编码
	 */
	public String deviceencode = "";
	
	/**
	 * 设备编号
	 */
	public String device_id = "";

	/**
	 * Constrator
	 */
	public ObjectWorkSheet() {
		super();
	}

	/**
	 * 显示13位设备编码
	 * @return Returns the deviceencode.
	 */
	public String getDeviceencode() {
		return deviceencode;
	}

	/**
	 * 显示13位设备编码
	 * @param deviceencode The deviceencode to set.
	 */
	public void setDeviceencode(String deviceencode) {
		this.deviceencode = deviceencode;
	}

	/**
	 * 采集点ID
	 * @return Returns the gather_id.	String
	 */
	public List getGather_id() {
		return gather_id;
	}

	/**
	 * 采集点ID
	 * @param gather_id The gather_id to set.	String
	 */
	public void setGather_id(List gather_id) {
		this.gather_id = gather_id;
	}

	/**
	 * 业务唯一标识
	 * @return Returns the product_id.	String
	 */
	public String getProduct_id() {
		return product_id;
	}

	/**
	 * 业务唯一标识
	 * @param product_id The product_id to set.		String
	 */
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	/**
	 * 业务类型
	 * @return Returns the producttype.
	 * 		<UL>
	 * 		<LI>1:客户
	 * 		<LI>2:窄带
	 * 		<LI>3:adsl
	 * 		<LI>4:LAN
	 * 		</UL>
	 */
	public int getProducttype() {
		return producttype;
	}

	/**
	 * 业务类型
	 * 		<UL>
	 * 		<LI>1:客户
	 * 		<LI>2:窄带
	 * 		<LI>3:adsl
	 * 		<LI>4:LAN
	 * 		</UL>
	 * @param producttype The producttype to set.
	 */
	public void setProducttype(int producttype) {
		this.producttype = producttype;
	}

	/**
	 * 操作类型
	 * @return Returns the servtype.
	 * 		<UL>
	 * 		<LI>1:打开
	 * 		<LI>2:关闭
	 * 		<LI>3:暂停
	 * 		<LI>4:更改速率
	 * 		<LI>5:移机
	 * 		<LI>6:移机
	 * 		</UL>
	 */
	public int getServtype() {
		return servtype;
	}

	/**
	 * 操作类型
	 * 		<UL>
	 * 		<LI>1:打开
	 * 		<LI>2:关闭
	 * 		<LI>3:暂停
	 * 		<LI>4:更改速率
	 * 		<LI>5:移机
	 * 		<LI>6:移机
	 * 		</UL>
	 * @param servtype The servtype to set.
	 */
	public void setServtype(int servtype) {
		this.servtype = servtype;
	}

	/**
	 * 97工单唯一标识	
	 * @return Returns the sheet_id.
	 */
	public String getSheet_id() {
		return sheet_id;
	}

	/**
	 * 97工单唯一标识
	 * @param sheet_id The sheet_id to set.
	 */
	public void setSheet_id(String sheet_id) {
		this.sheet_id = sheet_id;
	}

	/**
	 * 97属地标识
	 * @return Returns the system_id.
	 */
	public String getSystem_id() {
		return system_id;
	}

	/**
	 * 97属地标识
	 * @param system_id The system_id to set.
	 */
	public void setSystem_id(String system_id) {
		this.system_id = system_id;
	}

	/**
	 * 用户账号
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 用户账号
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 工单本次执行完成的时间
	 * @return Returns the worksheet_end_time.
	 */
	public String getWorksheet_end_time() {
		return worksheet_end_time;
	}

	/**
	 * 工单本次执行完成的时间
	 * @param worksheet_end_time The worksheet_end_time to set.
	 */
	public void setWorksheet_end_time(String worksheet_end_time) {
		this.worksheet_end_time = worksheet_end_time;
	}

	/**
	 * 工单执行结果描述
	 * @return Returns the worksheet_error_desc.
	 */
	public String getWorksheet_error_desc() {
		return worksheet_error_desc;
	}

	/**
	 * 工单执行结果描述
	 * @param worksheet_error_desc The worksheet_error_desc to set.
	 */
	public void setWorksheet_error_desc(String worksheet_error_desc) {
		this.worksheet_error_desc = worksheet_error_desc;
	}

	/**
	 * 工单出错原因
	 * @return Returns the worksheet_error_no.
	 */
	public int getWorksheet_error_no() {
		return worksheet_error_no;
	}

	/**
	 * 工单出错原因
	 * @param worksheet_error_no The worksheet_error_no to set.
	 */
	public void setWorksheet_error_no(int worksheet_error_no) {
		this.worksheet_error_no = worksheet_error_no;
	}

	/**
	 * 工单执行次数,工单第几被执行，从1开始计数
	 * @return Returns the worksheet_exec_time.
	 */
	public int getWorksheet_exec_time() {
		return worksheet_exec_time;
	}

	/**
	 * 工单执行次数,工单第几被执行，从1开始计数
	 * @param worksheet_exec_time The worksheet_exec_time to set.
	 */
	public void setWorksheet_exec_time(int worksheet_exec_time) {
		this.worksheet_exec_time = worksheet_exec_time;
	}

	/**
	 * 系统内部工单ID	String
	 * @return Returns the worksheet_id.
	 */
	public String getWorksheet_id() {
		return worksheet_id;
	}

	/**
	 * 系统内部工单ID	String
	 * @param worksheet_id The worksheet_id to set.
	 */
	public void setWorksheet_id(String worksheet_id) {
		this.worksheet_id = worksheet_id;
	}

	/**
	 * 处理优先级
	 * @return Returns the worksheet_priority.
	 */
	public int getWorksheet_priority() {
		return worksheet_priority;
	}

	/**
	 * 处理优先级
	 * @param worksheet_priority The worksheet_priority to set.
	 */
	public void setWorksheet_priority(int worksheet_priority) {
		this.worksheet_priority = worksheet_priority;
	}

	/**
	 * 工单接收时间
	 * @return Returns the worksheet_receive_time.
	 */
	public String getWorksheet_receive_time() {
		return worksheet_receive_time;
	}

	/**
	 * 工单接收时间
	 * @param worksheet_receive_time The worksheet_receive_time to set.
	 */
	public void setWorksheet_receive_time(String worksheet_receive_time) {
		this.worksheet_receive_time = worksheet_receive_time;
	}

	/**
	 * 来自何处
	 * 		<UL>
	 * 		<LI>0:97系统
	 * 		<LI>1:手工录入
	 * 		</UL>
	 * @return Returns the worksheet_source.
	 */
	public int getWorksheet_source() {
		return worksheet_source;
	}

	/**
	 * 来自何处
	 * 		<UL>
	 * 		<LI>0:97系统
	 * 		<LI>1:手工录入
	 * 		</UL>
	 * @param worksheet_source The worksheet_source to set.
	 */
	public void setWorksheet_source(int worksheet_source) {
		this.worksheet_source = worksheet_source;
	}

	/**
	 * 工单本次开始执行时间
	 * @return Returns the worksheet_start_time.
	 */
	public String getWorksheet_start_time() {
		return worksheet_start_time;
	}

	/**
	 * 工单本次开始执行时间
	 * @param worksheet_start_time The worksheet_start_time to set.
	 */
	public void setWorksheet_start_time(String worksheet_start_time) {
		this.worksheet_start_time = worksheet_start_time;
	}

	/**
	 * 工单本次状态
	 * @return Returns the worksheet_status.eg."1,2"	String
	 * 		<UL>
	 * 		<LI>0:未处理
	 * 		<LI>1:正在处理
	 * 		<LI>2:已处理，成功
	 * 		<LI>3:已处理，失败
	 * 		</UL>
	 */
	public String getWorksheet_status() {
		return worksheet_status;
	}

	/**
	 * 工单本次状态
	 * 		<UL>
	 * 		<LI>0:未处理
	 * 		<LI>1:正在处理
	 * 		<LI>2:已处理，成功
	 * 		<LI>3:已处理，失败
	 * 		</UL>
	 * @param worksheet_status The worksheet_status to set.	int
	 */
	public void setWorksheet_status(String worksheet_status) {
		this.worksheet_status = worksheet_status;
	}

	/**
	 * @return Returns the device_id.
	 */
	public String getDevice_id() {
		return device_id;
	}

	/**
	 * @param device_id The device_id to set.
	 */
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	
	
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//
//	}

}
