package com.linkage.module.gtms.stb.resource.serv;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.ftp.FtpClient;
import com.linkage.commons.util.StringUtil;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.dao.OpenDeviceShowPictureNewDAO;
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gtms.stb.utils.SqlUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.init.bio.AppInitBIO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-2-26
 * @category com.linkage.module.gtms.stb.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class OpenDeviceShowPictureNewBIO
{
	private static Logger logger = LoggerFactory
			.getLogger(OpenDeviceShowPictureNewBIO.class);
	private OpenDeviceShowPictureNewDAO dao;
	private String remoteAbsoluteUrl = ""; // 图片存放的目录
	private String remoteAbsoluteIP = "";  // 图片服务器的IP地址
	private String port ="";  //图片服务器的端口
	
	public List getOrderTaskList(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,String acc_loginname)
	{
		return dao.getOrderTaskList(curPage_splitPage, num_splitPage, startTime, endTime, cityId, taskName, acc_loginname);
	}
	public List getOrderTaskList1(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,String acc_loginname)
	{
		UserRes curUser = WebUtil.getCurrentUser();
		String city_id =  curUser.getUser().getCityId() ;
		List listhshow=new ArrayList();
		List listhshow1=new ArrayList();
		List list = CityDAO.getNextCityListByCityPid(city_id);
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			listhshow.add(map.get("city_id"));
		}
		List accoidList =dao.getrolename(listhshow);
		for (int i = 0; i < accoidList.size(); i++) {
			Map map = (Map) accoidList.get(i);
			listhshow1.add(map.get("acc_oid"));
		}
		return dao.getOrderTaskList1(curPage_splitPage, num_splitPage, startTime, endTime, cityId, taskName, acc_loginname, listhshow1);
	}
	public int countOrderTask1(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,String acc_loginname)
	{
		UserRes curUser = WebUtil.getCurrentUser();
		String city_id =  curUser.getUser().getCityId() ;
		List listhshow=new ArrayList();
		List listhshow1=new ArrayList();
		List list = CityDAO.getNextCityListByCityPid(city_id);
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			listhshow.add(map.get("city_id"));
		}
		List accoidList =dao.getrolename(listhshow);
		for (int i = 0; i < accoidList.size(); i++) {
			Map map = (Map) accoidList.get(i);
			listhshow1.add(map.get("acc_oid"));
		}
		return dao.countOrderTask1(curPage_splitPage, num_splitPage, startTime, endTime, cityId, taskName, acc_loginname, listhshow1);
	}
	public int countOrderTask(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,String acc_loginname)
	{
		return dao.countOrderTask(curPage_splitPage, num_splitPage, startTime, endTime, cityId, taskName, acc_loginname);
	}
	public List getTaskResult(String taskId ,int curPage_splitPage,int num_splitPage) {
		return dao.getTaskResult(taskId,curPage_splitPage, num_splitPage);
	}
	public int countDeviceTask(int curPage_splitPage, int num_splitPage,String taskId) {
		return dao.countTaskResult(curPage_splitPage, num_splitPage, taskId);
	}
	public List<Map<String,String>> getTaskDetail(String taskId )
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		String header = request.getScheme();
		List<Map> taskMap=dao.getTaskDetail1(taskId);
		List<Map<String,String>> listshow=new ArrayList<Map<String,String>>();
		String group="";
		
		for(int i=0;i<taskMap.size();i++)
		{
			Map<String,String> map=new HashMap<String, String>();
			
			String group_id=String.valueOf(taskMap.get(i).get("group_id"));
			if(!StringUtil.IsEmpty(group_id)&&!group_id.equals("null"))
			{
			String[] groupid=group_id.split(",");
			for(int j=0;j<groupid.length;j++)
			{
				List<Map<String,String>> list=dao.getGroupId(groupid[j]);
				group +=list.get(0).get("group_name")+",";
			}
			group=group.substring(0,group.length()-1);
			map.put("group_name", group);
			}else
			{
			map.put("group_name", " ");
			}
			
			String acc_oid=String.valueOf(taskMap.get(i).get("acc_oid"));//定制人
			List<Map> list=dao.getaccounts(acc_oid);
			map.put("acc_loginname", String.valueOf(list.get(0).get("acc_loginname")));
			
			String addtime=String.valueOf(taskMap.get(i).get("add_time"));//定制时间
			try {
				long add_time = StringUtil
						.getLongValue(addtime);
				DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
				map.put("add_time", dt.getLongDate());
			} catch (NumberFormatException e) {
				map.put("add_time", " ");
			} catch (Exception e) {
				map.put("add_time", " ");
			}
			String Invalid_time=String.valueOf(taskMap.get(i).get("invalid_time"));//定制时间
			try {
				long add_time = StringUtil
						.getLongValue(Invalid_time);
				DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
				map.put("Invalid_time", dt.getLongDate());
			} catch (NumberFormatException e) {
				map.put("Invalid_time", " ");
			} catch (Exception e) {
				map.put("Invalid_time", " ");
			}
			String priority=String.valueOf(taskMap.get(i).get("priority"));
			if(!StringUtil.IsEmpty(priority))
			{
				map.put("priority", "优先级"+priority);
			}else
			{
				map.put("priority", " ");
			}
			String vendor_id=String.valueOf(taskMap.get(i).get("vendor_id"));//厂商
			if (!StringUtil.IsEmpty(vendor_id)&&!vendor_id.equals("null")) {
				map.put("vendorName",DeviceTypeUtil.getVendorName(vendor_id));
			} else {
				map.put("vendorName", " ");
			}
			String webAddress = LipossGlobals.getLipossProperty("attachmentsDir.web");
			
			String sd_qd_pic_url=String.valueOf(taskMap.get(i).get("sd_qd_pic_url"));//启动图片
			if(!StringUtil.IsEmpty(sd_qd_pic_url)&!sd_qd_pic_url.equals("null"))
			{
				//String sdqdpicurl="http://"+webAddress+sd_qd_pic_url;
				map.put("sd_qd_pic_url", sd_qd_pic_url);
			}else
			{
				map.put("sd_qd_pic_url", " ");
			}
			String sd_kj_pic_url=String.valueOf(taskMap.get(i).get("sd_kj_pic_url"));//开机图片
			if(!StringUtil.IsEmpty(sd_kj_pic_url)&&!sd_kj_pic_url.equals("null"))
			{
				//String sdqdpicurl="http://"+webAddress+sd_kj_pic_url;
				map.put("sd_kj_pic_url", sd_kj_pic_url);
			}else
			{
				map.put("sd_kj_pic_url", " ");
			}
			String sd_rz_pic_url=String.valueOf(taskMap.get(i).get("sd_rz_pic_url"));//认证图片
			if(!StringUtil.IsEmpty(sd_rz_pic_url)&&!sd_rz_pic_url.equals("null"))
			{
				//String sdqdpicurl="http://"+webAddress+sd_rz_pic_url;
				map.put("sd_rz_pic_url", sd_rz_pic_url);
			}else
			{
				map.put("sd_rz_pic_url", " ");
			}
			
			String task_name=String.valueOf(taskMap.get(i).get("task_name"));//任务名称
			map.put("task_name", task_name);
			
			String task_id=String.valueOf(taskMap.get(i).get("task_id"));//任务名称
			map.put("task_id", task_id);
			
			String city_id=String.valueOf(taskMap.get(i).get("city_id"));//属地
			if(!StringUtil.IsEmpty(city_id)&&!city_id.equals("null")&&!city_id.equals("-1"))
			{
				String[] cityArr = city_id.split(",");
				String cityName = " ";
				for(String cityid : cityArr){
					cityName += CityDAO.getCityName(cityid) + ",";
				}
				map.put("cityName", cityName.substring(0, cityName.length() - 1).trim());
			}else
			{
				map.put("cityName", " ");	
			}
			
			String device_model_id=String.valueOf(taskMap.get(i).get("device_model_id"));//型号
			if(!StringUtil.IsEmpty(device_model_id)&&!device_model_id.equals("null"))
			{
				String[] devicemodelArr=device_model_id.split(",");
				String device_model="";
				for(String devicemodelid : devicemodelArr){
					List<HashMap<String, String>> devicemodel=dao.queryDeviceModel(devicemodelid);
					if(!StringUtil.IsEmpty(String.valueOf(devicemodel.get(0).get("device_model"))))
					{
						device_model += String.valueOf(devicemodel.get(0).get("device_model")) + ",";
					}
				}
				map.put("device_model", device_model);
			}else
			{
				map.put("device_model", " ");
			}
			String devicetype_id=String.valueOf(taskMap.get(i).get("devicetype_id"));//版本
			if(!StringUtil.IsEmpty(devicetype_id)&&!devicetype_id.equals("null"))
			{
				String[] devicetypeIds=devicetype_id.split(",");
				String devicetypeid="";
				for(String devicemodelid : devicetypeIds){
					List<HashMap<String, String>> devicetypeidArr=dao.getDeviceType(devicemodelid);
					if(devicetypeidArr != null && !devicetypeidArr.isEmpty() &&
							!StringUtil.IsEmpty(String.valueOf(devicetypeidArr.get(0).get("softwareversion"))))
					{
						devicetypeid += String.valueOf(devicetypeidArr.get(0).get("softwareversion")) + ",";
					}
				}
				map.put("softwareversion", devicetypeid);
			}else
			{
				map.put("softwareversion", " ");
			}
			
			String file_path=String.valueOf(taskMap.get(i).get("file_path"));
			String[] arrayStr = new String[6];
			if(!StringUtil.IsEmpty(file_path)&&!file_path.equals("null")){
				arrayStr = file_path.split("/");
			}
			if(!StringUtil.IsEmpty(file_path)&&!file_path.equals("null")){
				 file_path  = header+"://"+webAddress+arrayStr[3]+"/"+arrayStr[4]+"/"+arrayStr[5];
				 map.put("file_path",file_path);
			}else{
				map.put("file_path","否");
			}
			
			
			try{
				
				//开机路径
				String path_start_web=LipossGlobals.getLipossProperty("attachmentsDir.start");
				//启动路径
				String path_boot_web=LipossGlobals.getLipossProperty("attachmentsDir.boot");
				//认证路径
				String path_auth_web=LipossGlobals.getLipossProperty("attachmentsDir.auth");
				
				String path_file_web=LipossGlobals.getLipossProperty("attachmentsDir.accountFile");
				
				String projectName=LipossGlobals.getLipossProperty("projectName");
				
				String picName = sd_qd_pic_url.substring(sd_qd_pic_url.lastIndexOf("/")+1);
				
				logger.warn("projectName="+projectName+",path_auth_web="+path_auth_web);
				map.put("sd_qd_pic_url_view", projectName+path_boot_web+picName);
				
				picName = sd_kj_pic_url.substring(sd_kj_pic_url.lastIndexOf("/")+1);
				map.put("sd_kj_pic_url_view", projectName+path_start_web+ picName);
				
				picName = sd_rz_pic_url.substring(sd_rz_pic_url.lastIndexOf("/")+1);
				map.put("sd_rz_pic_url_view", projectName+path_auth_web+picName);
				
				String filePath = StringUtil.getStringValue(taskMap.get(i).get("file_path"));
				map.put("file_path_view", projectName+path_file_web+filePath.substring(filePath.lastIndexOf("/")+1));
			}
			catch (Exception e) {
				logger.error(e.getMessage());
			}
			logger.warn("sd_qd_pic_url_view="+map.get("sd_qd_pic_url_view"));
			logger.warn("sd_kj_pic_url_view="+map.get("sd_kj_pic_url_view"));
			logger.warn("sd_rz_pic_url_view="+map.get("sd_rz_pic_url_view"));
			logger.warn("file_path_view="+map.get("file_path_view"));
			
			listshow.add(map);
		}
		return listshow;
	}
	
	public static void main(String[] args)
	{
		String picviewIP = "http://11.11.11.11:1111";
		String sd_qd_pic_url = "http://172.0.10.179:8000/FileServer/STB/FILE/PIC/20180328112619_1.jpg";
		String sd_qd_pic_url_ori_ip = sd_qd_pic_url.substring(sd_qd_pic_url.indexOf("/",8));
		System.out.println(picviewIP + sd_qd_pic_url_ori_ip);
	}
	
	
	
	/**
	 * 删除
	 * @param status
	 * @param task_id
	 * @return
	 */
	public String doDelete(String taskId) {
		List<Map> list=dao.getFilepath(taskId);
		SqlUtil sqlUtil = new SqlUtil();
		int str=0;
		String delete="";
		if(list!=null&&list.size()>0)
		{
			delete=dao.doDelete(taskId);
			sqlUtil.delStrategy(taskId);
			if(delete.equals("1"))
			{
				str=dao.updatestatus1(taskId);	
				if(str>0)
				{
					delete="1";
				}else
				{
					delete="0";
				}
			}
		}else
		{
			str=dao.updatestatus1(taskId);
			if(str>0)
			{
				delete="1";
			}else
			{
				delete="0";
			}
		}
		return delete;
	}
	public String updatestatus(String status,String task_id){
		String str="";
		
		List<HashMap<String,String>> dataList = dao.getPicAcccounts(task_id);
		if(null != dataList && dataList.size() <= 5000){
		    Map<String, String> taskMap = dao.getTaskDetail(task_id);
		    String file_path = null;
		    //判断是否是导入定制
		    if(taskMap != null){
		    	file_path = StringUtil.getStringValue(taskMap, "file_path", null);
		    }
			
			if((Global.CQDX.equals(Global.instAreaShortName) ||
					Global.SXLT.equals(Global.instAreaShortName))
					&& null != dataList && !StringUtil.IsEmpty(file_path)){
				
				SqlUtil sqlUtil = new SqlUtil();
				if("2".equals(status)){
					Map<String, String> todoTaskMap = new HashMap<String, String>();
					List<StrategyOBJ> strategyObjs = null;
					todoTaskMap.put("sd_qd_pic_url", StringUtil.getStringValue(taskMap, "sd_qd_pic_url",""));
					todoTaskMap.put("sd_kj_pic_url", StringUtil.getStringValue(taskMap, "sd_kj_pic_url",""));
					todoTaskMap.put("sd_rz_pic_url", StringUtil.getStringValue(taskMap, "sd_rz_pic_url",""));
					strategyObjs = getStrategyOBJ(dataList, String.valueOf(task_id), logo2Xml(todoTaskMap));
					logger.warn("设备有定制的开机广告任务:[{}]，生成策略todoTaskMap:[{}]",task_id, todoTaskMap);
					//做的是导入文件的任务，需要更新明细表的状态为已做。
				    dao.updatePicAccountStatus(StringUtil.getLongValue(task_id), 1);
					
				    //重庆BUG 修改  开机广告的策略和日志在配置模块入表
					//sqlUtil.addStrategy(strategyObjs);
					
					String[] stragetyIds = new String[strategyObjs.size()];
					for (int i = 0; i < strategyObjs.size(); i++) {
						stragetyIds[i] = String.valueOf(strategyObjs.get(i).getId());
					}
					invokePreProcess(stragetyIds);
				}else if("1".equals(status)){
					sqlUtil.delStrategy(task_id);
				}
			}
		}
		
		int total=dao.updatestatus(status, task_id);
		if(total>0)
		{
			str="修改成功";
		}else
		{
			str="修改失败";
		}
		return str;
	}
	
	/**
	 * 开始调用预读模块,开机广告
	 * 
	 * @param invokeStruct
	 */
	private void invokePreProcess(String[] idArr) {
		try {
			CreateObjectFactory.createPreProcess(Global.GW_TYPE_STB).processOOBatch_stb(idArr,"10");
		} catch (Exception e) {
			AppInitBIO.initPreProcess(Global.GW_TYPE_STB);
			CreateObjectFactory.createPreProcess(Global.GW_TYPE_STB).processOOBatch_stb(idArr,"10");
		}
	}
	
	/**
	 * STB软件升级XML
	 * 
	 * @author gongsj
	 * @date 2010-11-8
	 * @param taskInfoMap
	 * @return
	 */
	private String logo2Xml(Map<String, String> taskInfoMap)
	{
		logger.debug("logo2Xml...");
		String strXml = null;
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("STB");
		
		Element StartPicURL = root.addElement("StartPicURL");
		Element BootPicURL = root.addElement("BootPicURL");
		Element AuthenticatePicURL = root.addElement("AuthenticatePicURL");
		
		if(!StringUtil.IsEmpty(taskInfoMap.get("sd_qd_pic_url"))){
			StartPicURL.addAttribute("flag", "1").addText(taskInfoMap.get("sd_qd_pic_url"));
		}else{
			StartPicURL.addAttribute("flag", "0");
		}
		
		if(!StringUtil.IsEmpty(taskInfoMap.get("sd_kj_pic_url"))){
			BootPicURL.addAttribute("flag", "1").addText(taskInfoMap.get("sd_kj_pic_url"));
		}else{
			BootPicURL.addAttribute("flag", "0");
		}
		
		if(!StringUtil.IsEmpty(taskInfoMap.get("sd_rz_pic_url"))){
			AuthenticatePicURL.addAttribute("flag", "1").addText(taskInfoMap.get("sd_rz_pic_url"));	
		}else{
			AuthenticatePicURL.addAttribute("flag", "0");	
		}
		
		strXml = doc.asXML();
		return strXml;
	}
	
	/**
	 * 获得策略对象
	 * 
	 * @author gongsj
	 * @date 2010-11-8
	 * @param deviceId
	 * @param sheetPara
	 * @return
	 */
	private List<StrategyOBJ> getStrategyOBJ(List<HashMap<String,String>> dataList, String taskId, String sheetPara)
	{
		String logo_con = LipossGlobals.getLipossProperty("batchImportAdv.logo_con");
		List<StrategyOBJ> list = new ArrayList<StrategyOBJ>();
		for (Map<String,String> map : dataList) {
			String device_id = StringUtil.getStringValue(map, "device_id");
			if(!StringUtil.IsEmpty(device_id)){
				StrategyOBJ strategyOBJ = new StrategyOBJ();
				strategyOBJ.setServiceId(StringUtil.getIntegerValue(logo_con));
				strategyOBJ.createId();
				strategyOBJ.setDeviceId(device_id);
				strategyOBJ.setTime(TimeUtil.getCurrentTime());
				strategyOBJ.setSheetPara(sheetPara);
				strategyOBJ.setAccOid(1);
				strategyOBJ.setOrderId(1);
				strategyOBJ.setIsLastOne(1);
				strategyOBJ.setType(4);
				// 是新类型的策略，策略参数为XML，组装模板
				strategyOBJ.setSheetType(2);
				strategyOBJ.setRedo(0);
				strategyOBJ.setTempId(StringUtil.getIntegerValue(logo_con));
				strategyOBJ.setTaskId(taskId);
				list.add(strategyOBJ);
			}
		}
		return list;
	}
	
	public String OpenDeviceShowPicConfig(long taskId,long add_time,long acc_oid,File bootFile, File startFile,
			File authFile, String bootFileName, String startFileName, String authFileName) {
		logger.warn("OpenDeviceShowPicConfig(booFile={},startFile={},authFile={},bootFileName={},startFileName={},authFileName={})",
	    		new Object[]{bootFile,startFile,authFile,bootFileName,startFileName,authFileName});
		 
		    String bootFilePath  = "" ;
		    String startFilePath = "" ;
		    String authFilePath  = "" ;
		    
			String upLoadServerIP   = "" ;    // FTP服务器的IP地址
			String upLoadServerUser = "" ;   // 上传服务器用户名
			String upLoadServerPwd  = "" ;    // 上传服务器密码
			
			String bootFilePath1 = "";
			String startFilePath1 = "";
			String authFilePath1 = "";
			//开机
			String starturl="";
			//启动
			String booturl="";
			//认证
			String authturl="";
			DateTimeUtil dt = new DateTimeUtil();
			String path_accountFile_web=LipossGlobals.getLipossProperty("attachmentsDir.accountFile");
			//开机路径
			String path_start_web=LipossGlobals.getLipossProperty("attachmentsDir.start");
			//启动路径
			String path_boot_web=LipossGlobals.getLipossProperty("attachmentsDir.boot");
			//认证路径
			String path_auth_web=LipossGlobals.getLipossProperty("attachmentsDir.auth");
			//需要ftp的其他web ip
			String ip_otherweb=LipossGlobals.getLipossProperty("attachmentsDir.otherweb");
			//需要ftp的其他web 的账号密码
			String name_ftp_otherweb=LipossGlobals.getLipossProperty("nodeftp.username");
			String pwd_ftp_otherweb=LipossGlobals.getLipossProperty("nodeftp.password");
			
			// 获取上传服务器，下载服务器相关参数
			List<Map<String, String>> serverMap = dao.getServerParameter();
			String url="";
			int ier=0;
			String timeString = dt.getYYYYMMDDHHMMSS();
			for(int i=0;i<serverMap.size();i++)
			{
				String ftpRemoteIp = LipossGlobals.getLipossProperty("attachmentsDir.ftpremoteip") ;
				// 图片服务器相关信息
				String server_url = serverMap.get(i).get("server_url");
				upLoadServerUser = serverMap.get(i).get("access_user");
				upLoadServerPwd = serverMap.get(i).get("access_passwd");
				if (null == server_url || "".equals(server_url) || 
						null == upLoadServerUser || "".equals(upLoadServerUser) || 
						null == upLoadServerPwd || "".equals(upLoadServerPwd)) {
						logger.warn("图片服务器维护不正确！");
						return "图片服务器维护不正确，请确认！";
					}
				String [] arrayStr = serverMap.get(i).get("server_url").split("/");
				
				upLoadServerIP = arrayStr[2].split(":")[0];
				if(StringUtil.IsEmpty(ftpRemoteIp)){
					ftpRemoteIp = upLoadServerIP;
				}
				port = arrayStr[2].split(":")[1];
				// 拼装图片服务器上的图片存放目录
				remoteAbsoluteUrl = "/"+arrayStr[3]+"/"+arrayStr[4]+"/"+arrayStr[5]+"/"+arrayStr[6];
				remoteAbsoluteIP = "http://"+upLoadServerIP;
				//启动图片
				if (null != bootFile && bootFile.isFile() && bootFile.length()>0) {
					logger.warn("bootFile");
					
					String result = checkPicture("bootFile", bootFile, bootFileName);
					
					if (result.charAt(0) == '0') {
						logger.warn("开机图片上传至WEB服务器成功");
						bootFilePath = result.substring(2);
						bootFilePath1 = bootFilePath.split("/")[3];
					}else if(result.charAt(0) == '1'){
						logger.warn("开机图片不合格，任务定制失败");
						return "开机图片不合格，任务定制失败";
					}else if(result.charAt(0) == '2'){
						logger.warn("开机图片上传失败，任务定制失败");
						return "开机图片上传失败，任务定制失败";
					}else if(result.charAt(0) == '3'){
						logger.warn("任务定制失败");
						return "任务定制失败";
					}
					
					// 将刚刚上传至WEB服务器上的图片FTP到图片服务器
					String upLoadResult = picUploadToFTPServer(ftpRemoteIp, upLoadServerUser, upLoadServerPwd, bootFilePath);
					
					if ("1".equals(upLoadResult)) {
						logger.warn("将开机图片FTP至图片服务器失败，任务定制失败");
						return "将开机图片FTP至图片服务器失败，任务定制失败";
					}
					if(Global.CQDX.equals(Global.instAreaShortName) ||
							Global.SXLT.equals(Global.instAreaShortName)){
					if(i==0){
						String newFileName = bootFileName.substring(bootFileName.lastIndexOf("/")+1);
						String ftpWEBResult = picUploadToOtherWEB(ip_otherweb, name_ftp_otherweb, pwd_ftp_otherweb, path_boot_web+newFileName);
						if ("1".equals(ftpWEBResult)) {
							logger.warn("将开机图片FTP至web服务器失败，可能会影响审核时预览图片.");
						}
					}
					}
					//booturl="/"+arrayStr[3]+"/"+arrayStr[4]+"/"+arrayStr[5]+"/"+arrayStr[6]+"/"+bootFileName+"_"+dt.getYYYYMMDDHHMMSS();
				}
				if (null != startFile && startFile.isFile() && startFile.length()>0) {
					logger.warn("startFile");
					
					String result = checkPicture("startFile", startFile, startFileName);
					
					if (result.charAt(0) == '0') {
						logger.warn("启动图片上传至WEB服务器成功");
						startFilePath = result.substring(2);
						startFilePath1 = startFilePath.split("/")[3];
					}else if(result.charAt(0) == '1'){
						logger.warn("启动图片不合格，任务定制失败");
						return "启动图片不合格，任务定制失败";
					}else if(result.charAt(0) == '2'){
						logger.warn("启动图片上传失败，任务定制失败");
						return "启动图片上传失败，任务定制失败";
					}else if(result.charAt(0) == '3'){
						logger.warn("任务定制失败");
						return "任务定制失败";
					}
					
					// 将刚刚上传至WEB服务器上的图片FTP到图片服务器
					String upLoadResult = picUploadToFTPServer(ftpRemoteIp, upLoadServerUser, upLoadServerPwd, startFilePath);
					
					if ("1".equals(upLoadResult)) {
						logger.warn("将启动图片FTP至图片服务器失败，任务定制失败");
						return "将启动图片FTP至图片服务器失败，任务定制失败";
					}
					
					if(Global.CQDX.equals(Global.instAreaShortName) ||
							Global.SXLT.equals(Global.instAreaShortName)){
						
					if(i==0){
						String newFileName = startFileName.substring(startFileName.lastIndexOf("/")+1);
						String ftpWEBResult = picUploadToOtherWEB(ip_otherweb, name_ftp_otherweb, pwd_ftp_otherweb, path_start_web+newFileName);
						if ("1".equals(ftpWEBResult)) {
							logger.warn("将开机图片FTP至web服务器失败，可能会影响审核时预览图片.");
						}
					}
					}
					//starturl="/"+arrayStr[3]+"/"+arrayStr[4]+"/"+arrayStr[5]+"/"+arrayStr[6]+"/"+startFileName+"_"+dt.getYYYYMMDDHHMMSS();

				}
				
				//认证如图片上传
				if (null != authFile && authFile.isFile() && authFile.length()>0) {
					
					logger.warn("authFile");
					String result = checkPicture("authFile", authFile, authFileName);
					
					if (result.charAt(0) == '0') {
						logger.warn("认证图片上传至WEB服务器成功");
						authFilePath = result.substring(2);
						authFilePath1 = authFilePath.split("/")[3];
					}else if(result.charAt(0) == '1'){
						logger.warn("认证图片不合格，任务定制失败");
						return "认证图片不合格，任务定制失败";
					}else if(result.charAt(0) == '2'){
						logger.warn("认证图片上传失败，任务定制失败");
						return "认证图片上传失败，任务定制失败";
					}else if(result.charAt(0) == '3'){
						logger.warn("任务定制失败");
						return "任务定制失败";
					}
					
					// 将刚刚上传至WEB服务器上的图片FTP到图片服务器
					String upLoadResult = picUploadToFTPServer(ftpRemoteIp, upLoadServerUser, upLoadServerPwd, authFilePath);
					if ("1".equals(upLoadResult)) {
						logger.warn("将标清启动图片FTP至图片服务器失败，任务定制失败");
						return "将标清启动图片FTP至图片服务器失败，任务定制失败";
					}
					if(Global.CQDX.equals(Global.instAreaShortName) ||
							Global.SXLT.equals(Global.instAreaShortName)){
					if(i==0){
						String newFileName = authFileName.substring(authFileName.lastIndexOf("/")+1);
						String ftpWEBResult = picUploadToOtherWEB(ip_otherweb, name_ftp_otherweb, pwd_ftp_otherweb, path_auth_web+newFileName);
						if ("1".equals(ftpWEBResult)) {
							logger.warn("将开机图片FTP至web服务器失败，可能会影响审核时预览图片.");
						}
					}
					}
				}
				
			}
			
			ier = dao.OpenDeviceShowPicConfig(taskId,  add_time);
			if (ier > 0)
			{
				return "任务定制成功!";
			}
			else
			{
				return "任务定制失败！";
			}
	}
	
	
	/**
	 * 检查将要上传的图片是否合格， 将合格的图片上传至WEB服务器<br>
	 * 
	 * @return
	 * 0 表示上传图片至WEB服务器成功<br>
	 * 1 表示上传的图片属性不合格
	 * 2 表示上传图片至WEB服务器失败<br>
	 * 3 表示分析图片分辨率及大小时异常<br>
	 */
	public String checkPicture(String picAttribute, File sourceFile, String fileName) {
		
		logger.debug("checkPicture()");
		try {
			
			// 将符合条件的图片上传至WEB服务器
			String retrunMsg = upLoadPicture(picAttribute, fileName, sourceFile);
			if (retrunMsg.startsWith("1")) {
				return "2;上传图片失败!";
			}else {
				return retrunMsg;
			}
		} catch (Exception e) {
			return "3;分析图片属性时异常，任务定制失败！";
		}
	}
	
	
	/**
	 * 将合格的图片上传至服务器指定的目录
	 * 
	 * @param pictureFilePath
	 * @param picAttribute
	 * @return
	 */
	public String upLoadPicture(String picAttribute, String fileName, File sourceFile) {
		
		logger.warn("upLoadPicture(picAttribute={},fileName={})",picAttribute,fileName);
		
		String returnMsg = "";   // 回参
		
		String newFileUrl = "";  // 图片在WEB服务器上的URL
		
		try {
			// 新文件名
			String newFileName = fileName.substring(fileName.lastIndexOf("/")+1);
			
	        // 将符合条件的图片上传至WEB服务器指定的目录,然后再从WEB服务器FTP到指定文件服务器的指定目录
	        // LipossGlobals.getLipossHome() : /export/home/data2/web/lims
	        
	        // 获取WEB服务器上的图片文件路径，此路径将会被存到数据库中，用于图片的展示
	        if("bootFile".equals(picAttribute)){
	        	newFileUrl = LipossGlobals.getLipossProperty("attachmentsDir.boot");
	        }else if("startFile".equals(picAttribute)){
	        	newFileUrl = LipossGlobals.getLipossProperty("attachmentsDir.start");
	        }else if("authFile".equals(picAttribute)){
	        	newFileUrl = LipossGlobals.getLipossProperty("attachmentsDir.auth");
	        }
	        
	        File target = new File(LipossGlobals.getLipossHome()+newFileUrl, newFileName);
	        FileUtils.copyFile(sourceFile, target);  // 实现文件的复制功能
	        
	        returnMsg = "0;"+newFileUrl+newFileName;
	        
		} catch (Exception e) {
			logger.warn("上传图片失败!");
			logger.error("上传图片出错，msg:({})", e.getMessage());
			returnMsg = "1;上传图片出错";
		}
		
		return returnMsg;
	}
	
	/**
	 * 将WEB服务器上的图片FTP至图片服务器，如果FTP失败，则定制任务失败，需要重新定制<br>
	 * 
	 * @param ip
	 * @param user
	 * @param pwd
	 * @param localFileUrl
	 * @return
	 * 0 表示上传成功<br>
	 * 1 表示上传失败<br>
	 */
	public String picUploadToFTPServer(String ip, String user, String pwd, String localFileUrl){
		
		logger.debug("==>picUploadToFTPServer()");
		
		String ftpPath = LipossGlobals.getLipossProperty("attachmentsDir.ftp");
		String localAbsoluteFile = LipossGlobals.getLipossHome()+localFileUrl;
		String fileName = localFileUrl.split("/")[3];
		String remoteAbsoluteFile = ftpPath+remoteAbsoluteUrl +"/" +fileName;
		
		
		try {
			
			boolean result = false;
			
			FtpClient ftpClient = new FtpClient(ip, user, pwd);
			boolean connectResult = ftpClient.connect();  // 连接FTP服务器
			
			if(connectResult){
				/** remoteAbsoluteFile 远程路径   localAbsoluteFile 本地路劲*/
				logger.warn(localFileUrl+"从"+localAbsoluteFile+"ftp至"+ip+"的"+remoteAbsoluteFile);
				result = ftpClient.put(remoteAbsoluteFile, localAbsoluteFile, false);
			}
			
			if (!result) {
				logger.warn("文件\""+localFileUrl+"\"FTP至图片服务器失败！");
				return "1";
			}else {
				logger.warn("文件\""+localFileUrl+"\"FTP至图片服务器成功！");
				return "0";
			}
		} catch (Exception e) {
			logger.warn("FTP图片文件时异常，msg=({})", e.getMessage());
			return "1";
		}
	}
	
	
	
	/**
	 * 将WEB服务器上的图片FTP至另一个web，若失败，不影响定制，审核预览图片可能会失败(有一半概率)<br>
	 * 
	 * @param ip
	 * @param user
	 * @param pwd
	 * @param localFileUrl
	 * @return
	 * 0 表示上传成功<br>
	 * 1 表示上传失败<br>
	 */
	public String picUploadToOtherWEB(String ip, String user, String pwd, String localFileUrl){
		logger.debug("==>picExcelUploadToOtherWEB()");
		String localAbsoluteFile = LipossGlobals.getLipossHome()+localFileUrl;
		String remoteAbsoluteFile = localAbsoluteFile;
		
		
		try {
			
			boolean result = false;
			FtpClient ftpClient = new FtpClient(ip, user, pwd);
			boolean connectResult = ftpClient.connect();  // 连接ftp服务器
			logger.warn("connectResult="+connectResult);
			if(connectResult){
				/** remoteAbsoluteFile 远程路径   localAbsoluteFile 本地路劲*/
				logger.warn("将"+localFileUrl+"从本地"+localAbsoluteFile+"上传至"+ip+"的"+remoteAbsoluteFile);
				result = ftpClient.put(remoteAbsoluteFile, localAbsoluteFile, false);
			}
			
			if (!result) {
				logger.warn("文件\""+localFileUrl+"\"FTP至另一个web服务器失败！");
				return "1";
			}else {
				logger.warn("文件\""+localFileUrl+"\"FTP至另一个web服务器成功！");
				return "0";
			}
		} catch (Exception e) {
			logger.warn("FTP至另一个web服务器时异常，msg=({})", e.getMessage());
			return "1";
		}
	}
	
	
	public void recordOperLog(String operaName, User user, String ip, String hosts){
		dao.recordOperLog(user.getAccount(), ip, hosts, user.getId(), "", operaName);
	}
	
	public OpenDeviceShowPictureNewDAO getDao()
	{
		return dao;
	}
	
	public void setDao(OpenDeviceShowPictureNewDAO dao)
	{
		this.dao = dao;
	}
	
	
	
}
