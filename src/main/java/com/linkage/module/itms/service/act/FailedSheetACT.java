package com.linkage.module.itms.service.act;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.itms.service.bio.FailedSheetBIO;

public class FailedSheetACT extends splitPageAction {

	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(FailedSheetACT.class);
	private FailedSheetBIO bio;
	private String batsheet_filename;
	private List<String> dataList;
	private String ajax = null;

	/**
	 * 从文件中读取并补发工单
	 * 
	 * @return
	 */
	public String reSendSheet() {
		logger.warn("FailedSheetACT-->reSendSheet");
		if (null != batsheet_filename) {
			batsheet_filename.trim();
		}
		// 从文件中过滤出工单内容，获取每行failedSheet:[]中的内容
		this.dataList = bio.getFaileSheetInList(batsheet_filename);
		int[] resArr = bio.reSendSheet(dataList);
		// 依次为补录总数，解析异常消息，补录发送失败条数，补录失败条数，补录成功条数
		ajax = resArr[0] + "," + bio.getMsg() + "," + resArr[1] + "," + resArr[2]
				+ "," + resArr[3];
		return "showReSendResult";
	}

	public FailedSheetBIO getBio() {
		return bio;
	}

	public void setBio(FailedSheetBIO bio) {
		this.bio = bio;
	}

	public String getBatsheet_filename() {
		return batsheet_filename;
	}

	public void setBatsheet_filename(String batsheet_filename) {
		this.batsheet_filename = batsheet_filename;
	}

	public List<String> getDataList() {
		return dataList;
	}

	public void setDataList(List<String> dataList) {
		this.dataList = dataList;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
}
