package com.linkage.liposs.buss.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;

/**
 * 
 * @author liuw
 * @version 1.0
 * @since 2007-8-14
 * @category 网络时延实体类
 */
public class PingTest {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(PingTest.class);
	
	private PrepareSQL PSQL = null;

	private DataSource dao;

	private JdbcTemplate jt;

	private ChartUtil cu;

	private String[] rows;

	/**
	 * 时延详细数据
	 * 
	 * @param ip
	 * @return
	 */
	public ArrayList getParticularDataList(String ip) {
		ArrayList array_obj = new ArrayList();
		ArrayList hourReportList = new ArrayList();
		String ip_add = ip;
		String delaythreshold = this.getDelaythreshold(ip);
		double db_delaythreshold = Double.parseDouble(delaythreshold);
		double critical_1 = db_delaythreshold * 1.5;
		double critical_2 = db_delaythreshold * 3;

		Date today = new Date();
		SimpleDateFormat sdf = null;
		sdf = new SimpleDateFormat("yyyy_M_d");

		try {
			today = sdf.parse(sdf.format(today));
		} catch (ParseException e) {
			logger.warn(e.getMessage());
		}

		String table_name_1 = "pping_result_" + sdf.format(today);
		long start = today.getTime() / 1000;
		long end = today.getTime() / 1000 + 86400;

		sdf = new SimpleDateFormat("yyyy_M");
		String table_name_2 = "pping_result_stat_" + sdf.format(today);

		String sql_HourReport = "select distinct packetid,sum(sentpacketcount) as sentpacketcount,"
				+ "sum(recvdpacketcount) as recvdpacketcount,sum(belowlimitpacketcount) as belowlimitpacketcount,"
				+ "avg(averageoftimedelay) as averageoftimedelay from ? where ip = ? and statistime>=? and statistime<? group by packetid ";
		PSQL.setSQL(sql_HourReport);
		PSQL.setStringExt(1, table_name_2, false);
		PSQL.setStringExt(2, ip_add, true);
		PSQL.setLong(3, start);
		PSQL.setLong(4, end);
		logger.debug("小时报表统计：" + PSQL.getSQL());
		jt = new JdbcTemplate(dao);
		hourReportList = (ArrayList) jt.queryForList(PSQL.getSQL());
		String sql_1 = "select ip, packetid, count(responsedelay) as num from ? where ip=? and responsedelay>"
				+ delaythreshold
				+ " and responsedelay<="
				+ critical_1
				+ " group by ip,packetid";
		PSQL.setSQL(sql_1);
		PSQL.setStringExt(1, table_name_1, false);
		PSQL.setStringExt(2, ip_add, true);
		logger.debug("不达标时延1：" + PSQL.getSQL());
		ArrayList list_pn2 = (ArrayList) jt.queryForList(PSQL.getSQL());
		String packetid = null;
		String pn2_num = null;
		HashMap<String, String> map_pn2 = new HashMap<String, String>();

		for (int i = 0; i < list_pn2.size(); i++) {
			packetid = String.valueOf(((Map) list_pn2.get(i)).get("packetid"));
			pn2_num = String.valueOf(((Map) list_pn2.get(i)).get("num"));
			map_pn2.put(packetid, pn2_num);
		}

		String sql_2 = "select ip, packetid, count(responsedelay) as num from ? where ip=? and responsedelay>"
				+ critical_1
				+ " and responsedelay <="
				+ critical_2
				+ " group by ip, packetid";
		PSQL.setSQL(sql_2);
		PSQL.setStringExt(1, table_name_1, false);
		PSQL.setStringExt(2, ip_add, true);
		logger.debug("不达标时延2：" + PSQL.getSQL());
		ArrayList list_pn3 = (ArrayList) jt.queryForList(PSQL.getSQL());
		String pn3_num = null;
		HashMap<String, String> map_pn3 = new HashMap<String, String>();

		for (int i = 0; i < list_pn3.size(); i++) {
			packetid = String.valueOf(((Map) list_pn3.get(i)).get("packetid"));
			pn3_num = String.valueOf(((Map) list_pn3.get(i)).get("num"));
			map_pn3.put(packetid, pn3_num);
		}

		String sql_3 = "select ip, packetid, count(responsedelay) as num from ? where ip=? and responsedelay>"
				+ critical_2 + " group by ip,packetid";
		PSQL.setSQL(sql_3);
		PSQL.setStringExt(1, table_name_1, false);
		PSQL.setStringExt(2, ip_add, true);
		logger.debug("不达标时延3：" + PSQL.getSQL());
		ArrayList list_pn4 = (ArrayList) jt.queryForList(PSQL.getSQL());
		String pn4_num = null;
		HashMap<String, String> map_pn4 = new HashMap<String, String>();

		for (int i = 0; i < list_pn4.size(); i++) {
			packetid = String.valueOf(((Map) list_pn4.get(i)).get("packetid"));
			pn4_num = String.valueOf(((Map) list_pn4.get(i)).get("num"));
			map_pn4.put(packetid, pn4_num);
		}

		String sql_max_min = "select ip, packetid, MAX(responsedelay) as max_resp, MIN(responsedelay) as min_resp from ? where ip=? group by ip, packetid";
		PSQL.setSQL(sql_max_min);
		PSQL.setStringExt(1, table_name_1, false);
		PSQL.setStringExt(2, ip_add, true);
		logger.debug("最大时延与最小时延：" + PSQL.getSQL());
		ArrayList list_max_min = (ArrayList) jt.queryForList(PSQL.getSQL());
		String max_resp = null;
		String min_resp = null;
		HashMap<String, String> map_max_min = new HashMap<String, String>();

		for (int i = 0; i < list_max_min.size(); i++) {
			packetid = String.valueOf(((Map) list_max_min.get(i))
					.get("packetid"));
			max_resp = String.valueOf(((Map) list_max_min.get(i))
					.get("max_resp"));
			min_resp = String.valueOf(((Map) list_max_min.get(i))
					.get("min_resp"));
			map_max_min.put(packetid + "max", max_resp);
			map_max_min.put(packetid + "min", min_resp);
		}

		String packetset = "select * from pping_packetset where groupid in(select groupid from pping_ip_group_map where ip=?)";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			packetset = "select packetid, packetsize from pping_packetset where groupid in(select groupid from pping_ip_group_map where ip=?)";
		}
		PSQL.setSQL(packetset);
		PSQL.setStringExt(1, ip, true);
		logger.debug("包类型信息：" + PSQL.getSQL());
		ArrayList list_packetset = (ArrayList) jt.queryForList(PSQL.getSQL());
		String packetsize = null;
		HashMap<String, String> map_packetset = new HashMap<String, String>();

