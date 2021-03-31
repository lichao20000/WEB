package com.linkage.litms.flux;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;

/**
 * 获得用户可以管理并同时存在于端口详细配置表的设备；
 */
public class UserManagerDev {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(UserManagerDev.class);
	private HttpServletRequest request = null;

	private PrepareSQL pSQL = null;

	private String strSQL1 = "select distinct device_id from flux_interfacedeviceport where device_id in (?)";

	private String strSQL2 = "select ifindex,ifdescr,ifname,ifnamedefined,ifportip,getway,if_real_speed from flux_interfacedeviceport where device_id=?";

	private String strSQL3 = "select port_info,getway,ifinoctetsbps,ifinoctetsbpsmax,ifinoctets,ifoutoctetsbps,ifoutoctetsbpsmax,ifoutoctets from ? "
			+ "where device_id=? and (?) and collecttime>? and collecttime<? order by getway,port_info";

	private String strSQL4 = "select distinct(port_info),device_id,getway,collecttime,? from ? where device_id=? and(?) and collecttime>=? and collecttime<? order by getway,port_info";

	private String condition = new String();

	private HashMap map_realSpeed = new HashMap();

	private HashMap map_baseFluxInfo = new HashMap();

	public UserManagerDev() {

		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Cursor getUserDevID() {
		Cursor cursor = null;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		List list_UserDevID = (List) curUser.getUserDevRes(curUser.getUser());
		String str_UserDevID = new String();

		if (list_UserDevID.size() != 0) {

			for (int i = 0; i < list_UserDevID.size(); i++) {
				str_UserDevID = str_UserDevID + "'"
						+ (String) list_UserDevID.get(i) + "',";
			}

			str_UserDevID = str_UserDevID.substring(0,
					str_UserDevID.length() - 1);

			pSQL.setSQL(strSQL1);
			pSQL.setStringExt(1, str_UserDevID, false);
			cursor = DataSetBean.getCursor(pSQL.getSQL());
			logger.debug("用户管理设备:" + pSQL.getSQL());
		}
		return cursor;
	}

	public Cursor getReport_Forms(String device_id, String tablName,
			String startTime, String endTime) {

		Cursor cursor_deviceport = null;
		Cursor cursor_fluxReport = null;
		Map result = null;
		String key = null;
		String ifindex = new String();
		String ifdescr = new String();
		String ifname = new String();
		String ifnamedefined = new String();
		String ifportip = new String();
		String str_getway = new String();
		String if_real_speed = null;

		pSQL.setSQL(strSQL2);
		pSQL.setString(1, device_id);
		cursor_deviceport = DataSetBean.getCursor(pSQL.getSQL());
		logger.debug("端口信息:" + pSQL.getSQL());
		result = cursor_deviceport.getNext();

		while (result != null) {
			ifindex = (String) result.get("ifindex");
			ifdescr = (String) result.get("ifdescr");
			ifname = (String) result.get("ifname");
			ifportip = (String) result.get("ifportip");
			ifnamedefined = (String) result.get("ifnamedefined");
			if_real_speed = (String) result.get("if_real_speed");

			BasePortInfo bpi = new BasePortInfo();
			bpi.setIfindex(ifindex);
			bpi.setIfdescr(ifdescr);
			bpi.setIfname(ifname);
			bpi.setIfportip(ifportip);
			bpi.setIfnamedefined(ifnamedefined);
			bpi.setIf_real_speed(if_real_speed);

			str_getway = (String) result.get("getway");
			int getway = Integer.parseInt(str_getway);

			switch (getway) {
			case 1: {
				condition = condition + "(getway=1 and port_info='" + ifindex
						+ "') or ";
				key = device_id + "/" + ifindex + "/" + getway;
				break;
			}
			case 2: {
				condition = condition + "(getway=2 and port_info='" + ifdescr
						+ "') or ";
				key = device_id + "/" + ifdescr + "/" + getway;
				break;
			}
			case 3: {
				condition = condition + "(getway=3 and port_info='" + ifname
						+ "') or ";
				key = device_id + "/" + ifname + "/" + getway;
				break;
			}
			case 4: {
				condition = condition + "(getway=4 and port_info='"
						+ ifnamedefined + "') or ";
				key = device_id + "/" + ifnamedefined + "/" + getway;
				break;
			}
			case 5: {
				condition = condition + "(getway=5 and port_info='" + ifportip
						+ "') or ";
				key = device_id + "/" + ifportip + "/" + getway;
				break;
			}
			}
			map_realSpeed.put(key, bpi);
			result = cursor_deviceport.getNext();
		}
		condition = condition.substring(0, condition.length() - 3);
		pSQL.setSQL(strSQL3);
		pSQL.setStringExt(1, tablName, false);
		pSQL.setString(2, device_id);
		pSQL.setStringExt(3, condition, false);
		pSQL.setStringExt(4, startTime, false);
		pSQL.setStringExt(5, endTime, false);
		cursor_fluxReport = DataSetBean.getCursor(pSQL.getSQL());
		logger.debug("报表信息：" + pSQL.getSQL());
		return cursor_fluxReport;
	}
	public String getFluxDataForTable(Cursor cursor,double unit){
		String type = request.getParameter("type");//类型０：明细报表１：日报表２：周报表３：月报表４：年报表
		String format=getDateFormat(Integer.parseInt(type));//时间格式
		String[] searchKind = request.getParameterValues("kind");
		String titleCoulumn=getTitleColumn(searchKind);
		StringBuffer bf=new StringBuffer();//返回的数据
		bf.append("<tr>")
		  .append("<td class=\"green_title\">索引</td>")
		  .append("<td class=\"green_title\">描述</td>")
		  .append("<td class=\"green_title\">端口名称</td>")
		  .append("<td class=\"green_title\">IP地址</td>")
		  .append("<td class=\"green_title\">时间点</td>")
		  .append(titleCoulumn)
		  .append("</tr>");
         bf.append(getData(cursor,Integer.parseInt(type),format,searchKind,unit));    
		return bf.toString();
	}
	/**
	 * 组装数据
	 * @param cursor:数据集
	 * @param type:报表类型:0:明细报表1:日报表2:周报表3:月报表4:年报表
	 * @param format：根据报表类型，使用不同的时间格式
	 * @param searchKind：选择的时间类型
	 * @param unit:单位
	 * @return
	 */
	private String getData(Cursor cursor,int type,String format,String[] searchKind,double unit){
		StringBuffer bf=new StringBuffer();
		String device_id = null;
        String port_info = null;
        String getway = null;
        String ifindex = null;
        String ifdescr = null;
        String ifname = null;
        String ifportip = null;
        String if_real_speed = null;
        String key = null;
        String value = null;
        String time = null;
        Map result=cursor.getNext();
        BasePortInfo bpi = null;
        int n=0;
        while (result != null) {
            device_id = (String)result.get("device_id");
            port_info = (String)result.get("port_info");
            getway = (String)result.get("getway");
            key = device_id + getway + port_info;
            bpi = (BasePortInfo)map_baseFluxInfo.get(key);
            ifindex = bpi.getIfindex();
            ifdescr = bpi.getIfdescr();
            ifname = bpi.getIfname();
            ifportip = bpi.getIfportip();
            if_real_speed = bpi.getIf_real_speed();
            if (ifportip.equals("null")) {
                ifportip = "";
            }
            bf.append("<tr>")
              .append("<td class=\"column\">").append(ifindex).append("</td>")
              .append("<td class=\"column\">").append(ifdescr).append("</td>")
              .append("<td class=\"column\">").append(ifname).append("</td>")
              .append("<td class=\"column\">").append(ifportip).append("</td>");
            switch (type) {
                case 0:{
                    bf.append("<td class=\"column\">" + StringUtils.formatDate(format,Long.parseLong((String)result.get("collecttime"))) + "</td>");
                    break;
                }
                case 1: {
                	bf.append("<td class=\"column\">" + StringUtils.formatDate(format,Long.parseLong((String)result.get("collecttime"))) + "点</td>");
                    break;
                }
                case 2: {
                	bf.append("<td class=\"column\">" + StringUtils.formatDate(format,Long.parseLong((String)result.get("collecttime"))) + "号</td>");
                    break;
                }
                case 3: {
                	bf.append("<td class=\"column\">" + StringUtils.formatDate(format,Long.parseLong((String)result.get("collecttime"))) + "号</td>");
                    break;
                }
                case 4: {
                	bf.append("<td class=\"column\">" + StringUtils.formatDate(format,Long.parseLong((String)result.get("collecttime"))) + "月</td>");
                    break;
                }
            }
            n=searchKind.length;
            if (Double.parseDouble(if_real_speed) <= 100000000) {   
                for (int i=0; i<n; i++) {
                    value = (String)result.get(searchKind[i]);
                    bf.append("<td class=\"column\">").append(StringUtils.formatNumber(Double.parseDouble(value)/unit, 3)).append("M</td>");
                }
            }
            else {
                for (int i=0; i<n; i++) {
                    value = (String)result.get(searchKind[i]);
                    bf.append("<td class=\"column\">").append(StringUtils.formatNumber(Double.parseDouble(value)/(unit*unit*unit), 3)).append("M</td>");
                }
            }
            bf.append("</tr>");
            result = cursor.getNext();
        }
        cursor=null;
        result=null;
		return bf.toString();
	}
	/**
	 * 根据选择的流量类型封装所需列出的标题
	 * @param Searchkind
	 * @return
	 */
	private String getTitleColumn(String[] Searchkind){
		StringBuffer bf=new StringBuffer();
		int n=Searchkind.length;
		String data="";
		for(int i=0;i<n;i++){
			data=Searchkind[i];
			if (data.equals("ifinoctetsbps")) {
                bf.append("<td class=\"green_title\">流入速率(bps)</td>");
            }
            else if (data.equals("ifindiscardspps")) {
                bf.append("<td class=\"green_title\">流入丢包率(bps)</td>");
            }
            else if (data.equals("ifinerrors")) {
                bf.append("<td class=\"green_title\">流入错误包数(bps)</td>");
            }
            else if (data.equals("ifinoctetsbpsmax")) {
                bf.append("<td class=\"green_title\">流入峰值(bps)</td>");
            }
            else if (data.equals("ifinucastpktspps")) {
                bf.append("<td class=\"green_title\">每秒流入单播包数(bps)</td>");
            }
            else if (data.equals("ifoutoctetsbps")) {
                bf.append("<td class=\"green_title\">流出速率(bps)</td>");
            }
            else if (data.equals("ifoutdiscardspps")) {
                bf.append("<td class=\"green_title\">流出丢包率(bps)</td>");
            }
            else if (data.equals("ifouterrors")) {
                bf.append("<td class=\"green_title\">流出错误包数(bps)</td>");
            }
            else if (data.equals("ifoutoctetsbpsmax")) {
                bf.append("<td class=\"green_title\">流出峰值(bps)</td>");
            }
            else if (data.equals("ifinnucastpktspps")) {
                bf.append("<td class=\"green_title\">每秒流入非单播包数(bps)</td>");
            }
            else if (data.equals("ifinerrorspps")) {
                bf.append("<td class=\"green_title\">流入错包率(bps)</td>");
            }
            else if (data.equals("ifinoctets")) {
                bf.append("<td class=\"green_title\">流入字节数(bps)</td>");
            }
            else if (data.equals("ifindiscards")) {
                bf.append("<td class=\"green_title\">流入丢弃包数(bps)</td>");
            }
            else if (data.equals("ifoutucastpktspps")) {
                bf.append("<td class=\"green_title\">每秒流出单播包数(bps)</td>");
            }
            else if (data.equals("ifouterrorspps")) {
                bf.append("<td class=\"green_title\">流出错包率(bps)</td>");
            }
            else if (data.equals("ifoutoctets")) {
                bf.append("<td class=\"green_title\">流出字节数(bps)</td>");
            }
            else if (data.equals("ifoutdiscards")) {
                bf.append("<td class=\"green_title\">流出丢弃包数(bps)</td>");
            }
            else if (data.equals("ifoutnucastpktspps")) {
                bf.append("<td class=\"green_title\">每秒流出非单播包数(bps)</td>");
            }
            else if (data.equals("ifinunknownprotospps")) {
                bf.append("<td class=\"green_title\">每秒流入未知协议包数(bps)</td>");
            }
            else if (data.equals("ifoutqlenpps")) {
                bf.append("<td class=\"green_title\">每秒流出队列大小(bps)</td>");
            }
		}
		return bf.toString();
	}
	/**
	 * 根据报表类型，使用不同的时间格式
	 * @param type:类型０：明细报表１：日报表２：周报表３：月报表４：年报表
	 * @return:时间格式
	 */
	private String getDateFormat(int type){
		String format = "";
		switch(type){
			case 0:{
				format="yyyy-MM-dd HH:mm:ss";
				break;
			}case 1:{
				format="HH:mm";
				break;
			}case 2:{
				format="dd";
				break;
			}case 3:{
				format="dd";
				break;
			}case 4:{
				format="MM";
				break;
			}
		}
		return format;
	}
/**
 *根据getFluxDataText()方法增加从原始表中读取数据
 * REQ:GZDX-REQ-20080325-ZYX-001
 * 	  :拓扑图上右键快捷菜单中的设备信息->设备流量的界面中的“历史报表显示”中报表类型选择一个“明细报表”,时间上需要具体到小时进行选择,查询原始数据表
 * @return
 */
	public Cursor getFluxDataCursor(){
		String[] fluxType = request.getParameterValues("kind");
		StringBuffer b_fluxType = new StringBuffer();
		for (int i = 0; i < fluxType.length; i++) {
			b_fluxType.append(fluxType[i] + ",");
		}
		String typeSearch = request.getParameter("type");//类型：０：明细报表１：日报表２：周报表３：月报表４：年报表
		String[] devPort = request.getParameterValues("port");
		StringBuffer b_condition = new StringBuffer();
		String device_id = null;
		String getway = null;
		String port_info = null;
		String ifindex = null;
		String ifdescr = null;
		String ifname = null;
		String ifportip = null;
		String if_real_speed = null;
		String str_portValue = null;
		String port_value[] = null;
		BasePortInfo bpi = null;
		String key = null;
		for (int i = 0; i < devPort.length; i++) {
			str_portValue = devPort[i];
			port_value = str_portValue.split("##");
			device_id = port_value[0];
			getway = port_value[1];
			port_info = port_value[2];
			ifindex = port_value[3];
			ifdescr = port_value[4];
			ifname = port_value[5];
			ifportip = port_value[6];
			if_real_speed = port_value[7];
			bpi = new BasePortInfo();
			bpi.setIfindex(ifindex);
			bpi.setIfdescr(ifdescr);
			bpi.setIfname(ifname);
			bpi.setIfportip(ifportip);
			bpi.setIf_real_speed(if_real_speed);
			key = device_id + getway + port_info;
			map_baseFluxInfo.put(key, bpi);
			b_condition.append("(getway=" + getway + " and port_info='"
					+ port_info + "') or ");
		}
		String tab_name;
		DateTimeUtil dt;
		String sql="";
		String start,end;
		String day=request.getParameter("day");//日周月时得到的数据
		String sel="select distinct(port_info),device_id,getway,collecttime,"+b_fluxType.substring(0, b_fluxType.length() - 1)+" from ";
		String whe=" where device_id='"+device_id+"' and ("+b_condition.substring(0, b_condition.length() - 3)+")";
		String order=" order by getway,port_info";
		switch(Integer.parseInt(typeSearch)){
			case 0:{//明细报表
				start=request.getParameter("starttime");
				end=request.getParameter("endtime");
				sql=CommonUtil.getSQLByDay(start,end, 0, 0,sel,whe,order, "collecttime","flux_data_");
				break;
			}case 1:{//日报表
				sql=CommonUtil.getSQLByDay(day+" 00:00:00",day+" 23:59:59", 0, 0,sel,whe,order, "collecttime","flux_hour_stat_");
				break;
			}case 2:{//周报表
				dt=new DateTimeUtil(day);
				sql=CommonUtil.getSQLByMonth(day,dt.getNextDate("day",7), 0, 0, sel, whe, order, "collecttime", "flux_day_stat_");
				break;
			}case 3:{//月报表2008-03
				dt=new DateTimeUtil(day+"-01");
				sql=CommonUtil.getSQLByMonth(dt.getDate(),dt.getLastDayOfMonth(), 0, 0, sel, whe, order, "collecttime", "flux_day_stat_");
				break;
			}case 4:{//年报表flux_year_stat
				dt=new DateTimeUtil(day+"-01-01");
				sql=sel +"flux_year_stat"+whe+" and collecttime>="+dt.getLongTime()+" and collecttime<="+(new DateTimeUtil(dt.getNextYear(1)+"-01-01").getLongTime());
				break;
			}
		}
		logger.debug("getFluxDataCursor:"+sql);
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return DataSetBean.getCursor(sql);
	}
/**
 * 2008-4-22
 * 严伟明修改了 tableName 的赋值  以及在不同的统计报表中日期的初始化问题
 * @return
 */
	public Cursor getFluxDataText() {
		logger.debug("Action******getFluxDataText******");
		String[] fluxType = request.getParameterValues("kind");
		StringBuffer b_fluxType = new StringBuffer();

		for (int i = 0; i < fluxType.length; i++) {
			b_fluxType.append(fluxType[i] + ",");
		}
		
		String typeSearch = request.getParameter("type");
		String searchDay = request.getParameter("day");
		// logger.debug("searchDay---->" + searchDay);
		int tmp1 = 0;
		String[] tmpDate = null;
		String combineDate = "";
		if (!searchDay.equals("4")) {
			tmpDate = searchDay.split("-");
		}

		 logger.debug("tmpDate.length---->" + tmpDate.length);
		long startSecond = 0;
		// logger.debug("startSecond---->" + startSecond);
		String tableName = null;
		long lStart = 0;
		long lEnd = 0;

		switch (Integer.parseInt(typeSearch)) {
		case 1: {
			logger.debug("日统计");
			// Date dt = new Date();
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
			//				
			// try {
			// dt = sdf.parse(searchDay);
			// }
			// catch (ParseException e) {
			// logger.debug(e);
			// }
			//				
			// lStart = dt.getTime()/1000;
			// lEnd = lStart + 86400;
			// sdf = new SimpleDateFormat("yyyy_M_d");
			// String time = sdf.format(dt);
			// tableName = "flux_hour_stat_" + time;
			// break;

			tmp1 = Integer.parseInt(tmpDate[2]);
			combineDate = tmpDate[0] + "-" + tmpDate[1] + "-"
					+ String.valueOf(tmp1);
			startSecond = UserManagerDev.DateToSecond(combineDate);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");//yyyy_M_d

			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = dt.getTime() / 1000;
			lEnd = lStart + 86400;
			sdf = new SimpleDateFormat("yyyy_M");//yyyy_M_d
			String time = sdf.format(dt);
			//tableName = "flux_hour_stat_" + time;
			tableName = "flux_day_stat_" + time;
			break;
		}
		case 2: {
			logger.debug("周统计");
			tmp1 = Integer.parseInt(tmpDate[2]);
			combineDate = tmpDate[0] + "-" + tmpDate[1] + "-"
					+ String.valueOf(tmp1);
			startSecond = UserManagerDev.DateToSecond(combineDate);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");//yyyy_M

			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = startSecond;
			lEnd = lStart + 86400 * 7;
			sdf = new SimpleDateFormat("yyyy");//yyyy_M
			String time = sdf.format(dt);
			//tableName = "flux_day_stat_" + time;
			tableName = "flux_week_stat_" + time;
			break;
		}
		case 3: {
			logger.debug("月统计");
			// String lastMonth = String.valueOf(Integer.parseInt(tmpDate[1]));
			combineDate = tmpDate[0] + "-" + tmpDate[1] + "-" + "1";
			// logger.debug("combineDate---->"+combineDate);
			startSecond = UserManagerDev.DateToSecond(combineDate);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");//yyyy_M
			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = startSecond;
			lEnd = lStart + 86400 * 31;
			sdf = new SimpleDateFormat("yyyy");//yyyy_M
			String time = sdf.format(dt);
			//tableName = "flux_day_stat_" + time;
			tableName = "flux_month_stat_" + time;
			break;
		}
		case 4: {
			logger.debug("年统计");
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = dt.getTime() / 1000;
			lEnd = lStart + 86400 * 365;

			String time = sdf.format(dt);
			tableName = "flux_year_stat";
			break;
		}
		}

		String[] devPort = request.getParameterValues("port");
		StringBuffer b_condition = new StringBuffer();

		String device_id = null;
		String getway = null;
		String port_info = null;
		String ifindex = null;
		String ifdescr = null;
		String ifname = null;
		String ifportip = null;
		String if_real_speed = null;
		String str_portValue = null;
		String port_value[] = null;
		BasePortInfo bpi = null;
		String key = null;

		for (int i = 0; i < devPort.length; i++) {
			str_portValue = devPort[i];
			port_value = str_portValue.split("##");
			device_id = port_value[0];
			getway = port_value[1];
			port_info = port_value[2];
			ifindex = port_value[3];
			ifdescr = port_value[4];
			ifname = port_value[5];
			ifportip = port_value[6];
			if_real_speed = port_value[7];
			bpi = new BasePortInfo();
			bpi.setIfindex(ifindex);
			bpi.setIfdescr(ifdescr);
			bpi.setIfname(ifname);
			bpi.setIfportip(ifportip);
			bpi.setIf_real_speed(if_real_speed);
			key = device_id + getway + port_info;
			map_baseFluxInfo.put(key, bpi);
			b_condition.append("(getway=" + getway + " and port_info='"
					+ port_info + "') or ");
		}
		// logger.debug("1---->"
		// + b_fluxType.substring(0, b_fluxType.length() - 1));
		// logger.debug("2---->" + tableName);
		// logger.debug("3---->" + device_id);
		// logger.debug("4---->"
		// + b_condition.substring(0, b_condition.length() - 3));
		// logger.debug("5---->" + lStart);
		// logger.debug("6---->" + lEnd);
		pSQL.setSQL(strSQL4);
		pSQL.setStringExt(1, b_fluxType.substring(0, b_fluxType.length() - 1),
				false);
		pSQL.setStringExt(2, tableName, false);
		pSQL.setString(3, device_id);
		pSQL.setStringExt(4,
				b_condition.substring(0, b_condition.length() - 3), false);
		pSQL.setLong(5, lStart);
		pSQL.setLong(6, lEnd);
		logger.warn("WEBTOPO设备历史流量查询：" + pSQL.getSQL());
		
		return DataSetBean.getCursor(pSQL.getSQL());
	}

	/**
	 * 生成图形的数据
	 * XJDX-XJ-BUG-20080104-XXF-001  修改图像无法显示全部的问题
	 * 将为数据是0的项不在图上画出，这样可以减少多余项
	 * @return
	 */
	public Cursor getDrawFluxDataText() {
		logger.debug("Action******getDrawFluxDataText******");
		String[] fluxType = request.getParameterValues("kind");
		StringBuffer b_fluxType = new StringBuffer();

		for (int i = 0; i < fluxType.length; i++) {
			b_fluxType.append(fluxType[i] + ",");
		}

		String typeSearch = request.getParameter("type");
		String searchDay = request.getParameter("day");
		// logger.debug("searchDay---->" + searchDay);
		int tmp1 = 0;
		String[] tmpDate = null;
		String combineDate = "";
		if (!searchDay.equals("4")) {
			tmpDate = searchDay.split("-");
		}
		// logger.debug("tmpDate.length---->" + tmpDate.length);
		long startSecond = 0;

		String tableName = null;
		long lStart = 0;
		long lEnd = 0;

		switch (Integer.parseInt(typeSearch)) {
		case 1: {
			tmp1 = Integer.parseInt(tmpDate[2]);
			combineDate = tmpDate[0] + "-" + tmpDate[1] + "-"
					+ String.valueOf(tmp1);
			startSecond = UserManagerDev.DateToSecond(combineDate);
			// logger.debug("startSecond---->" + startSecond);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = dt.getTime() / 1000;
			lEnd = lStart + 86400;
			sdf = new SimpleDateFormat("yyyy_M_d");
			String time = sdf.format(dt);
			tableName = "flux_hour_stat_" + time;
			break;
		}
		case 2: {
			tmp1 = Integer.parseInt(tmpDate[2]);
			combineDate = tmpDate[0] + "-" + tmpDate[1] + "-"
					+ String.valueOf(tmp1);
			startSecond = UserManagerDev.DateToSecond(combineDate);
			// logger.debug("startSecond---->" + startSecond);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = startSecond;
			lEnd = lStart + 86400 * 7;
			sdf = new SimpleDateFormat("yyyy_M");
			String time = sdf.format(dt);
			tableName = "flux_day_stat_" + time;
			break;
		}
		case 3: {
			// String lastMonth =
			// String.valueOf(Integer.parseInt(tmpDate[1])-1);
			combineDate = tmpDate[0] + "-" + tmpDate[1] + "-" + "1";
			startSecond = UserManagerDev.DateToSecond(combineDate);
			// logger.warn("startSecond---->" + startSecond);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M");
			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = startSecond;
			lEnd = lStart + 86400 * 31;
			sdf = new SimpleDateFormat("yyyy_M");
			String time = sdf.format(dt);
			tableName = "flux_day_stat_" + time;
			break;
		}
		case 4: {

			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = dt.getTime() / 1000L;
			lEnd = lStart + 0x1e13380L;
			String time = sdf.format(dt);
			tableName = "flux_month_stat_" + time;
			break;
		}
		}

		String[] devPort = request.getParameterValues("port");
		StringBuffer b_condition = new StringBuffer();

		String device_id = null;
		String getway = null;
		String port_info = null;
		String ifindex = null;
		String ifdescr = null;
		String ifname = null;
		String ifportip = null;
		String if_real_speed = null;
		String str_portValue = null;
		String port_value[] = null;
		BasePortInfo bpi = null;
		String key = null;

		for (int i = 0; i < devPort.length; i++) {
			str_portValue = devPort[i];
			port_value = str_portValue.split("##");
			device_id = port_value[0];
			getway = port_value[1];
			port_info = port_value[2];
			ifindex = port_value[3];
			ifdescr = port_value[4];
			ifname = port_value[5];
			ifportip = port_value[6];
			if_real_speed = port_value[7];
			bpi = new BasePortInfo();
			bpi.setIfindex(ifindex);
			bpi.setIfdescr(ifdescr);
			bpi.setIfname(ifname);
			bpi.setIfportip(ifportip);
			bpi.setIf_real_speed(if_real_speed);
			key = device_id + getway + port_info;
			map_baseFluxInfo.put(key, bpi);
			b_condition.append("(getway=" + getway + " and port_info='"
					+ port_info + "') or ");
		}
		pSQL.setSQL(strSQL4);
		pSQL.setStringExt(1, b_fluxType.substring(0, b_fluxType.length() - 1),
				false);
		pSQL.setStringExt(2, tableName, false);
		pSQL.setString(3, device_id);
		pSQL.setStringExt(4,
				b_condition.substring(0, b_condition.length() - 3), false);
		pSQL.setLong(5, lStart);
		pSQL.setLong(6, lEnd);
		logger.debug("WEBTOPO设备历史流量查询：" + pSQL.getSQL());

		return DataSetBean.getCursor(pSQL.getSQL());
	}

	public HashMap getMap_realSpeed() {
		return map_realSpeed;
	}

	public HashMap getMap_baseFluxInfo() {
		return map_baseFluxInfo;
	}

	public ArrayList getGrapCursor(Cursor cursors, HashMap map_baseFluxInfo) {
		cursors.Reset();
		HttpSession session = request.getSession();
		FluxUnit fu = FluxUnit.getFluxUnit(session);
		double unit = fu.getFluxBase();
		String type = request.getParameter("type");
		Map result = cursors.getNext();
		BasePortInfo bpi = null;
		String ifname = null;
		String device_id = null;
		String port_info = null;
		String getway = null;
		String collecttime = null;
		String value = null;
		String key = null;
		String temp_key = null;

		ArrayList obj = new ArrayList();
		ArrayList list_cursor = new ArrayList();
		ArrayList list_rows = new ArrayList();
		Cursor[] cursors_Grap = null;
		String[] rowsKey = null;

		Cursor cursor_Grap = new Cursor();
		HashMap map_grap = null;

		int i = 0;

		while (result != null) {
			i++;
			map_grap = new HashMap();
			device_id = (String) result.get("device_id");
			port_info = (String) result.get("port_info");
			getway = (String) result.get("getway");
			collecttime = (String) result.get("collecttime");
			value = (String) result.get(type);
			key = device_id + getway + port_info;
			
//			if(!value.equals("0") || Double.parseDouble(value)!=0){
				//key = device_id + getway + port_info;
				//logger.debug("getGrapCursor_value------------------->"+value.toString());
//			if (i != 1) {
				//logger.debug("getGrapCursor_key------------------->"+key);
				//logger.debug("getGrapCursor_temp_key------------------->"+temp_key);
				if (!key.equals(temp_key) && null != temp_key) {
					list_cursor.add(cursor_Grap);
					cursor_Grap = new Cursor();
					//bpi = (BasePortInfo) map_baseFluxInfo.get(temp_key);
					bpi = (BasePortInfo) map_baseFluxInfo.get(temp_key);
					ifname = bpi.getIfname();
					list_rows.add(ifname);
				}
//			}

			value = String.valueOf(Double.parseDouble(value) / (unit));
			map_grap.put("value", value);
			map_grap.put("collecttime", collecttime);
			cursor_Grap.add(map_grap);
//			}
			temp_key = key;
			//logger.debug("temp_key------------>"+temp_key);
			result = cursors.getNext();

			
		}

		if (i > 0) {
			list_cursor.add(cursor_Grap);
			bpi = (BasePortInfo) map_baseFluxInfo.get(temp_key);
			ifname = bpi.getIfname();
			list_rows.add(ifname);
		}

		cursors_Grap = new Cursor[list_cursor.size()];

		for (int x = 0; x < list_cursor.size(); x++) {
			cursors_Grap[x] = (Cursor) list_cursor.get(x);
		}
		rowsKey = new String[list_rows.size()];

		for (int y = 0; y < list_rows.size(); y++) {
			rowsKey[y] = (String) list_rows.get(y);
		}
		
		obj.add(cursors_Grap);
		obj.add(rowsKey);
		return obj;
	}

	/**
	 * 获取平均值
	 * 
	 * @return
	 */
	public Cursor getAvgFluxDataText() {
		logger.debug("Action******getAvgFluxDataText******");
		String[] fluxType = request.getParameterValues("kind");
		StringBuffer b_fluxType = new StringBuffer();

		for (int i = 0; i < fluxType.length; i++) {
			b_fluxType.append(fluxType[i] + ",");
		}
		String typeSearch = request.getParameter("type");
		String searchDay = request.getParameter("day");
		// logger.debug("searchDay---->" + searchDay);
		int tmp1 = 0;
		String[] tmpDate = null;
		String combineDate = "";
		if (!searchDay.equals("4")) {
			tmpDate = searchDay.split("-");
		}
		// logger.debug("tmpDate.length---->" + tmpDate.length);
		long startSecond = 0;
		String tableName = null;
		long lStart = 0;
		long lEnd = 0;

		switch (Integer.parseInt(typeSearch)) {
		case 1: {
			logger.debug("日统计");

			tmp1 = Integer.parseInt(tmpDate[2]);
			combineDate = tmpDate[0] + "-" + tmpDate[1] + "-"
					+ String.valueOf(tmp1);
			startSecond = UserManagerDev.DateToSecond(combineDate);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");

			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = startSecond;
			lEnd = lStart + 86400;
			sdf = new SimpleDateFormat("yyyy_M_d");
			String time = sdf.format(dt);
			tableName = "flux_hour_stat_" + time;
			break;
		}
		case 2: {
			logger.debug("周统计");
			tmp1 = Integer.parseInt(tmpDate[2]);
			combineDate = tmpDate[0] + "-" + tmpDate[1] + "-"
					+ String.valueOf(tmp1);
			startSecond = UserManagerDev.DateToSecond(combineDate);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M");

			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = startSecond;
			lEnd = lStart + 86400 * 7;
			sdf = new SimpleDateFormat("yyyy_M");
			String time = sdf.format(dt);
			tableName = "flux_day_stat_" + time;
			break;
		}
		case 3: {
			logger.debug("月统计");
			// String lastMonth = String.valueOf(Integer.parseInt(tmpDate[1]));
			combineDate = tmpDate[0] + "-" + tmpDate[1] + "-" + "1";
			// logger.debug("avgcombineDate---->"+combineDate);
			startSecond = UserManagerDev.DateToSecond(combineDate);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M");
			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = startSecond;
			lEnd = lStart + 86400 * 31;
			sdf = new SimpleDateFormat("yyyy_M");
			String time = sdf.format(dt);
			tableName = "flux_day_stat_" + time;

			break;
		}
		case 4: {
			logger.debug("年统计");
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = dt.getTime() / 1000;
			lEnd = lStart + 86400 * 365;

			String time = sdf.format(dt);
			tableName = "flux_year_stat";
			break;
		}
		}

		String[] devPort = request.getParameterValues("port");
		StringBuffer b_condition = new StringBuffer();

		String device_id = null;
		String getway = null;
		String port_info = null;
		String ifindex = null;
		String ifdescr = null;
		String ifname = null;
		String ifportip = null;
		String if_real_speed = null;
		String str_portValue = null;
		String port_value[] = null;
		BasePortInfo bpi = null;
		String key = null;

		for (int i = 0; i < devPort.length; i++) {
			str_portValue = devPort[i];
			port_value = str_portValue.split("##");
			device_id = port_value[0];
			getway = port_value[1];
			port_info = port_value[2];
			ifindex = port_value[3];
			ifdescr = port_value[4];
			ifname = port_value[5];
			ifportip = port_value[6];
			if_real_speed = port_value[7];
			bpi = new BasePortInfo();
			bpi.setIfindex(ifindex);
			bpi.setIfdescr(ifdescr);
			bpi.setIfname(ifname);
			bpi.setIfportip(ifportip);
			bpi.setIf_real_speed(if_real_speed);
			key = device_id + getway + port_info;
			map_baseFluxInfo.put(key, bpi);
			b_condition.append("(getway=" + getway + " and port_info='"
					+ port_info + "') or ");
		}
		// logger.debug("1---->"
		// + b_fluxType.substring(0, b_fluxType.length() - 1));logger.debugprintln("2---->" + tableName);
		// logger.debug("3---->" + device_id);
		// logger.debug("4---->"
		// + b_condition.substring(0, b_condition.length() - 3));
		// logger.debug("5---->" + lStart);
		// logger.debug("6---->" + lEnd);
		String avgSQL = "";
		String reColum = "";
		String columTmp1 = b_fluxType.substring(0, b_fluxType.length() - 1);
		// logger.debug("columTmp1---->"+columTmp1);
		String[] colum = columTmp1.split(",");
		for (int j = 0; j < colum.length; j++) {
			reColum += "avg(" + colum[j] + ") as " + colum[j] + ",";
		}
		avgSQL += "select distinct(port_info),device_id,getway,";
		avgSQL += reColum;
		avgSQL += "0 from " + tableName;
		avgSQL += " where device_id='" + device_id + "'";
		avgSQL += " and(" + b_condition.substring(0, b_condition.length() - 3)
				+ ")";
		avgSQL += " and collecttime>=" + lStart + " and collecttime<" + lEnd
				+ "";
		avgSQL += " group by port_info,device_id,getway order by port_info,device_id,getway";
		logger.debug("WEBTOPO设备历史流量平均值查询：" + avgSQL);
		PrepareSQL psql = new PrepareSQL(avgSQL);
    	psql.getSQL();
		return DataSetBean.getCursor(avgSQL);
	}

	/**
	 * 获取最大值
	 * 
	 * @return
	 */
	public Cursor getMaxFluxDataText() {
		logger.debug("Action******getMaxFluxDataText******");
		String[] fluxType = request.getParameterValues("kind");
		StringBuffer b_fluxType = new StringBuffer();

		for (int i = 0; i < fluxType.length; i++) {
			b_fluxType.append(fluxType[i] + ",");
		}

		String typeSearch = request.getParameter("type");
		String searchDay = request.getParameter("day");
		// logger.debug("searchDay---->" + searchDay);
		int tmp1 = 0;
		String[] tmpDate = null;
		String combineDate = "";
		if (!searchDay.equals("4")) {
			tmpDate = searchDay.split("-");
		}
		// logger.debug("tmpDate.length---->" + tmpDate.length);
		long startSecond = 0;
		String tableName = null;
		long lStart = 0;
		long lEnd = 0;

		switch (Integer.parseInt(typeSearch)) {
		case 1: {
			logger.debug("日统计");
			tmp1 = Integer.parseInt(tmpDate[2]);
			combineDate = tmpDate[0] + "-" + tmpDate[1] + "-"
					+ String.valueOf(tmp1);
			startSecond = UserManagerDev.DateToSecond(combineDate);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");

			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = startSecond;
			lEnd = lStart + 86400;
			sdf = new SimpleDateFormat("yyyy_M_d");
			String time = sdf.format(dt);
			tableName = "flux_hour_stat_" + time;
			break;
		}
		case 2: {
			logger.debug("周统计");
			tmp1 = Integer.parseInt(tmpDate[2]);
			combineDate = tmpDate[0] + "-" + tmpDate[1] + "-"
					+ String.valueOf(tmp1);
			startSecond = UserManagerDev.DateToSecond(combineDate);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M");
			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = startSecond;
			lEnd = lStart + 86400 * 7;
			sdf = new SimpleDateFormat("yyyy_M");
			String time = sdf.format(dt);
			tableName = "flux_day_stat_" + time;
			break;
		}
		case 3: {
			logger.debug("月统计");
			// String lastMonth = String.valueOf(Integer.parseInt(tmpDate[1]));
			combineDate = tmpDate[0] + "-" + tmpDate[1] + "-" + "1";
			startSecond = UserManagerDev.DateToSecond(combineDate);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M");
			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = startSecond;
			lEnd = lStart + 86400 * 31;
			sdf = new SimpleDateFormat("yyyy_M");
			String time = sdf.format(dt);
			tableName = "flux_day_stat_" + time;

			break;
		}
		case 4: {
			logger.debug("年统计");
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			try {
				dt = sdf.parse(searchDay);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
			lStart = dt.getTime() / 1000;
			lEnd = lStart + 86400 * 365;

			String time = sdf.format(dt);
			tableName = "flux_year_stat";
			break;
		}
		}

		String[] devPort = request.getParameterValues("port");
		StringBuffer b_condition = new StringBuffer();

		String device_id = null;
		String getway = null;
		String port_info = null;
		String ifindex = null;
		String ifdescr = null;
		String ifname = null;
		String ifportip = null;
		String if_real_speed = null;
		String ifnamedefined = null;
		String str_portValue = null;
		String port_value[] = null;
		BasePortInfo bpi = null;
		String key = null;

		for (int i = 0; i < devPort.length; i++) {
			str_portValue = devPort[i];
			port_value = str_portValue.split("##");
			device_id = port_value[0];
			getway = port_value[1];
			port_info = port_value[2];
			ifindex = port_value[3];
			ifdescr = port_value[4];
			ifname = port_value[5];
			ifportip = port_value[6];
			if_real_speed = port_value[7];
			ifnamedefined = port_value[8];
			bpi = new BasePortInfo();
			bpi.setIfindex(ifindex);
			bpi.setIfdescr(ifdescr);
			bpi.setIfname(ifname);
			bpi.setIfportip(ifportip);
			bpi.setIf_real_speed(if_real_speed);
			bpi.setIfnamedefined(ifnamedefined);
			key = device_id + getway + port_info;
			map_baseFluxInfo.put(key, bpi);
			b_condition.append("(getway=" + getway + " and port_info='"
					+ port_info + "') or ");
		}
		// logger.debug("1---->"
		// + b_fluxType.substring(0, b_fluxType.length() - 1));
		// logger.debug("2---->" + tableName);
		// logger.debug("3---->" + device_id);
		// logger.debug("4---->"
		// + b_condition.substring(0, b_condition.length() - 3));
		// logger.debug("5---->" + lStart);
		// logger.debug("6---->" + lEnd);
		String maxSQL = "";
		String reColum = "";
		String columTmp1 = b_fluxType.substring(0, b_fluxType.length() - 1);
		// logger.debug("columTmp1---->"+columTmp1);
		String[] colum = columTmp1.split(",");
		for (int j = 0; j < colum.length; j++) {
			reColum += "max(" + colum[j] + ") as " + colum[j] + ",";
		}
		maxSQL += "select distinct(port_info),device_id,getway,";
		maxSQL += reColum;
		maxSQL += "0 from " + tableName;
		maxSQL += " where device_id='" + device_id + "'";
		maxSQL += " and(" + b_condition.substring(0, b_condition.length() - 3)
				+ ")";
		maxSQL += " and collecttime>=" + lStart + " and collecttime<" + lEnd
				+ "";
		maxSQL += " group by port_info,device_id,getway order by port_info,device_id,getway";
		logger.debug("WEBTOPO设备历史流量最大值查询：" + maxSQL);
		PrepareSQL psql = new PrepareSQL(maxSQL);
    	psql.getSQL();
		return DataSetBean.getCursor(maxSQL);
	}

	public static long DateToSecond(String date) {
		long currSecond = 0;
		java.sql.Date start = java.sql.Date.valueOf(date);
		currSecond = start.getTime() / 1000;
		return currSecond;

	}
}
