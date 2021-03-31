package action.hgwip;

import static action.cst.AJAX;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.hgwip.IPGlobal;

import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;

import dao.hgwip.IPManagerDAO;
import dao.hgwip.SubnetOperationDAO;

/**
 * ip地址管理的ip分配的管理action
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-11-12
 * @category ipmg
 */
public class UnAssignIPAction extends ActionSupport implements SessionAware
{
	/**
	 * 
	 */
	/** log */
	private static Logger log = LoggerFactory.getLogger(UnAssignIPAction.class);
	private static final long serialVersionUID = 5509239168854825826L;
	private String attr;
	private String userstat;// 用户状态
	private String leaf;// 是否是叶子
	private final static String CUTSUBORGIVE = "cutorgive";
	private final static String GETCONNTRY = "getcountry";
	private final static String ASSIGNIPCITY = "assignipcity";// 分配ip到地市
	private final static String ASSIGNIPUSER = "assignipuser";// 分配ip到用户
	private String act = "cut";// 划分子网还是分配ip
	private String icnt = "";// 子网位数，生成下拉列表
	private String subcnt = "";// 子网个数，生成下拉列表
	private String subnet;// 子网
	private String subnetGrp;// 父节点
	private String inetMask;// 长度
	private IPManagerDAO ipdao;
	private SubnetOperationDAO sbdao;// 子网操作
	private int sublen;// 要划分的子网
	private int link = 0;// 默认不级联划分
	private String ajax = "";
	private Map session;//
	private UserRes res;// 用户资源
	private List<Map> firstCity;// 一级用户的属性列表
	private List<Map> countryCity;// 地县的属地列表
	private String city_id;// 一级地市的id
	private String countryName;// 县市的名称
	private List<Map> purpose1;//
	private List<Map> purpose2;//
	private List<Map> purpose3;//
	private String comment;// 备注
	private String purpose1Name;// 用途一
	private String purpose2Name;// 用途一
	private String purpose3Name;// 用途一
	private int purForNet = 0;// 是否为网络用途而划分的，如果是，则不允许再次划分。0为非网络用途，该值为写死的值，非0为网络
	// ------------------------------专线用户指定ip----------------
	// ----------------专线用户指定ip给用户的相关参数,该功能后期进行了删改，可能参数个数和页面的个数不一样
	private String cntmode;// 用户接入方式
	private List<Map> cntmodeList;// 用户接入方式列表
	private String cntspeed;// 用户接入速率
	private List<Map> cntspeedList;// 用户接入速率列表
	private HashMap<String, String> params = new HashMap<String, String>();// 专线用户的参数过多，封装个map传给后台
	private String usernamezw;// 用户名称中文
	private String usernameyw;// 用户名称英文
	private String usernamepyjc;// 用户拼音简称
	private String usernameywjc;// 用户英文简称
	private String address;// 用户地址
	private String netname;// 用户网络名
	private String netnamee;// 用户网络英文名
	private String netnamejc;// 用户网络名简称
	private String cntaddr;// 互联地址
	private String localun;// 本地用户编号
	private String rwaddr;// 入网地点
	private String usercountry;// 县
	private String purpose;// 入网用途
	private String memo;// 备注
	private String managerhandle;// 用户网络管理负责人Handle
	private String managername;// 用户网络管理负责人姓名
	private String managernamep;// 用户网络管理负责人姓名拼音
	private String managerduty;// 用户网络管理负责人职务
	private String managerphone;// 用户网络管理负责人电话
	private String manageremail;// 用户网络管理负责人E-mail
	private String managerfax;// 用户网络管理负责人传真
	private String manageraddress;// 用户网络管理负责人通信地址
	private String manageraddrE;// 用户网络管理负责人英文通信地址
	private String managerpc;// 用户网络管理负责人邮政编码
	private String techhandle;// 用户网络技术负责人Handle
	private String techname;// 用户网络技术负责人姓名
	private String technamep;// 用户网络技术负责人姓名拼音
	private String techduty;// 用户网络技术负责人职务
	private String techphone;// 用户网络技术负责人电话
	private String techemail;// 用户网络技术负责人E-mail
	private String techfax;// 用户网络技术负责人传真
	private String techaddr;// 用户网络技术负责人通信地址
	private String techaddre;// 用户网络技术负责人英文通信地址
	private String techpc;// 用户网络技术负责人邮政编码
	private String addrnum;// 子网个数
	private boolean allow;// 是否允许划分
	@Override
	public String execute() throws Exception
	{
		getinfo();
		if (Integer.parseInt(userstat) == IPGlobal.CITY_USER)
			{
				// 需要判断是否为网络用途而分配的ip，是则不允许在此划分，只显示信息
				Map<String, String> m = sbdao.getDetailPurposeSubnet(subnet,
						Integer.parseInt(inetMask));
				if (m.size() != 0)
					{
						purForNet = 1;
						purpose1Name = m.get("purpose1");
						purpose2Name = m.get("purpose2");
						purpose3Name = m.get("purpose3");
					}
			}
		return SUCCESS;
	}
	/**
	 * 分配ip地址 or 划分子网
	 * 
	 * @return
	 * @throws Exception
	 */
	public String assingIP() throws Exception
	{
		getinfo();
		if ("cut".equals(act))
			{
				// 切分网段的初始化工作
				HashMap<String, String> m = ipdao
						.getInetMaskAndChildCount(Integer.parseInt(inetMask));
				for (int i = Integer.parseInt(inetMask); i < 30; i++)
					{
						int j = i + 1;
						icnt += "<option value='" + j + "'>" + j + "</option>";
						subcnt += "<option value='" + j + "'>"
								+ m.get(String.valueOf(j)) + "</option>";
					}
			} else
			{
				res = (UserRes) session.get("curUser");
				String city_id = res.getCityId();
				firstCity = ipdao.getFirstLevelCity(city_id);
				// 省公司指定ip
				if ("0".equals(userstat))
					{
						// 分配ip的初始化方法
						purpose1 = ipdao.getDataByType("purpose1");
						purpose2 = ipdao.getDataByType("purpose2");
						purpose3 = ipdao.getDataByType("purpose3");
					} else
					{
						// 地市给专线用户指定ip
						allow = sbdao
								.isAllowAssign(subnet, subnetGrp, Integer
										.parseInt(inetMask), ipdao
										.getIPCityID(city_id));
						rwaddr = ipdao.getIPCityID(city_id);
						if (allow)
							{
								cntmodeList = ipdao.getDataByType("cntmode");
								cntspeedList = ipdao.getDataByType("cntspeed");
							}
					}
			}
		return CUTSUBORGIVE;
	}
	/**
	 * 负责县市的获取
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCountry() throws Exception
	{
		countryCity = ipdao.getCountryByCity(city_id);
		ajax +="<option value=''>==请选择==</option>";
		for (Map m : countryCity)
			{
				ajax += "<option value='" + m.get("city_id").toString()
						+ "'>==" + m.get("city_name") + "==</option>";
			}
		return GETCONNTRY;
	}
	/**
	 * 划分子网
	 * 
	 * @return
	 */
	public String assignSubNet() throws Exception
	{
		getinfo();
		try
			{
				int stat = sbdao.partSubnet(subnet, subnetGrp, Integer
						.parseInt(inetMask), link == 1, sublen);
				ajax = (stat == IPGlobal.SUNCCESS) ? "ok"
						: (stat == IPGlobal.IS_FORBID ? "no"
								: (stat == IPGlobal.OVER_MAXLEVEl ? "over"
										: "fail"));
			} catch (Exception e)
			{
				log.error("划分子网时发生错误", e);
			}
		return AJAX;
	}
	/**
	 * 分配ip到地市的action
	 * 
	 * @return
	 * @throws Exception
	 */
	public String assignIPCity() throws Exception
	{
		getinfo();
		ajax = (sbdao.assignSubnetToCity(subnet, subnetGrp, Integer
				.parseInt(inetMask), city_id, countryName, purpose1Name,
				purpose2Name, purpose3Name, comment) == IPGlobal.SUNCCESS) ? "ok"
				: "fail";
		return ASSIGNIPCITY;
	}
	/**
	 * 分配ip地址给专线用户
	 * 
	 * @return
	 * @throws Exception
	 */
	public String assignIPUser() throws Exception
	{
		getinfo();
		try
			{
				ajax = (sbdao.assignSubnetToUser(subnet, subnetGrp, Integer
						.parseInt(inetMask), params) == IPGlobal.SUNCCESS) ? "ok"
						: "fail";
			} catch (Exception e)
			{
				log.error("分配ip到专线用户发生错误", e);
			}
		return ASSIGNIPUSER;
	}
	public String getAttr()
	{
		return attr;
	}
	public void setAttr(String attr)
	{
		this.attr = attr;
	}
	public String getUserstat()
	{
		return userstat;
	}
	public void setUserstat(String userstat)
	{
		this.userstat = userstat;
	}
	public String getLeaf()
	{
		return leaf;
	}
	public void setLeaf(String leaf)
	{
		this.leaf = leaf;
	}
	public String getAct()
	{
		return act;
	}
	public void setAct(String act)
	{
		this.act = act;
	}
	public String getIcnt()
	{
		return icnt;
	}
	/**
	 * 内部公用的切割信息
	 */
	private void getinfo()
	{
		String[] attrs = attr.split("/");
		leaf = attrs[5];
		userstat = attrs[0];
		subnet = attrs[1];
		subnetGrp = attrs[2];
		inetMask = attrs[3];
	}
	public String getSubcnt()
	{
		return subcnt;
	}
	public void setIpdao(IPManagerDAO ipdao)
	{
		this.ipdao = ipdao;
	}
	public void setSbdao(SubnetOperationDAO sbdao)
	{
		this.sbdao = sbdao;
	}
	public void setLink(int link)
	{
		this.link = link;
	}
	public void setIcnt(String icnt)
	{
		this.icnt = icnt;
	}
	public void setSublen(int sublen)
	{
		this.sublen = sublen;
	}
	public String getAjax()
	{
		return ajax;
	}
	public String getSubnet()
	{
		return subnet;
	}
	public void setSubnet(String subnet)
	{
		this.subnet = subnet;
	}
	public String getSubnetGrp()
	{
		return subnetGrp;
	}
	public String getInetMask()
	{
		return inetMask;
	}
	
