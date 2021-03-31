package com.linkage.module.gtms.config.serv;

import java.util.List;
import java.util.Map;

public interface ControlTimeOfUserServ {

	/**
	 * 增加一条记录
	 * (non-Javadoc)
	 * @return int 是否增加成功
	 */
	public int  addRecord(String typId, String conTime1);
	/**
	 * 更新一条记录
	 * (non-Javadoc)
	 * @return int 是否更新成功
	 */
	public int updateRecord(String cuId,String typeId,String conTime1);
	/**
	 * 删除一条记录
	 * (non-Javadoc)
	 * @return int 是否删除成功
	 */
	public int deleteRecord(String cuId);
	/**
	 * 根据时间查询记录
	 * @param conTimeStart
	 * @param conTimeEnd
	 * @return
	 */
	public List<Map<String,Object>> getRecordByTime(String typeId,String conTimeStart,String conTimeEnd,String flag);
	/**
	 * 根据Id查询记录
	 * @param cuId
	 * @return
	 */
	public Map<String, Object> getRecordById(String cuId);
}
