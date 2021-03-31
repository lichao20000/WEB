package com.linkage.module.gtms.resource.serv;

import java.io.File;

import com.linkage.litms.system.UserRes;


public interface ImportDeviceInitServ {
	
	/**
	 * 解析导入的文件
	 * 
	 * @param file     上传的文件
	 * @param rowNum   最多处理的记录数（单位：行）
	 * @param curUser  当前操作人ID
	 * @param gw_type  设备类型（1 家庭网关，2 企业网管）
	 * @param fileType 文件类型（xls，txt）
	 * @param fileName 文件名
	 * @return
	 */
	public String readUploadFile(File file, int rowNum, UserRes curUser, String gw_type,
			String fileType, String fileName);
	
}
