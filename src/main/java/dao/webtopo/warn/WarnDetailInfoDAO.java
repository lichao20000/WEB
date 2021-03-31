package dao.webtopo.warn;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.CommonUtil;

/**
 * WebTopo实时告警牌告警规则定义与修改DAO
 * <li>REQ: GZDX-REQ-20080402-ZYX-001
 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
 * 
 * @author 段光锐
 * @version 1.0
 * @since 2008-4-8
 * @category WebTopo/实时告警牌/告警规则
 * 
 */
public class WarnDetailInfoDAO
{
	// jdbc模板
	private JdbcTemplate jt;

	
	private final String addTabKnowledgeInfoSQL = "insert into tab_knowledge(serialno,subserialno,gather_id,subject,content,"
			+ "creator,createtime,warnreason,warnresove,ack_createtime,devicetype,sourceip,creatortype,sourcename) ";

	/**
	 * 通过告警序列号,子序列号,采集点,表名查询告警详情
	 * 
	 * @param serialNo
	 *            告警序列号
	 * @param subSerialNo
	 *            子序列号(WarnDetailInfoAction写死0)
	 * @param gatherId
	 *            采集点
	 * @param tableName
	 *            表名(例如:event_raw_2008_16)
	 * @return
	 */
	public List getWarnDetailInfo(String serialNo, int subSerialNo,
			String gatherId, String tableName)
	{
		String getWarnDetailInfoSQL = "select a.devicecoding,a.serialno,a.creatortype,a.creatorname,a.createtime,"
				+ "a.displaytitle,a.displaystring,a.sourcename,a.sourceip,a.devicetype,a.severity,"
				+ "a.filttimes,a.cleartime,a.acktime,a.activestatus,a.clearstatus,a.operaccount,a.remark,"
				+ "b.device_model_id,b.device_addr,b.device_type,"
				+ "e.device_model,c.customer_name,d.warnreason,d.warnresove,d.serialno as firsttime from "
				+ tableName
				+ " a left join tab_gw_device b on a.devicecoding=b.device_id"
				+ " left join tab_customerinfo c on b.customer_id=c.customer_id "
				+ " left join tab_knowledge d on a.serialno=d.serialno and a.subserialno=d.subserialno and a.gather_id=d.gather_id"
				+ " left join gw_device_model e on b.device_model_id=e.device_model_id"
				+ " where a.subserialno="
				+ subSerialNo
				+ " and a.gather_id='"
				+ gatherId + "' and a.serialno='" + serialNo + "'";
		PrepareSQL psql = new PrepareSQL(getWarnDetailInfoSQL);
		psql.getSQL();
		return jt.queryForList(getWarnDetailInfoSQL);
	}

	/**
	 * 获取该告警源设备的SysLog日志相关信息
	 * 
	 * @param deviceId
	 *            告警源设备ID
	 * @return
	 */
	public List getSysLogInfo(String deviceId)
	{
		String getSysLogInfoSQL = "select a.file_name,a.file_desc,a.file_size,a.time,b.inner_url,b.server_dir from"
				+ " gw_syslog_file a left join tab_file_server b on a.dir_id=b.dir_id"
				+ " where a.device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(getSysLogInfoSQL);
		psql.getSQL();
		List<Map> list = jt.query(getSysLogInfoSQL, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1)
					throws java.sql.SQLException
			{
				Map m = new HashMap();

				m.put("file_desc", rs.getString("file_desc"));
				m.put("file_size", rs.getBigDecimal("file_size").intValue());
				// 时间
				long l = rs.getBigDecimal("time").longValue();
				m.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date(l * 1000)));

				String file_name = rs.getString("file_name");
				String inner_url = rs.getString("inner_url");
				String server_dir = rs.getString("server_dir");
				m.put("file_name", file_name);
				m.put("inner_url", inner_url);
				m.put("server_dir", server_dir);

