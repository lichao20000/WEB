package com.linkage.module.ids.bio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.VoiceRegStatusAnalysisRepDAO;


public class VoiceRegStatusAnalysisRepBIO {
	private static Logger logger = LoggerFactory
			.getLogger(VoiceRegStatusAnalysisRepBIO.class);
	private VoiceRegStatusAnalysisRepDAO dao; 
	/**
	 * 语音端口统计
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public List<Map<String,String>> voiceRegQueryInfo(String start_time,
			String end_time,String reportType) {
		return dao.voiceRegQueryInfo(start_time,end_time,reportType);
	}
	public List<Map<String,String>> voiceRegQueryInfoByModel(String start_time,
			String end_time,String reportType) {
		return dao.voiceRegQueryInfoByModel(start_time,end_time,reportType);
	}
	public List<Map<String,String>> getPPPOEFailStatus(String startOpenDate1,
			String endOpenDate1, String reportType) {
		// TODO Auto-generated method stub
		return dao.getPPPOEFailStatus(startOpenDate1,endOpenDate1,reportType);
	}
	/**
	 * 获取报表的公网地址路径
	 * @return
	 */
	public String getFilePath() {
		// TODO Auto-generated method stub
		return dao.getFilePath();
	}
	/***
	 * 下载报表文件
	 * @param filepath
	 * @param response
	 */
	public void download(String filepath, HttpServletResponse response) {
		logger.debug("download({},{})", new Object[]{filepath, response});
		
		InputStream fis=null;
		OutputStream os=null;
		try
		{
			// path是指欲下载的文件的路径
			File file = new File(filepath);
			// 取得文件名
			String filename = file.getName();

			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(filepath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			fis=null;
			
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			os = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			os.write(buffer);
			os.flush();
			os.close();
			os=null;
		}
		catch (IOException e)
		{
			logger.error("download file:[{}], error:", filepath, e);
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void setDao(VoiceRegStatusAnalysisRepDAO dao) {
		this.dao = dao;
	}
	public List<Map<String, String>> getVoicRegFailedReasonQuery(
			String startOpenDate1, String endOpenDate1, String reportType) {
		// TODO Auto-generated method stub
		return dao.getVoicRegFailedReasonQuery(startOpenDate1,endOpenDate1,reportType);
	}
	public List<Map<String, String>> getDeviceTempQuery(String startOpenDate1,
			String endOpenDate1, String reportType) {
		// TODO Auto-generated method stub
		return dao.getDeviceTempQuery(startOpenDate1, endOpenDate1, reportType);
	}
	/**
	 * 指欲下载的文件的路径的文件是否存在
	 * @param filePath
	 * @return 
	 */
	public String fileIsExist(String absPathSuffer ,String pathSuffer,String filePath) {
		String isExist = "-1, ";
		// path是指欲下载的文件的路径
		File file = new File(absPathSuffer+filePath);
		if(file.exists()){
			isExist = "1,"+pathSuffer+filePath;
		}
		return isExist;
	}
}
