/**
 * 
 */

package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.itms.resource.dao.DeviceTypeInfoExpDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;

/**
 * @author
 */
public class DeviceTypeInfoExpBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DeviceTypeInfoExpBIO.class);
	private DeviceTypeInfoExpDAO dao;
	private int maxPage_splitPage;

	public int getMaxPage_splitPage()
	{
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage)
	{
		this.maxPage_splitPage = maxPage_splitPage;
	}
	
	
	public String batchImportDevice(List elsData)
	{
		return dao.batchImportDevice(elsData);
	}
	
	
	/**
	 * @param vendor
	 *            厂商
	 * @param device_model
	 *            设备型号
	 * @param hard_version
	 *            硬件型号
	 * @param soft_version
	 *            软件型号
	 * @param is_check
	 *            是否审核
	 * @param rela_dev_type
	 *            设备类型
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return 查询的所有数据
	 */
	public List<Map> queryDeviceList(int vendor, int device_model, String hard_version,
			String soft_version, int is_check, int rela_dev_type, int curPage_splitPage,
			int num_splitPage, String startTime, String endTime,int access_style_relay_id,int spec_id)
	{
		logger.debug("queryDeviceList({},{},{},{},{},{})", new Object[] { vendor,
				device_model, hard_version, soft_version, is_check, rela_dev_type });
		List<Map> list = dao.queryDeviceList(vendor, device_model, hard_version,
				soft_version, is_check, rela_dev_type, curPage_splitPage, num_splitPage,
				startTime, endTime,access_style_relay_id,spec_id);
		//String specName = "";
//		for (Map map : list)
//		{
//			specName = dao.getSpecName((String) map.get("spec_id"));
//			map.put("specName", specName);
//		}
		maxPage_splitPage = dao.getDeviceListCount(vendor, device_model, hard_version,
				soft_version, is_check, rela_dev_type, curPage_splitPage, num_splitPage,
				startTime, endTime,access_style_relay_id,spec_id);
		return list;
	}
	
	public List<Map<String,String>> queryExeclDeviceList(int vendor, int device_model, String hard_version,
			String soft_version, int is_check, int rela_dev_type,int access_style_relay_id,int spec_id)
	{
		List<Map<String,String>> list = dao.queryExeclDeviceList(vendor, device_model, hard_version,
				soft_version, is_check, rela_dev_type,access_style_relay_id,spec_id);
		List<Map<String,String>> tempList = new ArrayList<Map<String,String>>();
		logger.warn("list="+list);
		for (Map<String,String> map : list)
		{
			Map tempMap = new HashMap();
			tempMap.put("vendor", DeviceTypeUtil.vendorMap.get(map.get("vendor_id")));
			tempMap.put("device_model", DeviceTypeUtil.deviceModelMap.get(map.get("device_model_id")));
			tempMap.put("specversion", StringUtil.getStringValue(map.get("specversion")));
			tempMap.put("hard_version", map.get("hardwareversion"));
			tempMap.put("soft_version", map.get("softwareversion"));
			tempMap.put("is_check", DeviceTypeUtil.checkMap.get(StringUtil.getStringValue(map.get("is_check"))));
			tempMap.put("rela_dev_type", DeviceTypeUtil.devTypeMap.get(StringUtil.getStringValue(map.get("rela_dev_type_id"))));
			tempMap.put("access_style_relay_id", DeviceTypeUtil.accessMap.get(StringUtil.getStringValue(map.get("access_style_relay_id"))));
			tempMap.put("spec_name", map.get("spec_name"));
			tempList.add(tempMap);
		}
		
		return tempList;
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
	 * @param vendor
	 * @return 查询端口和协议信息编辑用
	 */
	public String getPortAndType(long deviceTypeId)
	{
		return dao.getPortAndType(deviceTypeId);
	}

	/**
	 * 根据id删除记录
	 * 
	 * @param id
	 */
	public void deleteDevice(String gw_type,int id)
	{
		logger.debug("deleteDevice({})", new Object[] { id });
		dao.deleteDevice(id);
	}

	/**
	 * 查看当前设备版本是否可以删除，如果版本下面没有设备即可删除
	 * 
	 * @param id
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
	 * 
	 * @param vendor
	 *            厂商
	 * @param device_model
	 *            设备型号
	 * @param specversion
	 *            特定版本
	 * @param hard_version
	 *            硬件型号
	 * @param soft_version
	 *            软件型号
	 * @param is_check
	 *            是否审核
	 * @param rela_dev_type
	 *            设备类型
	 * @return 操作结果
	 */
	public String addDevTypeInfo(String gw_type,int vendor, int device_model, String specversion,
			String hard_version, String soft_version, int is_check, int rela_dev_type,
			String typeId, String portInfo, String servertype, long acc_oid,String ipType,long spec_id)
	{
		String msg = "";
		Object[] temp = dao.addDevTypeInfo(vendor, device_model, specversion,
				hard_version, soft_version, is_check, rela_dev_type, typeId,ipType,spec_id);
		int a = dao.addPortAddServertype(portInfo, acc_oid, servertype);
		long id = Long.valueOf(temp[1].toString());
		int result = Integer.valueOf(temp[0].toString());
		// 通知ACS
		//sendToACS(gw_type,1, 2, id);
		if (result != 0)
		{
			msg = "设备版本入库成功";
		}
		else
		{
			msg = "设备版本入库失败";
		}
		return msg;
	}

	/**
	 * 修改设备版本
	 * 
	 * @param deviceTypeId
	 *            ID
	 * @param vendor
	 *            厂商
	 * @param device_model
	 *            设备型号
	 * @param specversion
	 *            特定版本
	 * @param hard_version
	 *            硬件型号
	 * @param soft_version
	 *            软件型号
	 * @param is_check
	 *            是否审核
	 * @param rela_dev_type
	 *            设备类型
	 * @return 操作结果
	 */
	public String updateDevTypeInfo(String gw_type,long deviceTypeId, int vendor, int device_model,
			String specversion, String hard_version, String soft_version, int is_check,
			int rela_dev_type, String typeId, String portInfo, String servertype,
			long acc_oid,String ipType,String isNormal,long spec_id)
	{
		String msg = "";
		int res = dao.updateDevTypeInfo(deviceTypeId, vendor, device_model, specversion,
				hard_version, soft_version, is_check, rela_dev_type, typeId,ipType,isNormal,spec_id);
		int result = dao.updatePortAddServertype(deviceTypeId, portInfo, acc_oid,
				servertype);
		// 通知ACS
		//sendToACS(gw_type,1, 2, deviceTypeId);
		if (res != 0)
		{
			msg = "设备版本信息更新成功";
		}
		else
		{
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
	 * @param cursor
	 *            数据源
	 * @param name
	 *            用于生成下拉框时的value值
	 * @param cast
	 *            用于生成下拉框时的展示值(呈现给用户看的)
	 * @param pos
	 *            初始化下拉框的初始值
	 * @param hasDefault
	 * @return 生成下拉框的html代码后返回.
	 */
	public static String createListBox(Cursor cursor, String name, String cast,
			String pos, boolean hasDefault)
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
	public DeviceTypeInfoExpDAO getDao()
	{
		return dao;
	}

	public void setDao(DeviceTypeInfoExpDAO dao)
	{
		this.dao = dao;
	}

	public int isNormalVersion(int deviceModel) {
		return dao.isNormalVersion(deviceModel);
	}
}
