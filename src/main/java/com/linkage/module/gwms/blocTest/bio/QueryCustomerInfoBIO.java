
package com.linkage.module.gwms.blocTest.bio;

import java.util.List;


import com.linkage.module.gwms.blocTest.dao.QueryCustomerInfoDAO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-8-4 上午09:10:17
 * @category com.linkage.module.gwms.blocTest.bio
 * @copyright 南京联创科技 网管科技部
 */
public class QueryCustomerInfoBIO
{

	private QueryCustomerInfoDAO dao;

	public List QueryCustomerInfo(String device_id)
	{ 
		return dao.queryCustomerInfo(device_id);
	}

	public QueryCustomerInfoDAO getDao()
	{
		return dao;
	}

	public void setDao(QueryCustomerInfoDAO dao)
	{
		this.dao = dao;
	}
}
