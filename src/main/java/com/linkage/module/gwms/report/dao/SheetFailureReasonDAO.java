/**
 * 
 */
package com.linkage.module.gwms.report.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.report.dao.interf.I_SheetFailureReasonDAO;
import com.linkage.system.utils.StringUtils;

import dao.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-6-11
 * @category dao.report
 * 
 */
@SuppressWarnings("unchecked")
public class SheetFailureReasonDAO implements I_SheetFailureReasonDAO{

	private static Logger log = LoggerFactory.getLogger(SheetFailureReasonDAO.class);
	
	private JdbcTemplateExtend jt;
	
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}
	
	/**
	 * @category 查询所有的子属地，返回
	 * 
	 * @param cityId
	 * @return
	 */
	public List getChildCity(String cityId){
		
		StringBuffer sql = new StringBuffer();
		if(Global.SDLT.equals(Global.instAreaShortName))
		{
			List list=new ArrayList();
			String[] city=cityId.split(",");
			for(int i=0;i<city.length;i++)
			{
				list.add(city[i]);
			}
			sql.append(" select city_id,city_name,parent_id from tab_city ");
			
			if(null!=cityId && !"".equals(cityId)){
				sql.append(" where city_id in(");
				sql.append(StringUtils.weave(list));
				sql.append(") or parent_id in(");
				sql.append(StringUtils.weave(list));
				sql.append(") ");
			}
			
			sql.append(" order by city_id ");
		}
		else{
		sql.append(" select city_id,city_name,parent_id from tab_city ");
		
		if(null!=cityId && !"".equals(cityId)){
			sql.append(" where city_id='");
			sql.append(cityId);
			sql.append("' or parent_id='");
			sql.append(cityId);
			sql.append("' ");
		}
		
		sql.append(" order by city_id ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}
	
	/**
	 * @category 查询所有属地
	 * 
	 * @return
	 */
	public List getAllCity(){
		
		String sql = "select city_id,city_name,parent_id from tab_city ";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}
	
	/**
	 * 查询所有的错误码
	 * 
	 * @return
	 */
	public List getFaultCode(){
		String sql = "select fault_code,fault_desc from tab_cpe_faultcode where fault_code!=0 and fault_code!=1";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 * @category 根据属地，时间查询套餐对应的工单
	 * 
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getDevNum(List cityList,long startDate,long endDate){
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select fault_code,count(*) as num ");
		}else{
			sql.append("select fault_code,count(1) as num ");
		}
		sql.append("from tab_sheet_report where receive_time>");
		sql.append(startDate/1000);
		sql.append(" and receive_time<");
		sql.append(endDate/1000);
		if(cityList.size()>0){
			sql.append(" and city_id in (");
			
			for(int i=0;i<cityList.size();i++){
				if(i!=0){
					sql.append(",");
				}
				sql.append("'");
				sql.append(cityList.get(i));
				sql.append("'");
			}
			sql.append(") ");
		}
		sql.append(" group by fault_code");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
}
