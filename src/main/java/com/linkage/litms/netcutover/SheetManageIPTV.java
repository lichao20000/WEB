package com.linkage.litms.netcutover;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.ExportExcelUtil;
import com.linkage.litms.common.util.StringUtils;


public class SheetManageIPTV {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(SheetManageIPTV.class);

	/**
	 * 返回工单列表.
	 * 
	 * @param request
	 * 
	 * @param
	 * 
	 * @return 返回Cursor类型数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Cursor getSheetList(HttpServletRequest request) {
		String str_lms = request.getParameter("start");
		String str_lms_end = request.getParameter("end");

		long startTime = Long.parseLong(str_lms);
		logger.debug("startTime :" + startTime);

		Date dt;

		// 初始化页面的时候默认查询1天
		// 将查询细度由原半天放大到一天
		// 时间跨度为24小时
		if (StringUtil.IsEmpty(str_lms_end)) {
			dt = new Date(startTime + 24 * 3600);
		}
		else {
			dt = new Date(Long.parseLong(str_lms_end));
		}
		long endTime = dt.getTime();
		// 过滤条件
		String filter = request.getParameter("filter");

		StringBuffer sb = new StringBuffer("select a.id, a.username, a.device_serialnumber, ");
		sb.append("'iptv' as serv_type,  a.result_id, a.status, a.start_time, ");
		sb.append("a.end_time, b.fault_desc from stb_gw_serv_strategy a, tab_cpe_faultcode b ");
		sb.append("where a.result_id = b.fault_code ");
		if (!StringUtil.IsEmpty(filter)) {
			// 成功
			if ("0".equals(filter)) {
				sb.append("and a.result_id = 1 ");
			}
			// 失败
			if ("1".equals(filter)) {
				sb.append("and a.result_id != 1 ");
			}
		}
		sb.append(" and a.start_time>= " + startTime);
		sb.append(" and a.start_time <= " + endTime);
		sb.append(" order by a.start_time desc ");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sb.toString(), "proxool.xml-stb");
		Map map = cursor.getNext();
		Cursor ret = new Cursor();
		while (map != null) {
			String status = map.get("status").toString();
			String status_name = "";
			if ("0".equals(status)) {
				status_name = "等待执行";
			}
			else if ("1".equals(status)) {
				status_name = "预读PVC";
			}
			else if ("2".equals(status)) {
				status_name = "预读绑定端口";
			}
			else if ("3".equals(status)) {
				status_name = "预读无线";
			}
			else if ("4".equals(status)) {
				status_name = "业务下发";
			}
			else if ("100".equals(status)) {
				status_name = "执行完成";
			}
			map.put("status_name", status_name);
			map.put("oper_type", "开户");
			map.put("start_time_str", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", 
					Long.parseLong(StringUtil.getStringValue(map.get("start_time")))));
			
			map.put("end_time_str", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", 
					Long.parseLong(StringUtil.getStringValue(map.get("end_time")))));
			ret.add(map);
			map = cursor.getNext();
		}
		return ret;
	}
	
	/**
	 * 导出
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String toExcel(HttpServletRequest request, HttpServletResponse response) {
		// 处理数据导出
		ExportExcelUtil util = new ExportExcelUtil("", new String[] { "策略编号",
						//"业务账号",
						"设备序列号", "业务类型", "操作类型", "策略执行结果", "策略开始时间", "策略结束时间", "失败原因"});
		try
		{
			Cursor cursor = getSheetList(request);
			Map mp = cursor.getNext();
			List<Map> list = new ArrayList();
			while (mp != null) {
				list.add(mp);
				mp = cursor.getNext();
			}
			util.export(response, list, 
					new String[]{"id", 
					//"username", 
					"device_serialnumber", "serv_type", "oper_type", "status_name", "start_time_str", "end_time_str", "fault_desc"}, "worksheetiptv");
		}
		catch (Exception e)
		{
			logger.error("export error:", e);
		}
		return null;
	}
}
