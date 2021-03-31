/**
 *
 */
package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.dao.DataSourceContextHolder;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.litms.common.util.JdbcTemplateExtend;

/**
 *
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年6月11日
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
@SuppressWarnings("rawtypes")
public class StbSourceDAO extends SuperDAO {
	// 日志记录
	private static Logger logger = LoggerFactory .getLogger(StbSourceDAO.class);
	/**
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}


	public List qryStbSource(String loid, String devSn, String mac,String netUsername,String servAccount,String stbDevSn,String stbMac,
			int currPage,int numPage){

		String sql = "select b.username loid ,c.device_id devId, c.device_serialnumber devSn,c.cpe_mac mac,f.serv_account ,"+
		"e.device_serialnumber stbDevSn,e.device_id stbDevId,e.cpe_mac stbmac,a.update_time ";
		PrepareSQL pSQL = new PrepareSQL();

		pSQL.append(sql);// TODO wait (more table related)
		pSQL.append(" from tab_gw_device_stbmac a, tab_hgwcustomer b,tab_gw_device c,");
		if(!StringUtil.IsEmpty(netUsername)){
			pSQL.append("hgwcust_serv_info d,");
		}
		pSQL.append("stb_tab_gw_device e left join stb_tab_customer f on e.customer_id=f.customer_id ");
		pSQL.append("where a.device_id = c.device_id and a.device_id = b.device_id ");
		if(!StringUtil.IsEmpty(netUsername)){
			pSQL.append("and b.user_id=d.user_id and d.serv_type_id=10 and d.username='"+netUsername+"' ");
		}
		pSQL.append("and a.stb_mac=e.cpe_mac ");

		if(!StringUtil.IsEmpty(loid)){
			pSQL.append(" and b.username='" + loid + "' ");
		}

		if(!StringUtil.IsEmpty(devSn)){
			pSQL.append(" and c.device_serialnumber='" + devSn + "' ");
		}

		if(!StringUtil.IsEmpty(mac)){
			pSQL.append(" and c.cpe_mac='" + mac + "' ");
		}

	    if(!StringUtil.IsEmpty(servAccount)){
	    	pSQL.append(" and f.serv_account='" + servAccount + "'");
	    }

	    if(!StringUtil.IsEmpty(stbDevSn)){
	    	pSQL.append(" and e.device_serialnumber='" + stbDevSn + "'");
	    }

	    if(!StringUtil.IsEmpty(stbMac)){
	    	pSQL.append(" and e.cpe_mac='" + stbMac + "' ");
	    }

	    pSQL.append(" order by a.update_time desc ");
	    if(-1 != currPage){
	    	return querySP(pSQL.getSQL(), (currPage - 1) * numPage,
					numPage,new RowMapper() {
				  public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					try {
						//光猫
						map.put("devId", rs.getString("devId"));
						map.put("loid", rs.getString("loid"));
						map.put("devSn", rs.getString("devSn"));
						map.put("mac", rs.getString("mac"));

						//机顶盒
						map.put("servAccount", rs.getString("serv_account"));
						map.put("stbDevSn", rs.getString("stbDevSn"));
						map.put("stbDevId", rs.getString("stbDevId"));
						map.put("stbmac", rs.getString("stbmac"));
						map.put("updatetime",  new DateTimeUtil(rs.getLong("update_time") * 1000)
						.getYYYY_MM_DD_HH_mm_ss());

					} catch (SQLException e) {
						logger.error(e.getMessage());
					}
					return map;
				}
			});
	    }else{
	    	return jt.query(pSQL.getSQL(), new RowMapper()
			{
				public Object mapRow(ResultSet rs, int arg1) throws SQLException
				{
					Map<String, String> map = new HashMap<String, String>();
					try {
						//光猫
						map.put("devId", rs.getString("devId"));
						map.put("loid", rs.getString("loid"));
						map.put("devSn", rs.getString("devSn"));
						map.put("mac", rs.getString("mac"));

						//机顶盒
						map.put("servAccount", rs.getString("serv_account"));
						map.put("stbDevSn", rs.getString("stbDevSn"));
						map.put("stbDevId", rs.getString("stbDevId"));
						map.put("stbmac", rs.getString("stbmac"));
						map.put("updatetime",  new DateTimeUtil(rs.getLong("update_time") * 1000)
						.getYYYY_MM_DD_HH_mm_ss());

					} catch (SQLException e) {
						logger.error(e.getMessage());
					}
					return map;
				}
			});
	    }
	}

	public int qryStbResCount(String loid, String devSn, String mac,String netUsername,String servAccount,String stbDevSn,String stbMac){
		String sql = "select count(*) ";
		PrepareSQL pSQL = new PrepareSQL();

		pSQL.append(sql);// TODO wait (more table related)
		pSQL.append(" from tab_gw_device_stbmac a, tab_hgwcustomer b,tab_gw_device c,");
		if(!StringUtil.IsEmpty(netUsername)){
			pSQL.append("hgwcust_serv_info d,");
		}
		pSQL.append("stb_tab_gw_device e left join stb_tab_customer f on e.customer_id=f.customer_id ");
		pSQL.append("where a.device_id = c.device_id and a.device_id = b.device_id ");
		if(!StringUtil.IsEmpty(netUsername)){
			pSQL.append("and b.user_id=d.user_id and d.serv_type_id=10 and d.username='"+netUsername+"' ");
		}
		pSQL.append("and a.stb_mac=e.cpe_mac ");

		if(!StringUtil.IsEmpty(loid)){
			pSQL.append(" and b.username='" + loid + "' ");
		}

		if(!StringUtil.IsEmpty(devSn)){
			pSQL.append(" and c.device_serialnumber='" + devSn + "' ");
		}

		if(!StringUtil.IsEmpty(mac)){
			pSQL.append(" and c.cpe_mac='" + mac + "' ");
		}

	    if(!StringUtil.IsEmpty(servAccount)){
	    	pSQL.append(" and f.serv_account='" + servAccount + "'");
	    }

	    if(!StringUtil.IsEmpty(stbDevSn)){
	    	pSQL.append(" and e.device_serialnumber='" + stbDevSn + "'");
	    }

	    if(!StringUtil.IsEmpty(stbMac)){
	    	pSQL.append(" and e.cpe_mac='" + stbMac + "' ");
	    }
		try
		{
			return jt.queryForInt(pSQL.getSQL());
		}
		catch (Exception e)
		{
			return 0;
		}
	}
}
