package com.linkage.module.gtms.stb.resource.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 
 * @author 岩
 * @date 2016-6-8
 */
public class ZeroConfigRateCountDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(ZeroConfigRateCountDAO.class);

	/**
	 *
	 * @author 岩
	 * @date 2016-6-8
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getAllSheetNum(String startTime, String endTime)
	{
		logger.debug("getAllSheetNum({})", new Object[] { startTime, endTime });
		PrepareSQL sb = new PrepareSQL();
		sb.append("select a.city_id,a.bind_way,a.bind_state,count(*) as num");
		sb.append(" from  stb_tab_gw_device a,stb_tab_customer b");
		sb.append(" where a.serv_account=b.serv_account ");
		sb.append(" and b.openUserdate>=?");
		sb.append(" and b.openUserdate<=?");
		sb.append(" group by a.bind_way,a.bind_state,a.city_id");
		sb.setInt(1, StringUtil.getIntegerValue(startTime));
		sb.setInt(2, StringUtil.getIntegerValue(endTime));
		return jt.queryForList(sb.getSQL());
	}

	@SuppressWarnings("rawtypes")
	public List<Map> queryZeroConfigDetail(String cityId,String bindState,
			String bindWay,String startTime,String endTime,int curPageSplitPage,int numSplitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		// TODO wait (more table related)
		psql.append("select distinct a.device_id,a.city_id,c.vendor_name,d.device_model,e.softwareversion," +
				    " a.device_serialnumber,a.cpe_mac,a.serv_account,a.loopback_ip,a.bind_time ");
		psql.append(" from stb_tab_gw_device a,stb_tab_customer b,stb_tab_vendor c,stb_gw_device_model d,stb_tab_devicetype_info e");
		psql.append(" where a.serv_account=b.serv_account and a.vendor_id=c.vendor_id ");
		psql.append(" and a.device_model_id=d.device_model_id and a.devicetype_id=e.devicetype_id ");
		psql.append("  and b.openUserdate>="+startTime);
		psql.append("  and b.openUserdate<="+endTime);
		
		if("3".equals(bindWay))
		{
			//其他绑定数
			psql.append(" and a.bind_state!=0 and a.bind_way=3 ");
		}
		else if("0".equals(bindWay) || "1".equals(bindWay) 
				|| "2".equals(bindWay) || "5".equals(bindWay))
		{
			//求mac ip 以及itv成功数
			psql.append(" and a.bind_state=1 and a.bind_way = "+ bindWay);
		}
		
		else if("7".equals(bindWay))
		{
			//零配置总数 成功数 失败数
			if("1".equals(bindState)||"-1".equals(bindState))
			{
				//求成功数 失败数
				psql.append(" and a.bind_way in(0,1,2,5) and a.bind_state = "+bindState +"");
			}
			else
			{
				//求总数
				psql.append(" and a.bind_way in(0,1,2,5) and a.bind_state!=0 ");
			}
		}
		
		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId)))
		{
			psql.append(" and a.city_id in ");
			psql.append(" (select city_id from tab_city where city_id= '"+cityId+"' or parent_id= '"+cityId+"')");
		}
		else
		{
			psql.append(" and a.city_id in ('"+cityId+"')");
		}
		psql.append(" order by a.bind_time desc");
		return querySP(psql.getSQL(), (curPageSplitPage - 1) * numSplitPage,numSplitPage);
	}


	public int queryZeroConfigDetailCount(String cityId,String bindState,
			String bindWay,String startTime,String endTime)
	{
		PrepareSQL psql = new PrepareSQL();
		// TODO wait (more table related)
		psql.append(" select  count(*) from (");
		psql.append("select distinct a.device_id,a.city_id,c.vendor_name,d.device_model,e.softwareversion," +
				" a.device_serialnumber,a.cpe_mac,a.serv_account,a.loopback_ip");
		psql.append(" from stb_tab_gw_device a,stb_tab_customer b,stb_tab_vendor c,stb_gw_device_model d,stb_tab_devicetype_info e");
		psql.append(" where a.serv_account=b.serv_account and a.vendor_id=c.vendor_id");
		psql.append(" and a.device_model_id=d.device_model_id and a.devicetype_id=e.devicetype_id");
		psql.append(" and b.openUserdate>="+startTime);
		psql.append(" and b.openUserdate<="+endTime);
		
		if("3".equals(bindWay))
		{
			//其他绑定数
			psql.append(" and a.bind_state!=0 and a.bind_way=3 ");
		}
		else if("0".equals(bindWay) || "1".equals(bindWay) 
				||"2".equals(bindWay) || "5".equals(bindWay))
		{
			//求mac ip 以及itv成功数
			psql.append(" and a.bind_state=1 and a.bind_way = "+ bindWay);
		}
		else if("7".equals(bindWay))
		{
			//零配置总数 成功数 失败数
			if("1".equals(bindState)||"-1".equals(bindState))
			{
				//求成功数 失败数
				psql.append(" and a.bind_way in(0,1,2,5) and a.bind_state = "+bindState +"");
			}
			else
			{
				//求总数
				psql.append(" and a.bind_way in(0,1,2,5) and a.bind_state!=0 ");
			}
		}
		
		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId)))
		{
			psql.append(" and a.city_id in ");
			psql.append(" (select city_id from tab_city where city_id= '"+cityId+"' or parent_id= '"+cityId+"')) zerocount");
		}
		else
		{
			psql.append(" and a.city_id in ('"+cityId+"')) zerocount");
		}
		return jt.queryForInt(psql.getSQL());
	}
}
