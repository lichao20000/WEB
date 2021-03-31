package action.hgwip;

import static action.cst.AJAX;
import static action.cst.EDIT;

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
 * ip地址管理的ip取消/回收的管理action
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-11-12
 * @category ipmg
 */
public class AssignIPAction extends ActionSupport implements SessionAware
{
	/**
	 * 
	 */
	/** log */
	private static Logger logger = LoggerFactory.getLogger(AssignIPAction.class);
	private static final long serialVersionUID = -5435036937884120339L;
	private String attr;// ip的地址详情
	private String url;// 根据不同的分配状态和用户的权限，进行不同的跳转
	private String userstat;// 用户状态
	private String leaf;// 是否是叶子
	private String subnet;// 子网
	private String subnetGrp;// 父节点
	private String inetMask;// 长度
	private final static String CANSUBNET = "cancelsubnet";// 取消子网划分
	private final static String RECYCLE = "recycleip";// 回收ip地址
	private SubnetOperationDAO sbdao;// dao类，为了提供子网详情的访问格式
	private IPManagerDAO ipdao;
	List<Map> result;// 回收的ip列表
	private int stat = -1;// -1：原始状态， 操作状态 0 取消列表，需要确认 1,取消成功 2,取消失败
	// -------------------------------省市用户的初始化参数
	private List<Map> purpose1;//
	private List<Map> purpose2;//
	private List<Map> purpose3;//
	private String comment;// 备注
	private String purpose1Name;// 用途一
	private String purpose2Name;// 用途一
	private String purpose3Name;// 用途一
	private String city_id;// 城市属地
	private String countryName;// 地市
	private String assigntime;// 分配时间
	// 是否分配状态 该状态在分配给专线用户的时候，需要判断，如果为 1，则正常展示信息，
	// 如果为 5 则判断 approve： approve==1:提示网段上报审批没通过，请回收IP资源 approve!=1:提示网段在上报审批中
	private int approve;// ^^^^
	private String assign;// ^^^^
	private List<Map> firstCity;// 一级用户的属性列表
	private List<Map> countryCity;// 地县的属地列表
	private UserRes res;// 用户资源
	private Map session;
	private String ajax;//
	// ------------------------------------专线用户信息初始化-------------------------------------
	private HashMap<String, String> params = new HashMap<String, String>();// 为了修改而初始化，专线用户的参数过多，封装个map传给后台
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
	private String cntmode;// 用户接入方式
	private List<Map> cntmodeList;// 用户接入方式列表
	private String cntspeed;// 用户接入速率
	private List<Map> cntspeedList;// 用户接入速率列表
	@Override
	public String execute() throws Exception
	{
		getinfo();
		if ("true".equals(leaf))
			{
				// 省市的初始化
				if ("0".equals(userstat))
					{
						purpose1 = ipdao.getDataByType("purpose1");
						purpose2 = ipdao.getDataByType("purpose2");
						purpose3 = ipdao.getDataByType("purpose3");
						Map<String, String> m = sbdao.getDetailIpMainInfo(
								subnet, Integer.parseInt(inetMask));
						subnet = m.get("subnet");
						comment = m.get("subnetcomment");
						assigntime = m.get("assigntime");
						purpose1Name = m.get("purpose1");
						purpose2Name = m.get("purpose2");
						purpose3Name = m.get("purpose3");
						city_id = m.get("city_id");
						countryName = m.get("country");
						res = (UserRes) session.get("curUser");
						String city_ids = res.getCityId();
						firstCity = ipdao.getFirstLevelCity(city_ids);
						countryCity = ipdao.getCountryByCity(city_id);
					} else
					{
						// TODO,需要判断状态，显示提示信息
						// 专线用户的初始化
						if (!"5".equals(assign))
							{
								params = (HashMap) sbdao.getDetailUserIPInfo(
										subnet, subnetGrp, Integer
												.parseInt(inetMask));
								cntmodeList = ipdao.getDataByType("cntmode");
								cntspeedList = ipdao.getDataByType("cntspeed");
								rwaddr = params.get("rwaddr");
								countryCity = ipdao.getCountryByCity(rwaddr);
								countryName = params.get("country");
								res = (UserRes) session.get("curUser");
								String city_ids = res.getCityId();
								firstCity = ipdao.getFirstLevelCity(city_ids);
							} else
							{
								// 是否分配状态 该状态在分配给专线用户的时候，需要判断，如果为 1，则正常展示信息，
								// 如果为 5 则判断 approve： approve==1:提示网段上报审批没通过，请回收IP资源 approve!=1:提示网段在上报审批中
								approve = sbdao.getUserAssignStatus(subnet, subnetGrp, Integer.parseInt(inetMask));
								logger.debug("================================测试"+approve);
							}
					}
			}
		return SUCCESS;
	}
	/**
	 * 编辑分配的用户资料，省市，专线用户公用
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editUser() throws Exception
	{
		getinfo();
		// 省市的资料编辑
		if ("0".equals(userstat))
			{
				ajax = (sbdao.modifyToCitySubnet(subnet, subnetGrp, Integer
						.parseInt(inetMask), city_id, countryName,
						purpose1Name, purpose2Name, purpose3Name, comment) == IPGlobal.SUNCCESS) ? "ok"
						: "fail";
			} else
			{
				// 地市的数据保存
				try
					{
						ajax = (sbdao.modifyToUserSubnet(subnet, subnetGrp,
								Integer.parseInt(inetMask), params) == IPGlobal.SUNCCESS) ? "ok"
								: "fail";
					} catch (Exception e)
					{
						logger.error("保存修改的分配信息的用户资料发生错误", e);
					}
			}
		return EDIT;
	}
	/**
	 * 回收ip地址
	 * 
	 * @return
	 */
	public String recycleIp()
	{
		getinfo();
		ajax = (sbdao.cancelAssignSubnet(subnet, subnetGrp, Integer
				.parseInt(inetMask)) == IPGlobal.SUNCCESS) ? "ok" : "fail";
		return AJAX;
	}
	/**
	 * 取消子网划分
	 * 
	 * @return
	 * @throws Exception
	 */
	public String cancelSubNet() throws Exception
	{
		getinfo();
		if (stat == -1)
			{
				if (IPGlobal.SZX_USER == Integer.parseInt(userstat))
					{
						result = sbdao.getChildSubnetIpMainList(subnet,
								subnetGrp, Integer.parseInt(inetMask));
					} else
					{
						result = sbdao.getChildSubnetUseripList(subnet,
								subnetGrp, Integer.parseInt(inetMask));
					}
				if (result == null || result.size() == 0)
					{
						int re = sbdao.cancelPartSubnet(subnet, subnetGrp,
								Integer.parseInt(inetMask));
						if (re == IPGlobal.SUNCCESS)
							{
								stat = 1;// 成功
							} else
							{
								stat = 2;// 失败
							}
					} else
					{
						stat = 0;
					}
			} else
			{
				int re = sbdao.cancelPartSubnet(subnet, subnetGrp, Integer
						.parseInt(inetMask));
				if (re == IPGlobal.SUNCCESS)
					{
						stat = 1;// 成功
					} else
					{
						stat = 2;// 失败
					}
			}
		return CANSUBNET;
	}
	/**
	 * 回收ip地址
	 * 
	 * @return
	 */
	public String recycleIP()
	{
		return RECYCLE;
	}
	public String getAttr()
	{
		return attr;
	}
	public void setAttr(String attr)
	{
		this.attr = attr;
	}
	public String getUrl()
	{
		return url;
	}
	public String getLeaf()
	{
		return leaf;
	}
	public String getUserstat()
	{
		return userstat;
	}
	private void getinfo()
	{
		String[] attrs = attr.split("/");
		leaf = attrs[5];
		userstat = attrs[0];
		subnet = attrs[1];
		subnetGrp = attrs[2];
		inetMask = attrs[3];
		assign = attrs[4];
	}
	public void setSbdao(SubnetOperationDAO sbdao)
	{
		this.sbdao = sbdao;
	}
	public List<Map> getResult()
	{
		return result;
	}
	public int getStat()
	{
		return stat;
	}
	public void setStat(int stat)
	{
		this.stat = stat;
	}
	public void setIpdao(IPManagerDAO ipdao)
	{
		this.ipdao = ipdao;
	}
	public String getSubnet()
	{
		return subnet;
	}
	public void setSubnet(String subnet)
	{
		this.subnet = subnet;
	}
	public String getInetMask()
	{
		return inetMask;
	}
	public void setInetMask(String inetMask)
	{
		this.inetMask = inetMask;
	}
	public List<Map> getPurpose1()
	{
		return purpose1;
	}
	public void setPurpose1(List<Map> purpose1)
	{
		this.purpose1 = purpose1;
	}
	public List<Map> getPurpose2()
	{
		return purpose2;
	}
	public void setPurpose2(List<Map> purpose2)
	{
		this.purpose2 = purpose2;
	}
	public List<Map> getPurpose3()
	{
		return purpose3;
	}
	public void setPurpose3(List<Map> purpose3)
	{
		this.purpose3 = purpose3;
	}
	public String getComment()
	{
		return comment;
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
	public String getCity_id()
	{
		return city_id;
	}
	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}
	public String getCountryName()
	{
		return countryName;
	}
	public String getAssigntime()
	{
		return assigntime;
	}
	
	public void setSession(Map session)
	{
		this.session = session;
	}
	public List<Map> getFirstCity()
	{
		return firstCity;
	}
	public List<Map> getCountryCity()
	{
		return countryCity;
	}
	public String getAjax()
	{
		return ajax;
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
		params.put("country", countryName);
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
	public void setSubnetGrp(String subnetGrp)
	{
		this.subnetGrp = subnetGrp;
	}
	public Map<String, String> getParams()
	{
		return params;
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
		params.put("usercountry", this.usercountry);
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
		params.put("addrnum", addrnum);
	}
	public String getRwaddr()
	{
		return rwaddr;
	}
	public String getAssign()
	{
		return assign;
	}
	public int getApprove()
	{
		return approve;
	}
}
