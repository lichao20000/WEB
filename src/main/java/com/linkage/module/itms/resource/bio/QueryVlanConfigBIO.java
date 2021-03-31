package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.module.itms.resource.dao.QueryVlanConfigDAO;

public class QueryVlanConfigBIO {

	private QueryVlanConfigDAO dao;

	@SuppressWarnings("rawtypes")
	public List<Map> queryVlanConfigList(String selectType, String username,
			String deviceSerialnumber, String cityId, String isErrorPort,
			int curPage_splitPage, int num_splitPage) {
		List<Map> list = dao.queryVlanConfigList(selectType, username,
				deviceSerialnumber, cityId, isErrorPort, curPage_splitPage,
				num_splitPage);
		
		List<Map> newList=new ArrayList<Map>();
		int index=(curPage_splitPage-1)*num_splitPage+1;
		if(list!=null && list.size()>0){
			for(Map<String,String> map:list){
				Map<String,String> m=new HashMap<String,String>();
			
				m.put("index",index+"");
				m.put("parentName",map.get("parentName"));
				m.put("cityName",map.get("cityName"));
				m.put("username",map.get("username"));
				m.put("vendorName",map.get("vendorName"));
				m.put("deviceSn",map.get("deviceSn"));
				m.put("netAccount",map.get("netAccount"));
				m.put("lan1",map.get("lan1"));
				m.put("lan2",map.get("lan2"));
				m.put("lan3",map.get("lan3"));
				m.put("lan4",map.get("lan4"));
				m.put("isErrPort",map.get("isErrPort"));
				m.put("gatherTime",map.get("gatherTime"));
				
				newList.add(m);
				index++;
				m=null;
			}
		}
		return newList;
	}

	public int countVlanConfigList(String selectType, String username,
			String deviceSerialnumber, String cityId, String isErrorPort,
			int curPage_splitPage, int num_splitPage) {
		return dao.countVlanConfigList(selectType, username,
				deviceSerialnumber, cityId, isErrorPort, curPage_splitPage,
				num_splitPage);
	}

	@SuppressWarnings("rawtypes")
	public List<Map> exportVlanConfigList(String selectType, String username,
			String deviceSerialnumber, String cityId, String isErrorPort) {
		return dao.exportVlanConfigList(selectType, username, deviceSerialnumber, cityId, isErrorPort);
	}
	
	public int getQueryCount() {
		return dao.getQueryCount();
	}

	public QueryVlanConfigDAO getDao() {
		return dao;
	}

	public void setDao(QueryVlanConfigDAO dao) {
		this.dao = dao;
	}
}
