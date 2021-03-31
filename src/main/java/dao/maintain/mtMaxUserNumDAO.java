package dao.maintain;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;

public class mtMaxUserNumDAO {

	private static Logger log = LoggerFactory.getLogger(mtMaxUserNumDAO.class);
	
	private JdbcTemplate jt;
	
	/**
	 * 从数据库中取值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getMaxUserNumFromDB(String device_id) {
		StringBuffer buffSQL = new StringBuffer();

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			buffSQL.append("select a.username, b.device_serialnumber, c.mode, c.total_number from tab_egwcustomer a, tab_gw_device b, gw_mwband c ");
		}
		else {
			buffSQL.append("select a.username, b.device_serialnumber, c.* from tab_egwcustomer a, tab_gw_device b, gw_mwband c ");
		}
		buffSQL.append("where a.device_id = b.device_id and a.user_state in ('1','2') ");
		buffSQL.append("and a.device_serialnumber!=null and a.device_serialnumber!='' and b.device_id = c.device_id and b.device_id='");
		buffSQL.append(device_id);
		buffSQL.append("'");
		
		PrepareSQL psql = new PrepareSQL(buffSQL.toString());
		psql.getSQL();
		
		return jt.queryForList(buffSQL.toString());
	}

	
	/**
	 * 数据保存入库
	 */
	public void setMaxNumToDB() {
		StringBuffer buffSQL = new StringBuffer();
		buffSQL.append("insert  username, maxUserNum, mode");
		PrepareSQL psql = new PrepareSQL(buffSQL.toString());
		psql.getSQL();
		jt.execute(buffSQL.toString());
	}
	
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
}
