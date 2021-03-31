package com.linkage.module.liposs.monitor.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import action.splitpage.splitPageAction;

import com.linkage.module.liposs.monitor.bio.DbtableSpaceInfoBio;

/**
 * 数据库表空间监控
 *
 */
public class DbtableSpaceInfoAction  extends splitPageAction implements ServletRequestAware,ServletResponseAware
{
    /**
     * 序列
     */
    private static final long serialVersionUID = 2978942171925530648L;

    private static final Logger LOG = Logger.getLogger(DbtableSpaceInfoAction.class);

    private List<Map<String,String>> tableSpaceInfoList = null;
    
    private DbtableSpaceInfoBio bio = null;
    
    /**
     * 主机监控平台
     * @return
     */
    public String tableSpaceInfoList(){
    	//this.monitorHostList = mbio.getAllMonitorHostList();
    	this.tableSpaceInfoList = bio.getList();	
    	return "tableSpaceInfoList";
    }
    
    
	public DbtableSpaceInfoBio getBio() {
		return bio;
	}

	public void setBio(DbtableSpaceInfoBio bio) {
		this.bio = bio;
	}


	public List<Map<String, String>> getTableSpaceInfoList() {
		return tableSpaceInfoList;
	}


	public void setTableSpaceInfoList(List<Map<String, String>> tableSpaceInfoList) {
		this.tableSpaceInfoList = tableSpaceInfoList;
	}


	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		
	}

}
