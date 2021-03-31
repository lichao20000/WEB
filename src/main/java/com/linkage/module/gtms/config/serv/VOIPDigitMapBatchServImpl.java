package com.linkage.module.gtms.config.serv;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gtms.config.dao.VOIPDigitMapBatchDAO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.PreProcessCorba;

public class VOIPDigitMapBatchServImpl implements VOIPDigitMapBatchServ {
	private static Logger logger = LoggerFactory.getLogger(VOIPDigitMapBatchServImpl.class);
	private VOIPDigitMapBatchDAO dao;
	/**
	 * 查询所有语音数图
	 */
	public List<Map> queryAllDigitMap(String flag) {
			return dao.queryAllDigitMap(flag);
	}
	public String queryDigitMapById(String mapId, String flag){
		return dao.queryDigitMapById(mapId,flag);
	}
	/**
	 * 调用配置模块	
	 * @return 配置是否成功
	 */
	public String doConfigAll(String[] deviceIds, String serviceId,
			String[] paramArr, String gw_type) {
	   logger.debug("doConfigAll({},{},{})",new Object[]{deviceIds,serviceId,paramArr});
		//调用接口
		if (1== CreateObjectFactory.createPreProcess(gw_type).processDeviceStrategy(deviceIds,serviceId,paramArr)){
			logger.debug("调用后台预读模块成功");
			return "1";
		} else {
			logger.warn("调用后台预读模块失败");
			return "-4";
		}
		
	}
	/***
	 * 下载模板文件
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
			
			if(null==response){
				logger.warn("null==response");
			}
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
					fis=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				if(os!=null){
					os.close();
					os=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	 * 每页绑定数据
	 */
	public List<Map> getDetailsForPage(List<Map> device_idList,String starttime ,String endtime ,
				String openState,int curPage_splitPage, int num_splitPage) {
		logger.warn("VOIPDigitMapBatchServImpl.getDetailsForPage()");
		List<Map> list=new ArrayList<Map>();
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if(!"".equals(starttime) && starttime != null){
			starttimeCount = DateUtil.getTimeInSecond(starttime, "yyyy-MM-dd HH:mm:ss");
		}
		if(!"".equals(endtime) && endtime != null){
			endtimeCount = DateUtil.getTimeInSecond(endtime, "yyyy-MM-dd HH:mm:ss");
		}
		String device_id="'";
		device_id += StringUtil.getStringValue(device_idList.get(0),"device_id")+"'";
		list=dao.getDetailsForPage(device_id,starttimeCount,
		endtimeCount,openState,curPage_splitPage,num_splitPage);
			
		return list;
		
	}
	
	/*
	 * 获取最大页数
	 */
	public int getDetailsCount(List<Map> device_idList,String starttime ,String endtime,
			 String openState,int num_splitPage) {
		logger.warn("VOIPDigitMapBatchServImpl.getDetailsCount()");
		int maxPage = 1;
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if(!"".equals(starttime) && starttime != null){
			starttimeCount = DateUtil.getTimeInSecond(starttime, "yyyy-MM-dd HH:mm:ss");
		}
		if(!"".equals(endtime) && endtime != null){
			endtimeCount = DateUtil.getTimeInSecond(endtime, "yyyy-MM-dd HH:mm:ss");
		}
		String device_id="'";
		device_id += StringUtil.getStringValue(device_idList.get(0),"device_id")+"'";
		maxPage = dao.getDetailsCount(device_id,starttimeCount,endtimeCount,openState,num_splitPage);
		
		return maxPage;
		
	}
	
	/*
	 * 开始日期格式转换  yyyy-mm-dd --> 毫秒
	 */
	public static long startTimeOfMonth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		System.out.println(cal.getTime());
		
		return cal.getTimeInMillis() / 1000;
	}
	
	/*
	 * 结束日期转换  yyyy-mm-dd --> 毫秒
	 */
	public static long endTimeOfMonth(Date date){
		// 将当前月设置为第一天后，加1个月，然后-1秒。
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		System.out.println(cal.getTime());
		
		return cal.getTimeInMillis() / 1000 - 1;
	}
	public VOIPDigitMapBatchDAO getDao() {
		return dao;
	}

	public void setDao(VOIPDigitMapBatchDAO dao) {
		this.dao = dao;
	}


}
