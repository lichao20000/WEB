package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.database.Cursor;
import com.linkage.module.gtms.stb.resource.dao.DeviceTypeInfoDAO;
import com.linkage.module.gwms.util.corba.ACSCorba;

@SuppressWarnings("rawtypes")
public class DeviceTypeInfoBIO
{
	private static Logger logger = LoggerFactory.getLogger(DeviceTypeInfoBIO.class);
	private DeviceTypeInfoDAO dao;
	private int maxPage_splitPage;
	private int stbmaxPage_splitPage;

	public int getStbmaxPage_splitPage() {
		return stbmaxPage_splitPage;
	}

	public void setStbmaxPage_splitPage(int stbmaxPage_splitPage) {
		this.stbmaxPage_splitPage = stbmaxPage_splitPage;
	}

	public int getMaxPage_splitPage()
	{
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage)
	{
		this.maxPage_splitPage = maxPage_splitPage;
	}

	public List<Map> queryDeviceDetail(long deviceTypeId,long spec_id)
	{
		return dao.queryDeviceDetail(deviceTypeId,spec_id);
	}

	public List<Map<String,String>> querySpecList()
	{
		return dao.querySpecList();
	}
	
	/**
	 * 查询端口和协议信息编辑用
	 */
	public String getPortAndType(long deviceTypeId)
	{
		return dao.getPortAndType(deviceTypeId);
	}

	/**
	 * 根据id删除记录
	 */
	public void deleteDevice(String gw_type,int id)
	{
		logger.debug("deleteDevice({})", new Object[] { id });
		dao.deleteDevice(id);
		// 通知ACS
		sendToACS(gw_type,0, 2, id);
	}

	/**
	 * 查看当前设备版本是否可以删除，如果版本下面没有设备即可删除
	 */
	public boolean canDeleteDevice(int id)
	{
		logger.debug("canDeleteDevice({})", new Object[] { id });
		int num = dao.getDeviceListCount(id);
		// 有相关的设备，不可删除
		boolean result = num > 0 ? false : true;
		logger.debug("canDeleteDevice: " + result);
		return result;
	}

	/**
	 * 增加设备版本
	 * @param vendor 厂商
	 * @param device_model 设备型号
	 * @param specversion 特定版本
	 * @param hard_version 硬件型号
	 * @param soft_version 软件型号
	 * @param is_check 是否审核
	 * @param rela_dev_type 设备类型
	 * @return 操作结果
	 */
	public String addDevTypeInfo(String gw_type,int vendor, int device_model, String specversion,
			String hard_version, String soft_version, int is_check, int rela_dev_type,
			String typeId, String portInfo, String servertype, long acc_oid,String ipType,long spec_id)
	{
		String msg = "";
		Object[] temp = dao.addDevTypeInfo(vendor, device_model, specversion,
				hard_version, soft_version, is_check, rela_dev_type, typeId,ipType,spec_id);
		dao.addPortAddServertype(portInfo, acc_oid, servertype);
		long id = Long.valueOf(temp[1].toString());
		int result = Integer.valueOf(temp[0].toString());
		// 通知ACS
		sendToACS(gw_type,1, 2, id);
		if (result != 0){
			msg = "设备版本入库成功";
		}else{
			msg = "设备版本入库失败";
		}
		return msg;
	}

	/**
	 * 修改设备版本
	 * 
	 * @param deviceTypeId ID
	 * @param vendor 厂商
	 * @param device_model 设备型号
	 * @param specversion 特定版本
	 * @param hard_version 硬件型号
	 * @param soft_version 软件型号
	 * @param is_check 是否审核
	 * @param rela_dev_type 设备类型
	 * @return 操作结果
	 */
	public String updateDevTypeInfo(String gw_type,long deviceTypeId, int vendor, int device_model,
			String specversion, String hard_version, String soft_version, int is_check,
			int rela_dev_type, String typeId, String portInfo, String servertype,
			long acc_oid,String ipType,String isNormal,long spec_id)
	{
		String msg = "";
		int res = dao.updateDevTypeInfo(deviceTypeId,vendor,device_model,specversion,hard_version,
				soft_version,is_check,rela_dev_type,typeId,ipType,isNormal,spec_id);
		dao.updatePortAddServertype(deviceTypeId, portInfo, acc_oid,servertype);
		// 通知ACS
		sendToACS(gw_type,1, 2, deviceTypeId);
		if (res != 0){
			msg = "设备版本信息更新成功";
		}else{
			msg = "设备版本信息更新失败";
		}
		return msg;
	}

	public void updateIsCheck(long id)
	{
		logger.debug("updateIsCheck({})", new Object[] { id });
		dao.updateIsCheck(id);
	}

	public void sendToACS(String gw_type,int chgType, int infoType, long deviceTypeId)
	{
		new ACSCorba(gw_type).chgInfo(chgType, infoType, String.valueOf(deviceTypeId));
	}

	public String getTypeNameList(String typeId)
	{
		return dao.getTypeNameList(typeId);
	}

