
package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.report.bio.CustomerSQLReportBIO;

public class CustomerSQLReportACT extends splitPageAction implements SessionAware
{

	private static final long serialVersionUID = 972144923683847180L;
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(CustomerSQLReportACT.class);
	/** session */
	private Map session;
	/** 设备厂商 */
	private String custSQL = null;
	/** 导出数据 */
	private List<Map<String, String>> data;
	/** 查询数据 */
	private List<List<String>> queryData;
	/** 导出文件列标题 */
	private String[] title;
	/** 导出文件列 */
	private String[] column;
	/** 导出文件名 */
	private String fileName;
	private CustomerSQLReportBIO customerSQLReportBIO;
	// 查询结果的合计条数
	private int total = 0;
	private String ajax;

	/**
	 * 自定义SQL查询
	 * 
	 * @return
	 */
	public String queryList()
	{
		logger.debug("queryList()");
		UserRes curUser = (UserRes) session.get("curUser");
		logger.warn("自定义SQL查询   操作人ID：" + curUser.getUser().getId());
		String matchSQL = custSQL.replace("[", "'").replace("]", "+");
		total = customerSQLReportBIO.queryResultListCount(matchSQL);
		if (total <= 50)
		{
			queryData = customerSQLReportBIO.queryResultList(matchSQL);
		}
		else
		{
			queryData = customerSQLReportBIO.queryResultList(curPage_splitPage, num_splitPage,
					matchSQL);
			if (total % num_splitPage == 0)
			{
				maxPage_splitPage = total / num_splitPage;
			}
			else
			{
				maxPage_splitPage = total / num_splitPage + 1;
			}
		}
		return "list";
	}

	/**
	 * 自定义SQL查询
	 * 
	 * @return
	 */
	public String queryListCount()
	{
		logger.debug("queryList()");
		UserRes curUser = (UserRes) session.get("curUser");
		String matchSQL = custSQL.replace("[", "'").replace("]", "+");
		logger.warn("自定义SQL查询   操作人ID：" + curUser.getUser().getId());
		logger.warn("自定义SQL查询   queryListCount：" + custSQL);
		total = customerSQLReportBIO.queryResultListCount(matchSQL);
		ajax = total + "";
		return "ajax";
	}

	/**
	 * @return 导出终端版本规范率结果
	 */
	public String getAllResultExcel()
	{
		logger.debug("getAllResultExcel()");
		try
		{
			String matchSQL = custSQL.replace("[", "'").replace("]", "+");
			logger.warn("自定义SQL查询   getAllResultExcel->matchSQL :" + custSQL);
			fileName = "customerSQLReport";
			data = customerSQLReportBIO.queryResultList4Excel(matchSQL);
			if (data != null && !data.isEmpty())
			{
				title = new String[data.get(0).keySet().size()];
				data.get(0).keySet().toArray(title);
				column = title;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("getAllResultExcel-->Exception=" + e.getStackTrace());
		}
		return "excel";
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public CustomerSQLReportBIO getCustomerSQLReportBIO()
	{
		return customerSQLReportBIO;
	}

	public void setCustomerSQLReportBIO(CustomerSQLReportBIO customerSQLReportBIO)
	{
		this.customerSQLReportBIO = customerSQLReportBIO;
	}

	public String[] getTitle()
	{
		return title;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
	}

	public String[] getColumn()
	{
		return column;
	}

	public void setColumn(String[] column)
	{
		this.column = column;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getCustSQL()
	{
		return custSQL;
	}

	public void setCustSQL(String custSQL)
	{
		this.custSQL = custSQL;
	}
	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	
	public List<Map<String, String>> getData()
	{
		return data;
	}

	
	public void setData(List<Map<String, String>> data)
	{
		this.data = data;
	}

	
	public List<List<String>> getQueryData()
	{
		return queryData;
	}

	
	public void setQueryData(List<List<String>> queryData)
	{
		this.queryData = queryData;
	}
	
	public static void  main(String args[]){
		String sql = "select "+
"TO_DATE('1970-1-1', 'YYYY-MM-DD')+(f.start_time/(86400 ) + 1/3 ) start_time,"+
"TO_DATE('1970-1-1', 'YYYY-MM-DD')+(f.end_time/(86400 ) + 1/3 )end_time , end_time-start_time  duration,"+
" g.city_id"+
"from tab_vendor a , gw_device_model b,tab_gw_device c, gw_devicestatus d,tab_devicetype_info e,gw_serv_strategy f,tab_hgwcustomer g,"+
"UPDATE_device_version_log h"+
"where  a.vendor_id = 2   "+
"and a.vendor_id = b.vendor_id"+
"and b.vendor_id = c.vendor_id"+
"and b.device_model_id = c.device_model_id"+
"and c.device_id = d.device_id"+
"and c.device_id = f.device_id"+
"and c.device_status = 1"+
"and f.service_id = 5"+
"and c.devicetype_id = e.devicetype_id"+
"and f.result_id = 1"+
"and c.device_id = g.device_id"+
"and c.device_serialnumber = h.device_serialnumber"+
"and h.Remark is  null"+
"order by f.start_time asc";
		System.out.println(sql);

	}

}
