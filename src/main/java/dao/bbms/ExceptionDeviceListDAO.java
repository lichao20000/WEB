package dao.bbms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;

/**
 * @author 王志猛(工号) tel：12345678
 * @version 1.0
 * @since 2008-6-11
 * @category dao.bbms 版权：南京联创科技 网管科技部
 *
 */
public class ExceptionDeviceListDAO
{
	private JdbcTemplate jt;
	private static final Logger LOG = LoggerFactory.getLogger(ExceptionDeviceListDAO.class);
	/**
	 * 获取异常设备列表
	 *
	 * @param sDate
	 *            起始时间点 格式:yyyy-M-d HH:mm:SS
	 * @param eDate
	 *            结束时间点 格式:yyyy-M-d HH:mm:SS
	 * @return List《Map》
	 */
	public List<Map> getExpDevList(String sDate, String eDate,int dealstatus)
	{
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
		long st = 0;
		long et = 0;
		try
			{
				st = f.parse(sDate).getTime() / 1000L;
				et = f.parse(eDate).getTime() / 1000L;
			} catch (Exception e)
			{
				LOG.error("日期格式化信息错误，可能是传入的格式不对", e);
			}
		String sql = "select exception_time,a.device_id, "
				+ "type,status,result_id,oui,device_serialnumber,acs_config,cpe_config"
				+ " from gw_exception a left join tab_gw_device b on a.device_id=b.device_id where exception_time>"
				+ st + " and exception_time<" + et+(dealstatus==2?"":(" and status="+dealstatus));
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.query(sql, new RowMapper()
		{
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Map m = new HashMap();
				m.put("exception_time", new Date(Long.parseLong(rs.getBigDecimal(
						"exception_time").toPlainString()) * 1000));
				m.put("exception_mark", rs.getBigDecimal("exception_time")
						.toPlainString());
				m.put("device_id", rs.getString("device_id"));
				m.put("device_info", rs.getString("oui") + "-"
						+ rs.getString("device_serialnumber"));
				int type = rs.getInt("type");
				m.put("exceptionType", type == 1 ? "认证不通过" : type == 2 ? "定制终端ID 不存在"
						: type == 3 ? "定制终端ID、宽带帐号不匹配" : "定制终端ID、IP 地址不匹配");
				m.put("status", rs.getInt("status"));
				m.put("acs_config", rs.getString("acs_config"));
				m.put("cpe_config", rs.getString("cpe_config"));
				return m;
			}
		});
	}
	/**
	 * 处理异常
	 *
	 * @param device_id
	 *            设备id
	 * @param doinfo
	 *            处理的信息
	 * @param acc_oid
	 *            用户帐户id
	 * @return 是否处理成功
	 */
	public boolean doExpDev(String device_id, String doinfo, long acc_oid,
			long exception_time)
	{
		String sql = "update gw_exception set result_id=1,deal_time="
				+ new Date().getTime() / 1000 + ",acc_oid=" + acc_oid + ",result_desc='"
				+ doinfo + "',status=1 where device_id='" + device_id
				+ "' and exception_time=" + exception_time;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.update(sql) == 1;
	}
	/**
	 * 查看异常的确认信息
	 *
	 * @param device_id
	 *            异常设备id
	 * @param exception_time
	 *            异常发生时间
	 * @return
	 */
	public Map viewExpDev(String device_id, long exception_time)
	{
		String sql = "select deal_time,result_id,type,result_desc,acc_loginname,oui,device_serialnumber from gw_exception a  left join tab_accounts b on a.acc_oid=b.acc_oid left join tab_gw_device c on a.device_id=c.device_id where a.device_id='"
			+ device_id
			+ "' and exception_time="
			+ exception_time;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return (Map)jt.queryForObject(sql, new RowMapper(){
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Map m =new HashMap();
				m.put("deal_time", new Date(Long.parseLong(rs.getBigDecimal("deal_time").toPlainString())*1000));
				m.put("result_desc", rs.getString("result_desc"));
				m.put("acc_loginname", rs.getString("acc_loginname"));
				m.put("device_info", rs.getString("oui")+"-"+rs.getString("device_serialnumber"));
				int type = rs.getInt("type");
				m.put("exceptionType", type == 1 ? "认证不通过" : type == 2 ? "定制终端ID 不存在"
						: type == 3 ? "定制终端ID、宽带帐号不匹配" : "定制终端ID、IP 地址不匹配");
				m.put("dealResult", rs.getInt("result_id")==0?"失败":"成功");
				return m;
			}

		});
	}
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
}
