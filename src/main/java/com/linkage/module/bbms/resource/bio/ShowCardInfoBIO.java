
package com.linkage.module.bbms.resource.bio;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.bbms.dao.DataCardDAO;
import com.linkage.module.bbms.dao.UimCardDAO;
import com.linkage.module.bbms.resource.dao.CardDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

public class ShowCardInfoBIO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(ShowCardInfoBIO.class);
	private DataCardDAO dataCardDao;
	private UimCardDAO uimCardDao;
	private CardDAO cardDao;

	/**
	 * @param cardDao
	 *            the cardDao to set
	 */
	public void setCardDao(CardDAO cardDao)
	{
		this.cardDao = cardDao;
	}

	/**
	 * @param dataCardDao
	 *            the dataCardDao to set
	 */
	public void setDataCardDao(DataCardDAO dataCardDao)
	{
		this.dataCardDao = dataCardDao;
	}

	/**
	 * @param uimCardDao
	 *            the uimCardDao to set
	 */
	public void setUimCardDao(UimCardDAO uimCardDao)
	{
		this.uimCardDao = uimCardDao;
	}

	/**
	 * 通过网关ID（deviceId）获得数据卡详细信息的map
	 * 
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-12
	 * @return Map 没有数据返回null
	 */
	public Map getDataCardInfoMap(String deviceId)
	{
		logger.debug("getDataCardInfoMap({})", deviceId);
		Map rmap = null;
		Map dataCardInfoMap = dataCardDao.getDataCardInfoMap(deviceId);
		// 数据卡厂商（vendorId，vendorAlias）的map
		Map dataCardVendorMap = cardDao.getdataCardVendorMap();
		// 数据卡型号表（modelId，modelName）的map
		Map dataCardModelMap = cardDao.getDataCardModelMap();
		// 数据卡硬件版本表（hardId，hardName）的map
		Map dataCardHardMap = cardDao.getDataCardHardMap();
		// 数据卡固件版本表（firmId，firmName）的map
		Map dataCardFirmMap = cardDao.getDataCardFirmMap();
		if (null != dataCardInfoMap)
		{
			if (null != dataCardVendorMap)
			{
				rmap = new HashMap();
				rmap.put("dataCardId", dataCardInfoMap.get("data_card_id"));
				rmap.put("dataCardEsn", dataCardInfoMap.get("data_card_esn"));
				rmap.put("deviceId", dataCardInfoMap.get("device_id"));
				rmap.put("dataCardDesc", dataCardInfoMap.get("data_card_desc"));
				rmap.put("bindStat", dataCardInfoMap.get("bind_stat"));
				// 将completeTime转换成时间
				String completeTime = StringUtil.getStringValue(dataCardInfoMap
						.get("complete_time"));
				if (false == StringUtil.IsEmpty(completeTime))
				{
					DateTimeUtil dateTimeUtil = new DateTimeUtil(Long
							.parseLong(completeTime) * 1000);
					completeTime = dateTimeUtil.getLongDate();
					dateTimeUtil = null;
				}
				rmap.put("completeTime", completeTime);
				// 将bindTime转换成时间
				String bindTime = StringUtil.getStringValue(dataCardInfoMap
						.get("bind_time"));
				if (false == StringUtil.IsEmpty(bindTime))
				{
					DateTimeUtil dateTimeUtil = new DateTimeUtil(
							Long.parseLong(bindTime) * 1000);
					bindTime = dateTimeUtil.getLongDate();
					dateTimeUtil = null;
				}
				rmap.put("bindTime", bindTime);
				// 将bindTime转换成时间
				String updateTime = StringUtil.getStringValue(dataCardInfoMap
						.get("update_time"));
				if (false == StringUtil.IsEmpty(updateTime))
				{
					DateTimeUtil dateTimeUtil = new DateTimeUtil(Long
							.parseLong(updateTime) * 1000);
					updateTime = dateTimeUtil.getLongDate();
					dateTimeUtil = null;
				}
				rmap.put("updateTime", updateTime);
				rmap.put("remark", dataCardInfoMap.get("remark"));
				rmap.put("workMode", dataCardInfoMap.get("work_mode"));
				rmap.put("vendorAlias", dataCardVendorMap.get(StringUtil
						.getStringValue(dataCardInfoMap.get("vendor_id"))));
				rmap.put("modelName", dataCardModelMap.get(StringUtil
						.getStringValue(dataCardInfoMap.get("model_id"))));
				rmap.put("hardName", dataCardHardMap.get(StringUtil
						.getStringValue(dataCardInfoMap.get("hard_id"))));
				rmap.put("firmName", dataCardFirmMap.get(StringUtil
						.getStringValue(dataCardInfoMap.get("firm_id"))));
			}
			else
			{
				logger.warn("dataCardVendorMap is null");
			}
		}
		else
		{
			logger.warn("uimCardInfoMap is null");
		}
		return rmap;
	}

	/**
	 * 通过网关ID（deviceId）获得uim卡详细信息的map
	 * 
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-12
	 * @return Map 没有数据返回null
	 */
	public Map getUimCardInfoMap(String deviceId)
	{
		logger.debug("getUimCardInfoMap({})", deviceId);
		Map rmap = null;
		Map uimCardInfoMap = uimCardDao.getUimCardInfoMap(deviceId);
		if (null != uimCardInfoMap)
		{
			rmap = new HashMap();
			rmap.put("uimCardId", uimCardInfoMap.get("uim_card_id"));
			rmap.put("uimCardImsi", uimCardInfoMap.get("uim_card_imsi"));
			rmap.put("deviceId", uimCardInfoMap.get("device_id"));
			rmap.put("uimCardDesc", uimCardInfoMap.get("uim_card_desc"));
			rmap.put("bindStat", uimCardInfoMap.get("bind_stat"));
			// 将completeTime转换成时间
			String completeTime = StringUtil.getStringValue(uimCardInfoMap
					.get("complete_time"));
			if (false == StringUtil.IsEmpty(completeTime))
			{
				DateTimeUtil dateTimeUtil = new DateTimeUtil(
						Long.parseLong(completeTime) * 1000);
				completeTime = dateTimeUtil.getLongDate();
				dateTimeUtil = null;
			}
			rmap.put("completeTime", completeTime);
			// 将bindTime转换成时间
			String bindTime = StringUtil.getStringValue(uimCardInfoMap.get("bind_time"));
			if (false == StringUtil.IsEmpty(bindTime))
			{
				DateTimeUtil dateTimeUtil = new DateTimeUtil(
						Long.parseLong(bindTime) * 1000);
				bindTime = dateTimeUtil.getLongDate();
				dateTimeUtil = null;
			}
			rmap.put("bindTime", bindTime);
			// 将bindTime转换成时间
			String updateTime = StringUtil.getStringValue(uimCardInfoMap
					.get("update_time"));
			if (false == StringUtil.IsEmpty(updateTime))
			{
				DateTimeUtil dateTimeUtil = new DateTimeUtil(
						Long.parseLong(updateTime) * 1000);
				updateTime = dateTimeUtil.getLongDate();
				dateTimeUtil = null;
			}
			rmap.put("updateTime", updateTime);
			rmap.put("voltage", uimCardInfoMap.get("voltage"));
			rmap.put("remark", uimCardInfoMap.get("remark"));
		}
		else
		{
			logger.warn("uimCardInfoMap is null");
		}
		return rmap;
	}

	/**
	 * 通过网关ID（deviceId）获得卡插拔状态
	 * 
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-12
	 * @return 卡已插入返回“1” 卡未插入返回”卡未插入“ 采集失败返回失败信息 查询数据库失败返回null
	 */
	public String searchCardPlugStat(String deviceId)
	{
		logger.debug("searchCardPlugStat({})", deviceId);
		int iresult = new SuperGatherCorba(LipossGlobals.getGw_Type(deviceId)).getCpeParams(deviceId, 43);
		if (iresult == 1 || iresult == 4)
		{
			if(iresult == 1){
				logger.debug("采集EVDO成功,卡在位");
			}
			if(iresult == 4){
				logger.debug("9000以后错误");
			}
			Map dataCardInfoMap = dataCardDao.getDataCardInfoMap(deviceId);
			if (null != dataCardInfoMap)
			{
				String plugStat = StringUtil.getStringValue(dataCardInfoMap
						.get("plug_stat"));
				if ("1".equals(plugStat))
				{
					return "1";
				}
				else
				{
					return "卡不在位";
				}
			}
			else
			{
				logger.warn("dataCardInfoMap is null");
			}
		}
		else
		{
			logger.warn(Global.G_Fault_Map.get(iresult).getFaultReason());
			return Global.G_Fault_Map.get(iresult).getFaultReason();
		}
		return null;
	}

	/**
	 * 通过网关ID（deviceId）获得卡使用状态
	 * 
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-12
	 * @return Map
	 */
	public Map searchCardUseStat(String deviceId)
	{
		logger.debug("searchCardUseStat({})", deviceId);
		int cardResult = new SuperGatherCorba(LipossGlobals.getGw_Type(deviceId)).getCpeParams(deviceId, 2);
		Map rmap = null;
		if (1 == cardResult)
		{
			logger.debug("采集使用状态，工作模式成功！");
			
			rmap = dataCardDao.getEvdoDataCardBySession(deviceId);
		}
		else
		{
			rmap = new HashMap();
			logger.warn("采集卡厂商等信息成功后，采集SESSION信息失败");
			logger.warn(Global.G_Fault_Map.get(cardResult).getFaultReason());
			rmap.put("useStat", "采集SESSION信息失败");
			rmap.put("workMode", "采集SESSION信息失败");
		}
		return rmap;
	}
}
