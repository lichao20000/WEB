
package com.linkage.module.gwms.resource.act;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.bio.SoftwareBIO;
import com.linkage.module.gwms.resource.bio.VersionUpgradeBIO;
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
public class SoftwareNewACT extends ActionSupport implements SessionAware
{
	private static Logger logger = LoggerFactory.getLogger(SoftwareNewACT.class);
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
	private String starttime;
	private String endtime;
	private String taskId;
	private List taskList;
	// 任务号
	private Long taskid;
	private VersionUpgradeBIO vubio;

	public String init()
	{
		softStrategyHTML = bio.getStrategyList("softStrategy_type", new String[] { "4",
				"5" });
		return "success";
	}

	public String init4Ah()
	{
		softStrategyHTML = bio.getStrategyList("softStrategy_type", new String[] { "4",
				"5" });
		return "batch4Ah";
	}

	/**
	 * add by zhangsb 2013年6月5日 15:45:43
	 * 
	 * @return
	 */
	public String initAh()
	{
		softStrategyHTML = bio.getStrategyList("softStrategy_type", new String[] { "4",
				"5" });
		return "batchAh";
	}

	/**
	 * 校验任务号状态.
	 * 
	 * @return 任务状态
	 */
	public String checkTaskId()
	{
		List<Map> queryByTaskId = vubio.queryTaskStatusByTaskId(taskid);
		if(queryByTaskId != null && !queryByTaskId.isEmpty()){
			Map map = queryByTaskId.get(0);
			Object taskStatus = null;
			if(map!=null){
				taskStatus = (BigDecimal)(map.get("task_status"));
			}
			taskStatus = taskStatus == null ? 0 : taskStatus;
			ajax = taskStatus.toString();
			logger.warn("checkTaskId ended, ajax = {}", ajax);
		}
		// 根本没这条数据
		else{
			ajax = "0";
		}
		return "ajax";
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
		logger.debug("execute()");
		logger.warn("!!!!!!!!!!gwShare_queryType=" + gwShare_queryType);
		if ("4".equals(gwShare_queryType))
		{
			softUp();
		}
		UserRes curUser = (UserRes) session.get("curUser");
		// String isOpenSoftUpModule =
		// LipossGlobals.getLipossProperty("isOpenSoftUpModule");
		if (true == StringUtil.IsEmpty(deviceIds))
		{
			logger.debug("任务中没有设备");
		}

		List<Map> listByTaskId = vubio.queryMapByTaskId(taskid);
		// 说明表中已经存在这样的任务号了，那么tab_version_upgrade不操作。
		if(listByTaskId!=null && listByTaskId.size()>0){
			
		}
		// 该任务号查不到，是新的任务号
		else{
			// 定制人
			long accoid = curUser.getUser().getId();
			// 添加时间
			long add_time = TimeUtil.getCurrentTime();
			// 插入表tab_version_upgrade
			logger.warn(
					"开始插入表：tab_version_upgrade, taskid = {}, accoid = {}, add_time = {}， softStrategy_type = {}",
					new Object[] { taskid, accoid, add_time, softStrategy_type });
			vubio.insert(taskid, accoid, add_time, 0L, softStrategy_type, null, null);
		}
		
		// 由于之前已经有ajax查过状态做过校验了，这边就不再做校验了，直接插入表tab_version_upgrade_dev
		String[] deviceId_array = null;
		logger.warn("设备id字符串：deviceIds = {}", deviceIds);
		if (!"0".equals(deviceIds) && !deviceIds.isEmpty())
		{
			deviceId_array = deviceIds.split(",");
			logger.warn("设备id数组：deviceId_array = {}", deviceId_array);
			if (deviceId_array != null)
			{
				String queryString = "";
				if (deviceId_array.length > 0)
				{
					for (int i = 0; i < deviceId_array.length; i++)
					{
						if (i < deviceId_array.length - 1)
						{
							queryString += deviceId_array[i] + ",";
						}
						else
						{
							queryString += deviceId_array[i];
						}
						logger.warn("device_Id 字符串：queryString= {}", queryString);
						List list = vubio.queryByDeviceIds(queryString);
						if (list != null && !list.isEmpty())
						{
							for (Object obj : list)
							{
								Map map = (Map) obj;
								String device_id = (String) map.get("device_id");
								String oui = (String) map.get("oui");
								String device_serialnumber = (String) map
										.get("device_serialnumber");
								String city_id = (String) map.get("city_id");
								logger.warn(
										"开始插入表：tab_version_upgrade_dev：参数：taskid = {}, device_id = {}, oui = {}, device_serialnumber = {}, city_id = {}",
										new Object[] { taskid, device_id, oui,
												device_serialnumber, city_id });
								vubio.insertVersionUpgradeDev(taskid,
										Long.parseLong(device_id), oui,
										device_serialnumber, city_id);
							}
						}
					}
				}
			}
		}
		return "result";
	}