	/**
	 * 根据Cursor,创建下拉框 add by zhangchy 2011-10-25 改写FormUtil.java中的
	 * 
	 * @param cursor 数据源
	 * @param name 用于生成下拉框时的value值
	 * @param cast 用于生成下拉框时的展示值(呈现给用户看的)
	 * @param pos 初始化下拉框的初始值
	 * @param hasDefault
	 * @return 生成下拉框的html代码后返回.
	 */
	public static String createListBox(Cursor cursor,String name,String cast,String pos,boolean hasDefault)
	{
		String htmlStr;
		htmlStr = "<SELECT NAME=" + name + " CLASS=bk >";
		Map fields = cursor.getNext();
		if (fields == null)
		{
			if (hasDefault)
				htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
		}
		else
		{
			String tmp;
			htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";
			while (fields != null)
			{
				tmp = (String) fields.get(name);
				if (tmp != null)
					tmp = tmp.trim();
				htmlStr += "<OPTION VALUE='" + tmp;
				if (pos.trim().equals(tmp))
					htmlStr += "' selected>--";
				else
					htmlStr += "'>--";
				htmlStr += (String) fields.get(cast) + "--</OPTION>";
				fields = cursor.getNext();
			}
		}
		htmlStr += "</SELECT>";
		return htmlStr;
	}
	
	
	public List<Map<String,String>> getGwDevType(){
		
		return dao.getGwDevType();
	}

	/** getters and setters **/
	public DeviceTypeInfoDAO getDao(){
		return dao;
	}

	public void setDao(DeviceTypeInfoDAO dao){
		this.dao = dao;
	}

	public int isNormalVersion(int deviceModel) 
	{
		return dao.isNormalVersion(deviceModel);
	}

	public List<Map> querystbDeviceList(int vendor,int device_model,String hard_version,
			String soft_version,String bootadv, int curPage_splitPage,
			int num_splitPage, String startTime, String endTime) 
	{
		logger.debug("querystbDeviceList({},{},{},{},{},{})", new Object[] { vendor,
				device_model, hard_version, soft_version });
		
		List<Map> list = dao.querystbDeviceList(vendor, device_model, hard_version,
				soft_version,bootadv, curPage_splitPage, num_splitPage,
				startTime, endTime);
		stbmaxPage_splitPage = dao.getstbDeviceListCount(vendor, device_model, hard_version,
				soft_version,bootadv, curPage_splitPage, num_splitPage,startTime, endTime);
		return list;
	}

	public boolean canstbDeleteDevice(int deleteID) 
	{
		logger.debug("canstbDeleteDevice({})", new Object[] { deleteID });
		int num = dao.getstbDeviceListCount(deleteID);
		// 有相关的设备，不可删除
		boolean result = num > 0 ? false : true;
		logger.debug("canstbDeleteDevice: " + result);
		return result;
	}

	public void deletestbDevice(String gw_type, int deleteID)
	{
		logger.debug("deletestbDevice({})", new Object[] { deleteID });
		dao.deletestbDevice(deleteID);
		// 通知ACS
		sendToACS(gw_type,0, 2, deleteID);
	}

	public List<Map> querystbDeviceDetail(long deviceTypeId, long detailSpecId) 
	{
		return dao.querystbDeviceDetail(deviceTypeId,detailSpecId);
	}

	public String addstbDevTypeInfo(String gw_type,int vendor, int device_model, String specversion,
			String hard_version, String soft_version, int is_check, int rela_dev_type,
			String typeId, String portInfo, String servertype, long acc_oid,String zeroconf,String bootadv)
	{
		String msg = "";
		Object[] temp = dao.addstbDevTypeInfo(vendor, device_model, specversion,
				hard_version, soft_version, is_check, rela_dev_type, typeId,zeroconf,bootadv);
		long id = Long.valueOf(temp[1].toString());
		int result = Integer.valueOf(temp[0].toString());
		// 通知ACS
		sendToACS(gw_type,1, 2, id);
		if (result != 0){
			msg = "设备版本入库成功";
		}else{
			msg = "设备版本入库失败";
		}
		return msg;
	}

	public String updatestbDevTypeInfo(String gw_type,long deviceTypeId, int vendor, int device_model,
			String specversion, String hard_version, String soft_version, int is_check,
			int rela_dev_type, String typeId, String portInfo, String servertype,
			long acc_oid,String ipType,String isNormal,long spec_id,String zeroconf,String bootadv,
			int category,int is_probe)
	{
		String msg = "设备版本信息更新失败";
		int res = dao.updatestbDevTypeInfo(deviceTypeId, vendor, device_model, specversion,
				hard_version, soft_version, is_check, rela_dev_type, typeId,ipType,isNormal,spec_id,
				zeroconf,bootadv,category,is_probe);
		// 通知ACS
		sendToACS(gw_type,1, 2, deviceTypeId);
		if (res != 0){
			msg = "设备版本信息更新成功";
		}
		
		return msg;
	}
	
	public String updatestbDevTypeInfo(String gw_type,long deviceTypeId, int vendor, int device_model,
			String specversion, String hard_version, String soft_version, int is_check,
			int rela_dev_type, String typeId, String portInfo, String servertype,
			long acc_oid,String ipType,String isNormal,long spec_id,String zeroconf,String bootadv,
			int category)
	{
		String msg = "设备版本信息更新失败";
		int res = dao.updatestbDevTypeInfo(deviceTypeId, vendor, device_model, specversion,
				hard_version, soft_version, is_check, rela_dev_type, typeId,ipType,isNormal,spec_id,
				zeroconf,bootadv,category);
//		int result = dao.updatestbPortAddServertype(deviceTypeId, portInfo, acc_oid,
//				servertype);
		// 通知ACS
		sendToACS(gw_type,1, 2, deviceTypeId);
		if (res != 0){
			msg = "设备版本信息更新成功";
		}
		
		return msg;
	}
	
	/**
	 * 修改版本型号EPG数据，湖南联通特有
	 */
	public String updatestbDevTypeInfo(long deviceTypeId,String epg_version,String net_type)
	{
		String msg = "设备版本信息更新失败";
		int res = dao.updatestbDevTypeInfo(deviceTypeId,epg_version,net_type);
		if (res != 0){
			msg = "设备版本信息更新成功";
		}
		
		return msg;
	}

}
