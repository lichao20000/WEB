package com.linkage.module.gtms.config.serv;

public interface AlarmConfigServ {
	public String doAlarmConfig(String[] deviceIds, String serviceId,
			String[] paramArr, String gwType);

	public String getAccSQL(String tableName, String gwType);

	public String getDevSQL(String gwType);
}
