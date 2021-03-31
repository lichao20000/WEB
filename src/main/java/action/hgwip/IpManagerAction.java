package action.hgwip;

import static action.cst.ADD;
import static action.cst.AJAX;
import static action.cst.DEL;

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
 * ip地址管理的入口类，起始action
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-11-06
 * @category ipmg
 */
public class IpManagerAction extends ActionSupport implements SessionAware
{
	/**
	 * 
	 */
	/** log */
	private static Logger logger = LoggerFactory.getLogger(IpManagerAction.class);
	private static final long serialVersionUID = -1619955441753753481L;
	private Map session;// 服务器的会话对象
	private IPManagerDAO ipmdao;// 获取用户信息类
	private int userStat;// 用户状态
	private String userCity;// ip地址的用户city_id,省中心为原来的city_id,地市人员的为所在的一级地市city_id
	private List<Map> ipInfos;// 显示的ip信息
	private UserRes res;// 用户资源
	private boolean admin;//
	private String ip;// 查询的ip地址
	private String cutStatus;// 划分的状态
	private SubnetOperationDAO sopdao;// 查询ip地址的dao
	private String ajax;// ajax结果的字符串
	private int netMaskLen;// 掩码长度
	private String ipAdr;// 新增的ip地址资源
	private String comment;// ip注释
	private String netMask;// 掩码
	private String subnet;// 网段地址
	private String subnetGrp;// 网段父节点
	/**
	 * 起始的查询界面
	 */
	@Override
	public String execute() throws Exception
	{
		res = (UserRes) session.get("curUser");
		String city_id = res.getCityId();
		userStat = ipmdao.getUserState(city_id);
		ipInfos = sopdao.getSubnetList(ip, cutStatus, userStat, city_id);
		return SUCCESS;
	}
	/**
	 * 根据掩码长度返回掩码
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getNtMk() throws Exception
	{
		ajax = ipmdao.getNetMask(netMaskLen);
		logger.debug("getNtMk______________________________-ajax:"+ajax);
		return AJAX;
	}
	/**
	 * 增加ip地址
	 * 
	 * @return
	 */
	public String add() throws Exception
	{
		res = (UserRes) session.get("curUser");
		String city_id = res.getCityId();
		userCity = ipmdao.getIPCityID(city_id);
		int addStatus = sopdao.addRootSubnet(ipAdr, netMaskLen, netMask, null,
				comment, userCity);
		ajax = String.valueOf(addStatus);
		return ADD;
	}
	/**
	 * 删除网段的action
	 * 
	 * @return
	 * @throws Exception
	 */
	public String del() throws Exception
	{
		int del = sopdao.deleteSubnet(subnet, netMaskLen, subnetGrp);
		ajax = String.valueOf(del);
		return DEL;
	}
	
	public void setSession(Map session)
	{
		this.session = session;
	}
	public void setIpmdao(IPManagerDAO ipmdao)
	{
		this.ipmdao = ipmdao;
	}
	public int getUserStat()
	{
		return userStat;
	}
	public String getUserCity()
	{
		return userCity;
	}
	public List<Map> getIpInfos()
	{
		return ipInfos;
	}
	public boolean isAdmin()
	{
		admin = userStat == IPGlobal.SZX_USER;
		return admin;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	public void setCutStatus(String cutStatus)
	{
		this.cutStatus = cutStatus;
	}
	public void setSopdao(SubnetOperationDAO sopdao)
	{
		this.sopdao = sopdao;
	}
	public String getAjax()
	{
		return ajax;
	}
	public void setNetMaskLen(int netMaskLen)
	{
		this.netMaskLen = netMaskLen;
	}
	public void setIpAdr(String ipAdr)
	{
		this.ipAdr = ipAdr;
	}
	public void setComment(String comment)
	{
		logger.debug("============\n" + comment);
		// 通过ajax传递中文，需要将字符集合转码的。
		try
			{
				this.comment = java.net.URLDecoder.decode(comment, "UTF-8");
			} catch (Exception e)
			{
				this.comment = comment;
			}
	}
	public void setNetMask(String netMask)
	{
		this.netMask = netMask;
	}
	public void setSubnet(String subnet)
	{
		this.subnet = subnet;
	}
	public void setSubnetGrp(String subnetGrp)
	{
		this.subnetGrp = subnetGrp;
	}
	public String getIp()
	{
		return ip;
	}
	public String getCutStatus()
	{
		return cutStatus;
	}
}
