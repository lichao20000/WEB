
package com.linkage.module.gwms.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.system.User;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 异常绑定统计
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class SystemUserDAO extends SuperDAO
{

	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	/**
	 * 导出系统列表
	 * 登录用户创建的系统用户
	 *
	 * @author wangsenbo
	 * @date Jul 12, 2010
	 * @param 
	 * @return List<Map>
	 */
	public List<Map> getSystemUserExcel(User user)
	{
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		
		PrepareSQL  psql = new PrepareSQL();
		psql.append("select a.acc_loginname,b.per_city,b.per_name,d.role_desc,b.per_jobtitle ");
		psql.append("from tab_accounts a,tab_persons b,tab_acc_role c,tab_role d ");
		psql.append("where a.acc_oid=b.per_acc_oid and a.acc_oid=c.acc_oid and c.role_id=d.role_id ");
		if (!"admin".equals(user.getAccount())){
			psql.append("and a.acc_loginname!='admin' ");
			psql.append("and (a.creator = "+ user.getId() + " or a.acc_oid=" + user.getId() + ") ");
		}
		psql.append("order by b.per_city");
		
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("acc_loginname", rs.getString("acc_loginname"));
				String city_id = rs.getString("per_city");
				if(!StringUtil.IsEmpty(city_id)){
					String[] cityid=city_id.split(",");
					String city_name="";
					
					for(int i=0;i<cityid.length;i++)
					{
						city_name += StringUtil.getStringValue(cityMap.get(cityid[i]));
					}
					 
					if (false == StringUtil.IsEmpty(city_name))
					{
						map.put("city_name", city_name);
					}
					else
					{
						map.put("city_name", "");
					}
				}
				else
				{
					map.put("city_name", "");
				}
				map.put("per_name", rs.getString("per_name"));
				map.put("role_desc", rs.getString("role_desc"));
				map.put("per_jobtitle", rs.getString("per_jobtitle"));
				return map;
			}
		});
		cityMap = null;
		return list;
	}
}
