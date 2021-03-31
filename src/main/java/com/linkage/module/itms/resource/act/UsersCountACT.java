
package com.linkage.module.itms.resource.act;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;


import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.UsersCountBIO;


public class UsersCountACT implements ServletRequestAware, ServletResponseAware,
		Serializable
{   
	private static final long serialVersionUID = 1L;

    private String ajax="";
    
    private HttpSession session;
	
	// 属地列表
	private List<Map<String, String>> cityList = null;

    
  
    
    // 业务逻辑处理
 	private UsersCountBIO bio;
 	
	private static Logger logger = LoggerFactory.getLogger(UsersCountACT.class);
     
	public String mainPage(){
		return "main";
	}
	
	public String UsersCount(){

			logger.warn("UsersCountACT=>UsersCount()");
			UserRes curUser =null;
			String userCityId =null;
			try{
				curUser= (UserRes) session.getAttribute("curUser");
				userCityId= curUser.getCityId();
			}catch(NullPointerException n){
				logger.warn("loginedException");
				ajax="[]";
				return "ajax";
			}
			ajax =bio.getCountData(userCityId).toString();
           
		return "ajax";
    }
    
	public String UsersCountPerMinutes(){

		logger.warn("UsersCountACT=>UsersCount()");
		UserRes curUser =null;
		String userCityId =null;
		try{
			curUser= (UserRes) session.getAttribute("curUser");
			userCityId= curUser.getCityId();
		}catch(NullPointerException n){
			logger.warn("loginedException");
			ajax="[]";
			return "ajax";
		}
		ajax =bio.getCountDataPerMinutes(userCityId).toString();
      return "ajax";
     }
	
	public String getCityNames()
	{
		UserRes curUser =null;
		String userCityId =null;
		try{
			curUser= (UserRes) session.getAttribute("curUser");
			userCityId= curUser.getCityId();
		}catch(NullPointerException n){
			logger.warn("loginedException");
			ajax="[]";
			return "ajax";
		}
		cityList = CityDAO.getNextCityListByCityPid(userCityId);
		JSONArray jsonArray=null;
		if(cityList != null && !cityList.isEmpty())
		{
			jsonArray=new JSONArray(cityList);
		}else{
			jsonArray=new JSONArray();
		}
		ajax=jsonArray.toString();
		return "ajax";
	}
	@Override
	public void setServletResponse(HttpServletResponse response)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		this.session=session;
	}

	

	


	
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	
	
	
	public UsersCountBIO getBio()
	{
		return bio;
	}

	
	public void setBio(UsersCountBIO bio)
	{
		this.bio = bio;
	}

	
	public String getAjax()
	{
		return ajax;
	}

	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	
	public HttpSession getSession()
	{
		return session;
	}

	
	public void setSession(HttpSession session)
	{
		this.session = session;
	}
     
}
