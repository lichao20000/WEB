package com.linkage.module.gtms.config.obj;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;

public class ConfigOBJ 
{
	private static Logger logger = LoggerFactory.getLogger(ConfigOBJ.class);
	
	private long task_id;
	private int service_id;
	private String query_sql;
	private String param;
	private int gw_type;
	private long add_time;
	private int task_status;
	
	private ConfigOBJ obj;
	
	public ConfigOBJ joinObj(Map<String,String> map)
	{
		obj=new ConfigOBJ();
		obj.setTask_id(StringUtil.getLongValue(map,"task_id"));
		obj.setService_id(StringUtil.getIntValue(map,"service_id"));
		obj.setQuery_sql(StringUtil.getStringValue(map, "query_sql"));
		obj.setParam(StringUtil.getStringValue(map, "param"));
		obj.setGw_type(StringUtil.getIntValue(map, "gw_type"));
		obj.setAdd_time(StringUtil.getLongValue(map,"add_time"));
		obj.setTask_status(StringUtil.getIntValue(map, "task_status"));
		
		logger.warn("ConfigOBJ task_id:{}",obj.getTask_id());
		return obj;
	}
	
	
	
	public long getTask_id() {
		return task_id;
	}
	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}
	public int getService_id() {
		return service_id;
	}
	public void setService_id(int service_id) {
		this.service_id = service_id;
	}
	public String getQuery_sql() {
		return query_sql;
	}
	public void setQuery_sql(String query_sql) {
		this.query_sql = query_sql;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public int getGw_type() {
		return gw_type;
	}
	public void setGw_type(int gw_type) {
		this.gw_type = gw_type;
	}
	public long getAdd_time() {
		return add_time;
	}
	public void setAdd_time(long add_time) {
		this.add_time = add_time;
	}
	public int getTask_status() {
		return task_status;
	}
	public void setTask_status(int task_status) {
		this.task_status = task_status;
	}
	
}