	public void setSession(Map sesion)
	{
		this.session = sesion;
	}
	public List<Map> getFirstCity()
	{
		return firstCity;
	}
	public List<Map> getCountryCity()
	{
		return countryCity;
	}
	public void setFirstCity(List<Map> firstCity)
	{
		this.firstCity = firstCity;
	}
	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}
	public List<Map> getPurpose1()
	{
		return purpose1;
	}
	public List<Map> getPurpose2()
	{
		return purpose2;
	}
	public List<Map> getPurpose3()
	{
		return purpose3;
	}
	public void setCountryName(String countryName)
	{
		try
			{
				this.countryName = java.net.URLDecoder.decode(countryName,
						"UTF-8");
			} catch (Exception e)
			{
				this.countryName = countryName;
			}
	}
	public void setComment(String comment)
	{
		try
			{
				this.comment = java.net.URLDecoder.decode(comment, "UTF-8");
			} catch (Exception e)
			{
				this.comment = comment;
			}
	}
	public void setPurpose1Name(String purpose1Name)
	{
		try
			{
				this.purpose1Name = java.net.URLDecoder.decode(purpose1Name,
						"UTF-8");
			} catch (Exception e)
			{
				this.purpose1Name = purpose1Name;
			}
	}
	public void setPurpose2Name(String purpose2Name)
	{
		try
			{
				this.purpose2Name = java.net.URLDecoder.decode(purpose2Name,
						"UTF-8");
			} catch (Exception e)
			{
				this.purpose2Name = purpose2Name;
			}
	}
	public void setPurpose3Name(String purpose3Name)
	{
		try
			{
				this.purpose3Name = java.net.URLDecoder.decode(purpose3Name,
						"UTF-8");
			} catch (Exception e)
			{
				this.purpose3Name = purpose3Name;
			}
	}
	public List<Map> getCntmodeList()
	{
		return cntmodeList;
	}
	public List<Map> getCntspeedList()
	{
		return cntspeedList;
	}
	public void setCntmode(String cntmode)
	{
		try
			{
				this.cntmode = java.net.URLDecoder.decode(cntmode, "UTF-8");
			} catch (Exception e)
			{
				this.cntmode = cntmode;
			}
		params.put("cntmode", this.cntmode);
	}
	public void setCntspeed(String cntspeed)
	{
		try
			{
				this.cntspeed = java.net.URLDecoder.decode(cntspeed, "UTF-8");
			} catch (Exception e)
			{
				this.cntspeed = cntspeed;
			}
		params.put("cntspeed", this.cntspeed);
	}
	public void setUsernamezw(String usernamezw)
	{
		try
			{
				this.usernamezw = java.net.URLDecoder.decode(usernamezw,
						"UTF-8");
			} catch (Exception e)
			{
				this.usernamezw = usernamezw;
			}
		params.put("usernamezw", this.usernamezw);
	}
	public void setUsernameyw(String usernameyw)
	{
		try
			{
				this.usernameyw = java.net.URLDecoder.decode(usernameyw,
						"UTF-8");
			} catch (Exception e)
			{
				this.usernameyw = usernameyw;
			}
		params.put("usernameyw", this.usernameyw);
	}
	public void setUsernamepyjc(String usernamepyjc)
	{
		try
			{
				this.usernamepyjc = java.net.URLDecoder.decode(usernamepyjc,
						"UTF-8");
			} catch (Exception e)
			{
				this.usernamepyjc = usernamepyjc;
			}
		params.put("usernamepyjc", this.usernamepyjc);
	}
	public void setUsernameywjc(String usernameywjc)
	{
		try
			{
				this.usernameywjc = java.net.URLDecoder.decode(usernameywjc,
						"UTF-8");
			} catch (Exception e)
			{
				this.usernameywjc = usernameywjc;
			}
		params.put("usernameywjc", this.usernameywjc);
	}
	public void setAddress(String address)
	{
		try
			{
				this.address = java.net.URLDecoder.decode(address, "UTF-8");
			} catch (Exception e)
			{
				this.address = address;
			}
		params.put("address", this.address);
	}
	public void setNetname(String netname)
	{
		try
			{
				this.netname = java.net.URLDecoder.decode(netname, "UTF-8");
			} catch (Exception e)
			{
				this.netname = netname;
			}
		params.put("netname", this.netname);
	}
	public void setNetnamee(String netnamee)
	{
		try
			{
				this.netnamee = java.net.URLDecoder.decode(netnamee, "UTF-8");
			} catch (Exception e)
			{
				this.netnamee = netnamee;
			}
		params.put("netnamee", this.netnamee);
	}
	public void setNetnamejc(String netnamejc)
	{
		try
			{
				this.netnamejc = java.net.URLDecoder.decode(netnamejc, "UTF-8");
			} catch (Exception e)
			{
				this.netnamejc = netnamejc;
			}
		params.put("netnamejc", this.netnamejc);
	}
	public void setCntaddr(String cntaddr)
	{
		try
			{
				this.cntaddr = java.net.URLDecoder.decode(cntaddr, "UTF-8");
			} catch (Exception e)
			{
				this.cntaddr = cntaddr;
			}
		params.put("cntaddr", this.cntaddr);
	}
	public void setLocalun(String localun)
	{
		try
			{
				this.localun = java.net.URLDecoder.decode(localun, "UTF-8");
			} catch (Exception e)
			{
				this.localun = localun;
			}
		params.put("localun", this.localun);
	}
	public void setRwaddr(String rwaddr)
	{
		try
			{
				this.rwaddr = java.net.URLDecoder.decode(rwaddr, "UTF-8");
			} catch (Exception e)
			{
				this.rwaddr = rwaddr;
			}
		params.put("rwaddr", this.rwaddr);
	}
	public void setUsercountry(String usercountry)
	{
		try
			{
				this.usercountry = java.net.URLDecoder.decode(usercountry,
						"UTF-8");
			} catch (Exception e)
			{
				this.usercountry = usercountry;
			}
		params.put("country", this.usercountry);
	}
	public void setPurpose(String purpose)
	{
		try
			{
				this.purpose = java.net.URLDecoder.decode(purpose, "UTF-8");
			} catch (Exception e)
			{
				this.purpose = purpose;
			}
		params.put("purpose", this.purpose);
	}
	public void setMemo(String memo)
	{
		try
			{
				this.memo = java.net.URLDecoder.decode(memo, "UTF-8");
			} catch (Exception e)
			{
				this.memo = memo;
			}
		params.put("memo", this.memo);
	}
	public void setManagerhandle(String managerhandle)
	{
		try
			{
				this.managerhandle = java.net.URLDecoder.decode(managerhandle,
						"UTF-8");
			} catch (Exception e)
			{
				this.managerhandle = managerhandle;
			}
			log.debug("=====================管理人员的资料" + this.managerhandle);
		params.put("managerhandle", this.managerhandle);
	}
	public void setManagername(String managername)
	{
		try
			{
				this.managername = java.net.URLDecoder.decode(managername,
						"UTF-8");
			} catch (Exception e)
			{
				this.managername = managername;
			}
		params.put("managername", this.managername);
	}
	public void setManagernamep(String managernamep)
	{
		try
			{
				this.managernamep = java.net.URLDecoder.decode(managernamep,
						"UTF-8");
			} catch (Exception e)
			{
				this.managernamep = managernamep;
			}
		params.put("managernamep", this.managernamep);
	}
	public void setManagerduty(String managerduty)
	{
		try
			{
				this.managerduty = java.net.URLDecoder.decode(managerduty,
						"UTF-8");
			} catch (Exception e)
			{
				this.managerduty = managerduty;
			}
		params.put("managerduty", this.managerduty);
	}
	public void setManagerphone(String managerphone)
	{
		try
			{
				this.managerphone = java.net.URLDecoder.decode(managerphone,
						"UTF-8");
			} catch (Exception e)
			{
				this.managerphone = managerphone;
			}
		params.put("managerphone", this.managerphone);
	}
	public void setManageremail(String manageremail)
	{
		try
			{
				this.manageremail = java.net.URLDecoder.decode(manageremail,
						"UTF-8");
			} catch (Exception e)
			{
				this.manageremail = manageremail;
			}
		params.put("manageremail", this.manageremail);
	}
	public void setManagerfax(String managerfax)
	{
		try
			{
				this.managerfax = java.net.URLDecoder.decode(managerfax,
						"UTF-8");
			} catch (Exception e)
			{
				this.managerfax = managerfax;
			}
		params.put("managerfax", this.managerfax);
	}
	public void setManageraddress(String manageraddress)
	{
		try
			{
				this.manageraddress = java.net.URLDecoder.decode(
						manageraddress, "UTF-8");
			} catch (Exception e)
			{
				this.manageraddress = manageraddress;
			}
		params.put("manageraddress", this.manageraddress);
	}
	public void setManageraddrE(String manageraddrE)
	{
		try
			{
				this.manageraddrE = java.net.URLDecoder.decode(manageraddrE,
						"UTF-8");
			} catch (Exception e)
			{
				this.manageraddrE = manageraddrE;
			}
		params.put("manageraddrE", this.manageraddrE);
	}
	public void setManagerpc(String managerpc)
	{
		try
			{
				this.managerpc = java.net.URLDecoder.decode(managerpc, "UTF-8");
			} catch (Exception e)
			{
				this.managerpc = managerpc;
			}
		params.put("managerpc", this.managerpc);
	}
	public void setTechhandle(String techhandle)
	{
		try
			{
				this.techhandle = java.net.URLDecoder.decode(techhandle,
						"UTF-8");
			} catch (Exception e)
			{
				this.techhandle = techhandle;
			}
		params.put("techhandle", this.techhandle);
	}
	public void setTechname(String techname)
	{
		try
			{
				this.techname = java.net.URLDecoder.decode(techname, "UTF-8");
			} catch (Exception e)
			{
				this.techname = techname;
			}
		params.put("techname", this.techname);
	}
	public void setTechnamep(String technamep)
	{
		try
			{
				this.technamep = java.net.URLDecoder.decode(technamep, "UTF-8");
			} catch (Exception e)
			{
				this.technamep = technamep;
			}
		params.put("technamep", this.technamep);
	}
	public void setTechduty(String techduty)
	{
		try
			{
				this.techduty = java.net.URLDecoder.decode(techduty, "UTF-8");
			} catch (Exception e)
			{
				this.techduty = techduty;
			}
		params.put("techduty", this.techduty);
	}
	public void setTechphone(String techphone)
	{
		try
			{
				this.techphone = java.net.URLDecoder.decode(techphone, "UTF-8");
			} catch (Exception e)
			{
				this.techphone = techphone;
			}
		params.put("techphone", this.techphone);
	}
	public void setTechemail(String techemail)
	{
		try
			{
				this.techemail = java.net.URLDecoder.decode(techemail, "UTF-8");
			} catch (Exception e)
			{
				this.techemail = techemail;
			}
		params.put("techemail", this.techemail);
	}
	public void setTechfax(String techfax)
	{
		try
			{
				this.techfax = java.net.URLDecoder.decode(techfax, "UTF-8");
			} catch (Exception e)
			{
				this.techfax = techfax;
			}
		params.put("techfax", this.techfax);
	}
	public void setTechaddr(String techaddr)
	{
		try
			{
				this.techaddr = java.net.URLDecoder.decode(techaddr, "UTF-8");
			} catch (Exception e)
			{
				this.techaddr = techaddr;
			}
		params.put("techaddr", this.techaddr);
	}
	public void setTechaddre(String techaddre)
	{
		try
			{
				this.techaddre = java.net.URLDecoder.decode(techaddre, "UTF-8");
			} catch (Exception e)
			{
				this.techaddre = techaddre;
			}
		params.put("techaddre", this.techaddre);
	}
	public void setTechpc(String techpc)
	{
		try
			{
				this.techpc = java.net.URLDecoder.decode(techpc, "UTF-8");
			} catch (Exception e)
			{
				this.techpc = techpc;
			}
		params.put("techpc", this.techpc);
	}
	public void setAddrnum(String addrnum)
	{
		this.addrnum = addrnum;
		params.put("addrnum", this.addrnum);
	}
	public boolean isAllow()
	{
		return allow;
	}
	public String getAddrnum()
	{
		return addrnum;
	}
	public String getRwaddr()
	{
		return rwaddr;
	}
	public int getPurForNet()
	{
		return purForNet;
	}
	public String getPurpose1Name()
	{
		return purpose1Name;
	}
	public String getPurpose2Name()
	{
		return purpose2Name;
	}
	public String getPurpose3Name()
	{
		return purpose3Name;
	}
}
