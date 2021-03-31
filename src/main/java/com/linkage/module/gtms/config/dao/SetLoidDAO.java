package com.linkage.module.gtms.config.dao;

import java.util.ArrayList;
import java.util.List;

public interface SetLoidDAO {

	public void addSetLoidTask(Long taskId, String cityId, long userId);

	public String getDeviceId(String oui, String device_serialnumber);

	public List queryDeviceDetail(String loid, String deviceNumber,
			String startTime, String endTime, String statu,
			int curPage_splitPage, int num_splitPage);

	public List queryDeviceDetail(String loid, String deviceNumber,
			String startTime, String endTime, String statu);

	public int getCount(String loid, String deviceNumber, String startTime,
			String endTime, String statu, int curPage_splitPage,
			int num_splitPage);

	public List getDeviceList(String deviceStrs);

	public void doInsertDevices(ArrayList<String> sqllist);

}
