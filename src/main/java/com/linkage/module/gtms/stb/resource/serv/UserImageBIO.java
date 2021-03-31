
package com.linkage.module.gtms.stb.resource.serv;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.resource.dao.UserImageDAO;
import com.linkage.module.ids.util.WSClientUtil;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-12-15
 * @category com.linkage.module.gtms.stb.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class UserImageBIO
{

	private UserImageDAO dao;
	private static Logger logger = LoggerFactory.getLogger(UserImageDAO.class);

	/**
	 * 查询对应的设备id
	 * 
	 * @param con
	 * @param condition
	 * @return
	 */
	public List<Map<String, String>> getDevice_id(String con, String condition)
	{
		logger.debug("UserImageBIO====>getDevice_id({ftpenable},{UserName})", new Object[] {
				con, condition });
		return dao.getDevice_id(con, condition);
	}

	/**
	 * 查询用户基本信息
	 * 
	 * @param con
	 * @param condition
	 * @return
	 */
	public List<Map<String, String>> getInformation(String con, String condition)
	{
		logger.debug("UserImageBIO====>getInformation({ftpenable},{UserName})", new Object[] {
				con, condition });
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> listshow = new ArrayList<Map<String, String>>();
		list = dao.getInformation(con, condition);
		if (list == null)
		{
			return listshow;
		}
		else
		{
			for (int i = 0; i < list.size(); i++)
			{
				HashMap<String, String> stbMap = new HashMap<String, String>();
				if (StringUtil.IsEmpty(String.valueOf(list.get(i).get("linkman"))))
				{
					stbMap.put("linkman", "");
				}
				else
				{
					stbMap.put("linkman", String.valueOf(list.get(i).get("linkman")));
				}
				if (StringUtil.IsEmpty(String.valueOf(list.get(i).get("linkaddress"))))
				{
					stbMap.put("linkaddress", "");
				}
				else
				{
					stbMap.put("linkaddress",
							String.valueOf(list.get(i).get("linkaddress")));
				}
				if (StringUtil.IsEmpty(String.valueOf(list.get(i).get("linkman_credno"))))
				{
					stbMap.put("linkman_credno", "");
				}
				else
				{
					stbMap.put("linkman_credno",
							String.valueOf(list.get(i).get("linkman_credno")));
				}
				if (StringUtil.IsEmpty(String.valueOf(list.get(i).get("linkphone"))))
				{
					stbMap.put("linkphone", "");
				}
				else
				{
					stbMap.put("linkphone", String.valueOf(list.get(i).get("linkphone")));
				}
				if (StringUtil.IsEmpty(String.valueOf(list.get(i).get("username"))))
				{
					stbMap.put("username", "");
				}
				else
				{
					stbMap.put("username", String.valueOf(list.get(i).get("username")));
				}
				if (StringUtil.IsEmpty(String.valueOf(list.get(i).get("bandwidth"))))
				{
					stbMap.put("bandwidth", "");
				}
				else
				{
					stbMap.put("bandwidth", String.valueOf(list.get(i).get("bandwidth")));
				}
				if (StringUtil.IsEmpty(String.valueOf(list.get(i).get("aa"))))
				{
					stbMap.put("loid", "");
				}
				else
				{
					stbMap.put("loid", String.valueOf(list.get(i).get("aa")));
				}
				if (String.valueOf(list.get(i).get("cust_type_id")).equals("0"))
				{
					stbMap.put("cust_type", "公司客户");
				}
				else if (String.valueOf(list.get(i).get("cust_type_id")).equals("1"))
				{
					stbMap.put("cust_type", "网吧客户");
				}
				else
				{
					stbMap.put("cust_type", "个人客户");
				}
				listshow.add(stbMap);
			}
		}
		return listshow;
	}

	public List<Map<String, String>> getMac_adderss(String deviceid)
	{
		logger.debug("UserImageBIO====>getMac_adderss({deviceid})", new Object[] {
				deviceid });
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list = dao.getMac_adderss(deviceid);
		return list;
	}

	public List<Map<String, String>> getStbMac(String mac_address)
	{
		logger.debug("UserImageBIO====>getStbMac({mac_address})", new Object[] {
				mac_address });
		return dao.getStbMac(mac_address);
	}

	public List<Map<String, String>> xmlStr(String StbMac, String layer2_interface,
			String mac_address, String ipaddress)
	{
		logger.debug("UserImageBIO====>xmlStr({StbMac},{layer2_interface},{mac_address},{ipaddress})", new Object[] {
				StbMac,layer2_interface,mac_address,ipaddress });
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String xmlStr = sendCloseSSIDXML(StbMac);
		String url = LipossGlobals.getLipossProperty("getStbInfoUrl");
		String resultString = WSClientUtil.callItmsService(url, xmlStr, "getStbInfo");
		int tote = dao.tote(mac_address);
		String str = "";
		if (!StringUtil.IsEmpty(layer2_interface))
		{
			String[] layer2 = layer2_interface.split("\\.");
			String layer = layer2[layer2.length-2];
			if (layer.equals("LANEthernetInterfaceConfig"))
			{
				str = "LAN"+ layer2_interface.substring(layer2_interface.length() - 1,
								layer2_interface.length()) + "口";
			}
			else if (layer.equals("WLANConfiguration"))
			{
				str = "SSID " + layer2_interface.substring(layer2_interface.length() - 1,
								layer2_interface.length());
			}
		}
		logger.warn("接收的参数为=" + resultString);
		SAXReader reader = new SAXReader();
		Document document = null;
		try
		{
			document = reader.read(new StringReader(resultString));
			Element root = document.getRootElement();
			String result_flag = root.elementTextTrim("result_flag");
			String result = root.elementTextTrim("result");
			Element Sheets = root.element("Sheets");
			List<Element> listsho1 = Sheets.elements("sheetInfo");
			for (int i = 0; i < listsho1.size(); i++)
			{
				HashMap<String, String> stbMap = new HashMap<String, String>();
				String stb_ip = listsho1.get(i).elementTextTrim("stb_ip");
				String serv_account = listsho1.get(i).elementTextTrim("serv_account");
				String stb_type = listsho1.get(i).elementTextTrim("stb_type");
				String stb_mac = listsho1.get(i).elementTextTrim("stb_mac");
				String stb_sn = listsho1.get(i).elementTextTrim("stb_sn");
				String softversion = listsho1.get(i).elementTextTrim("softversion");
				String stb_vendor = listsho1.get(i).elementTextTrim("stb_vendor");
				String stb_status = listsho1.get(i).elementTextTrim("stb_status");
				stbMap.put("stb_ip", stb_ip);
				stbMap.put("serv_account", serv_account);
				stbMap.put("stb_type", stb_type);
				stbMap.put("stb_mac", stb_mac);
				stbMap.put("softversion", softversion);
				stbMap.put("stb_vendor", stb_vendor);
				stbMap.put("stb_status", stb_status);
				stbMap.put("stb_sn", stb_sn);
				stbMap.put("tote", String.valueOf(tote));
				if (!StringUtil.IsEmpty(str))
				{
					stbMap.put("LAN", str);
				}
				else
				{
					stbMap.put("LAN", str);
				}
				list.add(stbMap);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
public int tote(String mac_address)
{
	return dao.tote(mac_address);
}
	public List<Map<String, String>> companyname(String mac, String layer2_interface,
			String mac_address, String ipaddress)
	{
		logger.debug("UserImageBIO====>companyname({mac},{layer2_interface},{mac_address},{ipaddress})", new Object[] {
				mac,layer2_interface,mac_address,ipaddress });
		List<Map<String, String>> list = null;
		List<Map<String, String>> listshow = new ArrayList<Map<String, String>>();
		int tote = dao.tote(mac_address);
		list = dao.companyname(mac);
		String str = "";
		if (!StringUtil.IsEmpty(layer2_interface))
		{
			String[] layer2 = layer2_interface.split("\\.");
			String layer = layer2[layer2.length-2];
			if (layer.equals("LANEthernetInterfaceConfig"))
			{
				str = "LAN"
						+ layer2_interface.substring(layer2_interface.length() - 1,
								layer2_interface.length()) + "口";
			}
			else if (layer.equals("WLANConfiguration"))
			{
				str = "SSID "
						+ layer2_interface.substring(layer2_interface.length() - 1,
								layer2_interface.length());
			}
		}
		if (list == null)
		{
			return listshow;
		}
		else
		{
			for (Map<String, String> map : list)
			{
				HashMap<String, String> stbMap = new HashMap<String, String>();
				stbMap.put("company_name", map.get("company_name"));
				if (!StringUtil.IsEmpty(str))
				{
					stbMap.put("LAN", str);
				}
				else
				{
					stbMap.put("LAN", str);
				}
				stbMap.put("mac_address", mac_address);
				stbMap.put("ipaddress", ipaddress);
				stbMap.put("tote", String.valueOf(tote));
				listshow.add(stbMap);
			}
		}
		return listshow;
	}

	public List<Map<String, String>> getphoneinfo(String username,
			String layer2_interface, String mac_address, String ipaddress)
	{
		logger.debug("UserImageBIO====>getphoneinfo({username},{layer2_interface},{mac_address},{ipaddress})", new Object[] {
				username,layer2_interface,mac_address,ipaddress });
		List<Map<String, String>> list = null;
		list = dao.getphoneinfo(username);
		List<Map<String, String>> listshow = new ArrayList<Map<String, String>>();
		int tote = dao.tote(mac_address);
		String str = "";
		if (!StringUtil.IsEmpty(layer2_interface))
		{
			String[] layer2 = layer2_interface.split("\\.");
			String layer = layer2[layer2.length-2];
			if (layer.equals("LANEthernetInterfaceConfig"))
			{
				str = "LAN"
						+ layer2_interface.substring(layer2_interface.length() - 1,
								layer2_interface.length()) + "口";
			}
			else if (layer.equals("WLANConfiguration"))
			{
				str = "SSID "
						+ layer2_interface.substring(layer2_interface.length() - 1,
								layer2_interface.length());
			}
		}
		if (list == null)
		{
			return listshow;
		}
		else
		{
			for (Map map : list)
			{
				HashMap<String, String> stbMap = new HashMap<String, String>();
				if (!StringUtil.IsEmpty(str))
				{
					stbMap.put("LAN", str);
				}
				else
				{
					stbMap.put("LAN", str);
				}
				stbMap.put("mac_address", mac_address);
				stbMap.put("net_account", (String) map.get("net_account"));
				stbMap.put("ipaddress", ipaddress);
				stbMap.put("phone_number", (String) map.get("phone_number"));
				stbMap.put("tote", String.valueOf(tote));
				stbMap.put("brand", String.valueOf(map.get("brand")));
				listshow.add(stbMap);
			}
		}
		return listshow;
	}

	public List<Map<String, String>> getphonenumber(String username)
	{
		return dao.getphonenumber(username);
	}

	public String sendCloseSSIDXML(String StbMac)
	{
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		Element root = document.addElement("root");
		// 接口调用唯一ID
		root.addElement("CmdID").addText("123456789012345");
		root.addElement("CmdType").addText("CX_01");
		root.addElement("ClientType").addText("3");
		Element param = root.addElement("Param");
		param.addElement("SearchType").addText("2");
		param.addElement("SearchInfo").addText("" + StbMac);
		return document.asXML();
	}

	public UserImageDAO getDao()
	{
		return dao;
	}

	public void setDao(UserImageDAO dao)
	{
		this.dao = dao;
	}
}
