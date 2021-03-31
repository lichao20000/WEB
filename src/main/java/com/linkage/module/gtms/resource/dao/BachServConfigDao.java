
package com.linkage.module.gtms.resource.dao;

import java.util.Map;

import com.linkage.module.gtms.resource.obj.BachServObj;

/**
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since 2014-4-11
 * @category com.linkage.module.gtms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public interface BachServConfigDao
{

	public int updateNetServInfo(BachServObj objs, String gw_type);

	public int updateItvServInfo(BachServObj objs, String gw_type);

	public Map<String, String> queryUserId(String loid, String gw_type);

	public int getSipId(BachServObj objs);

	public int updateVoipServInfo(BachServObj objs, String gw_type);

	public int updateVoipParam(BachServObj objs, String gw_type, int sipId);

	public int updateNetServParam(BachServObj objs);
}
