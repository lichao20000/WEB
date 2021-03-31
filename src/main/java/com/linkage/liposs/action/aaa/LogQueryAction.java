package com.linkage.liposs.action.aaa;

import static com.linkage.liposs.action.cst.PRINT;

import java.util.ArrayList;
import java.util.Map;

import com.linkage.liposs.dao.aaa.LogQueryDAO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 设备与命令认证记录信息查询的Action
 * 
 * @author duangr 2007-11-27
 * 
 */
public class LogQueryAction extends ActionSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9140741509903232293L;

	private LogQueryDAO logDao;

	private String startDate = null;// 查询起始时间(long)

	private String endDate = null;// 查询结束时间(long)

	private String querytype = null;// 查询类型(device/account)

	private String input = null;// 页面输入查询数据

	private ArrayList<Map> resultList = null;// 查询返回的结果

	private String start = null;// 查询起始时间(String)

	private String end = null;// 查询结束时间(String)

	/**
	 * 查询设备认证信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryAuthenLog() throws Exception
	{
		resultList = (ArrayList<Map>) logDao.getTacAuthenLog(startDate,
				endDate, querytype, input);
		return SUCCESS;
	}

	/**
	 * 查询命令认证信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryAuthorLog() throws Exception
	{
		resultList = (ArrayList<Map>) logDao.getTacAuthorLog(startDate,
				endDate, querytype, input);
		return PRINT;
	}

	public ArrayList<Map> getResultList()
	{
		return resultList;
	}

	public void setLogDao(LogQueryDAO logDao)
	{
		this.logDao = logDao;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public void setQuerytype(String querytype)
	{
		this.querytype = querytype;
	}

	public void setInput(String input)
	{
		this.input = input;
	}

	public String getStart()
	{
		return start;
	}

	public void setStart(String start)
	{
		this.start = start;
	}

	public String getEnd()
	{
		return end;
	}

	public void setEnd(String end)
	{
		this.end = end;
	}

	public String getQuerytype()
	{
		return querytype;
	}

	public String getInput()
	{
		return input;
	}

}
