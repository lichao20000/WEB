
package com.linkage.module.gtms.stb.resource.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.DeviceVersionBIO;

/**
 * @author 王森博
 */
public class DeviceVersionACT extends splitPageAction
{

	private static final long serialVersionUID = 7847567957132933141L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DeviceVersionACT.class);
	private DeviceVersionBIO bio;
	private List versionList;
	private List stsVersionList;
	private String accoid;
	private String areaId;
	private List vendorList;
	private List versionTypeList;
	private String ajax;
	private String vendorId;
	private String isAdd;
	private String versionDesc;
	private String versionPath;
	private String deviceModelId;
	private String softwareversion;
	private String pathId;
	private String versionType;
	private String isAdmin;
	private String gwType = ""; // 此gwType为原来机顶盒代码中就有的
	private String gw_type = null; // 此gw_type用于区分终端类型1：家体网关 2：企业网关 4：机顶盒
	private String isUploadFile = null; // 该字段用户控制页面是否展示上传文件的组件 1:表示展示，其他情况表示不展示
	private File file1;
	private String urlParameter;
	private String fileName;
	private String response;
	// 查询条件
	private String queryVendorId;
	// 查询条件
	private String querySoftwareversion;
	 
	private String dcnPath;
	private String specialPath;

	/**
	 * 查询版本文件路径记录表中所有记录
	 * 
	 * @author wangsenbo
	 * @date Nov 8, 2010
	 * @param
	 * @return List
	 */
	public String execute()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		accoid = StringUtil.getStringValue(curUser.getUser().getId());
		if (curUser.getUser().isAdmin())
		{
			isAdmin = "1";
		}
		else
		{
			isAdmin = "0";
		}
		areaId = StringUtil.getStringValue(curUser.getAreaId());
		versionList = bio.getVersion(curPage_splitPage, num_splitPage, queryVendorId,
				querySoftwareversion);
		maxPage_splitPage = bio.getCountVersion(curPage_splitPage, num_splitPage,
				queryVendorId, querySoftwareversion);
		vendorList = bio.getVendor();
		return "list";
	}
	
	public String add()
	{
		vendorList = bio.getVendor();
		return "add";
	}

	public String uploadFile()
	{
		logger.warn("uploadFile:" + urlParameter);
		String path = ServletActionContext.getServletContext().getRealPath("/");
		response = bio.uploadFile(file1, urlParameter, fileName, path);
		logger.warn("response:" + response);
		return "response";
	}

	/**
	 * 判断是否存在赛特斯
	 * 
	 * @return
	 */
	public String isStsExsit()
	{
		ajax = bio.isStsExsit(vendorId, versionType);
		return "ajax";
	}

	/**
	 * 查询赛特斯
	 * 
	 * @return
	 */
	public String queryStsVersion()
	{
		logger.debug("queryStsVersion()");
		UserRes curUser = WebUtil.getCurrentUser();
		accoid = StringUtil.getStringValue(curUser.getUser().getId());
		areaId = StringUtil.getStringValue(curUser.getAreaId());
		stsVersionList = bio.getStsVersion();
		vendorList = bio.getVendor();
		Map<String, String> versionMap1 = new HashMap<String, String>();
		versionMap1.put("type_id", "1");
		versionMap1.put("type_name", "赛特斯版本");
		Map<String, String> versionMap2 = new HashMap<String, String>();
		versionMap2.put("type_id", "2");
		versionMap2.put("type_name", "退出赛特斯版本");
		versionTypeList = new ArrayList();
		versionTypeList.add(versionMap1);
		versionTypeList.add(versionMap2);
		return "stsList";
	}

	/**
	 * 查询设备型号
	 * 
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel()
	{
		logger.debug("getDeviceModel()");
		ajax = bio.getDeviceModel(vendorId);
		return "ajax";
	}

	/**
	 * 
	 */
	public String getVersionPathList()
	{
		ajax = "";
		ajax = bio.getNewVersionPath();
		return "ajax";
	}

	/**
	 * 添加赛特斯
	 * 
	 * @return
	 */
	public String addedit()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		
		if ("1".equals(isAdd))
		{
			ajax = bio.addVersion(curUser.getUser().getId(), vendorId, versionDesc,
					versionPath, versionType, softwareversion, deviceModelId,dcnPath,specialPath);
		}
		else
		{
			ajax = bio.editVersion(pathId, vendorId, versionDesc, versionPath,
					versionType, softwareversion, deviceModelId,dcnPath,specialPath);
		}
		ajax = ajax + "," + gwType;
		return "ajax";
	}

	/**
	 * 修改赛特斯
	 * 
	 * @return
	 */
	public String addeditSts()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		if ("1".equals(isAdd))
		{
			ajax = bio.addStsVersion(curUser.getUser().getId(), vendorId, versionDesc,
					versionPath, softwareversion, versionType);
		}
		else
		{
			ajax = bio.editStsVersion(pathId, vendorId, versionDesc, versionPath,
					softwareversion, versionType);
		}
		return "ajax";
	}

	public String deleteVersion()
	{
		ajax = bio.deleteVersion(pathId);
		ajax = ajax + "," + gwType;
		return "ajax";
	}

	/**
	 * @return the bio
	 */
	public DeviceVersionBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(DeviceVersionBIO bio)
	{
		this.bio = bio;
	}

	/**
	 * @return the versionList
	 */
	public List getVersionList()
	{
		return versionList;
	}

	/**
	 * @param versionList
	 *            the versionList to set
	 */
	public void setVersionList(List versionList)
	{
		this.versionList = versionList;
	}

	/**
	 * @return the accoid
	 */
	public String getAccoid()
	{
		return accoid;
	}

	/**
	 * @param accoid
	 *            the accoid to set
	 */
	public void setAccoid(String accoid)
	{
		this.accoid = accoid;
	}

	/**
	 * @return the areaId
	 */
	public String getAreaId()
	{
		return areaId;
	}

	/**
	 * @param areaId
	 *            the areaId to set
	 */
	public void setAreaId(String areaId)
	{
		this.areaId = areaId;
	}

	public String getGwType()
	{
		return gwType;
	}

	public void setGwType(String gwType)
	{
		this.gwType = gwType;
	}

	/**
	 * @return the vendorList
	 */
	public List getVendorList()
	{
		return vendorList;
	}

	/**
	 * @param vendorList
	 *            the vendorList to set
	 */
	public void setVendorList(List vendorList)
	{
		this.vendorList = vendorList;
	}

	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	/**
	 * @return the vendorId
	 */
	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getIsAdd()
	{
		return isAdd;
	}

	public void setIsAdd(String isAdd)
	{
		this.isAdd = isAdd;
	}

	public String getVersionDesc()
	{
		return versionDesc;
	}

	/**
	 * @param versionDesc
	 *            the versionDesc to set
	 */
	public void setVersionDesc(String versionDesc)
	{
		try
		{
			this.versionDesc = java.net.URLDecoder.decode(versionDesc, "UTF-8");
		}
		catch (Exception e)
		{
			this.versionDesc = versionDesc;
		}
	}

	/**
	 * @return the versionPath
	 */
	public String getVersionPath()
	{
		return versionPath;
	}

	/**
	 * @param versionPath
	 *            the versionPath to set
	 */
	public void setVersionPath(String versionPath)
	{
		this.versionPath = versionPath;
	}

	/**
	 * @return the deviceModelId
	 */
	public String getDeviceModelId()
	{
		return deviceModelId;
	}

	/**
	 * @param deviceModelId
	 *            the deviceModelId to set
	 */
	public void setDeviceModelId(String deviceModelId)
	{
		this.deviceModelId = deviceModelId;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the softwareversion
	 */
	public String getSoftwareversion()
	{
		return softwareversion;
	}

	public String getResponse()
	{
		return response;
	}

	public void setResponse(String response)
	{
		this.response = response;
	}

	/**
	 * @param softwareversion
	 *            the softwareversion to set
	 */
	public void setSoftwareversion(String softwareversion)
	{
		try
		{
			this.softwareversion = java.net.URLDecoder.decode(softwareversion, "UTF-8");
		}
		catch (Exception e)
		{
			this.softwareversion = softwareversion;
		}
	}

	public List getStsVersionList()
	{
		return stsVersionList;
	}

	public void setStsVersionList(List stsVersionList)
	{
		this.stsVersionList = stsVersionList;
	}

	/**
	 * @return the pathId
	 */
	public String getPathId()
	{
		return pathId;
	}

	public String getVersionType()
	{
		return versionType;
	}

	public void setVersionType(String versionType)
	{
		this.versionType = versionType;
	}

	public List getVersionTypeList()
	{
		return versionTypeList;
	}

	public void setVersionTypeList(List versionTypeList)
	{
		this.versionTypeList = versionTypeList;
	}

	/**
	 * @param pathId
	 *            the pathId to set
	 */
	public void setPathId(String pathId)
	{
		this.pathId = pathId;
	}

	public String getIsAdmin()
	{
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin)
	{
		this.isAdmin = isAdmin;
	}

	public String getUrlParameter()
	{
		return urlParameter;
	}

	public void setUrlParameter(String urlParameter)
	{
		this.urlParameter = urlParameter;
	}

	public File getFile1()
	{
		return file1;
	}

	public void setFile1(File file1)
	{
		this.file1 = file1;
	}

	public String getQueryVendorId()
	{
		return queryVendorId;
	}

	public void setQueryVendorId(String queryVendorId)
	{
		this.queryVendorId = queryVendorId;
	}

	public String getQuerySoftwareversion()
	{
		return querySoftwareversion;
	}

	public void setQuerySoftwareversion(String querySoftwareversion)
	{
		this.querySoftwareversion = querySoftwareversion;
	}

	public String getGw_type()
	{
		return gw_type;
	}

	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	public String getIsUploadFile()
	{
		return isUploadFile;
	}

	public void setIsUploadFile(String isUploadFile)
	{
		this.isUploadFile = isUploadFile;
	}
	
	public String getDcnPath() {
		return dcnPath;
	}

	public void setDcnPath(String dcnPath) {
		this.dcnPath = dcnPath;
	}

	public String getSpecialPath() {
		return specialPath;
	}

	public void setSpecialPath(String specialPath) {
		this.specialPath = specialPath;
	}

	public static void main(String[] args) {
		Map<String,String> aa = new HashMap<String, String>();
		aa.put("a", "2");
		aa.put("b", "2");
		System.out.println(aa.size());
	}
}
