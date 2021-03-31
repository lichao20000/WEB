package com.linkage.module.gtms.stb.resource.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.StbBidChageBIO;
import com.linkage.module.gtms.stb.share.action.FileUploadAction;
import com.linkage.module.gwms.Global;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年6月11日
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */

@SuppressWarnings("rawtypes")
public class StbBidChageACT extends splitPageAction implements
		ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	// 开始时间
	private String startOpenDate = "";
	// 结束时间
	private String endOpenDate = "";
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(StbBidChageACT.class);

	StbBidChageBIO bio = null;
	
	public StbBidChageBIO getBio()
	{
		return bio;
	}

	
	public void setBio(StbBidChageBIO bio)
	{
		this.bio = bio;
	}

	private Map deviceDetailMap = null;
	private String deviceId = null;
	private String ajax = null;
	private String is_awifi;
	private List deviceList = null;
	private List<Map> awifi;
	private int account;
	private String gwShare_cityId = null;

	private String gwShare_nextCityId = null;

	private String gwShare_vendorId = null;

	private String gwShare_deviceModelId = null;

	private String gwShare_devicetypeId = null;

	HttpServletRequest request = null;

	private String gwShare_queryType = null;

	private String currencyType = null;

	private String gwShare_queryResultType = null;

	private String gwShare_queryParam = null;

	private String gwShare_queryField = null;

	private String gwShare_onlineStatus = null;

	private String gwShare_bindType = null;

	private String gwShare_deviceSerialnumber = null;

	private String gwShare_fileName = null;

	private String gwShare_msg = null;

	private String gwShare_matchSQL = null;	
	//LOID
	private String loid = null;	
	//宽带名称
	private String username = null;

	// 回传消息
	private String msg = null;
	private String mac;

	/**
	 * 查询文本
	 */
	private String searchTxt;
	/**
	 * 匹配文本的分隔符
	 */
	private String separator;

	// 查询结果的合计条数
	private int total = 0;

	private String gw_type;

	private String fileName;

	/** 1，代表软件升级需求 */
	private String isBatch;
	
	private String isNewNeed = "";
	

	// 查询条件
	private String importQueryField = "username";

	private String instArea = Global.instAreaShortName;

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
	public String getDeviceSn() throws Exception {

		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");

		ajax = bio.getDeviceSn(curUser.getCityId(), searchTxt,
				separator, gwShare_queryField);

		return "ajax";
	}

	/**
	 * 查询属地
	 * 
	 * @return
	 */
	public String getCityNextChild() {
		logger.debug("StbBidChageACT=>getCity()");
		if (null == this.gwShare_cityId) {

			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.gwShare_cityId = curUser.getCityId();
		}
		this.ajax = bio.getCity(gwShare_cityId);
		return "ajax";
	}

	/**
	 * 查询属地
	 * 
	 * @return
	 */
	public String getCityNextChildCore() {
		logger.debug("GwDeviceQueryACT=>getCity()");
		if (null == this.gwShare_cityId) {

			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.gwShare_cityId = curUser.getCityId();
		}
		this.ajax = bio.getCityCore(gwShare_cityId);
		return "ajax";
	}

	/**
	 * 查询设备厂商
	 * 
	 * @return
	 */
	public String getVendor() {
		logger.debug("GwDeviceQueryACT=>getVendor()");
		this.ajax = bio.getVendor();
		return "ajax";
	}

	public String getGwShare_nextCityId() {
		return gwShare_nextCityId;
	}

	public void setGwShare_nextCityId(String gwShare_nextCityId) {
		this.gwShare_nextCityId = gwShare_nextCityId;
	}

	/**
	 * 查询设备型号
	 * 
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel() {
		logger.debug("GwDeviceQueryACT=>getDeviceModel()");
		this.ajax = bio.getDeviceModel(gwShare_vendorId);
		return "ajax";
	}

	/**
	 * 查询设备版本
	 * 
	 * @return
	 */
	public String getDevicetype() {
		logger.debug("GwDeviceQueryACT=>getDevicetype()");
		this.ajax = bio.getDevicetype(gwShare_deviceModelId,
				isBatch);
		return "ajax";
	}

	/**
	 * 查询设备硬件版本
	 * 
	 * @return
	 */
	public String getDevicetypeHD() {
		logger.debug("GwDeviceQueryACT=>getDevicetypeHD()");
		this.ajax = bio.getDevicetype(gwShare_deviceModelId);
		return "ajax";
	}

	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryBatchParamNodeDeviceList() {
		logger.debug("GwDeviceQueryACT=>queryBatchParamNodeDeviceList()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if (null != gwShare_queryType) {
			gwShare_queryType.trim();
		}
		if (null != gwShare_cityId) {
			gwShare_cityId.trim();
		} else {
			this.gwShare_cityId = curUser.getCityId();
		}
		if ("3".equals(gwShare_queryType)) {
			if (null != gwShare_fileName) {
				gwShare_fileName.trim();
			}
			this.deviceList = bio.getDeviceList(gw_type, areaId,
					gwShare_queryType, gwShare_cityId, gwShare_fileName,true);
			if (null == this.deviceList || this.deviceList.size() < 1) {
				this.gwShare_msg = bio.getMsg();
			}
			total = deviceList == null ? 0 : this.deviceList.size();
			return "shareList0";

		}
		if ("4".equals(gwShare_queryType)) {
			if (null != gwShare_fileName) {
				gwShare_fileName.trim();
			}

			if (gwShare_fileName.length() < 4) {
				this.msg = "上传的文件名不正确！";
				return null;
			}
			String fileName_ = gwShare_fileName.substring(
					gwShare_fileName.length() - 3, gwShare_fileName.length());
			logger.debug("fileName_;{}", fileName_);
			if (!"xls".equals(fileName_) && !"txt".equals(fileName_)) {
				this.msg = "上传的文件格式不正确！";
				return null;
			}
			List<String> dataList = null;
			try {
				if ("txt".equals(fileName_)) {
					dataList = getParamNodeImportDataByTXT(gwShare_fileName);
				} else {
					dataList = getParamNodeImportDataByXLS(gwShare_fileName);
				}
			} catch (FileNotFoundException e) {
				logger.warn("{}文件没找到！", gwShare_fileName);
				this.msg = "文件没找到！";
				return null;
			} catch (IOException e) {
				logger.warn("{}文件解析出错！", gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			} catch (Exception e) {
				logger.warn("{}文件解析出错！", gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			}

			if (null == this.deviceList || this.deviceList.size() < 1) {
				this.gwShare_msg = this.msg;
			}
			this.total = dataList.size();
			ajax = String.valueOf(total);
			return "ajax";
		} else {
			if (null != gwShare_queryParam) {
				gwShare_queryParam.trim();
			}
			if (null != gwShare_queryField) {
				gwShare_queryField.trim();
			}
			if (null != gwShare_onlineStatus) {
				gwShare_onlineStatus.trim();
			}
			if (null != gwShare_vendorId) {
				gwShare_vendorId.trim();
			}
			if (null != gwShare_deviceModelId) {
				gwShare_deviceModelId.trim();
			}
			if (null != gwShare_devicetypeId) {
				gwShare_devicetypeId.trim();
			}
			if (null != gwShare_bindType) {
				gwShare_bindType.trim();
			}
			if (null != gwShare_deviceSerialnumber) {
				gwShare_deviceSerialnumber.trim();
			}
			total = bio.getDeviceListCount(gw_type, areaId,
					gwShare_queryType, gwShare_queryParam, gwShare_queryField,
					gwShare_cityId, gwShare_onlineStatus, gwShare_vendorId,
					gwShare_deviceModelId, gwShare_devicetypeId,
					gwShare_bindType, gwShare_deviceSerialnumber);

			if (total <= 50) {
				this.deviceList = bio.getDeviceList(gw_type, areaId,
						gwShare_queryType, gwShare_queryParam,
						gwShare_queryField, gwShare_cityId,
						gwShare_onlineStatus, gwShare_vendorId,
						gwShare_deviceModelId, gwShare_devicetypeId,
						gwShare_bindType, gwShare_deviceSerialnumber);
				if (null == this.deviceList || this.deviceList.size() < 1) {
					this.gwShare_msg = bio.getMsg();
				}
				return "shareList0";
			} else {
				this.deviceList = bio.getDeviceList(gw_type,
						curPage_splitPage, num_splitPage, areaId,
						gwShare_queryType, gwShare_queryParam,
						gwShare_queryField, gwShare_cityId,
						gwShare_onlineStatus, gwShare_vendorId,
						gwShare_deviceModelId, gwShare_devicetypeId,
						gwShare_bindType, gwShare_deviceSerialnumber);
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage;
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				if (null == this.deviceList || this.deviceList.size() < 1) {
					this.gwShare_msg = bio.getMsg();
				}
				return "shareList";
			}
		}
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
		try {
			if (null != gwShare_queryType) {
				gwShare_queryType.trim();
			}
			if (null != gwShare_nextCityId && !"-1".equals(gwShare_nextCityId)
					&& !"null".equals(gwShare_nextCityId)) {
				this.gwShare_cityId = gwShare_nextCityId.trim();
			} else {
				if (null != gwShare_cityId) {
					gwShare_cityId.trim();
				} else {
					this.gwShare_cityId = curUser.getCityId();
				}
			}

			if (null != gwShare_fileName) {
				gwShare_fileName.trim();
			}

			if (null == this.deviceList || this.deviceList.size() < 1) {
				this.gwShare_msg = bio.getMsg();
			}
			this.deviceList = bio.getDeviceList(gw_type, areaId,
					gwShare_queryType, gwShare_cityId, gwShare_fileName,true);
			this.ajax = "1";
		} catch (Exception e) {
			logger.warn("{}文件解析出错！", gwShare_fileName);
			this.msg = "文件解析出错！";
			this.ajax = "-1";
		}
		
		return "ajax";
	}

	
	public String getDeviceLists() {
		logger.warn("GwDeviceQueryACT=>getDeviceList()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		try {
			if (null != gwShare_queryType) {
				gwShare_queryType.trim();
			}
			if (null != gwShare_nextCityId && !"-1".equals(gwShare_nextCityId)
					&& !"null".equals(gwShare_nextCityId)) {
				this.gwShare_cityId = gwShare_nextCityId.trim();
			} else {
				if (null != gwShare_cityId) {
					gwShare_cityId.trim();
				} else {
					this.gwShare_cityId = curUser.getCityId();
				}
			}

			if (null != gwShare_fileName) {
				gwShare_fileName.trim();
			}

			if (null == this.deviceList || this.deviceList.size() < 1) {
				this.gwShare_msg = bio.getMsg();
			}
			this.deviceList = bio.getDeviceList(gw_type, areaId,
					gwShare_queryType, gwShare_cityId, gwShare_fileName,false);
			total = deviceList == null ? 0 : this.deviceList.size();
			this.deviceList = bio.getDeviceList1(curPage_splitPage,
					num_splitPage, gw_type, areaId, gwShare_cityId,
					gwShare_fileName);
			
			logger.warn("deviceList:"+deviceList);
			logger.warn("deviceList.size:"+deviceList.size());
			logger.warn("total:"+total);
			if (total % num_splitPage == 0) {
				maxPage_splitPage = total / num_splitPage;
			} else {
				maxPage_splitPage = total / num_splitPage + 1;
			}
			this.ajax = "1";
		} catch (Exception e) {
			e.printStackTrace();
			/*logger.warn("{}文件解析出错！", gwShare_fileName);
			this.msg = "文件解析出错！";
			this.ajax = "-1";*/
		}
		
		return "list";
	}
	
	public String queryDeviceExcel() {
		logger.debug("GwDeviceQueryACT=>getDeviceList()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if (null != gwShare_queryType) {
			gwShare_queryType.trim();
		}
		if (null != gwShare_nextCityId && !"-1".equals(gwShare_nextCityId)
				&& !"null".equals(gwShare_nextCityId)) {
			this.gwShare_cityId = gwShare_nextCityId.trim();
		} else {
			if (null != gwShare_cityId) {
				gwShare_cityId.trim();
			} else {
				this.gwShare_cityId = curUser.getCityId();
			}
		}

		if (null != gwShare_fileName) {
			gwShare_fileName.trim();
		}
		this.deviceList = bio.getDeviceList1(-1, num_splitPage,
				gw_type, areaId, gwShare_cityId, gwShare_fileName);
		String excelCol = "loid#username#old_vendor_name#old_devsn#old_hardwareversion#old_softwareversion#old_device_model"
				+ "#vendor_name#new_devsn#softwareversion#hardwareversion#device_model#unbinddate#binddate";

		String excelTitle = "loid#宽带账号#旧厂家#旧SN#旧软件版本#旧硬件版本#旧型号#新厂家#新SN#新软件版本#新硬件版本#新型号#解绑时间#绑定时间";
		if (Global.NXDX.equals(instArea)){
			 excelCol = "username#old_vendor_name#old_device_model#old_devsn#old_mac"
						+ "#vendor_name#device_model#new_devsn#new_mac#binddate";
			 excelTitle = "业务账号#旧厂家#旧型号#旧SN#旧mac地址#新厂家#新型号#新SN#新mac地址#绑定时间";	
		}
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "机顶盒更换记录导入查询";
		data = deviceList;
		return "excel";
	}

	/**
	 * 查询数量
	 */
	public String querynumber() {
		logger.debug("GwDeviceQueryACT=>querynumber()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if (null != gwShare_queryType) {
			gwShare_queryType.trim();
		}
		if (null != gwShare_cityId) {
			gwShare_cityId.trim();
		} else {
			this.gwShare_cityId = curUser.getCityId();
		}
		if ("3".equals(gwShare_queryType)) {
			if (null != gwShare_fileName) {
				gwShare_fileName.trim();
			}
			this.deviceList = bio.getnumber(gw_type, areaId,
					gwShare_queryType, gwShare_cityId, gwShare_fileName);
			if (null == this.deviceList || this.deviceList.size() < 1) {
				this.ajax = bio.getMsg();
			}
			total = deviceList.size();
			ajax = String.valueOf(total);
			return "ajax";
		}
		if ("4".equals(gwShare_queryType)) {
			if (null != gwShare_fileName) {
				gwShare_fileName.trim();
			}

			if (gwShare_fileName.length() < 4) {
				this.msg = "上传的文件名不正确！";
				return null;
			}
			String fileName_ = gwShare_fileName.substring(
					gwShare_fileName.length() - 3, gwShare_fileName.length());
			logger.debug("fileName_;{}", fileName_);
			if (!"xls".equals(fileName_) && !"txt".equals(fileName_)) {
				this.msg = "上传的文件格式不正确！";
				return null;
			}
			List<String> dataList = null;
			try {
				if ("txt".equals(fileName_)) {
					dataList = getImportDataByTXT(gwShare_fileName);
				} else {
					dataList = getImportDataByXLS(gwShare_fileName);
				}
			} catch (FileNotFoundException e) {
				logger.warn("{}文件没找到！", gwShare_fileName);
				this.msg = "文件没找到！";
				return null;
			} catch (IOException e) {
				logger.warn("{}文件解析出错！", gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			} catch (Exception e) {
				logger.warn("{}文件解析出错！", gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			}

			if (null == this.deviceList || this.deviceList.size() < 1) {
				this.gwShare_msg = this.msg;
			}
			this.total = dataList.size();
			ajax = String.valueOf(total);
			return "ajax";
		} else {
			if (null != gwShare_queryParam) {
				gwShare_queryParam.trim();
			}
			if (null != gwShare_queryField) {
				gwShare_queryField.trim();
			}
			if (null != gwShare_onlineStatus) {
				gwShare_onlineStatus.trim();
			}
			if (null != gwShare_vendorId) {
				gwShare_vendorId.trim();
			}
			if (null != gwShare_deviceModelId) {
				gwShare_deviceModelId.trim();
			}
			if (null != gwShare_devicetypeId) {
				gwShare_devicetypeId.trim();
			}
			if (null != gwShare_bindType) {
				gwShare_bindType.trim();
			}
			if (null != gwShare_deviceSerialnumber) {
				gwShare_deviceSerialnumber.trim();
			}
			total = bio.getDeviceListCount(gw_type, areaId,
					gwShare_queryType, gwShare_queryParam, gwShare_queryField,
					gwShare_cityId, gwShare_onlineStatus, gwShare_vendorId,
					gwShare_deviceModelId, gwShare_devicetypeId,
					gwShare_bindType, gwShare_deviceSerialnumber);

			ajax = String.valueOf(total);
			return "ajax";
		}
	}

	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryDeviceListForDoubleParam() {
		logger.debug("GwDeviceQueryACT=>queryDeviceListForDoubleParam()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if (null != gwShare_queryType) {
			gwShare_queryType.trim();
		}
		if (null != gwShare_cityId) {
			gwShare_cityId.trim();
		} else {
			this.gwShare_cityId = curUser.getCityId();
		}
		if ("3".equals(gwShare_queryType)) {
			if (null != gwShare_fileName) {
				gwShare_fileName.trim();
			}
			this.deviceList = bio
					.getDeviceListForDoubleParam(gw_type, areaId,
							gwShare_queryType, gwShare_cityId, gwShare_fileName);
			if (null == this.deviceList || this.deviceList.size() < 1) {
				this.gwShare_msg = bio.getMsg();
			}
			total = deviceList == null ? 0 : this.deviceList.size();
		}
		return "shareList0";
	}

	/**
	 * 查询设备（带列表）(无线专线批量开通查询)
	 * 
	 * @return
	 */
	public String queryDeviceList4Wireless() {
		logger.debug("GwDeviceQueryACT=>queryDeviceList4Wireless()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if (null != gwShare_queryType) {
			gwShare_queryType.trim();
		}
		if (null != gwShare_cityId) {
			gwShare_cityId.trim();
		} else {
			this.gwShare_cityId = curUser.getCityId();
		}
		if ("3".equals(gwShare_queryType)) {
			if (null != gwShare_fileName) {
				gwShare_fileName.trim();
			}
			if (null != currencyType) {
				currencyType.trim();
			}
			this.deviceList = bio.getDeviceList4Wireless(gw_type,
					areaId, gwShare_queryType, gwShare_cityId,
					gwShare_fileName, currencyType);
			if (null == this.deviceList || this.deviceList.size() < 1) {
				this.gwShare_msg = bio.getMsg();
			}
			total = deviceList == null ? 0 : this.deviceList.size();
			return "shareList0";

		}
		if ("4".equals(gwShare_queryType)) {
			if (null != gwShare_fileName) {
				gwShare_fileName.trim();
			}

			if (gwShare_fileName.length() < 4) {
				this.msg = "上传的文件名不正确！";
				return null;
			}
			String fileName_ = gwShare_fileName.substring(
					gwShare_fileName.length() - 3, gwShare_fileName.length());
			logger.debug("fileName_;{}", fileName_);
			if (!"xls".equals(fileName_) && !"txt".equals(fileName_)) {
				this.msg = "上传的文件格式不正确！";
				return null;
			}
			List<String> dataList = null;
			try {
				if ("txt".equals(fileName_)) {
					dataList = getImportDataByTXT(gwShare_fileName);
				} else {
					dataList = getImportDataByXLS(gwShare_fileName);
				}
			} catch (FileNotFoundException e) {
				logger.warn("{}文件没找到！", gwShare_fileName);
				this.msg = "文件没找到！";
				return null;
			} catch (IOException e) {
				logger.warn("{}文件解析出错！", gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			} catch (Exception e) {
				logger.warn("{}文件解析出错！", gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			}

			if (null == this.deviceList || this.deviceList.size() < 1) {
				this.gwShare_msg = this.msg;
			}
			this.total = dataList.size();
			ajax = String.valueOf(total);
			return "ajax";
		} else {
			if (null != gwShare_queryParam) {
				gwShare_queryParam.trim();
			}
			if (null != gwShare_queryField) {
				gwShare_queryField.trim();
			}
			if (null != gwShare_onlineStatus) {
				gwShare_onlineStatus.trim();
			}
			if (null != gwShare_vendorId) {
				gwShare_vendorId.trim();
			}
			if (null != gwShare_deviceModelId) {
				gwShare_deviceModelId.trim();
			}
			if (null != gwShare_devicetypeId) {
				gwShare_devicetypeId.trim();
			}
			if (null != gwShare_bindType) {
				gwShare_bindType.trim();
			}
			if (null != gwShare_deviceSerialnumber) {
				gwShare_deviceSerialnumber.trim();
			}
			total = bio.getDeviceListCount(gw_type, areaId,
					gwShare_queryType, gwShare_queryParam, gwShare_queryField,
					gwShare_cityId, gwShare_onlineStatus, gwShare_vendorId,
					gwShare_deviceModelId, gwShare_devicetypeId,
					gwShare_bindType, gwShare_deviceSerialnumber);

			if (total <= 50) {
				this.deviceList = bio.getDeviceList(gw_type, areaId,
						gwShare_queryType, gwShare_queryParam,
						gwShare_queryField, gwShare_cityId,
						gwShare_onlineStatus, gwShare_vendorId,
						gwShare_deviceModelId, gwShare_devicetypeId,
						gwShare_bindType, gwShare_deviceSerialnumber);
				if (null == this.deviceList || this.deviceList.size() < 1) {
					this.gwShare_msg = bio.getMsg();
				}
				return "shareList0";
			} else {
				this.deviceList = bio.getDeviceList(gw_type,
						curPage_splitPage, num_splitPage, areaId,
						gwShare_queryType, gwShare_queryParam,
						gwShare_queryField, gwShare_cityId,
						gwShare_onlineStatus, gwShare_vendorId,
						gwShare_deviceModelId, gwShare_devicetypeId,
						gwShare_bindType, gwShare_deviceSerialnumber);
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage;
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				if (null == this.deviceList || this.deviceList.size() < 1) {
					this.gwShare_msg = bio.getMsg();
				}
				return "shareList";
			}
		}
	}

	/**
	 * 查询isawifi
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryisawifi() {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if (null != gwShare_queryType) {
			gwShare_queryType.trim();
		}
		if (null != gwShare_queryParam) {
			gwShare_queryParam.trim();
		}
		if (null != gwShare_queryField) {
			gwShare_queryField.trim();
		}
		if (null != gwShare_cityId) {
			gwShare_cityId.trim();
		}
		if (null != gwShare_onlineStatus) {
			gwShare_onlineStatus.trim();
		}
		if (null != gwShare_vendorId) {
			gwShare_vendorId.trim();
		}
		if (null != gwShare_deviceModelId) {
			gwShare_deviceModelId.trim();
		}
		if (null != gwShare_devicetypeId) {
			gwShare_devicetypeId.trim();
		}
		if (null != gwShare_bindType) {
			gwShare_bindType.trim();
		}
		if (null != gwShare_deviceSerialnumber) {
			gwShare_deviceSerialnumber.trim();
		}
		this.awifi = bio.queryisawifi(gw_type, areaId,
				gwShare_queryType, gwShare_queryParam, gwShare_queryField,
				gwShare_cityId, gwShare_onlineStatus, gwShare_vendorId,
				gwShare_deviceModelId, gwShare_devicetypeId, gwShare_bindType,
				gwShare_deviceSerialnumber);
		this.account = bio.queryaccount_number(gw_type, areaId,
				gwShare_queryType, gwShare_queryParam, gwShare_queryField,
				gwShare_cityId, gwShare_onlineStatus, gwShare_vendorId,
				gwShare_deviceModelId, gwShare_devicetypeId, gwShare_bindType,
				gwShare_deviceSerialnumber);
		Map<String, String> map = new HashMap<String, String>();
		if (account != 0) {
			if (null != awifi && awifi.size() > 0) {
				map = awifi.get(0);
				String wifi = StringUtil.getStringValue(map.get("is_awifi"));
				if ((!StringUtil.IsEmpty(wifi)) && (!"2".equals(wifi))) {
					this.ajax = "0";
				} else {
					this.ajax = "1";
				}
			} else {
				this.ajax = "1";
			}
		} else {
			this.ajax = "2";
		}
		return "ajax";
	}

	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws FileNotFoundException
	 *             IOException
	 */
	public List<String> getImportDataByTXT(String fileName)
			throws FileNotFoundException, IOException {
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()
				+ fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if (null != line && "设备序列号".equals(line)) {
			this.importQueryField = "device_serialnumber";
		} else {
			this.importQueryField = "username";
		}
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if (!"".equals(line.trim())) {
				list.add(line.trim());
			}
		}
		in.close();
		in = null;

		return list;
	}

	/**
	 * 解析文件（xls格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 * 
	 */
	public List<String> getImportDataByXLS(String fileName)
			throws BiffException, IOException {
		logger.debug("getImportDataByXLS{}", fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		Sheet ws = null;
		// 总sheet数
		// int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++) {
			ws = wwb.getSheet(m);

			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			// int columeCount = ws.getColumns();

			if (null != ws.getCell(0, 0).getContents()) {
				String line = ws.getCell(0, 0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if (null != line && "设备序列号".equals(line)) {
					this.importQueryField = "device_serialnumber";
				} else {
					this.importQueryField = "username";
				}
			}

			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++) {
				String temp = ws.getCell(0, i).getContents();
				if (null != temp && !"".equals(temp)) {
					if (!"".equals(ws.getCell(0, i).getContents().trim())) {
						list.add(ws.getCell(0, i).getContents().trim());
					}
				}
			}
		}
		f = null;
		return list;
	}

	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws FileNotFoundException
	 *             IOException
	 */
	public List<String> getParamNodeImportDataByTXT(String fileName)
			throws FileNotFoundException, IOException {
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()
				+ fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if (null != line && "设备序列号".equals(line)) {
			this.importQueryField = "device_serialnumber";
		} else {
			this.importQueryField = "username";
		}
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if (!"".equals(line.trim())) {
				list.add(line.trim());
			}
		}
		in.close();
		in = null;

		return list;
	}

	/**
	 * 解析文件（xls格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 * 
	 */
	public List<String> getParamNodeImportDataByXLS(String fileName)
			throws BiffException, IOException {
		logger.debug("getImportDataByXLS{}", fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		Sheet ws = null;
		// 总sheet数
		// int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++) {
			ws = wwb.getSheet(m);

			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			// int columeCount = ws.getColumns();

			if (null != ws.getCell(0, 0).getContents()) {
				String line = ws.getCell(0, 0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if (null != line && "设备序列号".equals(line)) {
					this.importQueryField = "device_serialnumber";
				} else {
					this.importQueryField = "username";
				}
			}

			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++) {
				String temp = ws.getCell(0, i).getContents();
				if (null != temp && !"".equals(temp)) {
					if (!"".equals(ws.getCell(0, i).getContents().trim())) {
						list.add(ws.getCell(0, i).getContents().trim());
					}
				}
			}
		}
		f = null;
		return list;
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try {
			lipossHome = java.net.URLDecoder.decode(
					a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryDeviceListBySQL() {
		logger.debug("GwDeviceQueryACT=>queryDeviceListBySQL()");
		try {
			if (!bio.checkMatchSQL(gwShare_matchSQL)) {
				this.gwShare_msg = bio.getMsg();
				return "shareList0";
			}
			gwShare_matchSQL = gwShare_matchSQL.replace("[", "'");
			logger.warn("使用自定义SQL查询：" + gwShare_matchSQL);
			total = bio.getDeviceListCountBySQL(gwShare_matchSQL);

			if (total <= 50) {
				this.deviceList = bio
						.getDeviceListBySQL(gwShare_matchSQL);
				if (null == this.deviceList || this.deviceList.size() < 1) {
					this.gwShare_msg = bio.getMsg();
				}
				return "shareList0";
			} else {
				this.deviceList = bio.getDeviceListBySQL(
						curPage_splitPage, num_splitPage, gwShare_matchSQL);
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage;
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				if (null == this.deviceList || this.deviceList.size() < 1) {
					this.gwShare_msg = bio.getMsg();
				}
				return "shareList";
			}
		} catch (Exception e) {
			logger.error("queryDeviceListBySQL-->Exception:{}"
					+ new Object[] { e.getMessage() });
			return "shareList0";
		}
	}

	/**
	 * 获取查询SQL
	 * 
	 * @return
	 */
	public String queryDeviceListSQL() {
		logger.debug("GwDeviceQueryACT=>queryDeviceListSQL()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if (null != gwShare_queryType) {
			gwShare_queryType.trim();
		}

		if (null != gwShare_nextCityId && !"-1".equals(gwShare_nextCityId)
				&& !"null".equals(gwShare_nextCityId)) {
			this.gwShare_cityId = gwShare_nextCityId.trim();
		} else {
			if (null != gwShare_cityId) {
				gwShare_cityId.trim();
			} else {
				this.gwShare_cityId = curUser.getCityId();
			}
		}

		if (null != gwShare_queryParam) {
			gwShare_queryParam.trim();
		}
		if (null != gwShare_queryField) {
			gwShare_queryField.trim();
		}
		if (null != gwShare_onlineStatus) {
			gwShare_onlineStatus.trim();
		}
		if (null != gwShare_vendorId) {
			gwShare_vendorId.trim();
		}
		if (null != gwShare_deviceModelId) {
			gwShare_deviceModelId.trim();
		}
		if (null != gwShare_devicetypeId) {
			gwShare_devicetypeId.trim();
		}
		if (null != gwShare_bindType) {
			gwShare_bindType.trim();
		}
		if (null != gwShare_deviceSerialnumber) {
			gwShare_deviceSerialnumber.trim();
		}
		ajax = bio.getDeviceListSQL(gw_type, areaId, gwShare_queryType,
				gwShare_queryParam, gwShare_queryField, gwShare_cityId,
				gwShare_onlineStatus, gwShare_vendorId, gwShare_deviceModelId,
				gwShare_devicetypeId, gwShare_bindType,
				gwShare_deviceSerialnumber);

		return "ajax";
	}

	public String queryTabBindList() {
		this.setTime();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		
		logger.warn("isNewNeed:"+isNewNeed);
		if ((null != mac && !"".equals(mac) && !"-1".equals(mac))
				|| (null != username && !"".equals(username) && !"-1".equals(username)))
		{
			// 如果loid或是username不为空，则为简单查询，不考虑别的查询条件
			logger.warn("机顶盒更换数据导出--简单查询");
			deviceList = bio.newDeviceQueryInfo4Simple(mac, username,
					curPage_splitPage, num_splitPage);
			total = deviceList == null ? 0 : this.deviceList.size();
			maxPage_splitPage = bio.countNewDeviceQueryInfo4Simple(mac,
					username, curPage_splitPage, num_splitPage);
			return "list";
		}
		if (null != gwShare_queryType) {
			gwShare_queryType.trim();
		}
		if (null != gwShare_nextCityId && !"-1".equals(gwShare_nextCityId)
				&& !"null".equals(gwShare_nextCityId)) {
			this.gwShare_cityId = gwShare_nextCityId.trim();
		} else {
			if (null != gwShare_cityId) {
				gwShare_cityId.trim();
			} else {
				this.gwShare_cityId = curUser.getCityId();
			}
		}

		if (null != gwShare_queryParam) {
			gwShare_queryParam.trim();
		}
		if (null != gwShare_queryField) {
			gwShare_queryField.trim();
		}
		if (null != gwShare_vendorId) {
			gwShare_vendorId.trim();
		}
		if (null != gwShare_deviceModelId) {
			gwShare_deviceModelId.trim();
		}
		if (null != gwShare_devicetypeId) {
			gwShare_devicetypeId.trim();
		}
		deviceList = bio.NewDeviceQueryInfo(startOpenDate, endOpenDate,
				gw_type, areaId, gwShare_queryType, gwShare_queryParam,
				gwShare_queryField, gwShare_cityId, gwShare_vendorId,
				gwShare_deviceModelId, gwShare_devicetypeId, curPage_splitPage,
				num_splitPage,isNewNeed);
		total = deviceList == null ? 0 : this.deviceList.size();
		maxPage_splitPage = bio.countNewDeviceQueryInfo(startOpenDate,
				endOpenDate, gw_type, areaId, gwShare_queryType,
				gwShare_queryParam, gwShare_queryField, gwShare_cityId,
				gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId,
				curPage_splitPage, num_splitPage,isNewNeed);
		return "list";
	}

	public String NewDeviceQueryExcel() {
		this.setTime();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();

		if (null != gwShare_queryType) {
			gwShare_queryType.trim();
		}

		if (null != gwShare_nextCityId && !"-1".equals(gwShare_nextCityId)
				&& !"null".equals(gwShare_nextCityId)) {
			this.gwShare_cityId = gwShare_nextCityId.trim();
		} else {
			if (null != gwShare_cityId) {
				gwShare_cityId.trim();
			} else {
				this.gwShare_cityId = curUser.getCityId();
			}
		}

		if (null != gwShare_queryParam) {
			gwShare_queryParam.trim();
		}
		if (null != gwShare_queryField) {
			gwShare_queryField.trim();
		}
		if (null != gwShare_vendorId) {
			gwShare_vendorId.trim();
		}
		if (null != gwShare_deviceModelId) {
			gwShare_deviceModelId.trim();
		}
		if (null != gwShare_devicetypeId) {
			gwShare_devicetypeId.trim();
		}
		logger.warn("NewDeviceQueryExcel isNewNeed :"+isNewNeed);

		if ((null != mac && !"".equals(mac) && !"-1".equals(mac))
				|| (null != username && !"".equals(username) && !"-1".equals(username)))
		{
			// 如果loid或是username不为空，则为简单查询，不考虑别的查询条件
			logger.warn("机顶盒更换数据导出--简单查询");
			deviceList = bio.newDeviceQueryInfo4Simple(mac, username,
					curPage_splitPage, num_splitPage);
		}else{
			deviceList = bio.NewDeviceQueryExcel(startOpenDate,
					endOpenDate, gw_type, areaId, gwShare_queryType,
					gwShare_queryParam, gwShare_queryField, gwShare_cityId,
					gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId,isNewNeed);
		}
		
		String excelCol = "";
		if(!StringUtil.IsEmpty(isNewNeed) && "yes".equals(isNewNeed)){
			 excelCol = "loid#username#old_vendor_name#old_devsn#old_hardwareversion#old_softwareversion#old_device_model"
						+ "#vendor_name#new_devsn#softwareversion#hardwareversion#device_model#binddate";
		}else{
			 excelCol = "username#old_vendor_name#old_device_model#old_devsn#old_mac"
						+ "#vendor_name#device_model#new_devsn#new_mac#binddate";
		}
		
		String excelTitle = "";
		if(!StringUtil.IsEmpty(isNewNeed) && "yes".equals(isNewNeed)){
			excelTitle = "loid#宽带账号#旧厂家#旧SN#旧软件版本#旧硬件版本#旧型号#新厂家#新SN#新软件版本#新硬件版本#新型号#绑定时间";
		}else{
			excelTitle = "业务账号#旧厂家#旧型号#旧SN#旧mac地址#新厂家#新型号#新SN#新mac地址#绑定时间";
		}
		 
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "更换机顶盒记录导出";
		data = deviceList;
		return "excel";
	}

	/**
	 * 分页查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String goPage() throws Exception {
		logger.debug("GwDeviceQueryACT=>getDeviceList()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		if (null != gwShare_queryType) {
			gwShare_queryType.trim();
		}
		if (null != gwShare_queryParam) {
			gwShare_queryParam.trim();
		}
		if (null != gwShare_queryField) {
			gwShare_queryField.trim();
		}
		if (null != gwShare_cityId) {
			gwShare_cityId.trim();
		}
		if (null != gwShare_onlineStatus) {
			gwShare_onlineStatus.trim();
		}
		if (null != gwShare_vendorId) {
			gwShare_vendorId.trim();
		}
		if (null != gwShare_deviceModelId) {
			gwShare_deviceModelId.trim();
		}
		if (null != gwShare_devicetypeId) {
			gwShare_devicetypeId.trim();
		}
		if (null != gwShare_bindType) {
			gwShare_bindType.trim();
		}
		if (null != gwShare_deviceSerialnumber) {
			gwShare_deviceSerialnumber.trim();
		}
		long areaId = curUser.getAreaId();
		this.deviceList = bio.getDeviceList(gw_type, curPage_splitPage,
				num_splitPage, areaId, gwShare_queryType, gwShare_queryParam,
				gwShare_queryField, gwShare_cityId, gwShare_onlineStatus,
				gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId,
				gwShare_bindType, gwShare_deviceSerialnumber);
		total = bio.getDeviceListCount(gw_type, areaId,
				gwShare_queryType, gwShare_queryParam, gwShare_queryField,
				gwShare_cityId, gwShare_onlineStatus, gwShare_vendorId,
				gwShare_deviceModelId, gwShare_devicetypeId, gwShare_bindType,
				gwShare_deviceSerialnumber);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "shareList";
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


	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	/**
	 * @param deviceList
	 *            the deviceList to set
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
	 * @param deviceDetailMap
	 *            the deviceDetailMap to set
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
	 * @param deviceId
	 *            the deviceId to set
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
	 * @param gwShare_bindType
	 *            the gwShare_bindType to set
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
	 * @param gwShare_cityId
	 *            the gwShare_cityId to set
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
	 * @param gwShare_deviceModelId
	 *            the gwShare_deviceModelId to set
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
	 * @param gwShare_deviceSerialnumber
	 *            the gwShare_deviceSerialnumber to set
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
	 * @param gwShare_devicetypeId
	 *            the gwShare_devicetypeId to set
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
	 * @param gwShare_onlineStatus
	 *            the gwShare_onlineStatus to set
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
	 * @param gwShare_queryField
	 *            the gwShare_queryField to set
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
	 * @param gwShare_queryParam
	 *            the gwShare_queryParam to set
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
	 * @param gwShare_queryResultType
	 *            the gwShare_queryResultType to set
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
	 * @param gwShare_queryType
	 *            the gwShare_queryType to set
	 */
	public void setGwShare_queryType(String gwShare_queryType) {
		this.gwShare_queryType = gwShare_queryType;
	}

	/**
	 * @return the currencyType
	 */
	public String getCurrencyType() {
		return currencyType;
	}

	/**
	 * @param currencyType
	 *            the currencyType to set
	 */
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	/**
	 * @return the gwShare_vendorId
	 */
	public String getGwShare_vendorId() {
		return gwShare_vendorId;
	}

	/**
	 * @param gwShare_vendorId
	 *            the gwShare_vendorId to set
	 */
	public void setGwShare_vendorId(String gwShare_vendorId) {
		this.gwShare_vendorId = gwShare_vendorId;
	}

	public String getSearchTxt() {
		return searchTxt;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;

	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	public void setSearchTxt(String searchTxt) {
		try {
			this.searchTxt = URLDecoder.decode(searchTxt, "UTF-8");
			if (searchTxt != null) {
				this.searchTxt = this.searchTxt.trim();
			}
		} catch (UnsupportedEncodingException e) {
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
	 * @param gwShare_fileName
	 *            the gwShare_fileName to set
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
	 * @param gwShare_msg
	 *            the gwShare_msg to set
	 */
	public void setGwShare_msg(String gwShare_msg) {
		this.gwShare_msg = gwShare_msg;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}

	public String getGwShare_matchSQL() {
		return gwShare_matchSQL;
	}

	public void setGwShare_matchSQL(String gwShare_matchSQL) {
		this.gwShare_matchSQL = gwShare_matchSQL;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * 时间转化
	 */
	private void setTime() {
		// start time
		if (StringUtil.IsEmpty(startOpenDate)) {
			startOpenDate = null;
		} else {
			String start = startOpenDate + " 00:00:00";
			DateTimeUtil st = new DateTimeUtil(start);
			startOpenDate = String.valueOf(st.getLongTime());
		}
		// end time
		if (StringUtil.IsEmpty(endOpenDate)) {
			endOpenDate = null;
		} else {
			String end = endOpenDate + " 23:59:59";
			DateTimeUtil et = new DateTimeUtil(end);
			endOpenDate = String.valueOf(et.getLongTime());
		}
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getIsBatch() {
		return isBatch;
	}

	public void setIsBatch(String isBatch) {
		this.isBatch = isBatch;
	}

	public String getImportQueryField() {
		return importQueryField;
	}

	public void setImportQueryField(String importQueryField) {
		this.importQueryField = importQueryField;
	}

	public List<Map> getAwifi() {
		return awifi;
	}

	public void setAwifi(List<Map> awifi) {
		this.awifi = awifi;
	}

	public String getIs_awifi() {
		return is_awifi;
	}

	public void setIs_awifi(String is_awifi) {
		this.is_awifi = is_awifi;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public String getInstArea() {
		return instArea;
	}

	public void setInstArea(String instArea) {
		this.instArea = instArea;
	}

	public String getStartOpenDate() {
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
	}

	public String getEndOpenDate() {
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate) {
		this.endOpenDate = endOpenDate;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	
	/**
	 * @return the loid
	 */
	public String getLoid()
	{
		return loid;
	}

	
	/**
	 * @param loid the loid to set
	 */
	public void setLoid(String loid)
	{
		this.loid = loid;
	}

	
	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getIsNewNeed()
	{
		return isNewNeed;
	}

	public void setIsNewNeed(String isNewNeed)
	{
		this.isNewNeed = isNewNeed;
	}


	public String getMac()
	{
		return mac;
	}


	public void setMac(String mac)
	{
		this.mac = mac;
	}


}
