package action.bbms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import action.cst;
import bio.bbms.GetSnmpInfoBIO;
import bio.hgwip.IpTool;

import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;

import dao.bbms.GetSnmpInfoDAO;

/**
 * @author wangzhimeng(工号) tel：12345678
 * @version 1.0
 * @since 2008-6-5
 * @category action.bbms 版权：南京联创科技 网管科技部
 *
 */
public class GetSnmpInfoAction extends ActionSupport implements ServletRequestAware
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7631202481894548707L;
	// 设备id
	String device_id;
	// oid类型
	String oid_type;
	//
	String ajax;
	// bio
	GetSnmpInfoBIO gsbio;
	// dao
	GetSnmpInfoDAO getSnmpInfo;
	// 0为get 1为walk
	private int type = 0;
	// 采集方式 0：SNMP 1：tr069
	private int gatherType;
	// request取登陆帐号使用
	private String netAddr;
	private String netMask;
	private HttpServletRequest request;
	/**
	 * 显示DHCP信息页面
	 */
	public String execute() throws Exception
	{
		return SUCCESS;
	}
	/**
	 * 获取对应的起始和结束地址
	 *
	 * @return
	 * @throws Exception
	 */
	public String getHLAddress() throws Exception
	{
		ajax = IpTool.getHighAddr(netAddr, netMask) + "-"
				+ IpTool.getLowAddr(netAddr, netMask);
		return cst.AJAX;
	}
	/**
	 * 显示vlan信息页面
	 *
	 * @return
	 * @throws Exception
	 */
	public String getVlanInfo() throws Exception
	{
		return "vlan";
	}
	/**
	 * 实时采集数据
	 *
	 * @return
	 * @throws Exception
	 */
	public String getSnmpInfo() throws Exception
	{
		// request
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		ajax = ((type == 0) ? gsbio.getSnmpInfoBIO(device_id, oid_type,
				curUser.getUser(), gatherType) : gsbio.walkSnmpInfoBIO(device_id,
				oid_type, curUser.getUser(), gatherType));
		return cst.AJAX;
	}
	/**
	 * 查询设备的mac信息
	 *
	 * @return
	 * @throws Exception
	 */
	public String getMacInfo() throws Exception
	{
		ajax = getSnmpInfo.getMacInfo(device_id);
		return cst.AJAX;
	}
	/**
	 * 根据SNMP方式采集mac地址
	 * @return
	 * @throws Exception
	 */
	public String getMacInfoSnmp() throws Exception
	{
		ajax = gsbio.getMacInfo(device_id);
		return cst.AJAX;
	}
	/**
	 * 采集设备vlan的端口列表
	 *
	 * @return
	 * @throws Exception
	 */
	public String getVlanPort() throws Exception
	{
		// request
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		ajax = gsbio.getVlanPort(device_id, curUser.getUser(), gatherType);
		return cst.AJAX;
	}
	/**
	 * 根据tr069方式获取数据
	 * @return
	 * @throws Exception
	 */
	public String getInfoByTr069() throws Exception
	{
		// request
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		ajax = gsbio.getInfoByTr069(device_id, oid_type, curUser.getUser());
		return cst.AJAX;
	}
	public String getDevice_id()
	{
		return device_id;
	}
	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}
	public String getOid_type()
	{
		return oid_type;
	}
	public void setOid_type(String oid_type)
	{
		this.oid_type = oid_type;
	}
	public String getAjax()
	{
		return ajax;
	}
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	public GetSnmpInfoBIO getGsbio()
	{
		return gsbio;
	}
	public void setGsbio(GetSnmpInfoBIO gsbio)
	{
		this.gsbio = gsbio;
	}
	public void setGetSnmpInfo(GetSnmpInfoDAO getSnmpInfo)
	{
		this.getSnmpInfo = getSnmpInfo;
	}
	public int getGatherType()
	{
		return gatherType;
	}
	public void setGatherType(int gatherType)
	{
		this.gatherType = gatherType;
	}
	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public String getNetAddr()
	{
		return netAddr;
	}
	public void setNetAddr(String netAddr)
	{
		this.netAddr = netAddr;
	}
	public String getNetMask()
	{
		return netMask;
	}
	public void setNetMask(String netMask)
	{
		this.netMask = netMask;
	}
}
