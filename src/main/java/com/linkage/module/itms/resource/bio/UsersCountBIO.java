package com.linkage.module.itms.resource.bio;

import org.json.JSONArray;

import com.linkage.module.itms.resource.dao.UsersCountDAO;

public class UsersCountBIO {

	private UsersCountDAO dao;


	public JSONArray getCountData(String city_id) {
		return dao.queryUsersCount(city_id);
	}
    
	public JSONArray getCountDataPerMinutes(String city_id) {
		return dao.queryUsersCountPerMinutes(city_id);
	}
	
	public UsersCountDAO getDao()
	{
		return dao;
	}

	
	public void setDao(UsersCountDAO dao)
	{
		this.dao = dao;
	}	
}
