
package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.stb.resource.dto.ZeroconfManualDTO;
import com.linkage.module.gtms.stb.resource.serv.ZeroconfManualBio;
import com.linkage.module.liposs.system.basesupport.BaseSupportAction;
import com.opensymphony.xwork2.ModelDriven;

/**
 * itv手动下发配置action
 * 
 * @author zhumiao
 * @version 1.0
 * @since 2011-12-5 下午03:19:35
 * @category com.linkage.module.lims.itv.zeroconf.action<br>
 * @copyright 南京联创科技 网管科技部
 */
public class ZeroconfManualAction extends BaseSupportAction implements
		ModelDriven<ZeroconfManualDTO>
{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5870143110279556911L;
	private ZeroconfManualDTO dto = new ZeroconfManualDTO();
	private String ajax = null;
	private ZeroconfManualBio manualBio;
	private List<Map> topBoxList;

	@Override
	public ZeroconfManualDTO getModel()
	{
		// TODO Auto-generated method stub
		return dto;
	}

	public String Manual()
	{
		return "success";
	}

	public String getUserAccount()
	{
		topBoxList = manualBio.getUserAccount(dto);
		return "topBox";
	}

	public String manualConfiguration()
	{
		ajax = manualBio.manualConfiguration(dto);
		return "ajax";
	}

	public ZeroconfManualDTO getDto()
	{
		return dto;
	}

	public void setDto(ZeroconfManualDTO dto)
	{
		this.dto = dto;
	}

	public void setManualBio(ZeroconfManualBio manualBio)
	{
		this.manualBio = manualBio;
	}

	public List<Map> getTopBoxList()
	{
		return topBoxList;
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
