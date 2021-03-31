
package com.linkage.module.gtms.stb.zeroconf.action;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.WebUtil;
import com.linkage.module.gtms.stb.utils.ResTool;
import com.linkage.module.gtms.stb.zeroconf.bio.ZeroConfStatisticsReportBIO;
import com.linkage.module.gtms.stb.zeroconf.dto.ZeroConfStatisticsReportDTO;

public class ZeroConfStatisticsReportAction extends splitPageAction
{

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(ZeroConfStatisticsReportAction.class);
	private ZeroConfStatisticsReportBIO bio;
	@SuppressWarnings("rawtypes")
	private List cityList;
	private String defaultCityId;
	private ZeroConfStatisticsReportDTO dto = new ZeroConfStatisticsReportDTO();
	@SuppressWarnings("rawtypes")
	private List data;
	@SuppressWarnings("rawtypes")
	private List<Map> dtoList;

	/**
	 * 进入工单查寻页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String initForm() throws Exception
	{
		logger.info("首次进入");
		String cityId = WebUtil.getCurrentUser().getUser().getCityId();
		cityList = ResTool.getSubCityList(cityId, 1, true);
		logger.info("查询完cityList：" + cityList);
		defaultCityId = cityId;
		return "success";
	}

	public String getZeroConfStatisticsReportByCityid()
	{
		data = bio.getZeroConfStatisticsReportByCityid(dto);
		return "list";
	}

	public String listCustomer() throws Exception
	{
		totalRowCount_splitPage = bio.countCustomer(dto);
		dtoList = bio.queryCustomerList(dto, (curPage_splitPage - 1) * num_splitPage,
				num_splitPage);
		return "cuslist";
	}

	public String failListCustomer() throws Exception
	{
		totalRowCount_splitPage = bio.countFailCustomer(dto);
		dtoList = bio.queryFailCustomerList(dto, (curPage_splitPage - 1) * num_splitPage,
				num_splitPage);
		return "cusfaillist";
	}

	public ZeroConfStatisticsReportBIO getBio()
	{
		return bio;
	}

	public void setBio(ZeroConfStatisticsReportBIO bio)
	{
		this.bio = bio;
	}

	@SuppressWarnings("rawtypes")
	public List getCityList()
	{
		return cityList;
	}

	@SuppressWarnings("rawtypes")
	public void setCityList(List cityList)
	{
		this.cityList = cityList;
	}

	public String getDefaultCityId()
	{
		return defaultCityId;
	}

	public void setDefaultCityId(String defaultCityId)
	{
		this.defaultCityId = defaultCityId;
	}

	public ZeroConfStatisticsReportDTO getDto()
	{
		return dto;
	}

	public void setDto(ZeroConfStatisticsReportDTO dto)
	{
		this.dto = dto;
	}

	@SuppressWarnings("unchecked")
	public List<ZeroConfStatisticsReportDTO> getData()
	{
		return data;
	}

	public void setData(List<ZeroConfStatisticsReportDTO> data)
	{
		this.data = data;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDtoList()
	{
		return dtoList;
	}

	@SuppressWarnings("rawtypes")
	public void setDtoList(List<Map> dtoList)
	{
		this.dtoList = dtoList;
	}
}
