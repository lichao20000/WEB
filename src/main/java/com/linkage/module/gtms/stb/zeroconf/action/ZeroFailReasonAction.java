
package com.linkage.module.gtms.stb.zeroconf.action;

import java.util.List;

import org.apache.log4j.Logger;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.WebUtil;
import com.linkage.module.gtms.stb.utils.ResTool;
import com.linkage.module.gtms.stb.zeroconf.bio.ZeroFailReasonBIO;
import com.linkage.module.gtms.stb.zeroconf.dto.ZeroConfStatisticsReportDTO;
import com.linkage.module.gtms.stb.zeroconf.dto.ZeroFailReasonDTO;

public class ZeroFailReasonAction extends splitPageAction
{

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(ZeroFailReasonAction.class);
	private ZeroFailReasonBIO bio;
	@SuppressWarnings("rawtypes")
	private List cityList;
	private String defaultCityId;
	private ZeroFailReasonDTO dto = new ZeroFailReasonDTO();
	@SuppressWarnings("rawtypes")
	private List data;
	@SuppressWarnings("rawtypes")
	private List deviceList = null;
	private String[] column = null;
	private String[] title = null;
	private String fileName = "fileexcl";

	public String getFileName()
	{
		return fileName;
	}

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

	public String zeroqueryDeviceList()
	{
		this.deviceList = bio.getZeroDeviceList(curPage_splitPage, num_splitPage, dto);
		int total = bio.getZeroDeviceListCount(dto);
		if (total % num_splitPage == 0)
		{
			maxPage_splitPage = total / num_splitPage;
		}
		else
		{
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "zeroshareList";
	}

	public String exportExcel()
	{
		title = new String[] { "地市", "成功", "E8-C终端未上报该机顶盒MAC",
				"E8-C上报机顶盒MAC异常（含绑定多个机顶盒MAC）", "IPTV账号不匹配", "AAA查询不到宽带账号拨号信息",
				"AAA反馈宽带账号信息匹配失败" };
		column = new String[] { "city_name", "zerofailsuccess", "e8cnoupmac",
				"e8cupmacexception", "iptvaccountnomatch", "aaanotfindaccount",
				"aaabackinfoerror" };
		data = bio.getZeroConfStatisticsReportByCityid(dto);
		return "excel";
	}

	public ZeroFailReasonBIO getBio()
	{
		return bio;
	}

	public void setBio(ZeroFailReasonBIO bio)
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

	public ZeroFailReasonDTO getDto()
	{
		return dto;
	}

	public void setDto(ZeroFailReasonDTO dto)
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
	public List getDeviceList()
	{
		return deviceList;
	}

	@SuppressWarnings("rawtypes")
	public void setDeviceList(List deviceList)
	{
		this.deviceList = deviceList;
	}

	public String[] getColumn()
	{
		return column;
	}

	public void setColumn(String[] column)
	{
		this.column = column;
	}

	public String[] getTitle()
	{
		return title;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
	}
}
