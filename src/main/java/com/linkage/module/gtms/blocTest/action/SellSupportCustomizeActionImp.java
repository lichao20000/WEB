
package com.linkage.module.gtms.blocTest.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.mail.SendMailUsingAuthentication;
import com.linkage.module.gtms.blocTest.serv.SellSupportCustomizeServ;

public class SellSupportCustomizeActionImp implements SellSupportCustomizeAction
{
	private static Logger logger = LoggerFactory
			.getLogger(SellSupportCustomizeActionImp.class);
	private SellSupportCustomizeServ serv;
	private String gw_type = null;
	private String ajax = "success";
	private String custManagerId = null;
	private String flowMax = null;
	private String flowMin = null;
	private String timeMax = null;
	private String timeMin = null;
	private String customerId = null;
	private String recieveMailAddress = null;
	private String mailTopic = null;
	private String mailContent = null;
	private List<Map<String, String>> custManagerList = new ArrayList<Map<String, String>>();
	private List<Map<String,String>> sellSupportReportList;
	// 客户资料列表
	private List customerList;
	public String initReport()
	{
		custManagerList = serv.getAllCustManager();
		return "initReport";
	}

	public String sendMail()
	{
		String[] emailList = { recieveMailAddress };
		logger.warn("{},{},{}",new Object[]{recieveMailAddress,mailTopic,mailContent});
		logger.warn("客户经理发送邮件："+custManagerId);
		Map<String,String> map = serv.getMailTmpt(custManagerId);

		SendMailUsingAuthentication smtpMailSender = SendMailUsingAuthentication.getInstance();
		try
		{
			smtpMailSender.postMail(map.get("mail_topic"),"客户"+customerId+":\n" + map.get("mail_content"),emailList);
		}
		catch (MessagingException e)
		{
			ajax = "邮件发送失败";
			e.printStackTrace();
		}
		ajax = "邮件发送成功";
		logger.warn(ajax);
		return "ajax";
	}
	public String queryReport()
	{
		sellSupportReportList = serv.queryReport(StringUtil.getLongValue(custManagerId));
		return "queryReport";
	}
	/**
	 * 查询客户资料结果列表
	 *
	 * @return
	 * @throws Exception
	 */
	public String queryData() throws Exception {
		custManagerList = serv.getAllCustManager();
		logger.warn("{}",new Object[]{custManagerList});
		customerList = serv.queryCustomer();
		return "customerlist";
	}
	public String add()
	{
		serv.addSellSupportCustomize(custManagerId, flowMax, flowMin, timeMax, timeMin, customerId);
		ajax = "定制成功";
		return "ajax";
	}

	public String init()
	{
		custManagerList = serv.getAllCustManager();
		return "init";
	}

	public String getGw_type()
	{
		return gw_type;
	}

	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}


	public List getCustomerList()
	{
		return customerList;
	}




	public String getMailTopic()
	{
		return mailTopic;
	}


	public void setMailTopic(String mailTopic)
	{
		this.mailTopic = mailTopic;
	}

	public String getMailContent()
	{
		return mailContent;
	}


	public void setMailContent(String mailContent)
	{
		this.mailContent = mailContent;
	}

	public String getRecieveMailAddress()
	{
		return recieveMailAddress;
	}


	public void setRecieveMailAddress(String recieveMailAddress)
	{
		this.recieveMailAddress = recieveMailAddress;
	}

	public List<Map<String, String>> getSellSupportReportList()
	{
		return sellSupportReportList;
	}


	public void setSellSupportReportList(List<Map<String, String>> sellSupportReportList)
	{
		this.sellSupportReportList = sellSupportReportList;
	}

	public void setCustomerList(List customerList)
	{
		this.customerList = customerList;
	}
	public String getAjax()
	{
		return ajax;
	}

	public String getCustManagerId()
	{
		return custManagerId;
	}

	public void setCustManagerId(String custManagerId)
	{
		this.custManagerId = custManagerId;
	}

	public String getFlowMax()
	{
		return flowMax;
	}


	public String getCustomerId()
	{
		return customerId;
	}


	public void setCustomerId(String customerId)
	{
		this.customerId = customerId;
	}

	public void setFlowMax(String flowMax)
	{
		this.flowMax = flowMax;
	}

	public String getFlowMin()
	{
		return flowMin;
	}

	public SellSupportCustomizeServ getServ()
	{
		return serv;
	}

	public void setServ(SellSupportCustomizeServ serv)
	{
		this.serv = serv;
	}

	public List<Map<String, String>> getCustManagerList()
	{
		return custManagerList;
	}

	public void setCustManagerList(List<Map<String, String>> custManagerList)
	{
		this.custManagerList = custManagerList;
	}

	public void setFlowMin(String flowMin)
	{
		this.flowMin = flowMin;
	}

	public String getTimeMax()
	{
		return timeMax;
	}

	public void setTimeMax(String timeMax)
	{
		this.timeMax = timeMax;
	}

	public String getTimeMin()
	{
		return timeMin;
	}

	public void setTimeMin(String timeMin)
	{
		this.timeMin = timeMin;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
}
