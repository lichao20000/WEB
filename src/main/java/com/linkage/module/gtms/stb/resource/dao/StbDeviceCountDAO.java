package com.linkage.module.gtms.stb.resource.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.dao.SuperDAO;

@SuppressWarnings("unchecked")
public class StbDeviceCountDAO extends SuperDAO
{
	/**获取所有地市id*/
	private static final String QUERYCITYSQL="select city_id from tab_city where parent_id='00' ";

	/**按属地分别统计所有绑定的设备量*/ // TODO wait (more table related)
	private static final String QUERYALLNUMSQL="select a.city_id,count(a.device_id) as all_num "
			+ "from stb_tab_gw_device a,stb_tab_customer b,stb_tab_devicetype_info c "
			+ "where a.customer_id=b.customer_id and a.devicetype_id=c.devicetype_id and c.category is not null group by a.city_id ";

	/**按属地、探针版本分别统计所有绑定的、4k设备量*/ // TODO wait (more table related)
	private static final String QUERY4K_PROBENUMSQL="select a.city_id,b.is_probe,"
			+ "count(a.device_id) as four_k_num "
			+ "from stb_tab_gw_device a,stb_tab_devicetype_info b,stb_tab_customer c "
			+ "where a.devicetype_id=b.devicetype_id and a.customer_id=c.customer_id and b.category=1 "
			+ "group by a.city_id,b.is_probe ";

	/**按属地、厂商统计全省非4K机顶盒分布报表*/ // TODO wait (more table related)
	private static final String QUERYN4KNUMSQL="select a.city_id,"
			+ "sum(case when a.vendor_id='2' then 1 else 0 end) as hw_num,"
			+"sum(case when a.vendor_id='11' then 1 else 0 end) as cw_num,"
			+"sum(case when a.vendor_id='10' then 1 else 0 end) as zx_num,"
			+"sum(case when a.vendor_id='20' then 1 else 0 end) as fh_num,"
			+"sum(case when a.vendor_id='15' then 1 else 0 end) as ch_num,"
			+"sum(case when a.vendor_id='17' then 1 else 0 end) as bst_num,"
			+"sum(case when a.vendor_id='29' then 1 else 0 end) as js_num,"
			+"sum(case when a.vendor_id not in('2','10','11','15','17','20','29') then 1 else 0 end) as other_num "
			+"from stb_tab_gw_device a,stb_tab_devicetype_info b,stb_tab_customer c "
			+"where a.devicetype_id=b.devicetype_id and a.customer_id=c.customer_id and b.category!=1 "
			+"group by a.city_id";

	/**按厂商统计全省4K机顶盒、探针部署分布报表*/ // TODO wait (more table related)
	private static final String QUERY4KNUMSQL="select a.city_id,a.vendor_id,"
			+ "sum(case when b.category=1 then 1 else 0 end) as four_k_num,"
			+ "sum(case when b.category=1 and b.is_probe=1 then 1 else 0 end) as y_probe_num "
			+ "from stb_tab_gw_device a,stb_tab_devicetype_info b,stb_tab_customer c "
			+ "where a.devicetype_id=b.devicetype_id and a.customer_id=c.customer_id "
			+ "group by a.city_id,a.vendor_id";




	/**
	 * 获取所有地市id
	 */
	public List<String> queryCityIdList()
	{
		List<Map<String,String>> lists=jt.queryForList(new PrepareSQL(QUERYCITYSQL).getSQL());
		List<String> list=new ArrayList<String>();
		for(Map<String,String> map:lists){
			list.add(StringUtil.getStringValue(map,"city_id"));
		}

		return list;
	}

	/**
	 * 按属地分别统计所有绑定的设备量
	 */
	public List<Map<String,String>> queryAllNumList()
	{
		return jt.queryForList(new PrepareSQL(QUERYALLNUMSQL).getSQL());
	}

	/**
	 * 按属地分别统计所有绑定的设备量 (开始时间，结束时间)
	 */
	public List<Map<String,String>> queryAllNumList(String startTime,String endTime )
	{// TODO wait (more table related)
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select a.city_id, count(a.device_id) as all_num  ");
		sqlStr.append("  from stb_tab_gw_device       a,  ");
		sqlStr.append("       stb_tab_customer        b,  ");
		sqlStr.append("       stb_tab_devicetype_info c,  ");
		sqlStr.append("       stb_gw_devicestatus     d  ");
		sqlStr.append(" where a.customer_id = b.customer_id  ");
		sqlStr.append("   and a.devicetype_id = c.devicetype_id  ");
		sqlStr.append("   and a.device_id = d.device_id  ");
		sqlStr.append("   and c.category is not null  ");
		if(startTime!=null&&!"".equals(startTime)) {
		sqlStr.append("   and d.last_time >= "+string2dateTime_PATTERN_YYYY_MM_DD_2(startTime).getTime() / 1000);
		}
		if(endTime!=null&&!"".equals(endTime)) {
		sqlStr.append("   and d.last_time < "+string2dateTime_PATTERN_YYYY_MM_DD_2(endTime).getTime() / 1000);
		}
		sqlStr.append(" group by a.city_id  ");

		return jt.queryForList(new PrepareSQL(sqlStr.toString()).getSQL());
	}