				// 在此拼装文件的URL
				m.put("URL", inner_url + "/" + server_dir + "/" + file_name);
				return m;
			}
		});

		return list;
	}

	/**
	 * 保存 告警附加说明
	 * 
	 * @param serialNo
	 *            告警序列号
	 * @param subSerialNo
	 *            子序列号(WarnDetailInfoAction写死0)
	 * @param gatherId
	 *            采集点
	 * @param tableName
	 *            表名(例如:event_raw_2008_16)
	 * @param remark
	 *            告警附加说明
	 * @return
	 */
	public boolean saveRemarkInfo(String serialNo, int subSerialNo,
			String gatherId, String tableName, String remark)
	{
		String saveRemarkInfoSQL = "update " + tableName + " set remark='"
				+ remark + "' where subserialno=" + subSerialNo
				+ " and gather_id='" + gatherId + "' and serialno='" + serialNo
				+ "'";
		PrepareSQL psql = new PrepareSQL(saveRemarkInfoSQL);
		psql.getSQL();
		int updateNum = 0;
		updateNum = jt.update(saveRemarkInfoSQL);
		return (updateNum > 0 ? true : false);
	}

	/**
	 * 添加告警知识库内容
	 * 
	 * @param serialNo
	 *            序列号
	 * @param subSerialNo
	 *            子序列号(WarnDetailInfoAction写死0)
	 * @param gatherId
	 *            采集点
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @param acc_loginname
	 *            确认记录创建人,即当前登陆用户
	 * @param createTime
	 *            告警创建时间
	 * @param warnReason
	 *            故障原因
	 * @param warnResove
	 *            解决方法
	 * @param curTime
	 *            告警确认时间,即当前时间
	 * @param deviceType
	 *            告警设备类型
	 * @param sourceIP
	 *            告警源设备IP
	 * @param createType
	 *            告警创建者类型
	 * @param sourceName
	 *            告警源设备名称
	 * @return
	 */
	public boolean addTabKnowledgeInfo(String serialNo, int subSerialNo,
			String gatherId, String subject, String content,
			String acc_loginname, long createTime, String warnReason,
			String warnResove, long curTime, int deviceType, String sourceIP,
			int createType, String sourceName)
	{
		String addTabKnowledgeInfoSQL = this.addTabKnowledgeInfoSQL
				+ "values('" + serialNo + "'," + subSerialNo + ",'" + gatherId
				+ "','" + subject + "','" + content + "','" + acc_loginname
				+ "'," + createTime + ",'" + warnReason + "','" + warnResove
				+ "'," + curTime + "," + deviceType + ",'" + sourceIP + "',"
				+ createType + ",'" + sourceName + "')";
		PrepareSQL psql = new PrepareSQL(addTabKnowledgeInfoSQL);
		psql.getSQL();
		int addNum = 0;
		addNum = jt.update(addTabKnowledgeInfoSQL);
		return (addNum > 0 ? true : false);
	}

	/**
	 * 修改告警知识库内容
	 * 
	 * @param serialNo
	 * @param subSerialNo
	 * @param gatherId
	 * @param warnReason
	 * @param warnResove
	 * @return
	 */
	public boolean updateTabKnowledgeInfo(String serialNo, int subSerialNo,
			String gatherId, String warnReason, String warnResove)
	{
		String updateTabKnowledgeInfoSQL = "update tab_knowledge set warnreason='"
				+ warnReason
				+ "',warnresove='"
				+ warnResove
				+ "' where serialno='"
				+ serialNo
				+ "' and subserialno="
				+ subSerialNo + " and gather_id='" + gatherId + "'";
		PrepareSQL psql = new PrepareSQL(updateTabKnowledgeInfoSQL);
		psql.getSQL();
		int updateNum = 0;
		updateNum = jt.update(updateTabKnowledgeInfoSQL);
		return (updateNum > 0 ? true : false);
	}

	/**
	 * 获取自动清除告警编号为 <cdoe>ackserialno</code> 的最近的一次告警
	 * 
	 * @param ackserialno
	 *            自动清除告警的编号 格式( 0.serialno ),其中serialno为排重为的告警序列号
	 * @param gatherId
	 *            采集点
	 * @param tableNameA
	 *            被排重告警所在表
	 * @param tableNameB
	 *            被排重告警所在表的下一张表
	 * @return
	 */
	public List getLastFilterCreateTime(String ackserialno, String gatherId,
			String tableNameA, String tableNameB)
	{
		// 判断tableNameB是否在数据库中存在
		int exist = CommonUtil.tableIsExist(tableNameB, jt);
		String getLastFilterCreateTimeSQL = null;
		if (exist == 0)
		{
			getLastFilterCreateTimeSQL = "select max(createtime) as lastfiltercreatetime from "
					+ tableNameA
					+ " where ackserialno='"
					+ ackserialno
					+ "' and gather_id = '" + gatherId + "'";
		} else
		{
			getLastFilterCreateTimeSQL = "select max(createtime) as lastfiltercreatetime from ("
					+ "select createtime from "
					+ tableNameA
					+ " where ackserialno='"
					+ ackserialno
					+ "' and gather_id = '"
					+ gatherId
					+ "' union all select createtime from "
					+ tableNameB
					+ " where ackserialno='"
					+ ackserialno
					+ "' and gather_id = '" + gatherId + "') d";
		}
		PrepareSQL psql = new PrepareSQL(getLastFilterCreateTimeSQL);
		psql.getSQL();
		return jt.queryForList(getLastFilterCreateTimeSQL);
	}

	/**
	 * 初始化数据库连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}

}