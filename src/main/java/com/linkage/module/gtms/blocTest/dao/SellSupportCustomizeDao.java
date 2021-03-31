
package com.linkage.module.gtms.blocTest.dao;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator(工号) Tel:��
 * @version 1.0
 * @since 2012-6-14 下午09:23:42
 * @category com.linkage.module.gtms.blocTest.dao<br>
 * @copyright 南京联创科技 网管科技部
 */
public interface SellSupportCustomizeDao
{
	public List queryReport(long custManagerId);
	public List<Map<String, String>> getAllCustManager();
	public List queryCustomer();
	public void addSellSupportCustomize(String custManagerId, String flowMax,
			String flowMin, String timeMax, String timeMin, String customerId);
	public  Map<String, String> getMailTmpt(String custManagerId);
}
