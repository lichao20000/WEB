package com.linkage.module.gtms.config.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.dao.DelWanConnToolsDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;

public class DelWanConnToolsBIO {

	private static Logger logger = LoggerFactory.getLogger(DelWanConnToolsBIO.class);
	// 回传消息
	private String msg = null;
	private StringBuffer error_info = null;
	private DelWanConnToolsDAO dao;
	
	/**
	 * 查询格局导入数据、校验数据并返回查询结果设备列表
	 * @param curUser
	 * @param gwShare_cityId
	 * @param gwShare_fileName
	 * @param faultList
	 * @return
	 */
	public List<HashMap<String, String>> getDeviceList(long userId,UserRes curUser,
			String cityId, String fileName, String faultList,int maxupline) {

		try {
			faultList = "";
			if (fileName.length() < 4) {
				this.msg = "上传的文件名不正确！";
				return null;
			}
			String fileName_ = fileName.substring(fileName.length() - 3, fileName.length());
			if (!"txt".equals(fileName_)) {
				this.msg = "上传的文件格式不正确！";
				return null;
			}
			List<HashMap<String,String>> dataList = getImportDataByTXT(fileName);
			if (null == dataList || dataList.isEmpty() || dataList.size() < 1 || !StringUtils.isEmpty(error_info)) {
				this.msg = "文件解析到不合法数据，请修正！" +error_info;
				return null;
			} else if (dataList.size() > maxupline) {
				this.msg = "文件行数不要超过"+maxupline+"行";
				return null;
			} else {
				// 处理结果
				//dao.insertTabDelWanConn(userId,dataList);
				logger.warn("解析完毕:{}",dataList);
			}
			return dataList;
		} catch (Exception e) {
			logger.warn("{}文件解析出错！", fileName);
			logger.warn("Exception:{}",ExceptionUtils.getStackTrace(e));
			this.msg = "文件解析出错，请重新上传！";
			return null;
		}
	}


	@SuppressWarnings("rawtypes")
	public String doConfigAll(long userId, List<Map> jsonToObject) {
		long insertTabDelWanConn = dao.insertTabDelWanConn(userId,jsonToObject);
		if(insertTabDelWanConn<0){
			return "-4";
		}
		return "1";
	}
	/**
	 * 解析文件
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	private List<HashMap<String,String>> getImportDataByTXT(String fileName) throws IOException {
		logger.warn("getImportDataByTXT:{}", fileName);
		error_info = new StringBuffer();
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath() + fileName));
		String line = in.readLine();
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		int coutline = 2;
		while ((line = in.readLine()) != null) {
			coutline++;
			if (!"".equals(line.trim())) {
				String[] split = line.trim().split("\\|");
				if(null == split || split.length!=3){
					error_info.append("第"+(coutline)+"行错误：必填数据为空,信息："+line+";");
					continue;
				}
				if(StringUtil.IsEmpty(split[0]) || StringUtil.IsEmpty(split[1]) || StringUtil.IsEmpty(split[2])){
					error_info.append("第"+(coutline)+"行错误：必填数据为空,信息："+line+";");
					continue;
				}
				if(!"INTERNET".equals(split[1]) && !"VOIP".equals(split[1]) 
						&& !"ITV".equals(split[1]) && !"VPDN".equals(split[1])){
					error_info.append("第"+(coutline)+"行错误：必填业务类型字段有误,信息："+line+";");
					continue;
				}
				
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("loid", split[0]);
				map.put("serv_type", split[1]);
				map.put("vlanid", split[2]);
				list.add(map);
			}
		}
		
		in.close();
		in = null;
		// 处理完毕时，则删掉文件
		File f = new File(getFilePath() + fileName);
		f.delete();
		f = null;
		if(!StringUtils.isEmpty(error_info)){
			return null;
		}
		return list;
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try {
			lipossHome = java.net.URLDecoder.decode(a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public StringBuffer getError_info() {
		return error_info;
	}

	public void setError_info(StringBuffer error_info) {
		this.error_info = error_info;
	}

	public DelWanConnToolsDAO getDao() {
		return dao;
	}

	public void setDao(DelWanConnToolsDAO dao) {
		this.dao = dao;
	}

	
}
