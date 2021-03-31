package com.linkage.module.gtms.stb.resource.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.JdbcTemplateExtend;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.system.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;


@SuppressWarnings("rawtypes")
public class BatchRebootDAO 
{
    private static Logger LOG = LoggerFactory.getLogger(BatchRebootDAO.class);
    private static final String ALL_CITY="00";
    private static final String NO_CHOOSE="-1";
    private static final String ALL_CITYS="allCity";
    private static final int ALL_STATUS=100;
    private static final int DATA_TYPE_ACCOUNT=1;
    private static final int DATA_TYPE_MAC=2;
    // 湖南联通记录软件升级管理操作日志
    private static final String WRITE_LOG_SQL = "insert into tab_oper_log("
    			+ "acc_oid,acc_login_ip,operationlog_type,operation_time,operation_name"
    			+ ",operation_object,operation_content,operation_device"
    			+ ",operation_result,result_id) values(?,?,?,?,?,?,?,?,?,?)";


    private JdbcTemplateExtend jt;

    public void setDao(DataSource dao) 
    {
        jt = new JdbcTemplateExtend(dao);
    }

   
    
    /**
	 *获取所有厂商 
	 */
	public List getVendor()
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select vendor_id,vendor_add from stb_tab_vendor");
		return jt.queryForList(pSQL.getSQL());
	}

	/**
     * 获取目标型号
     * @param vendorId
     * @return
     */
    public List getTargetVersion(String vendorId)
    {
        PrepareSQL sql = new PrepareSQL();
        sql.append("select a.devicetype_id,a.softwareversion,b.device_model ");
        sql.append("from stb_tab_devicetype_info a,stb_gw_device_model b ");
        sql.append("where a.device_model_id=b.device_model_id and a.vendor_id=? ");
        sql.append("order by b.device_model ");
        sql.setString(1, vendorId);
        return jt.queryForList(sql.getSQL());
    }
    
	/**
	 * 获取任务
	 * @param start
	 * @param num_splitPage
	 * @param cityId
	 * @param vendorId
	 * @param status
	 * @return
	 */
	public List<Map> getTask(int start, int numSplitPage,
			String cityId, String vendorId, String status) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.task_id,a.task_desc,a.data_type,a.city_id,a.vendor_id,");
		psql.append("a.add_time,a.update_time,a.status,b.acc_loginname ");
		psql.append("from stb_tab_batch_reboot a,tab_accounts b ");
		psql.append("where a.account_id=b.acc_oid ");
		
		if(!StringUtil.IsEmpty(cityId) && !ALL_CITY.equals(cityId) && !NO_CHOOSE.equals(cityId))
		{
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append("and a.city_id in (" + StringUtils.weave(list) + ") ");
			list = null;
		}
		
		if(!StringUtil.IsEmpty(vendorId) && !NO_CHOOSE.equals(vendorId)){
			psql.append("and a.vendor_id='"+vendorId+"' ");
		}

		//status: 任务状态 任务状态 0:未分析的任务,3:分析中，-2:待失效，-1:失效,1:激活的任务,4：执行中的任务，2:待删除
		if(!StringUtil.IsEmpty(status)){
			psql.append("and a.status="+status+" ");
		}else{
			psql.append("and a.status<>2 ");
		}
		psql.append("order by a.update_time desc,a.add_time desc ");
		return jt.querySP(psql.getSQL(),start,numSplitPage);
	}

	/**
	 * 获取任务总数
	 * @param num_splitPage
	 * @param cityId
	 * @param vendorId
	 * @param status
	 * @return
	 */
	public int countTask(int numSplitPage,String cityId, String vendorId, String status) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from stb_tab_batch_reboot a,tab_accounts b ");
		psql.append("where a.account_id=b.acc_oid ");
		
		if(!StringUtil.IsEmpty(cityId) && !ALL_CITY.equals(cityId) && !NO_CHOOSE.equals(cityId))
		{
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append("and a.city_id in (" + StringUtils.weave(list) + ") ");
			list = null;
		}
		
		if(!StringUtil.IsEmpty(vendorId) && !NO_CHOOSE.equals(vendorId)){
			psql.append("and a.vendor_id='"+vendorId+"' ");
		}

		//status: 任务状态 任务状态 0:未分析的任务,3:分析中，-2:待失效，-1:失效,1:激活的任务,4：执行中的任务，2:待删除
		if(!StringUtil.IsEmpty(status)){
			psql.append("and a.status="+status);
		}else{
			psql.append("and a.status<>2 ");
		}
		
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % numSplitPage == 0){
			maxPage = total / numSplitPage;
		}else{
			maxPage = total / numSplitPage + 1;
		}
		return maxPage;
	}
	
	/**
     * 激活、失效、删除任务
     */
    public int updateTask(String taskId,int status) 
    {
        PrepareSQL sql = new PrepareSQL();
        sql.append("update stb_tab_batch_reboot set status=?,update_time=? where task_id=? ");
        sql.setLong(1,status);
        sql.setLong(2,System.currentTimeMillis()/1000L);
        sql.setLong(3, StringUtil.getLongValue(taskId));
        return jt.update(sql.getSQL());
    }

    /**
     * 获取任务明细
     * @param taskId
     * @return
     */
    public List getTaskInfo(String taskId) 
    {
    	 PrepareSQL sql = new PrepareSQL();
         sql.append("select a.task_desc,a.task_detail,a.city_id,");
         sql.append("a.vendor_id,a.data_type,a.device_type_id,a.file_path,");
         sql.append("a.add_time,a.update_time,a.status,b.acc_loginname ");
         sql.append("from stb_tab_batch_reboot a,tab_accounts b ");
         sql.append("where a.account_id=b.acc_oid and a.task_id=? ");
         sql.setLong(1,StringUtil.getLongValue(taskId));
         return jt.queryForList(sql.getSQL());
	}
    
    /**
     * 统计设备
     * @param taskId
     * @param result_status
     * @return
     */
    public int getDataCount(String taskId,int resultStatus)
    {
    	PrepareSQL sql = new PrepareSQL();
        sql.append("select count(*) num ");
        sql.append("from stb_tab_batch_reboot_dev ");
        sql.append("where task_id=? ");
        if(resultStatus!=ALL_STATUS){
        	sql.append("and result_status="+resultStatus);
        }
        sql.setLong(1,StringUtil.getLongValue(taskId));
        return jt.queryForInt(sql.getSQL());
    }
    
    /**
     * 获取任务对应的设备型号
     * @param deviceTypeIds
     * @return
     */
	public List getTaskDeviceTypeData(String deviceTypeIds) 
    {
    	PrepareSQL sql = new PrepareSQL();
    	sql.append("select distinct a.softwareversion,b.device_model ");
    	sql.append("from stb_tab_devicetype_info a,stb_gw_device_model b ");
    	sql.append("where a.device_model_id=b.device_model_id ");
    	if(!StringUtil.IsEmpty(deviceTypeIds)){
    		sql.append("and a.devicetype_id in("+deviceTypeIds+") ");
    	}
    	sql.append("order by b.device_model ");
    	return jt.queryForList(sql.getSQL());
	}
    
	/**
	 * 获取导入账号
	 * @param taskId
	 * @param data_type
	 * @param status
	 * @return
	 */
	public List exportTaskServ(long taskId, int dataType, int status) 
	{
		PrepareSQL sql = new PrepareSQL();
		if(dataType==DATA_TYPE_ACCOUNT){
			sql.append("select serv_account ");
		}else if(dataType==DATA_TYPE_MAC){
			sql.append("select mac ");
		}
        sql.append("from stb_tab_batch_reboot_dev ");
        sql.append("where task_id=? ");
        if(status!=ALL_STATUS){
        	sql.append("and result_status="+status);
        }
        sql.setLong(1,StringUtil.getLongValue(taskId));
        return jt.queryForList(sql.getSQL());
	}
	
    /**
     * 统计
     * @param vendorId
     * @return
     */
    public List getCount(String taskId)
    {
        PrepareSQL sql = new PrepareSQL();
        sql.append("select city_id,result_status,count(*) num ");
        sql.append("from stb_tab_batch_reboot_dev ");
        sql.append("where task_id=? and result_status in(0,1,2) ");
        sql.append("group by city_id,result_status order by city_id ");
        sql.setLong(1,StringUtil.getLongValue(taskId));
        return jt.queryForList(sql.getSQL());
    }
    
    /**
     * 日志记录
     */
    public void addLog(long userId, String userIp, String operationContent,String result)
    {
        PrepareSQL psql = new PrepareSQL();
        psql.setSQL(WRITE_LOG_SQL);
        psql.setLong(1,userId);
        psql.setString(2,userIp);
        psql.setInt(3,1);
        psql.setLong(4,System.currentTimeMillis()/1000L);
        psql.setString(5,"5");
        psql.setString(6,"WEB");

        psql.setString(7,operationContent);
        psql.setString(8,"Web");
        psql.setString(9,"1".equals(result)?"成功":"失败");
        psql.setInt(10,1);
        jt.update(psql.getSQL());
    }
    
    /**
     * 设备详细
     * @param task_id
     * @param city_id
     * @param status
     * @param curPage_splitPage
     * @param num_splitPage
     * @return
     */
    public List getDevList(long taskId,String cityId,int status,
			int start, int numSplitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.mac,a.serv_account,b.city_id,");
		psql.append("b.device_serialnumber,b.vendor_id,b.device_model_id,");
		psql.append("b.devicetype_id,c.apk_version_name as version ");
		psql.append("from stb_tab_batch_reboot_dev a,stb_tab_gw_device b ");
		psql.append("left join stb_dev_supplement c on b.device_id=c.device_id ");
		psql.append("where a.device_id=b.device_id and b.device_status=1 ");
		psql.append("and a.task_id="+taskId+" ");
		
		if(!ALL_CITYS.equals(cityId)){
			psql.append("and a.city_id='" + cityId + "' ");
		}
		if(status==ALL_STATUS){
			psql.append("and a.result_status in(0,1,2) ");
		}else{
			psql.append("and a.result_status="+status+" ");
		}
		psql.append("order by b.city_id,b.vendor_id,b.device_model_id,b.devicetype_id");
		if(start==0 && numSplitPage==0){
			return jt.queryForList(psql.getSQL());
		}else{
			return jt.querySP(psql.getSQL(),start,numSplitPage);
		}
	}

	/**
	 * 设备总量
	 */
	public int countDevList(long taskId,String cityId,int status, int numSplitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) ");
		psql.append("from stb_tab_batch_reboot_dev a,stb_tab_gw_device b ");
		psql.append("where a.device_id=b.device_id and b.device_status=1 ");
		psql.append("and a.task_id="+taskId+" ");
		
		if(!ALL_CITYS.equals(cityId)){
			psql.append(" and a.city_id='" + cityId + "' ");
		}
		if(status==ALL_STATUS){
			psql.append("and a.result_status in(0,1,2) ");
		}else{
			psql.append("and a.result_status="+status+" ");
		}
		
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % numSplitPage == 0){
			maxPage = total / numSplitPage;
		}else{
			maxPage = total / numSplitPage + 1;
		}
		return maxPage;
	}
	
	/**
	 * 所有型号
	 * @return
	 */
	public List getDeviceModle()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_model_id,device_model from stb_gw_device_model ");
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 所有版本
	 * @return
	 */
	public List getDeviceType()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select devicetype_id,softwareversion,hardwareversion from stb_tab_devicetype_info ");
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 任务入库
	 * @param user_id
	 * @param taskId
	 * @param cityId
	 * @param vendorId
	 * @param deviceTypeIds
	 * @param dataType
	 * @param filePath
	 * @param taskDesc
	 * @param taskDetail
	 * @return
	 */
	public int addTask(long userId, long taskId,String cityId, String vendorId,
			String deviceTypeIds, int dataType,
			String filePath, String taskDesc, String taskDetail) 
	{
		String sql="insert into stb_tab_batch_reboot(task_id,task_desc,"
					+"task_detail,city_id,vendor_id,device_type_id,account_id,"
					+"file_path,data_type,add_time,status) "
					+"values("+taskId+",'"+taskDesc+"','"+taskDetail+"','"
					+cityId+"','"+vendorId+"','"+deviceTypeIds+"',"+userId+",'"
					+filePath+"',"+dataType+","+System.currentTimeMillis()/1000+",0)";
		
		sql=sql.replaceAll("''","null").replaceAll("'null'","null");
		LOG.info(sql);
		return jt.update(sql);
	}
	
	
	
}
