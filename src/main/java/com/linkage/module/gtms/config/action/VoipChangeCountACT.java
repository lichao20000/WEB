
package com.linkage.module.gtms.config.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.VoipChangeCountBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import action.splitpage.splitPageAction;

/**
 * @author guankai (Ailk No.300401)
 * @version 1.0
 * @since 2020年2月10日
 * @category com.linkage.module.gtms.config.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class VoipChangeCountACT extends splitPageAction implements SessionAware
{

	private static final long serialVersionUID = -3464620678936120546L;
	private Map<String, Object> session;
	private static Logger logger = LoggerFactory.getLogger(VoipChangeCountACT.class);
	private VoipChangeCountBIO bio;
	// 待用
	private String type = "";
	// 属地
	private String cityId = "00";
	// 查询结果数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 状态 1成功 2未做 3失败 空串全部
	private String status;
	// 导出文件名
	private String fileName;
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;

	public String getInfoExcel()
	{
		this.data = this.bio.queryVoipChangeInfo(this.type, this.cityId);
		this.fileName = "语音用户割接统计信息";
		this.title = new String[] { "属地", "总配置数", "成功", "失败","不在线" };
		this.column = new String[] { "city_name", "allup", "successnum", "failnum","noupnum" };
		return "excel";
	}

	public String getDev()
	{
		logger.debug("getDev()");
		this.data = this.bio.getDev(this.type, this.status, this.cityId, this.curPage_splitPage, this.num_splitPage);
		this.maxPage_splitPage = this.bio.getDevCount(this.type, this.status, this.cityId, this.curPage_splitPage,
				this.num_splitPage);
		return "devList";
	}

	public String getDevExcel()
	{
		logger.debug("getDevExcel()");
		this.fileName = "语音用户割接详细信息";
		this.data = this.bio.getDevExcel(this.type, this.cityId, this.status);
		this.title = new String[] { "LOID", "属地", "终端标识类型", "主用MGC地址", "主用MGC端口", "备用MGC地址", "备用MGC端口","新IMS侧终端物理标识", "临时终结点标识前缀" };
		this.column = new String[] { "username", "city_name", "reg_id_type_itms", "prox_serv", "prox_port", "stand_prox_serv",
				"stand_prox_port", "voip_port", "rtp_prefix" };
		return "excel";
	}

	/**
	 * 初始化页面
	 * 
	 * @author wangsenbo
	 * @date May 19, 2010
	 * @param
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		this.data = this.bio.queryVoipChangeInfo(this.type, this.cityId);
		return "init";
	}

	@Override
	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	public VoipChangeCountBIO getBio()
	{
		return bio;
	}

	public void setBio(VoipChangeCountBIO bio)
	{
		this.bio = bio;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public List<Map> getData()
	{
		return data;
	}

	public void setData(List<Map> data)
	{
		this.data = data;
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

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}
}
