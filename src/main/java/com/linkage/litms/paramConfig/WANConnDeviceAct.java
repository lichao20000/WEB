
package com.linkage.litms.paramConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.ParameterInfoStruct;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.service.GetParameterNames;
import com.linkage.litms.acs.soap.service.GetParameterNamesResponse;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.GetParameterValuesResponse;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * 创建WANConnection和PPPConnection及其设置参数操作类
 * 
 * @author hemc
 * @time 2007-06-21
 */
public class WANConnDeviceAct
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(WANConnDeviceAct.class);
	private static String RootWANConnection = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
	private static String PPPOEConnection = "WANPPPConnection.";
	private static String WANDSLLinkConfig = "WANDSLLinkConfig.";
	/**
	 * 存放device_id对应的设备属性{device_serialnumber oui loopback_ip port path acs_username
	 * acs_passwd}
	 */
	private static Map deviceAttributeMap = new HashMap();
	private static DevRPCManager devRPCManager = null;

	/**
	 * 创建WANConnection
	 * 
	 * @param device_id
	 * @param gather_id
	 * @param param
	 * @param ior
	 * @return
	 */
	public static boolean createWANConnection(String device_id, String gather_id,
			String ior)
	{
		return createConnection(device_id, gather_id, RootWANConnection, ior);
	}

	/**
	 * 创建PPPConnection
	 * 
	 * @param device_id
	 * @param gather_id
	 * @param strPath
	 *            用户选择了某个WANConnction，以便创建PPPConnection
	 * @param ior
	 * @return 成功：返回路径 失败：null
	 */
	public static boolean createPPPConnection(String device_id, String gather_id,
			String ior, String strPath)
	{
		logger.debug("获取路径：" + strPath);
		return createConnection(device_id, gather_id, strPath, ior);
	}

	/**
	 * 创建参数实例
	 * 
	 * @param device_id
	 * @param gather_id
	 * @param param
	 * @param ior
	 * @return
	 */
	private static boolean createConnection(String device_id, String gather_id,
			String param, String ior)
	{
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		boolean flag = paramTreeObject.addPara(param, device_id) == 1 ? true : false;
		paramTreeObject = null;
		return flag;
	}

	/**
	 * 设置WAN连接参数值
	 * 
	 * @param device_id
	 * @param gather_id
	 * @param param
	 * @param value
	 * @param ior
	 * @return
	 */
	public static boolean setAllConnectionParam(String device_id, String gather_id,
			String[][] paramValue, String ior)
	{
		if (paramValue == null)
			return false;
		ParameterValueStruct[] parameterValueStructArr = new ParameterValueStruct[paramValue.length];
		AnyObject anyObject = null;
		for (int i = 0; i < paramValue.length; i++)
		{
			ParameterValueStruct parameterValueStruct = new ParameterValueStruct();
			parameterValueStruct.setName(paramValue[i][0]);
			anyObject = new AnyObject();
			anyObject.para_value = paramValue[i][1];
			anyObject.para_type_id = "1";
			parameterValueStruct.setValue(anyObject);
			parameterValueStructArr[i] = parameterValueStruct;
		}
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		
		SetParameterValues setParameterValues = new SetParameterValues();
		setParameterValues.setParameterKey("ss");
		setParameterValues.setParameterList(parameterValueStructArr);
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		paramTreeObject.setGwType(gw_type);
		DevRpc[] devRPCArr = paramTreeObject.getDevRPCArr(device_id, setParameterValues);
		logger.debug("setAllConnectionParam>>>devRPCArr=" + devRPCArr + "gather_id:"
				+ gather_id);
		List<DevRpcCmdOBJ> devRPCRep = paramTreeObject.getDevRPCResponse(devRPCArr,Global.RpcCmd_Type);
		if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
		{
			return false;
		}
		// 一个设备返回的命令
		if (devRPCRep.get(0).getStat() == 1)
		{
			return true;
		}
		return false;
	}

	/**
	 * 设置PPP连接参数值
	 * 
	 * @param device_id
	 * @param gather_id
	 * @param paramValue
	 *            二维数组 [][0]:parameter [][1]:value
	 * @param ior
	 * @return
	 */
	public static boolean setPPPConnectionParam(HttpServletRequest request, String ior)
	{
		String device_id = request.getParameter("device_id");
		String gather_id = request.getParameter("gather_id");
		String Username = request.getParameter("Username");
		String Password = request.getParameter("Password");
		String ConnectionType = request.getParameter("ConnectionType");
		String Name = request.getParameter("Name");
		String Enable = request.getParameter("Enable");
		String WANConnection = request.getParameter("wanconnection");
		String[][] paramValue = null;
		// 桥连方式
		if (ConnectionType != null && ConnectionType.equals("PPPoE_Bridged"))
		{
			paramValue = new String[2][2];
		}
		else
		{
			paramValue = new String[5][2];
			paramValue[2][0] = WANConnection + "Name";
			paramValue[2][1] = Name;
			paramValue[3][0] = WANConnection + "Username";
			paramValue[3][1] = Username;
			paramValue[4][0] = WANConnection + "Password";
			paramValue[4][1] = Password;
		}
		paramValue[0][0] = WANConnection + "ConnectionType";
		paramValue[0][1] = ConnectionType;
		paramValue[1][0] = WANConnection + "Enable";
		paramValue[1][1] = Enable;
		print(paramValue);
		return setAllConnectionParam(device_id, gather_id, paramValue, ior);
	}

	public static boolean setWANConnectionParam(HttpServletRequest request, String ior)
	{
		String device_id = request.getParameter("device_id");
		String gather_id = request.getParameter("gather_id");
		String LinkType = request.getParameter("LinkType");
		String DestinationAddress = request.getParameter("DestinationAddress");
		String Enable = request.getParameter("Enable");
		String WANConnection = request.getParameter("wanconnection");
		String[][] paramValue = new String[3][2];
		paramValue[0][0] = WANConnection + "LinkType";
		paramValue[0][1] = LinkType;
		paramValue[1][0] = WANConnection + "DestinationAddress";
		paramValue[1][1] = DestinationAddress;
		paramValue[2][0] = WANConnection + "Enable";
		paramValue[2][1] = Enable;
		print(paramValue);
		return setAllConnectionParam(device_id, gather_id, paramValue, ior);
	}

	public static void print(String[][] paramValue)
	{
		for (int i = 0; i < paramValue.length; i++)
		{
			logger.debug("参数打印：" + paramValue[i][0] + "=");
			logger.debug(paramValue[i][1]);
		}
	}

	/**
	 * 获取所有的WAN连接，并生成表格，用户选择某个WANConnection继续创建PPPConnection
	 * InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection
	 * InternetGatewayDevice.WANDevice.1.WANConnectionDevice.2.WANPPPConnection
	 * InternetGatewayDevice.WANDevice.1.WANConnectionDevice.3.
	 * 
	 * @param device_id
	 * @param gather_id
	 * @param ior
	 * @return
	 */
	public static String getWANConnctionListTable(String device_id, String gather_id,
			String ior)
	{
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		
		// 获取InternetGatewayDevice.WANDevice.1.WANConnectionDevice.下面所有的节点，包括第一步创建的WANConnection
		Collection collection = getParameterNameList(device_id,
				RootWANConnection);
		Iterator iterator = collection.iterator();
		StringBuffer sb = new StringBuffer();
		GetParameterNames getParameterNames = new GetParameterNames();
		DevRpc[] devRPCArr = null;
		List<DevRpcCmdOBJ> devRPCRep = null;
		GetParameterNamesResponse getParameterNamesResponse = null;
		String path = null;
		
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		paramTreeObject.setGwType(gw_type);
		
		if (!iterator.hasNext())
		{
			return "<span>没有获取到WAN连接！</span>";
		}
		else
		{
			sb
					.append("<TABLE id='myTable' border=0 cellspacing=1 cellpadding=2 width=\"100%\" align=center bgcolor=#999999>");
		}
		
		String name = null;
		while (iterator.hasNext())
		{
			getParameterNames.setParameterPath(path = (String) iterator.next()
					+ PPPOEConnection);
			getParameterNames.setNextLevel(1);
			devRPCArr = paramTreeObject.getDevRPCArr(device_id, getParameterNames);
			devRPCRep = paramTreeObject.getDevRPCResponse(devRPCArr,Global.RpcCmd_Type);
			Element element = paramTreeObject.dealDevRPCResponse(
					"GetParameterValuesResponse", devRPCRep, device_id);
			if (element == null)
			{
				continue;
			}
			GetParameterValuesResponse getParameterValuesResponse = new GetParameterValuesResponse();
			getParameterValuesResponse = XmlToRpc.GetParameterValuesResponse(element);
			// 通过这个XML对象,获取参数列表
			// JavaScript Function create(1 || 0) 1:可以创建PPPConnection 0:已存在PPPConnection连接
			if (null != getParameterNamesResponse)
			{
				ParameterInfoStruct[] pisArr = getParameterNamesResponse
						.getParameterList();
				if (null == pisArr || pisArr.length == 0)
				{
					sb.append("<TR bgcolor=#ffffff class=column><TD>").append(path)
							.append("</TD>");
					sb
							.append("<TD nowrap><a href=javascript:// onclick=create(1)>创建</a></TD>");
					sb.append("</TR>");
				}
				else
				{
					for (int i = 0; i < pisArr.length; i++)
					{
						name = pisArr[i].getName();
						logger.debug("getWANConnctionListTable>>>" + name);
						// if(name.indexOf(PPPOEConnection) != -1){
						sb.append("<TR bgcolor=#ffffff class=column><TD>").append(name)
								.append("</TD>");
						sb
								.append("<TD nowrap><a href=javascript:// onclick=create(0)>创建</a></TD>");
						sb.append("</TR>");
						// }
					}
				}
			}
			else
			{
				logger.warn("getParameterNamesResponse=====null");
				sb.append("<TR bgcolor=#ffffff class=column><TD>").append(path).append(
						"</TD>");
				sb
						.append("<TD nowrap><a href=javascript:// onclick=create(1)>创建</a></TD>");
				sb.append("</TR>");
			}
		}
		sb.append("</table>");
		return sb.toString();
	}

	/**
	 * 获取所有含有PPPOEConnection的连接
	 * InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection
	 * InternetGatewayDevice.WANDevice.1.WANConnectionDevice.2.WANPPPConnection
	 * InternetGatewayDevice.WANDevice.1.WANConnectionDevice.3.WANPPPConnection
	 * InternetGatewayDevice.WANDevice.1.WANConnectionDevice.4.WANPPPConnection
	 * 
	 * @param device_id
	 * @param gather_id
	 * @param ior
	 * @return
	 */
	public static Collection getPPPOEConnectionList(String device_id)
	{
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		
		List list = new ArrayList();
		Collection collection = getParameterNameList(device_id, RootWANConnection);
		GetParameterNames getParameterNames = new GetParameterNames();
		DevRpc[] devRPCArr = null;
		List<DevRpcCmdOBJ> devRPCRep = null;
		GetParameterNamesResponse getParameterNamesResponse = null;
		Iterator iterator = collection.iterator();
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		paramTreeObject.setGwType(gw_type);
		
		while (iterator.hasNext())
		{
			getParameterNames.setParameterPath(iterator.next() + PPPOEConnection);
			getParameterNames.setNextLevel(1);
			devRPCArr = paramTreeObject.getDevRPCArr(device_id, getParameterNames);
			devRPCRep = paramTreeObject.getDevRPCResponse(devRPCArr,Global.RpcCmd_Type);
			// 一个设备返回的命令
			Element element = paramTreeObject.dealDevRPCResponse(
					"GetParameterValuesResponse", devRPCRep, device_id);
			if (element == null)
			{
				return list;
			}
			GetParameterValuesResponse getParameterValuesResponse = new GetParameterValuesResponse();
			getParameterValuesResponse = XmlToRpc.GetParameterValuesResponse(element);
			// 通过这个XML对象,获取参数列表
			if (null != getParameterNamesResponse)
			{
				String name = null;
				ParameterInfoStruct[] pisArr = getParameterNamesResponse
						.getParameterList();
				if (null != pisArr)
				{
					for (int i = 0; i < pisArr.length; i++)
					{
						name = pisArr[i].getName();
						logger.debug("getPPPOEConnectionList>>>>" + name);
						list.add(name);
						/*
						 * if(name.indexOf(PPPOEConnection) != -1){ list.add(name); }
						 */
					}
				}
			}
		}
		return list;
	}

	/**
	 * 获取参数的属性值
	 * InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.PPPOEConnection.UserName:admin
	 * InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.PPPOEConnection.PassWd:pwd
	 * 
	 * @param param
	 * @param ior
	 * @param device_id
	 * @param gather_id
	 * @return
	 */
	public static Map getParamValueMap(String param, String device_id)
	{
		Map result = new HashMap();
		String[] deviceAttribute = null;
		if (deviceAttribute == null)
		{
			String strSQL = "select *  from tab_gw_device where device_id='" + device_id
					+ "'";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				strSQL = "select oui, device_serialnumber, loopback_ip, cr_port, cr_path, acs_username, acs_passwd " +
						" from tab_gw_device where device_id='" + device_id
						+ "'";
			}
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(strSQL);
			Map fields = cursor.getNext();
			// 如果查询无设备，则直接返回NULL
			if (fields == null)
			{
				return null;
			}
			deviceAttribute = new String[7];
			deviceAttribute[0] = (String) fields.get("oui");// String oui = (String)
			// fields.get("oui");
			deviceAttribute[1] = (String) fields.get("device_serialnumber");// String
			// SerialNumber
			// = (String)
			// fields.get("device_serialnumber");
			deviceAttribute[2] = (String) fields.get("loopback_ip");// String ip =
			// (String)
			// fields.get("loopback_ip");
			deviceAttribute[3] = (String) fields.get("cr_port");// String Port = (String)
			// fields.get("port");
			deviceAttribute[4] = (String) fields.get("cr_path");// String path = (String)
			// fields.get("path");
			deviceAttribute[5] = (String) fields.get("acs_username");// String username
			// = (String)
			// fields.get("acs_username");
			deviceAttribute[6] = (String) fields.get("acs_passwd");// String passwd =
			// (String)
			// fields.get("acs_passwd");
			deviceAttributeMap.put(device_id, deviceAttribute);
			cursor = null;
		}
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] paramValues = { param };
		getParameterValues.setParameterNames(paramValues);
		DevRpc[] devRPCArr = paramTreeObject.getDevRPCArr(device_id, getParameterValues);
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		paramTreeObject.setGwType(gw_type);

		List<DevRpcCmdOBJ> devRPCRep = paramTreeObject.getDevRPCResponse(devRPCArr,Global.RpcCmd_Type);
		// 一个设备返回的命令
		Element element = paramTreeObject.dealDevRPCResponse(
				"GetParameterValuesResponse", devRPCRep, device_id);
		if (element == null)
		{
			return result;
		}
		GetParameterValuesResponse getParameterValuesResponse = new GetParameterValuesResponse();
		getParameterValuesResponse = XmlToRpc.GetParameterValuesResponse(element);
		if (null != getParameterValuesResponse)
		{
			ParameterValueStruct[] pisArr = getParameterValuesResponse.getParameterList();
			if (pisArr != null)
			{
				String name = null;
				String value = null;
				for (int i = 0; i < pisArr.length; i++)
				{
					name = pisArr[i].getName().substring(param.length());
					value = pisArr[i].getValue().para_value;
					result.put(name, value);
					logger.debug("getParamValueMap>>>>>" + name + "=" + value);
				}
			}
		}
		return result;
	}

	/**
	 * 获取PPPOE所有属性值，包括第二步创建的PPPOE连接
	 * 
	 * @param device_id
	 * @param gather_id
	 * @param ior
	 * @return
	 */
	public static String getPPPOEConnectionAttributeTale(String device_id)
	{
		// 先获取所有PPPOECtion
		Collection collection = getPPPOEConnectionList(device_id);
		Iterator iterator = collection.iterator();
		// Map allResult = new HashMap();
		StringBuffer sb = new StringBuffer();
		String param = null;
		if (!iterator.hasNext())
		{
			return "<span>没有获取到PPPOE连接，设备可能正在操作或无法连接!</span>";
		}
		sb
				.append("<TABLE id='myTable' border=0 cellspacing=1 cellpadding=2 width=\"100%\" align=center bgcolor=#999999>");
		sb
				.append("<tr><th>PPPOE连接</th><th nowrap>连接类型</th><th nowrap>名称</th><th nowrap>帐号</th><th nowrap>启用</th><th>&nbsp;</th>");
		// JavaScript Function
		// String editString = "edit('?','#')";
		while (iterator.hasNext())
		{
			param = (String) iterator.next();
			logger.debug("获取WANPPPConnection路径：" + param);
			// allResult = getParamValueMap(param, ior, device_id, gather_id);
			sb.append("<tr bgcolor=#ffffff><td class=column>").append(param).append(
					"</td>");
			sb.append("<td>&nbsp;</td>");
			sb.append("<td>&nbsp;</td>");
			sb.append("<td>&nbsp;</td>");
			sb.append("<td>&nbsp;</td>");
			/*
			 * sb.append("<td>").append(allResult.get("ConnectionType")).append("</td>");
			 * sb.append("<td>").append(allResult.get("Name")).append("</td>");
			 * sb.append("<td>").append(allResult.get("Username")).append("</td>");
			 */
			// sb.append("<td>").append(allResult.get("Password")).append("</td>");
			sb
					.append("<td align=center nowrap><a onclick=edit() href=javascript://>编辑</a></td>");
			// .append(editString.replaceAll("\\?",
			// param).replaceAll("\\#",(String)allResult.get("Username")))
			// .append(">编辑<span></td>");
			sb.append("</tr>");
		}
		sb.append("</TABLE>");
		return sb.toString();
	}

	/**
	 * 获取PPPOE所有属性值，包括第二步创建的PPPOE连接
	 * 
	 * @param device_id
	 * @param gather_id
	 * @param ior
	 * @return
	 */
	public static String getWANConnectionAttributeTale(String device_id,
			String gather_id, String ior)
	{
		// 先获取所有PPPOECtion
		Collection collection = getParameterNameList(device_id, RootWANConnection);
		Iterator iterator = collection.iterator();
		// Map allResult = new HashMap();
		StringBuffer sb = new StringBuffer();
		String param = null;
		if (!iterator.hasNext())
		{
			return "<span>没有获取到WAN连接,设备可能正在操作或无法连接!</span>";
		}
		sb
				.append("<TABLE id='myTable' border=0 cellspacing=1 cellpadding=2 width=\"100%\" align=center bgcolor=#999999>");
		sb
				.append("<tr><th>WAN连接</th><th>连接类型</th><th nowrap>PVC</th><th nowrap>启用</th><th>&nbsp;</th>");
		// JavaScript Function
		// String editString = "edit('?','#')";
		while (iterator.hasNext())
		{
			param = (String) iterator.next() + WANDSLLinkConfig;
			logger.debug("获取WANConnection.WANDSLLinkConfig路径：" + param);
			// allResult = getParamValueMap(param, ior, device_id, gather_id);
			sb.append("<tr bgcolor=#ffffff><td class=column>").append(param).append(
					"</td>");
			sb.append("<td>&nbsp;</td>");
			sb.append("<td>&nbsp;</td>");
			sb.append("<td>&nbsp;</td>");
			sb
					.append("<td align=center nowrap><a onclick=edit() href=javascript://>编辑</a></td>");
			/*
			 * sb.append("<td>").append(allResult.get("LinkType")).append("</td>");
			 * sb.append("<td>").append(allResult.get("DestinationAddress")).append("</td>");
			 * sb.append("<td>").append(allResult.get("Enable")).append("</td>");
			 * sb.append("<td align=center nowrap><a onclick=edit()
			 * href=javascript://>编辑</a></td>");
			 */
			// .append(editString.replaceAll("\\?",
			// param).replaceAll("\\#",(String)allResult.get("Username")))
			sb.append("</tr>");
		}
		sb.append("</TABLE>");
		return sb.toString();
	}

	/**
	 * 根据参数得到下一级参数列表
	 * 
	 * @param device_id
	 * @param gather_id
	 * @param param
	 * @param ior
	 * @return
	 */
	public static Collection getParameterNameList(String device_id, String param)
	{
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		GetParameterNames getParameterNames = new GetParameterNames();
		getParameterNames.setParameterPath(param);
		getParameterNames.setNextLevel(1);
		ArrayList list = new ArrayList();
		DevRpc[] devRPCArr = paramTreeObject.getDevRPCArr(device_id, getParameterNames);
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		
		paramTreeObject.setGwType(gw_type);

		List<DevRpcCmdOBJ> devRPCRep = paramTreeObject.getDevRPCResponse(devRPCArr,Global.RpcCmd_Type);
		Element element = paramTreeObject.dealDevRPCResponse("GetParameterNamesResponse",
				devRPCRep, device_id);
		// 一个设备返回的命令
		GetParameterNamesResponse getParameterNamesResponse = null;
		if (element == null)
		{
			return list;
		}
		getParameterNamesResponse = XmlToRpc.GetParameterNamesResponse(element);
		// 通过这个XML对象,获取参数列表
		if (null != getParameterNamesResponse)
		{
			ParameterInfoStruct[] pisArr = getParameterNamesResponse.getParameterList();
			if (null != pisArr)
			{
				for (int i = 0; i < pisArr.length; i++)
				{
					list.add(pisArr[i].getName());
				}
			}
		}
		logger.debug("HHHH>>>getParameterNameList>>>>" + list);
		return list;
	}
}
