package com.linkage.module.gwms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

@SuppressWarnings({"unchecked","rawtypes"})
public class BatchRemoveBindDAO extends SuperDAO 
{

	private static Logger logger = LoggerFactory.getLogger(BatchRemoveBindDAO.class);
	
	public List<Map> queryListByLoid(List loidList){
		
		logger.debug("queryListByLoid()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.user_id userid,a.username username,a.device_id deviceid from tab_hgwcustomer a");
		sql.append(" where a.username in(");
		sql.append(StringUtils.weave(loidList)).append(")");
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		List<Map> list = jt.query(pSQL.getSQL(), new RowMapper() {
			
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map map = new HashMap<String,String>();
				map.put("user_id",String.valueOf(rs.getInt("userid")));
				map.put("username", rs.getString("username"));
				map.put("device_id", rs.getString("deviceid"));
				return map;
			}
		});
		return list;
	}
	
	public void insertTask(String task_id,String user,String upload_date,String fileName){
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("insert into tab_unbind_task (task_id,user_id,upload_date,filename) values(?,?,?,?)");
		pSQL.setString(1, task_id);
		pSQL.setInt(2, Integer.parseInt(user));
		pSQL.setLong(3, Long.parseLong(upload_date));
		pSQL.setString(4, fileName);
		jt.update(pSQL.getSQL());
	}
	
	public void insertTaskLoid(ArrayList<String> sqlList){
		if (LipossGlobals.inArea(Global.JSDX)){
			DBOperation.executeUpdate(sqlList, "proxool.xml-test");
		}else{
			DBOperation.executeUpdate(sqlList);
		}
	}
	
	public List<Map> getResultList(String task_id,int curPage_splitPage, int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		psql.setSQL("select c.acc_loginname,a.upload_date,a.filename,b.loid,b.device_id,b.result_info " +
				"from tab_unbind_task a,tab_unbind_task_loid b,tab_accounts c " +
				"where a.task_id = b.task_id and a.user_id=c.acc_oid  and b.task_id=? ");
		psql.setString(1, task_id);
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, num_splitPage, 
				new RowMapper() {
			
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String,String> map = new HashMap<String, String>();
				map.put("acc_loginname", String.valueOf(rs.getString("acc_loginname")));
				map.put("upload_date", String.valueOf(DateUtil.transTime(rs.getLong("upload_date"),"yyyy-MM-dd HH:mm:ss")));
				String fileName = rs.getString("filename");
				fileName = fileName.split("_")[1];
				map.put("filename", String.valueOf(fileName));
				map.put("loid", String.valueOf(rs.getString("loid")));
				if(String.valueOf(rs.getString("device_id")).equals("null")){
					map.put("device_id", "空");
				}else{
					map.put("device_id", String.valueOf(rs.getString("device_id")));
				}
				map.put("result_info", String.valueOf(rs.getString("result_info")));
				return map;
			}
		});
		return list;
	}
	
	public int getResultCount(String task_id,int curPage_splitPage, int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_unbind_task a,tab_unbind_task_loid b ");
		psql.append("where a.task_id=b.task_id and b.task_id=? ");
		psql.setString(1, task_id);
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	public List<Map> getResultExcel(String task_id)
	{
		PrepareSQL psql = new PrepareSQL();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		psql.append("select c.acc_loginname,a.upload_date,a.filename,b.loid,b.device_id,b.result_info ");
		psql.append("from tab_unbind_task a,tab_unbind_task_loid b,tab_accounts c ");
		psql.append("where a.task_id=b.task_id and a.user_id=c.acc_oid and b.task_id=? ");
		psql.setString(1, task_id);
		List<Map> list = jt.queryForList(psql.getSQL());
		for(int i=0;i<list.size();i++){
			String filename = String.valueOf(list.get(i).get("filename"));
			filename = filename.split("_")[1];
			list.get(i).put("filename", filename);
		}
		return list;
	}
	
	public boolean isBind(String username)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select a.device_id from tab_hgwcustomer a where a.username=?");
		pSQL.setString(1, username);
		Map map = jt.queryForMap(pSQL.getSQL());
		return !StringUtil.IsEmpty(StringUtil.getStringValue(map,"device_id"));
	}

	public Map<String,String> isExist(String loid) 
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("select user_id,username,device_id from tab_hgwcustomer where username=?");
		sql.setString(1, loid);
		return queryForMap(sql.getSQL());
	}
	
}
