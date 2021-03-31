package com.linkage.module.gtms.stb.resource.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.gtms.stb.resource.serv.StbSourceBIO;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年6月11日
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */

@SuppressWarnings("rawtypes")
public class StbSourceACT extends splitPageAction implements ServletRequestAware {
	private static final long serialVersionUID = 1L;
	 
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(StbSourceACT.class);

	StbSourceBIO bio = null;
	
	
	/**
	 * 光猫信息
	 */
	//LOID
	private String loid = null;	
	//序列号
	private String devSn = null;
	private String mac;
	private String netUsername;
	
	/**
	 * 机顶盒信息
	 * @return
	 */
	private String servAccount;
	private String stbMac;
	private String stbdevSn;
	
	private List devSourceList;
	

	/**
	 * execute
	 */
	public String execute() throws Exception {
		logger.debug("execute()");
		return "success";
	}
	
	/**
	 * 查询结果
	 * @return
	 */
	public String qryStbSource(){
		devSourceList = bio.qryStbSource(loid, devSn, mac, netUsername, servAccount, stbdevSn, stbMac,curPage_splitPage,
				num_splitPage);
		int total = bio.qryStbResCount(loid, devSn, mac, netUsername, servAccount, stbdevSn, stbMac);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "list";
	}

	public StbSourceBIO getBio()
	{
		return bio;
	}

	
	public void setBio(StbSourceBIO bio)
	{
		this.bio = bio;
	}

	
	public String getLoid()
	{
		return loid;
	}

	
	public void setLoid(String loid)
	{
		this.loid = loid;
	}

	
	public String getDevSn()
	{
		return devSn;
	}

	
	public void setDevSn(String devSn)
	{
		this.devSn = devSn;
	}

	
	public String getMac()
	{
		return mac;
	}

	
	public void setMac(String mac)
	{
		this.mac = mac;
	}

	
	public String getNetUsername()
	{
		return netUsername;
	}

	
	public void setNetUsername(String netUsername)
	{
		this.netUsername = netUsername;
	}

	
	public String getServAccount()
	{
		return servAccount;
	}

	
	public void setServAccount(String servAccount)
	{
		this.servAccount = servAccount;
	}

	
	public String getStbMac()
	{
		return stbMac;
	}

	
	public void setStbMac(String stbMac)
	{
		this.stbMac = stbMac;
	}

	
	public String getStbdevSn()
	{
		return stbdevSn;
	}

	
	public void setStbdevSn(String stbdevSn)
	{
		this.stbdevSn = stbdevSn;
	}

	public List getDevSourceList()
	{
		return devSourceList;
	}

	public void setDevSourceList(List devSourceList)
	{
		this.devSourceList = devSourceList;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0)
	{
		// TODO Auto-generated method stub
		
	}

}
