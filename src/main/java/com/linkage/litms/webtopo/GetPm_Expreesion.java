package com.linkage.litms.webtopo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.webtopo.common.DeviceCommonOperation;

public class GetPm_Expreesion
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(GetPm_Expreesion.class);
//	private String	strSQL1			= "select oui vendor_id from gw_device_model where device_model_id=?";
	private String	strSQL1			= "select vendor_id from gw_device_model where device_model_id=?";

	private String	strSQL2			= "select expressionid,name from pm_expression where company=?";

	private String	strSQL3			= "select name from pm_expression where expressionid=?";

	private String	strSQL4			= "select indexid,descr from pm_map_instance where device_id=? and expressionid=?";

	/** *根据device_id expressionid得到id* */
	private String	strSQL5			= "select descr,id from pm_map_instance where device_id=? and expressionid=? and indexid in(?)";

	private String	dev_serial		= null;

	private String	strTime_start	= null;

	private String	strTime_end		= null;
	
	private String str_vendor_id    = null;

	private String str_device_id    = null;	
	
	Map map = new HashMap();
	/**
	 * 用于chart图标的标尺
	 */
	private long	marker			= 0L;

	public GetPm_Expreesion(HttpServletRequest request)
	{ 
		if(request.getParameter("dev_serial") == null)
		{
			String devID = request.getParameter("device_id");
			DeviceCommonOperation dco = new DeviceCommonOperation();
			dev_serial = dco.getSerialByDeviceid(devID);
		}else
		{
			dev_serial = request.getParameter("dev_serial");
		}
	}

	public Cursor getPm_exp()
	{
		if("".equals(dev_serial))
		{
			return new Cursor();
		}
		Map feilds = null;
		Cursor cursor1 = null;
		Cursor cursor2 = null;

		PrepareSQL pSQL1 = null;
		pSQL1 = new PrepareSQL(strSQL1);
		pSQL1.setStringExt(1,dev_serial,true);
		cursor1 = DataSetBean.getCursor(pSQL1.getSQL());

		feilds = cursor1.getNext();
		if(null==feilds)
		{
			return new Cursor();
		}
		String str_vendor_id = (String) feilds.get("vendor_id");
		feilds.clear();

		PrepareSQL pSQL2 = null;
		pSQL2 = new PrepareSQL(strSQL2);
		pSQL2.setStringExt(1,str_vendor_id,true);
		cursor2 = DataSetBean.getCursor(pSQL2.getSQL());

		return cursor2;
	
	}

	public String getpmName(String expressionid)
	{
		String expressionName = null;
		PrepareSQL pSQL = new PrepareSQL(strSQL3);
		pSQL.setStringExt(1,expressionid,false);
		HashMap hmap = DataSetBean.getRecord(pSQL.getSQL());
		expressionName = (String) hmap.get("name");

		return expressionName;
	}

	public Cursor getIndexInfo(String device_id,String expressionID)
	{
		Cursor cursor = null;
		PrepareSQL pSQL = new PrepareSQL(strSQL4);
		pSQL.setStringExt(1,device_id,true);
		pSQL.setStringExt(2,expressionID,false);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		return cursor;
	}

	/**
	 * @author hemc
	 * @param device_id
	 * @param expressionid
	 * @param indexids
	 * @return 从表Pm_Map_Instance中根据三个参数得到id,并返回.
	 */
	public List getIDFromPm_Map_Instance(String device_id,String expressionid,
			List indexids)
	{
		PrepareSQL pSQL = new PrepareSQL(strSQL5);
		List list_id = null;
		// 二维数组 索引indexid和唯一id
		String[] index_id = new String[2];
		pSQL.setStringExt(1,device_id,true);
		pSQL.setStringExt(2,expressionid,false);
		pSQL.setStringExt(3,StringUtils.weave(indexids),false);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		list_id = new ArrayList(cursor.getRecordSize());
		Map fields = cursor.getNext();
		while(fields != null)
		{
			index_id[0] = (String) fields.get("id");
			index_id[1] = (String) fields.get("descr");
			// 放入list中
			list_id.add(index_id.clone());
			fields = cursor.getNext();
		}
		pSQL = null;
		fields = null;
		cursor = null;
		return list_id;
	}

	/**
	 * 传入的listID 对应于getIDFromPm_Map_Instance函数返回的值
	 * 
	 * @author hemc
	 * @return 返回日报表的cursor
	 * @param listID
	 */
	public Cursor[] getCursorReport(List listID,DateTimeUtil dateTimeUtil)
	{
		int size = listID.size();
		Cursor[] cursor = new Cursor[size];
		String[] tmpIndex = null;
		rowKeys = new String[size];
		String tmpSQL = null;

		String strSQL = null;
		String tablenameA = null;
		String tablenameB = null;
		// 根据报表类型 时间向前推
		if(this.reportType == 1)
		{
			strTime_end = dateTimeUtil.getDate() + " " + dateTimeUtil.getTime();
			tablenameB = this.tableFirst + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth();
			dateTimeUtil.getNextDate(-1);
			tablenameA = this.tableFirst + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth();
			strTime_start = dateTimeUtil.getDate() + " "
					+ dateTimeUtil.getTime();
		}else if(this.reportType == 2)
		{
			DateTimeUtil tmpTimeUtil = new DateTimeUtil(dateTimeUtil
					.getLongTime()*1000);
			// 周报表暂时从原始数据表中取数据
			strTime_end = dateTimeUtil.getDate() + " " + dateTimeUtil.getTime();
			tablenameB = this.tableFirst + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth();
			dateTimeUtil.getNextDate(-7);
			tmpTimeUtil.getNextDate(-7);
			tablenameA = this.tableFirst + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth();
			strTime_start = dateTimeUtil.getDate() + " "
					+ dateTimeUtil.getTime();
			// 遍历时间 再检验某个日期是否为星期一
			for(int i = 1;i <= 8;i++)
			{
				// 是否是星期一
				if(tmpTimeUtil.getNoDayOfWeek("CN") == 1)
				{
					logger.debug(tmpTimeUtil.getDate() + "是星期一");
					marker = tmpTimeUtil.getLongTime();
				}
				// 向前加一天
				tmpTimeUtil.getNextDate(1);
			}
		}else
		{
			DateTimeUtil tmpTimeUtil = new DateTimeUtil(dateTimeUtil
					.getLongTime()*1000);
			// 月报表暂时从原始数据表中取数据
			strTime_end = dateTimeUtil.getDate() + " " + dateTimeUtil.getTime();
			long _end = dateTimeUtil.getLongTime();
			tablenameB = this.tableFirst + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth();
			dateTimeUtil.getNextMonth(-1);
			tmpTimeUtil.getNextMonth(-1);
			long _start = dateTimeUtil.getLongTime();
			tablenameA = this.tableFirst + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth();
			strTime_start = dateTimeUtil.getDate() + " "
					+ dateTimeUtil.getTime();
			long days = ( _end - _start ) / ( 24 * 3600 );
			days += 1;
			// 遍历时间 再检验某个日期是否为月初第一天
			for(int i = 1;i <= days;i++)
			{
				// 是否是是月初第一天
				if(tmpTimeUtil.getDay() == 1)
				{
					logger.debug(tmpTimeUtil.getDate() + "是月初第一天");
					marker = tmpTimeUtil.getLongTime();
				}
				// 向前加一天
				tmpTimeUtil.getNextDate(1);
			}
		}

		// 得到当前月份
		long start = dateTimeUtil.getLongTime();
		// 判断昨天和今天是否在同一个月内
		// 如果相等，则只需要查询一张表
		if(tablenameA.equals(tablenameB))
		{
			strSQL = "select ? ,gathertime from " + tablenameB
					+ " where gathertime >= " + start + " and id = ?";
		}else
		{
			strSQL = "select ? ,gathertime from " + tablenameA
					+ " where gathertime >= " + start + " and id = ?"
					+ " union " + "select ? ,gathertime from " + tablenameB
					+ " where id = ?";
		}
		PrepareSQL pSQL = new PrepareSQL(strSQL);
		for(int i = 0;i < size;i++)
		{
			// 从list中取出，并转为二维数组
			tmpIndex = (String[]) listID.get(i);
			pSQL.setStringExt(1,this.valueColumn,false);
			pSQL.setStringExt(2,tmpIndex[0],true);
			pSQL.setStringExt(3,this.valueColumn,false);
			pSQL.setStringExt(4,tmpIndex[0],true);
			tmpSQL = pSQL.getSQL();
			cursor[i] = DataSetBean.getCursor(tmpSQL);
			this.rowKeys[i] = tmpIndex[1];
		}
		pSQL = null;
		// dateTimeUtil = null;
		return cursor;
	}

	public String[] getRowKeys()
	{
		return this.rowKeys;
	}

	/**
	 * 
	 * @param type
	 *            报表类型 1：日 2：周 3:月
	 */
	public void setReportType(int type)
	{
		switch(type)
		{
			case 1:
				this.reportType = 1;
				this.tableFirst = "pm_raw_";
				break;
			case 2:
				this.reportType = 2;
				this.tableFirst = "pm_raw_";
				break;
			case 3:
				this.reportType = 3;
				this.tableFirst = "pm_hour_stats_";
				break;
		}
	}

	/**
	 * 设置字段名称 value、maxvalue,avgvalue,minvalue
	 * 
	 * @param value
	 */
	public void setValueColumn(String value)
	{
		this.valueColumn = value;
	}

	/**
	 * 报表类型 1：日 2：周 3:月
	 */
	private int			reportType	= 1;

	/**
	 * 设置表中字段
	 */
	private String		valueColumn	= "value";

	/**
	 * 用于构造表明
	 * 日报表为：pm_raw_year_month、周：pm_hour_stats_year_month、月:pm_day_stat_year
	 */
	private String		tableFirst	= "pm_raw_year_month_";

	/**
	 * @see com.linkage.litms.common.chart.TimeSeriesChart.java
	 */
	private String[]	rowKeys;

	public String getStrTime_end()
	{
		return strTime_end;
	}

	public String getStrTime_start()
	{
		return strTime_start;
	}

	public long getMarker()
	{
		return this.marker;
	}
}
