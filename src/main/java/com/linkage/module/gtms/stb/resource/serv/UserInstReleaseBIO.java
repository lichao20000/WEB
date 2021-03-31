/**
 * 
 */
package com.linkage.module.gtms.stb.resource.serv;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ResourceBind.BindInfo;
import ResourceBind.ResultInfo;
import ResourceBind.UnBindInfo;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.dao.UserInstReleaseDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.init.obj.CpeFaultcodeOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.ResourceBindInterface;
import com.linkage.module.gwms.util.StringUtil;


/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-11-5
 * @category com.linkage.module.gwms.resource.bio
 * 
 */
public class UserInstReleaseBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(UserInstReleaseBIO.class);

	UserInstReleaseDAO dao;

	/**
	 * @return the dao
	 */
	public UserInstReleaseDAO getDao() {
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(UserInstReleaseDAO dao) {
		this.dao = dao;
	}
	
	private Map<String, String> status_map = new HashMap<String, String>();
	public UserInstReleaseBIO()
	{
		status_map.put("0", "等待执行");
		status_map.put("1", "预读PVC");
		status_map.put("2", "预读绑定端");
		status_map.put("3", "预读无线");
		status_map.put("4", "业务下发");
		status_map.put("100", "执行完成");
	}

	/**
	 * 查询用户信息
	 * 
	 * @param cityId
	 *            必须
	 * @param username
	 *            |
	 * @param deviceSN
	 *            |username、deviceSN必须传一个，否则返回size为0的List实例
	 *            |安装时，传入username,deviceSN传入null |解绑时，传入deviceSN，username传入null
	 * @param servUsername
	 *            业务用户账号
	 * @param isNoBind
	 *            如果是查询未绑定，则传入true
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryUser(String cityId, String servAccount) {
		
		return dao.getUserInfo(cityId, servAccount);
	}
	
	/**
	 * 手工安装
	 * 
	 * @param customer_id 用户ID
	 * @param servAccount 业务账号
	 * @param deviceId 设备ID
	 * @param dealstaff 操作人
	 * @param userFlag 1:新装 3:修障
	 * @return 操作信息提示
	 */
	public String stbInst(String customer_id, String servAccount,
			String deviceId, String dealstaff, int userline) {
		logger.debug(
				"UserInstReleaseBIO=>itmsInst(customer_id:{},servAccount:{},deviceId:{},dealstaff:{})",
				new Object[] { customer_id, servAccount, deviceId, dealstaff });
		
		String msg = "";
		ResourceBindInterface corba = CreateObjectFactory.createResourceBind(Global.GW_TYPE_STB);
		
		BindInfo[] arr = new BindInfo[1];
		arr[0] = new BindInfo();
		arr[0].accOid = customer_id;
		arr[0].accName = dealstaff;
		arr[0].username = servAccount;
		arr[0].deviceId = deviceId;
		arr[0].userline = userline;
		
		ResultInfo rs = corba.bind(arr);
		if(rs == null)
		{
			msg = "绑定失败，系统内部错误";
		}
		else
		{
			//String status = rs.status;
			// 成功
			if(rs.resultId[0].equals("1"))
			{
				msg = "绑定" + Global.G_ResourceBind_statusCode.get(Integer.parseInt(rs.resultId[0]));
				return msg;
			}
			// 失败
			else
			{
				// 获取相关错误码
				msg = "绑定失败，" + Global.G_ResourceBind_resultCode.get(Integer.parseInt(rs.resultId[0]));
			}
		}
		return msg;
	}

	/**
	 * 解绑
	 * 
	 * @param customer_id
	 * @param deviceId
	 * @param dealstaff
	 * @param userline
	 * @return
	 */
	public String stbRelease(String customer_id,String deviceId, String dealstaff,int userline) {
		logger.debug("itmsRelease(userId:{};deviceId:{};dealstaff:{},userline:{})",
				new Object[]{customer_id,deviceId,dealstaff,userline});
		
		String msg = "";
		ResourceBindInterface corba = CreateObjectFactory.createResourceBind(Global.GW_TYPE_STB);
		
		UnBindInfo[] arr = new UnBindInfo[1];
		arr[0] = new UnBindInfo();
		arr[0].accOid = customer_id;
		arr[0].accName = dealstaff;
		arr[0].userId = customer_id;
		arr[0].deviceId = deviceId;
		arr[0].userline = userline;
		
		ResultInfo rs = corba.release(arr);
		if(rs == null)
		{
			msg = "解绑失败，系统内部错误";
		}
		else
		{
			//String status = rs.status;
			// 成功
			if(rs.resultId[0].equals("1"))
			{
				msg = "解绑" + Global.G_ResourceBind_statusCode.get(Integer.parseInt(rs.resultId[0]));
				return msg;
			}
			// 失败
			else
			{
				// 获取相关错误码
				msg = "解绑失败，" + Global.G_ResourceBind_resultCode.get(Integer.parseInt(rs.resultId[0]));
			}
		}
		
		return msg;
	}
	
	public String callPreProcess(String customer_id, String device_id, String oui, String deviceSn){
		logger.warn("inter_ PreProcess (customerId:{}, deviceId:{}, oui:{}, deviceSN:{}",
				new Object[] { customer_id, device_id, oui, deviceSn});
		//更新用户表业务开通状态
		dao.updateServOpenStatus(customer_id);
		int result = processService4STB(customer_id,device_id,oui,deviceSn);
		
		return StringUtil.getStringValue(result);
	}
	
	/**
	 * 业务下发后参数展示
	 * 
	 * @param deviceId
	 * @return String
	 */
	public List<Map<String,String>> serviceDoneList(String device_id){
		
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		List<HashMap<String,String>> mapList = dao.getBindUserByDeviceId(device_id);
		if(mapList.size() > 0 && mapList != null){
			Map<String,String> returnMap = mapList.get(0);
			returnMap = mapList.get(0);
			returnMap.put("cityName", CityDAO.getCityName(StringUtil.getStringValue(returnMap,"city_id")));
			returnMap.put("userStatus", transUserStatus(StringUtil.getStringValue(returnMap,"user_status")));
			returnMap.put("openUserdate",transDate(StringUtil.getStringValue(returnMap,"openuserdate")));
			returnList.add(returnMap);
		}
		return returnList;
	}
	
	/**
	 * 调用配置模块下发业务
	 * 
	 * @param customer_id
	 * @param deviceId
	 * @param oui
	 * @param deviceSn
	 * @return int
	 */
	public int processService4STB(String customer_id, String deviceId,
			String oui, String deviceSn)
	{
		StringBuffer xmlSB = new StringBuffer();
		xmlSB.append("<ServXml><servList><serv>");
		xmlSB.append("<userId>").append(String.valueOf(customer_id)).append("</userId>");
		xmlSB.append("<deviceId>").append(deviceId).append("</deviceId>");
		xmlSB.append("<serviceId>").append("120").append("</serviceId>");
		xmlSB.append("<oui>").append(oui).append("</oui>");
		xmlSB.append("<deviceSn>").append(deviceSn).append("</deviceSn>");
		xmlSB.append("</serv></servList></ServXml>");
		// 调配置模块下发业务
		logger.warn("[zerocfg-{}] 调配置模块下发业务", new Object[] { deviceId });
		
		int result = CreateObjectFactory.createPreProcess("4").processServiceInterface_STB(xmlSB.toString());
		return result;
	}
	
	private static String transUserStatus(String userStatus)
	{
		if ("-1".equals(userStatus))
		{
			return "失败";
		}
		else if ("1".equals(userStatus))
		{
			return "成功";
		}
		return "未做";
	}
	
	
	public List<HashMap<String, String>> getConfigInfoLogSXLT(String device_id, String id) {
		List<HashMap<String,String>> configList = new ArrayList<HashMap<String,String>>();
		if(!StringUtil.IsEmpty(id)){
			configList = dao.getConfigInfoLogSXLT(device_id, id);
		}
		else configList = dao.getConfigInfoLogSXLT(device_id);
		Map<String, String> serviceCodeMap = dao.getServiceCode();
		if(configList != null && configList.size() > 0 && configList.get(0) != null ){
			for(int i=0;i<configList.size();i++){
				Map<String,String> tmp = configList.get(i);
				tmp.put("serviceName", serviceCodeMap.get(StringUtil.getStringValue(tmp, "service_id")));
				tmp.put("start_time", transDate(StringUtil.getStringValue(tmp, "start_time")));
				tmp.put("end_time", transDate(StringUtil.getStringValue(tmp, "end_time")));
				tmp.put("status", status_map.get(StringUtil.getStringValue(tmp, "status")));
				CpeFaultcodeOBJ  obj = Global.G_Fault_Map.get(StringUtil.getIntValue(tmp, "result_id"));
				if (obj != null) {
					tmp.put("fault_path", StringUtil.getStringValue(tmp, "result_desc"));
					tmp.put("result_desc", obj.getFaultDesc());
					tmp.put("fault_desc", obj.getFaultDesc());
					tmp.put("fault_reason", obj.getFaultReason());
				}
				else {
					tmp.put("fault_path", "");
					tmp.put("result_desc", "");
					tmp.put("fault_desc", "");
					tmp.put("fault_reason", "");
				}
				tmp.put("device_id", device_id);
				
				String sheet_para = StringUtil.getStringValue(tmp, "sheet_para");
				String encoding = "UTF-8";
				Document doc;
				try {
					doc = DocumentHelper.parseText(sheet_para);
					StringWriter writer = new StringWriter();
					OutputFormat format = OutputFormat.createPrettyPrint();
					format.setIndent(true);
					format.setEncoding(encoding);
					XMLWriter xmlwriter = new XMLWriter(writer, format);
					xmlwriter.write(doc);
					sheet_para = writer.toString();
					// 有宽带密码、语音密码、用户姓名、地址等信息的页面，信息需要屏蔽
					if (LipossGlobals.inArea(Global.JSDX))
					{
						if (sheet_para.indexOf("<Password>") > 0)
						{
							String password = sheet_para.substring(
									sheet_para.indexOf("<Password>"),
									sheet_para.indexOf("</Password>"));
							sheet_para = sheet_para.replace(password, "<Password>");
							logger.warn("Password:" + password);
						}
						if (sheet_para.indexOf("<AuthPassword>") > 0)
						{
							String authPassword = sheet_para.substring(
									sheet_para.indexOf("<AuthPassword>"),
									sheet_para.indexOf("</AuthPassword>"));
							logger.warn("AuthPassword:" + authPassword);
							sheet_para = sheet_para.replace(authPassword,
									"<AuthPassword>");
						}
					}
					sheet_para = sheet_para.replace("&", "&amp;");
					sheet_para = sheet_para.replace("<", "&lt;");
					sheet_para = sheet_para.replace(">", "&gt;");
					sheet_para = sheet_para.replace("\r\n", "\n");
					sheet_para = sheet_para.replace("\n", "<br>\n");
					sheet_para = sheet_para.replace("\t", "    ");
					sheet_para = sheet_para.replace("  ", " &nbsp;");
					logger.warn("sheet_para== [" + sheet_para + "");
					tmp.put("sheet_para", sheet_para);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					tmp.put("sheet_para", "");
				}
			}
		}
		return configList;
	}
	
	public List<HashMap<String, String>> getConfigInfo(String device_id) {
		List<HashMap<String,String>> configList = dao.getConfigInfo(device_id);
		Map<String, String> serviceCodeMap = dao.getServiceCode();
		if(configList != null && configList.size() > 0 && configList.get(0) != null ){
			Map<String,String> tmp = configList.get(0);
			tmp.put("serviceName", serviceCodeMap.get(StringUtil.getStringValue(tmp, "service_id")));
			tmp.put("start_time", transDate(StringUtil.getStringValue(tmp, "start_time")));
			tmp.put("end_time", transDate(StringUtil.getStringValue(tmp, "end_time")));
			tmp.put("status", status_map.get(StringUtil.getStringValue(tmp, "status")));
			CpeFaultcodeOBJ  obj = Global.G_Fault_Map.get(StringUtil.getIntValue(tmp, "result_id"));
			if (obj != null) {
				tmp.put("fault_path", StringUtil.getStringValue(tmp, "result_desc"));
				tmp.put("result_desc", obj.getFaultDesc());
				tmp.put("fault_desc", obj.getFaultDesc());
				tmp.put("fault_reason", obj.getFaultReason());
			}
			else {
				tmp.put("fault_path", "");
				tmp.put("result_desc", "");
				tmp.put("fault_desc", "");
				tmp.put("fault_reason", "");
			}
			tmp.put("device_id", device_id);
			
			String sheet_para = StringUtil.getStringValue(tmp, "sheet_para");
			String encoding = "UTF-8";
			Document doc;
			try {
				doc = DocumentHelper.parseText(sheet_para);
				StringWriter writer = new StringWriter();
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setIndent(true);
				format.setEncoding(encoding);
				XMLWriter xmlwriter = new XMLWriter(writer, format);
				xmlwriter.write(doc);
				sheet_para = writer.toString();
				// 有宽带密码、语音密码、用户姓名、地址等信息的页面，信息需要屏蔽
				if (LipossGlobals.inArea(Global.JSDX))
				{
					if (sheet_para.indexOf("<Password>") > 0)
					{
						String password = sheet_para.substring(
								sheet_para.indexOf("<Password>"),
								sheet_para.indexOf("</Password>"));
						sheet_para = sheet_para.replace(password, "<Password>");
						logger.warn("Password:" + password);
					}
					if (sheet_para.indexOf("<AuthPassword>") > 0)
					{
						String authPassword = sheet_para.substring(
								sheet_para.indexOf("<AuthPassword>"),
								sheet_para.indexOf("</AuthPassword>"));
						logger.warn("AuthPassword:" + authPassword);
						sheet_para = sheet_para.replace(authPassword,
								"<AuthPassword>");
					}
				}
				sheet_para = sheet_para.replace("&", "&amp;");
				sheet_para = sheet_para.replace("<", "&lt;");
				sheet_para = sheet_para.replace(">", "&gt;");
				sheet_para = sheet_para.replace("\r\n", "\n");
				sheet_para = sheet_para.replace("\n", "<br>\n");
				sheet_para = sheet_para.replace("\t", "    ");
				sheet_para = sheet_para.replace("  ", " &nbsp;");
				logger.warn("sheet_para== [" + sheet_para + "");
				tmp.put("sheet_para", sheet_para);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				tmp.put("sheet_para", "");
			}
		}
		return configList;
	}
	
	public ArrayList<HashMap<String, String>> getBssSheet(String customer_id) {
		return dao.getBssSheet(customer_id);
	}

	private static final String transDate(Object seconds)
	{
		if (seconds != null)
		{
			try
			{
				DateTimeUtil dt = new DateTimeUtil(
						Long.parseLong(seconds.toString()) * 1000);
				return dt.getLongDate();
			}
			catch (NumberFormatException e)
			{
				logger.error(e.getMessage(), e);
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}
}
