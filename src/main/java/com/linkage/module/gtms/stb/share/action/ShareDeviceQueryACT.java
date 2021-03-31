/**
 * 
 */
package com.linkage.module.gtms.stb.share.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.diagnostic.serv.PingInfoBIO;
import com.linkage.module.gtms.stb.share.serv.ShareDeviceQueryBIO;
import com.linkage.module.gwms.Global;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 * 
 */
public class ShareDeviceQueryACT extends splitPageAction implements ServletRequestAware {
	
	/**
	 *  serial
	 */
	private static final long serialVersionUID = 1L;
	
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ShareDeviceQueryACT.class);

	ShareDeviceQueryBIO gwDeviceBio = null;
	
	// 2020/04/22 山西联通增加pingBio 是为了在ping Trace跳转页面的时候获取到IpMap初始值
	PingInfoBIO pingBio = null;
	private List<Map<String, String>> IpMap;
	
	private Map deviceDetailMap = null;
	private String deviceId = null;
	private String ajax = null;
	
	private List deviceList = null;

	private String gwShare_cityId = null;
	
	private String gwShare_nextCityId = null;

	private String gwShare_vendorId = null;

	private String gwShare_modelId = null;

	private String gwShare_deviceModelId = null;
	
	private String gwShare_hardwareVersion = null;

	private String gwShare_devicetypeId = null;

	HttpServletRequest request = null;

	private String gwShare_queryType = null;

	private String gwShare_queryResultType = null;

	private String gwShare_queryParam = null;

	private String gwShare_queryField = null;

	private String gwShare_onlineStatus = null;

	private String gwShare_bindType = null;

	private String gwShare_deviceSerialnumber = null;
	
	private String gwShare_fileName = null;
	
	private String gwShare_msg = null;
	
	private String version_path=null;
	private String special_path=null;
	
	private String gwShare_matchSQL = null;
	
	private String gwShare_platform = null;
	
	/**
	 * 江西itv新增根据ip地址段过滤
	 */
	private String gwShare_IPType = null;
	/**
	 * 
	 * 新疆新增平台
	 */
	private String gwShare_belong = null;
	
	// 是否是页面跳转过来
	private String isGoPage = "0";
	
	public String getGwShare_platform()
	{
		return gwShare_platform;
	}



	
	public void setGwShare_platform(String gwShare_platform)
	{
		this.gwShare_platform = gwShare_platform;
	}



	public String getGwShare_matchSQL()
	{
		return gwShare_matchSQL;
	}


	
	public void setGwShare_matchSQL(String gwShare_matchSQL)
	{
		this.gwShare_matchSQL = gwShare_matchSQL;
	}

	private String ITV = null;
	
	private String gw_type = null;
	
	
	public String getGw_type()
	{
		return gw_type;
	}

	
	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	/**
	 * 1开启记录按照上报时间排序，0不开启，默认不开启
	 */
	private String gwShare_OrderByUpdateDate = null;
	
	/**
	 * 查询设备记录数，默认全部查询
	 */
	private String gwShare_RecordNum = null;
	
	/**
	 * 查询文本
	 */
	private String searchTxt;
	/**
	 * 匹配文本的分隔符
	 */
	private String separator;
	
	//查询结果的合计条数
	private int total = 0;
	
	/**
	 * execute
	 */
	public String execute() throws Exception {
		logger.debug("execute()");
		return "success";
	}
	
	/**
	 * 模拟匹配提示
	 */
	public String getDeviceSn() throws Exception{
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		
		ajax = gwDeviceBio.getDeviceSn(curUser.getCityId(), searchTxt, separator,gwShare_queryField);
			
		return "ajax";
	}
	
	/**
	 * 查询属地
	 * 
	 * @return
	 */
	public String getCityNextChild() {
		logger.debug("GwDeviceQueryACT=>getCity()");
		if (null == this.gwShare_cityId) {

			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.gwShare_cityId = curUser.getCityId();
		}
		this.ajax = gwDeviceBio.getCity(gwShare_cityId);
		return "ajax";
	}

	/**
	 * 查询属地（包含子属地）
	 * 
	 * @return
	 */
	public String getAllCityNextChild() {
		logger.debug("GwDeviceQueryACT=>getAllCity()");
		if (null == this.gwShare_cityId) {

			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.gwShare_cityId = curUser.getCityId();
		}
		this.ajax = gwDeviceBio.getAllCity(gwShare_cityId);
		return "ajax";
	}
	/**
	 * 查询设备厂商
	 * 
	 * @return
	 */
	public String getVendor() {
		logger.debug("GwDeviceQueryACT=>getVendor()");
		this.ajax = gwDeviceBio.getVendor();
		return "ajax";
	}

	/**
	 * 查询设备型号
	 * 
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel() {
		logger.debug("GwDeviceQueryACT=>getDeviceModel()");
		this.ajax = gwDeviceBio.getDeviceModel(gwShare_vendorId);
		return "ajax";
	}


	/**
	 * 查询设备OUI
	 *
	 * @param vendorId,modelId
	 * @return
	 */
	public String getDeviceOui() {
		logger.debug("GwDeviceQueryACT=>getDeviceOui()");
		this.ajax = gwDeviceBio.getDeviceOui(gwShare_deviceModelId);
		return "ajax";
	}


	/**
	 * 查询设备版本
	 * 
	 * @return
	 */
	public String getDevicetype() {
		logger.debug("GwDeviceQueryACT=>getDevicetype()");
		this.ajax = gwDeviceBio.getDevicetype(gwShare_deviceModelId);
		return "ajax";
	}
	
	/**
	 * 查询设备硬件版本
	 * 
	 * @return
	 */
	public String getDeviceHardVersion() {
		logger.debug("GwDeviceQueryACT=>getDeviceHardVersion()");
		this.ajax = gwDeviceBio.getDeviceHardVersion(gwShare_deviceModelId);
		return "ajax";
	}
	
	/**
	 * 获取软件/目标版本信息
	 * @return
	 */
	public String getSoftVersion(){
		ajax=gwDeviceBio.getSoftVersion(gwShare_hardwareVersion,gwShare_deviceModelId);
		return "ajax";
	}
	
	/**
	 *获取公网路径 
	 * @return
	 */
	public String getVersionPath(){
		ajax=gwDeviceBio.getVersionPath(gwShare_vendorId,gwShare_deviceModelId);
		return "ajax";
	}
	
	/**
	 * 获取专网路径
	 * @return
	 */
	public String getSpecialPath(){
		ajax=gwDeviceBio.getSpecialPath(gwShare_vendorId,gwShare_deviceModelId,version_path);

		return "ajax";
	}

	/**
	 * 获取DCN网路径
	 * @return
	 */
	public String getDcnPath(){
		ajax=gwDeviceBio.getDcnPath(gwShare_vendorId,gwShare_deviceModelId,version_path,special_path);

	return "ajax";
}
	
	/**
	 * 查询设备硬件版本
	 * 
	 * @return
	 */
	public String getDevicetypeByHardVersion() {
		logger.debug("GwDeviceQueryACT=>getDevicetypeByHardVersion()");
		this.ajax = gwDeviceBio.getDevicetypeByHardVersion(gwShare_hardwareVersion);
		return "ajax";
	}

	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryDeviceList() {
		logger.debug("GwDeviceQueryACT=>getDeviceList()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if("3".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}
			this.deviceList = gwDeviceBio.getDeviceListForHblt(areaId, gwShare_queryType, gwShare_cityId, gwShare_fileName);
			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = gwDeviceBio.getMsg();
			}
			total = deviceList == null?0:this.deviceList.size();
			return "shareList0";

		}else{
			if(null!=gwShare_queryType){
				gwShare_queryType.trim();
			}
			if(null!=gwShare_cityId){
				gwShare_cityId.trim();
			}else{
				this.gwShare_cityId = curUser.getCityId();
			}
			
			if(null!=gwShare_queryParam){
				gwShare_queryParam.trim();
			}
			if(null!=gwShare_queryField){
				gwShare_queryField.trim();
			}
			if(null!=gwShare_onlineStatus){
				gwShare_onlineStatus.trim();
			}
			if(null!=gwShare_vendorId){
				gwShare_vendorId.trim();
			}
			if(null!=gwShare_deviceModelId){
				gwShare_deviceModelId.trim();
			}
			if(null!=gwShare_devicetypeId){
				gwShare_devicetypeId.trim();
			}
			if(null!=gwShare_bindType){
				gwShare_bindType.trim();
			}
			if(null!=gwShare_deviceSerialnumber){
				gwShare_deviceSerialnumber.trim();
			}
			
			if(Global.XJDX.equals(Global.instAreaShortName) && "2".equals(gwShare_queryType)){
				if(null != gwShare_hardwareVersion){
					gwShare_hardwareVersion.trim();
				}
				if(null != gwShare_belong){
					gwShare_belong.trim();
				}
				//新疆添加硬件版本和平台，由于涉及的接口有很多地方调用，所以针对新疆在此拼接
				if((!StringUtil.IsEmpty(gwShare_hardwareVersion) && !"-1".equals(gwShare_hardwareVersion)) || 
						!StringUtil.IsEmpty(gwShare_belong) && !"-1".equals(gwShare_belong)){
					gwShare_deviceSerialnumber = gwShare_deviceSerialnumber+":" + gwShare_hardwareVersion +":"+gwShare_belong;
				}
				
				logger.warn("xj_dx gwShare_deviceSerialnumber => "+gwShare_deviceSerialnumber);
				logger.warn("xj_dx querytype => "+gwShare_queryType);
			}
			
			total = gwDeviceBio.getDeviceListCount(areaId, gwShare_queryType,
						gwShare_queryParam, gwShare_queryField, gwShare_cityId,
						gwShare_onlineStatus, gwShare_vendorId,
						gwShare_deviceModelId, gwShare_devicetypeId,
						gwShare_bindType, gwShare_deviceSerialnumber);
			
			//如果页面设置了，则强制查询个数记录
			//logger.error("===========gwShare_RecordNum:{}......",gwShare_RecordNum);
			if(StringUtil.getIntegerValue(gwShare_RecordNum) > 0)
			{
				num_splitPage = StringUtil.getIntegerValue(gwShare_RecordNum);
				//total = 60; yaoli 20180529  此处注释，因为没怎么用到
				if(total == 0){
					return "shareList"; 
				}
				this.deviceList = gwDeviceBio.getDeviceList(curPage_splitPage,num_splitPage,
						areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
						gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,Integer.parseInt(gwShare_OrderByUpdateDate),null);
				
				if(null == this.deviceList || this.deviceList.size() < 1){
					this.gwShare_msg = gwDeviceBio.getMsg();
					return "shareList";
 				}
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage; 
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				return "shareList";
			}else{
				if(total <= 50){
					this.deviceList = gwDeviceBio.getDeviceList(areaId, gwShare_queryType,
							gwShare_queryParam, gwShare_queryField,	gwShare_cityId, 
							gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
							gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,null);
					if(null==this.deviceList || this.deviceList.size()<1){
						this.gwShare_msg = gwDeviceBio.getMsg();
					}
					return "shareList0"; 
				}else{
					this.deviceList = gwDeviceBio.getDeviceList(curPage_splitPage,num_splitPage,
							areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
							gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
							gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,Integer.parseInt(gwShare_OrderByUpdateDate),null);
					if (total % num_splitPage == 0) {
						maxPage_splitPage = total / num_splitPage;
					} else {
						maxPage_splitPage = total / num_splitPage + 1;
					}
					if(null==this.deviceList || this.deviceList.size()<1){
						this.gwShare_msg = gwDeviceBio.getMsg();
					}
					return "shareList";
				}
			}
		}

	}
	
	/**
	 * 山西联通
	 * 查询设备（带列表）
	 * @return
	 */
	public String queryDeviceListForSxlt() {
		logger.debug("queryDeviceListForSxlt=>queryDeviceListForSxlt()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if("3".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}
			this.deviceList = gwDeviceBio.getDeviceListForHblt(areaId, gwShare_queryType, gwShare_cityId, gwShare_fileName);
			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = gwDeviceBio.getMsg();
			}
			total = deviceList == null?0:this.deviceList.size();
			return "shareListForSxlt";
			
		}else{
			if(null!=gwShare_queryType){
				gwShare_queryType.trim();
			}
			if(null!=gwShare_cityId){
				gwShare_cityId.trim();
			}else{
				this.gwShare_cityId = curUser.getCityId();
			}
			
			if(null!=gwShare_queryParam){
				gwShare_queryParam.trim();
			}
			if(null!=gwShare_queryField){
				gwShare_queryField.trim();
			}
			if(null!=gwShare_onlineStatus){
				gwShare_onlineStatus.trim();
			}
			if(null!=gwShare_vendorId){
				gwShare_vendorId.trim();
			}
			if(null!=gwShare_deviceModelId){
				gwShare_deviceModelId.trim();
			}
			if(null!=gwShare_devicetypeId){
				gwShare_devicetypeId.trim();
			}
			if(null!=gwShare_bindType){
				gwShare_bindType.trim();
			}
			if(null!=gwShare_deviceSerialnumber){
				gwShare_deviceSerialnumber.trim();
			}
			
			total = gwDeviceBio.getDeviceListCount(areaId, gwShare_queryType,
					gwShare_queryParam, gwShare_queryField, gwShare_cityId,
					gwShare_onlineStatus, gwShare_vendorId,
					gwShare_deviceModelId, gwShare_devicetypeId,
					gwShare_bindType, gwShare_deviceSerialnumber);
			
			//如果页面设置了，则强制查询个数记录
			//logger.error("===========gwShare_RecordNum:{}......",gwShare_RecordNum);
			if(StringUtil.getIntegerValue(gwShare_RecordNum) > 0)
			{
				num_splitPage = StringUtil.getIntegerValue(gwShare_RecordNum);
				this.deviceList = gwDeviceBio.getDeviceList(curPage_splitPage,num_splitPage,
						areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
						gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,Integer.parseInt(gwShare_OrderByUpdateDate),null);
				
				if(null == this.deviceList || this.deviceList.size() < 1){
					this.gwShare_msg = gwDeviceBio.getMsg();
					return "shareListForSxlt";
				}
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage; 
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				return "shareListForSxlt";
			}else{
				this.deviceList = gwDeviceBio.getDeviceList(curPage_splitPage,num_splitPage,
						areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
						gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,Integer.parseInt(gwShare_OrderByUpdateDate),null);
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage;
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				if(null==this.deviceList || this.deviceList.size()<1){
					this.gwShare_msg = gwDeviceBio.getMsg();
				}
				return "shareListForSxlt";
			}
		}
		
	}
	
	
	/**
	 * 查询设备（带多选列表）
	 * 
	 * @return
	 */
	public String queryDeviceListNew() {
		logger.debug("GwDeviceQueryACT=>getDeviceList()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if("3".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}
			this.deviceList = gwDeviceBio.getDeviceListForBlackList(areaId, gwShare_queryType, gwShare_cityId, gwShare_fileName);
			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = gwDeviceBio.getMsg();
			}
			total = deviceList == null?0:this.deviceList.size();
			return "shareList0";

		}else{
			if(null!=gwShare_queryType){
				gwShare_queryType.trim();
			}
			if(null!=gwShare_nextCityId && !"-1".equals(gwShare_nextCityId) && !"null".equals(gwShare_nextCityId)){
				this.gwShare_cityId = gwShare_nextCityId.trim();
			}
			else{
				if(null!=gwShare_cityId){
					gwShare_cityId.trim();
				}else{
					this.gwShare_cityId = curUser.getCityId();
				}
			}
			
			if(null!=gwShare_queryParam){
				gwShare_queryParam.trim();
			}
			if(null!=gwShare_queryField){
				gwShare_queryField.trim();
			}
			if(null!=gwShare_onlineStatus){
				gwShare_onlineStatus.trim();
			}
			if(null!=gwShare_vendorId){
				gwShare_vendorId.trim();
			}
			if(null!=gwShare_deviceModelId){
				gwShare_deviceModelId.trim();
			}
			if(null!=gwShare_devicetypeId){
				gwShare_devicetypeId.trim();
			}
			if(null!=gwShare_bindType){
				gwShare_bindType.trim();
			}
			if(null!=gwShare_deviceSerialnumber){
				gwShare_deviceSerialnumber.trim();
			}
			
			if(Global.XJDX.equals(Global.instAreaShortName) && "2".equals(gwShare_queryType)){
				if(null != gwShare_hardwareVersion){
					gwShare_hardwareVersion.trim();
				}
				if(null != gwShare_belong){
					gwShare_belong.trim();
				}
				//新疆添加硬件版本和平台，由于涉及的接口有很多地方调用，所以针对新疆在此拼接
				if((!StringUtil.IsEmpty(gwShare_hardwareVersion) && !"-1".equals(gwShare_hardwareVersion)) || 
						!StringUtil.IsEmpty(gwShare_belong) && !"-1".equals(gwShare_belong)){
					gwShare_deviceSerialnumber = gwShare_deviceSerialnumber+":" + gwShare_hardwareVersion +":"+gwShare_belong;
				}
				
				logger.warn("xj_dx gwShare_deviceSerialnumber => "+gwShare_deviceSerialnumber);
				logger.warn("xj_dx querytype => "+gwShare_queryType);
			}
			
			total = gwDeviceBio.getDeviceListCount(areaId, gwShare_queryType,
						gwShare_queryParam, gwShare_queryField, gwShare_cityId,
						gwShare_onlineStatus, gwShare_vendorId,
						gwShare_deviceModelId, gwShare_devicetypeId,
						gwShare_bindType, gwShare_deviceSerialnumber);
			
			if(Integer.parseInt(gwShare_RecordNum) > 0)
			{
				num_splitPage = Integer.parseInt(gwShare_RecordNum);
				if(total == 0){
					return "shareList4"; 
				}
				this.deviceList = gwDeviceBio.getDeviceList(curPage_splitPage,num_splitPage,
						areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
						gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,Integer.parseInt(gwShare_OrderByUpdateDate),null);
				
				if(null == this.deviceList || this.deviceList.size() < 1){
					this.gwShare_msg = gwDeviceBio.getMsg();
					return "shareList4";
 				}
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage; 
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				return "shareList4";
			}else{
				if(total <= 50){
					this.deviceList = gwDeviceBio.getDeviceList(areaId, gwShare_queryType,
							gwShare_queryParam, gwShare_queryField,	gwShare_cityId, 
							gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
							gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,null);
					if(null==this.deviceList || this.deviceList.size()<1){
						this.gwShare_msg = gwDeviceBio.getMsg();
					}
					return "shareList0"; 
				}else{
					this.deviceList = gwDeviceBio.getDeviceList(curPage_splitPage,num_splitPage,
							areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
							gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
							gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,Integer.parseInt(gwShare_OrderByUpdateDate),null);
					if (total % num_splitPage == 0) {
						maxPage_splitPage = total / num_splitPage;
					} else {
						maxPage_splitPage = total / num_splitPage + 1;
					}
					if(null==this.deviceList || this.deviceList.size()<1){
						this.gwShare_msg = gwDeviceBio.getMsg();
					}
					return "shareList4";
				}
			}
		}

	}
	
	
	/**
	 * 机顶盒批量配置专用查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryDeviceListByStb() {
		logger.debug("GwDeviceQueryACT=>getDeviceList()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if("3".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}
			this.deviceList = gwDeviceBio.getDeviceListForXjdx(areaId, gwShare_queryType, gwShare_cityId, gwShare_fileName);
			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = gwDeviceBio.getMsg();
			}
			total = deviceList == null?0:this.deviceList.size();
			if(LipossGlobals.inArea(Global.SDLT)){
				gwShare_matchSQL=gwDeviceBio.queryImportSQL(areaId, gwShare_cityId, gwShare_fileName);
				return "shareList2";
			}
			logger.warn("gwShare_matchSQL：{}",gwShare_matchSQL);
			return "shareList0";

		}else{
			if(null!=gwShare_queryType){
				gwShare_queryType.trim();
			}
			if(null!=gwShare_cityId){
				gwShare_cityId.trim();
			}else{
				this.gwShare_cityId = curUser.getCityId();
			}
			
			if(null!=gwShare_queryParam){
				gwShare_queryParam.trim();
			}
			if(null!=gwShare_queryField){
				gwShare_queryField.trim();
			}
			if(null!=gwShare_onlineStatus){
				gwShare_onlineStatus.trim();
			}
			if(null!=gwShare_vendorId){
				gwShare_vendorId.trim();
			}
			if(null!=gwShare_deviceModelId){
				gwShare_deviceModelId.trim();
			}
			if(null!=gwShare_devicetypeId){
				gwShare_devicetypeId.trim();
			}
			if(null!=gwShare_bindType){
				gwShare_bindType.trim();
			}
			if(null!=gwShare_deviceSerialnumber){
				gwShare_deviceSerialnumber.trim();
			}
			if(null!=gwShare_platform){
				gwShare_platform.trim();
			}
			if(null!=gwShare_IPType){
				gwShare_IPType.trim();
			} 
			total = gwDeviceBio.getDeviceListCountByStb(areaId, gwShare_queryType,
						gwShare_queryParam, gwShare_queryField, gwShare_cityId,
						gwShare_onlineStatus, gwShare_vendorId,
						gwShare_deviceModelId, gwShare_devicetypeId,
						gwShare_bindType, gwShare_deviceSerialnumber,gwShare_platform,gwShare_IPType);
			
			//如果页面设置了，则强制查询个数记录
			if(Integer.parseInt(gwShare_RecordNum) > 0)
			{
				num_splitPage = Integer.parseInt(gwShare_RecordNum);
				//total = 60; yaoli 20180529  此处注释，因为没怎么用到
				if(total == 0){
					return "shareList2"; 
				}
				this.deviceList = gwDeviceBio.getDeviceListByStb(curPage_splitPage,num_splitPage,
						areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
						gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,Integer.parseInt(gwShare_OrderByUpdateDate),gwShare_platform,null);
				
				if(null == this.deviceList || this.deviceList.size() < 1){
					this.gwShare_msg = gwDeviceBio.getMsg();
					return "shareList2";
 				}
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage; 
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				return "shareList2";
			}else{
				if(total <= 50){
					this.deviceList = gwDeviceBio.getDeviceList(areaId, gwShare_queryType,
							gwShare_queryParam, gwShare_queryField,	gwShare_cityId, 
							gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
							gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,null);
					if(null==this.deviceList || this.deviceList.size()<1){
						this.gwShare_msg = gwDeviceBio.getMsg();
					}
					return "shareList0"; 
				}else{
					this.deviceList = gwDeviceBio.getDeviceList(curPage_splitPage,num_splitPage,
							areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
							gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
							gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,Integer.parseInt(gwShare_OrderByUpdateDate),null);
					if (total % num_splitPage == 0) {
						maxPage_splitPage = total / num_splitPage;
					} else {
						maxPage_splitPage = total / num_splitPage + 1;
					}
					if(null==this.deviceList || this.deviceList.size()<1){
						this.gwShare_msg = gwDeviceBio.getMsg();
					}
					return "shareList2";
				}
			}
		}

	}
	
	/**
	 * 机顶盒批量配置专用查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryDeviceList4SoftUp() {
		logger.debug("GwDeviceQueryACT=>getDeviceList()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		
			if(null!=gwShare_queryType){
				gwShare_queryType.trim();
			}
			if(null!=gwShare_cityId){
				gwShare_cityId.trim();
			}else{
				this.gwShare_cityId = curUser.getCityId();
			}
			
			if(null!=gwShare_queryParam){
				gwShare_queryParam.trim();
			}
			if(null!=gwShare_queryField){
				gwShare_queryField.trim();
			}
			if(null!=gwShare_onlineStatus){
				gwShare_onlineStatus.trim();
			}
			if(null!=gwShare_vendorId){
				gwShare_vendorId.trim();
			}
			if(null!=gwShare_deviceModelId){
				gwShare_deviceModelId.trim();
			}
			if(null!=gwShare_devicetypeId){
				gwShare_devicetypeId.trim();
			}
			if(null!=gwShare_bindType){
				gwShare_bindType.trim();
			}
			if(null!=gwShare_deviceSerialnumber){
				gwShare_deviceSerialnumber.trim();
			}
			if(null!=gwShare_platform){
				gwShare_platform.trim();
			}
			//江西itv新增根据ip端查询
			if(null!=gwShare_IPType){
				gwShare_IPType.trim();
			} 
			logger.warn("ACT:::gwShare_IPType"+gwShare_IPType);
			total = gwDeviceBio.getDeviceListCountByStb(areaId, gwShare_queryType,
						gwShare_queryParam, gwShare_queryField, gwShare_cityId,
						gwShare_onlineStatus, gwShare_vendorId,
						gwShare_deviceModelId, gwShare_devicetypeId,
						gwShare_bindType, gwShare_deviceSerialnumber,gwShare_platform,gwShare_IPType);
			
			if(Integer.parseInt(gwShare_RecordNum) > 0)
			{
				num_splitPage = Integer.parseInt(gwShare_RecordNum);
				if(total == 0){
					return "shareList3"; 
				}
				this.deviceList = gwDeviceBio.getDeviceListByStb(curPage_splitPage,num_splitPage,
						areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
						gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,Integer.parseInt(gwShare_OrderByUpdateDate),gwShare_platform,gwShare_IPType);
				
				if(null == this.deviceList || this.deviceList.size() < 1){
					this.gwShare_msg = gwDeviceBio.getMsg();
					return "shareList3";
 				}
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage; 
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				return "shareList3";
			}else{
				if(total <= 50){
					this.deviceList = gwDeviceBio.getDeviceList(areaId, gwShare_queryType,
							gwShare_queryParam, gwShare_queryField,	gwShare_cityId, 
							gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
							gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,gwShare_IPType);
					if(null==this.deviceList || this.deviceList.size()<1){
						this.gwShare_msg = gwDeviceBio.getMsg();
					}
					return "shareList0"; 
				}else{
					this.deviceList = gwDeviceBio.getDeviceList(curPage_splitPage,num_splitPage,
							areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
							gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
							gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,Integer.parseInt(gwShare_OrderByUpdateDate),gwShare_IPType);
					if (total % num_splitPage == 0) {
						maxPage_splitPage = total / num_splitPage;
					} else {
						maxPage_splitPage = total / num_splitPage + 1;
					}
					if(null==this.deviceList || this.deviceList.size()<1){
						this.gwShare_msg = gwDeviceBio.getMsg();
					}
					return "shareList3";
				}
			}
		}
	/*
	 * 
	 * 
	 */
	public String queryDeviceListSQL() {
		logger.debug("GwDeviceQueryACT=>queryDeviceListSQL()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		
			if(null!=gwShare_queryType){
				gwShare_queryType.trim();
			}
			if(null!=gwShare_nextCityId && !"-1".equals(gwShare_nextCityId) && !"null".equals(gwShare_nextCityId)){
				this.gwShare_cityId = gwShare_nextCityId.trim();
			}
			else{
				if(null!=gwShare_cityId){
					gwShare_cityId.trim();
				}else{
					this.gwShare_cityId = curUser.getCityId();
				}
			}
			
			if(null!=gwShare_queryParam){
				gwShare_queryParam.trim();
			}
			if(null!=gwShare_queryField){
				gwShare_queryField.trim();
			}
			if(null!=gwShare_onlineStatus){
				gwShare_onlineStatus.trim();
			}
			if(null!=gwShare_vendorId){
				gwShare_vendorId.trim();
			}
			if(null!=gwShare_deviceModelId){
				gwShare_deviceModelId.trim();
			}
			if(null!=gwShare_devicetypeId){
				gwShare_devicetypeId.trim();
			}
			if(null!=gwShare_bindType){
				gwShare_bindType.trim();
			}
			if(null!=gwShare_deviceSerialnumber){
				gwShare_deviceSerialnumber.trim();
			}
			if(null!=gwShare_platform){
				gwShare_platform.trim();
			} 
			ajax = gwDeviceBio.getDeviceListSQL(curPage_splitPage,num_splitPage,
					areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
					gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
					gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,Integer.parseInt(gwShare_OrderByUpdateDate),gwShare_platform);
			
		return "ajax";

	}
	
	
	
	/**
	 * 分页查询
	 * @return
	 * @throws Exception
	 */
	public String goPage() throws Exception {
		logger.debug("GwDeviceQueryACT=>getDeviceList()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_queryParam){
			gwShare_queryParam.trim();
		}
		if(null!=gwShare_queryField){
			gwShare_queryField.trim();
		}
		if(null!=gwShare_cityId){
			gwShare_cityId.trim();
		}
		if(null!=gwShare_onlineStatus){
			gwShare_onlineStatus.trim();
		}
		if(null!=gwShare_vendorId){
			gwShare_vendorId.trim();
		}
		if(null!=gwShare_deviceModelId){
			gwShare_deviceModelId.trim();
		}
		if(null!=gwShare_devicetypeId){
			gwShare_devicetypeId.trim();
		}
		if(null!=gwShare_bindType){
			gwShare_bindType.trim();
		}
		if(null!=gwShare_deviceSerialnumber){
			gwShare_deviceSerialnumber.trim();
		}
		long areaId = curUser.getAreaId();		
		this.deviceList = gwDeviceBio.getDeviceList(curPage_splitPage,num_splitPage,
				areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
				gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
				gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,0,null);
		total = gwDeviceBio.getDeviceListCount(areaId, gwShare_queryType,
				gwShare_queryParam, gwShare_queryField,gwShare_cityId, gwShare_onlineStatus,
				gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId, 
				gwShare_bindType, gwShare_deviceSerialnumber);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "shareList";
	}
	
	/**
	 * 山西联通 TraceRoute
	 * 分页查询 
	 * @return
	 * @throws Exception
	 */
	public String goPageForStbDeviceTraceRouteTest() throws Exception {
		logger.debug("goPageForStbDeviceTraceRouteTest=>goPageForStbDeviceTraceRouteTest()");
		this.IpMap = pingBio.queryIpMap();
		goPageForSxlt();
		return "stbDeviceTraceRouteTest";
	}
	
	/**
	 * 山西联通 Ping诊断
	 * 分页查询 
	 * @return
	 * @throws Exception
	 */
	public String goPageForStbDevicePingTest() throws Exception {
		logger.debug("goPageForStbDevicePingTest=>goPageForStbDevicePingTest()");
		this.IpMap = pingBio.queryIpMap();
		goPageForSxlt();
		return "stbDevicePingTest";
	}
	
	/**
	 * 山西联通 手工业务下发
	 * 分页查询 
	 * @return
	 * @throws Exception
	 */
	public String goPageForStbServiceDone() throws Exception {
		logger.debug("goPageForStbServiceDone=>goPageForStbServiceDone()");
		goPageForSxlt();
		return "stbServiceDone";
	}
	
	/**
	 * 山西联通 机顶盒手工解绑
	 * 分页查询 
	 * @return
	 * @throws Exception
	 */
	public String goPageForStbRelease() throws Exception {
		logger.debug("goPageForStbRelease=>goPageForStbRelease()");
		goPageForSxlt();
		return "stbRelease";
	}
	
	/**
	 * 山西联通 机顶盒手工绑定
	 * 分页查询 
	 * @return
	 * @throws Exception
	 */
	public String goPageForStbInst() throws Exception {
		logger.debug("goPageForStbInst=>goPageForStbInst()");
		goPageForSxlt();
		return "stbInst";
	}
	
	/**
	 * 山西联通 页面跳转
	 * @throws Exception
	 */
	public void goPageForSxlt() throws Exception  {
		logger.debug("goPageForSxlt=>goPageForSxlt()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_queryParam){
			gwShare_queryParam.trim();
		}
		if(null!=gwShare_queryField){
			gwShare_queryField.trim();
		}
		if(null!=gwShare_cityId){
			gwShare_cityId.trim();
		}
		if(null!=gwShare_onlineStatus){
			gwShare_onlineStatus.trim();
		}
		if(null!=gwShare_vendorId){
			gwShare_vendorId.trim();
		}
		if(null!=gwShare_deviceModelId){
			gwShare_deviceModelId.trim();
		}
		if(null!=gwShare_devicetypeId){
			gwShare_devicetypeId.trim();
		}
		if(null!=gwShare_bindType){
			gwShare_bindType.trim();
		}
		if(null!=gwShare_deviceSerialnumber){
			gwShare_deviceSerialnumber.trim();
		}
		long areaId = curUser.getAreaId();		
		this.deviceList = gwDeviceBio.getDeviceList(curPage_splitPage,num_splitPage,
				areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
				gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
				gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,0,null);
		total = gwDeviceBio.getDeviceListCount(areaId, gwShare_queryType,
				gwShare_queryParam, gwShare_queryField,gwShare_cityId, gwShare_onlineStatus,
				gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId, 
				gwShare_bindType, gwShare_deviceSerialnumber);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		isGoPage = "1";
	}
	
	/**
	 * 机顶盒批量配置专用分页查询
	 * @return
	 * @throws Exception
	 */
	public String goPageByStb() throws Exception {
		logger.debug("GwDeviceQueryACT=>getDeviceList()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_platform){
			gwShare_platform.trim();
		}
		if(null!=gwShare_queryParam){
			gwShare_queryParam.trim();
		}
		if(null!=gwShare_queryField){
			gwShare_queryField.trim();
		}
		if(null!=gwShare_cityId){
			gwShare_cityId.trim();
		}
		if(null!=gwShare_onlineStatus){
			gwShare_onlineStatus.trim();
		}
		if(null!=gwShare_vendorId){
			gwShare_vendorId.trim();
		}
		if(null!=gwShare_deviceModelId){
			gwShare_deviceModelId.trim();
		}
		if(null!=gwShare_devicetypeId){
			gwShare_devicetypeId.trim();
		}
		if(null!=gwShare_bindType){
			gwShare_bindType.trim();
		}
		if(null!=gwShare_deviceSerialnumber){
			gwShare_deviceSerialnumber.trim();
		}
		if(null!=gwShare_IPType){
			gwShare_IPType.trim();
		}
		long areaId = curUser.getAreaId();
		this.deviceList = gwDeviceBio.getDeviceListByStb(curPage_splitPage,num_splitPage,
				areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
				gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
				gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,0,gwShare_platform,gwShare_IPType);
		total = gwDeviceBio.getDeviceListCountByStb(areaId, gwShare_queryType,
				gwShare_queryParam, gwShare_queryField,gwShare_cityId, gwShare_onlineStatus,
				gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId, 
				gwShare_bindType, gwShare_deviceSerialnumber,gwShare_platform,gwShare_IPType);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "shareList2";
	}
	
	/**
	 * @return the ajax
	 */
	public String getAjax() {
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	/**
	 * @return the gwDeviceBio
	 */
	public ShareDeviceQueryBIO getGwDeviceBio() {
		return gwDeviceBio;
	}

	/**
	 * @param gwDeviceBio
	 *            the gwDeviceBio to set
	 */
	public void setGwDeviceBio(ShareDeviceQueryBIO gwDeviceBio) {
		this.gwDeviceBio = gwDeviceBio;
	}

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	/**
	 * @param deviceList the deviceList to set
	 */
	public void setDeviceList(List deviceList) {
		this.deviceList = deviceList;
	}

	/**
	 * @return the deviceList
	 */
	public List getDeviceList() {
		return deviceList;
	}

	/**
	 * @return the deviceDetailMap
	 */
	public Map getDeviceDetailMap() {
		return deviceDetailMap;
	}

	/**
	 * @param deviceDetailMap the deviceDetailMap to set
	 */
	public void setDeviceDetailMap(Map deviceDetailMap) {
		this.deviceDetailMap = deviceDetailMap;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the gwShare_bindType
	 */
	public String getGwShare_bindType() {
		return gwShare_bindType;
	}

	/**
	 * @param gwShare_bindType the gwShare_bindType to set
	 */
	public void setGwShare_bindType(String gwShare_bindType) {
		this.gwShare_bindType = gwShare_bindType;
	}

	/**
	 * @return the gwShare_cityId
	 */
	public String getGwShare_cityId() {
		return gwShare_cityId;
	}

	/**
	 * @param gwShare_cityId the gwShare_cityId to set
	 */
	public void setGwShare_cityId(String gwShare_cityId) {
		this.gwShare_cityId = gwShare_cityId;
	}

	/**
	 * @return the gwShare_deviceModelId
	 */
	public String getGwShare_deviceModelId() {
		return gwShare_deviceModelId;
	}

	/**
	 * @param gwShare_deviceModelId the gwShare_deviceModelId to set
	 */
	public void setGwShare_deviceModelId(String gwShare_deviceModelId) {
		this.gwShare_deviceModelId = gwShare_deviceModelId;
	}

	/**
	 * @return the gwShare_deviceSerialnumber
	 */
	public String getGwShare_deviceSerialnumber() {
		return gwShare_deviceSerialnumber;
	}

	/**
	 * @param gwShare_deviceSerialnumber the gwShare_deviceSerialnumber to set
	 */
	public void setGwShare_deviceSerialnumber(String gwShare_deviceSerialnumber) {
		this.gwShare_deviceSerialnumber = gwShare_deviceSerialnumber;
	}

	/**
	 * @return the gwShare_devicetypeId
	 */
	public String getGwShare_devicetypeId() {
		return gwShare_devicetypeId;
	}

	/**
	 * @param gwShare_devicetypeId the gwShare_devicetypeId to set
	 */
	public void setGwShare_devicetypeId(String gwShare_devicetypeId) {
		this.gwShare_devicetypeId = gwShare_devicetypeId;
	}

	/**
	 * @return the gwShare_onlineStatus
	 */
	public String getGwShare_onlineStatus() {
		return gwShare_onlineStatus;
	}

	/**
	 * @param gwShare_onlineStatus the gwShare_onlineStatus to set
	 */
	public void setGwShare_onlineStatus(String gwShare_onlineStatus) {
		this.gwShare_onlineStatus = gwShare_onlineStatus;
	}

	/**
	 * @return the gwShare_queryField
	 */
	public String getGwShare_queryField() {
		return gwShare_queryField;
	}

	/**
	 * @param gwShare_queryField the gwShare_queryField to set
	 */
	public void setGwShare_queryField(String gwShare_queryField) {
		this.gwShare_queryField = gwShare_queryField;
	}

	/**
	 * @return the gwShare_queryParam
	 */
	public String getGwShare_queryParam() {
		return gwShare_queryParam;
	}

	/**
	 * @param gwShare_queryParam the gwShare_queryParam to set
	 */
	public void setGwShare_queryParam(String gwShare_queryParam) {
		this.gwShare_queryParam = gwShare_queryParam;
	}

	/**
	 * @return the gwShare_queryResultType
	 */
	public String getGwShare_queryResultType() {
		return gwShare_queryResultType;
	}

	/**
	 * @param gwShare_queryResultType the gwShare_queryResultType to set
	 */
	public void setGwShare_queryResultType(String gwShare_queryResultType) {
		this.gwShare_queryResultType = gwShare_queryResultType;
	}

	/**
	 * @return the gwShare_queryType
	 */
	public String getGwShare_queryType() {
		return gwShare_queryType;
	}

	/**
	 * @param gwShare_queryType the gwShare_queryType to set
	 */
	public void setGwShare_queryType(String gwShare_queryType) {
		this.gwShare_queryType = gwShare_queryType;
	}

	/**
	 * @return the gwShare_vendorId
	 */
	public String getGwShare_vendorId() {
		return gwShare_vendorId;
	}

	/**
	 * @param gwShare_vendorId the gwShare_vendorId to set
	 */
	public void setGwShare_vendorId(String gwShare_vendorId) {
		this.gwShare_vendorId = gwShare_vendorId;
	}
	
	public String getSearchTxt()
	{
		return searchTxt;
	}

	public String getSeparator()
	{
		return separator;
	}

	public void setSeparator(String separator)
	{
		this.separator = separator;

	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	public void setSearchTxt(String searchTxt)
	{
		try
		{
			this.searchTxt = URLDecoder.decode(searchTxt, "UTF-8");
			if (searchTxt != null)
			{
				this.searchTxt = this.searchTxt.trim();
			}
		} catch (UnsupportedEncodingException e)
		{
			logger.error(e.getMessage());
		}
	}

	/**
	 * @return the gwShare_fileName
	 */
	public String getGwShare_fileName() {
		return gwShare_fileName;
	}

	/**
	 * @param gwShare_fileName the gwShare_fileName to set
	 */
	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}

	/**
	 * @return the gwShare_msg
	 */
	public String getGwShare_msg() {
		return gwShare_msg;
	}

	/**
	 * @param gwShare_msg the gwShare_msg to set
	 */
	public void setGwShare_msg(String gwShare_msg) {
		this.gwShare_msg = gwShare_msg;
	}

	/**
	 * @return the gwShare_OrderByUpdateDate
	 */
	public String getGwShare_OrderByUpdateDate() {
		return gwShare_OrderByUpdateDate;
	}

	/**
	 * @param gwShare_OrderByUpdateDate the gwShare_OrderByUpdateDate to set
	 */
	public void setGwShare_OrderByUpdateDate(String gwShare_OrderByUpdateDate) {
		if(gwShare_OrderByUpdateDate == null || "".equals(gwShare_OrderByUpdateDate.trim()))
		{
			//默认设置不排序
			gwShare_OrderByUpdateDate = "0";
		}else
		{
			this.gwShare_OrderByUpdateDate = gwShare_OrderByUpdateDate.trim();
		}
	}
	
	/**
	 * @return the gwShare_RecordNum
	 */
	public String getGwShare_RecordNum() {
		return gwShare_RecordNum;
	}

	/**
	 * @param gwShare_RecordNum the gwShare_RecordNum to set
	 */
	public void setGwShare_RecordNum(String gwShare_RecordNum) {
		if(gwShare_RecordNum == null || "".equals(gwShare_RecordNum.trim()))
		{
			//默认查询全部
			this.gwShare_RecordNum = "0";
		}else
		{
			this.gwShare_RecordNum = gwShare_RecordNum.trim();
		}
	}

	public String getITV() {
		return ITV;
	}

	public void setITV(String iTV) {
		ITV = iTV;
	}

	public String getGwShare_hardwareVersion() {
		return gwShare_hardwareVersion;
	}

	public void setGwShare_hardwareVersion(String gwShare_hardwareVersion) {
		this.gwShare_hardwareVersion = gwShare_hardwareVersion;
	}

	public String getVersion_path() {
		return version_path;
	}

	public void setVersion_path(String version_path) {
		this.version_path = version_path;
	}

	public String getSpecial_path() {
		return special_path;
	}

	public void setSpecial_path(String special_path) {
		this.special_path = special_path;
	}	
	
	public String getGwShare_IPType()
	{
		return gwShare_IPType;
	}
	
	public void setGwShare_IPType(String gwShare_IPType)
	{
		this.gwShare_IPType = gwShare_IPType;
	}

	public String getGwShare_belong()
	{
		return gwShare_belong;
	}

	public void setGwShare_belong(String gwShare_belong)
	{
		this.gwShare_belong = gwShare_belong;
	}

	public String getGwShare_nextCityId() {
		return gwShare_nextCityId;
	}

	public void setGwShare_nextCityId(String gwShare_nextCityId) {
		this.gwShare_nextCityId = gwShare_nextCityId;
	}

	public String getIsGoPage() {
		return isGoPage;
	}

	public void setIsGoPage(String isGoPage) {
		this.isGoPage = isGoPage;
	}

	public PingInfoBIO getPingBio() {
		return pingBio;
	}

	public void setPingBio(PingInfoBIO pingBio) {
		this.pingBio = pingBio;
	}

	public List<Map<String, String>> getIpMap() {
		return IpMap;
	}

	public void setIpMap(List<Map<String, String>> ipMap) {
		IpMap = ipMap;
	}
	
	
}
