package com.linkage.module.intelspeaker.verconfigfile.act;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.intelspeaker.verconfigfile.IntelSpeakerGlobal;
import com.linkage.module.intelspeaker.verconfigfile.bio.IntelSpeakerConfigMgrBio;
import com.linkage.module.intelspeaker.verconfigfile.entity.FilterTerminal;
import com.linkage.module.intelspeaker.verconfigfile.entity.FilterTerminalList;
import com.linkage.module.intelspeaker.verconfigfile.entity.Router;
import com.linkage.module.intelspeaker.verconfigfile.entity.RouterTerminal;
import com.linkage.module.intelspeaker.verconfigfile.entity.RouterTerminalList;
import com.linkage.module.intelspeaker.verconfigfile.entity.RouterTerminalListBak;

/**
 * 入口类
 * 1、智能音箱-公网通道ACL白名单信息存储、查询、编辑以及历史路由表的查询
 * 2、智能音箱-路由表信息存储、查询、编辑以及历史路由表的查询
 * @author jlp
 *
 */
public class IntelSpeakerConfigMgrAction extends splitPageAction implements ServletRequestAware,ServletResponseAware{
	private static final long serialVersionUID = 1L;
	private RouterTerminal routerTerminal;
	private String ajax;
	private FilterTerminal filterTerminal;
	private IntelSpeakerConfigMgrBio bio=null;
	private String version;
	private String viewStr;
	private InputStream stream;
	private RouterTerminalList routerTmList;
	private RouterTerminalListBak routerTmListBak;
	private FilterTerminalList filterTmList;
	/**
	 * 类型：1：路由表版本 2：白名单版本
	 */
	private int type ;
	public HttpServletRequest request;
	public HttpServletResponse response;
	/**
	 * 历史版本列表
	 */
	private List<Map<String,String>> verHistoryList=null;

	
	public String editNewst(){
		if(type == 1){
			routerTmList =bio.getRouterTmConfList();
			return "editRouter";
		}else{
			filterTmList =bio.getFilterTmConfList();
			return "editFilter";
		}
	}
	
	//编辑最新版本，编辑入口
	public String editNewstBak(){
		if(type == 1){
			routerTmListBak =bio.getRouterTmConfListBak();
			return "editRouterBak";
		}else{
			filterTmList =bio.getFilterTmConfList();
			return "editFilterBak";
		}
	}
	private List<Router> test;
	//保存最新编辑版本
	public String saveNewst(){
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		int res=0;
		if(type==1){
			res=bio.updateConfigFile(version,routerTmList,curUser.getUser().getAccount());
		}else{
			res=bio.updateFilterConfigFile(version,filterTmList,curUser.getUser().getAccount());
		}
		setAjax(res+"");
		return "ajax";
	}
	
	public String saveNewstBak(){
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		int res=0;
		if(type==1){
			res=bio.updateRouteConfigFile(version,routerTmListBak,curUser.getUser().getAccount());
		}else{
			res=bio.updateFilterConfigFile(version,filterTmList,curUser.getUser().getAccount());
		}
		setAjax(res+"");
		return "ajax";
	}
	
	
	//罗列历史版本
	public String listHistory(){
		totalRowCount_splitPage=bio.countHistory(type);
		setVerHistoryList(bio.listHistory(type,curPage_splitPage,num_splitPage,totalRowCount_splitPage));
		if(type==1){
			return "routerList";
		}else{
			return "filterList";
		}
	}
	
	
	//查询一个版本
	public String queryOneVersion(){
		setViewStr(bio.readOneFile(type,version));
		request.setAttribute("viewStr", viewStr);
		return "view";
	}
	
	
	//返回一个下载
	public String downloadOneVerFile(){
		String path = IntelSpeakerGlobal.getNewestFilePath(type).getPath();
		String downFileName = path.substring(0, path.length()-4)+version+".xml";
		stream=bio.returnDownLoadFile(downFileName);
		String fileName= "RouteConfig"+version+".xml";
		if(type == 2){
			fileName= "FilterConfig"+version+".xml";
		}
		try {
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "download";
	}
	
	
	public IntelSpeakerConfigMgrBio getBio() {
		return bio;
	}
	
	
	public void setBio(IntelSpeakerConfigMgrBio bio) {
		this.bio = bio;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
	}

	public List<Map<String, String>> getVerHistoryList() {
		return verHistoryList;
	}


	public void setVerHistoryList(List<Map<String, String>> verHistoryList) {
		this.verHistoryList = verHistoryList;
	}


	public RouterTerminal getRouterTerminal() {
		return routerTerminal;
	}


	public void setRouterTerminal(RouterTerminal routerTerminal) {
		this.routerTerminal = routerTerminal;
	}


	public FilterTerminal getFilterTerminal() {
		return filterTerminal;
	}


	public void setFilterTerminal(FilterTerminal filterTerminal) {
		this.filterTerminal = filterTerminal;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}


	public List<Router> getTest() {
		return test;
	}


	public void setTest(List<Router> test) {
		this.test = test;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}



	public String getViewStr() {
		return viewStr;
	}


	public void setViewStr(String viewStr) {
		this.viewStr = viewStr;
	}


	public InputStream getStream() {
		return stream;
	}


	public void setStream(InputStream stream) {
		this.stream = stream;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public HttpServletRequest getRequest() {
		return request;
	}


	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	public HttpServletResponse getResponse() {
		return response;
	}


	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}


	public RouterTerminalList getRouterTmList() {
		return routerTmList;
	}


	public void setRouterTmList(RouterTerminalList routerTmList) {
		this.routerTmList = routerTmList;
	}


	public RouterTerminalListBak getRouterTmListBak() {
		return routerTmListBak;
	}


	public void setRouterTmListBak(RouterTerminalListBak routerTmListBak) {
		this.routerTmListBak = routerTmListBak;
	}

	public FilterTerminalList getFilterTmList() {
		return filterTmList;
	}

	public void setFilterTmList(FilterTerminalList filterTmList) {
		this.filterTmList = filterTmList;
	}



}
