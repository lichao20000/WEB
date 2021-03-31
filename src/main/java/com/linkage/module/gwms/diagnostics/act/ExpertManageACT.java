package com.linkage.module.gwms.diagnostics.act;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.linkage.module.gwms.diagnostics.bio.ExpertManageBIO;

/**
 * @author Jason(3412)
 * @date 2009-9-4
 */
public class ExpertManageACT {

	// 专家库列表
	private List expertList;
	// 故障描述
	private String faultDesc;
	// 专家建议
	private String suggest;
	// ID
	private String expertId;
	// 专家建议领域(线路信息， 连接错误代码， 无线模块等)
	private String fileds;
	// BIO
	private ExpertManageBIO expertBio;
	//ajax
	private String ajax;

	/**
	 * 获取专家库列表
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-4
	 * @return String
	 */
	public String execute() {

		// 获取专家库列表
		expertList = expertBio.getExpertList();
		return "success";
	}

	/**
	 * 更新专家建议和故障描述
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-4
	 * @return String
	 */
	public String updateExpert() {
		if(1 == expertBio.updateExpert(expertId, faultDesc, suggest)){
			ajax = "更新成功";
		}else{
			ajax = "更新失败";
		}
		return "ajax";
	}

	public String getFileds() {
		return fileds;
	}

	public void setFileds(String fileds) {
		try {
			this.fileds = new String(fileds.getBytes("gbk"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public List getExpertList() {
		return expertList;
	}

	public void setFaultDesc(String faultDesc)
	{
		try
		{
			this.faultDesc = java.net.URLDecoder.decode(faultDesc, "UTF-8");
		}
		catch (Exception e)
		{
			this.faultDesc = faultDesc;
		}
	}

	public void setSuggest(String suggest)
	{
		try
		{
			this.suggest = java.net.URLDecoder.decode(suggest, "UTF-8");
		}
		catch (Exception e)
		{
			this.suggest = suggest;
		}
	}

	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}

	public void setExpertBio(ExpertManageBIO expertBio) {
		this.expertBio = expertBio;
	}

	public String getAjax() {
		return ajax;
	}

}
