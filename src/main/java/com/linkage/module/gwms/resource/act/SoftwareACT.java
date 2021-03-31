
package com.linkage.module.gwms.resource.act;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.struts2.interceptor.SessionAware;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.resource.bio.SoftwareBIO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.share.bio.GwDeviceQueryBIO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.SoftUpgradeCorba;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 软件升级
 * 
 * @author 王森博
 */
public class SoftwareACT extends ActionSupport implements SessionAware
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(SoftwareACT.class);
	private String deviceIds;
	private String ajax;
	private SoftwareBIO bio;
	// session
	private Map session;
	private String softStrategyHTML;
	private String goal_devicetype_id;
	private String softStrategy_type;
	private String param;
	
	private String strRename;
	private String urlParameter;
	private File file1;
	private String response;
	private String path;
	private String gw_type;
	private GwDeviceQueryBIO gwDeviceQueryBio;
	private String fileName;
	private String importQueryField;
	private String gwShare_queryType;
	private String gwShare_queryType_this;
	private String starttime;
	private String endtime;
	
	private String taskId;
	private String mode;
	private List taskList ;
	private String maxActive;
	private String taskName;
	private String type;
	private String expire_time_start = "";
	private String expire_time_end = "";
	private String expire_date_start = "";
	private String expire_date_end = "";
	
	private String gwShare_queryTypeHidden;
    private String gwShare_queryFieldHidden = "";
	private String gwShare_queryParamHidden = "";
	
	private String gwShare_cityIdHidden = "";
	private String gwShare_onlineStatusHidden = "";
    private String gwShare_vendorIdHidden = "";
    private String gwShare_deviceModelIdHidden = "";
    private String gwShare_devicetypeIdHidden = "";
    private String gwShare_bindTypeHidden = "";
    private String gwShare_deviceSerialnumberHidden = "";
    
    private String gwShare_fileNameHidden = "";

    private String task_desc="";

    private String AHLT_softUp="";
    
    private Map<String,String> taskResultMap;

	public String init4CQ()
	{
		maxActive = LipossGlobals.getLipossProperty("cqBatchSoftMaxActive");
		softStrategyHTML = bio.getStrategyCQList("softStrategy_type");
		return "batch4cq";
	}
	
	public String init4SXLT()
	{
		maxActive = LipossGlobals.getLipossProperty("cqBatchSoftMaxActive");
		softStrategyHTML = bio.getStrategySXLTList("softStrategy_type");
		DateTimeUtil dt = new DateTimeUtil();
		this.expire_time_start = "00:00:00";
		this.expire_time_end = "23:59:59";
		expire_date_start = dt.getYYYY_MM() + "-01";
		expire_date_end = dt.getYYYY_MM_DD();
		return "batch4sxlt";
	}

	public String init()
	{
		softStrategyHTML = bio.getStrategyList("softStrategy_type", new String[] { "4",
				"5" });
		return "success";
	}
	
	//用于测试云南是否支持立即下发
	public String init4Yn()
	{
		softStrategyHTML = bio.getStrategyList("softStrategy_type", new String[] { "0",
				"5" });
		return "success";
	}
	
	public String init4Ah()
	{
		softStrategyHTML = bio.getStrategyList("softStrategy_type", new String[] { "4",
				"5" });
		return "batch4Ah";
	}

	public String init4AHLT()
	{
		softStrategyHTML = bio.getStrategyList("softStrategy_type", new String[] { "4",
				"5" });
		return "batch4AHLT";
	}

	/**
	 * add by zhangsb 2013年6月5日 15:45:43  
	 * @return
	 */
	public String initAh()
	{
		softStrategyHTML = bio.getStrategyList("softStrategy_type", 
				new String[] { "4","5" });
		return "batchAh";
	}
	/**
	 * 设备批量升级
	 * 
	 * @author wangsenbo
	 * @date 2009-12-08
	 * @return String
	 */
	public String batchUp()
	{
		logger.debug("batchUp()");
		logger.warn("!!!!!!!!!!gwShare_queryType="+gwShare_queryType);
		if(Global.NXDX.equals(Global.instAreaShortName) || 
				Global.YNLT.equals(Global.instAreaShortName)){
			try
			{
				UserRes curUser = (UserRes) session.get("curUser");
				taskId = insertTask("1",curUser.getUser().getId(),curUser.getUser().getAccount(),gw_type);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}


		if ("4".equals(gwShare_queryType))
		{
			//被动升级 文件导入模式。
			softUp();
		}
		try
		{
			UserRes curUser = (UserRes) session.get("curUser");
			String isOpenSoftUpModule = LipossGlobals.getLipossProperty("isOpenSoftUpModule");
			if (true == StringUtil.IsEmpty(deviceIds))
			{
				logger.debug("任务中没有设备");
			}
			//直接入策略方式
			else if("0".equals(isOpenSoftUpModule))
			{
				logger.warn("批量软件升级：直接入策略方式");
				long accoid = curUser.getUser().getId();
				String[] deviceId_array = null;
				if (!"0".equals(deviceIds))
				{
					deviceId_array = deviceIds.split(",");
				}
				else
				{
					List list = gwDeviceQueryBio.getDeviceList(gw_type,curUser.getAreaId(), param);
					deviceId_array = new String[list.size()];
					for (int i = 0; i < list.size(); i++)
					{
						Map map = (Map) list.get(i);
						deviceId_array[i] = StringUtil.getStringValue(map.get("device_id"));
					}
				}
				
				if(LipossGlobals.inArea(Global.YNLT)){
					//bio.batchUp(accoid, deviceId_array, softStrategy_type,taskId);
				}else{
					bio.batchUp(accoid, deviceId_array, softStrategy_type);
				}
			}
			else if("2".equals(isOpenSoftUpModule))
			{
				batchUp4fen(param, curUser.getAreaId(), gw_type);
			}
			else
			{
				if (Global.AHDX.equals(Global.instAreaShortName)
						||Global.JXDX.equals(Global.instAreaShortName))
				{
					batchUp4SoftUpModule(param, curUser.getAreaId(), gw_type,goal_devicetype_id);
				}
				else 
				{
					batchUp4SoftUpModule(param, curUser.getAreaId(), gw_type);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception="+e.getMessage());
			return "result";
		}
		return "result";
	}
	
	
	//（重庆）主动触发批量升级(拆分配置模块)
	public String batchUpActive()
	{
		logger.debug("batchUpActive()");
		logger.warn("taskId [{}] , batchUpActive!!!!!!!!!!gwShare_queryType_this="+gwShare_queryType_this,taskId);

		/*if("ah_lt".equals(Global.instAreaShortName) && "1".equals(AHLT_softUp) && "3".equals(gwShare_queryType))
		{
			// 安徽联通 界面  由于前台界面使用的是3  原则上应该是4 但是由于已经在生产环境上线了3 修改为4之后，多了批量升级台数限制2000台
			// 为避免用户有意见  故在后台将 安徽联通 批量软件升级前台传递的3 修改为4
			// gwShare_queryType = 3  导入查询
			// gwShare_queryType = 4  升级导入查询
			gwShare_queryType = "4";
		}
		*/

		if("4".equals(gwShare_queryType_this) && Global.AHLT.equals(Global.instAreaShortName)){
			//安徽联通导入方式处理 这里直接获取设备信息写入任务设备明细表 AHLT_RMS-REQ-20200715-DXL-001
			return softUpActive4AHLT();
		}else if("4".equals(gwShare_queryType_this)){
			softUpActive();
		}

		try
		{
			UserRes curUser = (UserRes) session.get("curUser");
			String isOpenSoftUpModule = LipossGlobals.getLipossProperty("isOpenSoftUpModule");
			if (StringUtil.IsEmpty(deviceIds))
			{
				logger.debug("任务中没有设备");
			}
			else if("2".equals(isOpenSoftUpModule))
			{
				if(Global.AHLT.equals(Global.instAreaShortName)){
					//安徽联通单独处理 这里直接获取设备信息写入任务设备明细表 AHLT_RMS-REQ-20200715-DXL-001
					batchUp4fenActive4AHLT(param, curUser.getAreaId(), gw_type);
				}else {
					batchUp4fenActive(param, curUser.getAreaId(), gw_type);
				}
			}
			else{
				logger.warn("当前模块部署不是拆分配置模块形式！");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("Exception="+e.getMessage());
			return "result";
		}
		return "result";
	}
	
	
	
	/**
	 * 设备批量升级 重庆
	 * 
	 * @author fanjm
	 * @date 2018-5-02
	 * @return String
	 */
	public String batchUp4cq()
	{
		logger.debug("execute()");
		logger.warn("batchUp4cq!!!!!!!!!!mode="+mode+",gwShare_queryType_this="+gwShare_queryType_this+",taskName:"+taskName);
		softStrategy_type = "5";
		gwShare_queryType = gwShare_queryType_this;
		
		UserRes curUser = (UserRes) session.get("curUser");
		taskId = insertTask(mode,curUser.getUser().getId(),taskName,gw_type);
		
		//被动模式与原来一样
		if("1".equals(mode)){
			batchUp();
		}
		//主动的逻辑参考单台软件升级
		else{
			batchUpActive();
		}
		
		return "result";
	}
	
	public String batchUp4sxlt()
	{
		logger.debug("execute()");
		logger.warn("batchUp4sxlt,mode="+mode+",gwShare_queryType_this="+gwShare_queryType_this+",taskName:"+taskName);
		gwShare_queryType = gwShare_queryType_this;
		
		UserRes curUser = (UserRes) session.get("curUser");
		softStrategy_type = type;
		
		taskId = insertTaskNew(mode,curUser.getUser().getId(),taskName,gw_type,softStrategy_type,expire_date_start+" "+expire_time_start,expire_date_end+" "+expire_time_end
				,gwShare_queryTypeHidden,gwShare_queryFieldHidden,gwShare_queryParamHidden,gwShare_cityIdHidden,gwShare_onlineStatusHidden,gwShare_vendorIdHidden,gwShare_deviceModelIdHidden
				,gwShare_devicetypeIdHidden,gwShare_bindTypeHidden,gwShare_deviceSerialnumberHidden,gwShare_fileNameHidden);
		
		//被动模式与原来一样
		if("1".equals(mode)){
			batchUp();
		}
		//主动的逻辑参考单台软件升级
		else{
			batchUpActive();
		}
		
		return "result";
	}

	public String batchUp4ahlt()
	{
		logger.debug("execute()");
		logger.warn("batchUp4ahlt,mode="+mode+",gwShare_queryType_this="+gwShare_queryType_this+",taskName:"+taskName);
		gwShare_queryType = gwShare_queryType_this;

		UserRes curUser = (UserRes) session.get("curUser");
		softStrategy_type = type;

		taskId = insertTaskNew(mode,curUser.getUser().getId(),taskName,gw_type,softStrategy_type,expire_date_start+" "+expire_time_start,expire_date_end+" "+expire_time_end
				,gwShare_queryTypeHidden,gwShare_queryFieldHidden,gwShare_queryParamHidden,gwShare_cityIdHidden,gwShare_onlineStatusHidden,gwShare_vendorIdHidden,gwShare_deviceModelIdHidden
				,gwShare_devicetypeIdHidden,gwShare_bindTypeHidden,gwShare_deviceSerialnumberHidden,gwShare_fileNameHidden);

		//被动模式与原来一样
		if("1".equals(mode)){
			logger.warn("taskID [{}] 选择被动模式。",taskId);
			batchUp();
		}
		//主动的逻辑参考单台软件升级
		else{
			logger.warn("taskID [{}] 选择主动模式。",taskId);
			batchUpActive();
		}

		return "result";
	}
	
	/**
	 * 入任务表
	 * @param mode 模式
	 * @param id 定制人id
	 */
	private String insertTask(String mode, long id,String name, String gw_type,String softStrategy_type)
	{
		long time = new DateTimeUtil().getLongTime();
		String task_id = StringUtil.getStringValue(id)+StringUtil.getStringValue(time*3);
		//String task_name = name+StringUtil.getStringValue(time);
		bio.insertTask(time,task_id,name,mode,StringUtil.getStringValue(id),gw_type,softStrategy_type);
		return task_id;
	}
	
	/**
	 * 带各种定制的参数
	 * @param mode
	 * @param id
	 * @param name
	 * @param gw_type
	 * @param softStrategy_type
	 * @param gwShare_fileName2 
	 * @param gwShare_deviceSerialnumber2 
	 * @param gwShare_bindType2 
	 * @param gwShare_devicetypeId2 
	 * @param gwShare_deviceModelId2 
	 * @param gwShare_vendorId2 
	 * @param gwShare_onlineStatus2 
	 * @param gwShare_cityId2 
	 * @param gwShare_queryParam2 
	 * @param gwShare_queryField2 
	 * @param gwShare_queryType2 
	 * @param endTime 
	 * @param startTime 
	 * @return
	 */
	private String insertTaskNew(String mode, long id,String name, String gw_type,String softStrategy_type, String startTime, String endTime,
			String gwShare_queryType, String gwShare_queryField, String gwShare_queryParam, String gwShare_cityId, String gwShare_onlineStatus, 
			String gwShare_vendorId, String gwShare_deviceModelId, String gwShare_devicetypeId, String gwShare_bindType, String gwShare_deviceSerialnumber, String gwShare_fileName)
	{
		long time = new DateTimeUtil().getLongTime();
		String task_id = StringUtil.getStringValue(id)+StringUtil.getStringValue(time*3);
		//String task_name = name+StringUtil.getStringValue(time);
		bio.insertTaskNew(time,task_id,name,mode,StringUtil.getStringValue(id),gw_type,softStrategy_type,startTime,endTime,
				gwShare_queryType,gwShare_queryField,gwShare_queryParam,gwShare_cityId,gwShare_onlineStatus,gwShare_vendorId,
				gwShare_deviceModelId,gwShare_devicetypeId,gwShare_bindType,gwShare_deviceSerialnumber,gwShare_fileName);
		return task_id;
	}
	
	/**
	 * 升级任务表，带触发事件
	 * @param mode
	 * @param id
	 * @param name
	 * @param gw_type
	 * @return
	 */
	private String insertTask(String mode, long id,String name, String gw_type)
	{
		long time = new DateTimeUtil().getLongTime();
		String task_id = StringUtil.getStringValue(id)+StringUtil.getStringValue(time*3);
		//String task_name = name+StringUtil.getStringValue(time);
		bio.insertTask(time,task_id,name,mode,StringUtil.getStringValue(id),gw_type);
		return task_id;
	}

	public void batchUp4SoftUpModule(String param,long areaId,String gw_type)
	{
		logger.warn("batchUp4SoftUpModule({},{},{})", new Object[] {param,areaId,gw_type});
		try
		{
			SoftUpgradeCorba softUpgradeCorba = new SoftUpgradeCorba(gw_type);
			if(!"0".equals(deviceIds))
			{
				String[] deviceId_array = deviceIds.split(",");
				String [] paramArr;
					paramArr = new String[deviceId_array.length];
					for (int i = 0; i < deviceId_array.length; i++)
					{
						paramArr[i] = bio.getDeviceTypeId(deviceId_array[i]);
					}
					logger.warn("deviceIds={},调用后台软件升级corba接口",deviceIds);
				softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr);
			}
			//调用后台corba接口时，小于100的情况，传deviceID数组，否则传SQL
			else 
			{
				if(StringUtil.IsEmpty(param))
				{
					logger.warn("param为空");
				}
				String[] _param = param.split("\\|");
				String matchSQL = _param[10];
				long total = StringUtil.getLongValue(_param[11]);
				if(total < 100)
				{
					logger.warn("调用后台软件升级corba接口：数量小于100，传deviceID数组");
					List list = gwDeviceQueryBio.getDeviceList(gw_type,areaId, param);
					String[] deviceId_array = new String[list.size()];
					String [] paramArr = new String[list.size()];
					for (int i = 0; i < list.size(); i++)
					{
						Map map = (Map) list.get(i);
						deviceId_array[i] = StringUtil.getStringValue(map.get("device_id"));
						paramArr[i] = StringUtil.getStringValue(map.get("devicetype_id"));
					}
					softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr);
				}
				else
				{
					logger.warn("调用后台软件升级corba接口：数量大于100，传sql数组");
						softUpgradeCorba.processDeviceStrategy(new String[] { matchSQL
								.replace("[", "\'") }, "5", new String[] {});
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception="+e.getMessage());
		}
		
	}
	public void batchUp4fen(String param,long areaId,String gw_type)
	{
		logger.warn("支持分配置模块的批量软件升级batchUp4fen({},{},{})", new Object[] {param,areaId,gw_type});
		try
		{
			PreProcessInterface softUpgradeCorba = CreateObjectFactory.createPreProcess(gw_type);
			if(!"0".equals(deviceIds))
			{
				String[] deviceId_array = deviceIds.split(",");
				if(Global.AHLT.equals(Global.instAreaShortName))
				{
					String[] deviceId_array0 = deviceIds.split(",");
					int deviceIdLength = deviceId_array0.length;
					if(!StringUtil.IsEmpty(task_desc) && Integer.parseInt(task_desc) < deviceIdLength){
						deviceIdLength = Integer.parseInt(task_desc);
					}
					deviceId_array = new String[deviceIdLength];
					System.arraycopy(deviceId_array0, 0, deviceId_array, 0, deviceIdLength);
				}
				
				String[] paramArr = new String[deviceId_array.length];
				for (int i = 0; i < deviceId_array.length; i++)
				{
					paramArr[i] = bio.getDeviceTypeId(deviceId_array[i]);
				}
				logger.warn("deviceIds={},调用后台软件升级corba接口",deviceIds);

				if(Global.NXDX.equals(Global.instAreaShortName)
						||Global.CQDX.equals(Global.instAreaShortName)
						||Global.SXLT.equals(Global.instAreaShortName)
						||Global.AHLT.equals(Global.instAreaShortName))
				{
					softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr, taskId);
				}
				else 
				{
					softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr);
				}
			}
			//调用后台corba接口时，小于100的情况，传deviceID数组，否则传SQL
			//宁夏bug 修改， 宁夏修改传sql的条件为超过50台，不超过50台的时候，传deviceID数组
			//
			else 
			{
				if(StringUtil.IsEmpty(param))
				{
					logger.warn("param为空");
				}
				String[] _param = param.split("\\|");
				String matchSQL = _param[10];
				long total = StringUtil.getLongValue(_param[11]);
				if(Global.NXDX.equals(Global.instAreaShortName))
				{
					if(total <= 50)
					{
						logger.warn("调用后台软件升级corba接口：数量不超过50台，传deviceID数组");
						List list = gwDeviceQueryBio.getDeviceList(gw_type,areaId, param);
						String[] deviceId_array = new String[list.size()];
						String [] paramArr = new String[list.size()];
						for (int i = 0; i < list.size(); i++)
						{
							Map map = (Map) list.get(i);
							deviceId_array[i] = StringUtil.getStringValue(map.get("device_id"));
							paramArr[i] = StringUtil.getStringValue(map.get("devicetype_id"));
						}
						softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr, taskId);
					}
					else 
					{
						logger.warn("调用后台软件升级corba接口：数量大于50，传sql数组");
						softUpgradeCorba.processDeviceStrategy(new String[] { matchSQL
								.replace("[", "\'") }, "5", new String[] {"softup"}, taskId);
					}
				}
				else 
				{
					//以页面选择的数量为准
					int arrayLength = 0;
					if(!StringUtil.IsEmpty(task_desc)){
						arrayLength = Integer.parseInt(task_desc);
					}

					if(total < 100)
					{
						List list = gwDeviceQueryBio.getDeviceList(gw_type,areaId, param);
						if(list.size()<=arrayLength){
							arrayLength = list.size();
						}
						logger.warn("调用后台软件升级corba接口：数量小于100，传deviceID数组");
						String[] deviceId_array = new String[list.size()];
						String [] paramArr = new String[list.size()];
						for (int i = 0; i < arrayLength; i++)
						{
							Map map = (Map) list.get(i);
							deviceId_array[i] = StringUtil.getStringValue(map.get("device_id"));
							paramArr[i] = StringUtil.getStringValue(map.get("devicetype_id"));
						}
						if(Global.NXDX.equals(Global.instAreaShortName)
								|| Global.CQDX.equals(Global.instAreaShortName)
								|| Global.SXLT.equals(Global.instAreaShortName)
								||Global.AHLT.equals(Global.instAreaShortName))
						{
							softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr, taskId);
						}
						else 
						{
							softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr);
						}
					}
					else
					{
						logger.warn("调用后台软件升级corba接口：数量大于100，传sql数组");
						if(Global.NXDX.equals(Global.instAreaShortName)
								|| Global.CQDX.equals(Global.instAreaShortName)
								|| Global.SXLT.equals(Global.instAreaShortName)
								||Global.AHLT.equals(Global.instAreaShortName))
						{
							if(arrayLength != 0){
								if(DBUtil.GetDB()==Global.DB_MYSQL){
									//TODO wait
									matchSQL = "select a.device_id,a.device_group,a.oui,a.device_serialnumber," 
											+ "a.device_name,a.manage_staff,a.city_id,a.office_id,a.zone_id,"
											+ "a.device_addr,a.complete_time,a.buy_time,a.service_year,a.staff_id,"
											+ "a.remark,a.loopback_ip,a.pdevice_id,a.interface_id,a.device_status,"
											+ "a.device_id_ex,a.res_pro_id,a.gather_id,a.oper_status,a.devicetype_id,"
											+ "a.maxenvelopes,a.retrycount,a.cr_port,a.cr_path,a.cpe_mac,"
											+ "a.cpe_currentupdatetime,a.cpe_allocatedstatus,a.cpe_currentstatus,"
											+ "a.cpe_operationinfo,a.cpe_username,a.cpe_passwd,a.acs_username,"
											+ "a.acs_passwd,a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,"
											+ "a.device_model_id,a.snmp_udp,a.customer_id,a.device_url,"
											+ "a.resource_type_id,a.os_version,a.x_com_passwd_old,"
											+ "a.vendor_id,a.dev_sub_sn,a.device_owner,"
											+ " from ("+matchSQL.replace("[", "\'")+") where limit "+arrayLength;
								}else{
									matchSQL = "select * from ("+matchSQL.replace("[", "\'")+") where rownum<="+arrayLength;
								}
								
							}
							softUpgradeCorba.processDeviceStrategy(new String[] { matchSQL
								.replace("[", "\'") }, "5", new String[] {"softup"}, taskId);
						}
						else 
						{
							softUpgradeCorba.processDeviceStrategy(new String[] { matchSQL
								.replace("[", "\'") }, "5", new String[] {"softup"});
						}	
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception="+e.getMessage());
		}
		
	}

	
	
	public void batchUp4fenActive(String param,long areaId,String gw_type)
	{
		logger.warn("batchUp4fenActive支持分配置模块的批量软件升级batchUp4fen({},{},{})", new Object[] {param,areaId,gw_type});
		try
		{
			PreProcessInterface softUpgradeCorba = CreateObjectFactory.createPreProcess(gw_type);
			if(!"0".equals(deviceIds))
			{
				String[] deviceId_array0 = deviceIds.split(",");
				String[] deviceId_array = new String[deviceId_array0.length+1];
				deviceId_array[0] = "softupTime";
				int arrayLength = 0;
				if(!StringUtil.IsEmpty(task_desc)){
					arrayLength = Integer.parseInt(task_desc);
				}else{
					arrayLength = deviceId_array0.length;
				}
				System.arraycopy(deviceId_array0, 0, deviceId_array, 1, arrayLength);
				String[] paramArr;
				paramArr = new String[deviceId_array.length];
				paramArr[0] = "softupTime";
				for (int i = 1; i < deviceId_array.length; i++)
				{
					paramArr[i] = bio.getDeviceTypeId(deviceId_array[i]);
				}
				
				logger.warn("deviceIds={},调用后台软件升级corba/kafka，deviceId_array="+deviceId_array.length+",paramArr="+paramArr.length,deviceIds);
				softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr, taskId);
			}
			//调用后台corba接口时，小于100的情况，传deviceID数组，否则传SQL
			else 
			{
				if(StringUtil.IsEmpty(param))
				{
					logger.warn("param为空");
				}
				String[] _param = param.split("\\|");
				String matchSQL = _param[10];
				long total = StringUtil.getLongValue(_param[11]);
				int arrayLength = 0;
				if(!StringUtil.IsEmpty(task_desc)){
					arrayLength = Integer.parseInt(task_desc);
				}
				if(total < 100)
				{

					List list = gwDeviceQueryBio.getDeviceList(gw_type,areaId, param);
					if(list.size()<=arrayLength){
						arrayLength = list.size();
					}
					String[] deviceId_array = new String[list.size()+1];
					String [] paramArr = new String[list.size()+1];
					deviceId_array[0] = "softupTime";
					paramArr[0] = "softupTime";
					for (int i = 1; i < arrayLength; i++)
					{
						Map map = (Map) list.get(i);
						deviceId_array[i] = StringUtil.getStringValue(map.get("device_id"));
						paramArr[i] = StringUtil.getStringValue(map.get("devicetype_id"));
					}
					logger.warn("调用后台软件升级corba/kafka,数量小于100,deviceId_array="+deviceId_array.length+",paramArr="+paramArr.length,deviceIds);
					softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr, taskId);
				}
				else
				{
					if(arrayLength!=0){
						if(DBUtil.GetDB()==Global.DB_MYSQL){
							//TODO wait
							matchSQL = "select a.device_id,a.device_group,a.oui,a.device_serialnumber," 
									+ "a.device_name,a.manage_staff,a.city_id,a.office_id,a.zone_id,"
									+ "a.device_addr,a.complete_time,a.buy_time,a.service_year,a.staff_id,"
									+ "a.remark,a.loopback_ip,a.pdevice_id,a.interface_id,a.device_status,"
									+ "a.device_id_ex,a.res_pro_id,a.gather_id,a.oper_status,a.devicetype_id,"
									+ "a.maxenvelopes,a.retrycount,a.cr_port,a.cr_path,a.cpe_mac,"
									+ "a.cpe_currentupdatetime,a.cpe_allocatedstatus,a.cpe_currentstatus,"
									+ "a.cpe_operationinfo,a.cpe_username,a.cpe_passwd,a.acs_username,"
									+ "a.acs_passwd,a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,"
									+ "a.device_model_id,a.snmp_udp,a.customer_id,a.device_url,"
									+ "a.resource_type_id,a.os_version,a.x_com_passwd_old,"
									+ "a.vendor_id,a.dev_sub_sn,a.device_owner,"
									+ " from ("+matchSQL.replace("[", "\'")+") where limit "+arrayLength;
						}else{
							matchSQL = "select * from ("+matchSQL.replace("[", "\'")+") where rownum<="+arrayLength;
						}
					}
					logger.warn("调用后台软件升级corba/kafka：数量大于100，传sql数组,sql="+matchSQL.replace("[", "\'") );
						softUpgradeCorba.processDeviceStrategy(new String[] { matchSQL
								.replace("[", "\'") }, "5", new String[] {"softupTime"}, taskId);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception="+e.getMessage());
		}
		
	}

	public void batchUp4fenActive4AHLT(String param,long areaId,String gw_type)
	{
		logger.warn("begin batchUp4fenActive4AHLT:({},{},{})", param,areaId,gw_type);
		try
		{
			if(!"0".equals(deviceIds))
			{
				logger.warn("batchUp4fenActive4AHLT|deviceIds:{}",deviceIds);
				String[] deviceIdArray = deviceIds.split(",");
				List<Map<String,String>> deviceList = new ArrayList<Map<String, String>>();
				for (String deviceId : deviceIdArray) {
					Map<String, String> deviceMap = new HashMap<String, String>();
					deviceMap.put("device_id", deviceId);
					deviceMap.put("devicetype_id", bio.getDeviceTypeId(deviceId));
					deviceList.add(deviceMap);
				}
				//写入软件升级任务设备明细表
				logger.warn("batchUp4fenActive4AHLT|deviceList:{}",deviceList);
				int[] result = bio.insertSoftupTaskDev(taskId,deviceList);
				if(result == null || result.length == 0){
					logger.warn("batchUp4fenActive4AHLT|insert taskDev error,taskId:{}", taskId);
					response = "写入设备信息为空";
					return;
				}
				logger.warn("batchUp4fenActive4AHLT|insert taskDev success,taskId:{},insertSize:{}",taskId,result.length);
				return;
			}

			if(StringUtil.IsEmpty(param))
			{
				logger.warn("param为空");
				return;
			}
			//高级查询情况
			int arrayLength = 0;
			//若在页面选择了设备数量 则以选择的为主
			if(!StringUtil.IsEmpty(task_desc)){
				arrayLength = Integer.parseInt(task_desc);
			}
			List list = gwDeviceQueryBio.getDeviceList(gw_type,areaId, param);
			logger.warn("batchUp4fenActive4AHLT|taskId:{},senior listSize:{}", taskId,list.size());
			if(arrayLength != 0 && list.size() > arrayLength){
				list = list.subList(0,arrayLength);
			}
			//写入软件升级任务设备明细表
			int[] result = bio.insertSoftupTaskDev(taskId,list);
			if(result == null || result.length == 0){
				logger.warn("batchUp4fenActive4AHLT|senior insert taskDev error,taskId:{}", taskId);
				response = "写入设备信息为空";
				return;
			}
			logger.warn("batchUp4fenActive4AHLT|senior insert taskDev success,taskId:{},insertSize:{}",taskId,result.length);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception="+e.getMessage());
		}

	}
	
	
	
	public void softup(String[] device_list, long userId)
	{
		String strategyXmlParam = "";
		strategyXmlParam = softUpXml("", "", "", "", "", "", "", "");
		logger.debug("XML: " + strategyXmlParam);
		/** 入策略表，调预读 */
		ArrayList<String> sqllist = new ArrayList<String>();
		SuperDAO dao = new SuperDAO();
		String[] stragetyIds = new String[device_list.length];
		// 配置的service_id
		int serviceId = 5;
		//String strategyId = "";
		for (int i = 0; i < device_list.length; i++)
		{
			StrategyOBJ strategyObj = new StrategyOBJ();
			
			// 策略ID
			strategyObj.createId();
			
			/*if("".equals(strategyId)){
				strategyId = strategyObj.getId()+"";
			}else{
				strategyId = strategyId +";"+ strategyObj.getId();
			}*/
			
			// 策略配置时间
			strategyObj.setTime(TimeUtil.getCurrentTime());
			// 用户id
			strategyObj.setAccOid(userId);
			// 立即执行
			strategyObj.setType(0);
			// 设备ID
			strategyObj.setDeviceId(device_list[i]);
			// QOS serviceId
			strategyObj.setServiceId(serviceId);
			// 顺序,默认1
			strategyObj.setOrderId(1);
			// 工单类型: 新工单,工单参数为xml串的工单
			strategyObj.setSheetType(2);
			// 参数
			strategyObj.setSheetPara(strategyXmlParam);
			strategyObj.setTempId(serviceId);
			strategyObj.setIsLastOne(1);
			stragetyIds[i] = String.valueOf(strategyObj.getId());
			// 入策略表
			sqllist.addAll(dao.strategySQL(strategyObj));
		}
		boolean flag = false;
		logger.warn(sqllist.get(0));
		logger.warn(sqllist.get(1));
		logger.warn(sqllist.get(2));
		// 立即执行
		int iCode[] = DataSetBean.doBatch(sqllist);
		if (iCode != null && iCode.length > 0)
		{
			logger.warn("批量执行策略入库：  成功");
			flag = true;
		}
		else
		{
			logger.warn("批量执行策略入库：  失败");
			flag = false;
		}
		logger.warn("立即执行，开始调用预处理...");
		// 调用预读
		logger.warn("stragetyIds.length=========="+stragetyIds.length);
		if (true == CreateObjectFactory.createPreProcess(gw_type).processOOBatch(stragetyIds))
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
	}
	
	private String softUpXml(String file_ur, String username,String password,String softwarefile_size,
			String softwarefile_name,String delay_time,String sucess_url,String fail_url)
	{
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("SoftUpdate");
		root.addAttribute("flag", "1");
		root.addElement("CommandKey").addText("SoftUpdate");
		root.addElement("FileType").addText("1 Firmware Upgrade Image");
		root.addElement("URL").addText(file_ur);
		root.addElement("Username").addText(username);
		root.addElement("Password").addText(password);
		root.addElement("FileSize").addText(softwarefile_size);
		root.addElement("TargetFileName").addText(softwarefile_name);
		root.addElement("DelaySeconds").addText(delay_time);
		root.addElement("SuccessURL").addText(sucess_url);
		root.addElement("FailureURL").addText(fail_url);
		strXml = doc.asXML();
		return strXml;
	}
	
	public void batchUp4SoftUpModule(String param,long areaId,String gw_type,String goal_devicetype_id)
	{
		logger.warn("batchUp4SoftUpModule({},{},{})", new Object[] { param, areaId, goal_devicetype_id });
		logger.warn("deviceIds:"+deviceIds);
		try
		{
			PreProcessInterface softUpgradeCorba = CreateObjectFactory.createPreProcess(gw_type);
			if (!"0".equals(deviceIds))
			{
				String[] deviceId_array = deviceIds.split(",");
				String[] paramArr;
				paramArr = new String[1];
				paramArr[0] = goal_devicetype_id;
				softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr);
			}
			// 调用后台corba接口时，小于100的情况，传deviceID数组，否则传SQL
			else
			{
				if (StringUtil.IsEmpty(param))
				{
					logger.warn("param为空");
				}
				String[] _param = param.split("\\|");
				String matchSQL = _param[10];
				long total = StringUtil.getLongValue(_param[11]);
				if (total < 100)
				{
					logger.warn("调用后台软件升级corba接口：数量小于100，传deviceID数组");
					List list = gwDeviceQueryBio.getDeviceList(gw_type, areaId, param);
					String[] deviceId_array = new String[list.size()];
					String[] paramArr = new String[1];
					for (int i = 0; i < list.size(); i++)
					{
						Map map = (Map) list.get(i);
						deviceId_array[i] = StringUtil.getStringValue(map
								.get("device_id"));
					}
					paramArr[0] = goal_devicetype_id;
					softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr);
				}
				else
				{
					logger.warn("调用后台软件升级corba接口：数量大于100，传sql数组");
					softUpgradeCorba.processDeviceStrategy(new String[] { matchSQL.replace("[", "\'") }, "5", new String[] { goal_devicetype_id });
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception=" + e.getMessage());
		}
	}
	
	public String checkTmpData()
	{
		
		int i = gwDeviceQueryBio.getTmpList();
		logger.warn("@@@@@@@@@@@@@@@i="+i);
		ajax = String.valueOf(i);
		logger.warn("@@@@@@@@@@@@@@@ajax="+ajax);

		return "ajax";
	}
	
	private String softUp()
	{
		logger.warn("@@execute");
		int[] result;
		String fileName_ = fileName.substring(fileName.length()-3, fileName.length());
		List<String> dataList = null;

		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(fileName);
			}else{
				dataList = getImportDataByXLS(fileName);
			}
		}catch(FileNotFoundException e){
			response = "文件没找到！";
			return null;
		}catch(IOException e){
			response = "文件解析出错！";
			return null;
		}catch(Exception e){
			response = "文件解析出错！";
			return null;
		}

		//1为type, 默认1为软件升级，2为告警
		result = gwDeviceQueryBio.insertImportDataTmp(dataList,1);
		logger.warn("@@@@插入数结果result="+result.toString());
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select a.device_id,a.device_group,a.oui,a.device_serialnumber,");
			sql.append("a.device_name,a.manage_staff,a.city_id,a.office_id,a.zone_id,");
			sql.append("a.device_addr,a.complete_time,a.buy_time,a.service_year,a.staff_id,");
			sql.append("a.remark,a.loopback_ip,a.pdevice_id,a.interface_id,a.device_status,");
			sql.append("a.device_id_ex,a.res_pro_id,a.gather_id,a.oper_status,a.devicetype_id,");
			sql.append("a.maxenvelopes,a.retrycount,a.cr_port,a.cr_path,a.cpe_mac,");
			sql.append("a.cpe_currentupdatetime,a.cpe_allocatedstatus,a.cpe_currentstatus,");
			sql.append("a.cpe_operationinfo,a.cpe_username,a.cpe_passwd,a.acs_username,");
			sql.append("a.acs_passwd,a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,");
			sql.append("a.device_model_id,a.snmp_udp,a.customer_id,a.device_url,");
			sql.append("a.resource_type_id,a.os_version,a.x_com_passwd_old,");
			sql.append("a.vendor_id,a.dev_sub_sn,a.device_owner,");
		}else{
			sql.append("select a.*,");
		}
		sql.append("b.vendor_add,c.device_model,d.softwareversion ");
		
		if(this.importQueryField == "username"){
			String tableName = "tab_hgwcustomer";
			if(!StringUtil.IsEmpty(gw_type)&&"2".equals(gw_type)){
				tableName = "tab_egwcustomer";
			}
			sql.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,tab_softwareup_tmp t,");
			sql.append(tableName);
			sql.append(" e where a.device_id=e.device_id and a.device_status=1 ");
			sql.append("and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id ");
			sql.append("and a.devicetype_id=d.devicetype_id ");
			sql.append(" and e.user_state in ('1','2') ");
			sql.append(" and e.username = t.data");
		}
		else if(this.importQueryField == "device_serialnumber")
		{
			sql.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,tab_softwareup_tmp t ");
			sql.append(" where a.device_status =1 and a.vendor_id=b.vendor_id ");
			sql.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
			sql.append(" and a.device_serialnumber = t.data");
		}
		
		if (!StringUtil.IsEmpty(gw_type)&& !"null".equals(gw_type) ) {
			sql.append(" and a.gw_type = " + gw_type );
		}
		sql.append(" order by complete_time");
		logger.warn("###########sql ="+sql.toString().replace("[", "\'") +"goal_devicetype_id="+goal_devicetype_id);
		//安徽的软件升级模块 ， modified by zhangsb    2013年6月7日 
		if (Global.JSDX.equals(Global.instAreaShortName))
		{
			bio.batchUpAhSQL(sql.toString().replace("[", "\'"), softStrategy_type,starttime,endtime,goal_devicetype_id);
		}else{
			//直接调用软件升级模块，处理查询到的
			if(!Global.AHLT.equals(Global.instAreaShortName))
			{
				SoftUpgradeCorba softUpgradeImportCorba = new SoftUpgradeCorba(gw_type);
				softUpgradeImportCorba.processDeviceStrategy(new String[] { sql.toString().replace("[", "\'") }, "5", new String[] { goal_devicetype_id });
			}
			else
			{
				PreProcessInterface softUpgradeImportCorba = CreateObjectFactory.createPreProcess(gw_type);
				softUpgradeImportCorba.processDeviceStrategy(new String[] { sql.toString().replace("[", "\'") }, "5", new String[] {goal_devicetype_id}, taskId);
			}

		}
		return "result";
	}

	private String softUpActive()
	{
		int[] result;
		String fileName_ = fileName.substring(fileName.length()-3, fileName.length());
		List<String> dataList = null;

		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(fileName);
			}else{
				dataList = getImportDataByXLS(fileName);
			}
		}catch(FileNotFoundException e){
			response = "文件没找到！";
			return null;
		}catch(Exception e){
			response = "文件解析出错！";
			return null;
		}

		//1为type, 默认1为软件升级，2为告警
		result = gwDeviceQueryBio.insertImportDataTmp(dataList,1);
		logger.warn("@@@@插入数结果result="+result.toString());
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select a.device_id,a.device_group,a.oui,a.device_serialnumber,");
			sql.append("a.device_name,a.manage_staff,a.city_id,a.office_id,a.zone_id,");
			sql.append("a.device_addr,a.complete_time,a.buy_time,a.service_year,a.staff_id,");
			sql.append("a.remark,a.loopback_ip,a.pdevice_id,a.interface_id,a.device_status,");
			sql.append("a.device_id_ex,a.res_pro_id,a.gather_id,a.oper_status,a.devicetype_id,");
			sql.append("a.maxenvelopes,a.retrycount,a.cr_port,a.cr_path,a.cpe_mac,");
			sql.append("a.cpe_currentupdatetime,a.cpe_allocatedstatus,a.cpe_currentstatus,");
			sql.append("a.cpe_operationinfo,a.cpe_username,a.cpe_passwd,a.acs_username,");
			sql.append("a.acs_passwd,a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,");
			sql.append("a.device_model_id,a.snmp_udp,a.customer_id,a.device_url,");
			sql.append("a.resource_type_id,a.os_version,a.x_com_passwd_old,");
			sql.append("a.vendor_id,a.dev_sub_sn,a.device_owner,");
		}else{
			sql.append("select a.*,");
		}
		sql.append("b.vendor_add,c.device_model,d.softwareversion ");
		
		if(this.importQueryField.equals("username"))
		{
			String tableName = "tab_hgwcustomer";
			if(!StringUtil.IsEmpty(gw_type)&&"2".equals(gw_type)){
				tableName = "tab_egwcustomer";
			}
			sql.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,tab_softwareup_tmp t,");
			sql.append(tableName);
			sql.append(" e where a.device_id=e.device_id and a.device_status =1 and a.vendor_id=b.vendor_id ");
			sql.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
			sql.append(" and e.user_state in ('1','2') ");
			sql.append(" and e.username = t.data");
		}
		else if(this.importQueryField.equals("device_serialnumber"))
		{
			sql.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,tab_softwareup_tmp t ");
			sql.append(" where a.device_status =1 and a.vendor_id=b.vendor_id ");
			sql.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
			sql.append(" and a.device_serialnumber = t.data");
		}
		
		if (!StringUtil.IsEmpty(gw_type)&& !"null".equals(gw_type) ) {
			sql.append(" and a.gw_type = " + gw_type );
		}
		sql.append(" order by complete_time");
		logger.warn("###########sql ="+sql.toString().replace("[", "\'") +"goal_devicetype_id="+goal_devicetype_id);
		//安徽的软件升级模块 ， modified by zhangsb    2013年6月7日
		if (Global.JSDX.equals(Global.instAreaShortName))
		{
			bio.batchUpAhSQL(sql.toString().replace("[", "\'"), softStrategy_type,starttime,endtime,goal_devicetype_id);
		}else{
			//直接调用软件升级模块，处理查询到的
			if(!Global.AHLT.equals(Global.instAreaShortName))
			{
				SoftUpgradeCorba softUpgradeImportCorba = new SoftUpgradeCorba(gw_type);
				softUpgradeImportCorba.processDeviceStrategy(new String[] { sql.toString().replace("[", "\'") }, "5", new String[] { goal_devicetype_id });
			}
			else
			{
				PreProcessInterface softUpgradeImportCorba = CreateObjectFactory.createPreProcess(gw_type);
				softUpgradeImportCorba.processDeviceStrategy(new String[] { sql.toString().replace("[", "\'") },
						"5",
						new String[] {"softupTime"},
						taskId);
			}

		}
		return "result";
	}

	/**
	 * 安徽联通主动软件上升级-导入方式
	 * @return
	 */
	private String softUpActive4AHLT()
	{
		logger.warn("softUpActive4AHLT|begin softUpActive4AHLT import...");
		int[] result;
		String fileName_ = fileName.substring(fileName.length()-3, fileName.length());
		List<String> dataList = null;

		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(fileName);
			}else{
				dataList = getImportDataByXLS(fileName);
			}
		}catch(FileNotFoundException e){
			response = "文件没找到！";
			return null;
		}catch(Exception e){
			response = "文件解析出错！";
			return null;
		}

		//1为type, 默认1为软件升级，2为告警
		result = gwDeviceQueryBio.insertImportDataTmp(dataList,1);
		logger.warn("softUpActive4AHLT|import resultSize:{}",result.length);
		//根据导入数据类型关联查询获取设备id和设备版本
		List<Map<String,String>> deviceList = bio.getImportDeviceList(this.importQueryField,gw_type);
		if(deviceList == null || deviceList.size() == 0){
			logger.warn("softUpActive4AHLT|query deviceInfoList null,taskId:{}",taskId);
			response = "未查询到有效设备信息！";
			return null;
		}
		//写入软件升级任务设备明细表
		int[] insertResult = bio.insertSoftupTaskDev(taskId,deviceList);
		logger.warn("softUpActive4AHLT|insert taskDevList success,taskId:{},insert num:{}",taskId,insertResult.length);
		return "result";
	}

	/**
	 * 设备批量升级安徽用
	 * 
	 * @author modified by 张四辈
	 * @date 2013年6月6日 
	 * @return String
	 */
	public String batchUpAh()
	{
		logger.warn("batchUpAh()--gwShare_queryType="+gwShare_queryType);
		if ("4".equals(gwShare_queryType))
		{
			softUp();
		}else{
			try
			{
				UserRes curUser = (UserRes) session.get("curUser");
				long accoid = curUser.getUser().getId();
				if (true == StringUtil.IsEmpty(deviceIds))
				{
					logger.debug("任务中没有设备");
				}
				logger.warn("批量软件升级：直接入策略方式");
				//获取deviceId
				String[] deviceId_array = null;
				String matchSQL ="";
				long total = 0l;
				String[] _param = param.split("\\|");
				int len = _param.length;
				if(len>11){
					matchSQL = _param[10];
					total = StringUtil.getLongValue(_param[11]);
				}
				if(total<100){
					if (!"0".equals(deviceIds))
					{
						deviceId_array = deviceIds.split(",");
					}
					else
					{
						if (StringUtil.IsEmpty(param))
						{
							logger.warn("param为空");
						}
						List list = gwDeviceQueryBio.getDeviceList(gw_type, curUser.getAreaId(), param);
						deviceId_array = new String[list.size()];
				        for (int i = 0; i < list.size(); i++)
						{
						   Map map = (Map) list.get(i);
						   deviceId_array[i] = StringUtil.getStringValue(map.get("device_id"));
						}
					}
					//入库并通知软件升级模块
					bio.batchUpAh(accoid, deviceId_array, softStrategy_type,starttime,endtime,goal_devicetype_id);
				}else{
					logger.warn("数量大于等于100，根据传的sql解析");
					bio.batchUpAhSQL(matchSQL.replace("[", "\'"), softStrategy_type,starttime,endtime,goal_devicetype_id);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.warn("Exception="+e.getMessage());
				return "result";
			}
		}
		return "result";
	}
	/**
	 * 获取所有定制任务
	 * @return
	 */
	public String getAllTask(){
		taskList = bio.getAllTaskList();
		return "list";
	}
	public String deleteTask(){
		ajax =  bio.deleteTask(taskId);
		return "ajax";
	}
	/**
	 * 解析文件（xls格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws  
	 * 		   
	 */
	public List<String> getImportDataByXLS(String fileName) throws BiffException, IOException{
		logger.debug("getImportDataByXLS{}",fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath()+fileName);
		Workbook wwb = Workbook.getWorkbook(f);;
		Sheet ws = null;
		//总sheet数
		//int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m=0;m<sheetNumber;m++){
			ws = wwb.getSheet(m);
			
			//当前页总记录行数和列数
			int rowCount = ws.getRows();
			//int columeCount = ws.getColumns();
			
			if(null!=ws.getCell(0,0).getContents()){
				String line = ws.getCell(0,0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
				 * 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if(null!=line && "设备序列号".equals(line)){
					this.importQueryField = "device_serialnumber";
				}else{
					this.importQueryField = "username";
				}
			}
			
			//取当前页所有值放入list中
			for (int i=1;i<rowCount;i++){
				String temp = ws.getCell(0, i).getContents();
				if(!StringUtil.IsEmpty(temp)){
					if(!"".equals(ws.getCell(0, i).getContents().trim())){
						list.add(ws.getCell(0, i).getContents().trim());
					}
				}
			}
		}
		f.delete();
		f = null;
		return list;
	}
	
	
	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws FileNotFoundException 
	 * 		   IOException
	 */
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,IOException{
		logger.debug("getImportDataByTXT{}",fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
		 * 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if(null!=line && "设备序列号".equals(line)){
			this.importQueryField = "device_serialnumber";
		}else{
			this.importQueryField = "username";
		}
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if(!"".equals(line.trim())){
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		File f = new File(getFilePath()+fileName);
		f.delete();
		return list;
	}
	
	
	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		//获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径",lipossHome);
		return lipossHome + "/temp/";
	}
//	public String checkUploadLocalFile()
//	{
//	    Map fields = UploadDAO.getSoftFile(strRename);
//
//	    int icode = Integer.parseInt((String) fields.get("num"));
//	    if (icode > 0) {
//	        ajax = "ERROR:文件名重复，请重新上传！";
//	        fields = null;
//	        icode = 0;
//	    }else
//	    {
//	        ajax = "OK";
//	    }
//	    
//        return "ajax";
//	}
	
	/**
	 * <p>
	 * [文件上传到WEB]
	 * </p>
	 * @return
	 */
	public String uploadLocalFile()
	{
//        logger.error("开始!");
	    int index =  urlParameter.indexOf("path");
	   
        String strPath = urlParameter.substring(index + 5,urlParameter.indexOf("&", index));

        //目录
        File localPath = new File(LipossGlobals.G_ServerHome + "/" + strPath);
        if(!localPath.exists())
        {
            if(!localPath.mkdirs())
            {
                logger.error("缓存文件目录:{}创建失败！",LipossGlobals.G_ServerHome + "/" + strPath);
                this.response = "缓存文件目录创建失败！";
                return "response";
            }
        }
        
//        logger.error("-----------------------" + LipossGlobals.G_ServerHome + "/" + strPath);
        
        //缓存到本地
        File localFile = new File(LipossGlobals.G_ServerHome + "/" + strPath + "/" + file1.getName());
        if(localFile.exists())
        {
            if(!localFile.delete())
            {
                logger.error("旧缓存文件:{}删除失败！",LipossGlobals.G_ServerHome + "/" + strPath + "/" + file1.getName());
                this.response = "旧缓存文件删除失败！";
                return "response";
            }
        }
        
//        logger.error("-----------------------" + LipossGlobals.G_ServerHome + "/" + strPath + "/" + file1.getName());
        
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try
        {
            fis = new FileInputStream(file1);
            fos = new FileOutputStream(localFile);
            
//            byte[] tmps = new byte[1024];
            
            int ch = fis.read();
            while(ch != -1)
            {
                fos.write((char)ch);
                fos.flush();
                ch = fis.read();
            }
            fos.flush();
        }
        catch (FileNotFoundException e1)
        {
            //本地文件不存在
            logger.error("本地文件不存在！",e1);
            this.response = "本地文件不存在！";
            return "response";
        }
        catch (IOException e)
        {
            //读取文件失败
            logger.error("读取本地文件失败！",e);
            this.response = "读取本地文件失败！";
            return "response";
        }finally
        {
        	try {
        		if(fos!=null){
        			fos.close();
        			fos=null;
        		}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	try {
        		if(fis!=null){
        			fis.close();
        			fis=null;
        		}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        logger.info("WEB文件{}写入成功",localFile.getAbsolutePath());
        
        
        	
    	//转发文件服务器
        try  
        {   
        	String url = urlParameter.substring(0,urlParameter.indexOf("doUpload.jsp"));
            String[] urls = url.split(";");
            String urlLeft = urlParameter.substring(urlParameter.indexOf("doUpload.jsp"));
            StringBuffer response = new StringBuffer();   
          //防止FTP的账号密码对WEB上传时候造成影响，在通过WEB上传的时候将FTP密码置为空
            String serUser = urlLeft.substring(urlLeft.indexOf("seruser="), urlLeft.indexOf("&serpass"));
            String serPass = urlLeft.substring(urlLeft.indexOf("serpass="), urlLeft.indexOf("&remark"));
            String urlLeftNew = urlLeft; 
            urlLeftNew = com.linkage.commons.util.StringUtil.replace(urlLeftNew, serUser, "seruser=");
            urlLeftNew = com.linkage.commons.util.StringUtil.replace(urlLeftNew, serPass, "serpass=");
            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(urls[0]+(urls.length==1?"":"/")+urlLeftNew);
            method.setRequestHeader("Content-type", "multipart/form-data");
//            logger.error("urlParameter:{}",urlParameter);
      
            //设置Http Post数据，这里是上传文件   
//            File f= new File(LipossGlobals.G_ServerHome + "/" + strPath + "/" + localFile.getName());
//            FileInputStream fi=new FileInputStream(f);
//            FileInputStream fi=new FileInputStream(localFile);
//            
//            InputStreamRequestEntity fr=new InputStreamRequestEntity(fi);   
//            method.setRequestEntity((RequestEntity)fr);
//            NameValuePair[] data = { new NameValuePair("file1", localFile.getAbsolutePath())};
//            method.setRequestBody(fi);

//            StringPart sp  =   new  StringPart( " TEXT " ,  " testValue " ,  " GBK " );
            
            FilePart fp  =   new  FilePart( " file " ,  localFile.getName() ,  localFile,  null ,  " GBK " );

            method.getParams().setContentCharset( " GBK " );
             MultipartRequestEntity mrp =   new  MultipartRequestEntity( new  Part[]  { fp} , method
                    .getParams());
            method.setRequestEntity(mrp);

            //第一个通过web服务上传
            BufferedReader reader=null;
            try  
            {	
            		
                client.executeMethod(method); //这一步就把文件上传了   
                //下面是读取网站的返回网页，例如上传成功之类的
                int statusCode = method.getStatusCode();
                if (statusCode == HttpStatus.SC_OK)   
                {
                    //读取为 InputStream，在网页内容数据量大时候推荐使用     
                    reader = new BufferedReader(     
                            new InputStreamReader(method.getResponseBodyAsStream(),     
                                    "GBK"));     
                    String line;     
                    while ((line = reader.readLine()) != null)   
                    {
                            response.append(line);
                    }
                    
                    String str = response.toString();
                    int ind =  str.indexOf("idMsg");
                    this.response = str.substring(ind + 7,str.indexOf("</SPAN>", ind));
                }else
                {
                    this.response = urls[0]+"传输文件时异常";
                    logger.error("传输出现异常:{}!","传输文件时异常");
                }
            }   
            catch (IOException e)   
            {     
//                System.out.println("执行HTTP Post请求" + urlParameter + "时，发生异常！");     
//                e.printStackTrace();     
                logger.error("传输文件发生异常:{}", e);
                this.response = "执行HTTP Post请求时异常";
            }   
            finally  
            {     
            	try{
            		if(reader!=null){
            			reader.close();
            		}
            	}catch(IOException e){}
            	
                method.releaseConnection();
            }
            
            //其他采用ftp上传
            String newName = urlParameter.substring(urlParameter.indexOf("=", urlParameter.indexOf("fileRename"))+1, urlParameter.indexOf("&", urlParameter.indexOf("fileRename")));
            String seruser = urlParameter.substring(urlParameter.indexOf("=", urlParameter.indexOf("seruser"))+1, urlParameter.indexOf("&", urlParameter.indexOf("seruser")));
            String serpass = urlParameter.substring(urlParameter.indexOf("=", urlParameter.indexOf("serpass"))+1, urlParameter.indexOf("&", urlParameter.indexOf("serpass")));
            for(int i=1;i<urls.length;i++){
            	FTPClient ftp = new FTPClient();
            	int portIndex = urls[i].lastIndexOf(":");
            	String tmpUrl = urls[i].substring(urls[i].indexOf("://")+3, portIndex);
            	
            	//String prot = urls[i].substring(portIndex+1,urls[i].indexOf("/", portIndex));
        		logger.info(tmpUrl);
            	try
        		{
        			int reply;
        			String path = LipossGlobals.getLipossProperty("ftp.ftpDir");
        			int port = StringUtil.getIntegerValue(LipossGlobals
        					.getLipossProperty("ftp.port"));
        			File ftpLocalPath = new File(path);
        			if (!ftpLocalPath.exists())
        			{
        				ftpLocalPath.mkdir();
        			}
        			ftp.connect(tmpUrl,port);// 连接FTP服务器
        			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
        			//ftp.connect(tmpUrl);
        			ftp.login(seruser, serpass);// 登录
        			ftp.setBufferSize(100000);
        			reply = ftp.getReplyCode();
        			if (!FTPReply.isPositiveCompletion(reply))
        			{
        				ftp.disconnect();
        			}
        			ftp.changeWorkingDirectory(path);
        			FileInputStream in = new FileInputStream(localFile);
        			ftp.storeFile(newName, in);
        			in.close();
        			ftp.logout();
        			this.response = this.response + "\n"+urls[i]+"1#FTP文件上传成功!";
        		}
        		catch (Exception e)
        		{
        			this.response = this.response + "\n"+urls[i]+"-1#FTP文件上传失败!";
        		}
            }
         }
         catch (Exception e)   
         {
             logger.error("传输文件发生异常:{}", e);
             this.response = "传输文件发生异常";
         }finally
         {
           //删除缓存文件
             if(localFile.exists())
             {
                 if(localFile.delete())
                 {
                     logger.info("WEB文件{}删除成功",localFile.getAbsolutePath());
                 }else
                 {
                     logger.error("WEB文件{}删除失败",localFile.getAbsolutePath());
                 }
             }
             if(file1.exists())
             {
                 if(file1.delete())
                 {
                     logger.info("WEB文件{}删除成功",file1.getAbsolutePath());
                 }else
                 {
                     logger.error("WEB文件{}删除失败",file1.getAbsolutePath());
                 }
             }
             localFile = null;
             file1 = null;
         }
        
        return "response";
	}
	
	/**
	 * 将url中的参数转化成 Map<参数名，参数值>
	 * @param url
	 * @return
	 */
	public Map<String, String> getUrlParamMap(String url)
	{
		Map<String, String> map = new HashMap<String, String>();
		String urlParam = url.substring(url.indexOf('?') + 1);
		String[] urlArr = urlParam.split("&");
		
		String[] paramArr;
		for (int i = 0; i < urlArr.length; i++) {
			paramArr = urlArr[i].split("=");
			if(paramArr.length == 2) {
				map.put(paramArr[0], paramArr[1]);
			}else {
				map.put(paramArr[0], "");
			}
		}
		return map;
	}
	
	/**
	 * 校验版本文件是否存在
	 * @return
	 */
	public String checkUploadFile() 
	{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 0);
		
		// 获取参数
		Map paramMap = getUrlParamMap(urlParameter);
		
		String fileRename = StringUtil.getStringValue(paramMap, "fileRename");
		
		boolean fileIsExist = bio.fileIsExist(fileRename);
		
		logger.warn("[{}]文件是否存在:{}", fileRename, fileIsExist);
		
		if(fileIsExist) 
		{
			resultMap.put("code", 1);
			resultMap.put("message", "名称重复，请更改名称！");
			ajax = JSONObject.toJSONString(resultMap);
			return "ajax";
		}
		
		String tablename = StringUtil.getStringValue(paramMap, "tablename");
		String vendor_name = StringUtil.getStringValue(paramMap, "vendor_name");
		String device_model = StringUtil.getStringValue(paramMap, "device_model");
		String hardwareversion = StringUtil.getStringValue(paramMap, "hardwareversion");
		String softwareversion = StringUtil.getStringValue(paramMap, "softwareversion");
		
		if (tablename.equals("tab_software_file")) 
		{
			// 根据厂商名获取 型号Map<device_model, device_model_id>
			Map<String,String> devModelMap = bio.getModelMapByVendorName(vendor_name);
			
			// 获取设备类型
			List devTypeList = bio.getDevTypeList();
			
			// 设备类型List<vendor_id|device_model_id|hardwareversion>
			List<String> devicetypeList = getDevTypeList(devTypeList);
			// 设备类型Map<vendor_id|device_model_id|hardwareversion|softwareversion, devicetype_id>
			Map<String,String> devicetypeMap = getDevTypeMap(devTypeList);
			
			// 从版本文件信息表获取存在软件版本文件的设备类型id List<devicetype_id>
			List<String> devicetypeIdList = bio.getDevTypeIdBySoftwareFileIsexist();
			
			// 根据厂商名获取厂商列表 List<Map<"vendor_id",vendor_id>>
			List<Map<String,String>> vendorlist = bio.getVendorList(vendor_name);
			
			String[] arryDevModel = device_model.replace("AGELINK", " ").split(",");
			String[] arryHardware = hardwareversion.replace("AGELINK", " ").split(",");
			
			int iDevModelLen = arryDevModel.length;
			int iHardwareLen = arryHardware.length;

			int ouiNum = 0;
			ouiNum = vendorlist.size();
			Map<String, String> vendorMap = new HashMap<String, String>();
			//不考虑最后的LINKAGE
			iDevModelLen--;
			iHardwareLen--;
			String devicetypeId = "";
			String tmpOUI = "";
			String tmpDevModelId = null;
			
			for (int k = 0; k < ouiNum; k++) {
				vendorMap = vendorlist.get(k);
				tmpOUI = String.valueOf(vendorMap.get("vendor_id"));
				for (int i = 0; i < iDevModelLen; i++) 
				{
					tmpDevModelId = String.valueOf(devModelMap.get(arryDevModel[i]));
					for (int j = 0; j < iHardwareLen; j++) 
					{
						logger.warn("i,j,k:" + tmpOUI + "|"+ tmpDevModelId + "|"+ arryHardware[j]);
						
						if (devicetypeList.contains(tmpOUI + "|" + tmpDevModelId + "|" + arryHardware[j])) 
						{
							devicetypeId = (String) devicetypeMap
									.get(tmpOUI + "|" + tmpDevModelId + "|" + arryHardware[j] + "|" + softwareversion);
							if (devicetypeId != null || !"".equals(devicetypeId)) 
							{
								if (devicetypeIdList.contains(devicetypeId)) 
								{
									/*logger.warn("厂商:" + vendor_name 
											+ "(oui:" + tmpOUI 
											+ ") 型号：" + tmpDevModelId
											+ " 硬件版本：" + arryHardware[j]
											+ " 目标软件版本：" + softwareversion
											+ " 的软件升级文件已经存在");*/
									
									resultMap.put("code", 2);
									resultMap.put("message", "该厂商该版本的文件已经存在");
								}
							}
						}
					}
				}
			}

		}
		
		ajax = JSONObject.toJSONString(resultMap);
		return "ajax";
	}
	
	/**
	 * 获取设备类型Map<vendor_id|device_model_id|hardwareversion|softwareversion, devicetype_id>
	 * @param devTypeList
	 * @return
	 */
	private Map<String, String> getDevTypeMap(List<Map<String,String>> devTypeList) 
	{
		Map<String, String> devTypeMap = new HashMap<String, String>();
		String vendor_id;
		String device_model_id;
		String hardwareversion;
		String softwareversion;
		String devicetype_id;
		String dtype;
		
		for (Map<String,String> map : devTypeList) 
		{
			vendor_id = StringUtil.getStringValue(map, "vendor_id");
			device_model_id = StringUtil.getStringValue(map, "device_model_id");
			hardwareversion = StringUtil.getStringValue(map, "hardwareversion");
			softwareversion = StringUtil.getStringValue(map, "softwareversion");
			devicetype_id = StringUtil.getStringValue(map, "devicetype_id");
			
			dtype = vendor_id + "|" + device_model_id + "|" + hardwareversion + "|" + softwareversion;
					
			devTypeMap.put(dtype, devicetype_id);
		}
		return devTypeMap;
	}

	/**
	 * 获取设备类型List<vendor_id|device_model_id|hardwareversion>
	 * @param list
	 * @return
	 */
	private List<String> getDevTypeList(List<Map<String,String>> devTypeList) {
		List<String> list = new ArrayList<String>();
		String vendor_id;
		String device_model_id;
		String hardwareversion;
		String dtype;
		
		for (Map<String,String> map : devTypeList) 
		{
			vendor_id = StringUtil.getStringValue(map, "vendor_id");
			device_model_id = StringUtil.getStringValue(map, "device_model_id");
			hardwareversion = StringUtil.getStringValue(map, "hardwareversion");
			
			dtype = vendor_id + "|" + device_model_id + "|" + hardwareversion ;
					
			list.add(dtype);
		}
		return list;
	}
	
	public String queryTaskDetailById() 
	{
		taskResultMap = bio.queryTaskById(taskId);
		return "taskDetail";
	}

	/**
	 * @return the deviceIds
	 */
	public String getDeviceIds()
	{
		return deviceIds;
	}

	/**
	 * @param deviceIds
	 *            the deviceIds to set
	 */
	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
	}

	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	/**
	 * @return the bio
	 */
	public SoftwareBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(SoftwareBIO bio)
	{
		this.bio = bio;
	}

	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * @return the softStrategyHTML
	 */
	public String getSoftStrategyHTML()
	{
		return softStrategyHTML;
	}

	/**
	 * @param softStrategyHTML
	 *            the softStrategyHTML to set
	 */
	public void setSoftStrategyHTML(String softStrategyHTML)
	{
		this.softStrategyHTML = softStrategyHTML;
	}

	/**
	 * @return the softStrategy_type
	 */
	public String getSoftStrategy_type()
	{
		return softStrategy_type;
	}

	/**
	 * @param softStrategy_type
	 *            the softStrategy_type to set
	 */
	public void setSoftStrategy_type(String softStrategy_type)
	{
		this.softStrategy_type = softStrategy_type;
	}

	/**
	 * @return the param
	 */
	public String getParam()
	{
		return param;
	}

	/**
	 * @param param
	 *            the param to set
	 */
	public void setParam(String param)
	{
		this.param = param;
	}

	/**
	 * @return the gwDeviceQueryBio
	 */
	public GwDeviceQueryBIO getGwDeviceQueryBio()
	{
		return gwDeviceQueryBio;
	}

	/**
	 * @param gwDeviceQueryBio
	 *            the gwDeviceQueryBio to set
	 */
	public void setGwDeviceQueryBio(GwDeviceQueryBIO gwDeviceQueryBio)
	{
		this.gwDeviceQueryBio = gwDeviceQueryBio;
	}

    /**
     * 获取strRename
     * @return String strRename
     */
    public String getStrRename()
    {
        return strRename;
    }

    /**
     * 设置strRename
     * @param String strRename
     */
    public void setStrRename(String strRename)
    {
        this.strRename = strRename;
    }

    /**
     * 获取urlParameter
     * @return String urlParameter
     */
    public String getUrlParameter()
    {
        return urlParameter;
    }

    /**
     * 设置urlParameter
     * @param String urlParameter
     */
    public void setUrlParameter(String urlParameter)
    {
//        logger.error("setUrlParameter:{}",urlParameter);
        this.urlParameter = urlParameter;
    }

    /**
     * 获取file1
     * @return File file1
     */
    public File getFile1()
    {
        return file1;
    }

    /**
     * 设置file1
     * @param File file1
     */
    public void setFile1(File file1)
    {
        this.file1 = file1;
    }

    /**
     * 获取response
     * @return String response
     */
    public String getResponse()
    {
        return response;
    }

    /**
     * 设置response
     * @param String response
     */
    public void setResponse(String response)
    {
        this.response = response;
    }

    /**
     * 获取path
     * @return String path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * 设置path
     * @param String path
     */
    public void setPath(String path)
    {
        this.path = path;
    }

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}

	
	public String getGoal_devicetype_id()
	{
		return goal_devicetype_id;
	}

	
	public void setGoal_devicetype_id(String goal_devicetype_id)
	{
		this.goal_devicetype_id = goal_devicetype_id;
	}
	public String getImportQueryField() {
		return importQueryField;
	}

	public void setImportQueryField(String importQueryField) {
		this.importQueryField = importQueryField;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getGwShare_queryType() {
		return gwShare_queryType;
	}

	public void setGwShare_queryType(String gwShareQueryType) {
		gwShare_queryType = gwShareQueryType;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public List getTaskList() {
		return taskList;
	}

	public void setTaskList(List taskList) {
		this.taskList = taskList;
	}

	public String getStarttime() {
		return starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	
	public String getMode()
	{
		return mode;
	}

	
	public void setMode(String mode)
	{
		this.mode = mode;
	}

	
	public String getGwShare_queryType_this()
	{
		return gwShare_queryType_this;
	}

	
	public void setGwShare_queryType_this(String gwShare_queryType_this)
	{
		this.gwShare_queryType_this = gwShare_queryType_this;
	}

	
	public String getMaxActive()
	{
		return maxActive;
	}

	
	public void setMaxActive(String maxActive)
	{
		this.maxActive = maxActive;
	}

	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	
	public String getType()
	{
		return type;
	}

	
	public void setType(String type)
	{
		this.type = type;
	}

	
	public String getExpire_time_start()
	{
		return expire_time_start;
	}

	
	public void setExpire_time_start(String expire_time_start)
	{
		this.expire_time_start = expire_time_start;
	}

	
	public String getExpire_time_end()
	{
		return expire_time_end;
	}

	
	public void setExpire_time_end(String expire_time_end)
	{
		this.expire_time_end = expire_time_end;
	}

	
	public String getExpire_date_start()
	{
		return expire_date_start;
	}

	
	public void setExpire_date_start(String expire_date_start)
	{
		this.expire_date_start = expire_date_start;
	}

	
	public String getExpire_date_end()
	{
		return expire_date_end;
	}

	
	public void setExpire_date_end(String expire_date_end)
	{
		this.expire_date_end = expire_date_end;
	}

	
	public String getGwShare_queryTypeHidden()
	{
		return gwShare_queryTypeHidden;
	}

	
	public void setGwShare_queryTypeHidden(String gwShare_queryTypeHidden)
	{
		this.gwShare_queryTypeHidden = gwShare_queryTypeHidden;
	}

	
	public String getGwShare_queryFieldHidden()
	{
		return gwShare_queryFieldHidden;
	}

	
	public void setGwShare_queryFieldHidden(String gwShare_queryFieldHidden)
	{
		this.gwShare_queryFieldHidden = gwShare_queryFieldHidden;
	}

	
	public String getGwShare_queryParamHidden()
	{
		return gwShare_queryParamHidden;
	}

	
	public void setGwShare_queryParamHidden(String gwShare_queryParamHidden)
	{
		this.gwShare_queryParamHidden = gwShare_queryParamHidden;
	}

	
	public String getGwShare_cityIdHidden()
	{
		return gwShare_cityIdHidden;
	}

	
	public void setGwShare_cityIdHidden(String gwShare_cityIdHidden)
	{
		this.gwShare_cityIdHidden = gwShare_cityIdHidden;
	}

	
	public String getGwShare_onlineStatusHidden()
	{
		return gwShare_onlineStatusHidden;
	}

	
	public void setGwShare_onlineStatusHidden(String gwShare_onlineStatusHidden)
	{
		this.gwShare_onlineStatusHidden = gwShare_onlineStatusHidden;
	}

	
	public String getGwShare_vendorIdHidden()
	{
		return gwShare_vendorIdHidden;
	}

	
	public void setGwShare_vendorIdHidden(String gwShare_vendorIdHidden)
	{
		this.gwShare_vendorIdHidden = gwShare_vendorIdHidden;
	}

	
	public String getGwShare_deviceModelIdHidden()
	{
		return gwShare_deviceModelIdHidden;
	}

	
	public void setGwShare_deviceModelIdHidden(String gwShare_deviceModelIdHidden)
	{
		this.gwShare_deviceModelIdHidden = gwShare_deviceModelIdHidden;
	}

	
	public String getGwShare_devicetypeIdHidden()
	{
		return gwShare_devicetypeIdHidden;
	}

	
	public void setGwShare_devicetypeIdHidden(String gwShare_devicetypeIdHidden)
	{
		this.gwShare_devicetypeIdHidden = gwShare_devicetypeIdHidden;
	}

	
	public String getGwShare_bindTypeHidden()
	{
		return gwShare_bindTypeHidden;
	}

	
	public void setGwShare_bindTypeHidden(String gwShare_bindTypeHidden)
	{
		this.gwShare_bindTypeHidden = gwShare_bindTypeHidden;
	}

	
	public String getGwShare_deviceSerialnumberHidden()
	{
		return gwShare_deviceSerialnumberHidden;
	}

	
	public void setGwShare_deviceSerialnumberHidden(String gwShare_deviceSerialnumberHidden)
	{
		this.gwShare_deviceSerialnumberHidden = gwShare_deviceSerialnumberHidden;
	}

	
	public String getGwShare_fileNameHidden()
	{
		return gwShare_fileNameHidden;
	}

	
	public void setGwShare_fileNameHidden(String gwShare_fileNameHidden)
	{
		this.gwShare_fileNameHidden = gwShare_fileNameHidden;
	}

	
	public Map<String, String> getTaskResultMap()
	{
		return taskResultMap;
	}

	
	public void setTaskResultMap(Map<String, String> taskResultMap)
	{
		this.taskResultMap = taskResultMap;
	}

	public String getTask_desc() {
		return task_desc;
	}

	public void setTask_desc(String task_desc) {
		this.task_desc = task_desc;
	}

	public String getAHLT_softUp() {
		return AHLT_softUp;
	}

	public void setAHLT_softUp(String AHLT_softUp) {
		this.AHLT_softUp = AHLT_softUp;
	}
}
