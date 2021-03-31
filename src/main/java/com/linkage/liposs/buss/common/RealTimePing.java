package com.linkage.liposs.buss.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import I_Ip_Check.IPing_Result;
import I_Ip_Check.Ping_Config;

import com.linkage.liposs.resource.IpcheckProbe;

/**
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-08-16
 * @category 后台实时ping功能的工具类
 */
public class RealTimePing
{
	IPing_Result rs;
	private static Logger log = LoggerFactory.getLogger(RealTimePing.class);
	/**
	 * 获取实时网络时延
	 * @param gather_id 采集点id
	 * @param lookback_ip 设备ip地址
	 * @param count 发包数量
	 * @param size 发包尺寸
	 * @return IPing_Result对象。
	 */
	public IPing_Result getRealTimePing(String gather_id, String lookback_ip,
			int count, int size)
	{
		Ping_Config ping_Config = new Ping_Config(lookback_ip, count, size);
		Ping_Config[] _pingConfig_list =
			{ ping_Config };
		IpcheckProbe trapProbe = new IpcheckProbe(gather_id);
		try
			{
				rs = trapProbe.I_IPingCheck(_pingConfig_list)[0];
			} catch (Exception ex)
			{
				log.error("获取实时ping数据出错。可能是后台corba联通出错，或者参数出错");
				trapProbe.clearService();
			}
		return rs;
	}
}
