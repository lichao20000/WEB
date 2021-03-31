
package com.linkage.module.gtms.config.serv;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gtms.config.dao.ParamNodeBatchConfigDAO;
import com.linkage.module.gtms.config.obj.ConfigOBJ;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.itms.resource.bio.ServTemplateBIO;

import edu.emory.mathcs.backport.java.util.Arrays;

public class ParamNodeBatchConfigServImpl implements ParamNodeBatchConfigServ,SessionAware,ServletRequestAware {

	private static Logger logger = LoggerFactory
			.getLogger(ParamNodeBatchConfigServImpl.class);
	private ParamNodeBatchConfigDAO dao;
	private String instArea = Global.instAreaShortName;
	private HttpServletRequest request;
	private List<Map> deviceList;
	private ServTemplateBIO bio;
	private Map session;

	public ParamNodeBatchConfigDAO getDao()
	{
		return dao;
	}

	public void setDao(ParamNodeBatchConfigDAO dao)
	{
		this.dao = dao;
	}

	public String doConfigAll(String[] deviceIds, String serviceId, String[] paramArr,
			String gwType)
	{
		logger.debug("serv-->doConfigAll({},{},{},{})", new Object[] { deviceIds,
				serviceId, paramArr, gwType });
		if (Global.NXDX.equals(instArea))
		{
			String devices = Arrays.toString(deviceIds).replace("[", "").replace("]", "");
			String param = Arrays.toString(paramArr).replace("[", "").replace("]", "");
			long time = System.currentTimeMillis() / 1000L;
			if (devices.indexOf("select") != -1)
			{
				PrepareSQL psql = new PrepareSQL();
				psql.append("insert into tab_batchconfig_task(task_id,service_id,query_sql,");
				psql.append("param,gw_type,add_time,task_status) ");
				psql.append("values(?,?,?,?,?,?,?) ");
				psql.setLong(1, time);
				psql.setInt(2, Integer.parseInt(serviceId));
				psql.setString(3, devices);
				psql.setString(4, param);
				psql.setInt(5, Integer.parseInt(gwType));
				psql.setLong(6, time);
				psql.setInt(7, 0);
				if (DataSetBean.executeUpdate(psql.getSQL()) == 1)
				{
					return "1";
				}
				else
				{
					return "-4";
				}
			}
			else if (deviceIds.length > 0)
			{
				StringBuffer str = new StringBuffer();
				str.append("'" + deviceIds[0]);
				if (deviceIds.length > 1)
				{
					for (int i = 1; i < deviceIds.length; i++)
					{
						str.append("','" + deviceIds[i]);
					}
				}
				str.append("'");
				PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
				pSQL.append("select a.device_id,b.username,");
				pSQL.append("c.device_model,d.softwareversion,d.hardwareversion ");
				pSQL.append("from tab_gw_device a,tab_hgwcustomer b,gw_device_model c,tab_devicetype_info d ");
				pSQL.append("where a.device_status=1 and a.device_id=b.device_id ");
				pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
				if (!StringUtil.IsEmpty(gwType) && !"null".equals(gwType))
				{
					pSQL.append("and a.gw_type=" + gwType + " ");
				}
				pSQL.append("and a.device_id in(" + str.toString() + ") ");
				ArrayList<HashMap<String, String>> list = DBOperation.getRecords(pSQL
						.getSQL());
				if (null != list && !list.isEmpty())
				{
					ArrayList<String> sqlList = new ArrayList<String>();
					PrepareSQL psql = new PrepareSQL();
					psql.append("insert into tab_batchconfig_task(task_id,service_id,param,");
					psql.append("gw_type,add_time,task_status) ");
					psql.append("values(?,?,?,?,?,?) ");
					psql.setLong(1, time);
					psql.setInt(2, Integer.parseInt(serviceId));
					psql.setString(3, param);
					psql.setInt(4, Integer.parseInt(gwType));
					psql.setLong(5, time);
					psql.setInt(6, 1);
					DBOperation.executeUpdate(psql.getSQL());
					for (HashMap<String, String> mapDevice : list)
					{
						StringBuffer sb = new StringBuffer();
						sb.append("insert into tab_batchconfig_dev(task_id,device_id,");
						sb.append("user_name,device_model,softwareversion,hardwareversion,do_time,status) ");
						sb.append("values (" + time + ",");
						sb.append("'" + StringUtil.getStringValue(mapDevice, "device_id")
								+ "',");
						sb.append("'" + StringUtil.getStringValue(mapDevice, "username")
								+ "',");
						sb.append("'"
								+ StringUtil.getStringValue(mapDevice, "device_model")
								+ "',");
						sb.append("'"
								+ StringUtil.getStringValue(mapDevice, "softwareversion")
								+ "',");
						sb.append("'"
								+ StringUtil.getStringValue(mapDevice, "hardwareversion")
								+ "',");
						sb.append(time + ",");
						sb.append(-1);
						sb.append(") ");
						sqlList.add(sb.toString());
					}
					DBOperation.executeUpdate(sqlList);
				}
			}
		}
		if (deviceIds.length == 1)
		{
			String strategyXmlParam = "";
			strategyXmlParam = paramNodeBXml(paramArr);
			// 入策略表，调预读
			ArrayList<String> sqllist = new ArrayList<String>();
			SuperDAO dao = new SuperDAO();
			String[] stragetyIds = new String[deviceIds.length];
			// 得到设备类型
			int gw_type_int = -1;
			gw_type_int = Integer.parseInt(gwType);
			// 配置的service_id
			int serviceId_int = Integer.parseInt(serviceId);
			for (int i = 0; i < deviceIds.length; i++)
			{
				StrategyOBJ strategyObj = new StrategyOBJ();
				// 策略ID
				strategyObj.createId();
				// 策略配置时间
				strategyObj.setTime(TimeUtil.getCurrentTime());
				// 用户id
				strategyObj.setAccOid(1);
				// 立即执行
				strategyObj.setType(1);
				// 设备ID
				strategyObj.setDeviceId(deviceIds[i]);
				// serviceId
				strategyObj.setServiceId(serviceId_int);
				// 顺序,默认1
				strategyObj.setOrderId(1);
				// 工单类型: 新工单,工单参数为xml串的工单
				strategyObj.setSheetType(2);
				// 参数
				strategyObj.setSheetPara(strategyXmlParam);
				strategyObj.setTempId(serviceId_int);
				strategyObj.setIsLastOne(1);
				stragetyIds[i] = String.valueOf(strategyObj.getId());
				// 入策略表
				sqllist.addAll(dao.strategySQL(strategyObj));
			}
			// 立即执行
			int iCode[] = DataSetBean.doBatch(sqllist);
			if (iCode != null && iCode.length > 0)
			{
				logger.debug("批量执行策略入库：  成功");
			}
			else
			{
				logger.debug("批量执行策略入库：  失败");
			}
			logger.warn("立即执行，开始调用预处理...deviceId[{}],strategyId[{}]", deviceIds,
					stragetyIds);
			// 调用预读
			if (true == CreateObjectFactory.createPreProcess(String.valueOf(gw_type_int))
					.processOOBatch(stragetyIds))
			{
				logger.warn("预读完成");
				logger.debug("调用后台预读模块成功");
				return "1";
			}
			else
			{
				logger.warn("调用后台预读模块失败");
				return "-4";
			}
		}
		else
		{
			try
			{
				// 调用接口
				logger.warn("开始调用配置模块进行配量参数配置(deviceIds：{},serviceId：{},paramArr：{})",
						new Object[] { deviceIds, serviceId, paramArr });
				int ret = CreateObjectFactory.createPreProcess(gwType)
						.processDeviceStrategy(deviceIds, serviceId, paramArr);
				logger.warn("调用配置模块进行配量参数配置结果为(ret={})", new Object[] { ret });
				if (1 == ret)
				{
					logger.debug("调用后台预读模块成功");
					return "1";
				}
				else
				{
					logger.warn("调用后台预读模块失败");
					return "-4";
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.warn("Exception---" + e.getMessage());
				return "-4";
			}
		}
	}

	public static void main(String[] args)
	{
		//System.out.println(new DateTimeUtil("1970-01-01 "+"08:00:00").getLongTime());
		System.out.println(new DateTimeUtil("2020-04-14 09:23:53").getLongTime());
	}

	/**
	 * 添加任务
	 * @param template_id
	 * @param queryType
	 * @param task_name
	 * @param device_id
	 * @param file_name
	 * @param vendor
	 * @param model
	 * @param devicetype_id
	 * @param isBind
	 * @param type 触发方式
	 * @return
	 */
	public int addTask(String template_id, String queryType,String task_name,String device_id,String file_name,String city_id, String vendor,String model,String devicetype_id,String isBind,String type,
		long id,String starttime,String endtime,String donow){
		long start_time = 0L;
		long end_time = 86400L;
		if(!StringUtil.IsEmpty(starttime)){
			//start_time = new DateTimeUtil("1970-01-01 "+starttime).getLongTime() + 28800;
			start_time = new DateTimeUtil(starttime).getLongTime();
		}
		if(!StringUtil.IsEmpty(endtime)){
			//end_time = new DateTimeUtil("1970-01-01 "+endtime).getLongTime() + 28800;
			end_time = new DateTimeUtil(endtime).getLongTime();
		}

		long time = System.currentTimeMillis() / 1000L;
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into tab_batchSetTemplate_task(template_id,task_id,task_name,add_time,task_status,queryType");
		if("1".equals(queryType)){
			psql.append(",device_id,type,acc_oid,start_time,end_time,donow) values(?,?,?,?,?,?,?,?,?,?,?,?)");
			psql.setLong(1, StringUtil.getLongValue(template_id));
			psql.setLong(2, time);
			psql.setString(3, task_name);
			psql.setLong(4, time);
			psql.setInt(5, 0);
			psql.setString(6, queryType);
			psql.setString(7, device_id);
			psql.setInt(8, StringUtil.getIntegerValue(type));
			psql.setLong(9, id);
			psql.setLong(10, start_time);
			psql.setLong(11, end_time);
			psql.setString(12, donow);
		}
		else if("3".equals(queryType)){
			psql.append(",file_name,type,acc_oid,start_time,end_time,donow) values(?,?,?,?,?,?,?,?,?,?,?,?)");
			psql.setLong(1, StringUtil.getLongValue(template_id));
			psql.setLong(2, time);
			psql.setString(3, task_name);
			psql.setLong(4, time);
			psql.setInt(5, 0);
			psql.setString(6, queryType);
			psql.setString(7, file_name);
			psql.setInt(8, StringUtil.getIntegerValue(type));
			psql.setLong(9, id);
			psql.setLong(10, start_time);
			psql.setLong(11, end_time);
			psql.setString(12, donow);
		}
		else if("2".equals(queryType)){
			psql.append(",vendor_id,device_model_id,devicetype_id,isBind,city_id,type,acc_oid,start_time,end_time,donow) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			psql.setLong(1, StringUtil.getLongValue(template_id));
			psql.setLong(2, time);
			psql.setString(3, task_name);
			psql.setLong(4, time);
			psql.setInt(5, 0);
			psql.setString(6, queryType);
			psql.setString(7, vendor);
			psql.setString(8, model);
			if("-1".equals(devicetype_id)){
				psql.setLong(9, -1);
			}
			else{
				psql.setLong(9, StringUtil.getIntegerValue(devicetype_id));
			}
			psql.setString(10, isBind);
			psql.setString(11, city_id);
			psql.setInt(12, StringUtil.getIntegerValue(type));
			psql.setLong(13, id);
			psql.setLong(14, start_time);
			psql.setLong(15, end_time);
			psql.setString(16, donow);
		}
		return DBOperation.executeUpdate(psql.getSQL());
	}



	public int addTaskStb(String template_id, String queryType,String task_name,String device_id,String file_name,String city_id, String vendor,String model,String devicetype_id,String isBind,String type,
			long id,String starttime,String endtime,String donow){
			long start_time = 0L;
			long end_time = 86400L;
			if(!StringUtil.IsEmpty(starttime)){
				//start_time = new DateTimeUtil("1970-01-01 "+starttime).getLongTime() + 28800;
				start_time = new DateTimeUtil(starttime).getLongTime();
			}
			if(!StringUtil.IsEmpty(endtime)){
				//end_time = new DateTimeUtil("1970-01-01 "+endtime).getLongTime() + 28800;
				end_time = new DateTimeUtil(endtime).getLongTime();
			}

			long time = System.currentTimeMillis() / 1000L;
			PrepareSQL psql = new PrepareSQL();
			psql.append("insert into stb_tab_batchSetTemplate_task(template_id,task_id,task_name,add_time,task_status,queryType");
			if("1".equals(queryType)){
				psql.append(",device_id,type,acc_oid,start_time,end_time,donow) values(?,?,?,?,?,?,?,?,?,?,?,?)");
				psql.setLong(1, StringUtil.getLongValue(template_id));
				psql.setLong(2, time);
				psql.setString(3, task_name);
				psql.setLong(4, time);
				psql.setInt(5, 0);
				psql.setString(6, queryType);
				psql.setString(7, device_id);
				psql.setInt(8, StringUtil.getIntegerValue(type));
				psql.setLong(9, id);
				psql.setLong(10, start_time);
				psql.setLong(11, end_time);
				psql.setString(12, donow);
			}
			else if("3".equals(queryType)){
				psql.append(",file_name,type,acc_oid,start_time,end_time,donow) values(?,?,?,?,?,?,?,?,?,?,?,?)");
				psql.setLong(1, StringUtil.getLongValue(template_id));
				psql.setLong(2, time);
				psql.setString(3, task_name);
				psql.setLong(4, time);
				psql.setInt(5, 0);
				psql.setString(6, queryType);
				psql.setString(7, file_name);
				psql.setInt(8, StringUtil.getIntegerValue(type));
				psql.setLong(9, id);
				psql.setLong(10, start_time);
				psql.setLong(11, end_time);
				psql.setString(12, donow);
			}
			else if("2".equals(queryType)){
				psql.append(",vendor_id,device_model_id,devicetype_id,isBind,city_id,type,acc_oid,start_time,end_time,donow) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				psql.setLong(1, StringUtil.getLongValue(template_id));
				psql.setLong(2, time);
				psql.setString(3, task_name);
				psql.setLong(4, time);
				psql.setInt(5, 0);
				psql.setString(6, queryType);
				psql.setString(7, vendor);
				psql.setString(8, model);
				if("-1".equals(devicetype_id)){
					psql.setLong(9, -1);
				}
				else{
					psql.setLong(9, StringUtil.getIntegerValue(devicetype_id));
				}
				psql.setString(10, isBind);
				psql.setString(11, city_id);
				psql.setInt(12, StringUtil.getIntegerValue(type));
				psql.setLong(13, id);
				psql.setLong(14, start_time);
				psql.setLong(15, end_time);
				psql.setString(16, donow);
			}
			return DBOperation.executeUpdate(psql.getSQL());
		}

	/*
	 *
	 * 新疆机顶盒批量配置参数
	 */
	public String doConfigAllStb(String[] deviceIds, String serviceId, String[] paramArr,
			String gwType)
	{
		logger.debug("serv-->doConfigAllStb({},{},{},{})", new Object[] { deviceIds,
				serviceId, paramArr, gwType });
		String devices = Arrays.toString(deviceIds).replace("[", "").replace("]", "");
		String param = Arrays.toString(paramArr).replace("[", "").replace("]", "");
		long time = System.currentTimeMillis() / 1000L;
		if (devices.indexOf("select") != -1)
		{
			PrepareSQL psql = new PrepareSQL();
			psql.append("insert into stb_tab_batchconfig_task(task_id,service_id,query_sql,");
			psql.append("param,gw_type,add_time,task_status) ");
			psql.append("values(?,?,?,?,?,?,?) ");
			psql.setLong(1, time);
			psql.setInt(2, Integer.parseInt(serviceId));
			psql.setString(3, devices);
			psql.setString(4, param);
			psql.setInt(5, Integer.parseInt(gwType));
			psql.setLong(6, time);
			psql.setInt(7, 0);
			DataSetBean.executeUpdate(psql.getSQL());
			logger.warn("获取未执行的任务");
			List<HashMap<String, String>> unDoneTaskList = dao.queryUnDoneTask();
			if (null == unDoneTaskList || unDoneTaskList.isEmpty())
			{
				return "-4";
			}
			dao.init1();
			logger.warn("未执行完的任务[{}]", unDoneTaskList.size());
			for (HashMap<String, String> map : unDoneTaskList)
			{
				ConfigOBJ obj = new ConfigOBJ().joinObj(map);
				dao.updateStatus(obj.getTask_id());
				List<HashMap<String, String>> deviceIdsNew = dao.queryDevices(obj
						.getQuery_sql());
				if (null == deviceIdsNew || deviceIdsNew.isEmpty())
				{
					logger.warn("[{}]-查无设备！", obj.getTask_id());
					continue;
				}
				dao.insertDev(deviceIdsNew, obj.getTask_id());
				String sql = "select device_id from stb_tab_batchconfig_dev where task_id="
						+ obj.getTask_id();
				logger.warn(
						"{}-调用配置模块[{}],[{}],[{}],[{}]",
						new Object[] { obj.getTask_id(), obj.getGw_type(), sql,
								obj.getService_id(), obj.getParam() });
				String[] paramArrNew = { obj.getParam() };
				try
				{
					// 调用接口
					int res = CreateObjectFactory.createPreProcess("" + obj.getGw_type())
							.processDeviceStrategy(new String[] { sql },
									"" + obj.getService_id(), paramArrNew);
					logger.warn("{}-调配置模块，结果:{}", obj.getTask_id(), res);
					if (1 == res)
					{
						logger.debug("调用后台预读模块成功");
						return "1";
					}
					else
					{
						logger.warn("调用后台预读模块失败");
						return "-4";
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					logger.warn("Exception---" + e.getMessage());
					return "-4";
				}
				/*
				 * //调用配置模块 int
				 * res=CreateObjectFactory.createPreProcess(""+obj.getGw_type()).
				 * processDeviceStrategy(new
				 * String[]{sql},""+obj.getService_id(),paramArrNew);
				 * logger.warn("{}-调配置模块，结果:{}",obj.getTask_id(),res); if (1==res){
				 * logger.debug("调用后台预读模块成功"); return "1"; }else{
				 * logger.warn("调用后台预读模块失败"); return "-4"; } if(1!=res){ try {
				 * Thread.sleep(2000L); } catch (InterruptedException e) {
				 * e.printStackTrace(); }
				 * CreateObjectFactory.createPreProcess(""+obj.getGw_type()).
				 * processDeviceStrategy(new
				 * String[]{sql},""+obj.getService_id(),paramArrNew);
				 * logger.warn("{}-第二次调配置模块，结果:{}",obj.getTask_id(),res); } }
				 *
				 *
				 *
				 *
				 *
				 * if(DataSetBean.executeUpdate(psql.getSQL())==1){ return "1"; }else{
				 * return "-4"; }
				 */
			}
			return "-4";
		}
		else
		{
			try
			{
				// 调用接口
				if(LipossGlobals.inArea(Global.SDLT)){
					logger.warn("开始调用配置模块进行配量参数配置(serviceId：{},paramArr：{})",
							new Object[] {serviceId, paramArr });
				}else{
					logger.warn("开始调用配置模块进行配量参数配置(deviceIds：{},serviceId：{},paramArr：{})",
							new Object[] { deviceIds, serviceId, paramArr });
				}

				int ret = CreateObjectFactory.createPreProcess(gwType)
						.processDeviceStrategy(deviceIds, serviceId, paramArr);
				logger.warn("调用配置模块进行配量参数配置结果为(ret={})", new Object[] { ret });
				if (1 == ret)
				{
					logger.debug("调用后台预读模块成功");
					return "1";
				}
				else
				{
					logger.warn("调用后台预读模块失败");
					return "-4";
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.warn("Exception---" + e.getMessage());
				return "-4";
			}
		}
	}

	public String paramNodeBXml(String[] paramArr)
	{
		String strXml = null;
		// new root
		Document doc = DocumentHelper.createDocument();
		// root node
		Element root = doc.addElement("parameterConfig");
		Element parameterList = root.addElement("parameterList");
		for (int i = 0; i < paramArr.length; i++)
		{
			String parameterStr = paramArr[i];
			if (parameterStr == null || parameterStr.split("ailk!@#").length != 3)
			{
				logger.warn("[{}]批量配置参数的参数不正确,结束");
				logger.error("[{}]批量配置参数的参数不正确,结束");
				return null;
			}
			else
			{
				String[] parameterArr = parameterStr.split("ailk!@#");
				Element parameterEle = parameterList.addElement("parameter");
				parameterEle.addElement("name").addText(parameterArr[0]);
				parameterEle.addElement("value").addText(parameterArr[1]);
				parameterEle.addElement("type").addText(parameterArr[2]);
			}
		}
		strXml = root.asXML();
		return strXml;
	}
	/*
	 *
	 * 机顶盒批量配置参数转换
	 */
	public String paramNodeBXmlStb(String[] paramArr)
	{
		String strXml = null;
		// new root
		Document doc = DocumentHelper.createDocument();
		// root node
		Element root = doc.addElement("STB");
		Element parameterList = root.addElement("parameterList");
		for (int i = 0; i < paramArr.length; i++)
		{
			String parameterStr = paramArr[i];
			if (parameterStr == null || parameterStr.split("ailk!@#").length != 3)
			{
				logger.warn("[{}]批量配置参数的参数不正确,结束");
				logger.error("[{}]批量配置参数的参数不正确,结束");
				return null;
			}
			else
			{
				String[] parameterArr = parameterStr.split("ailk!@#");
				Element parameterEle = parameterList.addElement("parameter");
				parameterEle.addElement("name").addText(parameterArr[0]);
				parameterEle.addElement("value").addText(parameterArr[1]);
				parameterEle.addElement("type").addText(parameterArr[2]);
			}
		}
		strXml = root.asXML();
		return strXml;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String parNodeBatch(long accOid, String deviceIds, String gwType,
			String pathvalue, String paramvalues, String filename, String city_ids,
			String usernames)
	{
		String res = "1";
		long taskId = curMillAndRan(3);
		long addTime = System.currentTimeMillis() / 1000;
		int operType = 5;
		int servType = 116;
		int operStatus = 0;
		int strategyType = 1;
		int doStatus = 0;
		dao.addBatParamTask(taskId, accOid, operType, servType, operStatus,
				Integer.valueOf(gwType), addTime, pathvalue, filename);
		Map map = null;
		List<String> deviceList = null;
		List<String> paramList = null;
		List<String> cityIdList = null;
		List<String> usernameList = new ArrayList<String>();
		if (deviceIds.contains(","))
		{ // 多个设备
			String[] deviceArr = deviceIds.split(",");
			deviceList = Arrays.asList(deviceArr);
			String[] paramArr = paramvalues.split(",");
			paramList = Arrays.asList(paramArr);
			String[] cityIdsArr = city_ids.split(",");
			cityIdList = Arrays.asList(cityIdsArr);
			String[] usernameArr = usernames.split("\\*");
			String[] dn = usernameArr[0].split(",");
			String[] loid = usernameArr[1].split(",");
			for (int i = 0; i < dn.length; i++)
			{
				usernameList.add(dn[i] + "-" + loid[i]);
			}
		}
		else
		{ // 单个设备
			deviceList = new ArrayList<String>();
			paramList = new ArrayList<String>();
			cityIdList = new ArrayList<String>();
			usernameList = new ArrayList<String>();
			deviceList.add(deviceIds);
			paramList.add(paramvalues);
			cityIdList.add(city_ids);
			String new_username = usernames.replaceAll("\\*", "\\,");
			usernameList.add(new_username);
		}
		List<Map> list = new ArrayList<Map>();
		for (int i = 0; i < deviceList.size(); i++)
		{
			map = new HashMap();
			String deivce_id = (String) deviceList.get(i);
			String paramvalue = (String) paramList.get(i);
			String city_id = (String) cityIdList.get(i);
			String username = (String) usernameList.get(i);
			map.put("task_id", taskId);
			map.put("device_id", deivce_id);
			map.put("service_id", servType);
			map.put("strategy_type", strategyType);
			map.put("add_time", addTime);
			map.put("do_status", doStatus);
			map.put("paramvalue", paramvalue);
			map.put("city_id", city_id);
			map.put("username", username);
			list.add(map);
		}
		int[] resultArr = dao.addBatParamDev(list);
		for (int result : resultArr)
		{
			if (result != 1)
			{
				res = "0";
				break;
			}
		}
		return res;
	}

	/***
	 * 下载模板文件
	 *
	 * @param filepath
	 * @param response
	 */
	public void download(String filepath, HttpServletResponse response)
	{
		logger.debug("download({},{})", new Object[] { filepath, response });
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
			response.addHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(filename, "UTF-8"));
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

	public int queryCustomNum()
	{
		logger.debug("queryUndoNum()");
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		long todayTimeMillis = System.currentTimeMillis() / 1000
				- (long) (hour * 60 * 60 + minute * 60 + second);
		return dao.queryCustomNum(todayTimeMillis);
	}

	@SuppressWarnings("rawtypes")
	public void saveListToTxt(List<Map> list, String strFileName)
	{
		OutputStreamWriter outFile = null;
		FileOutputStream fileName=null;
		String strItems = null;
		try
		{
			fileName = new FileOutputStream(strFileName);
			outFile = new OutputStreamWriter(fileName);
			// 标题行
			outFile.write("属地,设备序列号,用户LOID,配置节点名称,配置节点值,配置时间\r\n");
			for (Map map : list)
			{
				strItems = map.get("city_id") + "," + map.get("username") + ","
						+ map.get("pathvalue") + "," + map.get("paramvalue") + ","
						+ map.get("add_time") + "\r\n";
				outFile.write(strItems);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(outFile!=null){
					outFile.flush();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			try
			{
				if(outFile!=null){
					outFile.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			try
			{
				if(fileName!=null){
					fileName.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
		}
	}

	public String deleteFile(String file_path)
	{
		logger.debug("deleteFile({})", file_path);
		if (StringUtil.IsEmpty(file_path))
		{
			return "0";
		}
		File file = new File(file_path);
		boolean result = true;
		if (file.exists())
		{
			result = file.delete();
		}
		return result ? "1" : "0"; // 1成功,0失败
	}

	public int getParNodeCount(int curPage_splitPage, int num_splitPage, String customId,
			String fileName, String starttime, String endtime)
	{
		return dao.getParNodeCount(curPage_splitPage, num_splitPage, customId, fileName,
				starttime, endtime);
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getParNodeList(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime)
	{
		return dao.getParNodeList(curPage_splitPage, num_splitPage, customId, fileName,
				starttime, endtime);
	}

	public static long curMillAndRan(int ranLen)
	{
		if (ranLen < 1)
			return 0;
		String curMill = Long.toString(System.currentTimeMillis() / 1000);
		String ran = Long.toString(Math.round((Math.random() * 9 + 1)
				* Math.pow(10, ranLen - 1)));
		return Long.parseLong(curMill + ran);
	}

	public int checkRepeatName(String checkRepeatname)
	{
		return dao.queryRepeatName(checkRepeatname);
	}

	public int queryUndoNum()
	{
		logger.debug("queryUndoNum()");
		return dao.queryUndoNum();
	}


	public String getPreResult(String deviceId,String gw_type)
	{
		// TODO Auto-generated method stub
		ACSCorba acsCorba = new ACSCorba(gw_type);
		logger.warn("走ijk采集", deviceId);
		String wanConnPath = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		String wanServiceList = ".X_CT-COM_ServiceList";
		String wanPPPConnection = ".WANPPPConnection.";
		String wanIPConnection = ".WANIPConnection.";
		String wanMulticastVlan  = "X_CT-COM_MulticastVlan";
		String wanLanInterface   = "X_CT-COM_LanInterface-DHCPEnable";
		String j1 = "";
		String k1 = "";
		String result = "";
		ArrayList<String> wanConnPathsList = null;
		wanConnPathsList =acsCorba.getParamNamesPath(deviceId, wanConnPath, 0);
		if (wanConnPathsList == null || wanConnPathsList.size() == 0
				|| wanConnPathsList.isEmpty())
		{
			logger.warn("[{}]获取WANConnectionDevice下所有节点路径失败，逐层获取", deviceId);
			wanConnPathsList = new ArrayList<String>();
			List<String> jList =acsCorba.getIList(deviceId, wanConnPath);
			if (null == jList || jList.size() == 0 || jList.isEmpty())
			{
				logger.warn("[{}]获取" + wanConnPath + "下实例号失败", deviceId);
			}
			else
			{
				for (String j : jList)
				{
					List<String> kPPPList = acsCorba.getIList(deviceId, wanConnPath
							+ j + wanPPPConnection);
					if (null == kPPPList || kPPPList.size() == 0 || kPPPList.isEmpty())
					{
						logger.warn("[{}]获取" + wanConnPath + wanConnPath + j
								+ wanPPPConnection + "下实例号失败", deviceId);
					}
					else
					{
						for (String kppp : kPPPList)
						{
							wanConnPathsList.add(wanConnPath + j + wanPPPConnection
									+ kppp + wanServiceList);
						}
					}
				}
			}
		}
		ArrayList<String> serviceListList = new ArrayList<String>();
		ArrayList<String> paramNameList = new ArrayList<String>();
		for (int i = 0; i < wanConnPathsList.size(); i++)
		{
			String namepath = wanConnPathsList.get(i);
			if (namepath.indexOf(wanServiceList) >= 0)
			{
				serviceListList.add(namepath);
				paramNameList.add(namepath);
				continue;
			}
		}
		if (serviceListList.size() == 0 || serviceListList.isEmpty())
		{
			logger.warn("[{}]不存在WANIP下的X_CT-COM_ServiceList节点，返回", deviceId);
			return result;
		}
		else
		{
			String[] paramNameArr = new String[paramNameList.size()];
			int arri = 0;
			for (String paramName : paramNameList)
			{
				paramNameArr[arri] = paramName;
				arri = arri + 1;
			}
			Map<String, String> paramValueMap = new HashMap<String, String>();
			for (int k = 0; k < (paramNameArr.length / 20) + 1; k++)
			{
				String[] paramNametemp = new String[paramNameArr.length - (k * 20) > 20 ? 20
						: paramNameArr.length - (k * 20)];
				for (int m = 0; m < paramNametemp.length; m++)
				{
					paramNametemp[m] = paramNameArr[k * 20 + m];
				}
				Map<String, String> maptemp = acsCorba.getParaValueMap(deviceId,
						paramNametemp);
				if (maptemp != null && !maptemp.isEmpty())
				{
					paramValueMap.putAll(maptemp);
				}
				logger.warn("获取节点值...");
				logger.warn("k : " + k);
			}
			if (paramValueMap.isEmpty())
			{
				logger.warn("[{}]获取ServiceList失败", deviceId);
				return result;
			}
			for (Map.Entry<String, String> entry : paramValueMap.entrySet())
			{
				logger.debug("[{}]{}={} ",
						new Object[] { deviceId, entry.getKey(), entry.getValue() });
				String paramName = entry.getKey();
				if (paramName.indexOf(wanPPPConnection) >= 0)
				{
				}
				else if (paramName.indexOf(wanIPConnection) >= 0)
				{
					continue;
				}
				if (paramName.indexOf(wanServiceList) >= 0)
				{
					if (!StringUtil.IsEmpty(entry.getValue())
							&& entry.getValue().indexOf("OTHER") >= 0)
					{
						logger.warn("param path is .." + entry.getKey());
						String res = entry.getKey().substring(0,
								entry.getKey().indexOf("X_CT-COM_ServiceList"));
						try
						{
						    j1 = res.split("WANConnectionDevice.")[1]
									.split(".WANPPPConnection")[0];
							k1 = res.split("WANPPPConnection.")[1].split("\\.")[0];
							logger.warn("j is : " + j1 + " k is : " + k1);
							break;
						}
						catch (Exception e)
						{
						}
					}
				}
			}
		}
		String gatherPath = wanConnPath + j1 +wanPPPConnection + k1 +".";
		ArrayList<ParameValueOBJ> objLlist = acsCorba.getValue(deviceId, gatherPath);
        if ((null == objLlist) || (objLlist.isEmpty()))
        {
          logger.warn("[{}]获取objLlist失败，返回", deviceId);
          return result;
        }
        logger.warn("[{}]获取objLlist成功，objLlist.size={}", deviceId, Integer.valueOf(objLlist.size()));
        for (ParameValueOBJ pvobj : objLlist)
        {

        	if(pvobj.getName().indexOf(wanMulticastVlan) >= 0)
        	{
        		result = result + gatherPath + wanMulticastVlan + ";" + pvobj.getValue() + ";" + pvobj.getType() + ";";
        	}
        	if(pvobj.getName().indexOf(wanLanInterface) >= 0)
        	{
        		result = result + gatherPath + wanLanInterface + ";" + pvobj.getValue()+ ";" + pvobj.getType() + ";";
        	}
        }
        logger.warn("预读采集结束，采集结果：" + result);
		return result;
	}

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;

	}


	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public Map getSession() {
		return session;
	}
}
