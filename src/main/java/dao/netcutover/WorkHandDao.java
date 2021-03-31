package dao.netcutover;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
/**
 * 
 * @author benny
 * @since2007-10-26
 * @version 1.0
 */
public class WorkHandDao {
	
	//jdbc模板
	private JdbcTemplate jt;
	
	//数据源
	private DataSource dao;
	
	//访问数据库返回的list
	private List<Map> list;
	
	/**
	 * 获取业务下拉框
	 * @return
	 */
	public List<Map> getServiceList(){
		
		PrepareSQL psql = new PrepareSQL("select service_id,service_name from tab_service where service_id >500 and service_id < 600");
		psql.getSQL();
		list  = jt.queryForList("select service_id,service_name from tab_service where service_id >500 and service_id < 600");
		
		return list;
		
	}
	

	public void setDao(DataSource dao) {
		this.dao = dao;
		jt = new JdbcTemplate(dao);
	}
	
	
	

}
