package com.linkage.module.gtms.config.dao;

import java.util.List;

public interface ControlTimeOfUserDao {

	public int  addRecord(String typeId, String conTime1);
	public int updateRecord (String cuId,String typeId,String conTime1);
	public List getRecordById(int cuId);
	public int deleteRecord(String cuId);
	public List getAllRecord(String typeId,String conTimeStart,String conTimeEnd);
}
