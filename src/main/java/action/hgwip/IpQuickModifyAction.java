package action.hgwip;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;

import dao.hgwip.IPManagerDAO;
import dao.hgwip.SubnetOperationDAO;

public class IpQuickModifyAction extends ActionSupport implements SessionAware
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(IpQuickModifyAction.class);
	private Map session;// 服务器的会话对象
	private SubnetOperationDAO snoDao;//
	private IPManagerDAO ipmDao;
	private List getIPList=null;//返回未分配的IP信息
	private String IpAdd="";//传入的参数IP地址
	private String cityid="";//传入的参数：所属地市
	private String country="";//传入的参数：县
	private String used1="";//传入的参数：用途1
	private String used2="";//传入的参数：用途2
	private String used3="";//传入的参数：用途3
	private UserRes res;// 用户资源
	private int total=0;//返回未分配IP记录条数
	private List getCityList=null;//返回一级地市的信息
	private List getCList=null;//返回县市信息
	private List UsedList1=null;//返回用途1
	private List UsedList2=null;//返回用途2
	private List UsedList3=null;//返回用途3
	
	@Override
	public String execute() throws Exception
	{
		logger.debug("===========getData=============");
		logger.debug("IpAdd="+IpAdd);
		logger.debug("cityid="+cityid);
		logger.debug("country="+country);
		logger.debug("used1="+used1);
		logger.debug("used2="+used2);
		logger.debug("used3="+used3);
		logger.debug("==============end=============");
		res = (UserRes) session.get("curUser");
		String id = res.getCityId();
		//获取未分配的IP地址信息
		getIPList=snoDao.getAssignSubnetList(IpAdd,cityid,country,used1,used2,used3,id);
		//获取未分配的IP地址的个数
		total=getIPList.size();
		//获取当前用户的所有地市信息
		getCityList=ipmDao.getFirstLevelCity(id);
		//获取第一个地市的县市信息
		Map map=(Map)getCityList.get(0);
		getCList=ipmDao.getCountryByCity((String)map.get("city_id"));
		map=null;
		//获取用途1、用途2、用途3的信息
		UsedList1=ipmDao.getDataByType("purpose1");
		UsedList2=ipmDao.getDataByType("purpose2");
		UsedList3=ipmDao.getDataByType("purpose3");
		// TODO Auto-generated method stub
		return SUCCESS;
	}
	public List getGetIPList()
	{
		return getIPList;
	}
	public int getTotal()
	{
		return total;
	}
	public List getGetCityList()
	{
		return getCityList;
	}
	public List getGetCList()
	{
		return getCList;
	}
	public List getUsedList1()
	{
		return UsedList1;
	}
	public List getUsedList2()
	{
		return UsedList2;
	}
	public List getUsedList3()
	{
		return UsedList3;
	}
	public void setSession(Map session)
	{
		this.session = session;
	}
	public void setSnoDao(SubnetOperationDAO snoDao)
	{
		this.snoDao = snoDao;
	}
	public void setIpmDao(IPManagerDAO ipmDao)
	{
		this.ipmDao = ipmDao;
	}
	public void setIpAdd(String ipAdd)
	{
		IpAdd = ipAdd;
	}
	public void setRes(UserRes res)
	{
		this.res = res;
	}
	public void setCityid(String cityid)
	{
		this.cityid = cityid;
	}
	public void setCountry(String country)
	{
		this.country = country;
	}
	public void setUsed1(String used1)
	{
		this.used1 = used1;
	}
	public void setUsed2(String used2)
	{
		this.used2 = used2;
	}
	public void setUsed3(String used3)
	{
		this.used3 = used3;
	}
}
