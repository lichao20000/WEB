package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.stb.resource.dao.StbGwDeviceQueryDAO;

/**
 * 
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-1-30
 */
public class StbGwDeviceQueryBIO
{
	/** 下发查询用dao */
	private StbGwDeviceQueryDAO dao;

	public StbGwDeviceQueryDAO getDao() {
		return dao;
	}

	public void setDao(StbGwDeviceQueryDAO dao) {
		this.dao = dao;
	}

	/**
	 * 获取总数
	 */
	public int getQueryCount()
	{
		return dao.getQueryCount();
	}

	/**
	 * 获取列表
	 */
	public List<Map<String, String>> getStbDeviceList(int curPage_splitPage,
			int num_splitPage, String vendorId, String deviceModelId,
			String deviceSn, String deviceMac, String servAccount,
			String cityId, String citynext, String startTime, String endTime) 
	{
		return dao.getStbDeviceList(curPage_splitPage, num_splitPage, vendorId,
				deviceModelId, deviceSn, deviceMac, servAccount, cityId, citynext, startTime, endTime);
	}

	/**
	 * 获取总数并分页
	 */
	public int countStbDeviceList(int curPage_splitPage, int num_splitPage,
			String vendorId, String deviceModelId, String deviceSn,
			String deviceMac, String servAccount, String cityId, 
			String citynext, String startTime, String endTime) 
	{
		return dao.countStbDeviceList(curPage_splitPage, num_splitPage, vendorId,
				deviceModelId, deviceSn, deviceMac, servAccount, cityId, citynext, startTime, endTime);
	}
	
	public List<Map<String, String>> getStbDeviceList_hnlt(int curPage_splitPage,
			int num_splitPage, String vendorId, String deviceModelId,
			String deviceSn, String deviceMac, String servAccount, String cityId, String citynext,
			String startTime, String endTime, String lastStartTime, String lastEndTime) 
	{
		return dao.getStbDeviceList_hnlt(curPage_splitPage, num_splitPage, vendorId,
				deviceModelId, deviceSn, deviceMac, servAccount, cityId, citynext, startTime, endTime,lastStartTime,  lastEndTime);
	}
	
	public int countStbDeviceList_hnlt(int curPage_splitPage, int num_splitPage,
			String vendorId, String deviceModelId, String deviceSn,
			String deviceMac, String servAccount, String cityId, String citynext,
			String startTime, String endTime, String lastStartTime, String lastEndTime) 
	{
		return dao.countStbDeviceList_hnlt(curPage_splitPage, num_splitPage, vendorId,
				deviceModelId, deviceSn, deviceMac, servAccount, cityId, citynext, startTime, endTime,lastStartTime,  lastEndTime);
	}

	/**
	 * 新增设备
	 */
	public String addGwDevice(String deviceSn, String oui, String deviceMac,
			String vendorId, String deviceModelId, String deviceTypeId,
			String cityId, String completeTime, String citynext)
	{
		int ier = dao.addGwDevice(deviceSn, oui, deviceMac, vendorId, deviceModelId, deviceTypeId, cityId, completeTime, citynext);
		// 1：成功 2：设备序列号或mac地址已存在 0：新增失败
		if (ier == 2) {
			return "2";
		}else if (ier == 1) {
			return "1";
		}
		return "0";
	}
	
	public String addGwDevice_hn(long id,String ip,String deviceSn, String oui, String deviceMac,
			String vendorId, String deviceModelId, String deviceTypeId,
			String cityId, String completeTime, String citynext)
	{
		int ier = dao.addGwDevice_hn(id,ip,deviceSn, oui, deviceMac, vendorId, deviceModelId, deviceTypeId, cityId, completeTime, citynext);
		// 1：成功 2：设备序列号或mac地址已存在 0：新增失败
		if (ier == 2) {
			return "2";
		}else if (ier == 1) {
			return "1";
		}
		return "0";
	}

	/**
	 * 删除设备
	 */
	public String deleteDevice(String deviceId, String deviceMac) 
	{
		int ier = dao.deleteDevice(deviceId, deviceMac);
		// 1：成功 2：mac地址在用户表中存在，不能删除 0：删除失败
		if (ier == 2) {
			return "2";
		}else if (ier >= 1) {
			return "1";
		}
		return "0";
	}
	
	public String deleteDevice_hn(long id,String ip,String deviceId, String deviceMac) 
	{
		int ier = dao.deleteDevice_hn(id,ip,deviceId, deviceMac);
		// 1：成功 2：mac地址在用户表中存在，不能删除 0：删除失败
		if (ier == 2) {
			return "2";
		}else if (ier >= 1) {
			return "1";
		}
		return "0";
	}

	/**
	 * 修改设备
	 */
	public String modifyDevice(String deviceId, String deviceSn, String oui,
			String deviceMac, String vendorId, String deviceModelId,
			String deviceTypeId, String cityId, String citynext) 
	{
		int ier = dao.modifyDevice(deviceId,deviceSn, oui, deviceMac, vendorId, deviceModelId, deviceTypeId, cityId, citynext);
		// 1：成功 2：mac地址在用户表中存在，不能删除 0：删除失败
		if (ier == 2) {
			return "2";
		}else if (ier >= 1) {
			return "1";
		}
		return "0";
	}
	
	public String modifyDevice_hn(long id,String ip,String deviceId, String deviceSn, String oui,
			String deviceMac, String vendorId, String deviceModelId,
			String deviceTypeId, String cityId, String citynext,int category) 
	{
		int ier = dao.modifyDevice_hn(id,ip,deviceId,deviceSn, oui, deviceMac, vendorId, deviceModelId, deviceTypeId, cityId, citynext, category);
		// 1：成功 2：mac地址在用户表中存在，不能删除 0：删除失败
		if (ier == 2) {
			return "2";
		}else if (ier >= 1) {
			return "1";
		}
		return "0";
	}

	public Map<String, String> queryDeviceById(String deviceId) 
	{
		return dao.queryDeviceById(deviceId);
	}

}
