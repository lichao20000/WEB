package com.linkage.module.gtms.config.serv;

import java.util.List;
import java.util.Map;

public interface SetLoidServ {

	public String doConfig(String cityId, long userId,
			List<Map<String, String>> list);

	public List queryDeviceDetail(String loid, String deviceNumber,
			String startTime, String endTime, String statu,
			int curPage_splitPage, int num_splitPage);

	public List queryDeviceDetail(String loid, String deviceNumber,
			String startTime, String endTime,String statu);

	public int getCount(String loid, String deviceNumber, String startTime,
			String endTime, String statu, int curPage_splitPage,
			int num_splitPage);

}
