package com.linkage.module.itms.resource.dao;

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
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

public class DeviceOUIInfoDAO extends SuperDAO {
	
	private static Logger logger = LoggerFactory.getLogger(DeviceOUIInfoDAO.class);
	
	
	public List<Map> getDeviceOUIinfo(String oui, String vendor_name,int curPage_splitPage,int num_splitPage){
		logger.debug("DeviceOUIInfoDAO->getDeviceOUIinfo");
		StringBuffer sql = new StringBuffer();
		sql.append("select id,oui,vendor_add,vendor_name,remark,add_date ");
		
		if(!LipossGlobals.inArea(Global.GSDX)) {
			sql.append(",device_model ");
		}
		
		sql.append(" from tab_gw_device_init_oui  where  1=1 ");
		if(!StringUtil.IsEmpty(oui) && !"0".equals(oui) ){
			sql.append(" and oui='").append(oui).append("' ");
		}
		if(!StringUtil.IsEmpty(vendor_name) && !"0".equals(vendor_name)){
			sql.append(" and vendor_name='").append(vendor_name).append("' ");
		}
		
		sql.append(" order by add_date desc ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = querySP(psql.getSQL(),  (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String,String> map = new HashMap<String, String>();
						map.put("id", StringUtil.getStringValue(rs.getString("id")));
						map.put("oui", rs.getString("oui"));
						map.put("vendor_add", rs.getString("vendor_add"));
						map.put("vendor_name", rs.getString("vendor_name"));
						map.put("remark", rs.getString("remark"));
						
						if(!LipossGlobals.inArea(Global.GSDX)) {
							map.put("device_model", rs.getString("device_model"));
						}
						
						try
						{
							long add_date = StringUtil.getLongValue(StringUtil.getStringValue(rs.getString("add_date")));
							DateTimeUtil dt = new DateTimeUtil(add_date*1000);
							map.put("add_date", dt.getLongDate());
						}
						catch (NumberFormatException e)
						{
							map.put("add_date", "");
						}
						catch (Exception e)
						{
							map.put("add_date", "");
						}
						
						return map;
					}
				});
		return list;
	}
	
	public List<Map> getDeviceOUIinfo(String device_type_qry, String oui, String vendor_name,String vendor_name_stb,int curPage_splitPage,int num_splitPage){
		logger.debug("DeviceOUIInfoDAO->getDeviceOUIinfo");
		StringBuffer sql = new StringBuffer();
		sql.append("select id,oui,vendor_add,vendor_name,remark,add_date ");
		if(!LipossGlobals.inArea(Global.GSDX)) {
			sql.append(",device_model ");
		}
		sql.append(" from ");
		if(device_type_qry.equals("4")){
			sql.append("stb_");
		}
		
		sql.append("tab_gw_device_init_oui  where  1=1 ");
		if(!StringUtil.IsEmpty(oui) && !"0".equals(oui) ){
			sql.append(" and oui='").append(oui).append("' ");
		}
		if(!StringUtil.IsEmpty(vendor_name) && !"0".equals(vendor_name)){
			sql.append(" and vendor_name='").append(device_type_qry.equals("4")?vendor_name_stb:vendor_name).append("' ");
		}
		
		sql.append(" order by add_date desc ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = querySP(psql.getSQL(),  (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String,String> map = new HashMap<String, String>();
						map.put("id", StringUtil.getStringValue(rs.getString("id")));
						map.put("oui", rs.getString("oui"));
						map.put("vendor_add", rs.getString("vendor_add"));
						map.put("vendor_name", rs.getString("vendor_name"));
						map.put("remark", rs.getString("remark"));
						if(!LipossGlobals.inArea(Global.GSDX)) {
							map.put("device_model", rs.getString("device_model"));
						}
						try
						{
							long add_date = StringUtil.getLongValue(StringUtil.getStringValue(rs.getString("add_date")));
							DateTimeUtil dt = new DateTimeUtil(add_date*1000);
							map.put("add_date", dt.getLongDate());
						}
						catch (NumberFormatException e)
						{
							map.put("add_date", "");
						}
						catch (Exception e)
						{
							map.put("add_date", "");
						}
						
						return map;
					}
				});
		return list;
	}
	
	public int countDeviceOUIinfo(String oui, String vendor_name,int curPage_splitPage,int num_splitPage){
		logger.debug("DeviceOUIInfoDAO->countDeviceOUIinfo");
		StringBuffer sql = new StringBuffer();
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql.append("select count(*) from tab_gw_device_init_oui  where  1=1  ");
		}
		else {
			sql.append("select count(1) from tab_gw_device_init_oui  where  1=1  ");
		}
		
		
		if(!StringUtil.IsEmpty(oui) && !"0".equals(oui) ){
			sql.append(" and oui='").append(oui).append("' ");
		}
		if(!StringUtil.IsEmpty(vendor_name) && !"0".equals(vendor_name)){
			sql.append(" and vendor_name='").append(vendor_name).append("' ");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	
	public int countDeviceOUIinfo(String device_type_qry, String oui, String vendor_name,String vendor_name_stb,int curPage_splitPage,int num_splitPage){
		logger.debug("DeviceOUIInfoDAO->countDeviceOUIinfo");
		StringBuffer sql = new StringBuffer();
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql.append("select count(*) from ");
		}
		else {
			sql.append("select count(1) from ");
		}
		
		if(device_type_qry.equals("4")){
			sql.append("stb_");
		}
		
		sql.append("tab_gw_device_init_oui  where  1=1 ");
		
		if(!StringUtil.IsEmpty(oui) && !"0".equals(oui) ){
			sql.append(" and oui='").append(oui).append("' ");
		}
		if(!StringUtil.IsEmpty(vendor_name) && !"0".equals(vendor_name)){
			sql.append(" and vendor_name='").append(device_type_qry.equals("4")?vendor_name_stb:vendor_name).append("' ");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	public List<Map<String,String>> getVendorMap(){
		logger.debug("DeviceOUIInfoDAO->getVendorMap");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("select distinct vendor_name from tab_gw_device_init_oui where 1=1 ");
		
		PrepareSQL psql = new PrepareSQL(stringBuilder.toString());
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
 		if(null != list && list.size()>0){
			for(int i=0; i<list.size(); i++){
				list.get(i).put("vendor_name", StringUtil.getStringValue(list.get(i).get("vendor_name")));
			}
		}
 		return list;
	}
	
	public List<Map<String,String>> getVendorMapStb(){
		logger.debug("DeviceOUIInfoDAO->getVendorMap");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("select distinct vendor_name from stb_tab_gw_device_init_oui where 1=1 ");
		
		PrepareSQL psql = new PrepareSQL(stringBuilder.toString());
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
 		if(null != list && list.size()>0){
			for(int i=0; i<list.size(); i++){
				list.get(i).put("vendor_name", StringUtil.getStringValue(list.get(i).get("vendor_name")));
			}
		}
 		return list;
	}
	
	public List<Map<String,String>> getOUIMap(){
		logger.debug("DeviceOUIInfoDAO->getVendorMap");
		PrepareSQL psql = new PrepareSQL("select distinct oui from tab_gw_device_init_oui");
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
 		if(null != list && list.size()>0){
			for(int i=0; i<list.size(); i++){
				list.get(i).put("oui", StringUtil.getStringValue(list.get(i).get("oui")));
			}
		}
		return list;
	} 
	
	public int deleteOUI(String id, String deviceType){
		logger.debug("DeviceOUIInfoDAO->deleteOUI");
		String tabName = "tab_gw_device_init_oui";
		if("4".equals(deviceType)) tabName = "stb_tab_gw_device_init_oui";
		PrepareSQL psql = new PrepareSQL("delete  from  "+tabName+" where id="+id);
		return jt.update(psql.getSQL());
	}

	public int deleteSXLTOUI(String id){
		logger.debug("DeviceOUIInfoDAO->deleteSXLTOUI");
		StringBuffer sql = new StringBuffer();
		sql.append("update tab_gw_device_init_oui set is_link = 0 ");
		sql.append(" where id=").append(id);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.update(psql.getSQL());
	}

	public int addOUI(int id, String ouiId, String ouiDesc, String vendorName, String remark, String add_date, String device_model){
		logger.debug("DeviceOUIInfoDAO->addOUI");
		StringBuffer sql = new StringBuffer();
		sql.append("insert into tab_gw_device_init_oui(id, add_date");
		if(!StringUtil.IsEmpty(ouiId)){
			sql.append(" ,oui");
		}
		if(!StringUtil.IsEmpty(ouiDesc)){
			sql.append(" ,vendor_add");
		}
		if(!StringUtil.IsEmpty(vendorName)){
			sql.append(" ,vendor_name");
		}
		if(!StringUtil.IsEmpty(remark)){
			sql.append(" ,remark");
		}
		if(!StringUtil.IsEmpty(device_model)){
			sql.append(" ,device_model");
		}
		sql.append(" ) values(").append(id).append(",").append(add_date);
		if(!StringUtil.IsEmpty(ouiId)){
			sql.append(" ,'").append(ouiId).append("' ");
		}
		if(!StringUtil.IsEmpty(ouiDesc)){
			sql.append(" ,'").append(ouiDesc).append("' ");
		}
		if(!StringUtil.IsEmpty(vendorName)){
			sql.append(" ,'").append(vendorName).append("' ");
		}
		if(!StringUtil.IsEmpty(remark)){
			sql.append(" ,'").append(remark).append("' ");
		}
		if(!StringUtil.IsEmpty(device_model)){
			sql.append(" ,'").append(device_model).append("' ");
		}
		sql.append(")");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.update(psql.getSQL());
	}
	
	public int editOUI(String id, String ouiId, String ouiDesc, String vendorName, String remark , String add_date, String device_model, String deviceType){
		logger.debug("DeviceOUIInfoDAO->editOUI");
		StringBuffer sql = new StringBuffer();
		String tabName = "tab_gw_device_init_oui";
		if("4".equals(deviceType)) tabName = "stb_tab_gw_device_init_oui";
		sql.append("update " + tabName + " set add_date=").append(add_date);
		if(!StringUtil.IsEmpty(ouiId)){
			sql.append(" ,oui='").append(ouiId).append("' ");
		}
		if(!StringUtil.IsEmpty(ouiDesc)){
			sql.append(" ,vendor_add='").append(ouiDesc).append("' ");
		}
		if(!StringUtil.IsEmpty(vendorName)){
			sql.append(" ,vendor_name='").append(vendorName).append("' ");
		}
		if(null != remark){
			sql.append(" ,remark='").append(remark).append("' ");
		}
		if(null != device_model){
			sql.append(" ,device_model='").append(device_model).append("' ");
		}
		
		sql.append(" where id=").append(id);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.update(psql.getSQL());
	}
	
	public String getId(){
		logger.debug("DeviceOUIInfoDAO->getId");
		//sybase数据库用法
		//PrepareSQL psql = new PrepareSQL("select max(convert(numeric, t.id)) id from tab_gw_device_init_oui t");
		//oracle数据库用法
		PrepareSQL psql = new PrepareSQL("select max(TO_NUMBER(t.id)) id from tab_gw_device_init_oui t");
		if(DBUtil.GetDB() == 3) 
		{// mysql
			psql = new PrepareSQL("select max(t.id) id from tab_gw_device_init_oui t");
		}
		
		Map map = DataSetBean.getRecord(psql.getSQL());
		return StringUtil.getStringValue(map.get("id"));
	}
	
	public String getId(String deviceType){
		logger.debug("DeviceOUIInfoDAO->getId");
		//sybase数据库用法
		//PrepareSQL psql = new PrepareSQL("select max(convert(numeric, t.id)) id from tab_gw_device_init_oui t");
		//oracle数据库用法
		String tabName = "tab_gw_device_init_oui";
		if("4".equals(deviceType)) tabName = "stb_tab_gw_device_init_oui";
		PrepareSQL psql = new PrepareSQL("select max(TO_NUMBER(t.id)) id from " + tabName + " t");
		if(DBUtil.GetDB() == 3) 
		{// mysql
			psql = new PrepareSQL("select max(t.id) id from " + tabName + " t");
		}
		Map map = DataSetBean.getRecord(psql.getSQL());
		return StringUtil.getStringValue(map.get("id"));
	}

	public int addSXLTOUI(int id, String ouiId, String ouiDesc, String vendorName, String remark, String add_date, String deviceModel,
							 String deviceType, String deviceModelId, String vendorId) {
		logger.debug("DeviceOUIInfoDAO->addOUI");
		StringBuffer sql = new StringBuffer();
		String tabName = "tab_gw_device_init_oui";
		if("4".equals(deviceType)) tabName = "stb_tab_gw_device_init_oui";
		sql.append("insert into " + tabName + "(id, add_date");
		if(!StringUtil.IsEmpty(ouiId)){
			sql.append(" ,oui");
		}
		if(!StringUtil.IsEmpty(ouiDesc)){
			sql.append(" ,vendor_add");
		}
		if(!StringUtil.IsEmpty(vendorName)){
			sql.append(" ,vendor_name");
		}
		if(!StringUtil.IsEmpty(remark)){
			sql.append(" ,remark");
		}
		if(!StringUtil.IsEmpty(deviceModel)){
			sql.append(" ,device_model");
		}
		sql.append(" ) values(").append(id).append(",").append(add_date);
		if(!StringUtil.IsEmpty(ouiId)){
			sql.append(" ,'").append(ouiId).append("' ");
		}
		if(!StringUtil.IsEmpty(ouiDesc)){
			sql.append(" ,'").append(ouiDesc).append("' ");
		}
		if(!StringUtil.IsEmpty(vendorName)){
			sql.append(" ,'").append(vendorName).append("' ");
		}
		if(!StringUtil.IsEmpty(remark)){
			sql.append(" ,'").append(remark).append("' ");
		}
		if(!StringUtil.IsEmpty(deviceModel)){
			sql.append(" ,'").append(deviceModel).append("' ");
		}
		sql.append(")");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.update(psql.getSQL());
	}

	/**
	 * 查询指定oui是否已激活
	 * @param vendorId
	 * @param deviceModelId
	 * @param ouiId
	 * @return
	 */
	public int ouiIsExist(String vendor_id, String device_model_id, String ouiId, String deviceType) {
		logger.debug("DeviceOUIInfoDAO->ouiIsLink");
		
		if(StringUtil.IsEmpty(vendor_id) || StringUtil.IsEmpty(device_model_id)) {
			return -1;
		}
		String tabName = "tab_gw_device_init_oui";
		if("4".equals(deviceType)) tabName = "stb_tab_gw_device_init_oui";
		StringBuffer sql = new StringBuffer();
		sql.append("select oui from "+tabName);
		sql.append(" where vendor_name='").append(vendor_id).append("'");
		sql.append(" and device_model='").append(device_model_id).append("'");
		sql.append(" and oui='").append(ouiId).append("'");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		if(list == null || list.size() == 0) {
			return -1;
		}else{
			return 1;
		}
	}
}
