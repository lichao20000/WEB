package com.linkage.module.gtms.blocTest.serv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.blocTest.dao.MaintainAppInfoDAO;
import com.linkage.module.itms.resource.act.DeviceTypeInfoACT;

/**
 * 
 * @author zzd (Ailk No.)
 * @version 1.0
 * @since 2016-8-9
 * @category com.linkage.module.gtms.blocTest.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MaintainAppInfoBIO
{
	// 日志记录
		private static Logger logger = LoggerFactory
				.getLogger(MaintainAppInfoBIO.class);
		private MaintainAppInfoDAO dao;
		private int maxPage_splitPage;
		
		public Map querydelPath(long id){
			return dao.querydelPath(id);
		}
		
		public List queryDetailList(String detailid){
			
			List list = dao.queryDetailList(detailid);
			Map map = (Map)list.get(0);
			if(null != map.get("app_publish_time")&& !"".equals(("app_publish_time"))){
				
				long app_publish_time=StringUtil.getLongValue(map.get("app_publish_time"));
				if(0 != app_publish_time){
					app_publish_time = app_publish_time * 1000L;
					DateTimeUtil dt = new DateTimeUtil(app_publish_time);
					map.put("app_publish_time", dt.getLongDate());
				}else{
					map.put("app_publish_time", "");
				}
				list.clear();
				list.add(map);
			}
			
			return list;
		}
		public int pubAppInfo(long id,long app_publish_time){
			return dao.pubAppInfo(id,app_publish_time);
		}
		
		public int updAppInfo(String id,String appuuid,String app_name,String app_desc,String app_vendor,String app_version,String app_publish_status,Long create_time,String create_user,Long app_publish_time,String fileAppPath)
		{
			
			return dao.updateAppInfo(id, appuuid, app_name, app_desc, app_vendor, app_version, app_publish_status,create_time , create_user, app_publish_time, fileAppPath);
		}
		public int delAppInfo(String id){
			return dao.delAppInfo(StringUtil.getLongValue(id));
		}
		
		public List<Map> queryDevList(int curPage_splitPage, int num_splitPage, String appuuid, String app_name, 
				String app_vendor, String app_version, String  app_publish_status, 
				String app_publish_time_start, String app_publish_time_end){
			if("-1".equals(app_publish_status)){
				app_publish_status = "";
			}
			
			List<Map> list = dao.queryDeviceList(curPage_splitPage, num_splitPage, appuuid, app_name, app_vendor, 
					app_version, app_publish_status, app_publish_time_start, app_publish_time_end);
			maxPage_splitPage = dao.getDeviceListCount(curPage_splitPage, num_splitPage, appuuid, app_name, app_vendor,
										app_version, app_publish_status, app_publish_time_start, app_publish_time_end);
			return list;
		}
		
		public String addMaintainAppInfo(long id, String appuuid, String app_name,
				String app_desc, String app_vendor, String app_version, long app_publish_time
				, String  app_publish_status, String create_user, long create_time, String file_path) {
			String msg = "";
			int temp = dao.addMaintainAppInfo(id, appuuid, app_name, app_desc, app_vendor, app_version, app_publish_time, 
					app_publish_status, create_user, create_time, file_path);
			
			if (temp != 0) {
				msg = "2";
			} else {
				msg = "3";
			}
			return msg;
		}
		
		public String deleteFile(String file_path)
		{
			logger.debug("deleteFile({})", file_path);
			if(StringUtil.IsEmpty(file_path))
			{
				return "0";
			}
			File file = new File(file_path);
			boolean result = true;
			if(file.exists())
			{
				result = file.delete();
			}
			return result?"1":"0"; //1成功,0失败
		}
		
		/**
		 * 保存文件
		 * 
		 * @param source
		 * @param newFileDir
		 * @param newFileName
		 * @return
		 */
		public String saveFile(File source, String newFileDir, String newFileName)
		{
			
			String result = "1";
			File target = new File(newFileDir, newFileName);
			try
			{
				// 文件复制
				FileUtils.copyFile(source, target);
			}
			catch (Exception e)
			{
				logger.error("copy file error:", e);
				result = "0";
			}
			// 1成功,0失败
			return result;
		}
		public int getMaxPage_splitPage() {
			return maxPage_splitPage;
		}
		public int checkFilename(String filename){
			List fileNamelist = dao.checkFileName();
			
			 for(Iterator<Map> it = fileNamelist.iterator();it.hasNext();)    {   
			       Map fileCheck = (Map)it.next();
			       
			       String filePath = fileCheck.get("file_path").toString();
			       String fileNameExist = filePath.substring(filePath.lastIndexOf("/")+1);
			       if(filename.equals(fileNameExist)){
			    	   return 1;
			       }
			   }   
			return 0;
		}
		public MaintainAppInfoDAO getDao()
		{
			return dao;
		}

		
		public void setDao(MaintainAppInfoDAO dao)
		{
			this.dao = dao;
		}

		
		public void setMaxPage_splitPage(int maxPage_splitPage)
		{
			this.maxPage_splitPage = maxPage_splitPage;
		}
		
}
