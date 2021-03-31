
package com.linkage.module.itms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.report.dao.CustomerSQLReportDAO;

/**
 */
public class CustomerSQLReportBIO
{
	private static Logger logger = LoggerFactory.getLogger(CustomerSQLReportBIO.class);
	private CustomerSQLReportDAO customerSQLReportDAO;

	/**
	 * @param custSQL
	 * @return
	 */
	public List<List<String>> queryResultList(String custSQL)
	{
		logger.debug("queryResultList({})", new Object[] { custSQL });
		List<List<String>> resultList = new ArrayList<List<String>>();
		try
		{
			List<Map<String, String>> maps = customerSQLReportDAO.queryAllResult(custSQL);
			if(maps.isEmpty())
			{
				return resultList;
			}
			List<String> titleList = new ArrayList<String>();
			for (String key : maps.get(0).keySet())
			{
				titleList.add(key);
			}
			logger.warn("List<String> titleList=" + titleList);
			resultList.add(0, titleList);
			for (Map<String, String> map : maps)
			{
				List<String> valueList = new ArrayList<String>();
				for (String title : titleList)
				{
					String value = StringUtil.getStringValue(map, title.trim());
					valueList.add(StringUtil.IsEmpty(value) || "null".equals(value) ? " "
							: value);
				}
				resultList.add(valueList);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("queryResultList-->Exception=" + e.getMessage());
		}
		return resultList;
	}

	/**
	 * @param custSQL
	 * @return
	 */
	public List<Map<String, String>> queryResultList4Excel(String custSQL)
	{
		logger.debug("queryResultList4Excel({})", new Object[] { custSQL });
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		try
		{
			result = customerSQLReportDAO.queryAllResult(custSQL);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("queryResultList4Excel-->Exception=" + e.getMessage());
		}
		return result;
	}

	/**
	 * @param custSQL
	 * @return
	 */
	public List<List<String>> queryResultList(int curPage_splitPage, int num_splitPage,
			String custSQL)
	{
		logger.debug("queryResultList({})", new Object[] { custSQL });
		List<List<String>> resultList = new ArrayList<List<String>>();
		try
		{
			List<Map> maps = customerSQLReportDAO.queryAllResult(curPage_splitPage,
					num_splitPage, custSQL);
			List<String> titleList = new ArrayList<String>();
			for (Object key : maps.get(0).keySet())
			{
				titleList.add(key.toString());
			}
			logger.warn("List<String> titleList=" + titleList);
			resultList.add(0, titleList);
			for (Map<String, String> map : maps)
			{
				List<String> valueList = new ArrayList<String>();
				for (String title : titleList)
				{
					String value = StringUtil.getStringValue(map, title.trim());
					valueList.add(StringUtil.IsEmpty(value) || "null".equals(value) ? " "
							: value);
				}
				resultList.add(valueList);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("Exception=" + e.getMessage());
		}
		return resultList;
	}

	/**
	 * @param custSQL
	 * @return
	 */
	public int queryResultListCount(String custSQL)
	{
		logger.debug("queryResultList({})", new Object[] { custSQL });
		int count = 0;
		try
		{
			if(DBUtil.GetDB()==3){
				count = customerSQLReportDAO.queryAllResultCount("select count(*) from "
						+ custSQL.split("from")[1]);
			}else{
				count = customerSQLReportDAO.queryAllResultCount("select count(1) from "
						+ custSQL.split("from")[1]);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("queryResultListCount-->Exception=" + e.getMessage());
		}
		return count;
	}

	/**
	 * 计算百分比
	 * 
	 * @param p1  分子
	 * @param p2 分母
	 * @return
	 */
	public String percent(long p1, long p2)
	{
		logger.debug("percent({},{})", new Object[] { p1, p2 });
		if (p2 == 0){
			return "N/A";
		}
		
		double p3 = (double) p1 / p2;
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}

	public CustomerSQLReportDAO getCustomerSQLReportDAO()
	{
		return customerSQLReportDAO;
	}

	public void setCustomerSQLReportDAO(CustomerSQLReportDAO customerSQLReportDAO)
	{
		this.customerSQLReportDAO = customerSQLReportDAO;
	}
}
