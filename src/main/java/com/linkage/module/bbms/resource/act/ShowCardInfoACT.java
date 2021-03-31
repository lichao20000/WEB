
package com.linkage.module.bbms.resource.act;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.bbms.resource.bio.ShowCardInfoBIO;
import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author wangsnebo
 * @date 2009-10-12
 */
public class ShowCardInfoACT extends ActionSupport
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(ShowCardInfoACT.class);
	/**
	 * 网关ID
	 */
	private String deviceId = null;
	/**
	 * 返回页面的Map,数据卡信息的对象
	 */
	private Map dataCardInfoMap;
	/**
	 * 返回页面的Map,uim卡信息的对象
	 */
	private Map uimCardInfoMap;
	/**
	 * 返回页面的Map,卡使用状态的对象
	 */
	private Map cardUseStatMap;
	/**
	 * BIO
	 */
	private ShowCardInfoBIO showCardInfoBio;
	/** ajax */
	private String ajax = "";

	/**
	 * @return the cardUseStatMap
	 */
	public Map getCardUseStatMap()
	{
		return cardUseStatMap;
	}

	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		logger.debug("getAjax()");
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax)
	{
		logger.debug("setAjax({})", ajax);
		this.ajax = ajax;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId()
	{
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	/**
	 * @return the dataCardInfoMap
	 */
	public Map getDataCardInfoMap()
	{
		return dataCardInfoMap;
	}

	/**
	 * @return the uimCardInfoMap
	 */
	public Map getUimCardInfoMap()
	{
		return uimCardInfoMap;
	}

	/**
	 * @return the showCardInfoBio
	 */
	public ShowCardInfoBIO getShowCardInfoBio()
	{
		return showCardInfoBio;
	}

	/**
	 * @param showCardInfoBio
	 *            the showCardInfoBio to set
	 */
	public void setShowCardInfoBio(ShowCardInfoBIO showCardInfoBio)
	{
		this.showCardInfoBio = showCardInfoBio;
	}

	/**
	 * 获取数据卡和uim卡列表,初始化页面
	 * 
	 * @author wangsenbo
	 * @date 2009-10-12
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		dataCardInfoMap = showCardInfoBio.getDataCardInfoMap(deviceId);
		uimCardInfoMap = showCardInfoBio.getUimCardInfoMap(deviceId);
		return "success";
	}

	/**
	 * 获取EVDO卡插拔状态
	 * 
	 * @author wangsenbo
	 * @date 2009-10-5
	 * @return String
	 */
	public String searchCardPlugStat()
	{
		logger.debug("searchCardPlugStat()");
		 String plugStat = showCardInfoBio.searchCardPlugStat(deviceId);
		 if (false == StringUtil.IsEmpty(plugStat))
		 {
		 ajax = plugStat;
		 }
		 else
		 {
		 ajax = "查询数据失败";
		 }
		return "ajax";
	}

	/**
	 * 获取EVDO卡使用状态
	 * 
	 * @author wangsenbo
	 * @date 2009-10-5
	 * @return String
	 */
	public String searchCardUseStat()
	{
		logger.debug("searchCardUseStat()");
		cardUseStatMap = showCardInfoBio.searchCardUseStat(deviceId);
		ajax = StringUtil.getStringValue(cardUseStatMap.get("useStat")) + ";"
		+ StringUtil.getStringValue(cardUseStatMap.get("workMode"));
		return "ajax";
	}
}
