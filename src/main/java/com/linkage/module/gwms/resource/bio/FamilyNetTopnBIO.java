
package com.linkage.module.gwms.resource.bio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.linkage.commons.util.StringUtil;
import com.linkage.commons.xml.XML;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.dao.FamilyNetTopnDAO;
import com.linkage.module.gwms.resource.model.GwInfoModel;
import com.linkage.module.gwms.resource.model.NetTopologicalInfoModel;
import com.linkage.module.gwms.resource.model.TopoInfoModel;
import com.linkage.module.ids.util.WSClientUtil;

/**
 * @author yages (Ailk No.78987)
 * @version 1.0
 * @since 2019-11-4
 * @category com.linkage.module.gwms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class FamilyNetTopnBIO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(FamilyNetTopnBIO.class);
	private FamilyNetTopnDAO dao;

	/**
	 * @param deviceId
	 * @param query_type
	 *            获取数据 1：ItmsService接口 2：集团接口
	 * @param next_handle
	 *            未获取到数据处理：1：查数据库上次记录 2：调用其他接口
	 * @return
	 */
	public NetTopologicalInfoModel getTopnInfo(String deviceId, String query_type,
			String next_handle)
	{
		long timestamp = System.currentTimeMillis() / 1000; // 当前时间戳
		boolean useDb = false; // 是否调用过数据库
		NetTopologicalInfoModel model = new NetTopologicalInfoModel();
		GwInfoModel gmModel = new GwInfoModel();
		List<TopoInfoModel> devInfos = new ArrayList<TopoInfoModel>();
		String devSN = StringUtil.getStringValue(dao.querySNByDeviceId(deviceId),"device_serialnumber","");
		if(StringUtil.IsEmpty(devSN))
		{
			logger.warn("根据设备id[{}]，未获取到设备SN！");
			return model;
		}
		String par_mac = StringUtil.getStringValue(dao.querySNByDeviceId(deviceId), "cpe_mac", "null"); // 由于ItmsService接口只能获取一级下挂设备，父mac即为光猫mac
		if (Global.GSDX.equals(Global.instAreaShortName))
		{
			par_mac = devSN;
		}
		String loid = dao.queryLoidByDeviceId(deviceId).get("username");
		// 现获取光猫信息，从数据库直接获取
		gmModel = getGmInfo(deviceId);
		gmModel.setLoid(loid);
		gmModel.setMac(par_mac);
		model.setTimestamp(timestamp);
		/**
		 * step1:先获取光猫设备信息
		 */
		if ("1".equals(query_type))
		{
			devInfos = getDataFromItms(devSN, loid, par_mac); // 从ItmsService获取
		}
		else if ("2".equals(query_type))
		{
			devInfos = getDataFromEsb(devSN); // 从集团接口获取
		}
		else if ("3".equals(query_type))
		{
			devInfos = dao.getNetTopnInfo(deviceId); // 从数据库获取
			useDb = true;
		}
		/**
		 * step2:如果未获取到数据，根据传入参数决定是否调用其他接口
		 */
		if (devInfos.isEmpty() && "2".equals(next_handle) && "1".equals(query_type))
		{
			devInfos = getDataFromEsb(devSN);
		}
		else if (devInfos.isEmpty() && "2".equals(next_handle) && "2".equals(query_type))
		{
			devInfos = getDataFromItms(devSN, loid, par_mac);
		}
		if (devInfos.isEmpty() && !useDb)
		{
			devInfos = dao.getNetTopnInfo(deviceId);
			useDb = true;
		}
		/**
		 * step3:处理数据
		 */
		if (!devInfos.isEmpty() && useDb)
		{
			model.setTimestamp(devInfos.get(0).getTimestamp()); // 同一设备下的下挂设备的入库时间一致，取一个放入外层，用于前台展示
		}
		//根据下挂设备mac，去数据库匹配厂商
		for (TopoInfoModel topoInfoModel : devInfos)
		{
			String mac = topoInfoModel.getMac();
			if(!StringUtil.IsEmpty(mac))
			{
				topoInfoModel.setVendor(dao.queryVendorByMac(mac));
			}
		}
		model.setGw_info(gmModel);
		model.setTopo_info(devInfos);
		if (!useDb && !devInfos.isEmpty())
		{
			/**
			 * useDb 为false则代表数据是通过调用接口获取，需要存入数据库
			 */
			boolean result = dao.saveNetTopnInfo(deviceId, devInfos, timestamp);
			logger.warn("拓扑数据存入数据库完成，设备id[{}]，结果result[{}]", deviceId, result);
		}
		return model;
	}

	/**
	 * 从ItmsService获取拓扑数据
	 * 
	 * @param devSN
	 * @param loid
	 * @return 拓扑数据
	 */
	public List<TopoInfoModel> getDataFromItms(String devSN, String loid, String par_mac)
	{
		logger.warn("从IimtService获取网络拓扑数据");
		final String url = LipossGlobals.getLipossProperty("ItmsServiceUri");
		logger.warn("ItmsServiceUrl:[{}]",url);
		List<TopoInfoModel> devInfos = new ArrayList<TopoInfoModel>();
		logger.warn("========================开始获取有线下挂设备==============================");
		StringBuffer topnInParam = new StringBuffer();
		topnInParam.append("<?xml version=\"1.0\" encoding=\"GBK\"?>	\n");
		topnInParam.append("<root>										\n");
		topnInParam.append("	<CmdID>123456789012345</CmdID>			\n");
		topnInParam.append("	<CmdType>CX_01</CmdType>				\n");
		topnInParam.append("	<ClientType>3</ClientType>				\n");
		topnInParam.append("	<Param>									\n");
		topnInParam.append("		<UserInfoType>6</UserInfoType>	\n");
		topnInParam.append("        <UserInfo>").append(devSN)
				.append("</UserInfo>      \n");
		topnInParam.append("	</Param>								\n");
		topnInParam.append("</root>										\n");
		logger.warn("http:" + topnInParam.toString());
		String topnMethod = "macGather";
		String topnInfo = WSClientUtil.callItmsService(url, topnInParam.toString(),
				topnMethod);
		logger.warn("macGather回参：" + topnInfo);
		if (!StringUtil.IsEmpty(topnInfo))
		{
			XML xml = new XML(topnInfo, "");
			String RstCode = xml.getStringValue("RstCode");
			if ("0".equals(RstCode))
			{
				// 有线下挂设备信息
				List<Element> LanPorts = xml.getElements("LanPorts.LanPort");
				if (!LanPorts.isEmpty())
				{
					for (Element element : LanPorts)
					{
						TopoInfoModel model = new TopoInfoModel();
						String port = element.getChildTextTrim("LanPortNum");
						String mac = element.getChildTextTrim("MacAddress").replace(":","");
						System.out.println(mac);
						String devModel = element.getChildTextTrim("HostName");
						model.setAcc_port(port);
						model.setMac(mac);
						model.setModel(devModel);
						model.setActive(1);
						model.setAcc_type(0);
						model.setPar_mac(par_mac);
						devInfos.add(model);
					}
				}
			}
		}
		logger.warn("========================开始获取无线下挂设备==============================");
		StringBuffer wifiInParam = new StringBuffer();
		wifiInParam.append("<?xml version=\"1.0\" encoding=\"GBK\"?>	\n");
		wifiInParam.append("<root>										\n");
		wifiInParam.append("	<CmdID>123456789012345</CmdID>			\n");
		wifiInParam.append("	<CmdType>CX_01</CmdType>				\n");
		wifiInParam.append("	<ClientType>3</ClientType>				\n");
		wifiInParam.append("	<Param>									\n");
		wifiInParam.append("		<UserInfoType>2</UserInfoType>					   \n");
		wifiInParam.append("        <UserInfo>" + loid + "</UserInfo>      \n");
		wifiInParam.append("        <SSIDnum>1</SSIDnum>           					   \n"); // 暂时先传1
		wifiInParam.append("        <CityId>00</CityId>          					   \n"); // 江西分支需要传入cityid,暂时先传入省会id
		wifiInParam.append("	</Param>								\n");
		wifiInParam.append("</root>										\n");
		logger.warn("http:" + wifiInParam.toString());
		String wifiMethod = "queryWIFIDeviceMAC";
		String wifiInfo = WSClientUtil.callItmsService(url, wifiInParam.toString(),
				wifiMethod);
		logger.warn("queryWIFIDeviceMAC回参：" + wifiInfo);
		if (!StringUtil.IsEmpty(wifiInfo))
		{
			XML xml_wifi = new XML(wifiInfo, "");
			String RstCode_wifi = xml_wifi.getStringValue("RstCode");
			if ("0".equals(RstCode_wifi))
			{
				// 无线下挂设备信息
				List<Element> WiFiMAC = (List<Element>) xml_wifi.getElements("WiFiMAC.WiFiDEV ");
				if (!WiFiMAC.isEmpty())
				{
					for (Element element : WiFiMAC)
					{
						TopoInfoModel wif_model = new TopoInfoModel();
						String mac = element.getChildTextTrim("WIFIDeviceMAC").replace(":", "");
						wif_model.setMac(mac);
						wif_model.setAcc_type(1);
						wif_model.setActive(1);
						wif_model.setPar_mac(par_mac);
						devInfos.add(wif_model);
					}
				}
			}
		}
		return devInfos;
	}

	/**
	 * 从集团接口获取拓扑数据
	 * 
	 * @param devSN
	 * @return 拓扑数据
	 */
	public List<TopoInfoModel> getDataFromEsb(String devSN)
	{
		logger.warn("从ESBService获取网络拓扑数据");
		final String url = LipossGlobals.getLipossProperty("ESBServiceUrl");
		List<TopoInfoModel> devInfos = new ArrayList<TopoInfoModel>();
		if (StringUtil.IsEmpty(url))
		{
			logger.warn("集团接口访问路径未配置，不允许访问集团接口");
			return devInfos;
		}
		logger.warn("========================开始获取网络拓扑信息==============================");
		StringBuffer topnInParam = new StringBuffer();
		topnInParam.append("<?xml version=\"1.0\" encoding=\"GBK\"?>	\n");
		topnInParam.append("<root>										\n");
		topnInParam.append("	<CmdID>123456789012345</CmdID>			\n");
		topnInParam.append("	<CmdType>CX_01</CmdType>				\n");
		topnInParam.append("	<ClientType>1</ClientType>				\n");
		topnInParam.append("	<Param>									\n");
		topnInParam.append("		<UserInfoType>4</UserInfoType>						\n");
		topnInParam.append("        <UserInfo>").append(devSN)
				.append("</UserInfo>      \n");
		topnInParam.append("        <CityID>791</CityID>      							\n"); // 江西环境需要传入
		topnInParam.append("	</Param>								\n");
		topnInParam.append("</root>										\n");
		logger.warn("http:" + topnInParam.toString());
		String topnMethod = "getNetTopologicakInfo";
		String topnInfo = WSClientUtil.callItmsService(url, topnInParam.toString(),
				topnMethod);
		logger.warn("getNetTopologicakInfo回参：" + topnInfo);
		if (!StringUtil.IsEmpty(topnInfo))
		{
			// 解析回参
			XML xml = new XML(topnInfo, "");
			String code = xml.getStringValue("RstCode");
			String message = xml.getStringValue("RstMsg");
			String result = xml.getStringValue("Result");
			logger.warn(message);
			if ("0".equals(code) && !StringUtil.IsEmpty(result))
			{
				NetTopologicalInfoModel netTopologicalInfo = JSONObject.parseObject(
						result, NetTopologicalInfoModel.class);
				devInfos = netTopologicalInfo.getTopo_info();
			}
		}
		return devInfos;
	}

	public GwInfoModel getGmInfo(String deviceId)
	{
		GwInfoModel model = new GwInfoModel();
		Map<String, String> map = dao.queryDevInfoByDeviceId(deviceId);
		model.setSn(StringUtil.getStringValue(map, "device_serialnumber"));
		model.setMac(StringUtil.getStringValue(map, "cpe_mac"));
		model.setVendor(StringUtil.getStringValue(map, "vendor_name"));
		model.setModel(StringUtil.getStringValue(map, "device_model"));
		model.setSoftwareversion(StringUtil.getStringValue(map, "softwareversion"));
		model.setLoopback_ip(StringUtil.getStringValue(map, "loopback_ip"));
		model.setHardwareversion(StringUtil.getStringValue(map, "hardwareversion"));
		model.setPhone(StringUtil.getStringValue(map, "phone"));
		return model;
	}

	/**
	 * @return the dao
	 */
	public FamilyNetTopnDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(FamilyNetTopnDAO dao)
	{
		this.dao = dao;
	}

	public static void main(String[] args)
	{
		List<TopoInfoModel> devInfos = new ArrayList<TopoInfoModel>();
		String topnInfo = "<?xml version=\"1.0\" encoding=\"GBK\"?><root>"
				+ "<CmdID>123456789012345</CmdID><RstCode>0</RstCode><RstMsg>成功</RstMsg>"
				+ "<DevSN>498366C3845294260</DevSN><LanPorts><LanPort><LanPortNum>1</LanPortNum>"
				+ "<MacAddress>74:05:A5:CF:A3:CB</MacAddress><HostName>unknown</HostName></LanPort>"
				+ "<LanPort><LanPortNum>2</LanPortNum><MacAddress>74:05:A5:CF:A3:CA</MacAddress>"
				+ "<HostName>empty</HostName></LanPort></LanPorts></root>";
		XML xml = new XML(topnInfo, "");
		String RstCode = xml.getStringValue("RstCode");
		if ("0".equals(RstCode))
		{
			// 有线下挂设备信息
			List<Element> LanPorts = xml.getElements("LanPorts.LanPort");
			if (!LanPorts.isEmpty())
			{
				for (Element element : LanPorts)
				{
					TopoInfoModel model = new TopoInfoModel();
					String port = element.getChildTextTrim("LanPortNum");
					System.out.println(port);
					String mac = element.getChildTextTrim("MacAddress").replace(":", "");
					System.out.println(mac);
					String devModel = element.getChildTextTrim("HostName");
					System.out.println(devModel);
					model.setAcc_port(port);
					model.setMac(mac);
					model.setModel(devModel);
					model.setAcc_type(0);
					model.setPar_mac("aaa");
					devInfos.add(model);
				}
			}
		}
		
		String wifi = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><CmdID>123456789012345</CmdID>" +
				"<RstCode>1000</RstCode><RstMsg>该ssid下没有挂设备或者请确认节点路径是否正确</RstMsg></root>";
		if (!wifi.isEmpty())
		{
			XML xmla = new XML(wifi,"");
			String RstCodea = xmla.getStringValue("RstCode");
			System.out.println(RstCodea);
			if ("0".equals(RstCodea))
			{
				// 无线下挂设备信息
				List<Element> WiFiMAC = (List<Element>) xmla.getElements("WiFiMAC.WiFiDEV ");
				if (!WiFiMAC.isEmpty())
				{
					for (Element element : WiFiMAC)
					{
						TopoInfoModel wif_model = new TopoInfoModel();
						String mac = element.getChildTextTrim("WIFIDeviceMAC").replace(
								":", "");
						wif_model.setMac(mac);
						wif_model.setAcc_type(1);
						wif_model.setPar_mac("aa");
						devInfos.add(wif_model);
					}
				}
			}
		}
	}
}
