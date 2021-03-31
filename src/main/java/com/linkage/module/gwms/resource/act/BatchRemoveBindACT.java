package com.linkage.module.gwms.resource.act;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.resource.bio.BatchRemoveBindBIO;

public class BatchRemoveBindACT extends splitPageAction implements SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//文件名
	private String filename;
	//上传时间
	private String uploadTime;
	
	private String ajax;
	
	private List<Map> list;
	
	private String taskId;
	
	private String upload_date;
	// session
	private Map session;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private BatchRemoveBindBIO bio;
	/**
	 * 解绑
	 * @return
	 * @throws IOException
	 */
	public String removeBind() throws IOException{
		UserRes curUser = (UserRes) session.get("curUser");
		String user_id = String.valueOf(curUser.getUser().getId());
		String[] arr = filename.split("_");
		upload_date = arr[0];
		taskId = String.valueOf(user_id)+"_"+String.valueOf(upload_date);
		bio.removeBind(filename, taskId, user_id,upload_date, curUser.getUser().getAccount(), 1);
		ajax = taskId;
		return "ajax";
	}
	
	/**
	 * 获取解绑结果
	 * @return
	 */
	public String getResultList(){
		UserRes curUser = (UserRes) session.get("curUser");
		String user_id = String.valueOf(curUser.getUser().getId());
		list = bio.getResultList(taskId, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getResultCount(taskId, curPage_splitPage, num_splitPage);
		return "rsList";
	}
	
	/**
	 * 判断文件行数
	 * @return
	 */
	public String fileLineTest(){
		ajax=bio.fileLineTest(filename);
		return "ajax";
	}
	
	/**
	 * 下载结果文件
	 * @return
	 */
	public String resultExcel(){
		column = new String[] { "acc_loginname", "upload_date", "filename", "loid","device_id", "result_info" };
		title = new String[] { "用户", "上传时间", "文件名", "loid", "设备ID", "解绑结果" };
		fileName = "批量解绑结果";
		data = bio.getResultExcel(taskId);
		return "excel";
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public List<Map> getList() {
		return list;
	}

	public void setList(List<Map> list) {
		this.list = list;
	}

	public BatchRemoveBindBIO getBio() {
		return bio;
	}

	public void setBio(BatchRemoveBindBIO bio) {
		this.bio = bio;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	

	public Map getSession() {
		return session;
	}


	public String getUpload_date() {
		return upload_date;
	}

	public void setUpload_date(String upload_date) {
		this.upload_date = upload_date;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	
}
