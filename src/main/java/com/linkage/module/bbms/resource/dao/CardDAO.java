
package com.linkage.module.bbms.resource.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class CardDAO extends SuperDAO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(CardDAO.class);

	/**
	 * 获得数据卡厂商（vendorId，vendorAlias）的map
	 *
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-12
	 * @return Map 没有数据返回null
	 */
	public Map getdataCardVendorMap()
	{
		logger.debug("getdataCardVendorMap()");
		Map rmap = null;
		String sql = "select vendor_id, vendor_alias from data_card_vendor";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		List list = jt.queryForList(sql);
		if (null != list && false == list.isEmpty())
		{
			rmap = new HashMap<String, String>();
			int size = list.size();
			for (int i = 0; i < size; i++)
			{
				Map tmap = (Map) list.get(i);
				String vendorId = StringUtil.getStringValue(tmap.get("vendor_id"));
				String vendorAlias = StringUtil.getStringValue(tmap.get("vendor_alias"));
				rmap.put(vendorId, vendorAlias);
			}
		}
		return rmap;
	}

	/**
	 * 获得数据卡型号表（modelId，modelName）的map
	 *
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-12
	 * @return Map 没有数据返回null
	 */
	public Map getDataCardModelMap()
	{
		logger.debug("getDataCardModelMap()");
		Map rmap = null;
		String sql = "select model_id, model_name from data_card_model";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		List list = jt.queryForList(sql);
		if (null != list && false == list.isEmpty())
		{
			rmap = new HashMap<String, String>();
			int size = list.size();
			for (int i = 0; i < size; i++)
			{
				Map tmap = (Map) list.get(i);
				String modelId = StringUtil.getStringValue(tmap.get("model_id"));
				String modelName = StringUtil.getStringValue(tmap.get("model_name"));
				rmap.put(modelId, modelName);
			}
		}
		return rmap;
	}

	/**
	 * 获得数据卡硬件版本表（hardId，hardName）的map
	 *
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-12
	 * @return Map 没有数据返回null
	 */
	public Map getDataCardHardMap()
	{
		logger.debug("getDataCardHardMap()");
		Map rmap = null;
		String sql = "select hard_id, hard_name from data_card_hard";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		List list = jt.queryForList(sql);
		if (null != list && false == list.isEmpty())
		{
			rmap = new HashMap<String, String>();
			int size = list.size();
			for (int i = 0; i < size; i++)
			{
				Map tmap = (Map) list.get(i);
				String hardId = StringUtil.getStringValue(tmap.get("hard_id"));
				String hardName = StringUtil.getStringValue(tmap.get("hard_name"));
				rmap.put(hardId, hardName);
			}
		}
		return rmap;
	}

	/**
	 * 获得数据卡固件版本表（firmId，firmName）的map
	 *
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-12
	 * @return Map 没有数据返回null
	 */
	public Map getDataCardFirmMap()
	{
		logger.debug("getDataCardFirmMap()");
		Map rmap = null;
		String sql = "select firm_id, firm_name from data_card_firm";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		List list = jt.queryForList(sql);
		if (null != list && false == list.isEmpty())
		{
			rmap = new HashMap<String, String>();
			int size = list.size();
			for (int i = 0; i < size; i++)
			{
				Map tmap = (Map) list.get(i);
				String firmId = StringUtil.getStringValue(tmap.get("firm_id"));
				String firmName = StringUtil.getStringValue(tmap.get("firm_name"));
				rmap.put(firmId, firmName);
			}
		}
		return rmap;
	}
}
