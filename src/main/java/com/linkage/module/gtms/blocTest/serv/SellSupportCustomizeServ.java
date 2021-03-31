
package com.linkage.module.gtms.blocTest.serv;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator(工号) Tel:��
 * @version 1.0
 * @since 2012-6-14 下午09:21:24
 * @category com.linkage.module.gtms.blocTest.serv<br>
 * @copyright 南京联创科技 网管科技部
 */
public interface SellSupportCustomizeServ
{
	public List queryReport(long custManagerId);
	public List<Map<String, String>> getAllCustManager();
	public List queryCustomer();
	public void addSellSupportCustomize(String custManagerId, String flowMax,
			String flowMin, String timeMax, String timeMin, String customerId);
	public Map<String, String> getMailTmpt(String custManagerId);
}
