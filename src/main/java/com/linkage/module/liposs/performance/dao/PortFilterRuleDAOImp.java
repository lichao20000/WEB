package com.linkage.module.liposs.performance.dao;

import java.util.List;
import java.util.Map;

import com.linkage.module.gwms.util.StringUtil;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.liposs.system.basesupport.BaseSupportDAO;

/**
 * 定义端口过滤规则操作数据库DAO的实现类
 * @author zhangsong(3704)
 * @since 2008-09-05
 * @category performance
 *
 */
public class PortFilterRuleDAOImp extends BaseSupportDAO implements  PortFilterRuleDAO{
	//SQL变量
	private String SQL = "";



	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.PortFilterRuleDAO#delFilterRule(java.lang.String, int)
	 */
	public int delFilterRule(String device_model, int type) {
		SQL = "delete from tab_model_port_filter where device_model='"+device_model+"' and type= "+type;
		PrepareSQL psql = new PrepareSQL(SQL);
		return jt.update(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.PortFilterRuleDAO#getAllDeviceModel()
	 */
	public List getAllDeviceModelByCompany(int company) {
		SQL = "select distinct device_model,device_model_id from gw_device_model where vendor_id='"+company+"'";
		PrepareSQL psql = new PrepareSQL(SQL);
		return jt.queryForList(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.PortFilterRuleDAO#getAllDeviceByCompany()
	 */
	public List getAllCompany() {
		SQL="select distinct vendor_id,vendor_name from tab_vendor order by vendor_name";
		PrepareSQL psql = new PrepareSQL(SQL);
		return jt.queryForList(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.PortFilterRuleDAO#getFilterRuleByDeviceModel(java.lang.String)
	 */
	public Map getFilterRuleByDeviceModelType(String device_model,int type) {
		SQL="select b.device_model, a.device_model as dm, b.device_model_id,a.type,a.value,a.blank " +
				"from tab_model_port_filter a,gw_device_model b " +
				"where a.device_model=b.device_model_id " +
				"and a.device_model='"+device_model+"' and a.type="+type;
		PrepareSQL psql = new PrepareSQL(SQL);
		List list =  jt.queryForList(psql.getSQL());
		if(list!=null&&!list.isEmpty()){
			Map map = (Map)list.get(0);
			String deviceModel = StringUtil.getStringValue(map, "device_model");
			String dm = StringUtil.getStringValue(map, "dm");
			map.put("device_model", deviceModel + "(" + dm + ")");
			return map;
		}else{
			return null;
		}
	}


	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.PortFilterRuleDAO#saveFilterRule(java.lang.String, int, java.lang.String)
	 */
	public int saveFilterRule(String device_model, int type, String value) {
		SQL="insert into tab_model_port_filter(device_model,type,value)values('"+device_model+"',"+type+",'"+value+"')";
		PrepareSQL psql = new PrepareSQL(SQL);
		return jt.update(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.PortFilterRuleDAO#getAllFilter()
	 */
	public List getAllFilter(int curPage_splitPage, int num_splitPage){
		SQL="select b.device_model, a.device_model as dm, b.device_model_id, " +
				"a.type,a.value,a.blank from tab_model_port_filter a, gw_device_model b " +
				"where a.device_model=b.device_model_id order by a.device_model";
		PrepareSQL psql = new PrepareSQL(SQL);
        List<Map> list = jte.querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage);
        for (Map map : list) {
            String deviceModel = StringUtil.getStringValue(map,"device_model");
            String dm = StringUtil.getStringValue(map,"dm");
            map.put("device_model", deviceModel + "(" + dm + ")");
        }
        return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.PortFilterRuleDAO#getFilterNumber()
	 */
	public int getFilterNumber(){
		SQL = "select count(*) from tab_model_port_filter a,gw_device_model b where a.device_model=b.device_model_id";
		PrepareSQL psql = new PrepareSQL(SQL);
		return jt.queryForInt(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.PortFilterRuleDAO#getFilterRule(java.lang.String, int, java.lang.String)
	 */
	public List getFilterRule(String device_model,int type,String value){
		SQL="select b.device_model, a.device_model as dm,b.device_model_id,a.type,a.value,a.blank " +
                "from tab_model_port_filter a,gw_device_model b where a.device_model=b.device_model_id " +
                "and a.device_model='"+device_model+"' and a.type="+type +" and a.value='"+value+"'";
		PrepareSQL psql = new PrepareSQL(SQL);
        List<Map> list = jt.queryForList(psql.getSQL());
        for (Map map : list) {
            String deviceModel = StringUtil.getStringValue(map,"device_model");
            String dm = StringUtil.getStringValue(map,"dm");
            map.put("device_model", deviceModel + "(" + dm + ")");
        }
        return list;
	}

	public List getFilterRuleBySql(String sql){
		return jt.queryForList(sql);
	}

}
