
package com.linkage.module.gtms.itv.action;

import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;

import com.linkage.module.gtms.itv.bio.ISAPicBIO;
import com.linkage.module.liposs.system.basesupport.BaseSupportAction;
import com.linkage.system.utils.chart.ChartTools;

public class ISAPicAction extends BaseSupportAction
{

	private ISAPicBIO ISAPicBio;
	private String ajax;
	private String title0 = "区域终端故障统计报表";
	private String title1 = "频道故障统计报表（全省）";
	private String title2 = "频道故障统计报表（本地）";
	private String xAname0 = "";
	private String yAName0 = "";

	public ISAPicBIO getISAPicBio()
	{
		return ISAPicBio;
	}

	public void setISAPicBio(ISAPicBIO ISAPicBio)
	{
		this.ISAPicBio = ISAPicBio;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	// 故障报表（省中心看全部 本地网看本地网的）
	public String getISATerminalReport()
	{
		String cityId = "";
		try
		{
			cityId = getUser().getCityId();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("ACTION属地：" + cityId);
		// ISAPicBIOImp ISAPicBio = new ISAPicBIOImp();
		List<Map> queryList = ISAPicBio.getISATerminalReport(cityId);
		System.out.println("size:  " + queryList.size());
		Util chartUtil = new Util();
		JFreeChart chart = chartUtil.createCategoryBarP(title0, xAname0, yAName0,
				queryList, "data", "weekScope", "type", true);
		// ajax= ChartTools.getChartWithContextUrl(chart, 324, 186, request);
		ajax = ChartTools.getChartWithContextUrl(chart, 434, 200, request);
		System.out.println("图片pic1位置" + ajax);
		return "ajax1";
	}

	public String getEpg()
	{
		String cityId = "";
		try
		{
			cityId = getUser().getCityId();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("ACTION属地：" + cityId);
		// ISAPicBIOImp ISAPicBio = new ISAPicBIOImp();
		List<Map> queryList = ISAPicBio.getEPG();
		System.out.println("size:  " + queryList.size());
		Util chartUtil = new Util();
		JFreeChart chart = chartUtil.createCategoryBarP("EPG告警统计报表", xAname0, yAName0,
				queryList, "data", "weekScope", "type", true);
		ajax = ChartTools.getChartWithContextUrl(chart, 324, 238, request);
		System.out.println("图片pic1位置" + ajax);
		return "ajax1";
	}

	// 本属地的频道报表
	public String getISAEPGReport()
	{
		String cityId = "";
		try
		{
			cityId = getUser().getCityId();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("ACTION属地：" + cityId);
		// ISAPicBIOImp ISAPicBio = new ISAPicBIOImp();
		List<Map> queryList = ISAPicBio.getISAEPGReport(cityId);
		System.out.println("size:  " + queryList.size());
		Util chartUtil = new Util();
		JFreeChart chart = chartUtil.createCategoryBarP(title1, xAname0, yAName0,
				queryList, "data", "weekScope", "type", true);
		// ajax= ChartTools.getChartWithContextUrl(chart, 324, 234, request);
		ajax = ChartTools.getChartWithContextUrl(chart, 434, 234, request);
		System.out.println("图片pic2位置" + ajax);
		return "ajax2";
	}

	// 本属地的频道报表
	public String getISAEPGReportSZX()
	{
		List<Map> queryList = ISAPicBio.getISAEPGReportSZX();
		System.out.println("size:  " + queryList.size());
		Util chartUtil = new Util();
		chartUtil.setCLablePosition(1);
		JFreeChart chart = chartUtil.createCategoryBarP(title2, xAname0, yAName0,
				queryList, "data", "weekScope", "type", true);
		// ajax= ChartTools.getChartWithContextUrl(chart,160, 240, request);
		ajax = ChartTools.getChartWithContextUrl(chart, 217, 255, request);
		System.out.println("图片pic3位置" + ajax);
		return "ajax3";
	}

	// 本属地的频道报表
	public String getISAEPGReportTwo()
	{
		List<Map> queryList = ISAPicBio.getISAEPGReportSZX();
		System.out.println("size:  " + queryList.size());
		Util chartUtil = new Util();
		chartUtil.setCLablePosition(1);
		JFreeChart chart = chartUtil.createCategoryBarP(title1, xAname0, yAName0,
				queryList, "data", "weekScope", "type", true);
		ajax = ChartTools.getChartWithContextUrl(chart, 160, 240, request);
		System.out.println("图片pic3位置" + ajax);
		return "ajax3";
	}
}