
package com.linkage.module.gtms.stb.report.act;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.report.bio.CheckFruitQueryBIO;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-4-10
 * @category com.linkage.module.lims.stb.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class CheckFruitQueryACT extends splitPageAction
{

	private static final long serialVersionUID = 1L;
	// 日志
	private static Logger logger = LoggerFactory.getLogger(CheckFruitQueryACT.class);
	private CheckFruitQueryBIO bio;
	// 开始时间
	private String starttime;
	// 结束时间
	private String endtime;
	// 业务账号
	private String user_id;
	// MAC
	private String mac;
	// 参数集合
	private List<Map> data;
	// 文件名
	private String fileName;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	private String ajax;

	/**
	 * 初始页面时间
	 * 
	 * @return
	 */
	public String init()
	{
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		return "init";
	}

	/**
	 * 查询
	 * 
	 * @return
	 */
	public String Query()
	{
		this.setTime();
		logger.warn("CheckFruitQueryACT======Query()===========>参数"+"starttime:"+starttime+"endtime:"+endtime+"user_id:"+user_id+"mac:"+mac);
		data = bio.Query(starttime, endtime, curPage_splitPage, num_splitPage, user_id,
				mac);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "success";
	}

	/**
	 * 导出
	 */
	public String Derive()
	{
		this.setTime();
		logger.warn("CheckFruitQueryACT======Derive()===========>");
		data = bio.derive(starttime, endtime, user_id, mac);
		title = new String[6];
		title[0] = "业务账号";
		title[1] = "MAC";
		title[2] = "接入方式";
		title[3] = "码率";
		title[4] = "丢包";
		title[5] = "上报时间";
		column = new String[6];
		column[0] = "user_id";
		column[1] = "mac";
		column[2] = "conn_type";
		column[3] = "bitrate";
		column[4] = "package_lost";
		column[5] = "report_time";
		fileName = "机顶盒检测查询结果";
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime))
		{
			starttime = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime))
		{
			endtime = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endtime = String.valueOf(dt.getLongTime());
		}
	}
	/**
	 * 比较时间间隔是否超过一个月
	 * 1表示没超过
	 * -1表示超过
	 * @return
	 */
	public String bijiao()
	{
		this.setTime();
		int A=Integer.valueOf(starttime);
		int B=Integer.valueOf(endtime);
		logger.warn("starttime====>"+A);
		logger.warn("endtime====>"+B);
		int C=B-A;
		logger.warn("时间差====>"+C);
		if(C<=2592000)
		{
			ajax="1";
		}
		else
		{
			ajax="-1";
		}
		return "ajax";
	}

	public String getStarttime()
	{
		return starttime;
	}

	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	public String getUser_id()
	{
		return user_id;
	}

	public void setUser_id(String user_id)
	{
		this.user_id = user_id;
	}

	public String getMac()
	{
		return mac;
	}

	public void setMac(String mac)
	{
		this.mac = mac;
	}

	public CheckFruitQueryBIO getBio()
	{
		return bio;
	}

	public void setBio(CheckFruitQueryBIO bio)
	{
		this.bio = bio;
	}

	public List<Map> getData()
	{
		return data;
	}

	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
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

	
	public String getAjax()
	{
		return ajax;
	}

	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	
}
