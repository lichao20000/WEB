package com.linkage.module.intelspeaker.verconfigfile.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
//import com.linkage.system.utils.DateTimeUtil;
import com.linkage.commons.util.DateTimeUtil;

public class IntelSpeakerConfigMgrDao 
{
	/**
	 * 保存编辑记录表
	 * @param version
	 * @param downloadUrl
	 * @param type
	 * @return
	 */
	public int insertVersionLog(String version,String downloadUrl,
			int type,long time,String userId)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("insert into tab_termver_editlog(ver_id,version,ver_type,down_path,updator,updatetime) "
				+ "values(?,?,?,?,?,?)");
		pSQL.setLong(1, time);
		pSQL.setString(2, version);
		pSQL.setInt(3, type);
		pSQL.setString(4, downloadUrl);
		pSQL.setString(5, userId);
		pSQL.setLong(6, time);
		return DataSetBean.executeUpdate(pSQL.getSQL());
		
	}
	
	/**
	 * 更新最新版本表
	 * @param version
	 * @param downloadUrl
	 * @param type
	 * @return
	 */
	public int updateNestVersionConfig(String version,String downloadUrl,int type,long time)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("update tab_terminal_version set version=?,down_path=?,updatetime=? where ver_type=? ");
		pSQL.setString(1, version);
		pSQL.setString(2, downloadUrl);
		pSQL.setLong(3, time);
		pSQL.setInt(4, type);
		return DataSetBean.executeUpdate(pSQL.getSQL());
	}
	
	/**
	 * 查询最新版本信息
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	//无用的方法
	public Map<String,String> queryNewestVerConfig(int type)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select * from tab_terminal_version where ver_type=? ");
		pSQL.setInt(1, type);
		return DataSetBean.getRecord(pSQL.getSQL());
	}
	
	//历史版本数
	public int countHistory(int type)
	{
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSQL.append("select count(*) as count ");
		}else{
			pSQL.append("select count(1) as count ");
		}
		pSQL.append("from tab_termver_editlog where ver_type=? ");
		pSQL.setInt(1, type);
		return StringUtil.getIntegerValue(DataSetBean.getRecord(pSQL.getSQL()).get("count"));
	}
	
	//历史版本数
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryHistoryVer(int type,int startIndex,int endIndex)
	{
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSQL.append("select ver_id,version,down_path,updator,updatetime ");
			pSQL.append("from tab_termver_editlog where ver_type=? ");
			pSQL.append("order by updatetime desc limt ?,? ");
			pSQL.setInt(1, type);
			pSQL.setInt(2, startIndex);
			pSQL.setInt(3, endIndex);
		}else{
			pSQL.append("select b.*,sortId from (select a.*,rownum as sortId from "
						+ "(select ver_id,version,ver_type,down_path,updator,updatetime from tab_termver_editlog "
						+ "where ver_type=? order by updatetime desc ) a where rownum<=?) b where sortId>? ");
			pSQL.setInt(1, type);
			pSQL.setInt(2, endIndex);
			pSQL.setInt(3, startIndex);
		}
		
		List<Map<String,String>> list = DataSetBean.executeQuery(pSQL.getSQL(),null);
		if (list != null) {
			int size = list.size();
			for (int i = 0; i< size; i++){
				Map<String,String> map = (Map<String,String>)list.get(i);
				long time = 0;
				time = Long.parseLong(StringUtil.getStringValue(map, "updatetime"));
				map.put("updatetime", new DateTimeUtil(time).getYYYY_MM_DD_HH_mm_ss());
			}
		}
		return list;
	}
	
}
