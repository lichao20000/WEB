
package com.linkage.module.gtms.stb.resource.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.resource.dao.UserImageDAO;
import com.linkage.module.gtms.stb.resource.serv.UserImageBIO;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-12-15
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class UserImageACT extends splitPageAction implements SessionAware
{

	private static Logger logger = LoggerFactory.getLogger(UserImageDAO.class);
	private UserImageBIO bio;
	private Map session;
	// 1按设备查询；2按用户查询
	private String ftpenable;
	// loid(宽带主账号)
	private String UserName;
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	List<Map<String, String>> listshow = new ArrayList<Map<String, String>>();
	List<Map<String, String>> listStbMac = new ArrayList<Map<String, String>>();
	List<Map<String, String>> listStb = new ArrayList<Map<String, String>>();
	List<Map<String, String>> companynameList = new ArrayList<Map<String, String>>();
	List<Map<String, String>> otherList = new ArrayList<Map<String, String>>();
	
	List<Map<String, String>> phonenumber = new ArrayList<Map<String, String>>();
	List<Map<String, String>> getInformationList = new ArrayList<Map<String, String>>();
	private String netaccount;
	List phoneinfoList = new ArrayList();
	List companynamebig = new ArrayList();
	List listStbShow = new ArrayList();
	List getInformationList1 = new ArrayList();
	public String querydevice()
	{	
		logger.debug("UserImageACT====>querydevice({ftpenable},{UserName})", new Object[] {
				ftpenable, UserName });
		getInformationList1.add("1");
		String deviceid = "";
		String mac_address = "";
		String StbMac = "";
		String layer2_interface = "";
		String username = "";
		String ipaddress = "";
		int a = 0;
		getInformationList = bio.getInformation(ftpenable, UserName);
		// 查询deviceid
		list = bio.getDevice_id(ftpenable, UserName);
		if (list != null&&list.size()>0)
		{
			deviceid = list.get(0).get("device_id");
			username = list.get(0).get("username");
			// 查询设备下挂的所有mac地址
			listshow = bio.getMac_adderss(deviceid);
			if (listshow != null && listshow.size() > 0)
			{
				for (int i = 0; i < listshow.size(); i++)
				{
					mac_address = listshow.get(i).get("mac_address");
					layer2_interface = listshow.get(i).get("layer2_interface");
					ipaddress = listshow.get(i).get("ip_address");
					String str = mac_address.replaceAll("\\:", "").substring(0, 6);
					// 有匹配的数据，则表示该mac值是机顶盒mac，需要调用接口查询机顶盒信息展示
					listStbMac = bio.getStbMac(mac_address);
					if (listStbMac != null && listStbMac.size() > 0)
					{
						StbMac = listStbMac.get(0).get("stb_mac");
						listStb = bio.xmlStr(StbMac, layer2_interface, mac_address,
								ipaddress);
						if(listStb!=null&&listStb.size()>0)
						{
						listStbShow.add(listStb);
						}
					}
					else
					{
						companynameList = bio.companyname(str, layer2_interface,
								mac_address, ipaddress);
						if (companynameList != null && companynameList.size() > 0)
						{
							companynamebig.add(companynameList);
						}
						else
						{
							List<Map<String, String>> phone = new ArrayList<Map<String, String>>();
							// 匹配手机库
							phone = bio.getphoneinfo(username, layer2_interface,
									mac_address, ipaddress);
							if (phone != null && phone.size() > 0)
							{
								phoneinfoList.add(phone);
							}
							else
							{
								HashMap<String, String> stbMap = new HashMap<String, String>();
								int top=bio.tote(mac_address);
								String lay="";
								stbMap.put("mac", listshow.get(i).get("mac_address"));
								if (!StringUtil.IsEmpty(layer2_interface))
								{
									String[] layer2 = layer2_interface.split("\\.");
									String layer = layer2[layer2.length-2];
									if (layer.equals("LANEthernetInterfaceConfig"))
									{
										lay = "LAN"
												+ layer2_interface.substring(layer2_interface.length() - 1,
														layer2_interface.length()) + "口";
									}
									else if (layer.equals("WLANConfiguration"))
									{
										lay = "SSID "
												+ layer2_interface.substring(layer2_interface.length() - 1,
														layer2_interface.length());
									}
								}
								if (!StringUtil.IsEmpty(lay))
								{
									stbMap.put("LAN", lay);
								}
								else
								{
									stbMap.put("LAN", lay);
								}
								stbMap.put("ipaddress", ipaddress);
								stbMap.put("tote", String.valueOf(top));
								otherList.add(stbMap);
							}
						}
					}
				}
			}
		}
		return "list";
	}
public String phonenumber()
{
	phonenumber=bio.getphonenumber(netaccount);
	return "phone";
}
	public UserImageBIO getBio()
	{
		return bio;
	}

	public void setBio(UserImageBIO bio)
	{
		this.bio = bio;
	}

	public Map getSession()
	{
		return session;
	}

	@Override
	public void setSession(Map session)
	{
		this.session = session;
	}

	public String getFtpenable()
	{
		return ftpenable;
	}

	public void setFtpenable(String ftpenable)
	{
		this.ftpenable = ftpenable;
	}

	public String getUserName()
	{
		return UserName;
	}

	public void setUserName(String userName)
	{
		UserName = userName;
	}

	public List<Map<String, String>> getList()
	{
		return list;
	}

	public void setList(List<Map<String, String>> list)
	{
		this.list = list;
	}

	public List<Map<String, String>> getListshow()
	{
		return listshow;
	}

	public void setListshow(List<Map<String, String>> listshow)
	{
		this.listshow = listshow;
	}

	public List<Map<String, String>> getListStbMac()
	{
		return listStbMac;
	}

	public void setListStbMac(List<Map<String, String>> listStbMac)
	{
		this.listStbMac = listStbMac;
	}

	public List<Map<String, String>> getListStb()
	{
		return listStb;
	}

	public void setListStb(List<Map<String, String>> listStb)
	{
		this.listStb = listStb;
	}

	public List<Map<String, String>> getCompanynameList()
	{
		return companynameList;
	}

	public void setCompanynameList(List<Map<String, String>> companynameList)
	{
		this.companynameList = companynameList;
	}

	public List<Map<String, String>> getOtherList()
	{
		return otherList;
	}

	public void setOtherList(List<Map<String, String>> otherList)
	{
		this.otherList = otherList;
	}

	public List<Map<String, String>> getGetInformationList()
	{
		return getInformationList;
	}

	public void setGetInformationList(List<Map<String, String>> getInformationList)
	{
		this.getInformationList = getInformationList;
	}

	public List getPhoneinfoList()
	{
		return phoneinfoList;
	}

	public void setPhoneinfoList(List phoneinfoList)
	{
		this.phoneinfoList = phoneinfoList;
	}

	public List getCompanynamebig()
	{
		return companynamebig;
	}

	public void setCompanynamebig(List companynamebig)
	{
		this.companynamebig = companynamebig;
	}

	
	public List getListStbShow()
	{
		return listStbShow;
	}

	
	public void setListStbShow(List listStbShow)
	{
		this.listStbShow = listStbShow;
	}

	
	public List getGetInformationList1()
	{
		return getInformationList1;
	}

	
	public void setGetInformationList1(List getInformationList1)
	{
		this.getInformationList1 = getInformationList1;
	}

	
	public String getNetaccount()
	{
		return netaccount;
	}

	
	public void setNetaccount(String netaccount)
	{
		this.netaccount = netaccount;
	}
	
	public List<Map<String, String>> getPhonenumber()
	{
		return phonenumber;
	}
	
	public void setPhonenumber(List<Map<String, String>> phonenumber)
	{
		this.phonenumber = phonenumber;
	}
	
}