	public void batchUp4SoftUpModule(String param, long areaId, String gw_type)
	{
		logger.warn("batchUp4SoftUpModule({},{},{})", new Object[] { param, areaId,
				gw_type });
		try
		{
			SoftUpgradeCorba softUpgradeCorba = new SoftUpgradeCorba(gw_type);
			if (!"0".equals(deviceIds))
			{
				String[] deviceId_array = deviceIds.split(",");
				String[] paramArr;
				paramArr = new String[deviceId_array.length];
				for (int i = 0; i < deviceId_array.length; i++)
				{
					paramArr[i] = bio.getDeviceTypeId(deviceId_array[i]);
				}
				logger.warn("deviceIds={},调用后台软件升级corba接口", deviceIds);
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
					String[] paramArr = new String[list.size()];
					for (int i = 0; i < list.size(); i++)
					{
						Map map = (Map) list.get(i);
						deviceId_array[i] = StringUtil.getStringValue(map
								.get("device_id"));
						paramArr[i] = StringUtil.getStringValue(map.get("devicetype_id"));
					}
					softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr);
				}
				else
				{
					logger.warn("调用后台软件升级corba接口：数量大于100，传sql数组");
					softUpgradeCorba.processDeviceStrategy(
							new String[] { matchSQL.replace("[", "\'") }, "5",
							new String[] {});
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception=" + e.getMessage());
		}
	}

	public void batchUp4fen(String param, long areaId, String gw_type)
	{
		logger.warn("支持分配置模块的批量软件升级batchUp4fen({},{},{})", new Object[] { param, areaId,
				gw_type });
		try
		{
			PreProcessInterface softUpgradeCorba = CreateObjectFactory.createPreProcess(gw_type);
			if (!"0".equals(deviceIds))
			{
				String[] deviceId_array = deviceIds.split(",");
				String[] paramArr;
				paramArr = new String[deviceId_array.length];
				for (int i = 0; i < deviceId_array.length; i++)
				{
					paramArr[i] = bio.getDeviceTypeId(deviceId_array[i]);
				}
				logger.warn("deviceIds={},调用后台软件升级corba接口", deviceIds);
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
					String[] paramArr = new String[list.size()];
					for (int i = 0; i < list.size(); i++)
					{
						Map map = (Map) list.get(i);
						deviceId_array[i] = StringUtil.getStringValue(map
								.get("device_id"));
						paramArr[i] = StringUtil.getStringValue(map.get("devicetype_id"));
					}
					softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr);
				}
				else
				{
					logger.warn("调用后台软件升级corba接口：数量大于100，传sql数组");
					softUpgradeCorba.processDeviceStrategy(
							new String[] { matchSQL.replace("[", "\'") }, "5",
							new String[] {});
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception=" + e.getMessage());
		}
	}

	public void batchUp4SoftUpModule(String param, long areaId, String gw_type,
			String goal_devicetype_id)
	{
		logger.warn("batchUp4SoftUpModule({},{},{})", new Object[] { param, areaId,
				goal_devicetype_id });
		logger.warn("deviceIds:" + deviceIds);
		try
		{
			SoftUpgradeCorba softUpgradeCorba = new SoftUpgradeCorba(gw_type);
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
					softUpgradeCorba.processDeviceStrategy(
							new String[] { matchSQL.replace("[", "\'") }, "5",
							new String[] { goal_devicetype_id });
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
		ajax = String.valueOf(i);
		logger.warn("@@@@@@@@@@@@@@@ajax=" + ajax);
		return "ajax";
	}

	private String softUp()
	{
		int[] result;
		String fileName_ = fileName.substring(fileName.length() - 3, fileName.length());
		List<String> dataList = null;
		try
		{
			if ("txt".equals(fileName_))
			{
				dataList = getImportDataByTXT(fileName);
			}
			else
			{
				dataList = getImportDataByXLS(fileName);
			}
		}
		catch (FileNotFoundException e)
		{
			response = "文件没找到！";
			return null;
		}
		catch (Exception e)
		{
			response = "文件解析出错！";
			return null;
		}
		// 1为type, 默认1为软件升级，2为告警
		result = gwDeviceQueryBio.insertImportDataTmp(dataList, 1);
		logger.warn("@@@@插入数结果result=" + result.toString());
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
		if (this.importQueryField == "username")
		{
			String tableName = "tab_hgwcustomer";
			if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type))
			{
				tableName = "tab_egwcustomer";
			}
			sql.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,tab_softwareup_tmp t,");
			sql.append(tableName);
			sql.append(" e where a.device_id=e.device_id and a.device_status =1 and a.vendor_id=b.vendor_id ");
			sql.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
			sql.append(" and e.user_state in ('1','2') ");
			sql.append(" and e.username = t.data");
		}
		else if (this.importQueryField == "device_serialnumber")
		{
			sql.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,tab_softwareup_tmp t ");
			sql.append(" where a.device_status =1 and a.vendor_id=b.vendor_id ");
			sql.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
			sql.append(" and a.device_serialnumber = t.data");
		}
		
