package com.linkage.module.gwms.resource.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.resource.bio.OfficeVoipBIO;

/**
 * @author Jason(3412)
 * @date 2009-9-25
 */
public class OfficeVoipACT extends splitPageAction implements SessionAware {

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(OfficeVoipACT.class);
	//返回页面的List,局向对应VOIP服务器地址的对象
	private List officeVoipList;
	//session
	private Map session;
	//BIO
	private OfficeVoipBIO officeVoipBio;
	//AJAX
	private String ajax;
	
	/**
	 * 获取列表,初始化页面
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-25
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		officeVoipList = officeVoipBio.getOfficeVoipList(cityId, curPage_splitPage,
				num_splitPage);
		maxPage_splitPage = officeVoipBio.getOfficeVoipCount(cityId, curPage_splitPage,
				num_splitPage);
		return "success";
	}
	
	public String goPage()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		officeVoipList = officeVoipBio.getOfficeVoipList(cityId, curPage_splitPage,
				num_splitPage);
		maxPage_splitPage = officeVoipBio.getOfficeVoipCount(cityId, curPage_splitPage,
				num_splitPage);
		return "success";
	}
	
	public String updateOfficeVoip(){
		logger.debug("updateOfficeVoip()");
		
		//更新
		
		return "ajax";
	}
	
	
	
	/** getter, setter methods */
	
	public List getOfficeVoipList() {
		return officeVoipList;
	}

	public void setOfficeVoipBio(OfficeVoipBIO officeVoipBio) {
		this.officeVoipBio = officeVoipBio;
	}

	public String getAjax() {
		return ajax;
	}

	@Override
	public void setSession(Map arg0) {
		session = arg0;
	}

}