	/**
	 * 按属地、探针版本分别统计所有绑定的、4k设备量
	 */
	public List<Map<String,String>> query4K_ProbeNumList()
	{
		return jt.queryForList(new PrepareSQL(QUERY4K_PROBENUMSQL).getSQL());
	}

	/**
	 * 按属地、探针版本分别统计所有绑定的、4k设备量 (开始时间，结束时间)
	 */
	public List<Map<String,String>> query4K_ProbeNumList(String startTime,String endTime)
	{// TODO wait (more table related)
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select a.city_id, b.is_probe, count(a.device_id) as '4k_num'  ");
		sqlStr.append("  from stb_tab_gw_device       a,  ");
		sqlStr.append("       stb_tab_devicetype_info b,  ");
		sqlStr.append("       stb_tab_customer        c,  ");
		sqlStr.append("       stb_gw_devicestatus     d  ");
		sqlStr.append(" where a.devicetype_id = b.devicetype_id  ");
		sqlStr.append("   and a.device_id = d.device_id  ");
		sqlStr.append("   and a.customer_id = c.customer_id  ");
		sqlStr.append("   and b.category = 1  ");
		if (startTime != null && !"".equals(startTime))
		{
			sqlStr.append("   and d.last_time >= "
					+ string2dateTime_PATTERN_YYYY_MM_DD_2(startTime).getTime() / 1000);
		}
		if (endTime != null && !"".equals(endTime))
		{
			sqlStr.append("   and d.last_time < "
					+ string2dateTime_PATTERN_YYYY_MM_DD_2(endTime).getTime() / 1000);
		}
		sqlStr.append(" group by a.city_id, b.is_probe  ");
		return jt.queryForList(new PrepareSQL(sqlStr.toString()).getSQL());
	}

	/**
	 * 按属地分别统计所有绑定的月活跃设备量
	 */
	public List<Map<String,String>> queryMonthActiveNumList(Long time)
	{// TODO wait (more table related)
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.city_id,count(a.device_id) as month_active_num ");
		psql.append("from stb_tab_gw_device a,stb_gw_devicestatus b,stb_tab_customer c ");
		psql.append("where a.device_id=b.device_id and a.customer_id=c.customer_id and b.last_time>"+time);
		psql.append(" group by a.city_id ");

		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 按属地分别统计所有绑定的月活跃设备量(开始时间，结束时间)
	 */
	public List<Map<String,String>> queryMonthActiveNumList(String startTime,String endTime)
	{// TODO wait (more table related)
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.city_id,count(a.device_id) as month_active_num ");
		psql.append("from stb_tab_gw_device a,stb_gw_devicestatus b,stb_tab_customer c ");
		psql.append("where a.device_id=b.device_id and a.customer_id=c.customer_id " );
		if (startTime != null && !"".equals(startTime))
		{
			psql.append("   and b.last_time >= "
					+ string2dateTime_PATTERN_YYYY_MM_DD_2(startTime).getTime() / 1000);
		}else {
			psql.append("   and b.last_time >= "+getBefor1Mon());
		}
		/*
		 * if (endTime != null && !"".equals(endTime)) {
		 * psql.append("   and b.last_time < " +
		 * string2dateTime_PATTERN_YYYY_MM_DD_2(endTime).getTime() / 1000); }
		 */
		psql.append(" group by a.city_id ");


		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 按属地、厂商分别统计非4K机顶盒分布报表
	 */
	public List<Map<String,String>> queryN4KNumList()
	{
		return jt.queryForList(new PrepareSQL(QUERYN4KNUMSQL).getSQL());
	}

	/**
	 * 按厂商统计全省4K机顶盒、探针部署分布报表
	 */
	public List<Map<String,String>> query4KNumList()
	{
		return jt.queryForList(new PrepareSQL(QUERY4KNUMSQL).getSQL());
	}

	/**
	 * @category 将传进来的字符串（"yyyy-MM-dd"格式）变换成日期
	 *
	 * @return Date
	 *
	 */
	public static Date string2dateTime_PATTERN_YYYY_MM_DD_2(String dateTime) {
		SimpleDateFormat simpleDateFormat = null;
		Date date = null;
		try {
		    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    date = new Date();
		    try {
		    	date = simpleDateFormat.parse(dateTime);
		    } catch (ParseException e) {
		    	System.out.println("string2dateTime方法转换出错");
		    	System.out.println("e:" + e.getMessage());
		    }
		} catch (Exception ee) {
			System.out.println("string2dateTime方法转换出错");
			System.out.println("e:" + ee.getMessage());
		}
		return date;
	}

	public static void main(String[] args)
	{
		Long a = string2dateTime_PATTERN_YYYY_MM_DD_2("2019-09-17").getTime() / 1000 ;
		System.out.println(a);
	}

	/**
	 * 获取一个月前的秒数
	 */
	private long getBefor1Mon()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = format.format(m);

    	Date str=new Date();
		try{
			str = format.parse(mon);
		}catch (ParseException e){
			System.out.println("时间格式不对:"+mon);
		}

		return str.getTime()/1000;
	}

}
