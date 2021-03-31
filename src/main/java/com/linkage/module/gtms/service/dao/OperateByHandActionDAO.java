
package com.linkage.module.gtms.service.dao;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-14
 * @category com.linkage.module.gtms.service.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public interface OperateByHandActionDAO
{

	public List<Map> getOperateByHandInfo(long accOid,String starttime, String endtime,
			String city_id, String servTypeId, String username, String resultType,
			int curPage_splitPage, int num_splitPage);

	public int countOperateByHandInfo(long accOid,String starttime, String endtime, String city_id,
			String servTypeId, String username, String resultType, int curPage_splitPage,
			int num_splitPage);

	public int countOperateByHandInfoExcel(String starttime, String endtime,
			String city_id, String servTypeId, String username, String resultType);

	public List<Map> getOperateByHandInfoExcel(String starttime, String endtime,
			String city_id, String servTypeId, String username, String resultType);
}
