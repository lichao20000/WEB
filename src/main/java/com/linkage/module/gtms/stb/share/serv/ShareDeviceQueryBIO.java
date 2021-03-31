/**
 *
 */
package com.linkage.module.gtms.stb.share.serv;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.dao.GwStbVendorModelVersionDAO;
import com.linkage.module.gtms.stb.share.action.FileUploadAction;
import com.linkage.module.gtms.stb.share.dao.ShareDeviceQueryDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.bio
 *
 */
public class ShareDeviceQueryBIO {
	//日志记录
	private static Logger logger = LoggerFactory.getLogger(ShareDeviceQueryBIO.class);
	private GwStbVendorModelVersionDAO vmvStbDao = null;
	private ShareDeviceQueryDAO gwDeviceDao = null;

	//回传消息
	private String msg = null;
	//查询条件
	private String importQueryField = "username";

	/**
	 * 模拟匹配提示
	 */
	@SuppressWarnings("unchecked")
	public String getDeviceSn(String cityId,String searchTxt,String separator,String gwShare_queryField){

		StringBuilder sb = new StringBuilder();

		List list = gwDeviceDao.getDeviceSn(cityId,searchTxt,gwShare_queryField);
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			String device_serialnumber = String.valueOf(map.get("device_serialnumber"));
			String cust_account = String.valueOf(map.get("cust_account"));
			String key = device_serialnumber + "|" + cust_account;
			if(i==0){
				sb.append(key);
			}else{
				sb.append(separator).append(key);
			}
		}
		return sb.toString();
	}

	/**
	 * 获取文件路径
	 *
	 * @return
	 */
	public String getFilePath() {
		//获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径",lipossHome);
		return lipossHome + "/temp/";
	}

	/**
	 * 针对高级查询，结果集比较大的话，二次查询
	 *
	 * @@param areaId 登录人所属域
	 * @param param 查询条件集 例如x|xx|xxx|xxx|xxxx
	 * @return
	 */
	public List getDeviceList(long areaId,String param){
		logger.debug("GwDeviceQueryBIO=>getDeviceList({})",param);
		if(null==param){
			return null;
		}
		String[] _param = param.split("\\|");

		String queryType = _param[0].trim();
		String queryField = _param[1].trim();
		String cityId = _param[2].trim();
		String queryParam = _param[3].trim();
		String onlineStatus = _param[4].trim();
		String vendorId = _param[5].trim();
		String deviceModelId = _param[6].trim();
		String devicetypeId = _param[7].trim();
		String bindType = _param[8].trim();
		String deviceSerialnumber = _param[9].trim();
		String gwShare_IPType = null;
		if (Global.XJDX.equals(Global.instAreaShortName))
		{
			gwShare_IPType = _param[10].trim();
		}
		return getDeviceList(areaId, queryType, queryParam, queryField, cityId,
				onlineStatus, vendorId, deviceModelId, devicetypeId,
				bindType, deviceSerialnumber,gwShare_IPType);
	}

	/**
	 * 查询设备（带列表）（针对数据量小于一定量时）
	 *
	 * @param areaId				登录人的areaId
	 * @param queryType				查询类型(1:简单查询；2:高级查询)
	 * @param queryParam			简单查询参数
	 * @param queryField			简单查询条件字段
	 * @param cityId				高级查询属地过滤
	 * @param onlineStatus			高级查询是否在线过滤
	 * @param vendorId				高级查询厂商过滤
	 * @param deviceModelId			高级查询设备型号过滤
	 * @param devicetypeId			高级查询软件版本过滤
	 * @param bindType				高级查询是否绑定过滤
	 * @param deviceSerialnumber	高级查询设备序列号模糊匹配
	 * @return
	 */
	public List getDeviceList(long areaId,String queryType,String queryParam,	String queryField,
			String cityId,String onlineStatus,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,
			String deviceSerialnumber,String gwShare_IPType){
		logger.debug("GwDeviceQueryBIO=>getDeviceList");
		List list = null;
		if("1".equals(queryType)){
			if("deviceSn".equals(queryField)){
				list = gwDeviceDao.queryDevice( areaId, cityId, null, null, null, null, queryParam, null);
			}else if("deviceIp".equals(queryField)){
				list = gwDeviceDao.queryDevice( areaId, cityId, null, null, null, null, null, queryParam);
			}else if("custaccount".equals(queryField)){
				list = gwDeviceDao.queryDevice( areaId, cityId , queryParam);
			}else if("deviceIpSix".equals(queryField)){
				list = gwDeviceDao.queryDeviceIpSix( areaId, cityId, null, null, null, null, null,null, queryParam);
			}else {
				list = gwDeviceDao.queryDeviceByFieldOr(areaId, queryParam,cityId);
			}
		}else{
			if (Global.HNLT.equals(Global.instAreaShortName)){
				onlineStatus=null;
			}
			if(null!=onlineStatus && !"".equals(onlineStatus) && !"-1".equals(onlineStatus)){
				list = gwDeviceDao.queryDeviceByLikeStatus( areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, null, onlineStatus);
			}else{
				list = gwDeviceDao.queryDevice(areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, gwShare_IPType);
			}
		}
		if(null==list || list.size()<1){
			if (Global.XJDX.equals(Global.instAreaShortName) && "1".equals(queryType)) {
				if("deviceSn".equals(queryField) || "deviceIp".equals(queryField)){
					this.msg = "设备未上报未被管理，请到机顶盒查询功能核实设备是否上报";
				}else if("custaccount".equals(queryField)){
					this.msg = "无此用户或者此用户未绑定机顶盒";
				}else{
					this.msg = "未查询到相关记录";
				}
			}else{
				this.msg = "未查询到相关记录";
			}
		}
		return list;
	}

	/**
	 * 查询设备（带列表）
	 *
	 * @param curPage_splitPage		分页选项当前页数
	 * @param num_splitPage			分页选项每页显示数
	 * @param areaId				登录人的areaId
	 * @param queryType				查询类型(1:简单查询；2:高级查询)
	 * @param queryParam			简单查询参数
	 * @param queryField			简单查询条件字段
	 * @param cityId				高级查询属地过滤
	 * @param onlineStatus			高级查询是否在线过滤
	 * @param vendorId				高级查询厂商过滤
	 * @param deviceModelId			高级查询设备型号过滤
	 * @param devicetypeId			高级查询软件版本过滤
	 * @param bindType				高级查询是否绑定过滤
	 * @param deviceSerialnumber	高级查询设备序列号模糊匹配
	 * @return
	 */
	public List getDeviceList(int curPage_splitPage,int num_splitPage,
			long areaId,String queryType,String queryParam,	String queryField,
			String cityId,String onlineStatus,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,
			String deviceSerialnumber,int gwShare_OrderByUpdateDate,String gwShare_IPType){
		logger.debug("GwDeviceQueryBIO=>getDeviceList");
		List list = null;
		//1:简单查询、2:高级查询、3、导入查询
		if("1".equals(queryType)){
			//根据设备序列号查询，根据设备IP查询，根据用户帐号查询
			if("deviceSn".equals(queryField)){
				list = gwDeviceDao.queryDevice(curPage_splitPage, num_splitPage, areaId, cityId, null, null, null, null, queryParam, null,gwShare_OrderByUpdateDate);
			}else if("deviceIp".equals(queryField)){
				list = gwDeviceDao.queryDevice(curPage_splitPage, num_splitPage, areaId, cityId, null, null, null, null, null, queryParam,gwShare_OrderByUpdateDate);
			}else if("custaccount".equals(queryField)){
				list = gwDeviceDao.queryDevice(curPage_splitPage, num_splitPage, areaId, cityId , queryParam,gwShare_OrderByUpdateDate);
			}else if("deviceIpSix".equals(queryField)){
				list = gwDeviceDao.queryDeviceIpSix(curPage_splitPage, num_splitPage, areaId, cityId, null, null, null, null, null, null,gwShare_OrderByUpdateDate,queryParam);
			}else {
				list = gwDeviceDao.queryDeviceByFieldOr(curPage_splitPage, num_splitPage, areaId, queryParam,cityId,gwShare_OrderByUpdateDate);
			}
		}else{
			if(Global.HNLT.equals(Global.instAreaShortName)){
				onlineStatus=null;
			}

			if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
				list = gwDeviceDao.queryDeviceByLikeStatus(curPage_splitPage, num_splitPage, areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, null, onlineStatus,gwShare_OrderByUpdateDate);
			}else{
				list = gwDeviceDao.queryDevice(curPage_splitPage, num_splitPage, areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, gwShare_IPType,gwShare_OrderByUpdateDate);
			}
		}
		if(null==list || list.size()<1){
			if (Global.XJDX.equals(Global.instAreaShortName) && "1".equals(queryType)) {
				if("deviceSn".equals(queryField) || "deviceIp".equals(queryField)){
					this.msg = "设备未上报未被管理，请到机顶盒查询功能核实设备是否上报";
				}else if("custaccount".equals(queryField)){
					this.msg = "无此用户或者此用户未绑定机顶盒";
				}else{
					this.msg = "未查询到相关记录";
				}
			}else{
				this.msg = "未查询到相关记录";
			}
		}
		return list;
	}

	/**
	 * 查询设备（带列表）
	 *
	 * @param curPage_splitPage		分页选项当前页数
	 * @param num_splitPage			分页选项每页显示数
	 * @param areaId				登录人的areaId
	 * @param queryType				查询类型(1:简单查询；2:高级查询)
	 * @param queryParam			简单查询参数
	 * @param queryField			简单查询条件字段
	 * @param cityId				高级查询属地过滤
	 * @param onlineStatus			高级查询是否在线过滤
	 * @param vendorId				高级查询厂商过滤
	 * @param deviceModelId			高级查询设备型号过滤
	 * @param devicetypeId			高级查询软件版本过滤
	 * @param bindType				高级查询是否绑定过滤
	 * @param deviceSerialnumber	高级查询设备序列号模糊匹配
	 * * @param deviceSerialnumber	高级查询设备ip10开头或非10开头
	 * @return
	 */
	public List getDeviceListByStb(int curPage_splitPage,int num_splitPage,
			long areaId,String queryType,String queryParam,	String queryField,
			String cityId,String onlineStatus,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,
			String deviceSerialnumber,int gwShare_OrderByUpdateDate,String gwShare_platform,String gwShare_IPType){
		logger.debug("GwDeviceQueryBIO=>getDeviceList");
		List list = null;
		//1:简单查询、2:高级查询、3、导入查询
		if("1".equals(queryType)){
			//根据设备序列号查询，根据设备IP查询，根据用户帐号查询
			if("deviceSn".equals(queryField)){
				list = gwDeviceDao.queryDevice(curPage_splitPage, num_splitPage, areaId, cityId, null, null, null, null, queryParam, null,gwShare_OrderByUpdateDate);
			}else if("deviceIp".equals(queryField)){
				list = gwDeviceDao.queryDevice(curPage_splitPage, num_splitPage, areaId, cityId, null, null, null, null, null, queryParam,gwShare_OrderByUpdateDate);
			}else if("custaccount".equals(queryField)){
				list = gwDeviceDao.queryDevice(curPage_splitPage, num_splitPage, areaId, cityId , queryParam,gwShare_OrderByUpdateDate);
			}else {
				list = gwDeviceDao.queryDeviceByFieldOr(curPage_splitPage, num_splitPage, areaId, queryParam,cityId,gwShare_OrderByUpdateDate);
			}
		}else{
			if(Global.HNLT.equals(Global.instAreaShortName)){
				onlineStatus=null;
			}

			if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
				list = gwDeviceDao.queryDeviceByLikeStatusByStb(curPage_splitPage, num_splitPage, areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, null, onlineStatus,gwShare_OrderByUpdateDate,gwShare_platform);
			}else{
				list = gwDeviceDao.queryDeviceByStb(curPage_splitPage, num_splitPage, areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, gwShare_IPType,gwShare_OrderByUpdateDate,gwShare_platform);
			}
		}
		if(null==list || list.size()<1){
			if (Global.XJDX.equals(Global.instAreaShortName) && "1".equals(queryType)) {
				if("deviceSn".equals(queryField) || "deviceIp".equals(queryField)){
					this.msg = "设备未上报未被管理，请到机顶盒查询功能核实设备是否上报";
				}else if("custaccount".equals(queryField)){
					this.msg = "无此用户或者此用户未绑定机顶盒";
				}else{
					this.msg = "未查询到相关记录";
				}
			}else{
				this.msg = "未查询到相关记录";
			}
		}
		return list;
	}

	/*
	 *
	 * 高级查询返回sql
	 */

	public String getDeviceListSQL(int curPage_splitPage,int num_splitPage,
			long areaId,String queryType,String queryParam,	String queryField,
			String cityId,String onlineStatus,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,
			String deviceSerialnumber,int gwShare_OrderByUpdateDate,String gwShare_platform)
	{
		logger.debug("GwDeviceQueryBIO=>getDeviceListSQL");
		String sqlStr = null;

		if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
			sqlStr = gwDeviceDao.queryDeviceSQLLikeStatusByStb(curPage_splitPage, num_splitPage, areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, null,onlineStatus,gwShare_OrderByUpdateDate,gwShare_platform);
		}else
		{
			sqlStr = gwDeviceDao.queryDeviceSQLByStb(curPage_splitPage, num_splitPage, areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, null,gwShare_OrderByUpdateDate,gwShare_platform);
		}
		return sqlStr;
	}




	/**
	 * 查询设备（带列表）
	 *
	 * @param areaId				登录人的areaId
	 * @param queryType				查询类型(1:简单查询；2:高级查询)
	 * @param queryParam			简单查询参数
	 * @param queryField			简单查询条件字段
	 * @param cityId				高级查询属地过滤
	 * @param onlineStatus			高级查询是否在线过滤
	 * @param vendorId				高级查询厂商过滤
	 * @param deviceModelId			高级查询设备型号过滤
	 * @param devicetypeId			高级查询软件版本过滤
	 * @param bindType				高级查询是否绑定过滤
	 * @param deviceSerialnumber	高级查询设备序列号模糊匹配
	 * @return
	 */
	public int getDeviceListCount(long areaId,String queryType,String queryParam,
			String queryField,String cityId,String onlineStatus,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,
			String deviceSerialnumber){
		logger.debug("GwDeviceQueryBIO=>getDeviceListCount");
		int count = 0;
 		if("1".equals(queryType)){
 			if("deviceSn".equals(queryField)){
				count = gwDeviceDao.queryDeviceCount(areaId, cityId, null, null, null, null, queryParam, null);
			}else if("deviceIp".equals(queryField)){
				count = gwDeviceDao.queryDeviceCount(areaId, cityId, null, null, null, null, null, queryParam);
			}else if("custaccount".equals(queryField)){
				count = gwDeviceDao.queryDeviceCount(areaId,cityId,queryParam);
			}else if("deviceIpSix".equals(queryField)){
				count = gwDeviceDao.queryDeviceIpSixCount(areaId, cityId, null, null, null, null, null, null,queryParam);
			}else {
				count = gwDeviceDao.queryDeviceByFieldOrCount(areaId, queryParam,cityId);
			}
		}else{
			if(Global.HNLT.equals(Global.instAreaShortName)){
				onlineStatus=null;
			}

			if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
				count = gwDeviceDao.queryDeviceByLikeStatusCount(areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, null, onlineStatus);
			}else{
				count = gwDeviceDao.queryDeviceCount(areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, null);
			}
		}
		return count;
	}

	/**
	 * 查询设备（带列表）
	 *
	 * @param areaId				登录人的areaId
	 * @param queryType				查询类型(1:简单查询；2:高级查询)
	 * @param queryParam			简单查询参数
	 * @param queryField			简单查询条件字段
	 * @param cityId				高级查询属地过滤
	 * @param onlineStatus			高级查询是否在线过滤
	 * @param vendorId				高级查询厂商过滤
	 * @param deviceModelId			高级查询设备型号过滤
	 * @param devicetypeId			高级查询软件版本过滤
	 * @param bindType				高级查询是否绑定过滤
	 * @param deviceSerialnumber	高级查询设备序列号模糊匹配
	 * @return
	 */
	public int getDeviceListCountByStb(long areaId,String queryType,String queryParam,
			String queryField,String cityId,String onlineStatus,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,
			String deviceSerialnumber,String gwShare_platform,String gwShare_IPType){
		logger.debug("GwDeviceQueryBIO=>getDeviceListCountByStb");
		int count = 0;
		if("1".equals(queryType)){
			if("deviceSn".equals(queryField)){
				count = gwDeviceDao.queryDeviceCount(areaId, cityId, null, null, null, null, queryParam, null);
			}else if("deviceIp".equals(queryField)){
				count = gwDeviceDao.queryDeviceCount(areaId, cityId, null, null, null, null, null, queryParam);
			}else if("custaccount".equals(queryField)){
				count = gwDeviceDao.queryDeviceCount(areaId,cityId,queryParam);
			}else {
				count = gwDeviceDao.queryDeviceByFieldOrCount(areaId, queryParam,cityId);
			}
		}else{
			if(Global.HNLT.equals(Global.instAreaShortName)){
				onlineStatus=null;
			}

			if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
				count = gwDeviceDao.queryDeviceByLikeStatusCountByStb(areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, null, onlineStatus,gwShare_platform);
			}else{

				count = gwDeviceDao.queryDeviceCountByStb(areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, gwShare_IPType,gwShare_platform);
			}
		}
		return count;
	}

	/**
	 * 查询属地
	 * @return
	 */
	public String getCity(String cityId){
		logger.debug("GwDeviceQueryBIO=>getCity(cityId:{})",cityId);
		List list = CityDAO.getNextCityListByCityPid(cityId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("city_id"));
			bf.append("$");
			bf.append(map.get("city_name"));
		}
		return bf.toString();
	}

	/**
	 * 查询属地(包含下级属地)
	 * @return
	 */
	public String getAllCity(String cityId){
		logger.debug("GwDeviceQueryBIO=>getAllCity(cityId:{})",cityId);
		List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			String city_id = (String)list.get(i);
			String city_name = Global.G_CityId_CityName_Map.get(city_id);
			if(i>0){
				bf.append("#");
			}
			bf.append(city_id);
			bf.append("$");
			bf.append(city_name);
		}
		return bf.toString();
	}
	/**
	 * 查询设备厂商
	 * @return
	 */
	public String getVendor(){
		logger.debug("GwDeviceQueryBIO=>getVendor()");
		List list = vmvStbDao.getVendor();
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("vendor_id"));
			bf.append("$");
			bf.append(map.get("vendor_add"));
			bf.append("(");
			bf.append(map.get("vendor_name"));
			bf.append(")");
		}
		return bf.toString();
	}

	/**
	 * 查询设备型号
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel(String vendorId){
		logger.debug("GwDeviceQueryBIO=>getDeviceModel(vendorId:{})",vendorId);
		List list = vmvStbDao.getDeviceModel(vendorId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("device_model_id"));
			bf.append("$");
			bf.append(map.get("device_model"));
		}
		return bf.toString();
	}

	/**
	 * 查询设备版本
	 * @param deviceModelId
	 * @return
	 */
	public String getDevicetype(String deviceModelId){
		logger.debug("GwDeviceQueryBIO=>getDevicetype(deviceModelId:{})",deviceModelId);
		List list = vmvStbDao.getVersionList(deviceModelId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("devicetype_id"));
			bf.append("$");
			bf.append(map.get("softwareversion"));
		}
		return bf.toString();
	}

	/**
	 * 查询设备版本
	 * @param deviceModelId
	 * @return
	 */
	public String getDeviceHardVersion(String deviceModelId){
		logger.debug("GwDeviceQueryBIO=>getDeviceHardVersion(deviceModelId:{})",deviceModelId);
		List list = vmvStbDao.getDeviceHardVersion(deviceModelId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("hardwareversion"));
			bf.append("$");
			bf.append(map.get("hardwareversion"));
		}
		return bf.toString();
	}

	public String getSoftVersion(String hardVercion,String deviceModelId){
		logger.debug("GwDeviceQueryBIO=>getSoftVersion({}-{})",hardVercion,deviceModelId);
		List list = vmvStbDao.getSoftVersion(hardVercion,deviceModelId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("devicetype_id"));
			bf.append("$");
			bf.append(map.get("softwareversion"));
		}
		return bf.toString();
	}

	/**
	 * 查询设备版本
	 * @param hardVersion
	 * @return
	 */
	public String getDevicetypeByHardVersion(String hardVersion){
		logger.debug("GwDeviceQueryBIO=>getDevicetypeByHardVersion(hardwareversion:{})",hardVersion);
		List list = vmvStbDao.getDevicetypeByHardVersion(hardVersion);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("devicetype_id"));
			bf.append("$");
			bf.append(map.get("softwareversion"));
		}
		return bf.toString();
	}

	/**
	 * @return the gwDeviceDao
	 */
	public ShareDeviceQueryDAO getGwDeviceDao() {
		return gwDeviceDao;
	}

	/**
	 * @param gwDeviceDao the gwDeviceDao to set
	 */
	public void setGwDeviceDao(ShareDeviceQueryDAO gwDeviceDao) {
		this.gwDeviceDao = gwDeviceDao;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}


	public GwStbVendorModelVersionDAO getVmvStbDao() {
		return vmvStbDao;
	}


	public void setVmvStbDao(GwStbVendorModelVersionDAO vmvStbDao) {
		this.vmvStbDao = vmvStbDao;
	}

	/**
	 * 查询设备（带列表）（针对导入查询）
	 * 此方法用于导入文件查询机顶盒信息（业务账号或设备序列号），虽然定义为湖北联通专用，但可以公用，建议放开//by zzs
	 * @param areaId				登录人的areaId
	 * @param queryType				查询类型(1:简单查询；2:高级查询)
	 * @param cityId				属地过滤
	 * @param fileName				文件名
	 * @return
	 */
	public List getDeviceListForHblt(long areaId,String queryType,String cityId,String fileName){
		logger.debug("getDeviceList({},{},{},{})",new Object[]{areaId,queryType,cityId,fileName});
		if(fileName.length()<4){
			this.msg = "上传的文件名不正确！";
			return null;
		}
		String fileName_ = fileName.substring(fileName.length()-3, fileName.length());
		logger.debug("fileName_;{}",fileName_);
		if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		List<String> dataList = null;
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(fileName);
			}else{
				dataList = getImportDataByXLS(fileName);
			}
		}catch(FileNotFoundException e){
			logger.warn("{}文件没找到！",fileName);
			this.msg = "文件没找到！";
			return null;
		}catch(IOException e){
			logger.warn("{}文件解析出错！",fileName);
			this.msg = "文件解析出错！";
			return null;
		}catch(Exception e){
			logger.warn("{}文件解析出错！",fileName);
			this.msg = "文件解析出错！";
			return null;
		}

		List list = null;
		if(dataList.size()<1){
			this.msg = "文件未解析到合法数据！";
			return null;
		}else{
			gwDeviceDao.insertTmp(fileName,dataList,importQueryField);
			if("username".equals(importQueryField)){
				list = gwDeviceDao.queryDeviceByImportUsername(areaId, cityId, dataList,fileName);
			}else{
				list = gwDeviceDao.queryDeviceByImportDevicesn(areaId, cityId, dataList,fileName);
			}
		}

		if(null==list || list.size()<1){
			this.msg = "账号不存在或账号未绑定设备";
		}
		return list;
	}

	public List getDeviceListForBlackList(long areaId,String queryType,String cityId,String fileName){
		logger.debug("getDeviceList({},{},{},{})",new Object[]{areaId,queryType,cityId,fileName});
		if(fileName.length()<4){
			this.msg = "上传的文件名不正确！";
			return null;
		}
		String fileName_ = fileName.substring(fileName.length()-3, fileName.length());
		logger.debug("fileName_;{}",fileName_);
		if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		List<String> dataList = null;
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXTBlackList(fileName);
			}else{
				dataList = getImportDataByXLSBlackList(fileName);
			}
		}catch(FileNotFoundException e){
			logger.warn("{}文件没找到！",fileName);
			this.msg = "文件没找到！";
			return null;
		}catch(IOException e){
			logger.warn("{}文件解析出错！",fileName);
			this.msg = "文件解析出错！";
			return null;
		}catch(Exception e){
			logger.warn("{}文件解析出错！",fileName);
			this.msg = "文件解析出错！";
			return null;
		}

		List list = null;
		if(dataList.size()<1){
			this.msg = "文件未解析到合法数据！";
			return null;
		}else{
			gwDeviceDao.insertTmp(fileName,dataList,importQueryField);
			if("username".equals(importQueryField)){
				list = gwDeviceDao.queryDeviceByImportUsername(areaId, cityId, dataList,fileName);
			}else{
				list = gwDeviceDao.queryDeviceByImportDevicesn(areaId, cityId, dataList,fileName);
			}
		}

		if(null==list || list.size()<1){
			this.msg = "账号不存在或账号未绑定设备";
		}
		return list;
	}

	/**
	 * 查询设备（带列表）（针对导入查询）
	 *
	 * @param areaId				登录人的areaId
	 * @param queryType				查询类型(1:简单查询；2:高级查询)
	 * @param cityId				属地过滤
	 * @param fileName				文件名
	 * @return
	 */
	public List getDeviceListForXjdx(long areaId,String queryType,String cityId,String fileName){
		logger.debug("getDeviceList({},{},{},{})",new Object[]{areaId,queryType,cityId,fileName});
		if(fileName.length()<4){
			this.msg = "上传的文件名不正确！";
			return null;
		}
		String fileName_ = fileName.substring(fileName.length()-3, fileName.length());
		logger.debug("fileName_;{}",fileName_);
		if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		List<String> dataList = null;
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(fileName);
			}else{
				dataList = getImportDataByXLS(fileName);
			}
		}catch(FileNotFoundException e){
			logger.warn("{}文件没找到！",fileName);
			this.msg = "文件没找到！";
			return null;
		}catch(IOException e){
			logger.warn("{}文件解析出错！",fileName);
			this.msg = "文件解析出错！";
			return null;
		}catch(Exception e){
			logger.warn("{}文件解析出错！",fileName);
			this.msg = "文件解析出错！";
			return null;
		}

		List list = null;
		if(dataList.size()<1){
			this.msg = "文件未解析到合法数据！";
			return null;
		}else{
			gwDeviceDao.insertTmp(fileName,dataList,importQueryField);
			if("username".equals(importQueryField)){
				list = gwDeviceDao.queryDeviceByImportUsernameByXjdx(areaId, cityId, dataList,fileName);
			}else{
				list = gwDeviceDao.queryDeviceByImportDevicesnByXjdx(areaId, cityId, dataList,fileName);
			}
		}

		if(null==list || list.size()<1){
			this.msg = "账号不存在或账号未绑定设备";
		}
		return list;
	}

	public String queryImportSQL(long areaId,String cityId,String fileName){
		String sql = "";
		logger.warn("");
		if("username".equals(importQueryField)){
			sql = gwDeviceDao.querySQLImportUsernameByStb(areaId, cityId, fileName);
		}else{
			sql = gwDeviceDao.querySQLImportDevicesnByStb(areaId, cityId,fileName);
		}

		return sql;
	}

	/**
	 * 解析文件（txt格式）
	 *
	 * @param fileName 文件名
	 * @return
	 * @throws FileNotFoundException
	 * 		   IOException
	 */
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,IOException{
		logger.debug("getImportDataByTXT{}",fileName);
		List<String> list = new ArrayList<String>();
		// 解决中文乱码问题
		FileInputStream in = new FileInputStream(getFilePath()+fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		String line = reader.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
		 * 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if(null!=line && line.contains("设备序列号")){
			this.importQueryField = "device_serialnumber";
		}else if(null!=line && line.contains("业务账号")){
			this.importQueryField = "username";
		}
		int rowCount = 1;
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = reader.readLine()) != null) {
			rowCount++;
			if(!"".equals(line.trim())){
				list.add(line.trim());
			}
			if(LipossGlobals.inArea(Global.HBLT) || LipossGlobals.inArea(Global.NXDX)){
				if(rowCount>3000){
					break;
				}
			}
			if(LipossGlobals.inArea(Global.JXDX) || LipossGlobals.inArea(Global.XJDX)){
				if(rowCount>30000){
					break;
				}
			}
			if(LipossGlobals.inArea(Global.SDLT)){
				if(rowCount>50000){
					break;
				}
			}

		}
		in.close();
		in = null;
		//处理完毕时，则删掉文件
		File f = new File(getFilePath()+fileName);
		f.delete();
		f = null;
		return list;
	}

	/**
	 * 解析文件（txt格式）
	 *
	 * @param fileName 文件名
	 * @return
	 * @throws FileNotFoundException
	 * 		   IOException
	 */
	public List<String> getImportDataByTXTBlackList(String fileName) throws FileNotFoundException,IOException{
		logger.debug("getImportDataByTXT{}",fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
		 * 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if(null!=line && line.contains("设备序列号")){
			this.importQueryField = "device_serialnumber";
		}else if(null!=line && line.contains("业务账号")){
			this.importQueryField = "username";
		}
		int rowCount = 1;
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			rowCount++;
			if(!"".equals(line.trim())){
				list.add(line.trim());
			}
			if(rowCount>10000){
				break;
			}
		}
		in.close();
		in = null;
		//处理完毕时，则删掉文件
		File f = new File(getFilePath()+fileName);
		f.delete();
		f = null;
		return list;
	}
	/**
	 * 解析文件（xls格式）
	 *
	 * @param fileName 文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 *
	 */
	public List<String> getImportDataByXLS(String fileName) throws BiffException, IOException{
		logger.debug("getImportDataByXLS{}",fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath()+fileName);
		Workbook wwb = Workbook.getWorkbook(f);;
		Sheet ws = null;
		//总sheet数
		//int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m=0;m<sheetNumber;m++){
			ws = wwb.getSheet(m);

			//当前页总记录行数和列数
			int rowCount = ws.getRows();
			if(LipossGlobals.inArea(Global.HBLT) || LipossGlobals.inArea(Global.NXDX)){
				if(rowCount>3000){
					rowCount = 3000;
				}
			}
			if(LipossGlobals.inArea(Global.JXDX) || LipossGlobals.inArea(Global.XJDX)){
				if(rowCount>30000){
					rowCount = 30000;
				}
			}
			if(LipossGlobals.inArea(Global.SDLT)){
				if(rowCount>50000){
					rowCount = 50000;
				}
			}
			//int columeCount = ws.getColumns();

			if(null!=ws.getCell(0,0).getContents()){
				String line = ws.getCell(0,0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
				 * 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if(null!=line && "设备序列号".equals(line)){
					this.importQueryField = "device_serialnumber";
				}else if(null!=line && "业务账号".equals(line)){
					this.importQueryField = "username";
				}
			}

			//取当前页所有值放入list中
			for (int i=1;i<rowCount;i++){
				String temp = ws.getCell(0, i).getContents();
				if(null!=temp && !"".equals(temp)){
					if(!"".equals(ws.getCell(0, i).getContents().trim())){
						list.add(ws.getCell(0, i).getContents().trim());
					}
				}
			}
		}
		f.delete();
		f = null;
		return list;
	}
	/**
	 * 解析文件（xls格式）
	 *
	 * @param fileName 文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 *
	 */
	public List<String> getImportDataByXLSBlackList(String fileName) throws BiffException, IOException {
		logger.debug("getImportDataByXLS{}", fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		;
		Sheet ws = null;
		// 总sheet数
		// int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++) {
			ws = wwb.getSheet(m);
			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			if (rowCount > 10000) {
				rowCount = 10000;
			}
			if (null != ws.getCell(0, 0).getContents()) {
				String line = ws.getCell(0, 0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if (null != line && "设备序列号".equals(line)) {
					this.importQueryField = "device_serialnumber";
				} else if (null != line && "业务账号".equals(line)) {
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
		f.delete();
		f = null;
		return list;
	}

	public String getVersionPath(String vendorId,String deviceModelId){
		List list = vmvStbDao.getVersionPath(vendorId,deviceModelId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("version_path"));
			bf.append("$");
			bf.append(map.get("version_path"));
		}
		return bf.toString();
	}

	public String getSpecialPath(String vendorId,String deviceModelId,String version_path)
	{
		List list = vmvStbDao.getSpecialPath(vendorId,deviceModelId,version_path);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("special_path"));
			bf.append("$");
			bf.append(map.get("special_path"));
		}
		return bf.toString();
	}

	public String getDcnPath(String vendorId,String deviceModelId,String version_path,String special_path){
		List list = vmvStbDao.getDcnPath(vendorId,deviceModelId,version_path,special_path);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("path_id")+"|"+map.get("dcn_path"));
			bf.append("$");
			bf.append(map.get("dcn_path"));
		}
		return bf.toString();
	}

    public String getDeviceOui( String gwShare_modelId) {
		logger.debug("GwDeviceQueryBIO=>getDeviceOui(gwShare_modelId:{})",gwShare_modelId);
		List list = vmvStbDao.getDeviceOui(gwShare_modelId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("oui"));
			bf.append("$");
			bf.append(map.get("oui"));
		}
		return bf.toString();
    }
}