		if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type))
		{
			sql.append(" and a.gw_type = " + gw_type);
		}
		sql.append(" order by complete_time");
		logger.warn("###########sql =" + sql.toString().replace("[", "\'")
				+ "goal_devicetype_id=" + goal_devicetype_id);
		// 安徽的软件升级模块 ， modified by zhangsb 2013年6月7日
		if (Global.JSDX.equals(Global.instAreaShortName))
		{
			bio.batchUpAhSQL(sql.toString().replace("[", "\'"), softStrategy_type,
					starttime, endtime, goal_devicetype_id);
		}
		else
		{
			// 直接调用软件升级模块，处理查询到的
			SoftUpgradeCorba softUpgradeImportCorba = new SoftUpgradeCorba(gw_type);
			softUpgradeImportCorba.processDeviceStrategy(new String[] { sql.toString()
					.replace("[", "\'") }, "5", new String[] { goal_devicetype_id });
		}
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
		logger.warn("batchUpAh()--gwShare_queryType=" + gwShare_queryType);
		if ("4".equals(gwShare_queryType))
		{
			softUp();
		}
		else
		{
			try
			{
				UserRes curUser = (UserRes) session.get("curUser");
				long accoid = curUser.getUser().getId();
				if (true == StringUtil.IsEmpty(deviceIds))
				{
					logger.debug("任务中没有设备");
				}
				logger.warn("批量软件升级：直接入策略方式");
				// 获取deviceId
				String[] deviceId_array = null;
				String matchSQL = "";
				long total = 0l;
				String[] _param = param.split("\\|");
				int len = _param.length;
				if (len > 11)
				{
					matchSQL = _param[10];
					total = StringUtil.getLongValue(_param[11]);
				}
				if (total < 100)
				{
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
						List list = gwDeviceQueryBio.getDeviceList(gw_type,
								curUser.getAreaId(), param);
						deviceId_array = new String[list.size()];
						for (int i = 0; i < list.size(); i++)
						{
							Map map = (Map) list.get(i);
							deviceId_array[i] = StringUtil.getStringValue(map
									.get("device_id"));
						}
					}
					// 入库并通知软件升级模块
					bio.batchUpAh(accoid, deviceId_array, softStrategy_type, starttime,
							endtime, goal_devicetype_id);
				}
				else
				{
					logger.warn("数量大于等于100，根据传的sql解析");
					bio.batchUpAhSQL(matchSQL.replace("[", "\'"), softStrategy_type,
							starttime, endtime, goal_devicetype_id);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.warn("Exception=" + e.getMessage());
				return "result";
			}
		}
		return "result";
	}

	/**
	 * 获取所有定制任务
	 * 
	 * @return
	 */
	public String getAllTask()
	{
		taskList = bio.getAllTaskList();
		return "list";
	}

	public String deleteTask()
	{
		ajax = bio.deleteTask(taskId);
		return "ajax";
	}

	/**
	 * 解析文件（xls格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 */
	public List<String> getImportDataByXLS(String fileName) throws BiffException,
			IOException
	{
		logger.debug("getImportDataByXLS{}", fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		;
		Sheet ws = null;
		// 总sheet数
		// int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++)
		{
			ws = wwb.getSheet(m);
			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			// int columeCount = ws.getColumns();
			if (null != ws.getCell(0, 0).getContents())
			{
				String line = ws.getCell(0, 0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if (null != line && "设备序列号".equals(line))
				{
					this.importQueryField = "device_serialnumber";
				}
				else
				{
					this.importQueryField = "username";
				}
			}
			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++)
			{
				String temp = ws.getCell(0, i).getContents();
				if (null != temp && !"".equals(temp))
				{
					if (!"".equals(ws.getCell(0, i).getContents().trim()))
					{
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
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws FileNotFoundException
	 *             IOException
	 */
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,
			IOException
	{
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath() + fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if (null != line && "设备序列号".equals(line))
		{
			this.importQueryField = "device_serialnumber";
		}
		else
		{
			this.importQueryField = "username";
		}
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null)
		{
			if (!"".equals(line.trim()))
			{
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		File f = new File(getFilePath() + fileName);
		f.delete();
		return list;
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath()
	{
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try
		{
			lipossHome = java.net.URLDecoder.decode(
					a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	// public String checkUploadLocalFile()
	// {
	// Map fields = UploadDAO.getSoftFile(strRename);
	//
	// int icode = Integer.parseInt((String) fields.get("num"));
	// if (icode > 0) {
	// ajax = "ERROR:文件名重复，请重新上传！";
	// fields = null;
	// icode = 0;
	// }else
	// {
	// ajax = "OK";
	// }
	//
	// return "ajax";
	// }
	/**
	 * <p>
	 * [文件上传到WEB]
	 * </p>
	 * 
	 * @return
	 */
	public String uploadLocalFile()
	{
		// logger.error("开始!");
		int index = urlParameter.indexOf("path");
		String strPath = urlParameter.substring(index + 5,
				urlParameter.indexOf("&", index));
		// 目录
		File localPath = new File(LipossGlobals.G_ServerHome + "/" + strPath);
		if (!localPath.exists())
		{
			if (!localPath.mkdirs())
			{
				logger.error("缓存文件目录:{}创建失败！", LipossGlobals.G_ServerHome + "/" + strPath);
				this.response = "缓存文件目录创建失败！";
				return "response";
			}
		}
		// logger.error("-----------------------" + LipossGlobals.G_ServerHome + "/" +
		// strPath);
		// 缓存到本地
		File localFile = new File(LipossGlobals.G_ServerHome + "/" + strPath + "/"
				+ file1.getName());
		if (localFile.exists())
		{
			if (!localFile.delete())
			{
				logger.error("旧缓存文件:{}删除失败！", LipossGlobals.G_ServerHome + "/" + strPath
						+ "/" + file1.getName());
				this.response = "旧缓存文件删除失败！";
				return "response";
			}
		}
		// logger.error("-----------------------" + LipossGlobals.G_ServerHome + "/" +
		// strPath + "/" + file1.getName());
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try
		{
			fis = new FileInputStream(file1);
			fos = new FileOutputStream(localFile);
			// byte[] tmps = new byte[1024];
			int ch = fis.read();
			while (ch != -1)
			{
				fos.write((char) ch);
				fos.flush();
				ch = fis.read();
			}
			fos.flush();
		}
		catch (FileNotFoundException e1)
		{
			// 本地文件不存在
			logger.error("本地文件不存在！", e1);
			this.response = "本地文件不存在！";
			return "response";
		}
		catch (IOException e)
		{
			// 读取文件失败
			logger.error("读取本地文件失败！", e);
			this.response = "读取本地文件失败！";
			return "response";
		}
		finally
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
		logger.info("WEB文件{}写入成功", localFile.getAbsolutePath());
		// 转发文件服务器
		try
		{
			StringBuffer response = new StringBuffer();
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(urlParameter);
			method.setRequestHeader("Content-type", "multipart/form-data");
			// logger.error("urlParameter:{}",urlParameter);
			// 设置Http Post数据，这里是上传文件
			// File f= new File(LipossGlobals.G_ServerHome + "/" + strPath + "/" +
			// localFile.getName());
			// FileInputStream fi=new FileInputStream(f);
			// FileInputStream fi=new FileInputStream(localFile);
			//
			// InputStreamRequestEntity fr=new InputStreamRequestEntity(fi);
			// method.setRequestEntity((RequestEntity)fr);
			// NameValuePair[] data = { new NameValuePair("file1",
			// localFile.getAbsolutePath())};
			// method.setRequestBody(fi);
			// StringPart sp = new StringPart( " TEXT " , " testValue " , " GBK " );
			FilePart fp = new FilePart(" file ", localFile.getName(), localFile, null,
					" GBK ");
			method.getParams().setContentCharset(" GBK ");
			MultipartRequestEntity mrp = new MultipartRequestEntity(new Part[] { fp },
					method.getParams());
			method.setRequestEntity(mrp);
			
			BufferedReader reader=null;
			try
			{
				client.executeMethod(method); // 这一步就把文件上传了
				// 下面是读取网站的返回网页，例如上传成功之类的
				int statusCode = method.getStatusCode();
				logger.error("statusCode:{}", statusCode);
				if (statusCode == HttpStatus.SC_OK)
				{
					// 读取为 InputStream，在网页内容数据量大时候推荐使用
					reader = new BufferedReader(new InputStreamReader(
							method.getResponseBodyAsStream(), "GBK"));
					String line;
					while ((line = reader.readLine()) != null)
					{
						response.append(line);
					}
					
					String str = response.toString();
					int ind = str.indexOf("idMsg");
					this.response = str.substring(ind + 7, str.indexOf("</SPAN>", ind));
				}
				else
				{
					this.response = "传输文件时异常";
					logger.error("传输出现异常:{}!", this.response);
				}
			}
			catch (IOException e)
			{
				// System.out.println("执行HTTP Post请求" + urlParameter + "时，发生异常！");
				// e.printStackTrace();
				logger.error("传输文件发生异常:{}", e);
				this.response = "执行HTTP Post请求时异常";
			}
			finally
			{
				try{
					if(reader!=null){
						reader.close();
					}
				}catch (IOException e){}
				
				method.releaseConnection();
			}
		}
		catch (Exception e)
		{
			logger.error("传输文件发生异常:{}", e);
			this.response = "传输文件发生异常";
		}
		finally
		{
			// 删除缓存文件
			if (localFile.exists())
			{
				if (localFile.delete())
				{
					logger.info("WEB文件{}删除成功", localFile.getAbsolutePath());
				}
				else
				{
					logger.error("WEB文件{}删除失败", localFile.getAbsolutePath());
				}
			}
			if (file1.exists())
			{
				if (file1.delete())
				{
					logger.info("WEB文件{}删除成功", file1.getAbsolutePath());
				}
				else
				{
					logger.error("WEB文件{}删除失败", file1.getAbsolutePath());
				}
			}
			localFile = null;
			file1 = null;
		}
		return "response";
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
	 * 
	 * @return String strRename
	 */
	public String getStrRename()
	{
		return strRename;
	}

	/**
	 * 设置strRename
	 * 
	 * @param String
	 *            strRename
	 */
	public void setStrRename(String strRename)
	{
		this.strRename = strRename;
	}

	/**
	 * 获取urlParameter
	 * 
	 * @return String urlParameter
	 */
	public String getUrlParameter()
	{
		return urlParameter;
	}

	/**
	 * 设置urlParameter
	 * 
	 * @param String
	 *            urlParameter
	 */
	public void setUrlParameter(String urlParameter)
	{
		// logger.error("setUrlParameter:{}",urlParameter);
		this.urlParameter = urlParameter;
	}

	/**
	 * 获取file1
	 * 
	 * @return File file1
	 */
	public File getFile1()
	{
		return file1;
	}

	/**
	 * 设置file1
	 * 
	 * @param File
	 *            file1
	 */
	public void setFile1(File file1)
	{
		this.file1 = file1;
	}

	/**
	 * 获取response
	 * 
	 * @return String response
	 */
	public String getResponse()
	{
		return response;
	}

	/**
	 * 设置response
	 * 
	 * @param String
	 *            response
	 */
	public void setResponse(String response)
	{
		this.response = response;
	}

	/**
	 * 获取path
	 * 
	 * @return String path
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * 设置path
	 * 
	 * @param String
	 *            path
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	public String getGw_type()
	{
		return gw_type;
	}

	public void setGw_type(String gwType)
	{
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

	public String getImportQueryField()
	{
		return importQueryField;
	}

	public void setImportQueryField(String importQueryField)
	{
		this.importQueryField = importQueryField;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getGwShare_queryType()
	{
		return gwShare_queryType;
	}

	public void setGwShare_queryType(String gwShareQueryType)
	{
		gwShare_queryType = gwShareQueryType;
	}

	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	public List getTaskList()
	{
		return taskList;
	}

	public void setTaskList(List taskList)
	{
		this.taskList = taskList;
	}

	public String getStarttime()
	{
		return starttime;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public Long getTaskid()
	{
		return taskid;
	}

	public void setTaskid(Long taskid)
	{
		this.taskid = taskid;
	}

	public VersionUpgradeBIO getVubio()
	{
		return vubio;
	}

	public void setVubio(VersionUpgradeBIO vubio)
	{
		this.vubio = vubio;
	}
}
