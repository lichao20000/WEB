
package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.dto.BSSStatisticsDTO;
import com.linkage.module.gtms.stb.resource.serv.BSSStatisticsBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 零配件BSS工单统计Action类
 * 
 * @author 田启明
 * @version 1.0
 * @since 2011-12-02
 * @category com.linkage.module.itv.zeroconf.action<br>
 *           版权：南京联创科技 网管科技部
 */
public class BSSStatisticsAction extends splitPageAction implements SessionAware
{

	private Logger log = Logger.getLogger(BSSStatisticsAction.class);
	private static final long serialVersionUID = 6335260331714996430L;
	private BSSStatisticsBIO bSSStatisticsBio;
	// bss工单实体
	private BSSStatisticsDTO dto;
	private String ajax;
	// 标题（导出用）
	private String[] title;
	// 列（导出用）
	private String[] column;
	// 数据（导出用）
	private List<Map> data;
	// 文件名称（导出用）
	private String fileName;
	// 属地集合
	private List cityList;
	private String defaultCityId;
	
	private List<BSSStatisticsDTO> dtos;
	
	private Map session = null;

	/**
	 * 进入工单查寻页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String initForm() throws Exception
	{
		// 默认属地
		/*
		 * try {
		 * 
		 * } catch (Throwable e) { log.info("发生异常"); e.printStackTrace();
		 * log.error("initForm Exception:" + e.getMessage(), e); //throw e; }
		 */
		log.info("首次进入");
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		log.info("查询完cityList：" + cityList);
		// ActionContext.getContext().put("defaultCityId", getUser().getCityId());
		defaultCityId = curUser.getCityId();
		// log.info("ActionContext.getContext()执行完");
		return "enter";
	}

	public String execute()
	{
		return "success";
	}

	/**
	 * 查寻相关的工单实体，并导出
	 */
	public String exportBss()
	{
		title = new String[] { "工单编号", "属地", "业务帐号", "操作类型", "接入方式", "产品ID", "BSS工单时间" };
		column = new String[] { "work_id", "city_name", "serv_account", "oper_type",
				"addressing_type", "prod_id", "bss_date" };
		fileName = "bss";
		data = bSSStatisticsBio.exportBSS(dto, (curPage_splitPage - 1) * num_splitPage,
				num_splitPage);
		return "excel";
	}

	/**
	 * 根据实体条件查询相关的工单
	 * 
	 * @return
	 * @throws Exception
	 */
	public String query() throws Exception
	{
		// 总共有多少务数据
		totalRowCount_splitPage = bSSStatisticsBio.queryTotalNum(dto);
		// BSS工单当前查寻分页数据
		dtos = bSSStatisticsBio.queryPageData(dto,
				(curPage_splitPage - 1) * num_splitPage, num_splitPage);
		// 属地集合
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		// 默认属地
		// ActionContext.getContext().put("defaultCityId", getUser().getCityId());
		defaultCityId = curUser.getCityId();
		// ActionContext.getContext().put("dtos", dtos);
		return SUCCESS;
	}

	public String getDefaultCityId()
	{
		return defaultCityId;
	}

	public void setDefaultCityId(String defaultCityId)
	{
		this.defaultCityId = defaultCityId;
	}

	public List<BSSStatisticsDTO> getDtos()
	{
		return dtos;
	}

	public void setDtos(List<BSSStatisticsDTO> dtos)
	{
		this.dtos = dtos;
	}

	// --------------------properties set and get methods-----------------------
	public BSSStatisticsBIO getbSSStatisticsBio()
	{
		return bSSStatisticsBio;
	}

	public void setbSSStatisticsBio(BSSStatisticsBIO bSSStatisticsBio)
	{
		this.bSSStatisticsBio = bSSStatisticsBio;
	}

	public BSSStatisticsDTO getDto()
	{
		return dto;
	}

	public void setDto(BSSStatisticsDTO dto)
	{
		this.dto = dto;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
	}

	public String[] getTitle()
	{
		return title;
	}

	public void setColumn(String[] column)
	{
		this.column = column;
	}

	public String[] getColumn()
	{
		return column;
	}

	public List<Map> getData()
	{
		return data;
	}

	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setCityList(List cityList)
	{
		this.cityList = cityList;
	}

	public List getCityList()
	{
		return cityList;
	}

	
	public Map getSession() {
		return session;
	}

	
	public void setSession(Map session) {
		this.session = session;
	}
}