		for (int i = 0; i < list_packetset.size(); i++) {
			packetid = String.valueOf(((Map) list_packetset.get(i))
					.get("packetid"));
			packetsize = String.valueOf(((Map) list_packetset.get(i))
					.get("packetsize"));
			map_packetset.put(packetid, packetsize);
		}

		array_obj.add(hourReportList);
		array_obj.add(map_pn2);
		array_obj.add(map_pn3);
		array_obj.add(map_pn4);
		array_obj.add(map_packetset);
		array_obj.add(map_max_min);

		return array_obj;
	}

	/**
	 * 查询出设备要生成图片的详细数据并生成图片
	 * 
	 * @param ip
	 * @return JFreeChart
	 */
	public JFreeChart getGraphicsCursor(String ip) {
		ArrayList tmpList = new ArrayList();
		ArrayList numList = new ArrayList();
		String ip_add = null;
		Map tmpMap = null;
		Date timeStart = null;
		Date timeEnd = null;
		String pid = "";
		long start = 0;
		long end = 0;
		Date today = new Date();
		SimpleDateFormat sdf = null;
		sdf = new SimpleDateFormat("yyyy_M_d");

		try {
			today = sdf.parse(sdf.format(today));
		} catch (ParseException e) {
			logger.warn(e.getMessage());
		}

		start = today.getTime() / 1000;
		end = today.getTime() / 1000 + 86400;
		ip_add = ip;

		String tableName = "pping_result_" + sdf.format(today);

		String strSQL = "select distinct(packetid) as pid from ? "
				+ " where ip=? and pingtime>=? and pingtime<? ";

		String sql = "select packetid, pingtime, responsedelay from ? "
				+ " where packetid=? group by packetid,pingtime having ip=? and pingtime>=? and pingtime<? "
				+ "order by packetid, pingtime";

		PSQL.setSQL(strSQL);
		PSQL.setStringExt(1, tableName, false);
		PSQL.setStringExt(2, ip_add, true);
		PSQL.setLong(3, start);
		PSQL.setLong(4, end);
		jt = new JdbcTemplate(dao);
		numList = (ArrayList) jt.queryForList(PSQL.getSQL());

		ArrayList<Map>[] objList = new ArrayList[numList.size()];
		rows = new String[numList.size()];
		for (int m = 0; m < numList.size(); m++) {
			tmpMap = (Map) numList.get(m);
			pid = String.valueOf(tmpMap.get("pid"));

			PSQL.setSQL(sql);
			PSQL.setStringExt(1, tableName, false);
			PSQL.setInt(2, Integer.parseInt(pid));
			PSQL.setStringExt(3, ip_add, true);
			PSQL.setLong(4, start);
			PSQL.setLong(5, end);
			rows[m] = this.getPackageSet(ip).get(pid).toString() + " (字节)";
			objList[m] = (ArrayList) jt.queryForList(PSQL.getSQL());
		}
		//cu.setYMMT(ChartYType.PING);  //Y轴的范围设置，暂时取消
		JFreeChart chart = cu.createSTP("", "时间", "时延:单位(毫秒)", rows, objList,
				"pingtime", "responsedelay", 1);
		return chart;
	}

	/**
	 * 通过IP查询出该IP对应的ping包大小
	 * 
	 * @param ip
	 * @return Map key:packetid value:packetsize
	 */
	public Map getPackageSet(String ip) {
		HashMap<String, String> map = new HashMap<String, String>();
		ArrayList<Map> al = new ArrayList<Map>();
		String packageSQL = "select packetid,packetsize from pping_packetset where groupid in(select groupid from pping_ip_group_map where ip='"
				+ ip + "')";
		jt = new JdbcTemplate(dao);
		PrepareSQL psql = new PrepareSQL(packageSQL);
		al = (ArrayList) jt.queryForList(psql.getSQL());
		for (int z = 0; z < al.size(); z++) {
			map.put(((Map) al.get(z)).get("packetid").toString(), ((Map) al
					.get(z)).get("packetsize").toString());
		}
		return map;
	}

	/**
	 * 根据IP地址获取不同的ping组的值
	 * 
	 * @param ipAdd
	 * @return String ping组的时延的值 字段：delaythreshold
	 */
	public String getDelaythreshold(String ipAdd) {
		String delaySQL = "select delaythreshold from pping_group_conf where groupid in(select groupid from pping_ip_group_map where ip='"
				+ ipAdd + "')";
		jt = new JdbcTemplate(dao);
		PrepareSQL psql = new PrepareSQL(delaySQL);
		ArrayList delList = (ArrayList) jt.queryForList(psql.getSQL());
		String delaythreshold = "";
		for (int i = 0; i < delList.size(); i++) {
			delaythreshold = String.valueOf(((Map) delList.get(i))
					.get("delaythreshold"));
		}
		return delaythreshold;
	}

	public void setDao(DataSource dao) {
		this.dao = dao;
	}

	public void setPSQL(PrepareSQL psql) {
		PSQL = psql;
	}

	public void setCu(ChartUtil cu) {
		this.cu = cu;
	}

}
