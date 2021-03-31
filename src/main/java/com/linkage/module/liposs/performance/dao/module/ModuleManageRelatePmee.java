package com.linkage.module.liposs.performance.dao.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.liposs.system.moduleinterface.ModuleManager;

public class ModuleManageRelatePmee implements I_ModuleManageRelateTab {
	//记录日志
	private static Logger log = LoggerFactory.getLogger(ModuleManageRelatePmee.class);
	/**
	 * 对获取的数据进行排序
	 * @param list
	 * @return
	 */
	private List<Map> sortData(List<Map> list){
		//对表达式进行排序
		String _name="";
		List<String> startList=new ArrayList<String>();
		List<String> normalList=new ArrayList<String>();
		Map<String,Map> _map=new HashMap<String,Map>();
		for(Map m:list){
			_name=""+m.get("show_column");
			_map.put(_name,m);
			if(_name.indexOf("*")==0){
				startList.add(_name);
			}else{
				normalList.add(_name);
			}
		}
		Collections.sort(startList);
		Collections.sort(normalList);
		list=new ArrayList<Map>();
		for(String s:startList){
			list.add(_map.get(s));
		}

		for(String s:normalList){
			list.add(_map.get(s));
		}
		//clear
		startList=null;
		normalList=null;
		_map=null;
		_name=null;
		return list;
	}
	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.module.I_ModuleManageRelateTab#getRelateTabAllData(com.linkage.module.liposs.performance.ModuleManager, org.springframework.jdbc.core.JdbcTemplate)
	 */
	public List<Map> getRelateTabAllData(ModuleManager mm, JdbcTemplate jt) {
		String sql="select expressionid as use_column,name as show_column from pm_expression where company="+mm.getVendor_id();
		PrepareSQL psql = new PrepareSQL(sql);
		return sortData(jt.queryForList(psql.getSQL()));
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.module.I_ModuleManageRelateTab#getConfigRelateTabData(com.linkage.module.liposs.performance.ModuleManager, org.springframework.jdbc.core.JdbcTemplate)
	 */
	public List<Map> getConfigRelateTabData(ModuleManager mm, JdbcTemplate jt) {
		String sql="select a.expressionid as use_column,a.name as show_column "
			+"from pm_expression a,tab_devicemodel_template_info b where a.expressionid=b.atrrvalue and a.company="+mm.getVendor_id()
			+" and b.serial="+mm.getSerial()+" and b.configtype="+mm.getConfigtype();
		log.debug("【北京酒店网管 模板管理】获取已配置性能表达式SQL：\n"+sql);
		PrepareSQL psql = new PrepareSQL(sql);
		return sortData(jt.queryForList(psql.getSQL()));
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.module.I_ModuleManageRelateTab#getUnConfigRelateTabData(com.linkage.module.liposs.performance.ModuleManager, org.springframework.jdbc.core.JdbcTemplate)
	 */
	public List<Map> getUnConfigRelateTabData(ModuleManager mm, JdbcTemplate jt) {
		String sql="select expressionid as use_column,name as show_column "
			+"from pm_expression a where company="+mm.getVendor_id()
			+" and expressionid not in (select distinct atrrvalue from tab_devicemodel_template_info where serial="
			+mm.getSerial()+" and configtype="+mm.getConfigtype()+")";
		log.debug("【北京酒店网管 模板管理】获取未配置性能表达式SQL：\n"+sql);
		PrepareSQL psql = new PrepareSQL(sql);
		return sortData(jt.queryForList(psql.getSQL()));
	}
}
