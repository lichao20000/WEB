package com.linkage.module.itms.resource.bio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.TerminalReplaceDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-12-23
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class TerminalReplaceBIO
{
	private static Logger logger = LoggerFactory.getLogger(TerminalReplaceBIO.class);
	
	private TerminalReplaceDAO dao;

	/**
	 * 终端替换率统计查询
	 * @param city_id
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @return
	 */
	public List<Map> TerminalReplaceInfo(String city_id, String startOpenDate1, String endOpenDate1){
		Map<String,String> vendorMap = dao.getVendor();
		List<String> tempList = dao.getVendorList();
		StringBuffer edtionIdBuffer = new StringBuffer();
		edtionIdBuffer.append(" (");
		//厂商e8-c终端数量必须是大于100才会显示该厂商
			for (int i = 0; i < tempList.size(); i++)
			{
				if (null == tempList || tempList.size() == 0)
				{
					return null;
				}
				String vendor_id = StringUtil.getStringValue(tempList.get(i));
				if (i + 1 == tempList.size())
				{
					edtionIdBuffer.append("'" + vendor_id+"'");
				}
				else
				{
					edtionIdBuffer.append("'" + vendor_id + "',");
				}
			}
		edtionIdBuffer.append(" )");
		Map<String,String> operMap = dao.TerminalReplaceOperInfo(city_id, startOpenDate1, endOpenDate1, edtionIdBuffer.toString());
		Map<String,String> allMap = dao.TerminalReplaceAllInfo(city_id, startOpenDate1, endOpenDate1, edtionIdBuffer.toString());
		
		String oper_num = "0";
		String  all_num = "0";
		String percentage = "00.00%";
		List<Map> dataList = new ArrayList<Map>();
		for (int i = 0; i < tempList.size(); i++)
			{
				Map tempMap = new HashMap<String, String>();
				oper_num = "0";
				all_num = "0";
				percentage = "00.0%";
				String vendor_id = StringUtil.getStringValue(tempList.get(i));
				tempMap.put("vendor_id", vendor_id);
				tempMap.put("vendor_name", StringUtil.getStringValue(vendorMap.get(vendor_id)));
				oper_num =  StringUtil.IsEmpty(operMap.get(vendor_id))?"0":StringUtil.getStringValue(operMap.get(vendor_id));
				all_num  =  StringUtil.IsEmpty(allMap.get(vendor_id))?"0":StringUtil.getStringValue(allMap.get(vendor_id));
				percentage = getDecimal(oper_num, all_num);
				tempMap.put("oper_num", oper_num);
				tempMap.put("all_num", all_num);
				tempMap.put("percentage", percentage);
				dataList.add(tempMap);
		}
		return dataList;
	}
	
	
	
	public TerminalReplaceDAO getDao()
	{
		return dao;
	}

	
	public void setDao(TerminalReplaceDAO dao)
	{
		this.dao = dao;
	}
	
	private String getDecimal(String total, String ttotal)
	{
		if (StringUtil.IsEmpty(total) || StringUtil.IsEmpty(ttotal) || "0".equals(total) || "0".equals(ttotal))
		{
			return "00.0%";
		}
		float t1 = Float.parseFloat(total);
		float t2 = Float.parseFloat(ttotal);
		float f = t1 / t2;
		DecimalFormat df = new DecimalFormat();
		String style = "0.00%";
		df.applyPattern(style);
		return df.format(f);
	}
}
