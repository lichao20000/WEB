package com.linkage.module.gtms.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.litms.common.util.JdbcTemplateExtend;

/**
 * NXDX-REQ-ITMS-20200817-LX-001(宁夏电信新增批量或单个修改光猫的ITV无线开关页面需求)-8.26修改
 */
@SuppressWarnings("rawtypes")
public class OperatSSIDDAO  extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(OperatSSIDDAO.class);
	private int queryCount;
    private JdbcTemplateExtend jt;

    public void setDao(DataSource dao) {
        jt = new JdbcTemplateExtend(dao);
    }

    /**
     * 查询设备
     */
    public List queryDevice(String queryParam,String queryField)
    {
    	logger.debug("OperatSSIDDAO queryDevice({},{})",queryParam,queryField);
    	PrepareSQL pSQL = new PrepareSQL();
    	pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,");
    	pSQL.append("a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,");
    	pSQL.append("a.interface_id,a.device_status,a.gather_id,a.devicetype_id,a.maxenvelopes,a.cr_port,");
    	pSQL.append("a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,");
    	pSQL.append("a.acs_username,a.acs_passwd,a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,");
    	pSQL.append("a.device_model_id,a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id ");
    	if("deviceSn".equals(queryField))
    	{
        	pSQL.append("from tab_gw_device a ");
        	pSQL.append("where a.device_status=1 and a.gw_type=1 ");

        	if (!StringUtil.IsEmpty(queryParam) && !"-1".equals(queryParam)) {
        		if (queryParam.length() > 5) {
        			pSQL.append(" and a.dev_sub_sn ='");
        			pSQL.append(queryParam.substring(queryParam.length() - 6, queryParam.length()));
        			pSQL.append("' ");
        		}
        		pSQL.append("and a.device_serialnumber like '%"+queryParam+"' ");
        	}
		}
    	else if("username".equals(queryField))
		{
			 pSQL.append("from tab_gw_device a,tab_hgwcustomer e ");
			 pSQL.append("where e.username=? and a.device_status=1 and a.gw_type=1 ");
			 pSQL.append("and a.device_id=e.device_id ");
			 pSQL.setString(1,queryParam);
		}
    	else if("kdname".equals(queryField))
		{// TODO wait (more table related)
			 pSQL.append("from tab_gw_device a,tab_hgwcustomer c,hgwcust_serv_info b ");
			 pSQL.append("where b.username=? and a.device_status=1 and a.gw_type=1 ");
			 pSQL.append("and a.device_id=c.device_id and c.user_id=b.user_id ");
			 pSQL.append("and b.serv_type_id=10 ");
			 pSQL.setString(1,queryParam);
		}
    	pSQL.append("order by a.complete_time ");

    	final Map<String,String> vendorMap=getVendor();
    	final Map<String,String> modelMap=getModel();
    	final Map<String,String> versionMap=getVersion();

    	return jt.query(pSQL.getSQL(), new RowMapper()
    	{
    		public Object mapRow(ResultSet rs, int arg1) throws SQLException
    		{
    			Map<String, String> map = new HashMap<String, String>();
    			map.put("device_id", rs.getString("device_id"));
                map.put("oui", rs.getString("oui"));
                map.put("device_serialnumber", rs.getString("device_serialnumber"));
                map.put("device_name", rs.getString("device_name"));
                map.put("city_id", rs.getString("city_id"));
                map.put("city_name", CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
                map.put("office_id", rs.getString("office_id"));
                map.put("complete_time", transDate(rs.getLong("complete_time")));
                map.put("zone_id", rs.getString("zone_id"));
                map.put("buy_time", transDate(rs.getLong("buy_time")));
                map.put("staff_id", rs.getString("staff_id"));
                map.put("remark", rs.getString("remark"));
                map.put("loopback_ip", rs.getString("loopback_ip"));
                map.put("interface_id", rs.getString("interface_id"));
                map.put("device_status", rs.getString("device_status"));
                map.put("gather_id", rs.getString("gather_id"));
                map.put("devicetype_id", rs.getString("devicetype_id"));
                map.put("softwareversion", versionMap.get(rs.getString("devicetype_id")));
                map.put("maxenvelopes", rs.getString("maxenvelopes"));
                map.put("cr_port", rs.getString("cr_port"));
                map.put("cr_path", rs.getString("cr_path"));
                map.put("cpe_mac", rs.getString("cpe_mac"));
                map.put("cpe_currentupdatetime", transDate(rs.getLong("cpe_currentupdatetime")));
                map.put("cpe_allocatedstatus", rs.getString("cpe_allocatedstatus"));
                map.put("cpe_username", rs.getString("cpe_username"));
                map.put("cpe_passwd", rs.getString("cpe_passwd"));
                map.put("acs_username", rs.getString("acs_username"));
                map.put("acs_passwd", rs.getString("acs_passwd"));
                map.put("device_type", rs.getString("device_type"));
                map.put("x_com_username", rs.getString("x_com_username"));
                map.put("x_com_passwd", rs.getString("x_com_passwd"));
                map.put("gw_type", rs.getString("gw_type"));
                map.put("device_model_id", rs.getString("device_model_id"));
                map.put("device_model", modelMap.get(rs.getString("device_model_id")));
                map.put("customer_id", rs.getString("customer_id"));
                map.put("device_url", rs.getString("device_url"));
                map.put("x_com_passwd_old", rs.getString("x_com_passwd_old"));
                map.put("vendor_id", rs.getString("vendor_id"));
                map.put("vendor_add", vendorMap.get(rs.getString("vendor_id")));
                map.put("gwShare_queryResultType","checkbox");
    			return map;
    		}
    	});
    }

    /**
     * 定制任务
     */
    public int addTask(String queryType, String deviceIds,String filePath, String task_name)
    {
    	logger.debug("OperatSSIDDAO addTask({},{},{},{},{})",queryType,deviceIds,filePath,task_name);
    	PrepareSQL pSQL = new PrepareSQL();

    	if("1".equals(queryType))
    	{
    		//ssid_type 1：itv无线；2：光猫无线
    		//operat_type 0：关闭；1：打开
    		//status 1：导入文件已分析；0：导入文件未分析，默认值；-1：导入文件分析失败
    		pSQL.append("insert into tab_dev_ssid_task(task_id,ssid_type,operat_type,status,device_id,add_time) ");
        	pSQL.append("values(?,1,0,0,?,?) ");

        	String[] devices=deviceIds.split(",");
        	for(String device:devices)
        	{
        		pSQL.setLong(1,System.currentTimeMillis());
        		pSQL.setString(2,device);
        		pSQL.setLong(3,System.currentTimeMillis()/1000L);

        		try{
            		jt.execute(pSQL.getSQL());
            	}catch(Exception e){
            		e.printStackTrace();
            		return 0;
            	}
        	}
        	return 1;
    	}
    	else if("3".equals(queryType))
    	{
    		pSQL.append("insert into tab_dev_ssid_task(task_id,task_name,ssid_type,operat_type,status,file_path,add_time) ");
        	pSQL.append("values(?,?,1,0,0,?,?) ");
        	pSQL.setLong(1,System.currentTimeMillis());
        	pSQL.setString(2,task_name);
        	pSQL.setString(3,filePath);
    		pSQL.setLong(4,System.currentTimeMillis()/1000L);

        	try{
        		jt.execute(pSQL.getSQL());
        		return 1;
        	}catch(Exception e){
        		e.printStackTrace();
        		return 0;
        	}
    	}

    	return 0;
    }

	public List queryList(int curPage_splitPage,int num_splitPage,String sn,
			String loid,String user_name,String task_name,long start_time,long end_time)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.task_name,b.result,b.operat_time,");
		psql.append("c.device_serialnumber,c.city_id,c.vendor_id,c.device_model_id ");
		psql.append(getSql(sn,loid,user_name,task_name,start_time,end_time));
		psql.append("order by b.operat_time desc");

		final Map<String,String> vendorMap=getVendor();
    	final Map<String,String> modelMap=getModel();
		//导出列表，查询所有数据
		if(curPage_splitPage==-1 && num_splitPage==-1)
		{
			return jt.query(psql.getSQL(), new RowMapper()
			{
	    		public Object mapRow(ResultSet rs, int arg1) throws SQLException
	    		{
	    			return getMap(rs,vendorMap,modelMap);
	    		}
	    	});
		}

		//页面分页展示
		return jt.querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
		    			return getMap(rs,vendorMap,modelMap);
					}
				});
	}

	/**
	 * 获取总数并分页
	 */
	public int countList(int num_splitPage,String sn,String loid,String user_name,
			String task_name,long start_time,long end_time)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) count_num ");
		psql.append(getSql(sn,loid,user_name,task_name,start_time,end_time));
		queryCount = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (queryCount % num_splitPage == 0){
			maxPage = queryCount / num_splitPage;
		}else{
			maxPage = queryCount / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 拼装页面展示参数
	 */
	private Map<String,String> getMap(ResultSet rs,Map<String,String> vendorMap,Map<String,String> modelMap)
	{
		Map<String, String> map = new HashMap<String, String>();
		try {
			map.put("task_name", rs.getString("task_name"));
			map.put("vendor_name", vendorMap.get(rs.getString("vendor_id")));
			map.put("city_name", CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
			map.put("sn", rs.getString("device_serialnumber"));
			map.put("device_model", modelMap.get(rs.getString("device_model_id")));

			int res=StringUtil.getIntegerValue(rs.getString("result"));
			map.put("result_desc","未下发");
			if(res==1){
				map.put("result_desc","下发成功");
			}else if(res==-1){
				map.put("result_desc","下发失败");
			}
			map.put("operat_time", transDate(StringUtil.getLongValue(rs.getString("operat_time"))));
		} catch (SQLException e) {
			e.printStackTrace();
			return map;
		}

		return map;
	}

	/**
	 * 拼装sql
	 */
	private String getSql(String sn, String loid, String user_name,String task_name,long start_time, long end_time)
	{
		StringBuffer sb=new StringBuffer();// TODO wait (more table related)
		sb.append("from tab_dev_ssid_task a,tab_dev_ssid_detail b,tab_gw_device c");

		if(!StringUtil.IsEmpty(user_name)){
			sb.append(",tab_hgwcustomer d,hgwcust_serv_info e");
		}else if(!StringUtil.IsEmpty(loid)){
			sb.append(",tab_hgwcustomer d");
		}

		sb.append(" where a.task_id=b.task_id and b.device_id=c.device_id and c.gw_type=1");

		if(!StringUtil.IsEmpty(task_name)){
			sb.append(" and a.task_name='"+task_name+"'");
		}

		if(start_time>0){
			sb.append(" and b.operat_time>="+start_time);
		}
		if(end_time>0){
			sb.append(" and b.operat_time<"+end_time);
		}

		if(!StringUtil.IsEmpty(sn)){
			sb.append(" and c.device_serialnumber='"+sn+"'");
		}

		if(!StringUtil.IsEmpty(loid)){
			sb.append(" and c.device_id=d.device_id and d.username='"+loid+"'");
		}

		if(!StringUtil.IsEmpty(user_name)){
			if(StringUtil.IsEmpty(loid)){
				sb.append(" and c.device_id=d.device_id");
			}
			sb.append(" and d.user_id=e.user_id and e.serv_type_id=10 and e.username='"+user_name+"'");
		}
		sb.append(" ");
		return sb.toString();
	}

	/**
	 * 秒数转成日期
	 */
	private String transDate(long seconds)
	{
		try{
			return new DateTimeUtil(seconds * 1000).getLongDate();
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取厂商
	 */
	private Map<String,String> getVendor()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select vendor_id,vendor_add,vendor_name from tab_vendor order by vendor_id");
		List list=jt.queryForList(psql.getSQL());

		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Map m=(Map)list.get(i);
				if(StringUtil.getStringValue(m,"vendor_add")==null){
					map.put(StringUtil.getStringValue(m,"vendor_id"),
							StringUtil.getStringValue(m,"vendor_name"));
				}else{
					map.put(StringUtil.getStringValue(m,"vendor_id"),
							StringUtil.getStringValue(m,"vendor_add"));
				}
			}
		}
		list=null;
		return map;
	}

	/**
	 * 获取型号
	 */
	private Map<String,String> getModel()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_model_id,device_model from gw_device_model order by device_model_id");
		List list=jt.queryForList(psql.getSQL());

		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Map m=(Map)list.get(i);
				map.put(StringUtil.getStringValue(m,"device_model_id"),
						StringUtil.getStringValue(m,"device_model"));
			}
		}
		list=null;
		return map;
	}

	/**
	 * 获取软件版本
	 */
	private Map<String,String> getVersion()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select devicetype_id,softwareversion from tab_devicetype_info order by devicetype_id");
		List list=jt.queryForList(psql.getSQL());

		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Map m=(Map)list.get(i);
				map.put(StringUtil.getStringValue(m,"devicetype_id"),
						StringUtil.getStringValue(m,"softwareversion"));
			}
		}
		list=null;
		return map;
	}



	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

}
