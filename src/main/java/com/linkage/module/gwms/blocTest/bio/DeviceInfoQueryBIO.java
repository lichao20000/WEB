
package com.linkage.module.gwms.blocTest.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.blocTest.dao.DeviceInfoQueryDAO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-7-21 下午04:36:55
 * @category resource.bio
 * @copyright 南京联创科技 网管科技部
 */
public class DeviceInfoQueryBIO
{

	private DeviceInfoQueryDAO dao;
	private int maxPage_splitPage;
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(DeviceInfoQueryBIO.class);

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
	public List<Map> queryDeviceList(String username, String user, String telephone,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryDeviceList({},{},{},{},{},{})", new Object[] { username, user,
				telephone });
		List<Map> list = dao.queryDeviceList(username, user, telephone,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = dao.getDeviceListCount(username, user, telephone,
				curPage_splitPage, num_splitPage);
		return list;
	}

	public List getHgwcustServInfo(String username, String user, String telephone)
	{
		return dao.getHgwcustServInfo(username, user, telephone);
	}

	// 查询设备版本配置信息
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
	public List<Map> queryList(int vendor, int device_model, String hard_version,
			String soft_version, int is_check, int rela_dev_type, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("queryDeviceList({},{},{},{},{},{})", new Object[] { vendor,
				device_model, hard_version, soft_version, is_check, rela_dev_type });
		List<Map> list = dao.queryList(vendor, device_model, hard_version, soft_version,
				is_check, rela_dev_type, curPage_splitPage, num_splitPage);
		maxPage_splitPage = dao.getListCount(vendor, device_model, hard_version,
				soft_version, is_check, rela_dev_type, curPage_splitPage, num_splitPage);
		return list;
	}

	public Map queryConfig(String devicetype_id)
	{
		return dao.queryConfig(devicetype_id);
	}

	public List<Map> queryDeviceTypeConfigList(int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryDeviceTypeConfigList()", new Object[] {});
		List<Map> list = dao.queryDeviceTypeConfigList(curPage_splitPage, num_splitPage);
		maxPage_splitPage = dao.getDeviceTypeConfigListCount(curPage_splitPage,
				num_splitPage);
		return list;
	}

	// 添加设备版本配置信息
	public int addDeviceTypeConfig(String devicetype_id, int accessType, int deviceType,
			int isCardApart, String wan_name, int wan_num, String wan_can,
			String lan_name, int lan_num, String lan_can, String wlan_name, int wlan_num,
			String wlan_can, String voip_name, int voip_num, String voip_can,
			int voipProtocol, int wirelessType, int wireless_num, int wireless_size)
	{
		return dao.addDeviceTypeConfig(devicetype_id, accessType, deviceType,
				isCardApart, wan_name, wan_num, wan_can, lan_name, lan_num, lan_can,
				wlan_name, wlan_num, wlan_can, voip_name, voip_num, voip_can,
				voipProtocol, wirelessType, wireless_num, wireless_size);
	}

	public DeviceInfoQueryDAO getDao()
	{
		return dao;
	}

	public void setDao(DeviceInfoQueryDAO dao)
	{
		this.dao = dao;
	}

	public int getMaxPage_splitPage()
	{
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPageSplitPage)
	{
		maxPage_splitPage = maxPageSplitPage;
	}
}
