package com.linkage.module.gtms.resource.action;

import java.io.InputStream;

/**
 * 
 * @author Administrator(工号) Tel:
 * @version 1.0
 * @since Apr 19, 2013 10:12:10 AM
 * @category com.linkage.module.gtms.resource.action
 * @copyright 南京联创科技 网管科技部
 *
 * 安徽电信 导入需要采购的设备 接口类 
 *
 */
public interface ImportDeviceInitAction {
	
	/**
	 * 初始化导入界面
	 * @return
	 */
	public String execute();
	
	/**
	 * 解析导入的文件
	 * @return
	 */
	public String readUploadFile();
	
	/**
	 * Excel模板下载
	 * @return
	 */
	public String downloadTemplate();
	
	/**
	 * 读取Excel2003模板文件
	 * @return
	 */
	public InputStream getExportExcelStream();
	
}
