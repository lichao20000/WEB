package com.linkage.module.gwms.blocTest.bio;

import java.util.List;
import java.util.Map;

import com.linkage.module.gwms.blocTest.dao.EgwExpertDAO;

/**
 * 
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-10-24 上午09:24:57
 * @category com.linkage.module.gwms.blocTest.bio
 * @copyright 南京联创科技 网管科技部
 *
 */
public class EgwExpertBIO
{
	
	
	private EgwExpertDAO dao;
	public  Map queryOne(int id){
		return dao.queryOne(id);
		
	}
	
	public List queryAll(int id ,	String name){
		return dao.queryAll( id , name);
	}
	
	
	
	
	public  int update(int id,String ex_regular,String ex_name,String ex_bias,String ex_succ_desc,String ex_fault_desc,
			String	ex_suggest,String ex_desc){
				return dao.update(id, ex_regular, ex_name, ex_bias, ex_succ_desc, ex_fault_desc, ex_suggest, ex_desc);
		
	}
	public EgwExpertDAO getDao()
	{
		return dao;
	}
	
	public void setDao(EgwExpertDAO dao)
	{
		this.dao = dao;
	}
}
