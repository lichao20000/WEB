
package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.SoftVersionBIO;

@SuppressWarnings("rawtypes")
public class SoftVersionACT extends splitPageAction
{
	private static final long serialVersionUID = 7847567957132933141L;
	private static Logger logger = LoggerFactory.getLogger(SoftVersionACT.class);
	
	private SoftVersionBIO bio;
	private String accoid;
	private List versionList;
	private List vendorList;
	private String ajax;
	private String vendorId;
	private String versionName;
	private String versionDesc;
	private String versionPath;
	private String deviceModelId;
	private String fileSize;
	private String MD5;
	private String epg_version;
	private String net_type;
	
	private Map<String,String> data;
	private String id;
	private String isAdd;
	// 查询条件
	private String queryVendorId;
	// 查询条件
	private String queryVersionName;
	
	/**湖南联通特制 	传入参数showType；1为只有查询功能，2为查询+编辑功能，3为查询+编辑+删除功能。*/
	private String showType="3";
	

	/**
	 * 查询版本文件路径记录表中所有记录
	 */
	public String execute()
	{
		logger.warn("execute:[{}],[{}]",queryVendorId,queryVersionName);
		versionList = bio.getVersion(curPage_splitPage, num_splitPage, queryVendorId,
				queryVersionName);
		maxPage_splitPage = bio.getCountVersion(curPage_splitPage, num_splitPage,
				queryVendorId, queryVersionName);
		vendorList = bio.getVendor();
		return "list";
	}
	
	/**
	 * 新增界面，获取厂商
	 */
	public String add()
	{
		vendorList = bio.getVendor();
		return "add";
	}
	
	/**
	 * 新增、编辑版本文件路径
	 */
	public String addedit()
	{
		logger.warn("addedit:[{}],[{}]",isAdd,versionPath);
		UserRes curUser = WebUtil.getCurrentUser();
		if ("1".equals(isAdd))
		{
			ajax = bio.addVersion(curUser.getUser().getId(), vendorId, versionDesc,
					versionPath,versionName, deviceModelId,fileSize,MD5,epg_version,net_type);
		}
		else
		{
			ajax = bio.editVersion(id, vendorId, versionDesc, versionPath,
					versionName, deviceModelId,fileSize,MD5,epg_version,net_type);
		}
		return "ajax";
	}

	/**
	 * 查询设备型号
	 */
	public String getDeviceModel()
	{
		logger.warn("getDeviceModel:[{}]",vendorId);
		ajax = bio.getDeviceModel(vendorId);
		return "ajax";
	}

	/**
	 * 删除版本
	 */
	public String deleteVersion()
	{
		logger.warn("deleteVersion:[{}]",id);
		ajax = bio.deleteVersion(id);
		return "ajax";
	}

	/**
	 * 查询版本详细信息
	 */
	public String getSoftVersionDetail()
	{
		logger.warn("getSoftVersionDetail:[{}]",id);
		data = bio.getSoftVersionDetail(id);
		return "detail";
	}
	
	
	public List getVersionList(){
		return versionList;
	}

	public void setVersionList(List versionList){
		this.versionList = versionList;
	}

	public String getAccoid(){
		return accoid;
	}

	public void setAccoid(String accoid){
		this.accoid = accoid;
	}

	public List getVendorList(){
		return vendorList;
	}

	public void setVendorList(List vendorList){
		this.vendorList = vendorList;
	}

	public String getAjax(){
		return ajax;
	}

	public void setAjax(String ajax){
		this.ajax = ajax;
	}

	public String getVendorId(){
		return vendorId;
	}

	public void setVendorId(String vendorId){
		this.vendorId = vendorId;
	}

	public String getVersionDesc(){
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc){
		try{
			this.versionDesc = java.net.URLDecoder.decode(versionDesc, "UTF-8");
		}catch (Exception e){
			this.versionDesc = versionDesc;
		}
	}

	public String getVersionPath(){
		return versionPath;
	}

	public void setVersionPath(String versionPath){
		this.versionPath = versionPath;
	}

	public String getDeviceModelId(){
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId){
		this.deviceModelId = deviceModelId;
	}

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getQueryVendorId(){
		return queryVendorId;
	}

	public void setQueryVendorId(String queryVendorId){
		this.queryVendorId = queryVendorId;
	}

	public SoftVersionBIO getBio() {
		return bio;
	}

	public void setBio(SoftVersionBIO bio) {
		this.bio = bio;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getMD5() {
		return MD5;
	}

	public void setMD5(String mD5) {
		MD5 = mD5;
	}

	public String getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(String isAdd) {
		this.isAdd = isAdd;
	}

	public String getQueryVersionName() {
		return queryVersionName;
	}

	public void setQueryVersionName(String queryVersionName) {
		this.queryVersionName = queryVersionName;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getEpg_version() {
		return epg_version;
	}

	public void setEpg_version(String epg_version) {
		this.epg_version = epg_version;
	}

	public String getNet_type() {
		return net_type;
	}

	public void setNet_type(String net_type) {
		this.net_type = net_type;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

}
