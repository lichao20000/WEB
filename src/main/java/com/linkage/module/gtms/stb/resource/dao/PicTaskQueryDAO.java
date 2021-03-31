package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.litms.common.util.JdbcTemplateExtend;
import com.linkage.system.utils.StringUtils;

/**
 * @author wuchao(工号) Tel:1 added zhangsb3
 * @version 1.0
 * @since 2012-3-30 上午10:05:07
 * @category com.linkage.module.lims.stb.resource.dao
 * @copyright 南京联创科技 网管科技部
 */
public class PicTaskQueryDAO
{

	private static Logger logger = LoggerFactory.getLogger(PicTaskQueryDAO.class);
	private static Map<String, String> groupMap = new HashMap<String, String>();

	private JdbcTemplateExtend jt;

	public List<Map> query(String priority, String status, String cityId,
			String groupId, String tradeId,String transactionId, int curPage_splitPage,
			int num_splitPage) {
		long  currentTime =new DateTimeUtil().getLongTime();
		//先更新已经过了失效时间的记录
		PrepareSQL psql1 = new PrepareSQL(" update gw_pic_task set status=1 where end_time<"+currentTime);
		jt.update(psql1.getSQL());
		PicTaskQueryDAO.groupMap = getAlllGroupNameForMap();
		String sql = " select task_id,transaction_id,add_time,priority,status," +
				"end_time,city_id,group_id,trade_id from gw_pic_task where 1=1";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		if (null != priority && !"".equals(priority) && !"-1".equals(priority)) {
			psql.append(" and priority=" + priority);
		}
		//选择条件
		if (null != status && !"".equals(status) && !"-1".equals(status)) {
			if("0".equals(status)){
				psql.append(" and end_time > "+currentTime);
			}
			psql.append(" and status=" + status);
		}
		if (null != tradeId && !"".equals(tradeId) && !"-1".equals(tradeId)) {
			psql.append(" and trade_id='" + tradeId + "'");
		}
		if(!StringUtil.IsEmpty(transactionId)){
			psql.append(" and transaction_id like '%"+transactionId+"%'");
		}
		if (null != groupId && !"".equals(groupId) && !"-1".equals(groupId)) {
			psql.append(" and group_id='" + groupId + "'");
		}
		if (null != cityId && !"".equals(cityId) && !"-1".equals(cityId)) {
			psql.append(" and  city_id in(?)");
			List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.setStringExt(1, StringUtils.weave(cityIdList), false);
		}

		List<Map> resultList = new ArrayList<Map>();
		final Map<String, String> priorityMap = new HashMap<String, String>();
		priorityMap.put("1", "导入用户账号");
		priorityMap.put("2", "业务区属地+分组+行业ID");
		priorityMap.put("3", "业务区属地+分组");
		priorityMap.put("4", "业务区属地");
		priorityMap.put("5", "分组");
		resultList = jt.querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("task_id", rs.getString("task_id"));
				map.put("transaction_id", rs.getString("transaction_id"));
				map.put("add_time", formatTime(StringUtil.getLongValue(rs
						.getString("add_time"))));
				map.put("priority", priorityMap.get(rs.getString("priority")));
				String status = rs.getString("status");
				if ("1".equals(status)) {
					map.put("status", "失效");
				} else {
					map.put("status", "生效");
				}
				map.put("status_bak", rs.getString("status"));
				map.put("end_time", formatTime(StringUtil.getLongValue(rs
						.getString("end_time"))));
			    long  dtu = new DateTimeUtil().getLongTime();
			    long endTime = StringUtil.getLongValue(rs.getString("end_time"));
				if(dtu > endTime){
					map.put("effect", "1");
				}
				map.put("city_id", CityDAO.getCityName(rs.getString("city_id")));
				map.put("group_id", PicTaskQueryDAO.groupMap.get(rs
						.getString("group_id")));
				map.put("trade_id", StringUtil.getStringValue(rs
						.getString("trade_id")));
				return map;
			}
		});
		return resultList;
	}

	public int getCount(String priority, String status, String cityId,
			String groupId, String tradeId,String transactionId, int curPage_splitPage,
			int num_splitPage) {
		String sql = " select  count(*) from gw_pic_task where 1=1";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		if (null != priority && !"".equals(priority) && !"-1".equals(priority)) {
			psql.append(" and priority=" + priority);
		}
		if (null != status && !"".equals(status) && !"-1".equals(status)) {
			psql.append(" and status=" + status);
		}
		if (null != tradeId && !"".equals(tradeId) && !"-1".equals(tradeId)) {
			psql.append(" and trade_id='" + tradeId + "'");
		}
		if(!StringUtil.IsEmpty(transactionId)){
			psql.append(" and transaction_id like '%"+transactionId+"%'");
		}
		if (null != groupId && !"".equals(groupId) && !"-1".equals(groupId)) {
			psql.append(" and group_id='" + groupId + "'");
		}

		if (null != cityId && !"".equals(cityId) && !"-1".equals(cityId)) {
			psql.append(" and city_id in(?)");
			List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.setStringExt(1, StringUtils.weave(cityIdList), false);
		}

		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public String formatTime(long time) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(time * 1000);
	}

	/**
	 * 机顶盒策略生效，失效操作 gw_pic_task_oper 中oper 有三种状态1：定制 2：生效 3：失效
	 *
	 * @param taskId
	 * @param transactionId
	 * @param status
	 *            策略的状态 0：生效 1：失效
	 * @param reason
	 * @return
	 */
	public int updateStatus(String taskId, String transactionId, String status,
			String reason, long accId) {
		int isSuc = 0;
		int res = 0;
		String sql = "update  gw_pic_task set status=? where task_id=?";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setInt(1, Integer.valueOf(status));
		psql.setString(2, taskId);
		isSuc = jt.update(psql.getSQL());

		long nowDate = new DateTimeUtil().getLongTime();

		if (isSuc > 0) {
			sql = " insert into  gw_pic_task_oper(operdate,oper,task_id,acc_oid) values(?,?,?,?)";
			PrepareSQL ps = new PrepareSQL(sql.toString());
			ps.setLong(1, nowDate);
			ps.setInt(2, Integer.parseInt(status) + 2);
			ps.setString(3, taskId);
			ps.setLong(4, accId);
			res = jt.update(ps.getSQL());
		}
		return res;
	}

	/**
	 * 详细
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> detail(String taskId) {
		String sql = "select status, city_id, add_time, end_time, priority, group_id from gw_pic_task where task_id=?";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, taskId);
		Map<String, String> detailMap = new HashMap<String, String>();
		detailMap = (Map<String, String>) jt.queryForMap(psql.getSQL());
		String status = StringUtil.getStringValue(detailMap.get("status"));

		long  dtu =new  DateTimeUtil().getLongTime();
	    long endTime = StringUtil.getLongValue(detailMap.get("end_time"));
		if(dtu > endTime){
			detailMap.put("effect", "1");
		}
		if ("1".equals(status)) {
			detailMap.put("status", "失效");
		} else {
			detailMap.put("status", "生效");
		}
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		detailMap.put("city_id", cityMap.get(detailMap.get("city_id")));
		detailMap.put("add_time", formatTime(StringUtil.getLongValue(detailMap
				.get("add_time"))));
		detailMap.put("end_time", formatTime(StringUtil.getLongValue(detailMap
				.get("end_time"))));
		Map<String, String> priorityMap = new HashMap<String, String>();
		priorityMap.put("1", "导入用户账号");
		priorityMap.put("2", "业务区属地+分组+行业ID");
		priorityMap.put("3", "业务区属地+分组");
		priorityMap.put("4", "业务区属地");
		priorityMap.put("5", "分组");
		detailMap.put("priority", priorityMap.get(StringUtil
				.getStringValue(detailMap.get("priority"))));
		detailMap.put("group_id", PicTaskQueryDAO.groupMap.get(detailMap
				.get("group_id")));
		return detailMap;
	}

	/**
	 * 查看结果 操作
	 *
	 * @param taskId
	 * @return
	 */
	public Map<String, String> result(String taskId) {
//		String qdSql = "select count(ucid) as num,hdorsd_qd_pic,qd_pic_result" +
//					" from gw_pic_record where hdorsd_qd_pic!=0 and task_id=? " +
//					"group by hdorsd_qd_pic,qd_pic_result " +
//					"order by hdorsd_qd_pic,qd_pic_result";
//		String kjSql = "select count(ucid) as num,hdorsd_kj_pic,kj_pic_result" +
//					" from gw_pic_record where hdorsd_kj_pic!=0 and task_id=?" +
//					" group by hdorsd_kj_pic,kj_pic_result" +
//					" order by hdorsd_kj_pic,kj_pic_result";
//		String rzSql = "select count(ucid) as num,hdorsd_rz_pic,rz_pic_result " +
//					"from gw_pic_record where hdorsd_rz_pic!=0 and task_id=? " +
//					"group by hdorsd_rz_pic,rz_pic_result " +
//					"order by hdorsd_rz_pic,rz_pic_result";
		//modified by zhangsb 2013年6月9日
		/**
		String zxQdSql = "select count(a.ucid) as num,a.zte_sd_qd_pic,a.zte_sd_qd_pic_result " +
	    " from gw_pic_record a,stb_tab_gw_device b where  a.device_id=b.device_id and a.zte_sd_qd_pic!=0 and a.task_id=? " +
	    " group by a.zte_sd_qd_pic,a.zte_sd_qd_pic_result " +
	    " order by a.zte_sd_qd_pic,a.zte_sd_qd_pic_result ";
	    */
		String qdSql = "select count(a.ucid) as num,a.hdorsd_qd_pic,a.qd_pic_result" +
		" from gw_pic_record a,stb_tab_gw_device b where a.device_id=b.device_id and a.hdorsd_qd_pic!=0 and a.task_id=? " +
		" group by a.hdorsd_qd_pic,a.qd_pic_result " +
		" order by a.hdorsd_qd_pic,a.qd_pic_result";
		String kjSql = "select count(a.ucid) as num,a.hdorsd_kj_pic,a.kj_pic_result" +
		" from gw_pic_record a,stb_tab_gw_device b where a.device_id=b.device_id and a.hdorsd_kj_pic!=0 and a.task_id=?" +
		" group by a.hdorsd_kj_pic,a.kj_pic_result" +
		" order by a.hdorsd_kj_pic,a.kj_pic_result";
		String rzSql = "select count(a.ucid) as num,a.hdorsd_rz_pic,a.rz_pic_result " +
		" from gw_pic_record a,stb_tab_gw_device b where a.device_id=b.device_id and a.hdorsd_rz_pic!=0 and a.task_id=? " +
		" group by a.hdorsd_rz_pic,a.rz_pic_result " +
		" order by a.hdorsd_rz_pic,a.rz_pic_result";

		/** 集团中心节点
		PrepareSQL zxQdPsql = new PrepareSQL(zxQdSql.toString());
		zxQdPsql.setString(1, taskId);
		List<Map> zxQdList = jt.queryForList(zxQdPsql.getSQL());
		*/
		PrepareSQL qdPsql = new PrepareSQL(qdSql.toString());
		qdPsql.setString(1, taskId);
		List<Map> qdList = jt.queryForList(qdPsql.getSQL());

		PrepareSQL kjPsql = new PrepareSQL(kjSql.toString());
		kjPsql.setString(1, taskId);
		List<Map> kjList = jt.queryForList(kjPsql.getSQL());

		PrepareSQL rzPsql = new PrepareSQL(rzSql.toString());
		rzPsql.setString(1, taskId);
		List<Map> rzList = jt.queryForList(rzPsql.getSQL());

		Map<String, String> resultMap = new HashMap<String, String>();
		//中心标清
		for(int i = 0 ;i<3;i++){
			resultMap.put("zx"+i, "0");
		}

		for (int i = 1; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				resultMap.put(i + "qd" + j, "0");
			}
		}
		for (int i = 1; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				resultMap.put(i + "kj" + j, "0");
			}
		}
		for (int i = 1; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				resultMap.put(i + "rz" + j, "0");
			}
		}
		//计算中心标清
		//getResultZX(resultMap,zxQdList,"zte_sd_qd_pic","zte_sd_qd_pic_result");
		//计算启动时的数量
		getResult(resultMap,qdList,"hdorsd_qd_pic","qd_pic_result");
		//计算开机时的数量
		getResult(resultMap,kjList,"hdorsd_kj_pic","kj_pic_result");
		//计算认证时的数量
		getResult(resultMap,rzList,"hdorsd_rz_pic","rz_pic_result");
		resultMap.put("taskId", taskId);
		return resultMap;
	}
	/**
	 * 中心标清
	 * @param rsMap
	 * @param lt
	 * @param pic
	 * @param result
	 */
	public void getResultZX(Map<String,String> rsMap ,List<Map> lt,String pic ,String result){
    	int temp10 = 0 ;
    	int temp1 = 0 ;
    	int temp2 = 0 ;
    	int temp3 = 0 ;
    	int temp4 = 0 ;

        for (int i = 0; i < lt.size(); i++) {
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==1
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==0){
        		temp10 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==1
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==1){
        		temp1 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==1
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==2){
        		temp2 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==1
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==3){
        		temp3 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==1
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==4){
        		temp4 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}

        	rsMap.put(pic.substring(7,9)+"0", temp10+temp3+"");
        	rsMap.put(pic.substring(7,9)+"1", temp1+"");
        	rsMap.put(pic.substring(7,9)+"2", temp2+temp4+"");
		}
    }
	/**
	 * 因为要根据后台的数据进行计算，最大的可能是10行3列，要对其中的8行进行计算
	 * 高清是 1、3行的num值相加作为更新成功，2、4行进行相加作为更新失败显示
	 * 标清时相同.
	 * @param rsMap
	 * @param lt
	 * @param pic
	 * @param result
	 */
    public void getResult(Map<String,String> rsMap ,List<Map> lt,String pic ,String result){
    	int temp10 = 0 ;
    	int temp1 = 0 ;
    	int temp2 = 0 ;
    	int temp3 = 0 ;
    	int temp4 = 0 ;

    	int temp20 = 0;
    	int temp5 = 0 ;
    	int temp6 = 0 ;
    	int temp7 = 0 ;
    	int temp8 = 0 ;
        for (int i = 0; i < lt.size(); i++) {
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==1
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==0){
        		temp10 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==1
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==1){
        		temp1 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==1
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==2){
        		temp2 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==1
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==3){
        		temp3 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==1
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==4){
        		temp4 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}

        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==2
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==0){
        		temp20 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==2
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==1){
        		temp5 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==2
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==2){
        		temp6 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==2
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==3){
        		temp7 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}
        	if(StringUtil.getIntegerValue(lt.get(i).get(pic))==2
        			&&StringUtil.getIntegerValue(lt.get(i).get(result))==4){
        		temp8 = StringUtil.getIntegerValue(lt.get(i).get("num"));
        	}
        	rsMap.put("1"+pic.substring(7,9)+"0", temp10+temp3+"");
        	rsMap.put("1"+pic.substring(7,9)+"1", temp1+"");
        	rsMap.put("1"+pic.substring(7,9)+"2", temp2+temp4+"");

        	rsMap.put("2"+pic.substring(7,9)+"0", temp20+temp7+"");
        	rsMap.put("2"+pic.substring(7,9)+"1", temp5+"");
        	rsMap.put("2"+pic.substring(7,9)+"2", temp6+temp8+"");
		}
    }
	public List<Map<String, String>> getAlllGroupName() {
		String sql = "select user_grp,grp_name from grp_info";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	public Map<String, String> getAlllGroupNameForMap() {
		List<Map<String, String>> groupList = getAlllGroupName();
		for (int i = 0; i < groupList.size(); i++) {
			PicTaskQueryDAO.groupMap.put(groupList.get(i).get("user_grp"),
					groupList.get(i).get("grp_name"));
		}
		return PicTaskQueryDAO.groupMap;
	}

	/**
	 *查询一天中生效失效的次数。
	 *
	 * @param taskId
	 *            任务ID
	 * @param starttime
	 * @param endtime
	 * @param flag
	 *            是否为单一任务
	 * @return 操作的次数
	 */
	public int getCountOper(String taskId, long starttime, long endtime,
			String flag) {
		StringBuffer sb = new StringBuffer();
		sb
				.append(" select count(*) from gw_pic_task_oper where oper in (2,3)  ");
		if (null != taskId && !"".equals(taskId) && "1".equals(flag)) {
			sb.append(" and task_id='").append(taskId).append("'");
		}
		sb.append(" and operdate>=? and operdate<=? ");

		PrepareSQL psql = new PrepareSQL(sb.toString());
		psql.setLong(1, starttime);
		psql.setLong(2, endtime);
		return jt.queryForInt(psql.getSQL());
	}

	/**
	 * 根据taskId删除策略
	 *
	 * @param taskId
	 * @return
	 */

	public int deleteStrategyAndRecord(String taskId) {
		List<String> tempList = new ArrayList<String>();
		String sql = "delete from  gw_pic_task  where task_id=?";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, taskId);
		tempList.add(psql.getSQL());

		sql = "delete from  gw_pic_record  where task_id=?";
		psql = new PrepareSQL(sql.toString());
		psql.setString(1, taskId);
		tempList.add(psql.getSQL());

		String[] sqlArray = tempList.toArray(new String[0]);
		int[] ier = jt.batchUpdate(sqlArray);
		if(ier != null && ier.length > 0) {
			logger.warn("任务失效：  成功");
			return ier.length;
		}else{
			return 0;
		}
	}
		/**
		 * 根据taskId删除机顶盒运营画面导入帐号表
		 *
		 * @param taskId
		 * @return
		 */
		public int deletePicRecord(String taskId) {
			String sql = "delete from  gw_pic_record  where task_id=?";
			PrepareSQL psql = new PrepareSQL(sql.toString());
			psql.setString(1, taskId);
			return jt.update(psql.getSQL());
		}
	/**
	 * 根据taskId删除机顶盒运营画面导入帐号表
	 *
	 * @param taskId
	 * @return
	 */
	public int deleteBatchStrategy(String taskId) {
		String sql = "delete from  gw_pic_task_batch  where task_id=?";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, taskId);
		return jt.update(psql.getSQL());
	}

	/**
	 *查询优先级，行业，分组，业务属地
	 *
	 * @param taskId
	 * @return
	 */
	public Map<String,Object> getByTaskId(String taskId) {
		String sql = "select priority , city_id,trade_id ,group_id  from  gw_pic_task  where task_id=?";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, taskId);
		return jt.queryForMap(psql.getSQL());
	}
	/**
	 *查询策略是否存在
	 *
	 * @param taskId
	 * @return
	 */
	public int getStrategyByTaskId(String taskId) {
		String sql = "select count(*)  from  gw_pic_task  where task_id=?";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, taskId);
		return jt.queryForInt(psql.getSQL());
	}

	/**
	 *查询策略是否存在
	 *
	 * @param taskId
	 * @return
	 */
	public int getBatchStrategyByTaskId(String taskId) {
		String sql = "select count(*)  from  gw_pic_task_batch  where task_id=?";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, taskId);
		return jt.queryForInt(psql.getSQL());
	}
	public List<Map> getSoftUpDetail(String taskId,final String updateStatus,
			int curPage_splitPage,int num_splitPage,String sortName,String hdorsd){

			List<Map> detailResult  = new ArrayList<Map>();
		    StringBuffer sql = new StringBuffer();
			sql.append("select v.vendor_add,d.device_serialnumber,d.serv_account, ");
			sql.append(" r.starttime,r.endtime,dm.device_model ");
			sql.append(getSoftUpString(taskId, updateStatus, curPage_splitPage, num_splitPage, sortName, hdorsd));
			PrepareSQL psql = new PrepareSQL(sql.toString());
			psql.setString(1, taskId);
			detailResult = jt.querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("vendor_add", StringUtil.getStringValue(rs.getString("vendor_add")));
				map.put("device_serialnumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));
				map.put("starttime", formatTime(StringUtil.getLongValue(rs
						.getString("starttime"))));
				map.put("endtime", formatTime(StringUtil.getLongValue(rs
						.getString("endtime"))));
				map.put("serv_account",StringUtil.getStringValue(rs.getString("serv_account")));
				map.put("device_model", StringUtil.getStringValue(rs.getString("device_model")));
				map.put("updateStatus", updateStatus);
				return map;
			}
		});
		return detailResult;
	}
	public int getSoftUpCount(String taskId, String updateStatus,
			int curPageSplitPage, int numSplitPage, String sortName,
			String hdorsd) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) ");
		sql.append(getSoftUpString(taskId, updateStatus, curPageSplitPage, numSplitPage, sortName, hdorsd));
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, taskId);
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % numSplitPage == 0) {
			maxPage = total / numSplitPage;
		} else {
			maxPage = total / numSplitPage + 1;
		}
		return maxPage;
	}
	public String getSoftUpString(String taskId, String updateStatus,
			int curPageSplitPage, int numSplitPage, String sortName,
			String hdorsd){
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append(" from stb_tab_gw_device d,gw_pic_record r, " );
		sql.append(" gw_vendor v,gw_device_model dm ");
		sql.append(" where d.device_id=r.device_id ");
		sql.append(" and d.vendor_id=v.vendor_id ");
		sql.append(" and d.device_model_id=dm.device_model_id ");
		if("1".equals(hdorsd)){
			if("kj".equals(sortName)){
				sql.append(" and r.hdorsd_kj_pic = 1");
				if("0".equals(updateStatus)){
					sql.append(" and r.kj_pic_result in (0,3)");
				}
				if("1".equals(updateStatus)){
					sql.append(" and r.kj_pic_result = 1");
				}
				if("2".equals(updateStatus)){
					sql.append(" and r.kj_pic_result in (2,4)");
				}
			}
			if("qd".equals(sortName)){
				sql.append(" and r.hdorsd_qd_pic = 1");
				if("0".equals(updateStatus)){
					sql.append(" and r.qd_pic_result in (0,3)");
				}
				if("1".equals(updateStatus)){
					sql.append(" and r.qd_pic_result = 1");
				}
				if("2".equals(updateStatus)){
					sql.append(" and r.qd_pic_result in (2,4)");
				}
			}
			if("rz".equals(sortName)){
				sql.append(" and r.hdorsd_rz_pic = 1");
				if("0".equals(updateStatus)){
					sql.append(" and r.rz_pic_result in (0,3)");
				}
				if("1".equals(updateStatus)){
					sql.append(" and r.rz_pic_result = 1");
				}
				if("2".equals(updateStatus)){
					sql.append(" and r.rz_pic_result in (2,4)");
				}
			}
		}else if("2".equals(hdorsd)){
			if("kj".equals(sortName)){
				sql.append(" and r.hdorsd_kj_pic = 2");
				if("0".equals(updateStatus)){
					sql.append(" and r.kj_pic_result in (0,3)");
				}
				if("1".equals(updateStatus)){
					sql.append(" and r.kj_pic_result = 1");
				}
				if("2".equals(updateStatus)){
					sql.append(" and r.kj_pic_result in (2,4)");
				}
			}
			if("qd".equals(sortName)){
				sql.append(" and r.hdorsd_qd_pic = 2");
				if("0".equals(updateStatus)){
					sql.append(" and r.qd_pic_result in (0,3)");
				}
				if("1".equals(updateStatus)){
					sql.append(" and r.qd_pic_result = 1");
				}
				if("2".equals(updateStatus)){
					sql.append(" and r.qd_pic_result in (2,4)");
				}
			}
			if("rz".equals(sortName)){
				sql.append(" and r.hdorsd_rz_pic = 2");
				if("0".equals(updateStatus)){
					sql.append(" and r.rz_pic_result in (0,3)");
				}
				if("1".equals(updateStatus)){
					sql.append(" and r.rz_pic_result = 1");
				}
				if("2".equals(updateStatus)){
					sql.append(" and r.rz_pic_result in (2,4)");
				}
			}
		}
		//modified by zhangsb 2013年6月9日
		/**
		else{
			sql.append(" and r.zte_sd_qd_pic = != 0 ");
			if("0".equals(updateStatus)){
				sql.append(" and r.zte_sd_qd_pic_result in (0,3)");
			}
			if("1".equals(updateStatus)){
				sql.append(" and r.zte_sd_qd_pic_result = 1");
			}
			if("2".equals(updateStatus)){
				sql.append(" and r.zte_sd_qd_pic_result (2,4)");
			}
		}
		*/
		sql.append(" and r.task_id=?");
		return sql.toString();
	}

	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplateExtend(dao);
	}
}
