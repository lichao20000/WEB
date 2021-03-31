package com.linkage.module.gwms.report.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.report.dao.V6OpenStatDAO;


public class V6OpenStatBIO {
	Logger logger = LoggerFactory.getLogger(V6OpenStatBIO.class);
	// 持久层
	private V6OpenStatDAO dao;

	public List<Map> stat(int curPage_splitPage, int num_splitPage){
		 return dao.stat(curPage_splitPage, num_splitPage);
	}
	
	public int count(int curPage_splitPage, int num_splitPage){
         return dao.count(curPage_splitPage, num_splitPage);		
	}
	public List<Map> toExcel(){
		 return dao.toExcel();
	}
	
	public V6OpenStatDAO getDao()
	{
		return dao;
	}
	public void setDao(V6OpenStatDAO dao)
	{
		this.dao = dao;
	}
	 
	
}
