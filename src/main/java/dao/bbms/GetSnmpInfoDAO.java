package dao.bbms;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;

public class GetSnmpInfoDAO
{
	private JdbcTemplate jt;
	/**
	 * 初始化数据库链接
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
	/**
	 * 查询指定设备的mac地址
	 * @param device_id
	 * @return
	 */
	public String getMacInfo(String device_id){
		String sql = "select cpe_mac from tab_gw_device where device_id='" + device_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List list = jt.queryForList(sql);
		if (list != null && list.size() > 0){
			Map map = (Map)list.get(0);
			String mac = (String)map.get("cpe_mac");
			if (mac != null){
				return mac;
			}
			else{
				return "没有配置MAC地址";
			}
		}
		else{
			return "没有配置MAC地址";
		}
	}
}
