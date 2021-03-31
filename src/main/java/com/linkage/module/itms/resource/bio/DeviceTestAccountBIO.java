package com.linkage.module.itms.resource.bio;

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

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.resource.dao.DeviceTestAccountDAO;

public class DeviceTestAccountBIO {
	private static Logger logger = LoggerFactory.getLogger(DeviceTestAccountBIO.class);

	private DeviceTestAccountDAO dao;

	public String getDeviceHardVersion(String deviceModelId) {
		logger.debug("GwDeviceQueryBIO=>getDeviceHardVersion(deviceModelId:{})",deviceModelId);
		List list = dao.getDeviceHardVersion(deviceModelId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("hardwareversion"));
			bf.append("$");
			bf.append(map.get("hardwareversion"));
		}
		return bf.toString();
	}

	public String getSoftVersion(String hardVercion,
			String deviceModelId) {
		logger.debug("GwDeviceQueryBIO=>getSoftVersion({}-{})",hardVercion,deviceModelId);
		List list = dao.getSoftVersion(hardVercion,deviceModelId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("devicetype_id"));
			bf.append("$");
			bf.append(map.get("softwareversion"));
		}
		return bf.toString();
	}

	public List<Map<String, String>> getList(String vendorId,String deviceModelId, String goal_devicetypeId, String starttime1, String endtime1, int curPage_splitPage, int num_splitPage) {
		return dao.getList(vendorId,deviceModelId,goal_devicetypeId,starttime1,endtime1, curPage_splitPage, num_splitPage);
	}

	public String addTestAccountPath(String device_serialnumber,
			String vendorId, String deviceModelId, String goal_devicetypeId,
			String gwShare_fileName, long userid) {
		
		return dao.addTestAccountPath(device_serialnumber,vendorId,deviceModelId,goal_devicetypeId,gwShare_fileName,userid);
	}

	public DeviceTestAccountDAO getDao() {
		return dao;
	}

	public void setDao(DeviceTestAccountDAO dao) {
		this.dao = dao;
	}

	public void download(String filepath, HttpServletResponse response) {
		logger.warn("download({},{})", new Object[]{filepath, response});
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
			response.addHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
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

	public int getListCount(String vendorId, String deviceModelId,
			String goal_devicetypeId, String starttime1, String endtime1, int curPage_splitPage, int num_splitPage) {
		return dao.getListCount(vendorId,deviceModelId,goal_devicetypeId,starttime1,endtime1, curPage_splitPage, num_splitPage);
	}
	
}
