package com.linkage.module.gwms.resource.bio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.dao.BatchRestartDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-10-11
 * @category com.linkage.module.gwms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BatchRestartBIO 
{
	private static Logger logger = LoggerFactory.getLogger(BatchRestartBIO.class);
	private BatchRestartDAO dao;
	
	public void download(String filepath, HttpServletResponse response) 
	{
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
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
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
			try {
				if(fis!=null){
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(os!=null){
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String insert(List<String> dataList,String importQueryField,String user_Account,String batchType)
	{
		String res="";
		if("device_serialnumber".equals(importQueryField))
		{
			res=String.valueOf(dao.insertDevSn(dataList,user_Account,batchType));
		}else if(Global.NXDX.equals(Global.instAreaShortName) 
				 	&& "4".equals(batchType)){
			res=String.valueOf(dao.insertStbDeviceId(dataList,user_Account));
		}else{
			res=String.valueOf(dao.insertDeviceId(dataList,user_Account,batchType));
		}
		return res;
	}
	
	public String test(String batchType)
	{
		String res=String.valueOf(dao.TodayNumber(batchType));
		return res;
	}
	
	
	
	public BatchRestartDAO getDao(){
		return dao;
	}

	public void setDao(BatchRestartDAO dao){
		this.dao = dao;
	}
	
}
